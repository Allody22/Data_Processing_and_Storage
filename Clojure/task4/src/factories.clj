(ns factories)

(declare supply-msg)
(declare notify-msg)

(defn producer
  "Тут будет появляться из воздуха какой-то изначальный ресурс необходимый для дальнейших действий.
  Запускаем поток, который бесконечно после заданного sleep-time делает заданное кол-во ресурса для заданного target-storage"
  [amount-in-a-time sleep-time target-storage]
  (println "Производитель одного" amount-in-a-time "раз в " sleep-time "для" (:resource-name target-storage))
  (new Thread
       (fn []
         (Thread/sleep sleep-time)
         ;; Отправляем сообщение о том что ресурс произведён для того чтобы на нашем заводе шла работа
         (send (target-storage :worker) supply-msg amount-in-a-time)
         (recur))))


(defn storage
  "А это хранилище какого-то ресурса с информацией куда этот ресурс может дальше идти (потребители)"
  [name notify-step & consumers]
  (println "Создано хранилище для" name)
  (let [current-counter (atom 0 :validator #(>= % 0))       ;; Атом для отслеживания текущего количества ресурсов в хранилище
        total-produced-counter (atom 0 :validator #(>= % 0)) ;; Атом для отслеживания всего когда-либо произведённого количества ресурсов
        ;; Вся информация о хранилище, которая нам пригодится
        worker-state {:resource-counter       current-counter,
                      :total-produced-counter total-produced-counter,
                      :resource-name          name,
                      :notify-step            notify-step,
                      :consumers              consumers}]
    {:resource-counter       current-counter,
     :total-produced-counter total-produced-counter,
     :resource-name          name,
     :worker                 (agent worker-state)}))


(defn factory
  "Ну это фабрика (агент), которая используется для промежуточного/конечного производства чего-то еще, то есть она берёт что-то уже созданное и делает из этого что-то новое"
  [amount-in-a-time sleep-time target-storage & ware-amounts]
  ;; Это тоже скорее в целях логов.
  (println "Создана фабрика, производящая" amount-in-a-time "за" sleep-time "для" (:resource-name target-storage))
  ;; Создаем словарь `bill`, содержащий информацию о необходимых ресурсах для производства.
  (let [required-resources (apply hash-map ware-amounts),
        ;; Ну в обычной джаве я бы сделал это каким-то фориком, но тут нашёл такой способ.
        ;; keys извлекает список ключей из required-resources, а repeat 0 создает бесконечный поток нулей.
        ;; И главное - zipmap соединяет эти два списка в словарь, где каждому ключу соответствует значение 0.
        current-resources (zipmap (keys required-resources) (repeat 0)),
        ;; Вся инфа фабрики, которая нам понадобится для работы.
        worker-state {:amount             amount-in-a-time,
                      :duration           sleep-time,
                      :target-storage     target-storage,
                      :required-resources required-resources,
                      :current-resources  current-resources}]
    ;; Возвращаем словарь, содержащий агента фабрики.
    {:worker (agent worker-state)}))


(defn supply-msg
  "Тут наверное происходит топ 2 часть по важности. Мы увеличиваем общее и текущее кол-во ресурса на переданный amount,
  затем проверяем условие на превышение порога для отправки лога в консоль и идём по всем потребителям этого хранилища"
  [state amount]
  (swap! (state :resource-counter) #(+ % amount))
  (swap! (state :total-produced-counter) #(+ % amount))
  (let [ware (state :resource-name)
        total-counter @(state :total-produced-counter),
        notify-step (state :notify-step),
        consumers (state :consumers)]
    ;; Проверяем, нужно ли отправлять уведомление в консоль.
    (when (and (> (int (/ total-counter notify-step))       ;; Текущее количество достигает шага уведомления.
                  (int (/ (- total-counter amount) notify-step)))) ;; И это новое достижение шага (после добавления ресурса).
      (println "Ресурса" ware "всего сделано:" total-counter " на момент" (.format (new java.text.SimpleDateFormat "hh.mm.ss.SSS") (new java.util.Date))))
    ;; У финальных продуктов нет потребителей, так что надо отследить это
    (when consumers
      ;; Перебирает всех потребителей
      (doseq [consumer (shuffle consumers)]
        ;; Отправляет каждому потребителю сообщение notify-msg
        (send (consumer :worker) notify-msg ware (state :resource-counter) amount)))
    state))

(defn notify-msg
  "А это наверное самая важная функция и это спам рассылка всех на заводе, чтобы сообщить о новом произведённом ресурсе и при возможности сделать из него дальше ресурс."
  [state ware storage-atom amount]
  (let [
        ;; Получаем информацию о необходимых и текущих ресурсах для производства.
        required-resources (state :required-resources)
        current-resources (state :current-resources)
        ;; Определяем, сколько ресурса необходимо изъять из хранилища.
        needed-resources (min (- (required-resources ware) (current-resources ware)) amount)
        ;; Проверяем, достаточно ли ресурсов в хранилище для начала производства.
        new-resources
        (if (>= (required-resources ware) (current-resources ware))
          ;; Если достаточно, извлекаем необходимое количество ресурсов и обновляем буфер.
          (try
            (do
              ;; Уменьшаем количество ресурса в хранилище на требуемое количество.
              (swap! storage-atom #(- % needed-resources))
              ;; Добавляем ресурс на фабрику.
              (update current-resources ware #(+ % needed-resources)))
            ;; Обработка возможных исключений при изменении состояния.
            (catch IllegalStateException _ current-resources))
          ;; Если ресурсов недостаточно, то ничего не меняем.
          current-resources)
        ]
    ;; Проверяем, хватает ли ресурсов.
    (if (= required-resources new-resources)
      (do
        (Thread/sleep (state :duration))
        ;; Отправляем сообщение о новой произведенной партии.
        (send ((state :target-storage) :worker) supply-msg (state :amount))
        ;; Сбрасываем информацию о ресурсы в нуль на фабрике после производства.
        (assoc state :current-resources (zipmap (keys required-resources) (repeat 0))))
      ;; Если условие для начала производства не выполнено, сохраняем текущее состояние.
      (assoc state :current-resources new-resources))))

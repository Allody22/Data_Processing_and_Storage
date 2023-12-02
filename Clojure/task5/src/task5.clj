(ns task5)

; Те самые параметры из условия, которые можно легко изменить
(def philosophers-number 4)
(def max-thinking-length 3000)
(def max-dining-length 3000)
(def dining-periods-number 5)

(def forks (take philosophers-number (repeatedly #(ref {:transactional-count 0, :casual-in-use (atom 0)}))))
(def transaction-restarts (atom 0 :validator #(>= % 0)))

(defn philosopher
  "Функция, отвечающая за действия философа"
  [number left-fork left-fork-id right-fork right-fork-id]
  (dotimes [iteration dining-periods-number]
    (do
      (println (str "Философ " number " думает"))
      (Thread/sleep (rand-int (inc max-thinking-length)))
      (dosync
        (swap! transaction-restarts inc)
        (alter left-fork update :transactional-count inc)
        (swap! (:casual-in-use @left-fork) inc)
        (println (str "Философ " number " поднял вилку с айди " left-fork-id))
        (alter right-fork update :transactional-count inc)
        (swap! (:casual-in-use @right-fork) inc)
        (println (str "Философ " number " поднял вилку с айди " right-fork-id))
        (Thread/sleep (rand-int (inc max-dining-length)))
        (println (str "Философ " number " поел уже " (+ 1 iteration) " раз"))
        (swap! transaction-restarts dec)))))

;А тут мы занимаемся распределением вилок
(def philosophers
  (map
    #(new Thread
          (fn []
            (if (= % (dec philosophers-number))
              (philosopher % (nth forks (mod (inc %) philosophers-number)) (mod (inc %) philosophers-number) (nth forks %) %)
              (philosopher % (nth forks %) % (nth forks (mod (inc %) philosophers-number)) (mod (inc %) philosophers-number)))))
    (range philosophers-number)))

;Смотрим сколько раз была использована какая вилка
(defn print-forks-stats []
  (let [total-casual-use (reduce (fn [sum [idx fork-ref]]
                                   (let [fork @fork-ref]
                                     (println (str "Вилка " idx ":"
                                                   "\n\tИспользована без транзакции: " @(:casual-in-use fork)
                                                   "\n\tИспользована в транзакции: " (:transactional-count fork)))
                                     (+ sum @(:casual-in-use fork))))
                                 0
                                 (map-indexed vector forks))]
    (println (str "\nВсего использовано вилок без транзакций: " total-casual-use))))



(defn dining-philosophers
  "Функция для запуска всей нашей философии и её обеда"
  []
  (print "Запускаем философов")
    (doall (map #(.start %) philosophers))
    (doall (map #(.join %) philosophers))
    (print-forks-stats)
    (println "\nРестартов транзакции: " @transaction-restarts))

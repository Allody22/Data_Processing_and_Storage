(ns task3)

(defn async-filter-chunk [pred chunk]
  "Функция для создания future, фильтрующая каждый набор данных"
  (future (doall (filter pred chunk))))

(defn lazy-combine-futures [processed-results remaining-futures pred]
  "Функция для ленивой обработки результатов futures"
  (if-let [remaining (seq remaining-futures)]
    ;; Если есть еще futures для обработки, продолжаем лениво обрабатывать их.
    (lazy-seq (lazy-cat (deref (first processed-results))
                        (lazy-combine-futures (rest processed-results) (rest remaining) pred)))
    ;; Если futures больше нет, объединяем результаты.
    (apply concat (map deref processed-results))))


(defn pfilter
  "Реализация главной функции. Принимаем предикат и коллекцию и делаем свой параллельный фильтр."
  ([pred coll]
   ;; Получаем количество доступных процессоров для определения уровня параллелизма.
   (let [n (.availableProcessors (Runtime/getRuntime))
         ;; Устанавливаем размер чанка для обработки.
         chunk-size 60
         ;; Разбиваем коллекцию на чанки.
         parts (map doall (partition-all chunk-size coll))
         ;; Создаем пул futures.
         pool (map #(async-filter-chunk pred %) parts)]
     ;; Начинаем выполнение основной логики
     (lazy-combine-futures pool (drop n pool) pred))))

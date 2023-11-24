(ns tests
  (:require [clojure.test :refer :all]
            [task3 :refer [async-filter-chunk lazy-combine-futures pfilter]]))

(deftest test-pfilter-basic
  (testing "Базовые функции и их сравнение с библиотечной реализацией"
    (is (= (pfilter even? [-6 -5 -4 -3 -2 1 2 3 4 5 6])
           (filter even? [-6 -4 -2 1 2 3 4 5 6]))
        "Просто чётные числа от -6 до 6")
    (is (= (pfilter odd? [])
           (filter odd? []))
        "Пустой массив")
    (is (= (pfilter #(> % 5) (range 10))
           (filter #(> % 5) (range 10)))
        "Числа больше 5 до 10")))

(deftest test-pfilter-basic-big
  (testing "Проверяем что моя функция и библиотечная правильно оставляют только чётные числа до 100"
    (is (= (pfilter even? (range 500))
           (filter even? (range 500)))
        "Просто чётные числа до 500")
    (is (= (pfilter #(> % 5) (range -500 5))
           (filter #(> % 5) (range -1000 4)))
        "Числа больше 5 с условием в котором их нет")))

(deftest test-pfilter-performance-small
  (testing "Сравниваем скорость моего фильтра и библиотечного, оставляя только чётные числа"
    (println "Тест скорости на небольших данных")
    (let [test-data (range 500)]
      (println "Тестирования скорости обычного фильтра:")
      (time (doall (filter even? test-data)))
      (println "Тестирования скорости моего фильтра:")
      (time (doall (pfilter even? test-data))))))


(deftest test-pfilter-performance-big
  (testing "Сравниваем скорость моего фильтра и библиотечного, оставляя только кратные 17 числа"
    (println "Тест скорости на больших данных")
    (let [test-data (range 10000)]
      (println "Тестирования скорости обычного фильтра:")
      (time (doall (filter #(zero? (mod % 17)) test-data)))
      (println "Тестирования скорости моего фильтра:")
      (time (doall (pfilter #(zero? (mod % 17)) test-data))))))


(deftest test-pfilter-large-collection
  (testing "Сравниваем результаты моего фильтра и библиотечного на большой коллекции, оставляя только чётные числа"
    (let [large-coll (range 100000)]
      (is (= (pfilter even? large-coll)
             (filter even? large-coll))))))

(deftest test-pfilter-lazy
  (testing "Тесты на ленивость сравнения моего фильтра и библиотечному на бесконечной последовательности"
    (let [infinite-coll (range)
          result (pfilter even? infinite-coll)]
      (is (= (take 10 result)
             (take 10 (filter even? infinite-coll)))
          "Берём первые 10 четных элементов из бесконечной последовательности и проверяем что всё хорошо.
          Если бы моя функция неправильно работала, то этот тест бы выполнялся бесконечно или пока стек не кончится"))))

(deftest test-async-filter-chunk
  (testing "Проверяем что моя функция для фильтрации отдельного чанка работает правильно"
    (let [chunk (range 20)
          result (async-filter-chunk even? chunk)]
      ;; Используем deref для ожидания результата асинхронной задачи (future) и его получения.
      (is (= (doall (filter even? chunk)) (deref result))))))

(deftest test-lazy-combine-futures
  (testing "Тестируем правильность ленивой обработки и объединения результатов асинхронных задач в lazy-combine-futures."
    (let [chunks (partition-all 20 (range 100))             ;; Разбиваем диапазон чисел от 0 до 100 на чанки по 20 элементов.
          pool (map #(async-filter-chunk even? %) chunks)   ;; Для каждого чанка создаём future с фильтрацией четных чисел.
          result (lazy-combine-futures pool pool even?)]    ;; Объединяем результаты futures лениво.
      (is (= (doall (filter even? (range 100))) (doall result))))))

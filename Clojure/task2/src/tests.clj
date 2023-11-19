(ns tests
  (:require [clojure.test :refer :all]
            [task2 :refer :all]))

; Ну на всякий случай сделаем проверку того, что моя функция
; проверка деления без остатка работает правильно
(deftest test-divisible
  (is (true? ((divisible? 4) 2)))
  (is (false? ((divisible? 5) 2)))
  (is (true? ((divisible? 15) 3)))
  (is (false? ((divisible? 17) 4))))

; Функция для проверки, является ли число простым.
; Сделал просто чтобы мой алгоритм точно проверить
(defn prime? [n]
  (and (> n 1)
       (not-any? (divisible? n)
                 (take-while #(<= % (Math/sqrt n)) primes))))

; Проверка, что первые 10 чисел в последовательности моего алгоритма являются простыми
(deftest test-prime-numbers
    (doseq [p (take 10 primes)]
      (is (prime? p))))

; Проверка конкретных простых чисел
(deftest test-specific-primes
    (is (= (nth primes 0) 2))
    (is (= (nth primes 1) 3))
    (is (= (nth primes 2) 5))
    (is (= (nth primes 3) 7))
    ;Ну тут просто загуглил значения на позициях побольше
    (is (= (nth primes 303) 2003))
    (is (= (nth primes 504) 3613))
    )
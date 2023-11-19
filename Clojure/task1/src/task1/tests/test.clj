(ns task1.tests.test
  (:require [clojure.test :refer :all]
            [task1.main.task1 :as t]))

; Тест из условия для проверки создания всех комбинаций длиной 2 из алфавита ("a" "b" "c").
; Проверяет, что функция возвращает все уникальные комбинации без повторяющихся последовательных символов.
(deftest test-create-language
  (is (= (sort (t/create-language 2 '("a" "b" "c")))
         (sort '("ab" "ac" "ba" "bc" "ca" "cb")))))

; Проверяем работу функции с пустым алфавитом.
; Ожидается пустой список, так как из пустого алфавита нельзя создать слова.
(deftest test-empty-alphabet
  (is (= (t/create-language 2 '()) '())))

; Тест для проверки создания слов длиной 1 из алфавита ("a" "b" "c").
; Проверяет, что функция возвращает каждый символ алфавита как отдельное слово.
(deftest test-single-letter-words
  (is (= (sort (t/create-language 1 '("a" "b" "c")))
         (sort '("a" "b" "c")))))

; Проверяет, что функция is-correct-word возвращает true для слова без повторяющихся последовательных символов ("ab")
; и false для слова с повторяющимися последовательными символами ("aa").
(deftest test-is-correct-word
  (is (true? (t/is-correct-word "ab")))
  (is (false? (t/is-correct-word "aa"))))

; Проверяет, что функция concat-letter правильно добавляет букву "c" к каждому слову из списка '("a" "b"),
; формируя новые слова '("ac" "bc").
(deftest test-concat-letter
  (is (= (t/concat-letter '("a" "b") "c") '("ac" "bc"))))

; Проверяет, что функция extend-words правильно расширяет список слов ("a" "b"), добавляя каждую
; букву из алфавита ("a" "b" "c") и исключая слова с повторяющимися символами.
(deftest test-extend-words
  (is (= (sort (t/extend-words '("a" "b") '("a" "b" "c")))
         (sort '("ab" "ac" "ba" "bc")))))
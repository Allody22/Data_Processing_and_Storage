(ns task1.main.task1)

; Функция для проверки, что последние два символа слова не одинаковые.
; Это необходимо для соблюдения условия задачи о том что не повторяются последовательные символы.
(defn is-correct-word [word]
  (not= (last word) (nth word (- (count word) 2))))

; Функция для конкатенации каждого слова с буквой.
; Создает новые слова путем добавления буквы к каждому слову из списка.
(defn concat-letter [words letter]
  (map (fn [word] (str word letter)) words))

; Функция для расширения списка слов, добавляя к ним каждую букву алфавита.
; Фильтрует результат, удаляя слова с повторяющимися символами.
(defn extend-words [words alphabet]
  (filter
    is-correct-word
    (reduce
      (fn [extended-words letter] (concat extended-words (concat-letter words letter)))
      '()
      alphabet)))

; Главная функция для создания языка.
; Генерирует все возможные комбинации слов заданной длины из алфавита, исключая комбинации с повторяющимися символами.
(defn create-language [length alphabet]
  (if (> length 0)
    (reduce
      (fn [words _] (extend-words words alphabet))
      alphabet
      (range (- length 1)))))

(ns main-test2
  (:require [clojure.test :refer :all]
            [factories :refer :all]))

(def safe-storage (storage "Сейф" 1))
(def safe-factory (factory 2 3000 safe-storage "Какаши" 4 "Боря" 4))

(def clock-storage (storage "Часы" 1))
(def clock-factory (factory 1 1000 clock-storage "Саске" 1 "Какаши" 1 "Наруто" 1))

; Три Бори в три секунды
(def borya-storage (storage "Боря" 2 safe-factory))
(def borya-factory (factory 3 3000 borya-storage "Саске" 1 "Наруто" 12))

;Один Какаши в три секунды
(def kakashi-storage (storage "Какаши" 1 safe-factory clock-factory))
(def kakashi-factory (factory 1 3000 kakashi-storage "Саске" 4 "Наруто" 8))

;Три Наруто в секунду (теневое клонирование)
(def naruto-storage (storage "Наруто" 12 clock-factory borya-factory kakashi-factory))
(def naruto-mill (producer 4 1000 naruto-storage))

;Два Саске в пять секунд
(def saske-storage (storage "Саске" 4 kakashi-factory borya-factory clock-factory))
(def saske-mine (producer 2 5000 saske-storage))

(defn start []
  (.start saske-mine)
  (.start naruto-mill))

(deftest test-producers
  (start)
  ;За это врёмя мы получим 2*2 Саске и 4*10 ММФ
  ;На всякий случай такие тесты чтобы было понятно что я не обманщик
  (Thread/sleep 10100)
  (is (= @(saske-storage :total-produced-counter) 4))
  (is (= @(naruto-storage :total-produced-counter) 40))
  ;Ну а через время их в два раза больше будет
  (Thread/sleep 10100)
  (is (= @(saske-storage :total-produced-counter) 8))
  (is (= @(naruto-storage :total-produced-counter) 80))
  )

(deftest test-clock
  (start)
  ; Для часов надо одного Саске, Какаши и Наруто, а это как минимум (10 + 2) + 5 + 1 + 1 секунд, но ресурсы параллельно уходят и на Борю с сейфом,
  ; поэтому реально посчитать всё тут тяжело
  (Thread/sleep 19100)
  (is (< @(clock-storage :total-produced-counter) 1))
  (Thread/sleep 10000)
  (is (>= @(clock-storage :total-produced-counter) 0))
  (is (< @(clock-storage :total-produced-counter) 2))
  (println "По итогу кол-во: часов " @(clock-storage :total-produced-counter)))

(deftest test-safe-borya-and-saske
  (start)
  ; Для 1 сейфа нужно 4 Какаши и 4 Бори, то есть как минимум ((5+5+2)*4 + (5+3)*4) +3 = 83, а сейфы только 2 производятся, то есть минимум 190 сек,
  ; не считая другие затраты на часы
  (Thread/sleep 192100)
  (is (= @(safe-storage :total-produced-counter) 2))
  (is (>= @(borya-storage :total-produced-counter) 4))
  (is (>= @(kakashi-storage :total-produced-counter) 4))
  (println "По итогу кол-во: сейфы " @(safe-storage :total-produced-counter) ", боря" @(borya-storage :total-produced-counter)
           ",Какаши" @(kakashi-storage :total-produced-counter))
  )
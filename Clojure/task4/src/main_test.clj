(ns main-test
  (:require [clojure.test :refer :all]
            [factories :refer :all]))

(def safe-storage (storage "Сейф" 1))
(def safe-factory (factory 1 3000 safe-storage "Железо" 3 "Чертёж сейфа" 1))

(def clock-storage (storage "Часы" 1))
(def clock-factory (factory 1 2000 clock-storage "ММФ" 5))

(def metal-storage (storage "Железо" 2 safe-factory))
(def metal-factory (factory 1 1000 metal-storage "ФИТ" 5))

(def safe-plan-storage (storage "Чертёж сейфа" 1 safe-factory))
(def safe-plan-factory (factory 1 10000 safe-plan-storage "ФИТ" 10))

(def mmf-storage (storage "ММФ" 10 clock-factory))
(def mmf-mill (producer 5 4000 mmf-storage))

(def fit-storage (storage "ФИТ" 9 metal-factory safe-plan-factory))
(def fit-mine (producer 3 2000 fit-storage))

(defn start []
  (.start fit-mine)
  (.start mmf-mill))

(deftest test-producers
  (start)
  ;За это врёмя мы получим 3*4 ФИТ и 5*2 ММФ
  ;На всякий случай такие тесты чтобы было понятно что я не обманщик
  (Thread/sleep 8100)
  (is (= @(fit-storage :total-produced-counter) 12))
  (is (= @(mmf-storage :total-produced-counter) 10))
  (is (< @(mmf-storage :total-produced-counter) 15))
  (is (< @(fit-storage :total-produced-counter) 15))
  ;Ну а через время их в два раза больше будет
  (Thread/sleep 8100)
  (is (= @(fit-storage :total-produced-counter) 24))
  (is (= @(mmf-storage :total-produced-counter) 20))
  (is (< @(mmf-storage :total-produced-counter) 30))
  (is (< @(fit-storage :total-produced-counter) 30))
  )


(deftest test-clock
  (start)
  ; Раз в 4 секунды получаем 5 ММФ, а для одних часов нужно 5 ММФ и 2 секунды, то есть 6 сек. Теперь проверим
  (Thread/sleep 6100)
  (is (= @(clock-storage :total-produced-counter) 1))
  ; Тут уже не просто в два раза больше. Те две секунды пока делались часы уже успела сделаться половина из 5 ММФ,
  ; то есть для двух часов надо еще 2+2 времени
  (Thread/sleep 4100)
  (is (= @(clock-storage :total-produced-counter) 2))
  )

(deftest test-safe-metal-and-fit
  (start)
  ; Для 1 железа нужно 5 ФИТ, а каждые 3 ФИТ за 2 секунды, так что 5 ФИТ будет через 4 секунды.
  ; Плюс 1 секунда на производство железа, так что первое железо будет через 5 секунд.
  ; Для 1 чертежа сейфа нужно 10 ФИТ + 10 секунд на его создание, то есть 18 секунд
  ; Для одного сейфа требуется 3 железа плюс 3 секунды на сейф плюс чертёж, то есть первый сейф будет через 36 секунд.
  (Thread/sleep 36100)
  (is (= @(safe-storage :total-produced-counter) 1))
  (is (>= @(metal-storage :total-produced-counter) 3))
  (is (>= @(fit-storage :total-produced-counter) 51))
   ;Теперь, чтобы сделать второй сейф, нужно еще 3 железа (15 секунды) и 3 секунды на сейф + чертёж, то есть в общей сложности еще как минимум 28 секунд,но
   ; пока делался чертёж (долго) железо продолжало делаться, поэтому его нужно меньше, но мы действуем наверняка.
  (Thread/sleep 28000)
  (is (>= @(safe-storage :total-produced-counter) 2))
  (is (>= @(metal-storage :total-produced-counter) 6))
  (is (>= @(fit-storage :total-produced-counter) 90))
  (println "По итогу кол-во: сейфы " @(safe-storage :total-produced-counter) ", метал" @(metal-storage :total-produced-counter)
           ",ФИТ" @(fit-storage :total-produced-counter))
  )
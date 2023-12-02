(ns main_deadlock
  (:require [task5_deadlock :as t5]))

(defn -main [& args]
  (t5/dining-philosophers))

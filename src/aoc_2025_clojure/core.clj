(ns aoc-2025-clojure.core
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require aoc-2025-clojure.solutions.day-01)
  (:require aoc-2025-clojure.solutions.day-02)
  (:require aoc-2025-clojure.solutions.day-03))

(defn -main
  "Execute the solution to the given day's given problem."
  [day problem & args]
  (println (apply
            (deref (resolve (symbol
                             (str "aoc-2025-clojure.solutions.day-" (two-digit-num day))
                             (str "solve" problem))))
            args)))

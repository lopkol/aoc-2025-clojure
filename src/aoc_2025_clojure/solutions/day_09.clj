(ns aoc-2025-clojure.solutions.day-09
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split]]))

;; input parsing

(defn parse-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (reduce
      (fn [acc line]
        (let [[a b] (map parse-long (split line #","))]
          (conj acc [a b])))
      []
     (line-seq rdr))))

;; problem 1

(defn rect-area [[x1 y1] [x2 y2]]
  (* (inc (Math/abs (- x1 x2))) (inc (Math/abs (- y1 y2)))))

(defn solve1 [filename]
  (let [input (parse-input filename)
        indices (range (count input))
        pairs (for [i indices
                    j indices
                    :when (< i j)]
                [i j])
        areas (map (fn [[i j]]
                     (rect-area (nth input i) (nth input j)))
                   pairs)]
    (apply max areas)))



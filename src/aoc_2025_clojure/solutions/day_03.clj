(ns aoc-2025-clojure.solutions.day-03
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split]])
  (:require [clojure.math.numeric-tower :refer [expt]]))

;; input parsing

(defn parse-line [line]
  (map parse-int (split line #"")))

(defn read-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (map parse-line (reduce conj [] (line-seq rdr)))))

;; problem 1

(defn max-val-and-index [s]
  (reduce
    (fn [[mval midx] [idx x]]
      (if (= x 9)
        (reduced [x idx])
        (if (or (nil? mval) (> x mval))
          [x idx]
          [mval midx])))
    [nil nil]
    (map-indexed vector s)))

(defn max-joltage-in-line [line]
  (let [length (count line)
       vec-line (vec line)
       [first-digit first-digit-idx] (max-val-and-index (subvec vec-line 0 (- length 1)))
       second-digit (apply max (subvec vec-line (+ 1 first-digit-idx) length))]
    (+ (* 10 first-digit) second-digit)))

(defn solve1 [filename]
  (let [input (read-input filename)]
    (reduce + (map max-joltage-in-line input))))

;; problem 2

(defn max-joltage [line n]
  (let [length (count line)
       vec-line (vec line)
       positions (range (dec n) -1 -1)]
    (first (reduce
      (fn [[sum start-idx] pos]
        (let [[next-max-val next-max-idx] (max-val-and-index (subvec vec-line start-idx (- length pos)))]
          [(+ sum (* next-max-val (expt 10 pos))) (+ start-idx next-max-idx 1)]))
      [0 0]
      positions))))

(defn solve2 [filename]
  (let [input (read-input filename)]
    (reduce + (map #(max-joltage % 12) input))))

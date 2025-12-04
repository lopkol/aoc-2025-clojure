(ns aoc-2025-clojure.solutions.day-04
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split]]))

;; input parsing

(defn parse-char [char]
  (if (= char "@") true nil))

(defn parse-line [line]
  (mapv parse-char (split line #"")))

(defn read-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (mapv parse-line (line-seq rdr))))

;; problem 1

(defn neighbors [x y]
  [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)] [(dec x) (dec y)] [(dec x) (inc y)] [(inc x) (dec y)] [(inc x) (inc y)]])

(defn count-rolls-around [x y grid]
  (reduce (fn [acc neighbor]
    (if (get-in grid neighbor)
      (inc acc)
      acc))
    0
    (neighbors x y)))

(defn solve1 [filename]
  (let [grid (read-input filename)
        m (count grid)
        n (count (first grid))]
    (reduce
      (fn [acc [x y]]
        (if (and (get-in grid [x y]) (< (count-rolls-around x y grid) 4))
          (inc acc)
          acc))
      0
      (for [x (range m)
            y (range n)]
        [x y]))))

;; problem 2

(defn solve2 [filename]
  (let [grid (read-input filename)
        m (count grid)
        n (count (first grid))]
    (loop [total-sum 0
           current-grid grid]
      (let [[count modified-grid]
            (reduce
              (fn [[count modified-grid] [x y]]
                (if (and (get-in modified-grid [x y]) (< (count-rolls-around x y modified-grid) 4))
                  [(inc count) (assoc-in modified-grid [x y] nil)]
                  [count modified-grid]))
              [0 current-grid]
              (for [x (range m)
                    y (range n)]
                [x y]))]
        (if (zero? count)
          total-sum
          (recur (+ total-sum count) modified-grid))))))

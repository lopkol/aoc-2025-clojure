(ns aoc-2025-clojure.solutions.day-01
  (:require [aoc-2025-clojure.utils :refer :all]))

;; input parsing

(defn parse-line [line]
  (let [sign (if (= (first line) \R) 1 -1)
        n    (parse-int (subs line 1))]
    (* sign n)))

(defn read-input [filename]
  (with-open [rdr (clojure.java.io/reader (str "./inputs/" filename ".txt"))]
    (map parse-line (reduce conj [] (line-seq rdr)))))

;; problem 1

(defn count-zeros [input]
  (second (reduce (fn [acc move] (let [next-pos  (mod (+ (first acc) move) 100)
                                       num-zeros (second acc)]
    (if (= next-pos 0) [next-pos (inc num-zeros)] [next-pos num-zeros]))) [50, 0] input)))

(defn solve1 [filename]
  (let [input (read-input filename)]
    (count-zeros input)))

;; problem 2

(defn count-touching-zero [current-pos move]
  (let [rem-move (rem move 100)
        full-turns (quot (Math/abs move) 100)
        next-pos (+ current-pos rem-move)]
    (+ full-turns (if (or (= current-pos 0) (and (> next-pos 0) (< next-pos 100))) 0 1))))

(defn count-zeros-continuous [input]
  (second (reduce (fn [acc move] (let [current-pos (first acc)
                                       num-zeros (second acc)
                                       next-pos  (mod (+ current-pos move) 100)]
    [next-pos (+ num-zeros (count-touching-zero current-pos move))])) [50, 0] input)))

(defn solve2 [filename]
  (let [input (read-input filename)]
    (count-zeros-continuous input)))
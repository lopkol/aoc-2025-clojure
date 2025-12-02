(ns aoc-2025-clojure.solutions.day-02
  (:require [aoc-2025-clojure.utils :refer :all])
  (:require [clojure.string :refer [split]]))

;; input parsing

(defn parse-ranges [s]
  (->> (split s #",")
       (map #(split % #"-"))
       (map (fn [[a b]] [(Long/parseLong a) (Long/parseLong b)]))))

(defn read-input [filename]
  (parse-ranges (slurp (str "./inputs/" filename ".txt"))))

;; problem 1

(defn is-valid-id [num]
  (let [s (str num)
        len (count s)]
    (when (even? len)
      (let [half (/ len 2)
            first-half (subs s 0 half)
            second-half (subs s half len)]
        (= first-half second-half)))))

(defn sum-invalid-in-interval [start end is-valid-fn]
  (->> (range start (inc end))
       (filter is-valid-fn)
       (reduce + 0)))

(defn solve-with-valid-func [filename is-valid-fn]
  (let [input (read-input filename)]
    (reduce
      (fn [sum-invalid interval]
        (let [start (first interval)
              end (second interval)]
          (+ sum-invalid (sum-invalid-in-interval start end is-valid-fn))))
      0 input)))

(defn solve1 [filename]
  (solve-with-valid-func filename is-valid-id))

;; problem 2

(defn divisible? [n k]
  (zero? (mod n k)))

(defn split-n-by-n [s n]
  (map #(apply str %) (partition n s)))

(defn is-n-repeated [s len n]
  (when (divisible? len n)
    (apply = (split-n-by-n s n))))

(defn is-valid-id-2 [num]
  (let [s (str num)
        len (count s)]
    (->> (range 1 (inc (quot len 2)))
         (map #(is-n-repeated s len %))
         (some true?))))

(defn solve2 [filename]
  (solve-with-valid-func filename is-valid-id-2))

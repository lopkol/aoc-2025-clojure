(ns aoc-2025-clojure.utils)

(defn parse-int [n]
  (Integer/parseInt n))

(defn parse-long [n]
  (Long/parseLong n))

(defn two-digit-num
  "Takes a one- or two-digit number as string, returns it as a two-digit number string"
  [num]
  (if (< (count num) 2)
    (str "0" num)
    (str num)))

(ns mire.player)

;Player staff
(def ^:dynamic *current-room*)
(def ^:dynamic *inventory*)
(def ^:dynamic *name*)
(def ^:dynamic *keys-count* (ThreadLocal.))

; Constants
(def prompt "> ")
(def eol (System/getProperty "line.separator"))
(def target-score 50000)
(def finished (atom false))

(def streams (ref {}))
(def scores (ref {}))

(defn carrying? [thing]
  (some #{(keyword thing)} @*inventory*))

(defn game-is-finished? [_]
  (>= (count (filter #(>= % target-score) (vals @scores))) 1))

(defn add-points [points]
  (dosync
    (commute scores assoc *name* (+ (@scores *name*) points))
    (swap! finished game-is-finished?)))

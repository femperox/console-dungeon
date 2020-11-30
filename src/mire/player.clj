(ns mire.player)

;Player staff
(def ^:dynamic *current-room*)
(def ^:dynamic *inventory*)
(def ^:dynamic *name*)
(def ^:dynamic *keys-count* (ThreadLocal.))
(def health (ref {}))
(def max-health 100)
(def attack-value 25)
(def scores (ref {}))

; Constants
(def prompt "> ")
(def eol (System/getProperty "line.separator"))
(def target-score 50000)
(def finished (atom false))

(def streams (ref {}))

(defn carrying? [thing]
  (some #{(keyword thing)} @*inventory*))

(defn game-is-finished? [_]
  "Check if game is finished"
  (>= (count (filter #(>= % target-score) (vals @scores))) 1))

(defn add-points [points]
  "Add points to current player"
  (dosync
    (commute scores assoc *name* (+ (@scores *name*) points))
    (swap! finished game-is-finished?)))

(defn attack [target value]
  "Deal damage to player.
   Return true if was successful and false if not."
  (dosync
    (if (contains? @health target)
      (do
        (commute health assoc target (- (@health target) value))
        true)
      false)))

(defn set-health-value [target value]
  "Set players health value.
   Return true if was successful and false if not."
  (dosync
    (if (contains? @health target)
      (do
        (commute health assoc target value)
        true)
      false)))


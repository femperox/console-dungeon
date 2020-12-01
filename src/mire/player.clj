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

(defn set-health-value [target value]
  "Set players health value.
   Return true if was successful and false if not."
  (dosync
    (if (contains? @health target)
      (do
        (commute health assoc target value)
        true)
      false)))

(defn kill-player-for [target time room]
  "Remove player from room for 'time' seconds
   then restore all health and return to the same room"
  (.start (Thread. (fn []
    (binding [*out* (streams target)]
      (dosync
        (alter (:inhabitants @room) disj target))
      (println)
      (println (str "You was killed. Respawn in " time " sec."))
      (Thread/sleep (* time 1000))
      (set-health-value target max-health)
      (dosync
        (alter (:inhabitants @room) conj target))
      (println "You are ready to go.")
      (print prompt)(flush))))))

(defn attack [target value]
  "Deal damage to player.
   Return 0 target don't exist
          1 damage was done
          2 target died."
  (dosync
    (if (contains? @health target)
      (do
        (commute health assoc target (- (@health target) value))
        (if (<= (@health target) 0)
          (do
            (kill-player-for target 10 *current-room*)
            2)
          1))
      0)))

(defn get-health []
  "Get health value of current player"
  (@health *name*))


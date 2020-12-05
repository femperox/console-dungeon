(ns mire.player)

;Player staff
; комната в которой находися игрок
(def ^:dynamic *current-room*)
; инветраь игрока
(def ^:dynamic *inventory*)
; имя игрока
(def ^:dynamic *name*)
; колличество ключей
(def ^:dynamic *keys-count* (ThreadLocal.))
; мапа с хп всех игроков
(def health (ref {}))
; мапа с атаками всех игроков
(def attack-values (ref {}))
; мапа с очками игроков
(def scores (ref {}))

; константы игрока
; константа с максимальным хп
(def max-health 100)
; занчение базовой атаки
(def base-attack-value 25)
; время возрождения игрока
(def respawn-time 10)
; очки за убийства игрока
(def points-for-kill 5000)

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

(defn attack [target]
  "Deal damage to player.
   Return 0 target don't exist
          1 damage was done
          2 target died."
  (dosync
    (if (contains? @health target)
      (do
        (commute health assoc target (- (@health target) (@attack-values *name*)))
        (if (<= (@health target) 0)
          (do
            (kill-player-for target 10 *current-room*)
            (add-points points-for-kill)
            2)
          (do
            (commute health assoc *name* (- (@health *name*) (@attack-values target)))
            (if (<= (@health *name* ) 0)
              (kill-player-for *name* respawn-time *current-room*))
            1)))
      0)))

(defn get-health []
  "Get health value of current player"
  (@health *name*))


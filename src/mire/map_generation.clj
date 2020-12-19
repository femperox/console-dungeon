(ns map_generation)

(def room_struct (ref {}))

(defn gen-lvl [cur-room lvl]
  (dosync 
   (loop [lv lvl
          cr cur-room]
         (do
           (commute cr assoc :room-name (apply str ["room " lv]) :exits (ref {}))
           (commute (:exits cr) assoc  :next (ref {}))
           (if (not= lv 1)
             (do
               (commute (:exits cr) assoc :next (ref {}))
               (commute (:next (:exits cr)) assoc :prev cr))
            )
           (if (< lv 5)
             (recur (:next (:exits cr)) (+ lv 1)))
            )
           )
         )
    )

;; assoc для добавления новой пары ключ-значение
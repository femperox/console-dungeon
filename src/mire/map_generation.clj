(ns map_generation)

(def room_struct (ref {}))

(defn gen-lvl [cur-room lvl]
  (dosync 
   (loop([lv lvl
          cr cur-room]
         (do
           (commute assoc cr :room-name (apply str ["room " lv]) :exits (ref {}))
           (if (= lv 1)
               (commute assoc (:exits cr) :next (ref {}))
               (recur (:next (:exits cr)) (+ lv 1))))
           (if (and (< lv 5) (!= lv 1))
                (do
                  (commute assoc (:exits cr) :next (ref {}))
                  (commute assoc (:next (:exits cr)) :prev cr)
                  (recur (:next (:exits cr)) (+ lv 1))
                  ))
           )
         )
    )
   ))


;; assoc для добавления новой пары ключ-значение
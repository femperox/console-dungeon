(ns map_generation)

(def room_struct (ref {}))

(defn gen-lvl [cur-room lvl]
  (try
  (dosync
    (loop [lv 1
          cr cur-room
          pr nil]
          
          ;  (println cr)
          ;  (println lv)
            (do 

                (commute cr assoc :room-name (apply str ["room " lv])) 
                (commute cr assoc :exits (ref {})) 
                (commute (:exits @cr) assoc :next (ref {}))
                (commute (:exits @cr) assoc :prev pr)
                (println lv)
                (println cr)
              ; (if (= lv 1)
              ;   ; (do
              ;   ;   (commute (:exits @cr) assoc :next (ref {}))
              ;   ;   (commute (:next @(:exits @cr)) assoc :prev cr))
              ;   (commute (:exits @cr) assoc :prev nil)
              ; )
                  (if (< lv lvl)
                    (do
                      (println "we are here")
                      (recur (+ lv 1) (:next @(:exits @cr)) (cr))
                    )
                    (commute (:exits @cr) assoc :next nil)
                  )
            )
    )
  )
    (catch Exception e
    (.printStackTrace e (new java.io.PrintWriter *err*))) 
  )
)

(defn through [room]
  (loop [r room]
    (do
      (println (:room-name @r))
      (if (not= (:next @(:exits @r)) nil)
        (recur (:next @(:exits @r)))
      )
    )
  )
)

(gen-lvl room_struct 3)
; (through room_struct)
;; assoc для добавления новой пары ключ-значение
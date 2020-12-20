(ns map_generation)

(def rooms_graf_head (ref {}))

(defn gen-lvl [cur-room lvl]
  (try
  (dosync
    (loop 
      [lv 1
      cr cur-room
      pr nil]
      (do 
        (commute cr assoc :room-name (apply str ["room " lv]) :exits (ref {})) 
        (commute (:exits @cr) assoc :next (ref {}) :prev pr)
        (println lv)
          (if (< lv lvl)
            (do
              (println "we are here")
              (recur (+ lv 1) (:next @(:exits @cr)) cr)
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
        (def final r)
      )
    )
  )
  (println)
  (loop [r final]
    (do
      (println (:room-name @r))
      (if (not= (:prev @(:exits @r))) nil
        (recur (:prev @(:exits @r)))
      )
    )
  )
)

(gen-lvl rooms_graf_head 5) ; создание двунаправленного списка 
(through rooms_graf_head) ; просмотр в обе стороны
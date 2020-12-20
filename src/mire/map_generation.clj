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

(defn get-n-rand-from-set [n set]
  (loop [iter n
        rand-n-set #{}
        old-set set] 
    (let [rand-value (rand-int (count old-set))]
      (if (> iter 0)
        (recur
          (- iter 1)
          (conj rand-n-set (nth (vec old-set) rand-value))
          (disj old-set (nth (vec old-set) rand-value))
        )
        rand-n-set
      )
    )
  )
)

(defn gen-sides [origin-way]
  (let [sides (disj #{:north :south :west :east} origin-way)
        sides-count (randint 3)]
    (if (= sides-count 2)
      sides
      (loop []

      )
    )
  )
)

; принимает ключевое слово направления и возвращает противоположное
(defn opposite_way [way]
  (keyword (way {:north "south" :east "west" :south "north" :west "east"}))  
)

(defn gen-head [graf_head]
  (dosync
    (commute graf_head assoc :exits (ref {}))
    (doseq [world_side [:north :east :south :west]] 
      (commute (:exits @graf_head) assoc world_side (ref {}))
      (commute (world_side @(:exits @graf_head)) assoc (opposite_way world_side) graf_head)
    )
  )
)

; (gen-lvl rooms_graf_head 5) ; создание двунаправленного списка 
; (through rooms_graf_head) ; просмотр в обе стороны
(gen-head rooms_graf_head)
(print rooms_graf_head)

; (defn pr [subj] (println subj))

; (println (for [w '(:north :east :south :west)] w)) ; :north :east :south :west "north" "east" "south" "west"
; (doseq [w [:north :east :south :west]] (pr w))

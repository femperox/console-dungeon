;; Прописали комнаты в пространство имён
(ns mire.rooms
  (:use [clojure.contrib str-utils]))

;; Определяем комнаты
;; rooms - hash-map, в котором 2 комнаты - start, closet, которые также являются hash-map-ами
(def rooms
     {:start {:desc "You find yourself in a round room with a pillar in the middle."
              :exits {:north :closet}}
      :closet {:desc "You are in a cramped closet."
                :exits {:south :start}}})
;; создаём глобальную переменную текущей комнаты, передавая hash-map стартовой комнаты
(def *current-room* (rooms :start))

;; Определяем функцию, 
(defn set-current-room [target]
     (def *current-room* target))
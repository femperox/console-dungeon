(ns mire.rooms
  (:use [clojure.contrib str-utils]))

(def rooms
     {:start {:desc "You find yourself in a round room with a pillar in the middle."
              :exits {:north :closet}
              ; сет с именами игроков доступен по ключу :inhabitants
              :inhabitants (ref #{})}
      :closet {:desc "You are in a cramped closet."
               :exits {:south :start}
               ; сет с именами игроков доступен по ключу :inhabitants
               :inhabitants (ref #{})}})

(def *current-room*)
(def player-name)
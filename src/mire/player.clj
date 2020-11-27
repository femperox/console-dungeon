(ns mire.player
  (:use clojure.contrib.seq-utils))

(def *current-room*)
(def *inventory*)
(def *player-name*)


;; Определяем функцию, которая проверяет наличие у игрока вещи в инвентаре
(defn carrying?
  [thing]
  (includes? @*inventory* (keyword thing)))
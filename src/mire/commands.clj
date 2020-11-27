(ns mire.commands
  (:use [mire rooms util])
  (:use [clojure.contrib str-utils seq-utils]))

;; Command functions

;; Команда look, не принимает параметров, она получает как строчку описание комнаты,
;; затем показывает, в какую комнату идёт выход
(defn look "Get a description of the surrounding environs and its contents."
  []
  (str (:desc *current-room*)
       "\nExits: " (keys (:exits *current-room*))
       ".\n"))

;; Команда move принимает параметр direction - строка направления, например north
(defn move
  "\"♬ We gotta get out of this place... ♪\" Give a direction."
  [direction]
  ;; здесь target-name получает ключевое имя комнаты
  ;; а target будет соответствовать hash-map-у комнаты, куда мы попали, если комната в этом направлении существует
  (let [target-name ((:exits *current-room*) (keyword direction))
        target (rooms target-name)]
    ;; Если комната существует, то вызывается функция, устанавливающая текущую комнату, после вызывается look,
    ;; иначе выводится надпись "You can't go that way."
    (if target
      (do (set-current-room target)
          (look))
      "You can't go that way.")))

;; Command data

;; Объявляются ассоциации строчек и функций, которым они соответствуют
(def commands {"move" move
               "north" (fn [] (move :north))
               "south" (fn [] (move :south))
               "east" (fn [] (move :east))
               "west" (fn [] (move :west))
               "look" look})

;; Command handling

(defn execute
  "Execute a command that is passed to us."
  [input]
  (let [input-words (re-split #"\s+" input)
        command (first input-words)
        args (rest input-words)]
    (apply (commands command) args)))
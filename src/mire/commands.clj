(ns mire.commands
  (:use [mire rooms])
  (:use [clojure.contrib str-utils seq-utils]))

;; Command functions

; описание команты
(defn look "Get a description of the surrounding environs and its contents."
  []
  ; полу описание комнаты
  (str (:desc @*current-room*)
        ; список выходов
       "\nExits: " 
       ; получаем список ключей т.е. список доступных напралений
       (keys (:exits @*current-room*))
       ".\n"))

(defn move
  "\"♬ We gotta get out of this place... ♪\" Give a direction."
  [direction]
  ; начинаем транзакцию
  (dosync
    (let [target-name ((:exits @*current-room*) (keyword direction))
          target (rooms target-name)]
      ; если комната сушествует та на чинаем обработку
      (if target
        (do 
          ; обработка синхранизирований переменной
          (alter 
            ; получаем занчение сылки current-room 
            (:inhabitants @*current-room*) 
            ; удаляем игрока из сета
            disj player-name)
          ; делаем тоже самое но добаляем игрока в таргет комнату
          (alter (:inhabitants target) conj player-name)
          ; переставляем указатель на новую текушию комнату
          (ref-set *current-room* target)
          ; осматриваем комнату
          (look))
        ; комнаты нет сообшении ошибки
       "You can't go that way."))))

;; Command data

(def commands {"move" move,
               "north" (fn [] (move :north)),
               "south" (fn [] (move :south)),
               "east" (fn [] (move :east)),
               "west" (fn [] (move :west)),
               "look" look})

;; Command handling

(defn execute
  "Execute a command that is passed to us."
  [input]
  (let [input-words (re-split #"\s+" input)
        command (first input-words)
        args (rest input-words)]
    (apply (commands command) args)))
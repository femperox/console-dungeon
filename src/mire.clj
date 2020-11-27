#!/usr/bin/env clj

(add-classpath (str "file://" (.getParent (java.io.File. *file*)) "/"))

(ns mire
  (:use [mire commands rooms])
  (:use [clojure.contrib server-socket duck-streams]))

(def port 3333)
(def prompt "> ")

(defn- mire-handle-client [in out]
  (binding [*in* (reader in)
            *out* (writer out)]

    ;; We have to nest this in another binding call instead of using
    ;; the one above so *in* and *out* will be bound to the socket
    (print "\n\rWhat is your name? ") (flush)
    (binding [player-name (read-line)
              *current-room* (ref (rooms :start))]
      ; Добовляем игрока в камнату
      (dosync (alter (:inhabitants @*current-room*) conj player-name))

      ; Описание комнаты
      (println (look)) (print prompt) (flush)

      (loop [input (read-line)]
        ; выполнить команду
        (println (execute input))
        (print prompt)
        (flush)
        ; читаем нову команду
        (recur (read-line))))))

(def server (create-server port mire-handle-client))
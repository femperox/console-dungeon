#!/usr/bin/env clj
;; Добавили функционал commands в пространство имён mire
(ns mire
  (:use [mire commands])
  (:use [clojure.contrib server-socket duck-streams]))

(def port 3333)
(def prompt "> ")

(defn- mire-handle-client [in out]
  (binding [*in* (reader in)
            *out* (writer out)]
    ;; Выводит > в выходной поток, затем чистит поток вывода
    (print prompt) (flush)
    ;; инициализуем input строчкой из потока ввода
    ;; затем выводим результат выполнения на экран, снова очищаем поток вывода
    ;; делаем рекурсию по вводу строчки
    (loop [input (read-line)]
      (println (execute input))
      (print prompt)
      (flush)
      (recur (read-line)))))

(def server (create-server port mire-handle-client))
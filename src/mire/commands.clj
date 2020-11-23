;; Определили пространство имён commands, заполнив в буфер библиотеку java для работы со строчками
(ns mire.commands
  (:use [clojure.contrib str-utils]))
;; Функция current-time при вызове выводит текущее время
(defn current-time []
  (str "It is now " (java.util.Date.)))
;; Команды - map (пара "ключ-значение"), который содержит "ассоциации":
;; time будет ссылкой на вызов функции current-time
;; look будет вызовом анонимной функции без параметров, которая возвращает строку
(def commands {"time" current-time
               "look" (fn [] "You see an empty room, waiting to be filled.")})

;; Определяем функцию, вызываемую сервером, принимающую один параметр input
(defn execute
  "Execute a command that is passed to us."
  [input]
  ;; Привязываем переменные к их значениям
  ;; input-words - массив строк, полученный разбиением сроки, полученной из входного потока
  ;; command - первый элемент input-words, как правило, это название команды
  ;; args - остальной массив (может быть и пустым) строк, это будут параметры команды
  (let [input-words (re-split #"\s+" input)
        command (first input-words)
        args (rest input-words)]
    ;; apply применяет аргументы к функции
    ;; здесь мы получаем значение из мапа commands по ключу command (это у нас функции),
    ;; далее к функции apply подставляет список аргументов args
    (apply (commands command) args)))
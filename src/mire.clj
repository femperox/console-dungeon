#!/usr/bin/env clj

;; Определяем пространство имён mire, подключаем библиотеки для работы с сокетами, многопоточностью
(ns mire
  (:use [clojure.contrib server-socket duck-streams]))

;; Определяем порт 3333
(def port (* 3 1111))

;; Определяем функцию обработки клиента. 
;; Параметры in, out берутся как классы java, отвечающие за чтение/запись данных из/в поток(а)
;; соответственно ввод с клавиатуры в цикле перенаправляется в in, а вывод на экран в out,
;; всё это вызывается как бесконечная рекурсия
(defn mire-handle-client [in out]
  (binding [*in* (reader in)
            *out* (writer out)]
    
    (loop []
      (println (read-line))
      (recur))))
;; Определяем сервер с помощью create-server, передавая порт, на котором будет открыт сокет, а также
;; функцию, вызываемую, пока сокет активен
(def server (create-server port mire-handle-client))

(ns mire.rooms)

; объявляем комнату
(declare rooms)

; загружаем комнаты из файла
; rooms - с писок комнат к которму добаляется новая комната
; file - файл из которого загружается комната
; возврашается мапа с добаленной комнатой
(defn load-room [rooms file]
  (let [room 
    ; читает объект из сторки
    (read-string 
      ; читает весь файл в строчку
      (slurp 
        ; полный путь к файлу
        (.getAbsolutePath file)))]

    ; собираем новую комнату
    (conj rooms
      ; задаем ключевое слово для комнаты по имени файла
      {(keyword (.getName file))
        {; заполняем описание комнаты
          :desc (:desc room)
          ; заполняем выходы
          :exits (ref (:exits room))
          ; создаем пустой список игроков в этой комнате
          :inhabitants (ref #{})}})))

; загружаем комнаты из директории
; dir - директория из которой загружаются комнаты
; возврашается мапа с комнотами
(defn load-rooms [dir]
  "Given a dir, return a map with an entry corresponding to each file
  in it. Files should be maps containing room data."
  ; поочериде каждую комнату
  (reduce load-room {} 
    ; возврашает список файлов из директории
    (.listFiles 
      ; возврашает объект файл указываюший на директорию
      (java.io.File. dir))))

(defn set-rooms
  "Set mire.rooms/rooms to a map of rooms corresponding to each file
  in dir. This function should be used only once at mire startup, so
  having a def inside the function body should be OK."
  [dir]
  (def rooms (load-rooms dir)))

(def *current-room*)
(def player-name)
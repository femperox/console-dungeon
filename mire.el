;; For launching Mire from SLIME
;; В clojure используется пространство имён для работы в REPL (ввод-вывод в консоли)
;; По умолчанию это user. Для того чтобы подгрузить библиотеку, но не добавлять данные из неё в пространство
;; имён используют require
(require) 'clojure-mode

;; Здесь задаётся последовательное объявление переменных, хранящих пути к определённым директориям
(setq mire-dir (file-name-directory
                (or (buffer-file-name) load-file-name))
      swank-clojure-jar-path (concat mire-dir "jars/clojure.jar")
      ;; Здесь задаётся список, хранящий нужные пути
      swank-clojure-extra-classpaths (list (concat mire-dir "jars/clojure-contrib.jar")
                                           (concat mire-dir "src")))

;; Superior Lisp Interaction Mode for Emacs
;; Иными словами, здесь задействуется специальный режим для разработки на Lisp
;; для семейства многофункциональных текстовых редакторов Emacs.
;; Дальнейший код написан на Lisp
(slime)

;; Находит файл и вставляет содержимое файла в буффер
(find-file (concat mire-dir "src/mire.clj"))

;; Определяем функцию mire
(defun mire ()
  (interactive)
  (if (get-buffer "*mire*")
    (switch-to-buffer "*mire*")
    (telnet "localhost" 3333)
    (rename-buffer "*mire*")))

(global-set-key (kbd "C-c m") 'mire)

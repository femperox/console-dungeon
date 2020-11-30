(ns mire.player)

(def ^:dynamic *current-room*)
(def ^:dynamic *inventory*)
(def ^:dynamic *name*)
(def ^:dynamic *keys-count* (ThreadLocal.))

(def prompt "> ")
(def eol (System/getProperty "line.separator"))

(def streams (ref {}))
(def scores (ref {}))

(defn carrying? [thing]
  (some #{(keyword thing)} @*inventory*))

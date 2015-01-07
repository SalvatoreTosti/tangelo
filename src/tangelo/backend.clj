(ns tangelo.backend
  (:gen-class)
  (:require
      [seesaw.core :as seesaw]))

(defn save-file [location text]
  (spit location text))

(defn open-file [location]
   ;;(if (fs/exists? location) ;TODO: add
  (slurp location))

(defn text-from-widget [widget]
  (seesaw/text widget))

(defn print-text [text]
  (println text)) ;text))

(defn get-and-print [content]
  (-> ;(text-from-widget content)
      (print-text)))

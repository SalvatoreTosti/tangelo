(ns tangelo.backend
  (:gen-class)
  (:require
      [seesaw.core :as seesaw]))

(defn text-from-widget [widget]
  widget)

(defn print-text [text]
  (println text)) ;text))

(defn get-and-print [content]
  (-> ;(text-from-widget content)
      (print-text)))

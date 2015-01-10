(ns tangelo.backend
  (:gen-class)
  (:require
      [seesaw.core :as seesaw]))

(defn build-full-location [directory name ext]
  (let [sep (System/getProperty "file.separator")
        path (str directory sep name ext)]
    path))

(defn save-file [{
                  directory :directory
                  name :name
                  text :text
                  links :links
                  }]
  (let [text-location (build-full-location directory name ".txt")
       link-location (build-full-location directory name ".lslc")]
    (spit text-location text)
    (spit link-location links)))



(defn open-text-file [location]
   ;;(if (fs/exists? location) ;TODO: add file exist protections
  (slurp location))

(defn open-data-file [location]
  (->> (slurp location)
       (read-string )))

(defn text-from-widget [widget]
  (seesaw/text widget))

(defn print-text [text]
  (println text)) ;text))

(defn get-and-print [content]
  (-> ;(text-from-widget content)
      (print-text)))



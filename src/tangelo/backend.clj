(ns tangelo.backend
  (:gen-class)
  (:require
      [seesaw.core :as seesaw]))


(defn add-link-head [datab link-head]
  (assoc datab link-head #{}))

(defn add-link-pair-helper [datab link-head link-tail]
  (update-in datab [link-head] #(conj % link-tail)))

;link pairs represented as:
;{[head-start,head-end] #{[tail-start,tail end]}}
;Note there can be many 'tail' vectors in the set.

(defn add-link-pair [datab link-head link-tail]
  (if (contains? datab link-head)
    (add-link-pair-helper datab link-head link-tail)
    (add-link-pair-helper (assoc datab link-head #{}) link-head link-tail)))


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



(defn open-file [location]
   ;;(if (fs/exists? location) ;TODO: add file exist protections
  (slurp location))

(defn text-from-widget [widget]
  (seesaw/text widget))

(defn print-text [text]
  (println text)) ;text))

(defn get-and-print [content]
  (-> ;(text-from-widget content)
      (print-text)))



(ns tangelo.backend
  (:gen-class)
  (:require
      [seesaw.core :as seesaw]))


(defn add-link-head [datab link-head]
  (assoc datab link-head #{}))

(defn add-link-pair [datab link-head link-tail]
  (update-in datab [link-head] #(conj % link-tail)) ;(conj (datab link-head) link-tail))
  )


(defn save-file [location text]
  (spit location text))

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

(let [a (atom [])]
  (swap! a #(assoc % 0 5))
  (swap! a #(assoc % 1 6))
  )

(let [a (atom {})]
  (swap! a #(add-link-head % [2,3]))
  @a
  (swap! a #(add-link-pair % [2,3] [7,8]))
  (swap! a #(add-link-pair % [2,3] [10,10]))
)
({} :a)
(conj #{} 1)

(conj [1 2] 2)

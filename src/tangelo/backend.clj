(ns tangelo.backend
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [seesaw.chooser :as chooser]
   [clojure.string :as cljstr]
   [me.raynes.fs :as fs]))

(defn build-full-location [directory name ext]
  (let [sep (System/getProperty "file.separator")
        path (str directory sep name ext)]
    path))

;old save file method, does not implement seesaw's built in libraries
#_(defn save-file [{
                  directory :directory
                  name :name
                  text :text
                  links :links
                  }]
  (let [text-location (build-full-location directory name ".txt")
       link-location (build-full-location directory name ".lslc")]
    (spit text-location text)
    (spit link-location links)))

(defn save-file [text-pane]
  (chooser/choose-file
   :type :save
   :success-fn (fn [fc file]
                 (spit file  (seesaw/config text-pane :text)))))


(defn open-file [text-pane]
   (seesaw/text! text-pane (chooser/choose-file :type :open)))

(defn keypress-char [e]
  (.getKeyChar e))

(defn blank-char? [character]
  (cljstr/blank? (str character)))

(defn undo-manager-text [atom-undo-db current-text e]
  (let [pressed-char (keypress-char e)
        existing-doc (reduce str @atom-undo-db)]
    (if (blank-char? pressed-char)
      (swap! atom-undo-db conj current-text))
    (println @atom-undo-db)
      ))

(defmulti undo-manager
 (fn [atom-editor-mode atom-undo-db current-text e] @atom-editor-mode))
(defmethod undo-manager :text
  [atom-editor-mode atom-undo-db current-text e]
  (undo-manager-text atom-undo-db current-text e))
  ;(swap! atom-editor-mode (constantly :hyper)))
(defmethod undo-manager :hyper
  [atom-editor-mode]
  nil)
  ;(swap! atom-editor-mode (constantly :text)))


(defn open-text-file [location]
  (if (fs/exists? location)
    (slurp location)))

(defn open-data-file [location]
  (if (fs/exists? location)
    (->> (slurp location)
         (read-string ))))

(defn text-from-widget [widget]
  (seesaw/text widget))

(defn print-text [text]
  (println text)) ;text))

(defn get-and-print [content]
  (-> ;(text-from-widget content)
      (print-text)))



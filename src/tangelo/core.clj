(ns tangelo.core
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [tangelo.gui :as gui]))


(defn -main
  [& args]
  (seesaw/native!)
  (gui/display (gui/build-content)))

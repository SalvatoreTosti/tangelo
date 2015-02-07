(ns tangelo.textEditor
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [seesaw.font :as font]))

(defn seesaw-font-test []
  "test method for examining how fonts are listed in seesaw."
  (let [font-list (font/font-families)]
    (dorun (map println font-list))))


;;Maybe have a menu with a button here, which saves current status
(defn customize-display-list []
  (let [font-list (font/font-families)
        window (seesaw/frame
               :title "Display Options"
               ;:on-close :exit
               ;:menubar menu-bar
               :content (seesaw/scrollable (seesaw/listbox :model font-list))
               :width 225 ;850
               :height 275)]
    (seesaw/show! window)
    ))

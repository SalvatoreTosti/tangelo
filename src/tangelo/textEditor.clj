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
(defn customize-display-list [text-pane display-information-atom]
  "Creates popup listing fonts available on system"
  (let [fonts (font/font-families)
        font-list (seesaw/listbox
                   :model fonts)
        window (seesaw/frame
               :title "Display Options"
               :content (seesaw/scrollable font-list)
               :width 225
               :height 275)]
    (seesaw/listen font-list
                   :mouse-clicked (fn [e]
                                    (->> (swap! display-information-atom
                                           :font (seesaw/selection font-list))
                                         (seesaw/config! text-pane :font))
                                    ))
    (seesaw/show! window)
    ))

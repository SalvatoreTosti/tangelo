(ns tangelo.gui
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [tangelo.backend :as backend]))

(defn build-menubar [text-pane]
  (seesaw/menubar
   :items [(seesaw/menu :text "File"
                        :items [(seesaw/action
                                :name "Save"
                        ;:key "menu N"
                                :handler (fn [e] (backend/text-from-widget text-pane)))]
                                           ;(backend/get-and-print text-pane)))]
                        )]))

(defn build-content []
  (let [text-pane (seesaw/text
                     :multi-line? true
                     :editable? true
                     :margin 20  ;margin in pixels
                     :caret-position 0
                     )]
        (seesaw/scrollable text-pane)))

(defn display [content]
  "General display function, based on lecture slides, builds Jframe for program."
  (let [menu-bar (build-menubar content)

        window (seesaw/frame
                :title "Tangelo"
                :on-close :exit
                :menubar menu-bar
                :content content
                :width 425 ;850
                :height 550)] ;1100)
    (seesaw/show! window)))

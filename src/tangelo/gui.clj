(ns tangelo.gui
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [tangelo.backend :as backend]))

(defn build-menubar [text-pane link-helper-atom link-db]
  (seesaw/menubar
   :items [(seesaw/menu :text "File"
                        :items [(seesaw/action
                                :name "Save"
                        ;:key "menu N"
                                :handler (fn [e]
                                           (backend/save-file
                                            {:directory "target"
                                             :name "test-text"
                                             :text (backend/text-from-widget text-pane)
                                             :links @link-db
                                             })))
                                (seesaw/action
                                 :name "Open"
                                 :handler (fn [e]
                                           (seesaw/text! text-pane (backend/open-text-file "target/test-text.txt"))
                                           (reset! link-db (backend/open-data-file "target/test-text.lslc"))
                                            ))
                                (seesaw/action
                                 :name "Hyper start"
                                 :handler (fn [e]
                                            (swap! link-helper-atom assoc :start (seesaw/selection text-pane))))
                                (seesaw/action
                                 :name "Hyper end"
                                 :handler (fn [e]
                                            ;nil))
                                            (swap! link-helper-atom assoc :end (seesaw/selection text-pane))))
                                (seesaw/action
                                 :name "Hyper add"
                                 :handler (fn[e]
                                            ;nil))
                                            (swap! link-db #(backend/add-link-pair % (@link-helper-atom :start) (@link-helper-atom :end)))))
                                (seesaw/action
                                 :name "View hyper links"
                                 :handler (fn [e]
                                            (println @link-db)))


                                 ]
                        )]))

(defn build-content []
  (let [text-pane (seesaw/text
                     :multi-line? true
                     :editable? true
                     :margin 20  ;margin in pixels
                     :caret-position 0)]
    text-pane))
        ;(seesaw/scrollable text-pane)))

(defn display [content]
  "General display function, based on lecture slides, builds Jframe for program."
  (let [menu-bar (build-menubar content (atom {}) (atom {}))
        scroll-content (seesaw/scrollable content)
        window (seesaw/frame
                :title "Tangelo"
                :on-close :exit
                :menubar menu-bar
                :content scroll-content
                :width 425 ;850
                :height 550)] ;1100)
    (seesaw/show! window)))

(defn run []
    (display (build-content)))

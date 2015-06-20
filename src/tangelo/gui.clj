(ns tangelo.gui
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [tangelo.backend :as backend]
   [tangelo.hyperEditor :as hyper]))

(defmulti cycle-mode
 (fn [atom-editor-mode] @atom-editor-mode))
(defmethod cycle-mode :text
  [atom-editor-mode]
  (swap! atom-editor-mode (constantly :hyper)))
(defmethod cycle-mode :hyper
  [atom-editor-mode]
  (swap! atom-editor-mode (constantly :text)))

(defn build-menubar [text-pane link-helper-atom link-db editor-mode]
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
                                ])

           (seesaw/menu :text "Link"
                        :items [(seesaw/action
                                 :name "Hyper start"
                                 :handler (fn [e]
                                            (swap! link-helper-atom assoc :start (seesaw/selection text-pane))))
                                (seesaw/action
                                 :name "Hyper end"
                                 :handler (fn [e]
                                            (swap! link-helper-atom assoc :end (seesaw/selection text-pane))))
                                (seesaw/action
                                 :name "Hyper add"
                                 :handler (fn [e]
                                            (swap! link-db #(hyper/add-link-pair % (@link-helper-atom :start) (@link-helper-atom :end)))))
                                (seesaw/action
                                 :name "Jump link"
                                 :handler (fn [e]
                                            (let [tail (first (@link-db (seesaw/selection text-pane)))
                                                  begin (first tail)
                                                  end (last tail)]
                                              (if tail (do
                                              (seesaw/config! text-pane :caret-position begin)
                                              (seesaw/selection! text-pane tail)))
                                              nil)))
                                (seesaw/action
                                 :name "View hyper links"
                                 :handler (fn [e]
                                            (println @link-db)))
                                ])
           (seesaw/menu :text "Mode"
                        :items [(seesaw/action
                                 :name "View mode"
                                 :handler (fn [e]
                                            (println (str @editor-mode))))
                                (seesaw/action
                                 :name "Hyper"
                                 :handler (fn [e]
                                            (swap! editor-mode (constantly :hyper))))
                                (seesaw/action
                                 :name "Text"
                                 :handler (fn [e]
                                            (swap! editor-mode (constantly :text))))
                                (seesaw/action
                                 :name "Cycle mode"
                                 :handler (fn [e]
                                            (cycle-mode editor-mode)))
                                ])

           ]))


(defn build-content []
  (let [text-pane (seesaw/styled-text
                     ;:multi-line? true
                     :wrap-lines? true
                     :editable? true
                     :margin 20  ;margin in pixels
                     :caret-position 0)]
    text-pane))
        ;(seesaw/scrollable text-pane)))

(defn display [content]
  "General display function, based on lecture slides, builds Jframe for program."
  (let [editor-mode (atom :text)
        menu-bar (build-menubar content (atom {}) (atom {}) editor-mode)
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

(ns tangelo.gui
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [tangelo.backend :as backend]
   [tangelo.hyperEditor :as hyper]
   [tangelo.textEditor :as text-edit]
   ;[seesaw.font :as font]
   ))

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
                                 ;TODO: Maybe create a wrapper function for this?
                                 :handler (fn [e]
                                            (swap! link-helper-atom assoc
                                                   :head (seesaw/selection text-pane)
                                                   :head-text (hyper/text-from-selection
                                                                (seesaw/text text-pane)
                                                                (seesaw/selection text-pane)))))
                                (seesaw/action
                                 :name "Hyper end"
                                 :handler (fn [e]
                                            (swap! link-helper-atom assoc
                                                   :tail (seesaw/selection text-pane)
                                                   :tail-text (hyper/text-from-selection
                                                               (seesaw/text text-pane)
                                                               (seesaw/selection text-pane)))))
                                (seesaw/action
                                 :name "Hyper add"
                                 :handler (fn [e]
                                            (swap! link-db #(hyper/insert-new-link % link-helper-atom))))
                                (seesaw/action
                                 :name "Jump link"
                                 :handler (fn [e]
                                            (let [tail (first (@link-db (seesaw/selection text-pane)))
                                                  begin (first tail)
                                                  end (last tail)]
                                              (seesaw/config! text-pane :caret-position begin)
                                              (seesaw/selection! text-pane tail))))
                                (seesaw/action
                                 :name "View hyper links"
                                 :handler (fn [e]
                                            (println @link-db)))
                                #_(seesaw/action
                                 :name "text test"
                                 :handler (fn [e]
                                            (println
                                             (hyper/text-from-selection
                                              (seesaw/text text-pane)
                                              (seesaw/selection text-pane)))))
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

           (seesaw/menu :text "Text"
                        :items [(seesaw/action
                                 :name "change font"
                                 :handler (fn [e]
                                            (text-edit/customize-display-list)
                                            ;(seesaw-font-test)
                                            ))])
                                            ;(seesaw/config! text-pane
                                            ;        :font "MONOSPACED-PLAIN-12"
                                            ;         ;:background "#f88"
                                            ;                )))])

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
    (seesaw/show!  window)));to enable full screen: (seesaw/toggle-full-screen! window))))

(defn run []
    (display (build-content)))

(ns tangelo.gui
  (:gen-class)
  (:require
   [seesaw.core :as seesaw]
   [seesaw.chooser :as chooser]
   [clojure.java.io :as io]
   [clojure.string :as cljstr]
   [tangelo.backend :as backend]
   [tangelo.hyperEditor :as hyper]
   [tangelo.textEditor :as text-edit]
   ;[seesaw.font :as font]
   )
  (import java.awt.Insets))

(defmulti cycle-mode
 (fn [atom-editor-mode] @atom-editor-mode))
(defmethod cycle-mode :text
  [atom-editor-mode]
  (swap! atom-editor-mode (constantly :hyper)))
(defmethod cycle-mode :hyper
  [atom-editor-mode]
  (swap! atom-editor-mode (constantly :text)))

;(defn build-menubar [text-pane link-helper-atom link-db editor-mode]
(defn build-menubar [{text-pane :text-pane
                      link-helper-atom :link-helper-atom
                      link-db :link-db
                      editor-mode :editor-mode
                      undo-history-text :undo-history-text
                      display-information-atom :display-information-atom},
                     scroll-content]
  (seesaw/menubar
   :items [(seesaw/menu :text "File"
                        :items [(seesaw/action
                                :name "Save"
                        ;:key "menu N"
                                :handler (fn [e]
                                           (backend/save-file text-pane)))

                                (seesaw/action
                                 :name "Open"
                                 :handler (fn [e]
                                            (backend/open-file text-pane)
                                            ;(reset! link-db (backend/open-data-file "target/test-text.lslc"))
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
                                            (text-edit/customize-display-list text-pane display-information-atom)
                                            ;(seesaw/listen :item-state-changed display-information-atom
                                            ;               (fn [e]
                                            ;                 (seesaw/config! text-pane :font (@display-information-atom :font))))
                                            (let [font (:font @display-information-atom)]
                                              )
                                              ;(println font))
                                            ;(println (:font @display-information-atom))
                                            ;(seesaw/config! text-pane
                                            ;              :font (@display-information-atom :font))
                                            ))
                                (seesaw/action
                                 :name "undo"
                                 :handler (fn [e]
                                            (let [last-state (peek @undo-history-text)]
                                              (seesaw/config! text-pane :text last-state)
                                              (swap! undo-history-text pop))))

                                 (seesaw/action
                                 :name "line numbers"
                                 :handler (fn [e]
                                            ;(println (seesaw/config text-pane :rows))
                                            (seesaw/config! scroll-content :row-header (seesaw/text :text  "1.\n2.\n";(get-line-numbers text-pane)
                                                                                                    ;:wrap-lines? (seesaw/config text-pane :wrap-lines?)
                                                                                                    :editable? false
                                                                                                    :margin 10 ;margin in pixels
                                                                                                    :multi-line? true))  ;make this information another textpane ["1" "2" "3"])

                                            ;(seesaw/config! text-pane :margin 10)

                                            (.setMargin text-pane (new Insets 20 50 50 20))
                                            ;(seesaw/style-text! (seesaw/text :text "test") :font "Baskerville")
                                            ))

                                (seesaw/action
                                 :name "REPL"
                                 :handler (fn [e]
                                            (let [window (seesaw/to-root e)
                                                  ]
                                            (seesaw/config! window :content (seesaw/left-right-split (seesaw/button :text "testL") (seesaw/config window :content)))
                                            )))

                                 (seesaw/action
                                 :name "full screen"
                                 :handler (fn [e]

                                            ))

                                ])

           ]))


(defn keypress-char [e]
  (.getKeyChar e))

(defn blank-char? [character]
  (cljstr/blank? (str character)))

#_(defn ager [atom-undo-db current-text e]
  (let [pressed-char (keypress-char e)
        existing-doc (reduce str @atom-undo-db)]
    (if (blank-char? pressed-char)
      (swap! atom-undo-db conj current-text))
    (println @atom-undo-db)
      ))

(defn build-content []
  (let [text-pane (seesaw/styled-text
                   ;:listen [:key-pressed (ager (atom {}) text-pane)]
                   ;:multi-line? true
                   :wrap-lines? true
                   :editable? true
                   :margin 10  ;margin in pixels
                   :caret-position 0)]
    text-pane))
        ;(seesaw/scrollable text-pane)))

(defn display [content]
  "General display function, based on lecture slides, builds Jframe for program."
  (let [;core-widget (atom (seesaw/make-widget nil)),
        editor-mode (atom :text),
        editor-context {;:core-widget core-widget,
                        :text-pane content,
                        :link-helper-atom (atom {}),
                        :link-db (atom {}),
                        :editor-mode editor-mode
                        :undo-history-text (atom ())
                        :display-information-atom (atom {:font "Baskerville"})}

        undo-listener (seesaw/listen content
                              ;#{:insert-update}(fn[e]
                                    :key-pressed (fn [e]
                                                 ;(println (keypress-char e))))
                                                 ;(println (seesaw/config content :text))))
                                                  (backend/undo-manager
                                                   editor-mode
                                                   (editor-context :undo-history-text)
                                                   (seesaw/config content :text)
                                                   e)
                                                   ;(println (seesaw/config content :text))
                                                   ))
        ;menu-bar (build-menubar content (atom {}) (atom {}) editor-mode)
        scroll-content (seesaw/scrollable content)
        menu-bar (build-menubar editor-context, scroll-content)


        window (seesaw/frame
                :title "Tangelo"
                :on-close :exit
                :menubar menu-bar
                :content scroll-content
                :width 425 ;850
                :height 550)] ;1100)
    ;(seesaw/config! window :content (seesaw/left-right-split (seesaw/button :text "testL") (seesaw/config window :content))) ;(seesaw/button :text "testR")))
    ;(swap! core-widget (seesaw/make-widget nil))
    (seesaw/show! window)));to enable full screen: (seesaw/toggle-full-screen! window))))

(defn run []
    (display (build-content)))

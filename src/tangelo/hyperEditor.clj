(ns tangelo.hyperEditor
  (:gen-class))

;Note: Example of how a datab with a node would be set up
;{[1,2] {:text "head text" :links {[3,4] "tail text"}}}

(defn build-link-node [head-text tail tail-text]
  "Constructs a formatted representation of link data."
  (-> {}
      (assoc :text head-text)
      (assoc :tails (assoc {} tail tail-text))))

(defn add-link-pair [datab head tail-node]
  (assoc datab head tail-node))

(defn get-head-text [datab head]
  (-> (datab head)
      (:text)))

(defn get-tails [datab head]
  (-> (datab head)
      (:tails)))

(defn insert-new-link [datab link-helper-atom]
  (let [tail-info
        (build-link-node
         (@link-helper-atom :head-text)
         (@link-helper-atom :tail)
         (@link-helper-atom :tail-text))
        head (@link-helper-atom :head)
        ]
  (if (contains? datab head)
    (println "already exists")
    (assoc datab head tail-info))))

(defn text-from-selection [string [head,tail]]
  (subs string head tail))

(text-from-selection "testing 4 u" [0,5])

(subs "ayvyy man" (first [0,5]) (second [0,5]))
;(build-link-node "h text" [3,4] "t text")

(assoc {} [1,2] (build-link-node "h text" [3,4] "t text"))


(defn add-link-head [datab link-head]
  (assoc datab link-head #{}))

(defn add-link-pair-helper [datab link-head link-tail]
  (update-in datab [link-head] #(conj % link-tail)))

(defn add-link-pair [datab link-head link-tail]
  (if (contains? datab link-head)
    (add-link-pair-helper datab link-head link-tail)
    (add-link-pair-helper (assoc datab link-head #{}) link-head link-tail)))

(defn add-new-link [datab link-helper-atom])


#_(defn extract-link-information [datab link-helper-atom]
    "DEPRICATED. Creates new links in datab, after splitting info out of link-helper-atom"
  (let [start (@link-helper-atom :start)
        start-text (@link-helper-atom :start-text)
        end (@link-helper-atom :end)]
        (add-link-pair datab start end)))

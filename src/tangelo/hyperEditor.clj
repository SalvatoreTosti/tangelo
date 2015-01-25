(ns tangelo.hyperEditor
  (:gen-class))

(defn make-link [])

(defn add-link-head [datab link-head]
  (assoc datab link-head #{}))

(defn add-link-pair-helper [datab link-head link-tail]
  (update-in datab [link-head] #(conj % link-tail)))

;link pairs represented as:
;{[head-start,head-end] #{[tail-start,tail end]}}
;Note there can be many 'tail' vectors in the set.

(defn add-link-pair [datab link-head link-tail]
  (if (contains? datab link-head)
    (add-link-pair-helper datab link-head link-tail)
    (add-link-pair-helper (assoc datab link-head #{}) link-head link-tail)))

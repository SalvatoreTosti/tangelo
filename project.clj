(defproject tangelo "0.1.0-SNAPSHOT"
  :description "A text editor written in Clojure."
  :url "https://github.com/SalvatoreTosti/tangelo"
  :license {:name "Artistic License 2.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [seesaw "1.4.4"]
                 [me.raynes/fs "1.4.6"]]
  :main ^:skip-aot tangelo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

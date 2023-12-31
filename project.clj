(defproject hospital "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [cheshire "5.10.1"]
                 [json-schema "0.2.0-RC9"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.xerial/sqlite-jdbc "3.34.0"]]

  :main ^:skip-aot hospital.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

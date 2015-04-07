(ns hipstr.routes.home
  (:require [hipstr.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn foo-response [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<html><body><dt>Bowling much?</dt>"
              "<dd>" (:go-bowling? request) "</dd></body></html>")})
  

(defroutes home-routes
  (GET "/" [] (home-page))
  ;; (GET "/about" request (foo-response request)))
  (GET "/about" [] (about-page))
  ;; (ANY "/req" request (str request))
  (GET "/req" request (str request))
  )
  

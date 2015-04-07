(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defroutes test-routes
  (GET "/req" request (str request)))

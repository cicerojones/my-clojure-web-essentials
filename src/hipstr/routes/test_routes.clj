(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defroutes test-routes
  (GET "/req" request (str request))
  ;; (GET "/req/:val" [val] (str val))
  (GET "/req/:val" [val more] (str val "<br>" more)))

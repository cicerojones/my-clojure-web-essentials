(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defroutes test-routes
  (GET "/req" request (str request))
  ;; (GET "/req/:val" [val] (str val))
  ;; (GET "/req/:val" [val more] (str val "<br>" more)))
  ;; (GET "/req/:val" [val :as request] (str val "<br>" request))
  (GET "/req/:val/:another-val/:and-yet-another"
       [val & remainders] (str val "<br>" remainders)))

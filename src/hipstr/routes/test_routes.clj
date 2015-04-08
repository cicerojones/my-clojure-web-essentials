(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defroutes test-routes
  (POST "/req" request (str request))
  (GET "/req" request (str request))
  ;; (GET "/req/:val" [val] (str val))
  ;; (GET "/req/:val" [val more] (str val "<br>" more)))
  ;; (GET "/req/:val" [val :as request] (str val "<br>" request))
  (GET "/req/:val/:another-val/:and-yet-another"
       [val & remainders] (str val "<br>" remainders))
  ;;use :as to get access to unbound params, and call our route
  ;;handler function
  (GET "/req/key/:key" [key :as request]
       (render-request-val request key))
  )

(defn render-request-val [request-map & [request-key]]
  "Simply returns the value of request-key in request-map, if
  request-key is provided; Otherwise return the request-map. If
  request-key is provided, but not found in the request-map, a message
  indicating as such will be returned."

  (str (if request-key
         (if-let [result ((keyword request-key) request-map)]
           result
           (str request-key " is not a valid key"))
         request-map)))



(ns hipstr.middleware
  (:require [hipstr.session :as session]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
            [noir-exception.core :refer [wrap-internal-error]]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.format :refer [wrap-restful-format]]
            
            ))

;; some problems with using this; the request is being ignored, but
;; still some html is being returned with the desired string question,
;; but no interpolation fanciness

(defn go-bowling? [handler]
  (fn [request]
    (let [request (assoc request :go-bowling? "YES! NOW!")]
      (handler request))))

(defn log-request [handler]
  (fn [req]
    (timbre/debug req)
    (handler req)))

(defn development-middleware [handler]
  (if (env :dev)
    (-> handler
        wrap-error-page
        wrap-exceptions)
    handler))

;; appear that the following middleware failed, whereas the above,
;; luminus version 'works' with about being told about the "go-bowling"
;; see the definition of go-bowling?

;; (def development-middleware
;;   [go-bowling?
;;    wrap-error-page
;;    wrap-exceptions])


(defn production-middleware [handler]
  (-> handler
      
      (wrap-restful-format :formats [:json-kw :edn :transit-json :transit-msgpack])
      (wrap-idle-session-timeout
        {:timeout (* 60 30)
         :timeout-response (redirect "/")})
      (wrap-defaults
        (assoc-in site-defaults [:session :store] (memory-store session/mem)))
      (wrap-internal-error :log #(timbre/error %))))

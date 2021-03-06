(ns hipstr.handler
  (:require [compojure.core :refer [defroutes routes]]
            [hipstr.routes.home :refer [home-routes]]
            [hipstr.routes.test-routes :refer [test-routes]]
            [hipstr.middleware
             :refer [development-middleware production-middleware]]
            [hipstr.session :as session]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [taoensso.timbre.appenders.rolling :as rolling]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [cronj.core :as cronj]))

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  ;; (timbre/set-config!
  ;;  [:appenders :rotor]
  ;;  {:min-level :info
  ;;   :enabled? true
  ;;   :async? false ; should be always false for rotor
  ;;   :max-message-per-msecs nil
  ;;   :fn rotor/appender-fn})

  (timbre/set-config!
   [:appenders :rolling]
   (rolling/make-rolling-appender {:min-level :info}))
  
  ;; (timbre/set-config!
  ;;  [:shared-appender-config :rotor]
  ;;  {:path "hipstr.log" :max-size (* 512 1024) :backlog 10})

  (timbre/set-config!
   [:shared-appender-config :rolling :path] "logs/hipstr.log")
  
  
  (if (env :dev) (parser/cache-off!))
  ;;start the expired session cleanup job
  (cronj/start! session/cleanup-job)
  (timbre/info "\n-=[ hipstr started successfully"
               (when (env :dev) "using the development profile") "]=-"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "hipstr is shutting down...")
  (cronj/shutdown! session/cleanup-job)
  (timbre/info "shutdown complete!"))

;; (def app
;;   (-> (routes
;;         home-routes
;;         base-routes)
;;       development-middleware
;;       production-middleware))

(def app
  (-> (routes
       home-routes test-routes base-routes ) ; not 'app-routes' as in tutorial
      development-middleware
      production-middleware
          ;; :session-options {:timeout (* 60 30)}
          ;; :access-rules []
          ;; :formats [:json-kw :edn]
          ))

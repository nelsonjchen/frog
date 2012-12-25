(ns frog.ftp
  [:use frog.service])

; We would like to be called with
; :host hostname
; :user username
; :pass ftp password

(defservice :ftp
  (println "[FTP] I am an ftp service.")
  (println "[FTP] Hostname: " (ticket :host)) 
  (println "[FTP] Username: " (ticket :user)) 
  (println "[FTP] Password: " (ticket :pass))
  (call-stage ticket))
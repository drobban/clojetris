(ns clojetris.gui)

(use 'seesaw.core
     'seesaw.graphics
     'seesaw.color
     'seesaw.dev)

(def test_prop (atom {:width 20 :height 20}))

;; Paint functions
;; refresh gets triggered from the timer.
(defn refresh [timer_arg]
  (repaint! @main_frame))

(defn paint [context graphics]
  (draw graphics (rect 0 0 (:width @test_prop) (:height @test_prop))
        (style :background :red)))

;; Container with a canvas.
;; main_frame is an atom that gets refreshed in 100FPS
(defn content []
  ;; (vertical-panel :items ["Test 1" "Test 2" "Test 3"]))
  (flow-panel
   :align :left
   :hgap 50
   :vgap 50
   :background "#000000"
   :items [(canvas :background "#FFFFFF" :size [300 :by 500] :paint paint)]))

(def main_frame (atom (frame :title "Clojetris" :size [600 :by 600] :content (content))))

;; Setup gui and timer
(-> @main_frame pack! show!)
(timer refresh :delay 10)

;; Example
;; Test in repl (reset! clojetris.gui/test_prop {:width 100 :height 200})

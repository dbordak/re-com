(ns re-demo.typeahead
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-com.core   :refer [h-box v-box box gap line typeahead label checkbox radio-button slider title p]]
            [re-com.typeahead   :refer [typeahead-args-desc]]
            [re-demo.utils :refer [panel-title title2 args-table github-hyperlink status-text]]
            [reagent.core  :as    reagent]
            [cljs.core.async  :refer    [timeout]]))

(declare states-data)

(defn typeahead-demo
  []
  (let [text-val        (reagent/atom "")
        regex           (reagent/atom nil)
        regex999        #"^(\d{0,2})$|^(\d{0,2}\.\d{0,1})$"
        status          (reagent/atom nil)
        status-icon?    (reagent/atom false)
        status-tooltip  (reagent/atom "")
        disabled?       (reagent/atom false)
        change-on-blur? (reagent/atom true)
        slider-val      (reagent/atom 4)
        data-source     (fn [s callback]
                          (go
                            (<! (timeout 500))
                            (callback
                             s
                             (for [r states-data
                                   :when (re-find (re-pattern (str "(?i)" s)) r)]
                               r))))
        ]
    (fn
      []
      [v-box
       :size     "auto"
       :gap      "10px"
       :children [[panel-title  "[typeahead ... ]"
                   "src/re_com/typeahead.cljs"
                   "src/re_demo/typeahead.cljs"]

                  [h-box
                   :gap      "100px"
                   :children [[v-box
                               :gap      "10px"
                               :width    "450px"
                               :children [[title2 "Notes"]
                                          [status-text "Stable"]
                                          [p "Text entry components."]
                                          [p "The " [:code ":on-change"] " function will be called either after each character is entered or on blur."]
                                          [p "Input warnings and errors can be indicated visually by border colors and icons."]
                                          [args-table typeahead-args-desc]]]
                              [v-box
                               :gap      "10px"
                               :children [[title2 "Demo"]
                                          [h-box
                                           :gap "40px"
                                           :children [[v-box
                                                       :children [[label :label "[typeahead ... ]"]
                                                                  [gap :size "5px"]
                                                                  [typeahead
                                                                   :model            text-val
                                                                   :data-source      data-source
                                                                   :status           @status
                                                                   :status-icon?     @status-icon?
                                                                   :status-tooltip   @status-tooltip
                                                                   :width            "300px"
                                                                   :placeholder      "Choose state"
                                                                   :on-change        #(reset! text-val %)
                                                                   :change-on-blur?  change-on-blur?
                                                                   :disabled?        disabled?]]]
                                                      [v-box
                                                       :gap      "15px"
                                                       :children [[title :level :level3 :label "Callbacks"]
                                                                  [h-box
                                                                   :align    :center
                                                                   :gap      "5px"
                                                                   :children [[:code ":on-change"]
                                                                              " last called with this value: "
                                                                              [:span.bold (if @text-val @text-val "nil")]]]
                                                                  [title :level :level3 :label "Parameters"]
                                                                  [v-box
                                                                   :children [[box :align :start :child [:code ":change-on-blur?"]]
                                                                              [radio-button
                                                                               :label     "false - Call on-change on every keystroke"
                                                                               :value     false
                                                                               :model     @change-on-blur?
                                                                               :on-change #(reset! change-on-blur? false)
                                                                               :style     {:margin-left "20px"}]
                                                                              [radio-button
                                                                               :label     "true - Call on-change only on blur or Enter key (Esc key resets text)"
                                                                               :value     true
                                                                               :model     @change-on-blur?
                                                                               :on-change #(reset! change-on-blur? true)
                                                                               :style     {:margin-left "20px"}]]]
                                                                  [v-box
                                                                   :children [[box :align :start :child [:code ":status"]]
                                                                              [radio-button
                                                                               :label     "nil/omitted - normal input state"
                                                                               :value     nil
                                                                               :model     @status
                                                                               :on-change #(do
                                                                                             (reset! status nil)
                                                                                             (reset! status-tooltip ""))
                                                                               :style {:margin-left "20px"}]
                                                                              [radio-button
                                                                               :label     ":warning - border color becomes orange"
                                                                               :value     :warning
                                                                               :model     @status
                                                                               :on-change #(do
                                                                                             (reset! status :warning)
                                                                                             (reset! status-tooltip "Warning tooltip - this (optionally) appears when there are warnings on typeahead components."))
                                                                               :style     {:margin-left "20px"}]
                                                                              [radio-button
                                                                               :label     ":error - border color becomes red"
                                                                               :value     :error
                                                                               :model     @status
                                                                               :on-change #(do
                                                                                             (reset! status :error)
                                                                                             (reset! status-tooltip "Error tooltip - this (optionally) appears when there are errors on typeahead components."))
                                                                               :style     {:margin-left "20px"}]]]
                                                                  [h-box
                                                                   :align :start
                                                                   :gap      "5px"
                                                                   :children [[checkbox
                                                                               :label     [:code ":status-icon?"]
                                                                               :model     status-icon?
                                                                               :on-change (fn [val]
                                                                                            (reset! status-icon? val))]
                                                                              [:span " (notice the tooltips on the icons)"]]]

                                                                  [checkbox
                                                                   :label     [box :align :start :child [:code ":disabled?"]]
                                                                   :model     disabled?
                                                                   :on-change (fn [val]
                                                                                (reset! disabled? val))]
                                                                  ]]]]]]]]]])))


;; core holds a reference to panel, so need one level of indirection to get figwheel updates
(defn panel
  []
  [typeahead-demo])

(def ^:private states-data
  ["Alabama"
   "Alaska"
   "Arizona"
   "Arkansas"
   "California"
   "Colorado"
   "Connecticut"
   "Delaware"
   "Florida"
   "Georgia"
   "Hawaii"
   "Idaho"
   "Illinois"
   "Indiana"
   "Iowa"
   "Kansas"
   "Kentucky"
   "Louisiana"
   "Maine"
   "Maryland"
   "Massachusetts"
   "Michigan"
   "Minnesota"
   "Mississippi"
   "Missouri"
   "Montana"
   "Nebraska"
   "Nevada"
   "New Hampshire"
   "New Jersey"
   "New Mexico"
   "New York"
   "North Carolina"
   "North Dakota"
   "Ohio"
   "Oklahoma"
   "Oregon"
   "Pennsylvania"
   "Rhode Island"
   "South Carolina"
   "South Dakota"
   "Tennessee"
   "Texas"
   "Utah"
   "Vermont"
   "Virginia"
   "Washington"
   "West Virginia"
   "Wisconsin"
   "Wyoming"])
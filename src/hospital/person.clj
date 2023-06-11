(ns hospital.person)

(defn get-bmi
  "Divides the kg weight with the height in meters to the power of 2."
  [{:keys [weight height]}]
  (if (zero? height)
    (throw (IllegalArgumentException. "Height cannot be zero."))
    (/ weight (* height height))))

(defn add-bmi
  "Adds the bmi field to the person record."
  [person]
  (assoc person :bmi (get-bmi person)))

(defn get-total-cholesterol
  "Gets the total cholesterol for the person by adding their hdl and ldl."
  [{:keys [hdl ldl]}]
  (+ hdl ldl))

(defn add-total-cholesterol
  "Adds the total-cholesterol as a field to the person record."
  [person]
  (assoc person :total-cholesterol (get-total-cholesterol person)))

(defn get-risk-score
  "Gets the risk score by taking the person's bmi and adding it to their total-cholesterol."
  [{:keys [bmi total-cholesterol]}]
  (+ bmi total-cholesterol))

(defn add-risk-score
  "Adds the risk score field to the person record."
  [person]
  (assoc person :risk-score (get-risk-score person)))

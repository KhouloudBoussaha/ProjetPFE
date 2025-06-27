from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np

app = Flask(__name__)
CORS(app, resources={r"/predict": {"origins": ["http://localhost:8080", "http://localhost:4200"]}})

# Charger le modèle .pkl (adapté à ton fichier)
model = joblib.load("decision_tree.pkl")
print("Modèle chargé avec succès !")

@app.route("/predict", methods=["POST"])
def predict():
    # Récupérer les données JSON de la requête
    data = request.get_json()
    features = np.array(data["features"]).reshape(1, -1)
    
    # Faire la prédiction
    prediction = model.predict(features)
    
    # Retourner la prédiction sous forme de JSON
    return jsonify({"prediction": prediction.tolist()})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
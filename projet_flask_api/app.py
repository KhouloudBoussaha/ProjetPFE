 
from flask import Flask, request, jsonify
from flask_cors import CORS
import pickle
import numpy as np

app = Flask(__name__)
CORS(app)

# Charger le modèle
with open('decision_tree.pkl', 'rb') as f:
    model = pickle.load(f)

# Charger le label encoder
with open('label_encoder_pca.pkl', 'rb') as f:
    label_encoder_pca = pickle.load(f)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        print("📥 Requête reçue")
        print("Headers:", request.headers)
        print("Body brut:", request.data)

        data = request.get_json(force=True)

        if not data:
            return jsonify({'error': 'JSON vide ou malformé'}), 400

        if 'incident_type_encoded' not in data or 'impact_encoded' not in data:
            return jsonify({'error': 'Champs requis: incident_type_encoded et impact_encoded'}), 400

        features = np.array([[int(data['incident_type_encoded']), int(data['impact_encoded'])]])
        prediction_encoded = model.predict(features)[0]
        prediction_label = label_encoder_pca.inverse_transform([prediction_encoded])[0]

        return jsonify({
            'pca_encoded': int(prediction_encoded),
            'pca_label': prediction_label
        })

    except Exception as e:
        print("❌ Erreur : ", str(e))
        return jsonify({'error': str(e)}), 400


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)

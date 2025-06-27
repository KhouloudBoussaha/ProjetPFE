import joblib

# Charger le fichier .pkl
model = joblib.load("decision_tree.pkl")

# Vérifier si le modèle est entraîné
is_fitted = hasattr(model, "n_features_in_")
print(f"Modèle chargé : {type(model).__name__}")
print(f"Est-il entraîné ? {is_fitted}")

# Afficher quelques informations (si entraîné)
if is_fitted:
    print(f"Nombre de features attendues : {model.n_features_in_}")
else:
    print("Le modèle n'a pas été entraîné. Vérifiez votre fichier .pkl.")
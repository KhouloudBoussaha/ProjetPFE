# Étape 1 : build Angular
FROM node:18 AS build
WORKDIR /app

# Copier uniquement les fichiers de dépendances d'abord
COPY package*.json ./

# Installer Angular CLI v16 + dépendances locales
RUN npm install -g @angular/cli@16 && npm install

# Copier tout le reste du projet (code source Angular, angular.json, etc.)
COPY . .

# Builder l'application
RUN npm run build --configuration production

# Étape 2 : servir avec NGINX
FROM nginx:stable-alpine

# Copier le build généré vers le dossier NGINX
COPY --from=build /app/dist/continuix /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

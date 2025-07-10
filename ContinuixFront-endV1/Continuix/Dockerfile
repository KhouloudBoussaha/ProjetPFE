# Étape 1 : Build Angular
FROM node:18 AS build
WORKDIR /app

# Installer dépendances
COPY package*.json ./
RUN npm install -g @angular/cli@16 && npm install

# Copier tout le code Angular
COPY . .

# Build avec le nom correct du projet Angular
RUN ng build Continuix --configuration production

# Étape 2 : NGINX pour servir l'app
FROM nginx:stable-alpine

# Copier les fichiers compilés (note : dist/**continuix**)
COPY --from=build /app/dist/continuix /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

# Étape 1 : build Angular
FROM node:18 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --configuration production

# Étape 2 : servir avec NGINX
FROM nginx:stable-alpine
COPY --from=build /app/dist/continuix /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

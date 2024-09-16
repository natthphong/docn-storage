#./mvnw  clean pacakge
docker build --platform linux/amd64 -t app:dev .
doctl registry login
docker tag app:dev registry.digitalocean.com/ect-app-api/app:dev
docker push registry.digitalocean.com/ect-app-api/app:dev
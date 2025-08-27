set -e

#TODO: update after creating client

echo "Building project"
cd api
mvn clean install
cd ..

echo "running docker-compose"
docker-compose up --build
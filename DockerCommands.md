docker-compose ps -a
docker-compose up -d
docker-compose logs app
docker-compose down

docker images
docker login

docker build -t pavvel/bitcoincharts:latest .
docker tag bitcoincharts:latest pavvel/bitcoincharts:latest
docker push pavvel/bitcoincharts:latest
docker pull pavvel/bitcoincharts:latest
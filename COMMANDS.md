docker-compose ps -a
docker-compose up -d
docker-compose logs app
docker-compose down

docker images
docker login

docker build -t pavvel/bitcoin_charts:0.1 -t us-east5-docker.pkg.dev/bitcoin-charts/bitcoin-charts/bitcoin_charts:0.1 .
docker tag bitcoin_charts:0.1 pavvel/bitcoin_charts:0.1
docker push pavvel/bitcoin_charts:0.1
docker pull pavvel/bitcoin_charts:0.1
docker run -d -p 8080:8080 --name bitcoin_charts pavvel/bitcoin_charts:0.1
docker ps -a
docker stop bitcoin_charts

gcloud components install gke-gcloud-auth-plugin
gke-gcloud-auth-plugin --version

gcloud auth login
gcloud auth configure-docker us-east5-docker.pkg.dev
docker tag pavvel/bitcoin_charts:0.1 us-east5-docker.pkg.dev/bitcoin-charts/bitcoin-charts/bitcoin_charts:0.1
docker push us-east5-docker.pkg.dev/bitcoin-charts/bitcoin-charts/bitcoin_charts:0.1
docker pull us-east5-docker.pkg.dev/bitcoin-charts/bitcoin-charts/bitcoin_charts:0.1
docker run -d -p 8080:8080 --name bitcoin_charts us-east5-docker.pkg.dev/bitcoin-charts/bitcoin-charts/bitcoin_charts:0.1

gcloud artifacts repositories list --project bitcoin-charts

gcloud container clusters get-credentials bitcoin-charts --region us-east5 --project bitcoin-charts
gcloud container clusters list --project bitcoin-charts

kubectl apply -f deployment/deployment.yaml
kubectl apply -f deployment/service.yaml

kubectl get pods --selector=app=bitcoin-charts
kubectl logs 

kubectl get services
kubectl delete pod 

gradle build --no-daemon
gradle --stop

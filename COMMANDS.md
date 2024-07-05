docker-compose ps -a
docker-compose up -d
docker-compose logs app
docker-compose down

docker images
docker login

docker build -t bitcoin_charts:0.1 .
docker tag bitcoin_charts:0.1 pavvel/bitcoin_charts:0.1
docker push pavvel/bitcoin_charts:0.1
docker pull pavvel/bitcoin_charts:0.1
docker run -d -p 8080:8080 --name bitcoin_charts bitcoin_charts:0.1
docker ps -a

gcloud components install gke-gcloud-auth-plugin
gke-gcloud-auth-plugin --version

gcloud auth login
gcloud auth configure-docker us-east5-docker.pkg.dev
docker tag pavvel/bitcoin_charts:latest us-east5-docker.pkg.dev/bitcoin_charts/bitcoin_charts/bitcoin_charts:0.1
docker push us-east5-docker.pkg.dev/bitcoin_charts/bitcoin_charts/bitcoin_charts:0.1
docker pull us-east5-docker.pkg.dev/bitcoin_charts/bitcoin_charts/bitcoin_charts:0.1
gcloud artifacts repositories list --project bitcoin_charts

gcloud container clusters get-credentials bitcoin_charts --region us-east5 --project bitcoin_charts
gcloud container clusters list --project bitcoin_charts

kubectl apply -f deployment/deployment.yaml
kubectl apply -f deployment/service.yaml

kubectl get pods
kubectl get services
kubectl delete pod --selector=app=bitcoin_charts

gradle build --no-daemon
gradle --stop

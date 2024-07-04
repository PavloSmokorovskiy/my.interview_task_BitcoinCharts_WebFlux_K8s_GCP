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

gcloud components install gke-gcloud-auth-plugin
gke-gcloud-auth-plugin --version

gcloud auth login
gcloud auth configure-docker us-east5-docker.pkg.dev
docker tag pavvel/bitcoincharts:latest us-east5-docker.pkg.dev/bitcoincharts/bitcoincharts/bitcoincharts:latest
docker push us-east5-docker.pkg.dev/bitcoincharts/bitcoincharts/bitcoincharts:latest
docker pull us-east5-docker.pkg.dev/bitcoincharts/bitcoincharts/bitcoincharts:latest
gcloud artifacts repositories list --project bitcoincharts

gcloud container clusters get-credentials bitcoincharts --region us-east5 --project bitcoincharts
gcloud container clusters list --project bitcoincharts

kubectl apply -f deployment/deployment.yaml
kubectl apply -f deployment/service.yaml

kubectl get pods
kubectl get services
kubectl delete pod --selector=app=bitcoincharts



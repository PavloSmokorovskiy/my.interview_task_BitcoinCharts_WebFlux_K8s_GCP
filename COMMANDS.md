docker-compose ps -a
docker-compose up -d
docker-compose logs app
docker-compose down

docker images
docker login

docker build -t pavvel/bitcoin_charts:0.1 -t us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1 .
docker tag bitcoin_charts:0.1 pavvel/bitcoin_charts:0.1
docker push pavvel/bitcoin_charts:0.1
docker pull pavvel/bitcoin_charts:0.1
docker run -d -p 8080:8080 --name bitcoin_charts pavvel/bitcoin_charts:0.1
docker ps -a
docker stop bitcoin_charts

gcloud auth revoke
gcloud auth list
gcloud auth login
gcloud config set project bitcoincharts

gcloud components install gke-gcloud-auth-plugin
gke-gcloud-auth-plugin --version

gcloud auth login
gcloud auth configure-docker us-east5-docker.pkg.dev

docker push us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1
docker pull us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1
docker run -d -p 8080:8080 --name bitcoin_charts us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1

gcloud artifacts repositories list --project bitcoincharts

gcloud container clusters get-credentials bitcoin-charts --region us-east5 --project bitcoincharts
gcloud container clusters list --project bitcoincharts

kubectl apply -f deployment/deployment.yaml
kubectl apply -f deployment/service.yaml

kubectl get pods --selector=app=bitcoin-charts
kubectl logs 

kubectl get services
kubectl delete pod 

docker inspect --format='{{.Architecture}}' us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1
kubectl get nodes -o jsonpath='{.items[*].status.nodeInfo.architecture}'

docker buildx create --name mymultiarchbuilder --driver docker-container --use
docker buildx inspect --bootstrap
docker buildx build --platform linux/amd64,linux/arm64 -t us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1 . --push
docker buildx build --platform linux/amd64,linux/arm64 -t pavvel/bitcoin_charts:0.1 -t us-east5-docker.pkg.dev/bitcoincharts/bitcoin-charts/bitcoin_charts:0.1 . --push

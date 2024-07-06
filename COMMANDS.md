docker-compose ps -a
docker-compose up -d
docker-compose logs app
docker-compose down

docker images
docker login

docker build -t pavvel/price_charts:0.1 -t us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1 .
docker tag price_charts:0.1 pavvel/price_charts:0.1
docker push pavvel/price_charts:0.1
docker pull pavvel/price_charts:0.1
docker run -d -p 8080:8080 --name price_charts pavvel/price_charts:0.1
docker ps -a
docker stop price_charts

gcloud auth revoke
gcloud auth list
gcloud auth login
gcloud config set project pricecharts

gcloud components install gke-gcloud-auth-plugin
gke-gcloud-auth-plugin --version

gcloud auth login
gcloud auth configure-docker us-east5-docker.pkg.dev

docker push us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1
docker pull us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1
docker run -d -p 8080:8080 --name price_charts us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1

gcloud artifacts repositories list --project pricecharts

gcloud container clusters get-credentials price-charts --region us-east5 --project pricecharts
gcloud container clusters list --project pricecharts


gcr.io/pricecharts/helloworld-image:latest
docker tag pavvel/price_charts:0.1 us-east5-docker.pkg.dev/pricecharts/price-charts/price-charts:0.1
docker push us-east5-docker.pkg.dev/pricecharts/price-charts/price-charts:0.1

kubectl apply -f deployment/deployment.yaml
kubectl apply -f deployment/service.yaml

kubectl get pods --selector=app=price-charts
kubectl logs 

kubectl get services
kubectl delete pod 

docker inspect --format='{{.Architecture}}' us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1
kubectl get nodes -o jsonpath='{.items[*].status.nodeInfo.architecture}'

docker buildx create --name mymultiarchbuilder --driver docker-container --use
docker buildx inspect --bootstrap
docker buildx build --platform linux/amd64,linux/arm64 -t us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1 . --push
docker buildx build --platform linux/amd64,linux/arm64 -t pavvel/price_charts:0.1 -t us-east5-docker.pkg.dev/pricecharts/price-charts/price_charts:0.1 . --push

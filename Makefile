IMAGE_REPO = andreysemetchkov/rate-limiter
TAG = "latest"
build-local:
	echo "Building image"
	docker build -t rate-limiter:latest --platform linux/amd64 .

dist-local:
	echo "Pushing latest image to kind repo"
	export KIND_EXPERIMENTAL_PROVIDER=podman
	podman save rate-limiter:latest --format oci-archive -o ~/code/playground/rate-limiter-image
	kind load image-archive ~/code/playground/rate-limiter-image --name home-cluster

deploy-local: build dist
	kubectx kind-home-cluster
	kubectl create namespace app 
	kubectl apply -n app -f deployment.yaml

run: build
	docker run rate-limiter:latest	

helm-deps:
	helm dependency build k8s

helm-lint:
	helm lint k8s

deploy: helm-deps helm-lint
	helm upgrade --install rate-limiter k8s \
	  --namespace app --create-namespace \
	  --set image.tag=$(TAG)
	  --set image.repository=$(IMAGE_REPO)
	
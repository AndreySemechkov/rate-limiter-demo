build:
	echo "Building image"
	podman build -t rate-limiter:latest --platform linux/amd64 .

dist:
	echo "Pushing latest image to kind repo"
	export KIND_EXPERIMENTAL_PROVIDER=podman
	podman save rate-limiter:latest --format oci-archive -o ~/code/playground/rate-limiter-image
	kind load image-archive ~/code/playground/rate-limiter-image --name home-cluster

run: build dist
	# when on kind context
	kubectl apply -f deployment.yaml
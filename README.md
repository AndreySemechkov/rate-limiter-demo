######

Welcome to my demo playground app!

Its a simple Spring Boot web app that decides wheter to rate limit a url based on a threshold config.
The app is containerized, the build & run scripts are in the Makefile.

To build & run using podman (on linux x86):
```
make run
```

To build & deploy to a local kind k8s:
```
make deploy
```

To test API on local k8s:
```
kubectl port-forward <POD_NAME> 9090:9090
```

Access API documentation on a browser via swagger visit:

http://localhost:9090/swagger-ui/index.html

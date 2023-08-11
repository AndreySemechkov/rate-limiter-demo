######

Welcome to my playground app!

Prerequisites to run the app are java 18 and make

To build & run (on linux x86):
```
make run
```

To test on local k8s:

```
kubectl port-forward <POD_NAME> 9090:9090
```

Access API documentation via swagger here:

http://localhost:9090/swagger-ui/index.html

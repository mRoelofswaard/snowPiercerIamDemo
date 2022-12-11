#!/bin/bash

if [ "$#" -eq 1 ]; then
    MY_IP=$1
else
    MY_IP=$(ip route get 1 | awk '{print $7}')
fi

# create docker image and push it
cp ../target/*.jar .
docker build -t k3s-local.markey-marc.nl/snowpiercer:latest .
docker push k3s-local.markey-marc.nl/snowpiercer:latest
rm *.jar

# create deployment
kubectx k3d-k3s-default
kubectl create namespace workshop --dry-run=client -o yaml | kubectl apply -f -
kubens workshop

# Containers in snowpiercer deployment must be able to access k3s-local.markey-marc.nl
cat snowpiercer-k3d.yaml | sed 's/MY_IP_ADDRESS/'$MY_IP'/g' | kubectl apply -f -

# remove old pods..
kubectl delete pods -l=app=snowpiercer

echo "See to container logs with this command:"
echo "kubectl logs -f -l=app=snowpiercer"
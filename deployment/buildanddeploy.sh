#!/bin/bash

FLAG_MAVEN=false
for var in "$@"
do
    if [[ $var=="maven" ]]; then
        FLAG_MAVEN=true
    fi
done

# if flag maven then build software
if $FLAG_MAVEN; then
   current_dir=$(pwd)
   cd ..
   mvn clean install -Dmaven.test.skip=true
   cd $current_dir
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
MY_IP=$(ip route get 1 | awk '{print $7}')
cat snowpiercer-k3d.yaml | sed 's/MY_IP_ADDRESS/'$MY_IP'/g' | kubectl apply -f -

# remove old pods..
kubectl delete pods -l=app=snowpiercer

echo "See to container logs with this command:"
echo "kubectl logs -f -l=app=snowpiercer"
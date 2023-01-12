#!/bin/bash
# mvn打包项目
mvn clean package -DskipTests

repository=registry.cn-shenzhen.aliyuncs.com
aliyun_user=csb_dev_test@test.aliyunid.com
aliyun_password=sca@1234
namespace=sca-demo
image=default-provider-version-example
#version=`git branch | sed -n '/\* /s///p'`
version=develop

docker buildx inspect --bootstrap
docker buildx build -t $repository/$namespace/$image:$version --platform linux/amd64 -o type=docker .

docker login $repository --username $aliyun_user --password $aliyun_password
docker push $repository/$namespace/$image:$version

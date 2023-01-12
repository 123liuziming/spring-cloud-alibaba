FROM reg.docker.alibaba-inc.com/cnstack/cnstack-java:1.0-beta
MAINTAINER jingfeng.xjf <jingfeng.xjf@alibaba-inc.com>

ADD bin /home/admin/bin

ENV JAR_FILE=/home/admin/default-provider-version-example/default-provider-version-example.jar \
    JAR_OPTS="-Dloader.path=/home/admin/default-provider-version-example" \
    TZ=Asia/Shanghai

ENV TERM xterm

RUN mkdir -p /home/admin/default-provider-version-example && \
    mkdir -p /home/admin/default-provider-version-example/logs

COPY target/*.jar /home/admin/default-provider-version-example/default-provider-version-example.jar

RUN chown -R admin:admin /home/admin  && chmod -R 755 /home/admin
RUN export PATH=$PATH:/home/admin/bin

ENTRYPOINT ["/bin/bash", "/home/admin/bin/start.sh"]
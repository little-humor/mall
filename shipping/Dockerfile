FROM openjdk
MAINTAINER humor <guxingyin@live.cn>
ARG JAR_FILE
ENV PROFILE default
ADD target/${JAR_FILE} /opt/app.jar
EXPOSE 8080
ENTRYPOINT java ${JAVA_OPTS} -Duser.timezone=Asia/beijing -Dfile.encoding=UTF-8 \
            -Dspring.profiles.active=${PROFILE} -jar /opt/app.jar
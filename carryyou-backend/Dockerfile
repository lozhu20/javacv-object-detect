FROM java:8
WORKDIR /app
ARG JAR_FILE=carryyou-backend-1.0.0.jar
ADD target/${JAR_FILE} app.jar

VOLUME /data
ARG yoloCfgPath="/data/yolov4.cfg"
ARG yoloWeightsPath="/data/yolov4.weights"
ARG yoloCoconamesPath="/data/coco.names"

ENV dbURL=""
ENV dbUsername=""
ENV dbPassword=""
ENV dbDriver=""

ENV tokenExpireAfterMinutes=0

ENV imageBasePath="/data/images"

ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=${dbURL}", "-Dspring.datasource.username=${dbUsername}", "-Dspring.datasource.password=${dbPassword}", "-Dspring.datasource.driver-class-name=${dbDriver}", "-Dtoken.expire.after.minutes=${tokenExpireAfterMinutes}", "-Dimage.base.path=${imageBasePath}", "-Dyolo.yolo-cfg-path=${yoloCfgPath}", "-Dyolo.yolo-weights-path=${yoloWeightsPath}", "-Dyolo.yolo-coconames-path=${yoloCoconamesPath}", "app.jar"]
EXPOSE 8000

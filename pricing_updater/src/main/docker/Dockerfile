FROM openjdk:14-alpine
#RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/pricing-updater
ADD @project.build.finalName@.jar /usr/local/pricing-updater/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh

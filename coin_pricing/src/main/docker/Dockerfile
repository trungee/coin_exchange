FROM openjdk:14-alpine
#RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/coin-pricing
ADD @project.build.finalName@.jar /usr/local/coin-pricing/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
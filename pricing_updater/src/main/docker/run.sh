#!/bin/sh

echo "********************************************************"
echo "Starting Pricing Updater Service with Configuration Service"
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE -jar /usr/local/pricing-updater/@project.build.finalName@.jar

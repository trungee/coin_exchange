#!/bin/sh

echo "********************************************************"
echo "Starting Coin Pricing Service with Configuration Service"
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE -jar /usr/local/coin-pricing/@project.build.finalName@.jar

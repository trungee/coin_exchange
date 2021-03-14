# Introduction
Welcome to Crypto Currency Exchange Spring Microservices  

1.  A Coin Pricing Service that will handle the request to calculate the equivalent in currency (USD, NZD, ...) for the desired amount of Bitcoin (BTC) with realtime updated price and fee.
2.  A Pricing Updater that will provide latest profit and spot price to the system by periodic sync spot prices from api.coinbase.com
3.  A Kafka broker that receives message from Pricing Updater and distributes events to all Coin Pricing Service instants

# Software needed
1.	[Apache Maven] (http://maven.apache.org). Extremely popular, Maven is build tool in use in the Java ecosystem.
2.	[Docker] (http://docker.com). 
3.	[Git Client] (http://git-scm.com).

# Building the Docker Images for Coin Exchange

To build the code of Coin Exchange as a docker image, open a command-line window and change to the directory coin_exchange

Run the following maven command

   ```bash
   mvn clean package docker:build
   ```

 Running the above command at the root of the project directory will build all of the projects.

# Running the Kafka broker

To start the docker image, issue the following docker-compose command

   ```bash
   docker-compose -f ./kafka-docker/docker-compose.yml -p coin_exchange up -d
   ```

# Running the Pricing Updater service
Open a command-line window and change to the directory coin_exchange/pricing_updater

Run the following maven command
   ```bash
   mvn clean spring-boot:run
   ```

# Running the Coin Pricing service
Open a command-line window and change to the directory coin_exchange/coin_pricing

Run the following maven command
   ```bash
   mvn clean spring-boot:run
   ```

As of now, docker-compose for coin_pricing & pricing_update service is in-progress since I could not connect my services to Kafka broker from docker containers.

# Consuming the services by Rest API calls
Curl commands:
1. Get spot price for buying:
   ```bash
   curl --request GET \
   --url curl http://localhost:8085/api/prices/BTC-NZD/buy?amount=1
   ```

2. Get spot price for selling:
   ```bash
   curl --request GET \
   --url http://localhost:8085/api/prices/BTC-NZD/buy?amount=1
   ```
## Overview

This has two modules "command-app" and "query-app"

## Command app
A simple application to create a SpaceCraft resource. It has an aggregate,
a command and a command handler and exposes a REST endpoint to create a space craft.

After successfully create a SpaceCraft object it publishes an event to the amqp "Exchange".

## Query app
A simple application that printouts the received SpaceCraftCreatedEvent name to the console.
It has EventHandler and configured to listen to the amqp queue.

### Prerequsites
* RabbitMQ
* Mongodb

Running locally with default users and permissions

### How to build and run

```
./gradlew build
```

Run command-app using `../gradlew run` inside "command-app" directory. [http://localhost:8080/health](http://localhost:8080/health)

Run query-app using `../gradlew run` inside "query-app" directory. [http://localhost:8081/health](http://localhost:8081/health)

### Testing

```
curl -X POST \
  http://localhost:8080/create \
  -H 'content-type: application/json' \
  -d '{
	"name": "MyAwesomeSpaceCraft"
}'
```

View the console of "query-app" you should see
**spaceCraftCreatedEvent.getName() = MyAwesomeSpaceCraft**

## TODO
* Use embedded mongodb
* Persist the event received on the query-app
* One more flow to update the aggregate e.g. Apply space craft modules.
* ...
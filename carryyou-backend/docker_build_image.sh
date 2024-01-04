#!/usr/bin/env bash

echo "🛠️ start work flow!"

echo "🧹 package cleaning.."
mvn clean

echo "📦 package application.."
mvn package -DskipTests

echo "🗑️ removing local image.."
docker rmi chenxii81/carryyou-backend:1.0.0

echo "🐳 building docker image.."
docker build -t chenxii81/carryyou-backend:1.0.0 .

echo "🎉 all works finished!"

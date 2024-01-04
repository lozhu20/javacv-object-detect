#!/usr/bin/env bash

echo "🐳 start carryyou-backend docker container.."

echo "🗑️ remove container if exists.."
docker rm carryyou-backend

echo "🧑‍💻 start application.."
docker run -dp 8000:8000 --name=carryyou-backend -v /home/longkun/downloads/yolo:/data -e dbURL=jdbc:postgresql://localhost:5432/db_carryyou -e dbUsername=postgres -e dbPassword=secret -e dbDriver=org.postgresql.Driver -e tokenExpireAfterMinutes=10 chenxii81/carryyou-backend:1.0.0

echo "🎉 carryyou-backend is running now!"

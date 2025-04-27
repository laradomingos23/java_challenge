#!/bin/bash

echo "Aguardando Kafka ficar disponível..."
while ! nc -z kafka 9092; do
  sleep 1
done

echo "Kafka disponível. Criando tópicos..."

kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists --topic operations-request --partitions 1 --replication-factor 1
kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists --topic operations-response --partitions 1 --replication-factor 1

echo "Tópicos criados com sucesso."
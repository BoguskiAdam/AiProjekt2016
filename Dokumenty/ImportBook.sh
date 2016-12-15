#!/bin/bash
echo "Importowanie Book - Start"
tar -xf Book.json.tar.gz
mongoimport --db AiProjekt --collection book --file Book.json
rm Book.json
echo "Importowanie Book - Stop"

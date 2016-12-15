#!/bin/bash
echo "Importowanie BookRatings - Start"
tar -xf BookRatings.json.tar.gz
mongoimport --db AiProjekt --collection book --file BookRatings.json
rm BookRatings.json
echo "Importowanie BookRatings - Stop"

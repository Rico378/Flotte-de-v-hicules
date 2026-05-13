#!/bin/bash

echo "=== Compilation du projet ==="
cd "$(dirname "$0")"

mkdir -p bin

javac -d bin -sourcepath src \
    src/model/*.java \
    src/util/*.java \
    src/controller/*.java \
    src/view/*.java

if [ $? -ne 0 ]; then
    echo "Erreur de compilation!"
    exit 1
fi

echo "=== Compilation réussie ==="
echo ""
echo "=== Lancement de l'application ==="

java -cp bin view.VueApplication

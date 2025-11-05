#!/bin/bash

echo "================================"
echo "  API Test Automation - Desafio"
echo "================================"
echo

echo "[INFO] Verificando Java..."
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java não encontrado. Instale Java 17 ou superior."
    exit 1
fi
java -version

echo
echo "[INFO] Verificando Maven..."
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven não encontrado. Instale Maven 3.8 ou superior."
    exit 1
fi
mvn -version

echo
echo "[INFO] Limpando e compilando projeto..."
mvn clean compile test-compile

echo
echo "[INFO] Executando testes..."
mvn test

echo
echo "[INFO] Gerando relatório Allure..."
mvn allure:report

echo
echo "[SUCCESS] Execução concluída!"
echo "[INFO] Para visualizar o relatório: mvn allure:serve"
echo
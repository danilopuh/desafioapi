@echo off
echo ================================
echo   API Test Automation - Desafio
echo ================================
echo.

echo [INFO] Verificando Java...
java -version
if errorlevel 1 (
    echo [ERROR] Java nao encontrado. Instale Java 17 ou superior.
    pause
    exit /b 1
)

echo.
echo [INFO] Verificando Maven...
mvn -version
if errorlevel 1 (
    echo [ERROR] Maven nao encontrado. Instale Maven 3.8 ou superior.
    pause
    exit /b 1
)

echo.
echo [INFO] Limpando e compilando projeto...
mvn clean compile test-compile

echo.
echo [INFO] Executando testes...
mvn test

echo.
echo [INFO] Gerando relatorio Allure...
mvn allure:report

echo.
echo [SUCCESS] Execucao concluida!
echo [INFO] Para visualizar o relatorio: mvn allure:serve
echo.
pause
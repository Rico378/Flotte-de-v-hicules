@echo off

chcp 65001 >nul

set UI_SCALE=2.0

echo === Compilation du projet ===
cd /d "%~dp0"

where javac >nul 2>&1
if errorlevel 1 (
    echo ERREUR: javac introuvable. Installe un JDK et ajoute-le au PATH.
    echo Astuce: java seul ne suffit pas, il faut javac.
    pause
    exit /b 1
)

where java >nul 2>&1
if errorlevel 1 (
    echo ERREUR: java introuvable. Verifie l'installation du JDK/JRE et le PATH.
    pause
    exit /b 1
)

if not exist bin mkdir bin

REM Compiler tous les fichiers Java
javac -encoding UTF-8 -d bin -sourcepath src ^
    src/model/*.java ^
    src/util/*.java ^
    src/controller/*.java ^
    src/view/*.java

if errorlevel 1 (
    echo Erreur de compilation!
    pause
    exit /b 1
)

echo === Compilation réussie ===

echo.
echo === Lancement de l'application ===
echo Echelle interface Swing: %UI_SCALE%
java -Dfile.encoding=UTF-8 -Dsun.java2d.dpiaware=true -Dsun.java2d.uiScale=%UI_SCALE% -Dapp.uiScale=%UI_SCALE% -cp bin view.VueApplication

if errorlevel 1 (
    echo.
    echo L'application s'est arretee avec une erreur.
)

pause

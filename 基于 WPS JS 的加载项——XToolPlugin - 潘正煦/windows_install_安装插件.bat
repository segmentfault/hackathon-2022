@echo OFF

echo start

set sourceDir=%~dp0
set tarDir=%appdata%\kingsoft\wps\jsaddons

xcopy "%sourceDir%XToolPlugin\" "%tarDir%\XToolPlugin_1.0.0\" /s/y
copy "%sourceDir%XToolPlugin\publish.xml" "%tarDir%\publish.xml" /y

echo ok
 
pause



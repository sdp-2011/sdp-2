@echo off
del /S build\classes\*.class > NUL 2>&1
cd src
cmd /C nxjc -d ..\build\classes -sourcepath .;..\..\Melmac.Core\src melmac\masterblock\Program.java
cd ..

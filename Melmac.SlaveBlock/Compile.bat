@echo off
rmdir /s /q build\classes
mkdir build\classes
cmd /c lejosjc -sourcepath src -cp ..\Melmac.Core\build\classes -source 1.2 -d build\classes src\melmac\slaveblock\Program.java

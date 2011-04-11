@echo off
cd build\classes
xcopy /e /q ..\..\..\Melmac.Core\build\classes .
cmd /c lejoslink -o ..\..\Melmac.SlaveBlock.rcx --verbose melmac.slaveblock.Program > ..\..\lejoslink.out
cd ..\..
rmdir /s /q build\classes
mkdir build\classes

@echo off
del Melmac.MasterBlock.nxj > tmp.txt 2>&1
cmd /C nxjlink -cp build\classes -v -o Melmac.MasterBlock.nxj melmac.masterblock.Program > nxjlink.out
del /S build\classes\*.class > tmp.txt 2>&1
del tmp.txt

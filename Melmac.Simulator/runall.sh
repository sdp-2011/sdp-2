xterm -e "java -cp \"../Melmac.App/build/classes:../Melmac.Core/build/classes:../Melmac.Vision/build/classes\" melmac.app.Program Yellow" &
xterm -e "java -cp \"../Melmac.App/build/classes:../Melmac.Core/build/classes:../Melmac.Vision/build/classes\" melmac.app.Program Blue" &
xterm -e "java -jar dist/Melmac.Simulator.jar" &

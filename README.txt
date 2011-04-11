This directory contains the source code for SDP 2011 group 2 (a.k.a. Hat-Trick).

Each of the 6 subdirectories are Netbeans projects. However, you needn't use Netbeans, just use the contents of the "src" directory from each however you like.

Melmac.Code is a library project that is referenced by all the others.

Melmac.SlaveBlock is a LeJOS project for the code running on the RCX. This project contains some batch files in its root to help with compiling, linhking and uploading to the NXT.

Melmac.MasterBlock is a LeJOS project for the code running on the RCX. This project contains some batch files in its root to help with compiling, linhking and uploading to the NXT.

Melmac.Vision is a library project containing two components: the Java "client" for connecting to the vision server and the Python code implementing the vision server. The root of the vision code is trunk/Melmac.Vision/src/melmac/python/src.

Melmac.App is the main Java application running on the PC which provides a GUI and in which the agent and strategies are run.

Melmac.Simulator provides a mechanism, using Phys2D, to simulate two robots and a ball on a pitch.

See the team's reports for details and documentation on using this code.

Good luck!

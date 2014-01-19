#! /bin/bash

rm -rv bin
mkdir bin
cp -rv resources bin/resources
cd src
javac -d ../bin life/threedee/game/Game.java
cd ../bin
mkdir META-INF
touch META-INF/MANIFEST.MF
echo Main-Class: life.threedee.game.Game > META-INF/MANIFEST.MF
#echo Class-Path: ./ 3DPacman.jar >> META-INF/MANIFEST.MF
zip -r 3DPacman.jar *
mv 3DPacman.jar ..
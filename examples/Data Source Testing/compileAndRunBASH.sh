#!/bin/sh
# Compiles and runs DSTest.java

javac -classpath "..\..\dist\BotLot.jar" DSTest.java

java -classpath "..\..\dist\BotLot.jar:." DSTest
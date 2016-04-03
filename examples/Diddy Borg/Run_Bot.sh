#!/bin/bash
# Script to take care of everything for running the bot.
#		Installing Py4J:	https://www.py4j.org/install.html

# compile and run server
javac -classpath "../../dist/BotLot.jar" StartPy4JServer/StartServer.java
java -classpath "../../dist/BotLot.jar" StartPy4JServer/StartServer

echo "NOTE:: if you ran this before w/o restarting, the server might not start correctly.... but the one from before will still be running. So it's okay.";

# run bot
python bot.py
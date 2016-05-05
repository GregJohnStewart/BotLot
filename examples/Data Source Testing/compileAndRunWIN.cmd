@ECHO OFF
REM		Compiles and runs DSTest.java
REM		IF IT FAILS TO COMPILE, ensure to have the JDK configured for command line:
REM			set path=%path%;C:\Program Files\Java\jdk1.*.***\bin  <-set the appropriate jdk folder

javac -classpath "..\..\dist\BotLot.jar" DSTest.java

java -classpath "..\..\dist\BotLot.jar;." DSTest
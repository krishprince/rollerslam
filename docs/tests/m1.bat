@cls
@echo off
@set jdk=p:\J2SE1.5.0\
@set classpath=%classpath%;%jdk%\lib;.;.\hsqldb.jar
@set path=C:\Program Files\Java\jdk1.5.0_06\bin;%path%

start java -cp environment.jar rollerslam.environment.gui.ServerDisplay
echo MAQUINA 3
pause

start java -jar mobile-display.jar

start java -jar display.jar
echo MAQUINA 3

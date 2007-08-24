@cls
@echo off
@set jdk=p:\J2SE1.5.0\
@set classpath=%classpath%;%jdk%\lib;.;.\hsqldb.jar
@set path=C:\Program Files\Java\jdk1.5.0_06\bin;%path%

start java -Xms512m -Xmx1024m -cp infrastructure.jar rollerslam.infrastructure.logging.LogRecordingServiceImpl
echo MAQUINA 1
pause


start java -jar display.jar
start java -jar microemulator.jar RollerslamMobile.jad


echo PRONTO

@cls
@echo off
@set jdk=p:\J2SE1.5.0\
@set classpath=%classpath%;%jdk%\lib;.;.\hsqldb.jar
@set path=C:\Program Files\Java\jdk1.5.0_06\bin;%path%

IF "%1"=="" GOTO Continue

start java -jar environment.jar %1
echo MACHINE #3
pause

start java -jar mobile-display.jar

start java -jar display.jar
echo MACHINE #3

goto End

:Continue
echo LOG SERVER IP PARAMETER REQUIRED

:End

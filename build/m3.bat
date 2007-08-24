@cls
@set jdk=p:\J2SE1.5.0\
@set classpath=%classpath%;%jdk%\lib;.;.\hsqldb.jar
@set path=C:\Program Files\Java\jdk1.5.0_06\bin;%path%

start java -jar repeater.jar

start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A auto

start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B auto
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B auto
echo MAQUINA 1
pause

start java -jar microemulator.jar RollerslamMobile.jad
echo MAQUINA 2

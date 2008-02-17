@cls
@set jdk=p:\J2SE1.5.0\
@set classpath=%classpath%;%jdk%\lib;.;.\hsqldb.jar
@set path=C:\Program Files\Java\jdk1.5.0_06\bin;%path%

REM start java -cp rollerslam.jar;hsqldb.jar rollerslam.display.RepeaterDisplay 172.17.200.1
start java -jar repeater.jar %1
pause

start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent A %1

start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1
start java -cp testagent.jar rollerslam.agent.goalbased.RollerslamGoalBasedAgent B %1

start java -jar microemulator.jar RollerslamMobile.jad
echo MAQUINA 4

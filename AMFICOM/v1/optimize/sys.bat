SET PROJECT="D:\Java\NetOptimisation"
SET LIBS=d:\lib91

SET PATCHCP=%PROJECT%\libs\syruspatch.jar;.

SET SERVERCP=%PROJECT%\libs\ServerObjectInterface.jar;%PROJECT%\libs\ServerInterface_General.jar;%PROJECT%\libs\ServerInterface_Map.jar;%PROJECT%\libs\ServerInterface_Network.jar;%PROJECT%\libs\ServerInterface_ISM.jar;%PROJECT%\libs\ServerInterface_Alarm.jar;%PROJECT%\libs\ServerInterface_Survey.jar;%PROJECT%\libs\ServerInterface_Admin.jar;%PROJECT%\libs\ServerInterface_Scheme.jar;%PROJECT%\libs\ServerInterface_Report.jar;%PROJECT%\libs\corba_portable.jar;%PROJECT%\libs\corba_portable_client.jar

SET CLIENTCP=%PROJECT%\libs\io.jar;%PROJECT%\libs\util.jar;%PROJECT%\libs\general.jar;%PROJECT%\libs\main.jar;%PROJECT%\libs\administrate.jar;%PROJECT%\libs\configure.jar;%PROJECT%\libs\map.jar;%PROJECT%\libs\model.jar;%PROJECT%\libs\survey.jar;%PROJECT%\libs\analysis.jar;%PROJECT%\optimize.jar;%PROJECT%\libs\schedule.jar;%PROJECT%\libs\scheme.jar;%PROJECT%\libs\histoAnalysis.jar;%PROJECT%\libs\prognosis.jar;%PROJECT%\libs\localize.jar;%PROJECT%\libs\dadara.jar;%PROJECT%\libs\report.jar;%PROJECT%\libs\reportBuilder.jar;%PROJECT%\libs\mail.jar;%PROJECT%\libs\filter.jar

SET LAYOUTCP=%LIBS%\layout.jar;%LIBS%\kunststoff.jar

SET ORACLECP=%LIBS%\aurora.zip;%LIBS%\aurora_client_orbindep.jar;%LIBS%\aurora_client_orbdep.jar;%LIBS%\vbjorb.jar;%LIBS%\vbjapp.jar

SET SPATIALCP=%LIBS%\ofx.spatialfx.jar

SET REPORTCP=%LIBS%\jcommon-0.9.1.jar;%LIBS%\jfreechart-0.9.16.jar

SET JGRAPHCP=%LIBS%\jgraphpad.jar

SET SERVEROBJECTCP=%PROJECT%\libs\server.jar;%PROJECT%\libs\utilserver.jar;%PROJECT%\libs\syrusjdbc.jar;%PROJECT%\libs\kisserver.jar

SET JDBCCP=%LIBS%\translator.jar;%LIBS%\transpator.zip;%LIBS%\classes12.jar;%LIBS%\runtime12.jar;%LIBS%\classes12.zip;%LIBS%\runtime12.zip

SET JAVAOPTIONS=-Xms128M -Xmx512M -server

SET JAVA="C:\j2sdk1.4.1_03\bin\java.exe" %JAVAOPTIONS%

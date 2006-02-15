-- $Id: DROP.sql,v 1.13.2.2 2006/02/15 19:46:38 arseniy Exp $

PROMPT 20. Dropping table ModelingResultParameter...;
DROP TABLE ModelingResultParameter;
DROP SEQUENCE ModelingResultParameter_seq;

PROMPT 19. Dropping table AnalysisResultParameter...;
DROP TABLE AnalysisResultParameter;
DROP SEQUENCE AnalysisResultParameter_seq;

PROMPT 18. Dropping table MeasurementResultParameter...;
DROP TABLE MeasurementResultParameter;
DROP SEQUENCE MeasurementResultParameter_seq;

PROMPT 17. Dropping table Modeling...;
DROP TABLE Modeling;
DROP SEQUENCE Modeling_seq;

PROMPT 16. Dropping table Analysis...;
DROP TABLE Analysis;
DROP SEQUENCE Analysis_seq;

PROMPT 15. Dropping table Measurement...;
DROP TABLE Measurement;
DROP SEQUENCE Measurement_seq;



PROMPT 14. Dropping table TestStopLink...;
DROP TABLE TestStopLink;

PROMPT 13. Dropping table TestMSLink...;
DROP TABLE TestMSLink;

PROMPT 12. Dropping table Test...;
DROP TABLE Test;
DROP SEQUENCE Test_seq;



PROMPT 11. Dropping table PeriodicalTemporalPattern...;
DROP TABLE PeriodicalTemporalPattern;
DROP SEQUENCE PeriodicalTemporalPattern_seq;



PROMPT 10. Dropping table MSMELink...;
DROP TABLE MSMELink;

PROMPT 09. Dropping table MeasurementSetup...;
DROP TABLE MeasurementSetup;
DROP SEQUENCE MeasurementSetup_seq;

PROMPT 08. Dropping table AcTmplMELink...;
DROP TABLE AcTmplMELink;

PROMPT 07. Dropping table AcTmplParLink...;
DROP TABLE AcTmplParLink;

PROMPT 06. Dropping table ActionTemplate...;
DROP TABLE ActionTemplate;
DROP SEQUENCE ActionTemplate_seq;

PROMPT 05. Dropping table ActionParameter...;
DROP TABLE ActionParameter;
DROP SEQUENCE ActionParameter_seq;



PROMPT 04. Dropping table TransmissionPathMELink...;
DROP TABLE TransmissionPathMELink;

PROMPT 03. Dropping table MonitoredElement...;
DROP TABLE MonitoredElement;
DROP SEQUENCE MonitoredElement_seq;

PROMPT 02. Dropping table MeasurementPort...;
DROP TABLE MeasurementPort;
DROP SEQUENCE MeasurementPort_seq;

PROMPT 01. Dropping table KIS...;
DROP TABLE KIS;
DROP SEQUENCE KIS_seq;

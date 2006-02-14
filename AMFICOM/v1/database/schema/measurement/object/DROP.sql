-- $Id: DROP.sql,v 1.13.2.1 2006/02/14 10:02:20 arseniy Exp $

PROMPT 19. Dropping table ModelingResultParameter...;
DROP TABLE ModelingResultParameter;
DROP SEQUENCE ModelingResultParameter_seq;

PROMPT 18. Dropping table AnalysisResultParameter...;
DROP TABLE AnalysisResultParameter;
DROP SEQUENCE AnalysisResultParameter_seq;

PROMPT 17. Dropping table MeasurementResultParameter...;
DROP TABLE MeasurementResultParameter;
DROP SEQUENCE MeasurementResultParameter_seq;

PROMPT 16. Dropping table Modeling...;
DROP TABLE Modeling;
DROP SEQUENCE Modeling_seq;

PROMPT 15. Dropping table Analysis...;
DROP TABLE Analysis;
DROP SEQUENCE Analysis_seq;

PROMPT 14. Dropping table Measurement...;
DROP TABLE Measurement;
DROP SEQUENCE Measurement_seq;



PROMPT 13. Dropping table TestStopLink...;
DROP TABLE TestStopLink;

PROMPT 12. Dropping table TestAnaTmplLink...;
DROP TABLE TestAnaTmplLink;

PROMPT 11. Dropping table TestMeasTmplLink...;
DROP TABLE TestMeasTmplLink;

PROMPT 10. Dropping table Test...;
DROP TABLE Test;
DROP SEQUENCE Test_seq;



PROMPT 09. Dropping table TemporalPattern...;
DROP TABLE TemporalPattern;
DROP SEQUENCE TemporalPattern_seq;



PROMPT 08. Dropping table METmplLink...;
DROP TABLE METmplLink;

PROMPT 07. Dropping table ActParTmplLink...;
DROP TABLE ActParTmplLink;

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

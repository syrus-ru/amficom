-- $Id: DROP.sql,v 1.12 2005/08/28 14:29:24 arseniy Exp $

DROP TABLE ResultParameter;
DROP TABLE Result;
DROP TABLE Modeling;
DROP TABLE Analysis;
DROP TABLE Measurement;
DROP TABLE MeasurementSetupTestLink;
DROP TABLE Test;
DROP TABLE PeriodicalTemporalPattern;
DROP TABLE CronTemporalPattern;
DROP TYPE CronStringArray;
DROP TABLE MeasurementSetupMTLink;
DROP TABLE MeasurementSetupMELink;
DROP TABLE MeasurementSetup;
DROP TABLE Parameter;
DROP TABLE ParameterSetMELink;
DROP TABLE ParameterSet;
DROP TABLE TransmissionPathMELink;
DROP TABLE EquipmentMELink;
DROP TABLE MonitoredElement;
DROP TABLE MeasurementPort;
DROP TABLE KIS;

DROP SEQUENCE ResultParameter_seq;
DROP SEQUENCE Result_seq;
DROP SEQUENCE Test_seq;
DROP SEQUENCE PeriodicalTemporalPattern_Seq;
DROP SEQUENCE CronTemporalPattern_Seq;
DROP SEQUENCE Modeling_seq;
DROP SEQUENCE Analysis_seq;
DROP SEQUENCE Measurement_seq;
DROP SEQUENCE MeasurementSetup_seq;
DROP SEQUENCE Parameter_seq;
DROP SEQUENCE ParameterSet_seq;
DROP SEQUENCE MonitoredElement_seq;
DROP SEQUENCE MeasurementPort_seq;
DROP SEQUENCE KIS_seq;

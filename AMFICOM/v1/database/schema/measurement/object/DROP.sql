-- $Id: DROP.sql,v 1.11 2005/08/21 16:44:22 arseniy Exp $

DROP TABLE monitoredelement;
DROP TABLE measurementport;
DROP TABLE kis;
DROP TABLE ResultParameter;
DROP TABLE Result;
DROP TABLE Modeling;
DROP TABLE Evaluation;
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
DROP TABLE SetMELink;
DROP TABLE ParameterSet;

DROP SEQUENCE monitoredelement_seq;
DROP SEQUENCE measurementport_seq;
DROP SEQUENCE kis_seq;
DROP SEQUENCE resultparameter_seq;
DROP SEQUENCE result_seq;
DROP SEQUENCE test_seq;
DROP SEQUENCE PeriodicalTemporalPattern_Seq;
DROP SEQUENCE CronTemporalPattern_Seq;
DROP SEQUENCE modeling_seq;
DROP SEQUENCE evaluaition_seq;
DROP SEQUENCE analysis_seq;
DROP SEQUENCE measurement_seq;
DROP SEQUENCE measurementsetup_seq;
DROP SEQUENCE parameter_seq;
DROP SEQUENCE parameterset_seq;

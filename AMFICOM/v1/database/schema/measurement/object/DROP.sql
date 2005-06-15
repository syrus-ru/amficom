-- $Id: DROP.sql,v 1.7 2005/06/15 07:50:18 bass Exp $

DROP TABLE ResultParameter;
DROP TABLE Result;
DROP TABLE Modeling;
DROP TABLE Evaluation;
DROP TABLE Analysis;
DROP TABLE Measurement;
DROP TABLE MeasurementSetupTestLink;
DROP TABLE Test;
DROP TABLE TemporalPattern;
DROP TYPE CronStringArray;
DROP TABLE MeasurementSetupMELink;
DROP TABLE MeasurementSetup;
DROP TABLE Parameter;
DROP TABLE SetMELink;
DROP TABLE ParameterSet;

DROP SEQUENCE temporalpattern_seq;
DROP SEQUENCE resultparameter_seq;
DROP SEQUENCE result_seq;
DROP SEQUENCE test_seq;
DROP SEQUENCE modeling_seq;
DROP SEQUENCE evaluaition_seq;
DROP SEQUENCE analysis_seq;
DROP SEQUENCE measurement_seq;
DROP SEQUENCE measurementsetup_seq;
DROP SEQUENCE parameter_seq;
DROP SEQUENCE parameterset_seq;

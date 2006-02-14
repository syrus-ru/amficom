-- $Id: DROP.sql,v 1.7.2.1 2006/02/14 09:54:33 arseniy Exp $

PROMPT 06. Dropping table ActionParameterTypeBinding...;
DROP TABLE ActionParameterTypeBinding;
DROP SEQUENCE ActionParameterTypeBinding_seq;

PROMPT 05. Dropping table ActionTypeKind...;
DROP TABLE ActionTypeKind;

PROMPT 04. Dropping table ModelingType...;
DROP TABLE ModelingType;
DROP SEQUENCE ModelingType_seq;

PROMPT 03. Dropping table AnalysisType...;
DROP TABLE AnalysisType;
DROP SEQUENCE AnalysisType_seq;

PROMPT 02. Dropping table MeasurementType...;
DROP TABLE MeasurementType;
DROP SEQUENCE MeasurementType_seq;

PROMPT 01. Dropping table MeasurementPortType...;
DROP TABLE MeasurementPortType;
DROP SEQUENCE MeasurementPortType_seq;

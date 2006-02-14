-- $Id: DROP.sql,v 1.2.2.1 2006/02/14 10:32:49 arseniy Exp $

PROMPT 05. Dropping table ReportTableData...;
DROP TABLE ReportTableData;
DROP SEQUENCE ReportTableData_seq;

PROMPT 04. Dropping table ReportData...;
DROP TABLE ReportData;
DROP SEQUENCE ReportData_seq;

PROMPT 03. Dropping table ReportImage...;
DROP TABLE ReportImage;
DROP SEQUENCE ReportImage_seq;

PROMPT 02. Dropping table AttachedText...;
DROP TABLE AttachedText;
DROP SEQUENCE AttachedText_Seq;

PROMPT 01. Dropping table ReportTemplate...;
DROP TABLE ReportTemplate;
DROP SEQUENCE ReportTemplate_Seq;

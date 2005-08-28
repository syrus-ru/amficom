-- $Id: analysistype.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE AnalysisType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT anatyp_pk PRIMARY KEY (code)
);


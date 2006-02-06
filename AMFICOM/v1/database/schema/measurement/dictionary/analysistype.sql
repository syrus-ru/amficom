-- $Id: analysistype.sql,v 1.2 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE AnalysisType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT anatyp_pk PRIMARY KEY (code),
 CONSTRAINT anatyp_uniq UNIQUE (codename)
);


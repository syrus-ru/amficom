-- $Id: measurementtype.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE MeasurementType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT mnttyp_pk PRIMARY KEY (code)
);


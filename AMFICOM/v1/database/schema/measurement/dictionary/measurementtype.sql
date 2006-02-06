-- $Id: measurementtype.sql,v 1.2 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE MeasurementType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT mnttyp_pk PRIMARY KEY (code),
 CONSTRAINT mnttyp_uniq UNIQUE (codename)
);


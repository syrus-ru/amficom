-- $Id: measurementunit.sql,v 1.2 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE MeasurementUnit (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(64 CHAR),
--
 CONSTRAINT mu_pk PRIMARY KEY (code),
 CONSTRAINT mu_uniq UNIQUE (codename)
);

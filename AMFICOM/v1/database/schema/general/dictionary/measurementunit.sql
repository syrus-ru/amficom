-- $Id: measurementunit.sql,v 1.1 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE MeasurementUnit (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(64 CHAR),
--
 CONSTRAINT mu_pk PRIMARY KEY (code)
);

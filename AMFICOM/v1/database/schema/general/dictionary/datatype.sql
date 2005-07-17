-- $Id: datatype.sql,v 1.4 2005/07/17 04:53:00 arseniy Exp $

CREATE TABLE DataType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT dt_pk PRIMARY KEY (code)
);

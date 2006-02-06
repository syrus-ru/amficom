-- $Id: datatype.sql,v 1.5 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE DataType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT dt_pk PRIMARY KEY (code),
 CONSTRAINT dt_uniq UNIQUE (codename)
);

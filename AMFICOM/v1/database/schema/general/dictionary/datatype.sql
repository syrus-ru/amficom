-- $Id: datatype.sql,v 1.3 2005/06/15 09:40:34 bass Exp $

CREATE TABLE DataType (
 id NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT dt_pk PRIMARY KEY (id)
);

-- $Id: datatype.sql,v 1.2 2005/06/15 07:50:18 bass Exp $

CREATE TABLE DataType (
 id NUMBER(2, 0),
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT dt_pk PRIMARY KEY (id)
);

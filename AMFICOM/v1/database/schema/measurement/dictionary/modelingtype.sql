-- $Id: modelingtype.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE ModelingType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT modtyp_pk PRIMARY KEY (code)
);

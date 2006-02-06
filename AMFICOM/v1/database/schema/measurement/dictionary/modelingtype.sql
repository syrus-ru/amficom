-- $Id: modelingtype.sql,v 1.2 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE ModelingType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT modtyp_pk PRIMARY KEY (code),
 CONSTRAINT modtyp_uniq UNIQUE (codename)
);

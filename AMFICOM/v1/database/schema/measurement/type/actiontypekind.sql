-- $Id: actiontypekind.sql,v 1.1.2.1 2006/02/14 09:53:54 arseniy Exp $

CREATE TABLE ActionTypeKind (
 code NUMBER(2) NOT NULL,
 name VARCHAR2(128 CHAR) NOT NULL,
--
 CONSTRAINT atk_pk PRIMARY KEY (code)
);

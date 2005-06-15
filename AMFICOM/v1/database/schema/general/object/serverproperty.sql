-- $Id: serverproperty.sql,v 1.3 2005/06/15 09:40:34 bass Exp $

CREATE TABLE ServerProperty (
 key VARCHAR2(32 CHAR),
 value VARCHAR2(32 CHAR),
--
 CONSTRAINT sp_pk PRIMARY KEY (key)
);


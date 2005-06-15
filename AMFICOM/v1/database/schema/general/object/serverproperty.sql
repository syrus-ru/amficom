-- $Id: serverproperty.sql,v 1.2 2005/06/15 07:50:18 bass Exp $

CREATE TABLE ServerProperty (
 key VARCHAR2(32),
 value VARCHAR2(32),
--
 CONSTRAINT sp_pk PRIMARY KEY (key)
);


-- $Id: shadow.sql,v 1.4 2005/06/15 09:40:35 bass Exp $

CREATE TABLE Shadow (
 user_id NUMBER(19) NOT NULL,
 password VARCHAR2(64 CHAR) NOT NULL,
--
 CONSTRAINT sdw_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

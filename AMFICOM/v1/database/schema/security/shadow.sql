-- $Id: shadow.sql,v 1.5 2005/06/15 17:03:10 bass Exp $

CREATE TABLE Shadow (
 user_id NOT NULL,
 password VARCHAR2(64 CHAR) NOT NULL,
--
 CONSTRAINT sdw_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

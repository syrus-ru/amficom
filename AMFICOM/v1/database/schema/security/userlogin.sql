-- $Id: userlogin.sql,v 1.7 2005/06/15 17:03:10 bass Exp $

CREATE TABLE UserLogin (
 session_key VARCHAR2(128 CHAR) NOT NULL,
 user_id NOT NULL,
 domain_id,
 login_date DATE NOT NULL,
 last_activity_date DATE NOT NULL,
--
 CONSTRAINT ul_pk PRIMARY KEY (session_key),
 CONSTRAINT ul_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ul_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

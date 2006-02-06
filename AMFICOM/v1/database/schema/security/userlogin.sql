-- $Id: userlogin.sql,v 1.9 2005/12/14 11:31:09 arseniy Exp $

CREATE TABLE UserLogin (
 session_key VARCHAR2(128 CHAR) NOT NULL,
 user_id NOT NULL,
 domain_id,
 user_ior VARCHAR2(1000 BYTE) NOT NULL,
 login_date DATE NOT NULL,
 last_activity_date DATE NOT NULL,
--
 CONSTRAINT ul_pk PRIMARY KEY (session_key),
 CONSTRAINT ul_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ul_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

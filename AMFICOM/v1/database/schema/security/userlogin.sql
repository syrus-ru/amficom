-- $Id: userlogin.sql,v 1.6 2005/06/15 09:40:35 bass Exp $

CREATE TABLE UserLogin (
 session_key VARCHAR2(128 CHAR) NOT NULL,
 user_id NUMBER(19) NOT NULL,
 domain_id NUMBER(19),
 login_date DATE NOT NULL,
 last_activity_date DATE NOT NULL,
--
 CONSTRAINT ul_pk PRIMARY KEY (session_key),
 CONSTRAINT ul_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ul_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

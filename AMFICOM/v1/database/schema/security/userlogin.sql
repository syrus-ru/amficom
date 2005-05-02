CREATE TABLE UserLogin (
 security_key VARCHAR2(128) NOT NULL,
 user_id VARCHAR2(32) NOT NULL,
 login_date DATE NOT NULL,
 last_activity_date DATE NOT NULL,
--
 CONSTRAINT ul_pk PRIMARY KEY (security_key),
 CONSTRAINT ul_user_fk FOREIGN KEY (user_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE TABLE Shadow (
 user_id NUMBER(19) NOT NULL,
 password VARCHAR2(64) NOT NULL,
--
 CONSTRAINT sdw_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

-- $Id: teststoplink.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE TestStopLink (
 test_id NOT NULL,
 stop_time DATE NOT NULL,
 stop_reason VARCHAR2(256 CHAR),
--
 CONSTRAINT tsl_uniq UNIQUE (test_id, stop_time),
 CONSTRAINT tsl_t_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE
);

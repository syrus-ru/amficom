-- $Id: measurement.sql,v 1.12 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Measurement (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 monitored_element_id NOT NULL,
 name VARCHAR2(128 CHAR),
--
 setup_id NOT NULL,
 start_time DATE NOT NULL,
 duration NUMBER(20) NOT NULL,
 status NUMBER(2, 0) NOT NULL,
 local_address VARCHAR2(64 CHAR) NOT NULL,
 test_id NOT NULL,
--
 CONSTRAINT measurement_pk PRIMARY KEY (id),
 CONSTRAINT mnt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mnt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mnt_mnttype_fk FOREIGN KEY (type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnt_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
--
 CONSTRAINT mnt_mntsetup_fk FOREIGN KEY (setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mnt_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurement_seq ORDER;

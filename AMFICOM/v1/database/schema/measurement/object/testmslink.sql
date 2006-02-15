-- $Id: testmslink.sql,v 1.1.2.1 2006/02/15 19:44:46 arseniy Exp $

CREATE TABLE TestMSLink (
 test_id NOT NULL,
 measurement_setup_id NOT NULL,
--
 CONSTRAINT tmsl_uniq UNIQUE (test_id, measurement_setup_id),
 CONSTRAINT tmsl_t_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT tmsl_actmpl_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE
);

-- $Id: result.sql,v 1.13 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE Result (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 measurement_id,
 analysis_id,
 modeling_id,
 sort NUMBER(2, 0) NOT NULL,
--
 CONSTRAINT res_pk PRIMARY KEY (id),
 CONSTRAINT res_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT res_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT res_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE,
 CONSTRAINT res_ana_fk FOREIGN KEY (analysis_id)
  REFERENCES Analysis (id) ON DELETE CASCADE,
 CONSTRAINT res_mod_fk FOREIGN KEY (modeling_id)
  REFERENCES Modeling (id) ON DELETE CASCADE
);

CREATE SEQUENCE Result_seq ORDER;

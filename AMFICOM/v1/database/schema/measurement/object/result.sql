CREATE TABLE Result (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 measurement_id VARCHAR2(32) NOT NULL,
 analysis_id VARCHAR2(32),
 evaluation_id VARCHAR2(32),
 modeling_id VARCHAR2(32),
 sort NUMBER(2, 0) NOT NULL,
 alarm_level NUMBER(2, 0) NOT NULL,
--
 CONSTRAINT res_pk PRIMARY KEY (id),
 CONSTRAINT res_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT res_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT res_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE,
 CONSTRAINT res_ana_fk FOREIGN KEY (analysis_id)
  REFERENCES Analysis (id) ON DELETE CASCADE,
 CONSTRAINT res_eva_fk FOREIGN KEY (evaluation_id)
  REFERENCES Evaluation (id) ON DELETE CASCADE,
 CONSTRAINT res_mod_fk FOREIGN KEY (modeling_id)
  REFERENCES Modeling (id) ON DELETE CASCADE
);

CREATE SEQUENCE result_seq ORDER;

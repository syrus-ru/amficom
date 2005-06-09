CREATE TABLE ITmpPattrnOffsetDur (
 inter_temp_patt_id NUMBER(19) NOT NULL,
-- 
 offset NUMBER(19) NOT NULL,
 temp_pattern_id NUMBER(19) NOT NULL,
 duration VARCHAR2(32) NOT NULL,
--
 CONSTRAINT itp_id_fk FOREIGN KEY (inter_temp_patt_id)
  REFERENCES ITempPattern (id) ON DELETE CASCADE
);

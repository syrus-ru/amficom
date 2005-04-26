CREATE TABLE IntervalsTemporalPatternDuration (
 inter_temp_patt_id VARCHAR2(32) NOT NULL,
-- 
 offset NUMBER(19) NOT NULL,
 duration VARCHAR2(32) NOT NULL,
--
 CONSTRAINT itp_id_fk FOREIGN KEY (inter_temp_patt_id)
  REFERENCES IntervalsTemporalPattern (id) ON DELETE CASCADE
)

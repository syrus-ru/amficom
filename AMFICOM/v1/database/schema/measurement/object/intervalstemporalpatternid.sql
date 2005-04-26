CREATE TABLE IntervalsTemporalPatternOffsetId (
 inter_temp_patt_header_id VARCHAR2(32) NOT NULL,
-- 
 offset NUMBER(19) NOT NULL,
 temporal_pattern_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT itp_id_fk FOREIGN KEY (inter_temp_patt_header_id)
  REFERENCES IntervalsTemporalPattern (id) ON DELETE CASCADE
--  ,
-- CONSTRAINT temporal_pattern_id_fk FOREIGN KEY (temporal_pattern_id)
--  REFERENCES TemporalPattern (id) ON DELETE CASCADE
 )

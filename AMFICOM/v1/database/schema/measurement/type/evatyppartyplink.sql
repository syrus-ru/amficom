CREATE TABLE EvaTypParTypLink (
 evaluation_type_id VARCHAR2(32) NOT NULL,
 parameter_type_id VARCHAR2(32) NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
--
 CONSTRAINT evatpartlnk_evat_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
 CONSTRAINT evatpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
--
 CONSTRAINT evatpartlnk_chk CHECK (parameter_mode = 'IN'
  OR parameter_mode = 'THS'
  OR parameter_mode = 'ETA'
  OR parameter_mode = 'OUT')
);

CREATE TABLE AnaTypParTypLink (
 analysis_type_id Identifier NOT NULL,
 parameter_type_id Identifier NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
--
 CONSTRAINT anatpartlnk_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT anatpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
--
 CONSTRAINT anatpartlnk_chk CHECK (parameter_mode = 'IN'
  OR parameter_mode = 'CRI'
  OR parameter_mode = 'OUT')
);

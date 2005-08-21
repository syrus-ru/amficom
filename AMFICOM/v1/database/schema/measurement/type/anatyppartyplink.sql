-- $Id: anatyppartyplink.sql,v 1.10 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE AnaTypParTypLink (
 analysis_type_id NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT anatpartlnk_uniq
  UNIQUE (analysis_type_id, parameter_type_code),
 CONSTRAINT anatpartlnk_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT anatpartlnk_part_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT anatpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'CRI',
   'ETA',
   'OUT')
  )
);


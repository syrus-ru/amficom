-- $Id: anatyppartyplink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE AnaTypParTypLink (
 analysis_type_code NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT anatpartlnk_uniq
  UNIQUE (analysis_type_code, parameter_type_code),
 CONSTRAINT anatpartlnk_anatyp_fk FOREIGN KEY (analysis_type_code)
  REFERENCES AnalysisType (code) ON DELETE CASCADE,
 CONSTRAINT anatpartlnk_pt_fk FOREIGN KEY (parameter_type_code)
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


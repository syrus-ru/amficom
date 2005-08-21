-- $Id: evatyppartyplink.sql,v 1.10 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE EvaTypParTypLink (
 evaluation_type_id NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT evatpartlnk_uniq
  UNIQUE (evaluation_type_id, parameter_type_code),
 CONSTRAINT evatpartlnk_evat_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
 CONSTRAINT evatpartlnk_part_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT evatpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'THS',
   'ETA',
   'OUT')
 )
);


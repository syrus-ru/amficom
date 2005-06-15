-- $Id: evatyppartyplink.sql,v 1.7 2005/06/15 07:50:19 bass Exp $

CREATE TABLE EvaTypParTypLink (
 evaluation_type_id NUMBER(19) NOT NULL,
 parameter_type_id NUMBER(19) NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
--
 CONSTRAINT evatpartlnk_uniq
  UNIQUE (evaluation_type_id, parameter_type_id),
 CONSTRAINT evatpartlnk_evat_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
 CONSTRAINT evatpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
--
 CONSTRAINT evatpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'THS',
   'ETA',
   'OUT')
 )
);


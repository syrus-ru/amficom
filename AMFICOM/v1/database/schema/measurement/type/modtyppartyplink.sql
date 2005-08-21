-- $Id: modtyppartyplink.sql,v 1.6 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE ModTypParTypLink (
 modeling_type_id NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT modtpartlnk_uniq
  UNIQUE (modeling_type_id, parameter_type_code),
 CONSTRAINT modtpartlnk_modt_fk FOREIGN KEY (modeling_type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT modtpartlnk_part_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT modtpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
  )
);

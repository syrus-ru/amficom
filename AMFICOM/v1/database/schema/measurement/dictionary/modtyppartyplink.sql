-- $Id: modtyppartyplink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE ModTypParTypLink (
 modeling_type_code NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT modtpartlnk_uniq
  UNIQUE (modeling_type_code, parameter_type_code),
 CONSTRAINT modtpartlnk_modtyp_fk FOREIGN KEY (modeling_type_code)
  REFERENCES ModelingType (code) ON DELETE CASCADE,
 CONSTRAINT modtpartlnk_pt_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT modtpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
  )
);

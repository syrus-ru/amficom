-- $Id: modtyppartyplink.sql,v 1.4 2005/06/15 09:40:35 bass Exp $

CREATE TABLE ModTypParTypLink (
 modeling_type_id NUMBER(19) NOT NULL,
 parameter_type_id NUMBER(19) NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT modtpartlnk_modt_fk FOREIGN KEY (modeling_type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT modtpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
--
 CONSTRAINT modtpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
  )
);

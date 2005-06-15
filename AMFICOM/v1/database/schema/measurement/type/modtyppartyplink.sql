-- $Id: modtyppartyplink.sql,v 1.3 2005/06/15 07:50:19 bass Exp $

CREATE TABLE ModTypParTypLink (
 modeling_type_id NUMBER(19) NOT NULL,
 parameter_type_id NUMBER(19) NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
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

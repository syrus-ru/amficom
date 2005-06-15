-- $Id: port.sql,v 1.11 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Port (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
--
 description VARCHAR2(256 CHAR),
--
 equipment_id,
 sort NUMBER(2) NOT NULL,
--
 CONSTRAINT port_pk PRIMARY KEY (id),
 CONSTRAINT port_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT port_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT port_porttype_fk FOREIGN KEY (type_id)
  REFERENCES PortType (id) ON DELETE CASCADE,
--
 CONSTRAINT port_eq_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE
);

CREATE SEQUENCE port_seq ORDER;


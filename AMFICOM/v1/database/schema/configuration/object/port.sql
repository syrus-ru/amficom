-- $Id: port.sql,v 1.10 2005/06/15 09:40:34 bass Exp $

CREATE TABLE Port (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NUMBER(19) NOT NULL,
--
 description VARCHAR2(256 CHAR),
--
 equipment_id NUMBER(19),
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


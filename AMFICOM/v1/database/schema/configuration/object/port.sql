CREATE TABLE Port (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
--
 description VARCHAR2(256),
--
 equipment_id VARCHAR2(32),
 sort NUMBER(2) NOT NULL,
--
 CONSTRAINT port_pk PRIMARY KEY (id),
 CONSTRAINT port_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT port_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT port_porttype_fk FOREIGN KEY (type_id)
  REFERENCES PortType (id) ON DELETE CASCADE,
--
 CONSTRAINT port_eq_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE
);

CREATE SEQUENCE port_seq ORDER;


CREATE TABLE Modeling (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NUMBER(19) NOT NULL,
 monitored_element_id NUMBER(19) NOT NULL,
--
 name VARCHAR(128),
--
 argument_set_id NUMBER(19) NOT NULL,
--
 CONSTRAINT mod_pk PRIMARY KEY (id),
 CONSTRAINT mod_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mod_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mod_modtype_fk FOREIGN KEY (type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT mod_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
--
 CONSTRAINT mod_argset_fk FOREIGN KEY (argument_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
);

CREATE SEQUENCE Modeling_seq ORDER;

CREATE TABLE Modeling (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR(128),
--
 argument_set_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mod_pk PRIMARY KEY (id),
 CONSTRAINT mod_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mod_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT mod_modtype_fk FOREIGN KEY (type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT mod_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
--
 CONSTRAINT mod_argset_fk FOREIGN KEY (argument_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE
);

CREATE SEQUENCE Modeling_seq ORDER;

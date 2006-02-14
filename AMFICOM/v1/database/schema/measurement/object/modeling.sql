-- $Id: modeling.sql,v 1.12.2.1 2006/02/14 09:58:40 arseniy Exp $

CREATE TABLE Modeling (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 monitored_element_id NOT NULL,
 action_template_id NOT NULL,
 name VARCHAR2(128 CHAR),
 start_time DATE NOT NULL,
 duration NUMBER(20) NOT NULL,
 status NUMBER(2, 0) NOT NULL,
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
 CONSTRAINT mod_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);

CREATE SEQUENCE Modeling_seq ORDER;

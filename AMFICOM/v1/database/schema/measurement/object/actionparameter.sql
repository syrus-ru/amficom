-- $Id: actionparameter.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE ActionParameter (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 binding_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT ap_pk PRIMARY KEY (id),
 CONSTRAINT ap_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ap_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT ap_aptb_fk FOREIGN KEY (binding_id)
  REFERENCES ActionParameterTypeBinding (id) ON DELETE CASCADE
);

CREATE SEQUENCE ActionParameter_seq ORDER;

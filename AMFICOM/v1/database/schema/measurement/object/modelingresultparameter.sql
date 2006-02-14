-- $Id: modelingresultparameter.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE ModelingResultParameter (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 modeling_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT modrp_pk PRIMARY KEY (id),
 CONSTRAINT modrp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT modrp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT modrp_pt_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT modrp_mod_fk FOREIGN KEY (modeling_id)
  REFERENCES Modeling (id) ON DELETE CASCADE
);

CREATE SEQUENCE ModelingResultParameter_seq ORDER;

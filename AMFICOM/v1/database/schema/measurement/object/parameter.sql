-- $Id: parameter.sql,v 1.4 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE Parameter (
 id NUMBER(19),
 type_code NOT NULL,
 set_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT spar_pk PRIMARY KEY (id),
--
 CONSTRAINT par_partype_fk FOREIGN KEY (type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
 CONSTRAINT par_parset_fk FOREIGN KEY (set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
);

CREATE SEQUENCE Parameter_seq ORDER;

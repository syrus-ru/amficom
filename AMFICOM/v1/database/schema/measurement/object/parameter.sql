-- $Id: parameter.sql,v 1.2 2005/06/15 07:50:18 bass Exp $

CREATE TABLE Parameter (
 id NUMBER(19),
 type_id NUMBER(19) NOT NULL,
 set_id NUMBER(19) NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT spar_pk PRIMARY KEY (id),
--
 CONSTRAINT par_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT par_parset_fk FOREIGN KEY (set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
);

CREATE SEQUENCE Parameter_seq ORDER;

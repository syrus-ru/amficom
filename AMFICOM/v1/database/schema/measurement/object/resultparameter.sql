-- $Id: resultparameter.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE ResultParameter (
 id NUMBER(19),
 type_id NOT NULL,
 result_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT rtpar_pk PRIMARY KEY (id),
 CONSTRAINT rtpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT rtpar_result_fk FOREIGN KEY (result_id)
  REFERENCES Result (id) ON DELETE CASCADE
);

CREATE SEQUENCE resultparameter_seq ORDER;

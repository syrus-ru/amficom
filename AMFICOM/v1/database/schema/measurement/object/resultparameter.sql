-- $Id: resultparameter.sql,v 1.7 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE ResultParameter (
 id NUMBER(19),
 type_code NOT NULL,
 result_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT rtpar_pk PRIMARY KEY (id),
 CONSTRAINT rtpar_partype_fk FOREIGN KEY (type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
 CONSTRAINT rtpar_result_fk FOREIGN KEY (result_id)
  REFERENCES Result (id) ON DELETE CASCADE
);

CREATE SEQUENCE resultparameter_seq ORDER;

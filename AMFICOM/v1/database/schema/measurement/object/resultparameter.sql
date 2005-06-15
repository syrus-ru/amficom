-- $Id: resultparameter.sql,v 1.5 2005/06/15 07:50:18 bass Exp $

CREATE TABLE ResultParameter (
 id NUMBER(19),
 type_id NUMBER(19) NOT NULL,
 result_id NUMBER(19) NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT rtpar_pk PRIMARY KEY (id),
 CONSTRAINT rtpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT rtpar_result_fk FOREIGN KEY (result_id)
  REFERENCES Result (id) ON DELETE CASCADE
);

CREATE SEQUENCE resultparameter_seq ORDER;

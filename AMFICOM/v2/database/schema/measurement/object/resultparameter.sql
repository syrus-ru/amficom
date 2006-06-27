CREATE TABLE ResultParameter (
 id NUMBER(20, 0),
 type_id NUMBER(20, 0) NOT NULL,
 result_id NUMBER(20, 0) NOT NULL,
 value BLOB NOT NULL,
 CONSTRAINT rtpar_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT rtpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT rtpar_result_fk FOREIGN KEY (result_id)
  REFERENCES Result (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE resultparameter_seq ORDER;

CREATE TABLE ResultParameter (
 id VARCHAR2(32),
 type_id VARCHAR2(32) NOT NULL,
 result_id VARCHAR2(32) NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT rtpar_pk PRIMARY KEY (id),
 CONSTRAINT rtpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT rtpar_result_fk FOREIGN KEY (result_id)
  REFERENCES Result (id) ON DELETE CASCADE
);

CREATE SEQUENCE resultparameter_seq ORDER;

CREATE TABLE SetParameter (
 id NUMBER(20, 0),
 type_id NUMBER(20, 0) NOT NULL,
 set_id NUMBER(20, 0) NOT NULL,
 value BLOB NOT NULL,
 CONSTRAINT setparameter_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT setparameter_parametertype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT setparameter_sett_fk FOREIGN KEY (set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE setparameter_seq ORDER;

CREATE TABLE SetParameter (
 id VARCHAR2(32),
 type_id VARCHAR2(32) NOT NULL,
 set_id VARCHAR2(32) NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT spar_pk PRIMARY KEY (id),
--
 CONSTRAINT spar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT spar_sett_fk FOREIGN KEY (set_id)
  REFERENCES Sett (id) ON DELETE CASCADE
);

CREATE SEQUENCE SetParameter_seq ORDER;

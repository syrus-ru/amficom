CREATE TABLE EventParameter (
 id VARCHAR2(32),
 type_id VARCHAR2(32) NOT NULL,
 event_id VARCHAR2(32) NOT NULL,
 sort NUMBER(2) NOT NULL,
 value_number NUMBER,
 value_string VARCHAR2(256),
 value_raw BLOB,
--
 CONSTRAINT evpar_pk PRIMARY KEY (id),
--
 CONSTRAINT evpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT evpar_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE,
 CONSTRAINT evpar_chk CHECK (
  value_number IS NOT NULL
  OR value_string IS NOT NULL
  OR value_raw IS NOT NULL
 )
);

CREATE SEQUENCE EventParameter_seq ORDER;

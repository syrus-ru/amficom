CREATE TABLE EventParameter (
 id VARCHAR2(32),
 type_id VARCHAR2(32) NOT NULL,
 event_id VARCHAR2(32) NOT NULL,
 value VARCHAR2(256) NOT NULL,
--
 CONSTRAINT evpar_pk PRIMARY KEY (id),
--
 CONSTRAINT evpar_partype_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT evpar_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE
);

CREATE SEQUENCE EventParameter_seq ORDER;

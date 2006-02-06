-- $Id: eventparameter.sql,v 1.7 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE EventParameter (
 id NUMBER(19),
 type_code NOT NULL,
 event_id NOT NULL,
 value VARCHAR2(256 CHAR) NOT NULL,
--
 CONSTRAINT evpar_pk PRIMARY KEY (id),
--
 CONSTRAINT evpar_partype_fk FOREIGN KEY (type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
 CONSTRAINT evpar_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE
);

CREATE SEQUENCE EventParameter_seq ORDER;

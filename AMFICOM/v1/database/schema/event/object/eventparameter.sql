-- $Id: eventparameter.sql,v 1.4 2005/06/15 07:50:18 bass Exp $

CREATE TABLE EventParameter (
 id NUMBER(19),
 type_id NUMBER(19) NOT NULL,
 event_id NUMBER(19) NOT NULL,
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

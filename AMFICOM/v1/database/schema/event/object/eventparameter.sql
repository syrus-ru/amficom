-- $Id: eventparameter.sql,v 1.7.2.1 2006/02/14 11:10:04 arseniy Exp $

CREATE TABLE EventParameter (
 id NUMBER(19),
 type_id NOT NULL,
 event_id NOT NULL,
 value VARCHAR2(256 CHAR) NOT NULL,
--
 CONSTRAINT ep_pk PRIMARY KEY (id),
--
 CONSTRAINT ep_pt_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT ep_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE
);

CREATE SEQUENCE EventParameter_seq ORDER;

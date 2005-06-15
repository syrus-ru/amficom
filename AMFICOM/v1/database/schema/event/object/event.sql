-- $Id: event.sql,v 1.5 2005/06/15 09:40:34 bass Exp $

CREATE TABLE Event (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NUMBER(19) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT event_pk PRIMARY KEY (id),
 CONSTRAINT event_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT event_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT event_evtype_fk FOREIGN KEY (type_id)
  REFERENCES EventType (id) ON DELETE CASCADE
);

CREATE SEQUENCE Event_seq ORDER;

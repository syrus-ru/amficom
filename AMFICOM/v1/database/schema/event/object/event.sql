-- $Id: event.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Event (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
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

-- $Id: collector.sql,v 1.4 2005/06/03 11:48:02 bass Exp $

CREATE TABLE Collector (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
--
 CONSTRAINT collector_pk PRIMARY KEY (id),
 CONSTRAINT collector_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT collector_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

COMMENT ON TABLE Collector IS '$Id: collector.sql,v 1.4 2005/06/03 11:48:02 bass Exp $';

CREATE SEQUENCE Collector_Seq ORDER;

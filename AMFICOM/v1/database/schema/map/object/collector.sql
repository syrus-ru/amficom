-- $Id: collector.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Collector (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT collector_pk PRIMARY KEY (id),
 CONSTRAINT collector_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT collector_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

COMMENT ON TABLE Collector IS '$Id: collector.sql,v 1.7 2005/06/15 17:03:09 bass Exp $';

CREATE SEQUENCE Collector_Seq ORDER;

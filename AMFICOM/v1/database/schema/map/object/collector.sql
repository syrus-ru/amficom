-- $Id: collector.sql,v 1.5 2005/06/09 14:40:11 max Exp $

CREATE TABLE Collector (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
--
 CONSTRAINT collector_pk PRIMARY KEY (id),
 CONSTRAINT collector_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT collector_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

COMMENT ON TABLE Collector IS '$Id: collector.sql,v 1.5 2005/06/09 14:40:11 max Exp $';

CREATE SEQUENCE Collector_Seq ORDER;

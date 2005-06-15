-- $Id: transmissionpath.sql,v 1.14 2005/06/15 07:50:18 bass Exp $

CREATE TABLE TransmissionPath (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 type_id NUMBER(19) NOT NULL,
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 start_port_id NUMBER(19) NOT NULL,
 finish_port_id NUMBER(19) NOT NULL,
--
 CONSTRAINT tpath_pk PRIMARY KEY (id),
 CONSTRAINT tpath_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT tpath_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT tpath_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT tpath_tpathtype_fk FOREIGN KEY (type_id)
  REFERENCES TransmissionPathType (id) ON DELETE CASCADE,
--
 CONSTRAINT tpath_start_port_fk FOREIGN KEY (start_port_id)
  REFERENCES Port (id) ON DELETE CASCADE,
 CONSTRAINT tpath_finish_port_fk FOREIGN KEY (finish_port_id)
  REFERENCES Port (id) ON DELETE CASCADE
);

CREATE SEQUENCE transmissionpath_seq ORDER;


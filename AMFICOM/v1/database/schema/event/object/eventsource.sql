-- $Id: eventsource.sql,v 1.8 2005/11/11 02:25:48 bass Exp $

CREATE TABLE EventSource (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 source_entity_code NUMBER(5) NOT NULL,
--
 mcm_id,
--
 port_id,
 equipment_id,
 transmission_path_id,
 link_id,
-- monitored_element_id,
--
 CONSTRAINT evsrc_pk PRIMARY KEY (id),
 CONSTRAINT evsrc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT evsrc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT evsrc_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES MCM (id) ON DELETE CASCADE,
--
 CONSTRAINT evsrc_port_fk FOREIGN KEY (port_id)
  REFERENCES Port (id) ON DELETE CASCADE,
 CONSTRAINT evsrc_eqp_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT evsrc_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT evsrc_link_fk FOREIGN KEY (link_id)
  REFERENCES Link (id) ON DELETE CASCADE,
-- CONSTRAINT evsrc_me_fk FOREIGN KEY (monitored_element_id)
--  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
--
 CONSTRAINT evsrc_mcm_uniq UNIQUE (mcm_id),
--
 CONSTRAINT evsrc_port_uniq UNIQUE (port_id),
 CONSTRAINT evsrc_eqp_uniq UNIQUE (equipment_id),
 CONSTRAINT evsrc_tpath_uniq UNIQUE (transmission_path_id),
 CONSTRAINT evsrc_link_uniq UNIQUE (link_id),
-- CONSTRAINT evsrc_me_uniq UNIQUE (monitored_element_id),
--
 CONSTRAINT evsrc_chk CHECK (
  mcm_id IS NOT NULL
  OR port_id IS NOT NULL
  OR equipment_id IS NOT NULL
  OR transmission_path_id IS NOT NULL
  OR link_id IS NOT NULL
--  OR monitored_element_id IS NOT NULL
 )
);

COMMENT ON TABLE EventSource IS '$Id: eventsource.sql,v 1.8 2005/11/11 02:25:48 bass Exp $';

CREATE TABLE EventSourceLink (
 event_id NOT NULL,
 source_id NOT NULL,
--
 CONSTRAINT evsrclnk_uniq UNIQUE (event_id, source_id),
 CONSTRAINT evsrclnk_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE,
 CONSTRAINT evsrclnk_evsrc_fk FOREIGN KEY (source_id)
  REFERENCES EventSource (id) ON DELETE CASCADE
);

COMMENT ON TABLE EventSourceLink IS '$Id: eventsource.sql,v 1.8 2005/11/11 02:25:48 bass Exp $';

CREATE SEQUENCE EventSource_Seq ORDER;

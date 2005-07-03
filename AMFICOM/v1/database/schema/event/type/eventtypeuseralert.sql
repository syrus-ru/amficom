-- $Id: eventtypeuseralert.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE EventTypeUserAlert (
 event_type_id NOT NULL,
 user_id NOT NULL,
 alert_kind NUMBER(2) NOT NULL,
--
 CONSTRAINT uetlnk_evtype_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id)  ON DELETE CASCADE,
 CONSTRAINT uetlnk_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT uetlnk_uniq UNIQUE (event_type_id, user_id, alert_kind)
);

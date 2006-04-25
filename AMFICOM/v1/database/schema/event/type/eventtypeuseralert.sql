-- $Id: eventtypeuseralert.sql,v 1.5 2006/04/25 08:06:56 arseniy Exp $

CREATE TABLE EventTypeUserAlert (
 event_type_id NOT NULL,
 user_id NOT NULL,
 alert_kind NUMBER(2) NOT NULL,
--
 CONSTRAINT uetlnk_et_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id)  ON DELETE CASCADE,
 CONSTRAINT uetlnk_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT uetlnk_uniq UNIQUE (event_type_id, user_id, alert_kind)
);

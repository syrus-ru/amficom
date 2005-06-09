CREATE TABLE EventTypeUserAlert (
 event_type_id NUMBER(19) NOT NULL,
 user_id NUMBER(19) NOT NULL,
 alert_kind NUMBER(2) NOT NULL,
--
 CONSTRAINT uetlnk_evtype_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id)  ON DELETE CASCADE,
 CONSTRAINT uetlnk_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT uetlnk_uniq UNIQUE (event_type_id, user_id, alert_kind)
);

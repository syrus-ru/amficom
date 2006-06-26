-- $Id: linemismatchevent.sql,v 1.6 2006/06/26 12:44:39 bass Exp $

CREATE TABLE LineMismatchEvent (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	affected_path_element_id NUMBER(19),
-- affectedPathElementSpacious
	physical_distance_to_start BINARY_DOUBLE, -- nullable
	physical_distance_to_end BINARY_DOUBLE, -- nullable
--
	mismatch_optical_distance BINARY_DOUBLE NOT NULL,
	mismatch_physical_distance BINARY_DOUBLE NOT NULL,
	plain_text_message VARCHAR2(4000 char),
	rich_text_message VARCHAR2(4000 char),
	reflectogram_mismatch_event_id NOT NULL,
	alarm_status NUMBER(2),
	parent_line_mismatch_event_id,
--
	CONSTRAINT lm_event_pk PRIMARY KEY(id),
--
	CONSTRAINT lm_event_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT lm_event_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
-- affectedPathElementSpacious
	CONSTRAINT lm_event_spacious_chk CHECK
		((physical_distance_to_start IS NULL
		AND physical_distance_to_end IS NULL)
		OR (physical_distance_to_start IS NOT NULL
		AND physical_distance_to_end IS NOT NULL)),
--
	CONSTRAINT lm_event_rm_event_fk FOREIGN KEY(reflectogram_mismatch_event_id)
		REFERENCES ReflectogramMismatchEvent(id) ON DELETE CASCADE,
	CONSTRAINT lm_event_alarm_status_chk CHECK
		(alarm_status >= 0),
	constraint lm_event_parent_lm_event_fk FOREIGN KEY(parent_line_mismatch_event_id)
		REFERENCES LineMismatchEvent(id) ON DELETE CASCADE,
	CONSTRAINT lm_event_parent_lm_event_chk CHECK
		((alarm_status IS NULL
		AND parent_line_mismatch_event_id IS NOT NULL)
		OR (alarm_status IS NOT NULL
		AND parent_line_mismatch_event_id IS NULL))
);

COMMENT ON TABLE LineMismatchEvent IS '$Id: linemismatchevent.sql,v 1.6 2006/06/26 12:44:39 bass Exp $';

CREATE INDEX lm_event_pln_txt_msg_idx ON LineMismatchEvent(plain_text_message)
	INDEXTYPE IS ctxsys.context
	ONLINE
	PARAMETERS('POPULATE SYNC(ON COMMIT) TRANSACTIONAL');

CREATE INDEX lm_event_rch_txt_msg_idx ON LineMismatchEvent(rich_text_message)
	INDEXTYPE IS ctxsys.context
	ONLINE
	PARAMETERS('POPULATE SYNC(ON COMMIT) TRANSACTIONAL');

CREATE TABLE ChangeLogRecord (
	parent_line_mismatch_event_id NOT NULL,
	modified TIMESTAMP NOT NULL,
	modifier_id NOT NULL,
	key VARCHAR2(32 char) NOT NULL,
	old_value VARCHAR2(4000 char),
	new_value VARCHAR2(4000 char),
--
	CONSTRAINT chnglgrcrd_pk PRIMARY KEY(parent_line_mismatch_event_id, modified, key),
	CONSTRAINT chnglgrcrd_parent_lm_event_fk FOREIGN KEY(parent_line_mismatch_event_id)
		REFERENCES LineMismatchEvent(id) ON DELETE CASCADE,
	CONSTRAINT chnglgrcrd_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT chnglgrcrd_value_chk CHECK
		(old_value <> new_value)
);

CREATE INDEX chnglgrcrd_old_value_idx ON ChangeLogRecord(old_value)
	INDEXTYPE IS ctxsys.context
	ONLINE
	PARAMETERS('POPULATE SYNC(ON COMMIT) TRANSACTIONAL');

CREATE INDEX chnglgrcrd_new_value_idx ON ChangeLogRecord(new_value)
	INDEXTYPE IS ctxsys.context
	ONLINE
	PARAMETERS('POPULATE SYNC(ON COMMIT) TRANSACTIONAL');

CREATE SEQUENCE LineMismatchEvent_Seq ORDER;

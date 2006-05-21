-- $Id: linemismatchevent.sql,v 1.3 2006/05/21 15:46:13 bass Exp $

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
	rich_text_mesage VARCHAR2(4000 char),
	reflectogram_mismatch_event_id NUMBER(19) NOT NULL,
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
		REFERENCES ReflectogramMismatchEvent(id) ON DELETE CASCADE
);

COMMENT ON TABLE LineMismatchEvent IS '$Id: linemismatchevent.sql,v 1.3 2006/05/21 15:46:13 bass Exp $';

CREATE SEQUENCE LineMismatchEvent_Seq ORDER;

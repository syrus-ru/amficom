-- $Id: reflectogrammismatchevent.sql,v 1.5 2006/03/30 12:10:06 bass Exp $

CREATE TABLE ReflectogramMismatchEvent (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	alarm_type NUMBER(1) NOT NULL,
	severity NUMBER(1) NOT NULL,
	coord NUMBER(10) NOT NULL,
	end_coord NUMBER(10) NOT NULL,
	delta_x BINARY_DOUBLE NOT NULL,
-- mismatch
	min_mismatch BINARY_DOUBLE, -- nullable
	max_mismatch BINARY_DOUBLE, -- nullable
-- anchors
	anchor1_id NUMBER(19),
	anchor2_id NUMBER(19),
	anchor1_coord NUMBER(10), -- nullable
	anchor2_coord NUMBER(10), -- nullable
--
	measurement_id,
--
	CONSTRAINT rm_event_pk PRIMARY KEY(id),
--
	CONSTRAINT rm_event_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT rm_event_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT rm_event_alarm_type_chk CHECK
		(0 <= alarm_type
		AND alarm_type <= 3),
	CONSTRAINT rm_event_severity_chk CHECK
		(0 <= severity
		AND severity <= 2),
	CONSTRAINT rm_event_coord_chk CHECK
		(0 <= coord
		AND coord <= end_coord),
	CONSTRAINT rm_event_delta_x_chk CHECK
		(0 <= delta_x),
-- mismatch
	CONSTRAINT rm_event_mismatch_chk CHECK
		((min_mismatch IS NULL
		AND max_mismatch IS NULL)
		OR (min_mismatch <= max_mismatch)),
-- anchors
	CONSTRAINT rm_event_anchors_chk CHECK
		(((anchor1_id IS NULL
		AND anchor1_coord IS NULL)
		OR (anchor1_id IS NOT NULL
		AND anchor1_coord IS NOT NULL))
		AND ((anchor2_id IS NULL
		AND anchor2_coord IS NULL)
		OR (anchor2_id IS NOT NULL
		AND anchor2_coord IS NOT NULL))),
--
	CONSTRAINT rm_event_measurement_fk FOREIGN KEY(measurement_id)
		REFERENCES Measurement(id) ON DELETE SET NULL
);

COMMENT ON TABLE ReflectogramMismatchEvent IS '$Id: reflectogrammismatchevent.sql,v 1.5 2006/03/30 12:10:06 bass Exp $';

CREATE SEQUENCE ReflectogramMismatchEvent_Seq ORDER;

CREATE TABLE MapView
(
	id VARCHAR2(32),
	name VERCHAR(32),
	created DATE NOT NULL,
	creator_id VARCHAR2(32),
	modified DATE NOT NULL,
	modifier_id VARCHAR2(32),
--
	description VARCHAR2(256),
	user_id VARCHAR2(32),
	domain_id VARCHAR2(32),
	longitude NUMBER(7, 4),
	latitude NUMBER(7, 4),
	scale NUMBER(20, 10),
	default_scale NUMBER(20, 10),

	map_id VARCHAR2(32),
--
	CONSTRAINT mv_pk PRIMARY KEY (id),
	CONSTRAINT mv_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE,
	CONSTRAINT mv_domain_fk FOREIGN KEY (domain_id)
		REFERENCES Domain (id) ON DELETE CASCADE,
	CONSTRAINT mv_user_fk FOREIGN KEY (user_id)
		REFERENCES Users (id) ON DELETE CASCADE,
	CONSTRAINT mv_creator_fk FOREIGN KEY (creator_id)
		REFERENCES Users (id) ON DELETE CASCADE,
	CONSTRAINT mv_modifier_fk FOREIGN KEY (modifier_id)
		REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapView_seq ORDER;


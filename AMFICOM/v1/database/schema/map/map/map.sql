CREATE TABLE Map
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
--
	CONSTRAINT map_pk PRIMARY KEY (id),
	CONSTRAINT map_domain_fk FOREIGN KEY (domain_id)
		REFERENCES Domain (id) ON DELETE CASCADE,
	CONSTRAINT map_user_fk FOREIGN KEY (user_id)
		REFERENCES Users (id) ON DELETE CASCADE,
	CONSTRAINT map_creator_fk FOREIGN KEY (creator_id)
		REFERENCES Users (id) ON DELETE CASCADE,
	CONSTRAINT map_modifier_fk FOREIGN KEY (modifier_id)
		REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE Map_seq ORDER;


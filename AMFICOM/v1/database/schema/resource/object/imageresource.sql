CREATE TABLE "ImageResource" (
	id VARCHAR2(32) NOT NULL,
--
	created DATE NOT NULL,
	modified DATE NOT NULL,
	creator_id VARCHAR2(32) NOT NULL,
	modifier_id VARCHAR2(32) NOT NULL,
--
	codename VARCHAR2(256),
	image BLOB,
	sort NUMBER(1) NOT NULL,
--
	CONSTRAINT imgres_pk PRIMARY KEY(id),
	CONSTRAINT imgres_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT imgres_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT imgres_uniq UNIQUE(codename)
);

CREATE SEQUENCE "ImageResource_Seq" ORDER;

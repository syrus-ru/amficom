-- $Id: scheme.sql,v 1.6 2005/06/03 11:48:02 bass Exp $

CREATE TABLE "Scheme" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	label VARCHAR2(64 CHAR),
	width NUMBER(10) DEFAULT 840 NOT NULL,
	height NUMBER(10) DEFAULT 1190 NOT NULL,
	domain_id VARCHAR2(32 CHAR) NOT NULL,
	map_id VARCHAR2(32 CHAR),
	symbol_id VARCHAR2(32 CHAR),
	ugo_cell_id VARCHAR2(32 CHAR),
	scheme_cell_id VARCHAR2(32 CHAR),
	kind NUMBER(1) NOT NULL,
	scheme_monitoring_solution_id VARCHAR2(32 CHAR),
	parent_scheme_element_id VARCHAR2(32 CHAR),
--
	CONSTRAINT scheme_pk PRIMARY KEY(id),
--
	CONSTRAINT scheme_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT scheme_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT scheme_domain_fk FOREIGN KEY(domain_id)
		REFERENCES Domain(id) ON DELETE CASCADE,
	CONSTRAINT scheme_map_fk FOREIGN KEY(map_id)
		REFERENCES Map(id) ON DELETE CASCADE,
	CONSTRAINT scheme_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT scheme_scheme_cell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT scheme_ugo_cell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE
);

COMMENT ON TABLE "Scheme" IS '$Id: scheme.sql,v 1.6 2005/06/03 11:48:02 bass Exp $';
COMMENT ON COLUMN "Scheme".kind IS 'Logically this is a SchemeKind. While SchemeType table is absent, it will remain an enum.';

CREATE SEQUENCE Scheme_Seq ORDER;

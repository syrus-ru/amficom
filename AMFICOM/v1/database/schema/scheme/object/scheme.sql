-- $Id: scheme.sql,v 1.1 2005/02/04 15:26:50 bass Exp $

CREATE TABLE "Scheme" (
	id VARCHAR2(32) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32) NOT NULL,
	modifier_id VARCHAR2(32) NOT NULL,
--
	name VARCHAR2(32) NOT NULL,
	description VARCHAR2(256),
--
	label VARCHAR2(64),
	width NUMBER(10) DEFAULT 840 NOT NULL,
	height NUMBER(10) DEFAULT 1190 NOT NULL,
	domain_id VARCHAR2(32) NOT NULL,
	map_id VARCHAR2(32),
	symbol_id VARCHAR2(32),
	ugo_cell_id VARCHAR2(32),
	scheme_cell_id VARCHAR2(32),
	sort NUMBER(1) NOT NULL,
	scheme_monitoring_solution_id VARCHAR2(32),
	parent_scheme_element_id VARCHAR2(32),
--
	CONSTRAINT scheme_pk PRIMARY KEY(id),
--
	CONSTRAINT scheme_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT scheme_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
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
COMMENT ON COLUMN "Scheme".sort IS 'Logically this is a schemeTypeId. While SchemeType table is absent, it will remain to be an enum.';

CREATE SEQUENCE "Scheme_Seq" ORDER;

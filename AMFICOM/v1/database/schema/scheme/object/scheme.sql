-- $Id: scheme.sql,v 1.11 2005/09/26 10:29:18 bass Exp $

CREATE TABLE Scheme (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	label VARCHAR2(64 CHAR),
	width NUMBER(10) DEFAULT 840 NOT NULL,
	height NUMBER(10) DEFAULT 1190 NOT NULL,
	domain_id NOT NULL,
	map_id,
	symbol_id,
	ugo_cell_id,
	scheme_cell_id,
	kind NUMBER(1) NOT NULL,
	parent_scheme_element_id NUMBER(19),
--
	CONSTRAINT scheme_pk PRIMARY KEY(id),
--
	CONSTRAINT scheme_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT scheme_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT scheme_domain_fk FOREIGN KEY(domain_id)
		REFERENCES Domain(id) ON DELETE CASCADE,
	CONSTRAINT scheme_map_fk FOREIGN KEY(map_id)
		REFERENCES Map(id) ON DELETE SET NULL,
	CONSTRAINT scheme_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT scheme_scheme_cell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT scheme_ugo_cell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL
);

COMMENT ON TABLE Scheme IS '$Id: scheme.sql,v 1.11 2005/09/26 10:29:18 bass Exp $';
COMMENT ON COLUMN Scheme.kind IS 'Logically this is a SchemeKind. While SchemeType table is absent, it will remain an enum.';

CREATE SEQUENCE Scheme_Seq ORDER;

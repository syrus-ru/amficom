-- $Id: schemecablelink.sql,v 1.4 2005/06/03 11:48:02 bass Exp $

CREATE TABLE "SchemeCableLink" (
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
	physical_length BINARY_DOUBLE NOT NULL,
	optical_length BINARY_DOUBLE NOT NULL,
	cable_link_type_id VARCHAR2(32 CHAR),
	cable_link_id VARCHAR2(32 CHAR),
	source_scheme_cable_port_id VARCHAR2(32 CHAR),
	target_scheme_cable_port_id VARCHAR2(32 CHAR),
	parent_scheme_id VARCHAR2(32 CHAR) NOT NULL,
--
	CONSTRAINT schmcbllnk_pk PRIMARY KEY(id),
--
	CONSTRAINT schmcbllnk_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schmcbllnk_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schmcbllnk_cbl_lnk_tp_fk FOREIGN KEY(cable_link_type_id)
		REFERENCES LinkType(id) ON DELETE CASCADE,
	CONSTRAINT schmcbllnk_cbl_lnk_fk FOREIGN KEY(cable_link_id)
		REFERENCES Link(id) ON DELETE CASCADE,
	CONSTRAINT schmcbllnk_src_schm_cbl_prt_fk FOREIGN KEY(source_scheme_cable_port_id)
		REFERENCES "SchemeCablePort"(id) ON DELETE CASCADE,
	CONSTRAINT schmcbllnk_tgt_schm_cbl_prt_fk FOREIGN KEY(target_scheme_cable_port_id)
		REFERENCES "SchemeCablePort"(id) ON DELETE CASCADE,
	CONSTRAINT schmcbllnk_prnt_scheme_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES "Scheme"(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of cable_link_type_id and cable_link_id may be
	-- defined, and only one may be null.
	CONSTRAINT schmcbllnk_cable_link_chk CHECK
		((cable_link_type_id IS NULL
		AND cable_link_id IS NOT NULL)
		OR (cable_link_type_id IS NOT NULL
		AND cable_link_id IS NULL))
);

COMMENT ON TABLE "SchemeCableLink" IS '$Id: schemecablelink.sql,v 1.4 2005/06/03 11:48:02 bass Exp $';

CREATE SEQUENCE SchemeCableLink_Seq ORDER;

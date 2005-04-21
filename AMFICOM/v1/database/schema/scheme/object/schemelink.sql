-- $Id: schemelink.sql,v 1.4 2005/04/21 10:42:38 bass Exp $

CREATE TABLE "SchemeLink" (
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
--
	link_type_id VARCHAR2(32 CHAR),
	link_id VARCHAR2(32 CHAR),
--
	site_node_id VARCHAR2(32 CHAR),
	source_scheme_port_id VARCHAR2(32 CHAR),
	target_scheme_port_id VARCHAR2(32 CHAR),
--
	parent_scheme_id VARCHAR2(32 CHAR),
	parent_scheme_element_id VARCHAR2(32 CHAR),
	parent_scheme_proto_element_id VARCHAR2(32 CHAR),
--
	CONSTRAINT schmlnk_pk PRIMARY KEY(id),
--
	CONSTRAINT schmlnk_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schmlnk_lnk_tp_fk FOREIGN KEY(link_type_id)
		REFERENCES LinkType(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_lnk_fk FOREIGN KEY(link_id)
		REFERENCES Link(id) ON DELETE CASCADE,
--
	CONSTRAINT schmlnk_site_node_fk FOREIGN KEY(site_node_id)
		REFERENCES SiteNode(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_source_scheme_port_fk FOREIGN KEY(source_scheme_port_id)
		REFERENCES "SchemePort"(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_target_scheme_port_fk FOREIGN KEY(target_scheme_port_id)
		REFERENCES "SchemePort"(id) ON DELETE CASCADE,
--
	CONSTRAINT schmlnk_prnt_schm_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES "Scheme"(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_prnt_schm_elmnt_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES "SchemeElement"(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_prnt_schm_prt_elmnt_fk FOREIGN KEY(parent_scheme_proto_element_id)
		REFERENCES "SchemeProtoElement"(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of link_type_id and link_id may be defined,
	-- and only one may be null.
	CONSTRAINT schmlnk_link_chk CHECK
		((link_type_id IS NULL
		AND link_id IS NOT NULL)
		OR (link_type_id IS NOT NULL
		AND link_id IS NULL)),
	-- Exactly one parent allowed.
	CONSTRAINT schmlnk_prnt_chk CHECK
		((parent_scheme_id IS NOT NULL
		AND parent_scheme_element_id IS NULL
		AND parent_scheme_proto_element_id IS NULL)
		OR (parent_scheme_id IS NULL
		AND parent_scheme_element_id IS NOT NULL
		AND parent_scheme_proto_element_id IS NULL)
		OR (parent_scheme_id IS NULL
		AND parent_scheme_element_id IS NULL
		AND parent_scheme_proto_element_id IS NOT NULL))
);

COMMENT ON TABLE "SchemeLink" IS '$Id: schemelink.sql,v 1.4 2005/04/21 10:42:38 bass Exp $';

CREATE SEQUENCE "SchemeLink_Seq" ORDER;

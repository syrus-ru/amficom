-- $Id: schemeelement.sql,v 1.6 2005/03/22 13:36:03 bass Exp $

CREATE TABLE "SchemeElement" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR)NOT NULL,
	description VARCHAR2(256 CHAR),
--
	label VARCHAR2(64 CHAR),
	equipment_type_id VARCHAR2(32 CHAR),
	equipment_id VARCHAR2(32 CHAR),
	rtu_id VARCHAR2(32 CHAR),
	site_id VARCHAR2(32 CHAR),
	symbol_id VARCHAR2(32 CHAR),
	ugo_cell_id VARCHAR2(32 CHAR),
	scheme_cell_id VARCHAR2(32 CHAR),
	parent_scheme_id VARCHAR2(32 CHAR),
	parent_scheme_element_id VARCHAR2(32 CHAR),
--
	CONSTRAINT schemeelement_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeelement_equipmnt_type_fk FOREIGN KEY(equipment_type_id)
		REFERENCES EquipmentType(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_equipmnt_fk FOREIGN KEY(equipment_id)
		REFERENCES Equipment(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_rtu_fk FOREIGN KEY(rtu_id)
		REFERENCES Kis(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_sitenode_fk FOREIGN KEY(site_id)
		REFERENCES SiteNode(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_ugo_cell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_scheme_cell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_prnt_schm_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES "Scheme"(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_prnt_schmlmnt_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES "SchemeElement"(id) ON DELETE CASCADE,
--
	-- Boolean OR: equipment_type_id and equipment_id may be both nulls.
	CONSTRAINT schemeelement_equipmnt_chk CHECK
		((equipment_type_id IS NULL)
		OR (equipment_id IS NULL)),
	-- Boolean XOR: only one of parent_scheme_id and
	-- parent_scheme_element_id may be defined, and only one may be null.
	CONSTRAINT schemeelement_prnt_chk CHECK
		((parent_scheme_id IS NULL
		AND parent_scheme_element_id IS NOT NULL)
		OR (parent_scheme_id IS NOT NULL
		AND parent_scheme_element_id IS NULL))
);

COMMENT ON TABLE "SchemeElement" IS '$Id: schemeelement.sql,v 1.6 2005/03/22 13:36:03 bass Exp $';

ALTER TABLE "Scheme" ADD (
	CONSTRAINT scheme_prnt_scheme_element_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES "SchemeElement"(id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE "SchemeElement_Seq" ORDER;

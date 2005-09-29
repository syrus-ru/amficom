-- $Id: schemeelement.sql,v 1.15 2005/09/29 13:05:10 bass Exp $

CREATE TABLE SchemeElement (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR)NOT NULL,
	description VARCHAR2(256 CHAR),
--
	kind NUMBER(1) NOT NULL,
	label VARCHAR2(64 CHAR),
	proto_equipment_id,
	equipment_id,
	kis_id,
	site_node_id,
	symbol_id,
	ugo_cell_id,
	scheme_cell_id,
	parent_scheme_id,
	parent_scheme_element_id,
--
	CONSTRAINT schemeelement_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeelement_protoequipmnt_fk FOREIGN KEY(proto_equipment_id)
		REFERENCES ProtoEquipment(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_equipmnt_fk FOREIGN KEY(equipment_id)
		REFERENCES Equipment(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_kis_fk FOREIGN KEY(kis_id)
		REFERENCES Kis(id) ON DELETE SET NULL,
	CONSTRAINT schemeelement_sitenode_fk FOREIGN KEY(site_node_id)
		REFERENCES SiteNode(id) ON DELETE SET NULL,
	CONSTRAINT schemeelement_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeelement_ugo_cell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeelement_scheme_cell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeelement_prnt_schm_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES Scheme(id) ON DELETE CASCADE,
	CONSTRAINT schemeelement_prnt_schmlmnt_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES SchemeElement(id) ON DELETE CASCADE,
--
	-- Boolean OR: proto_equipment_id and equipment_id may be both nulls.
	-- see bug #136
	CONSTRAINT schemeelement_equipmnt_chk CHECK
		((proto_equipment_id IS NULL)
		OR (equipment_id IS NULL)),
	-- Boolean XOR: only one of parent_scheme_id and
	-- parent_scheme_element_id may be defined, and only one may be null.
	CONSTRAINT schemeelement_prnt_chk CHECK
		((parent_scheme_id IS NULL
		AND parent_scheme_element_id IS NOT NULL)
		OR (parent_scheme_id IS NOT NULL
		AND parent_scheme_element_id IS NULL))
);

COMMENT ON TABLE SchemeElement IS '$Id: schemeelement.sql,v 1.15 2005/09/29 13:05:10 bass Exp $';

ALTER TABLE Scheme ADD (
	CONSTRAINT scheme_prnt_scheme_element_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES SchemeElement(id) ON DELETE SET NULL ENABLE
);

CREATE SEQUENCE SchemeElement_Seq ORDER;

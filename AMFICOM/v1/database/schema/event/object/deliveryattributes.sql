-- $Id: deliveryattributes.sql,v 1.2 2005/11/11 04:55:49 bass Exp $

CREATE TABLE DeliveryAttributes (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	severity NUMBER(1) NOT NULL,
--
	CONSTRAINT dlvrattrs_pk PRIMARY KEY(id),
--
	CONSTRAINT dlvrattrs_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT dlvrattrs_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT dlvrattrs_severity_chk CHECK
		(0 <= severity AND severity <= 2),
	CONSTRAINT dlvrattrs_severity_uniq UNIQUE(severity)
);

COMMENT ON TABLE DeliveryAttributes IS '$Id: deliveryattributes.sql,v 1.2 2005/11/11 04:55:49 bass Exp $';

CREATE TABLE DeliveryAttributesRoleLink (
	delivery_attributes_id NOT NULL,
	role_id NOT NULL,
--
	CONSTRAINT dlvrattrsrolelnk_dlvrattrs_fk FOREIGN KEY(delivery_attributes_id)
		REFERENCES DeliveryAttributes(id) ON DELETE CASCADE,
	CONSTRAINT dlvrattrsrolelnk_role_fk FOREIGN KEY(role_id)
		REFERENCES Role(id) ON DELETE CASCADE,
	CONSTRAINT dlvrattrsrolelnk_uniq UNIQUE(delivery_attributes_id, role_id)
);

COMMENT ON TABLE DeliveryAttributesRoleLink IS '$Id: deliveryattributes.sql,v 1.2 2005/11/11 04:55:49 bass Exp $';

CREATE TABLE DeliveryAttributesUserLink (
	delivery_attributes_id NOT NULL,
	system_user_id NOT NULL,
--
	CONSTRAINT dlvrattrsusrlnk_dlvrattrs_fk FOREIGN KEY(delivery_attributes_id)
		REFERENCES DeliveryAttributes(id) ON DELETE CASCADE,
	CONSTRAINT dlvrattrsusrlnk_sysusr_fk FOREIGN KEY(system_user_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT dlvrattrsusrlnk_uniq UNIQUE(delivery_attributes_id, system_user_id)
);

COMMENT ON TABLE DeliveryAttributesUserLink IS '$Id: deliveryattributes.sql,v 1.2 2005/11/11 04:55:49 bass Exp $';

CREATE SEQUENCE DeliveryAttributes_Seq ORDER;

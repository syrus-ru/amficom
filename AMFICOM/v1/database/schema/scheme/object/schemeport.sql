-- $Id: schemeport.sql,v 1.7 2005/06/15 17:03:10 bass Exp $

CREATE TABLE SchemePort (
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
	direction_type NUMBER(1) NOT NULL,
	port_type_id,
	port_id,
	measurement_port_id,
	parent_device_id NOT NULL,
--
	CONSTRAINT schemeport_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeport_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemeport_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeport_port_type_fk FOREIGN KEY(port_type_id)
		REFERENCES PortType(id) ON DELETE CASCADE,
	CONSTRAINT schemeport_port_fk FOREIGN KEY(port_id)
		REFERENCES Port(id) ON DELETE CASCADE,
	CONSTRAINT schemeport_msrmnt_port_fk FOREIGN KEY(measurement_port_id)
		REFERENCES MeasurementPort(id) ON DELETE CASCADE,
	CONSTRAINT schemeport_prnt_schemdev_fk FOREIGN KEY(parent_device_id)
		REFERENCES SchemeDevice(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of port_type_id and port_id may be defined,
	-- and only one may be null.
	CONSTRAINT schemeport_port_chk CHECK
		((port_type_id IS NULL
		AND port_id IS NOT NULL)
		OR (port_type_id IS NOT NULL
		AND port_id IS NULL))
);

COMMENT ON TABLE SchemePort IS '$Id: schemeport.sql,v 1.7 2005/06/15 17:03:10 bass Exp $';

CREATE SEQUENCE SchemePort_Seq ORDER;

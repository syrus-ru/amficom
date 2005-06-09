-- $Id: schemecableport.sql,v 1.6 2005/06/09 14:40:12 max Exp $

CREATE TABLE SchemeCablePort (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NUMBER(19) NOT NULL,
	modifier_id NUMBER(19) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	direction_type NUMBER(1) NOT NULL,
	cable_port_type_id NUMBER(19),
	cable_port_id NUMBER(19),
	measurement_port_id NUMBER(19),
	parent_device_id NUMBER(19) NOT NULL,
--
	CONSTRAINT schmcblprt_pk PRIMARY KEY(id),
--
	CONSTRAINT schmcblprt_creator_fk FOREIGN KEY(creator_id)
		REFERENCES User(id) ON DELETE CASCADE,
	CONSTRAINT schmcblprt_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES User(id) ON DELETE CASCADE,
--
	CONSTRAINT schmcblprt_cable_port_type_fk FOREIGN KEY(cable_port_type_id)
		REFERENCES PortType(id) ON DELETE CASCADE,
	CONSTRAINT schmcblprt_cable_port_fk FOREIGN KEY(cable_port_id)
		REFERENCES Port(id) ON DELETE CASCADE,
	CONSTRAINT schmcblprt_msrmnt_port_fk FOREIGN KEY(measurement_port_id)
		REFERENCES MeasurementPort(id) ON DELETE CASCADE,
	CONSTRAINT schmcblprt_prnt_schemdev_fk FOREIGN KEY(parent_device_id)
		REFERENCES SchemeDevice(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of cable_port_type_id and cable_port_id may be
	-- defined, and only one may be null.
	CONSTRAINT schmcblprt_port_chk CHECK
		((cable_port_type_id IS NULL
		AND cable_port_id IS NOT NULL)
		OR (cable_port_type_id IS NOT NULL
		AND cable_port_id IS NULL))
);

COMMENT ON TABLE SchemeCablePort IS '$Id: schemecableport.sql,v 1.6 2005/06/09 14:40:12 max Exp $';

CREATE SEQUENCE SchemeCablePort_Seq ORDER;

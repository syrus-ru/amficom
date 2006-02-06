-- $Id: schemeoptimizeinfoswitch.sql,v 1.1 2005/07/24 15:42:19 bass Exp $

CREATE TABLE SchemeOptimizeInfoSwitch (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	no_of_ports NUMBER(3) NOT NULL,
	parent_scheme_optimize_info_id NOT NULL,
--
	CONSTRAINT schmoptmzinfswitch_pk PRIMARY KEY(id),
--
	CONSTRAINT schmoptmzinfswitch_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmoptmzinfswitch_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmoptmzinfswitch_uk UNIQUE(
		name,
		price_usd,
		no_of_ports,
		parent_scheme_optimize_info_id
	),
--
	CONSTRAINT schmoptmzinfswitch_parent_fk FOREIGN KEY(parent_scheme_optimize_info_id)
		REFERENCES SchemeOptimizeInfo(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfoSwitch IS '$Id: schemeoptimizeinfoswitch.sql,v 1.1 2005/07/24 15:42:19 bass Exp $';
COMMENT ON COLUMN SchemeOptimizeInfoSwitch.price_usd IS 'Optical switch price in US dollars.';
COMMENT ON COLUMN SchemeOptimizeInfoSwitch.no_of_ports IS 'Number of ports in this optical switch. Up to 256.';

CREATE SEQUENCE SchemeOptimizeInfoSwitch_Seq ORDER;

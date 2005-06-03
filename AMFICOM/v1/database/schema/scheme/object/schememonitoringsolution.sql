-- $Id: schememonitoringsolution.sql,v 1.4 2005/06/03 11:48:02 bass Exp $

CREATE TABLE "SchemeMonitoringSolution" (
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
	price_usd NUMBER(10) NOT NULL,
	scheme_optimize_info_id VARCHAR2(32 CHAR),
--
	CONSTRAINT schememonitoringsolution_pk PRIMARY KEY(id),
--
	CONSTRAINT schmmonitoringsltn_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schmmonitoringsltn_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schmmonitoringsltn_schmptnf_fk FOREIGN KEY(scheme_optimize_info_id)
		REFERENCES "SchemeOptimizeInfo"(id) ON DELETE CASCADE
);

COMMENT ON TABLE "SchemeMonitoringSolution" IS '$Id: schememonitoringsolution.sql,v 1.4 2005/06/03 11:48:02 bass Exp $';
COMMENT ON COLUMN "SchemeMonitoringSolution".price_usd IS 'Cost of this solution in US dollars.';

ALTER TABLE "Scheme" ADD (
	CONSTRAINT scheme_schm_monitoring_sltn_fk FOREIGN KEY(scheme_monitoring_solution_id)
		REFERENCES "SchemeMonitoringSolution"(id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE SchemeMonitoringSolution_Seq ORDER;

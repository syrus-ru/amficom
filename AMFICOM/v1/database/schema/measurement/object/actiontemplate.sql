-- $Id: actiontemplate.sql,v 1.1.2.3 2006/04/12 13:50:07 arseniy Exp $

CREATE TABLE ActionTemplate (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 action_type_kind_code NUMBER(2) NOT NULL,
 measurement_type_id,
 analysis_type_id,
 modeling_type_id,
 measurement_port_type_id NOT NULL,
 description VARCHAR2(256 CHAR),
 approximate_action_duration NUMBER(10) NOT NULL,
--
 CONSTRAINT actmpl_pk PRIMARY KEY (id),
 CONSTRAINT actmpl_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT actmpl_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT actmpl_atkc_check CHECK (
  action_type_kind_code >= 0 AND action_type_kind_code <= 2),
 CONSTRAINT actmpl_atk_check CHECK (
  (measurement_type_id IS NOT NULL
   AND analysis_type_id IS NULL
   AND modeling_type_id IS NULL)
  OR (measurement_type_id IS NULL
   AND analysis_type_id IS NOT NULL
   AND modeling_type_id IS NULL)
  OR (measurement_type_id IS NULL
   AND analysis_type_id IS NULL
   AND modeling_type_id IS NOT NULL)),
 CONSTRAINT actmpl_meat_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT actmpl_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT actmpl_modt_fk FOREIGN KEY (modeling_type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT actmpl_mpt_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE
);

CREATE SEQUENCE ActionTemplate_seq ORDER;

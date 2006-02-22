-- $Id: actionparametertypebinding.sql,v 1.1.2.2 2006/02/22 15:53:43 arseniy Exp $

CREATE TABLE ActionParameterTypeBinding (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 parameter_value_kind NUMBER(1) NOT NULL,
 parameter_type_id NOT NULL,
 action_type_kind_code NUMBER(2) NOT NULL,
 measurement_type_id,
 analysis_type_id,
 modeling_type_id,
 measurement_port_type_id NOT NULL,
--
 CONSTRAINT aptb_pk PRIMARY KEY (id),
 CONSTRAINT aptb_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT aptb_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT aptb_pvk_check CHECK (
  parameter_value_kind = 0 OR parameter_value_kind = 1),
 CONSTRAINT aptb_atkc_check CHECK (
  action_type_kind_code >= 0 AND action_type_kind_code <= 2),
 CONSTRAINT aptb_atk_check CHECK (
  (measurement_type_id IS NOT NULL
   AND analysis_type_id IS NULL
   AND modeling_type_id IS NULL)
  OR (measurement_type_id IS NULL
   AND analysis_type_id IS NOT NULL
   AND modeling_type_id IS NULL)
  OR (measurement_type_id IS NULL
   AND analysis_type_id IS NULL
   AND modeling_type_id IS NOT NULL)),
 CONSTRAINT aptb_uniq UNIQUE (
  parameter_type_id,
  measurement_type_id,
  analysis_type_id,
  modeling_type_id,
  measurement_port_type_id),
 CONSTRAINT aptb_pt_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT aptb_meat_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT aptb_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT aptb_modt_fk FOREIGN KEY (modeling_type_id)
  REFERENCES ModelingType (id) ON DELETE CASCADE,
 CONSTRAINT aptb_mpt_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE
);

CREATE SEQUENCE ActionParameterTypeBinding_seq ORDER;

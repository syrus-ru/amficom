-- $Id: mnttypanatypevatyp.sql,v 1.7 2005/06/15 07:50:19 bass Exp $

CREATE TABLE MntTypAnaTypEvaTyp (
 measurement_type_id NUMBER(19) NOT NULL,
 analysis_type_id NUMBER(19) NOT NULL,
 evaluation_type_id NUMBER(19),
--
 CONSTRAINT mnttanatevat_uniq
  UNIQUE (measurement_type_id, analysis_type_id, evaluation_type_id),
--
 CONSTRAINT mnttanatevat_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttanatevat_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT mnttanatevat_evat_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE
);

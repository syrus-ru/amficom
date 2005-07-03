-- $Id: mnttypanatypevatyp.sql,v 1.8 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MntTypAnaTypEvaTyp (
 measurement_type_id NOT NULL,
 analysis_type_id NOT NULL,
 evaluation_type_id,
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

CREATE TABLE MntTypAnaTypEvaTyp (
 measurement_type_id VARCHAR2(32) NOT NULL,
 analysis_type_id VARCHAR2(32) NOT NULL,
 evaluation_type_id VARCHAR2(32) NOT NULL,
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

CREATE TABLE MntTypAnaTypEvaTyp (
 measurement_type_id NUMBER(20, 0) NOT NULL,
 analysis_type_id NUMBER(20, 0) NOT NULL,
 evaluation_type_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT mnttanatevat_uniq UNIQUE (measurement_type_id, analysis_type_id, evaluation_type_id),
 CONSTRAINT mnttanatevat_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mnttanatevat_anat_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mnttanatevat_evat_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE ENABLE
);

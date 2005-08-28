-- $Id: mnttypanatyplink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE MntTypAnaTypLink (
 measurement_type_code NOT NULL,
 analysis_type_code NOT NULL,
--
 CONSTRAINT mnttanatlnk_mnttyp_link FOREIGN KEY (measurement_type_code)
  REFERENCES MeasurementType(code) ON DELETE CASCADE,
 CONSTRAINT mnttanatlnk_anatyp_link FOREIGN KEY (analysis_type_code)
  REFERENCES AnalysisType(code) ON DELETE CASCADE
);

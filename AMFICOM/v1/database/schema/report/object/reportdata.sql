--$Id: reportdata.sql,v 1.3.2.1 2006/02/14 10:32:49 arseniy Exp $

CREATE TABLE ReportData (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 location_x NUMBER(10) NOT NULL,
 location_y NUMBER(10) NOT NULL,
 width NUMBER(10) NOT NULL,
 height NUMBER(10) NOT NULL,
 report_template_id,
--
 report_name VARCHAR2(128 CHAR) NOT NULL,
 model_class_name VARCHAR2(128 CHAR) NOT NULL,
--
 CONSTRAINT reportdata_pk PRIMARY KEY (id),
 CONSTRAINT reportdata_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reportdata_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reportdata_reptempl_fk FOREIGN KEY (report_template_id)
  REFERENCES ReportTemplate (id) ON DELETE CASCADE
);

COMMENT ON TABLE ReportData IS '$Id: reportdata.sql,v 1.3.2.1 2006/02/14 10:32:49 arseniy Exp $';

CREATE SEQUENCE ReportData_Seq ORDER;

--$Id: reporttabledata.sql,v 1.4 2006/04/25 07:53:30 arseniy Exp $

CREATE TABLE ReportTableData (
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
 vertical_division NUMBER(10) NOT NULL,
--
 CONSTRAINT reporttabledata_pk PRIMARY KEY (id),
 CONSTRAINT reporttabledata_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reporttabledata_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reporttabledata_reptempl_fk FOREIGN KEY (report_template_id)
  REFERENCES ReportTemplate (id) ON DELETE CASCADE
);

COMMENT ON TABLE ReportTableData IS '$Id: reporttabledata.sql,v 1.4 2006/04/25 07:53:30 arseniy Exp $';

CREATE SEQUENCE ReportTableData_Seq ORDER;

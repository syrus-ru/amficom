--$Id: reporttemplate.sql,v 1.5.2.2 2006/04/10 10:13:32 arseniy Exp $

CREATE TABLE ReportTemplate (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
 sheet_size NUMBER(3) NOT NULL,
 orientation NUMBER(1) NOT NULL,
 margin_size NUMBER(10) NOT NULL,
 destination_module VARCHAR2(64 CHAR) NOT NULL,
--
 CONSTRAINT reporttemplate_pk PRIMARY KEY (id),
 CONSTRAINT reporttemplate_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reporttemplate_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

COMMENT ON TABLE ReportTemplate IS '$Id: reporttemplate.sql,v 1.5.2.2 2006/04/10 10:13:32 arseniy Exp $';

CREATE SEQUENCE ReportTemplate_Seq ORDER;

-- $Id: attachedtext.sql,v 1.4.2.1 2006/02/14 10:32:49 arseniy Exp $

CREATE TABLE AttachedText (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 location_x NUMBER(10)NOT NULL,
 location_y NUMBER(10) NOT NULL,
 width NUMBER(10) NOT NULL,
 height NUMBER(10) NOT NULL,
 report_template_id,
--
 text VARCHAR2(4000) NOT NULL,
 distance_x NUMBER(10) NOT NULL,
 distance_y NUMBER(10) NOT NULL,
 font_name VARCHAR2(64 CHAR) NOT NULL,
 font_syle NUMBER(10) NOT NULL,
 font_size NUMBER(10) NOT NULL,
 vertical_attach_type NUMBER(3) NOT NULL,
 horizontal_attach_type NUMBER(3) NOT NULL,
 vertical_attacher_id	NUMBER(19),
 horizontal_attacher_id NUMBER(19),
--
 CONSTRAINT attachedtext_pk PRIMARY KEY (id),
 CONSTRAINT attachedtext_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT attachedtext_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT attachedtext_reptempl_fk FOREIGN KEY (report_template_id)
  REFERENCES ReportTemplate (id) ON DELETE CASCADE
--  ,
-- CONSTRAINT vertical_att_id_fk FOREIGN KEY (vertical_attacher_id)
--  REFERENCES ReportData (id) ON DELETE CASCADE,
-- CONSTRAINT horizontal_att_id_fk FOREIGN KEY (horizontal_attacher_id)
-- REFERENCES ReportData (id) ON DELETE CASCADE
);

COMMENT ON TABLE AttachedText IS '$Id: attachedtext.sql,v 1.4.2.1 2006/02/14 10:32:49 arseniy Exp $';

CREATE SEQUENCE AttachedText_Seq ORDER;

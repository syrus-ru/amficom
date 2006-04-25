--$Id: reportimage.sql,v 1.5 2006/04/25 07:53:30 arseniy Exp $

CREATE TABLE ReportImage (
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
 bitmap_image_res_id NOT NULL,
--
 CONSTRAINT reportimage_pk PRIMARY KEY (id),
 CONSTRAINT reportimage_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reportimage_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT reportimage_img_fk FOREIGN KEY (bitmap_image_res_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE,
 CONSTRAINT reportimage_reptempl_fk FOREIGN KEY (report_template_id)
  REFERENCES ReportTemplate (id) ON DELETE CASCADE
);

COMMENT ON TABLE ReportImage IS '$Id: reportimage.sql,v 1.5 2006/04/25 07:53:30 arseniy Exp $';

CREATE SEQUENCE ReportImage_Seq ORDER;

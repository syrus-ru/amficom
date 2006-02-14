-- $Id: testanatmpllink.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE TestAnaTmplLink (
 test_id NOT NULL,
 analysis_template_id NOT NULL,
--
 CONSTRAINT tatl_uniq UNIQUE (test_id, analysis_template_id),
 CONSTRAINT tatl_t_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT tatl_actmpl_fk FOREIGN KEY (analysis_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);

-- $Id: testmeastmpllink.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE TestMeasTmplLink (
 test_id NOT NULL,
 measurement_template_id NOT NULL,
--
 CONSTRAINT tmtl_uniq UNIQUE (test_id, measurement_template_id),
 CONSTRAINT tmtl_t_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT tmtl_actmpl_fk FOREIGN KEY (measurement_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);

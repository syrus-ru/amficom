-- $Id: actpartmpllink.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE ActParTmplLink (
 action_parameter_id NOT NULL,
 action_template_id NOT NULL,
--
 CONSTRAINT aptmpl_uniq UNIQUE (action_parameter_id, action_template_id),
 CONSTRAINT aptmpl_ap_fk FOREIGN KEY (action_parameter_id)
  REFERENCES ActionParameter (id) ON DELETE CASCADE,
 CONSTRAINT aptmpl_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);

-- $Id: actmplparlink.sql,v 1.1.2.1 2006/02/15 19:41:00 arseniy Exp $

CREATE TABLE AcTmplParLink (
 action_template_id NOT NULL,
 action_parameter_id NOT NULL,
--
 CONSTRAINT aptmplp_uniq UNIQUE (action_template_id, action_parameter_id),
 CONSTRAINT aptmplp_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE,
 CONSTRAINT aptmplp_ap_fk FOREIGN KEY (action_parameter_id)
  REFERENCES ActionParameter (id) ON DELETE CASCADE
);

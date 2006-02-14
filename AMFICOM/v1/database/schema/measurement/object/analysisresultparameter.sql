-- $Id: analysisresultparameter.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE AnalysisResultParameter (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 analysis_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT arp_pk PRIMARY KEY (id),
 CONSTRAINT arp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT arp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT arp_pt_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT arp_a_fk FOREIGN KEY (analysis_id)
  REFERENCES Analysis (id) ON DELETE CASCADE
);

CREATE SEQUENCE AnalysisResultParameter_seq ORDER;

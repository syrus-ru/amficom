CREATE OR REPLACE TYPE TimeQuantum AS OBJECT (
 scale NUMBER(2),
 value NUMBER(3)
)
/

CREATE OR REPLACE TYPE DayTime AS OBJECT (
 hour NUMBER(2),
 minute NUMBER(2),
 second NUMBER(2)
)
/

CREATE OR REPLACE TYPE DayTimeArray AS TABLE OF DayTime
/

CREATE OR REPLACE TYPE TimeQuantumArray AS TABLE OF TimeQuantum
/

CREATE OR REPLACE TYPE TemporalTemplate AS OBJECT (
 period TimeQuantum,
 day_times DayTimeArray,
 dates TimeQuantumArray
)
/

CREATE OR REPLACE TYPE TimeStampArray AS TABLE OF DATE
/


CREATE TABLE PTTemporalTemplate (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 description VARCHAR2(256),
 value TemporalTemplate,

 CONSTRAINT pttt_pk PRIMARY KEY(id) ENABLE,
 CONSTRAINT pttt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT pttt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
) 
 NESTED TABLE value.day_times STORE AS day_times_tab 
 NESTED TABLE value.dates STORE AS dates_tab;

CREATE SEQUENCE pttemporaltemplate_seq ORDER;

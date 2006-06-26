-- $Id: DROP.sql,v 1.6 2006/06/26 12:44:39 bass Exp $

-- 06. LineMismatchEvent
PROMPT 06. Dropping table LineMismatchEvent...;
DROP TABLE ChangeLogRecord;
DROP TABLE LineMismatchEvent;
DROP SEQUENCE LineMismatchEvent_Seq;

-- 05. ReflectogramMismatchEvent
PROMPT 05. Dropping table ReflectogramMismatchEvent...;
DROP TABLE ReflectogramMismatchEvent;
DROP SEQUENCE ReflectogramMismatchEvent_Seq;

-- 04. DeliveryAttributes
PROMPT 04. Dropping table DeliveryAttributes...;
DROP TABLE DeliveryAttributesUserLink;
DROP TABLE DeliveryAttributesRoleLink;
DROP TABLE DeliveryAttributes;
DROP SEQUENCE DeliveryAttributes_Seq;

-- 03. EventParameter
PROMPT 03. Dropping table EventParameter...;
DROP TABLE EventParameter;
DROP SEQUENCE EventParameter_Seq;

-- 02. EventSource
PROMPT 02. Dropping table EventSource...;
DROP TABLE EventSourceLink;
DROP TABLE EventSource;
DROP SEQUENCE EventSource_Seq;

-- 01. Event
PROMPT 01. Dropping table Event...;
DROP TABLE Event;
DROP SEQUENCE Event_Seq;

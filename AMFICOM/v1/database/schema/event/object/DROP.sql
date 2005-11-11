-- $Id: DROP.sql,v 1.3 2005/11/11 02:26:12 bass Exp $

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

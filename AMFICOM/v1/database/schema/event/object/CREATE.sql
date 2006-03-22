-- $Id: CREATE.sql,v 1.4 2006/03/22 08:53:27 bass Exp $

-- 01. Event
PROMPT 01. Creating table Event...;
@@event;

-- 02. EventSource
PROMPT 02. Creating table EventSource...;
@@eventsource;

-- 03. EventParameter
PROMPT 03. Creating table EventParameter...;
@@eventparameter;

-- 04. DeliveryAttributes
PROMPT 04. Creating table DeliveryAttributes...;
@@deliveryattributes;

-- 05. ReflectogramMismatchEvent
PROMPT 04. Creating table ReflectogramMismatchEvent...;
@@reflectogrammismatchevent;

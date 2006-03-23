-- $Id: CREATE.sql,v 1.5 2006/03/23 16:12:24 bass Exp $

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

-- 06. LineMismatchEvent
PROMPT 05. Creating table LineMismatchEvent...;
@@linemismatchevent;

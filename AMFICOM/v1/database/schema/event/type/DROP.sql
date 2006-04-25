-- $Id: DROP.sql,v 1.4 2006/04/25 08:06:04 arseniy Exp $

PROMPT 03. Dropping table EventTypeUserAlert...;
DROP TABLE EventTypeUserAlert;

PROMPT 02. Dropping table EventTypParTypLink...;
DROP TABLE EventTypParTypLink;

PROMPT 01. Dropping table EventType...;
DROP TABLE EventType;
DROP SEQUENCE EventType_seq;

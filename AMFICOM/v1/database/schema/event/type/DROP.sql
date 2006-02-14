-- $Id: DROP.sql,v 1.3.2.1 2006/02/14 11:08:36 arseniy Exp $

PROMPT 03. Dropping table EventTypeUserAlert...;
DROP TABLE EventTypeUserAlert;

PROMPT 02. Dropping table EventTypParTypLink...;
DROP TABLE EventTypParTypLink;

PROMPT 01. Dropping table EventType...;
DROP TABLE EventType;
DROP SEQUENCE EventType_seq;

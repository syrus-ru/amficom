-- $Id: DROP.sql,v 1.7 2006/04/25 07:53:20 arseniy Exp $

PROMPT 03. Dropping table LayoutItem...;
DROP TABLE LayoutItem;
DROP SEQUENCE LayoutItem_Seq;

PROMPT 02. Dropping table ImportUIDMap...;
DROP TABLE ImportUIDMap;

PROMPT 01. Dropping table ImageResource...;
DROP TABLE ImageResource;
DROP SEQUENCE ImageResource_Seq;

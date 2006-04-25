-- $Id: DROP.sql,v 1.6 2006/04/25 07:50:07 arseniy Exp $

PROMPT 03. Dropping table SiteNodeType...;
DROP TABLE SiteNodeType;
DROP SEQUENCE SiteNodeType_Seq;

PROMPT 02. Dropping table PhysicalLinkType...;
DROP TABLE PhysicalLinkType;
DROP SEQUENCE PhysicalLinkType_Seq;

PROMPT 01. Dropping table MapLibrary...;
DROP TABLE MapLibrary;
DROP SEQUENCE MapLibrary_Seq;

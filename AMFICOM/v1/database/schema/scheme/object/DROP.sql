-- $Id: DROP.sql,v 1.2 2005/02/04 15:26:50 bass Exp $

-- 04. SchemeElement
PROMPT 04. Dropping table "SchemeElement"...;
ALTER TABLE "Scheme" DROP CONSTRAINT scheme_prnt_scheme_element_fk;
DROP TABLE "SchemeElement";
DROP SEQUENCE "SchemeElement_Seq";

-- 03. Scheme
PROMPT 03. Dropping table "Scheme"...;
DROP TABLE "Scheme";
DROP SEQUENCE "Scheme_Seq";

-- 02. SchemeProtoElement
PROMPT 02. Dropping table "SchemeProtoElement"...;
DROP TABLE "SchemeProtoElement";
DROP SEQUENCE "SchemeProtoElement_Seq";

-- 01. SchemeProtoGroup
PROMPT 01. Dropping table "SchemeProtoGroup"...;
DROP TABLE "SchemeProtoGroup";
DROP SEQUENCE "SchemeProtoGroup_Seq";

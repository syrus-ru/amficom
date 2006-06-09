-- $Id: DROP.sql,v 1.10.2.1 2006/06/09 16:40:09 arseniy Exp $

PROMPT 06. Dropping table PermissionAttributes...;
DROP TABLE PermissionAttributes;
DROP SEQUENCE PermissionAttributes_Seq;

PROMPT 05. Dropping table SystemUserRoleLink...;
DROP TABLE SystemUserRoleLink;

PROMPT 04. Dropping table Role...;
DROP TABLE Role;
DROP SEQUENCE Role_Seq;

PROMPT 03. Dropping table MCM...;
DROP TABLE MCM;
DROP SEQUENCE MCM_Seq;

PROMPT 02. Dropping table Server...;
DROP TABLE Server;
DROP SEQUENCE Server_Seq;

PROMPT 01. Dropping table Domain...;
DROP TABLE Domain;
DROP SEQUENCE Domain_Seq;

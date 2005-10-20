-- $Id: DROP.sql,v 1.8 2005/10/20 11:08:35 bob Exp $

DROP TABLE PermissionAttributes;
DROP TABLE SystemUserRoleLink;
DROP TABLE Role;
DROP TABLE ServerProcess;
DROP TABLE MCM;
DROP TABLE Server;
DROP TABLE Domain;

DROP TABLE PermissionAttributes_seq;
DROP SEQUENCE Role_seq; 
DROP SEQUENCE ServerProcess_seq;
DROP SEQUENCE mcm_seq;
DROP SEQUENCE server_seq;
DROP SEQUENCE domain_seq;

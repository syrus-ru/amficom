-- $Id: DROP.sql,v 1.7 2005/10/10 15:31:35 bob Exp $

DROP TABLE SystemUserRoleLink;
DROP TABLE Role;
DROP TABLE ServerProcess;
DROP TABLE MCM;
DROP TABLE Server;
DROP TABLE Domain;

DROP SEQUENCE Role_seq; 
DROP SEQUENCE ServerProcess_seq;
DROP SEQUENCE mcm_seq;
DROP SEQUENCE server_seq;
DROP SEQUENCE domain_seq;

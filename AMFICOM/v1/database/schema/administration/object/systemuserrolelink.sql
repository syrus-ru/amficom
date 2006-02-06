-- $Id: systemuserrolelink.sql,v 1.1 2005/12/01 12:42:57 arseniy Exp $

CREATE TABLE SystemUserRoleLink (
 system_user_id NOT NULL,
 role_id NOT NULL,
--
 CONSTRAINT sysusrollnk_user_fk FOREIGN KEY (system_user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sysusrollnk_role_fk FOREIGN KEY (role_id)
  REFERENCES Role (id) ON DELETE CASCADE
);

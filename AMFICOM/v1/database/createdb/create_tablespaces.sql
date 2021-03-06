CREATE TABLESPACE users_cb
 DATAFILE
  '/u11/oradata/mongol/users_cb11.dbf' SIZE 500M REUSE AUTOEXTEND ON NEXT 10M MAXSIZE 2000M,
  '/u12/oradata/mongol/users_cb12.dbf' SIZE 500M REUSE AUTOEXTEND ON NEXT 10M MAXSIZE 2000M,
  '/u13/oradata/mongol/users_cb13.dbf' SIZE 500M REUSE AUTOEXTEND ON NEXT 10M MAXSIZE 2000M,
  '/u14/oradata/mongol/users_cb14.dbf' SIZE 500M REUSE AUTOEXTEND ON NEXT 10M MAXSIZE 2000M
 LOGGING
 ONLINE
 EXTENT MANAGEMENT LOCAL AUTOALLOCATE
 SEGMENT SPACE MANAGEMENT AUTO;

CREATE TEMPORARY TABLESPACE temp_cb
 TEMPFILE
  '/u11/oradata/mongol/temp_cb11.dbf' SIZE 10M REUSE AUTOEXTEND ON NEXT 1M MAXSIZE 100M,
  '/u12/oradata/mongol/temp_cb12.dbf' SIZE 10M REUSE AUTOEXTEND ON NEXT 1M MAXSIZE 100M,
  '/u13/oradata/mongol/temp_cb13.dbf' SIZE 10M REUSE AUTOEXTEND ON NEXT 1M MAXSIZE 100M,
  '/u14/oradata/mongol/temp_cb14.dbf' SIZE 10M REUSE AUTOEXTEND ON NEXT 1M MAXSIZE 100M
 EXTENT MANAGEMENT LOCAL;

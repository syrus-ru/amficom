INSERT INTO alarmlevel (id, name, comments)
 VALUES (0, 'ALARM_LEVEL_NONE', 'Норма');
INSERT INTO alarmlevel (id, name, comments)
 VALUES (1, 'ALARM_LEVEL_SOFT', 'Предупреждение');
INSERT INTO alarmlevel (id, name, comments)
 VALUES (2, 'ALARM_LEVEL_HARD', 'Тревога');

COMMIT;

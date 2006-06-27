INSERT INTO measurementstatus (id, name, comments)
 VALUES (0, 'MEASUREMENT_STATUS_SCHEDULED', 'Измерение готово к выполнению');
INSERT INTO measurementstatus (id, name, comments)
 VALUES (1, 'MEASUREMENT_STATUS_PROCESSING', 'Измерение выполняется');
INSERT INTO measurementstatus (id, name, comments)
 VALUES (2, 'MEASUREMENT_STATUS_COMPLETED', 'Измерение завершено');
INSERT INTO measurementstatus (id, name, comments)
 VALUES (3, 'MEASUREMENT_STATUS_ABORTED', 'Измерение прервано');

COMMIT;

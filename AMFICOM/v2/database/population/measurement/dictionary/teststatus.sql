INSERT INTO teststatus (id, name, comments)
 VALUES (0, 'TEST_STATUS_SCHEDULED', 'Тест готов к выполнению');
INSERT INTO teststatus (id, name, comments)
 VALUES (1, 'TEST_STATUS_PROCESSING', 'Тест выполняется');
INSERT INTO teststatus (id, name, comments)
 VALUES (2, 'TEST_STATUS_COMPLETED', 'Тест завершён');
INSERT INTO teststatus (id, name, comments)
 VALUES (3, 'TEST_STATUS_ABORTED', 'Тест прерван');

COMMIT;

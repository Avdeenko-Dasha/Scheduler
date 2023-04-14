INSERT INTO worker (id, surname, initials)
VALUES (1, 'Авдеенко', 'Д.О.'),
       (2, 'Миронов', 'Е.И.');

INSERT INTO task (id, done, start_date, finish_date, completion_date, name, id_worker)
VALUES (1, 0, '2023-01-19', '2023-02-20', '2023-02-09', 'Test', 1),
       (2, 1, '2023-02-24', '2023-03-26', '2023-03-18', 'Test', 1),
       (3, 0, '2023-03-19', '2023-04-15', '2023-03-22', 'Test', 2),
       (4, 1, '2023-01-05', '2023-02-10', '2023-01-26', 'Test', 2);
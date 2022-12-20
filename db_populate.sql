SET session_replication_role = replica;
DO $$ DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
        EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;
INSERT INTO lhd.classrooms (id, name) OVERRIDING SYSTEM VALUE VALUES (3, 'Random0.27399662853769461') on conflict do nothing;
INSERT INTO lhd.classrooms (id, name) OVERRIDING SYSTEM VALUE VALUES (4, 'TestClassroom') on conflict do nothing;
INSERT INTO lhd.groups (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'Groupe A0.20395765192045456') on conflict do nothing;
INSERT INTO lhd.groups (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'Groupe A0.75627668576376681111') on conflict do nothing;
INSERT INTO lhd.subjects (id, name, hour_count_max) OVERRIDING SYSTEM VALUE VALUES (1, 'Matiere A0.7384491331125208', 150) on conflict do nothing;
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 3) on conflict do nothing;
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (2, 3) on conflict do nothing;
INSERT INTO lhd.users (id, name, fname, email, password) OVERRIDING SYSTEM VALUE VALUES (9, 'TheTestStudent', 'UnitTestFirstName', 'UnitTestName.Firstname@email.com', '1234') on conflict do nothing;
INSERT INTO lhd.users (id, name, fname, email, password) OVERRIDING SYSTEM VALUE VALUES (1, 'Dupont', 'Martin', 'martin.dupont@lhd.org', 'password') on conflict do nothing;
INSERT INTO lhd.group_user (id_group, id_user) VALUES (1, 1) on conflict do nothing;
INSERT INTO lhd.professors (id, name, fname, email, password, title) OVERRIDING SYSTEM VALUE VALUES (1, 'UnitTest1', 'UnitTestFirstName1', 'UnitTestName.Firstname0.5848029661921753@email.com1', '1234', 'REseracher') on conflict do nothing;
INSERT INTO lhd.professors (id, name, fname, email, password, title) OVERRIDING SYSTEM VALUE VALUES (4, 'test', 'test', 'test@lhd.org', '1234', 'PHD') on conflict do nothing;
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (1, 3) on conflict do nothing;
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (4, 3) on conflict do nothing;
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (3, '["2022-12-19 20:41:31.72463+01","2022-12-19 21:41:31.72463+01")', 3, NULL, 1, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (4, '["2022-12-19 20:41:38.174491+01","2022-12-19 21:41:38.174491+01")', 4, NULL, 1, 'TD');

--
-- met à jour les séquence d'ID.
--
SELECT pg_catalog.setval('lhd.admins_id_seq', 1, false);
SELECT pg_catalog.setval('lhd.classrooms_id_seq', 25, true);
SELECT pg_catalog.setval('lhd.groups_id_seq', 3, true);
SELECT pg_catalog.setval('lhd.professors_id_seq', 5, true);
SELECT pg_catalog.setval('lhd.slots_id_seq', 5, true);
SELECT pg_catalog.setval('lhd.subject_id_seq', 10, true);
SELECT pg_catalog.setval('lhd.users_id_seq', 10, true);


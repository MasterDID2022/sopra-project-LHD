--
-- PostgreSQL database dump
--

-- Dumped from database version 14.6
-- Dumped by pg_dump version 14.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP SCHEMA IF EXISTS lhd CASCADE;
--
-- Name: lhd; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA lhd;
ALTER DATABASE lhd SET search_path TO lhd;

--
-- Name: btree_gist; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA lhd;
CREATE EXTENSION IF NOT EXISTS pgcrypto  WITH SCHEMA lhd;

--
-- Name: EXTENSION btree_gist; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION btree_gist IS 'support for indexing common datatypes in GiST';


--
-- Name: email; Type: DOMAIN; Schema: lhd; Owner: -
--

CREATE DOMAIN lhd.email AS character varying(254)
	CONSTRAINT email_check CHECK (((VALUE)::text ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::text));


--
-- Name: check_date_overlap_trigger_function_group_slot(); Type: FUNCTION; Schema: lhd; Owner: -
--

CREATE FUNCTION lhd.check_date_overlap_trigger_function_group_slot() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF EXISTS(select from slots  s
           where id=new.id_slot--- we select the timerange from the id in the query
            and exists (
                    select from group_slot gs--- then we check whether there exists an overlapping one
                    join slots s2 on gs.id_slot=s2.id
                    where gs.id_group = new.id_group
                    and s2.id <> new.id_slot--- we don't wanna compare the slot specified in the query to itself
                    and s.timerange && s2.timerange
                       ))

       THEN
            RAISE integrity_constraint_violation USING MESSAGE = 'Overlapping timeranges for group : ' || new.id_group;
       END IF;
   RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_function_professor_slot(); Type: FUNCTION; Schema: lhd; Owner: -
--

CREATE FUNCTION lhd.check_date_overlap_trigger_function_professor_slot() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF EXISTS(select from slots  s
           where id=new.id_slot--- we select the timerange from the id in the query
            and exists (
                    select from professor_slot ls--- then we check whether there exists an overlapping one
                    join slots s2 on ls.id_slot=s2.id
                    where ls.id_professor = new.id_professor
                    and s2.id <> new.id_professor--- we don't wanna compare the slot specified in the query to itself
                    and s.timerange && s2.timerange))

       THEN
            RAISE integrity_constraint_violation USING MESSAGE = 'Overlapping timeranges for group : ' || new.id_professor;
       END IF;
   RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_hook(); Type: FUNCTION; Schema: lhd; Owner: -
--

CREATE FUNCTION lhd.check_date_overlap_trigger_hook() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF EXISTS(select from slots  s
       where id=new.id_slot and exists (
        select from group_slot gs2
        join slots s2 on gs2.id_slot=s2.id
        where gs2.id_group =new.id_group
             and s2.id <> new.id_slot
             and s.timerange && s2.timerange)) THEN
                   RAISE EXCEPTION 'Timerange is overlapping between two courses';
               END IF;
           RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_slot_update_group(); Type: FUNCTION; Schema: lhd; Owner: -
--

CREATE FUNCTION lhd.check_date_overlap_trigger_slot_update_group() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin

       IF EXISTS (select from slots s join group_slot gs
       on s.id=gs.id_slot
       where s.id=new.id
     and exists (
        select * from slots s2 join group_slot gs2
        on s2.id=gs2.id_slot
        where gs2.id_group = gs.id_group
            and s2.id <> new.id
            and s2.timerange && new.timerange))

       THEN
            RAISE unique_violation USING MESSAGE = 'Unavailable group on slot : ' || new.id;
       END IF;
   RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_slot_update_professor(); Type: FUNCTION; Schema: lhd; Owner: -
--

CREATE FUNCTION lhd.check_date_overlap_trigger_slot_update_professor() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin

       IF EXISTS (select from slots s join professor_slot gp
       on s.id=gp.id_slot
       where s.id=new.id
     and exists (
        select * from slots s2 join professor_slot gp2
        on s2.id=gp2.id_slot
        where gp2.id_professor = gp.id_professor
            and s2.id <> new.id
            and s2.timerange && new.timerange))

       THEN
            RAISE integrity_constraint_violation USING MESSAGE = 'Unavailable group on slot : ' || new.id;
       END IF;
   RETURN NEW;
       end
       $$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: users; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.users (
    id bigint NOT NULL,
    name character varying NOT NULL,
    fname character varying NOT NULL,
    email lhd.email NOT NULL,
    password character varying NOT NULL
);


--
-- Name: admins; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.admins (
    dpt character varying
)
INHERITS (lhd.users);


--
-- Name: classrooms; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.classrooms (
    id bigint NOT NULL,
    name character varying NOT NULL
);


--
-- Name: classrooms_id_seq; Type: SEQUENCE; Schema: lhd; Owner: -
--

ALTER TABLE lhd.classrooms ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME lhd.classrooms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: group_slot; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.group_slot (
    id_group bigint NOT NULL,
    id_slot bigint NOT NULL
);


--
-- Name: group_user; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.group_user (
    id_group bigint NOT NULL,
    id_user bigint NOT NULL
);


--
-- Name: groups; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.groups (
    id bigint NOT NULL,
    name character varying NOT NULL
);


--
-- Name: groups_id_seq; Type: SEQUENCE; Schema: lhd; Owner: -
--

ALTER TABLE lhd.groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME lhd.groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: professor_slot; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.professor_slot (
    id_professor bigint NOT NULL,
    id_slot bigint NOT NULL
);


--
-- Name: professors; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.professors (
    title character varying NOT NULL
)
INHERITS (lhd.users);


--
-- Name: slots; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.slots (
    id bigint NOT NULL,
    timerange tstzrange,
    classroom bigint,
    memo character varying,
    subject bigint NOT NULL,
    type character varying,
    CONSTRAINT timerange_inc_exc CHECK ((lower_inc(timerange) AND (NOT upper_inc(timerange)))),
    CONSTRAINT timerange_not_empty_check CHECK ((NOT isempty(timerange)))
);


--
-- Name: slots_id_seq; Type: SEQUENCE; Schema: lhd; Owner: -
--

ALTER TABLE lhd.slots ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME lhd.slots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: subjects; Type: TABLE; Schema: lhd; Owner: -
--

CREATE TABLE lhd.subjects (
    id bigint NOT NULL,
    name character varying NOT NULL,
    hour_count_max double precision NOT NULL
);


--
-- Name: subject_id_seq; Type: SEQUENCE; Schema: lhd; Owner: -
--

ALTER TABLE lhd.subjects ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME lhd.subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: lhd; Owner: -
--

ALTER TABLE lhd.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME lhd.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: admins id; Type: DEFAULT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.admins ALTER COLUMN id SET DEFAULT nextval('lhd.users_id_seq'::regclass);


--
-- Name: professors id; Type: DEFAULT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.professors ALTER COLUMN id SET DEFAULT nextval('lhd.users_id_seq'::regclass);


--
-- Name: admins admins_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.admins
    ADD CONSTRAINT admins_pkey PRIMARY KEY (id);


--
-- Name: classrooms classroom_unique; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.classrooms
    ADD CONSTRAINT classroom_unique UNIQUE (name);


--
-- Name: slots slot_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.slots
    ADD CONSTRAINT slot_pkey PRIMARY KEY (id);


--
-- Name: groups group_unique; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.groups
    ADD CONSTRAINT group_unique UNIQUE (name);


--
-- Name: professors prof_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.professors
    ADD CONSTRAINT prof_pkey PRIMARY KEY (id);


--
-- Name: group_slot promotion_slot_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.group_slot
    ADD CONSTRAINT promotion_slot_pkey PRIMARY KEY (id_group, id_slot);


--
-- Name: professor_slot promotion_professors_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.professor_slot
    ADD CONSTRAINT promotion_professors_pkey PRIMARY KEY (id_professor, id_slot);


--
-- Name: group_user promotion_user_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.group_user
    ADD CONSTRAINT promotion_user_pkey PRIMARY KEY (id_group, id_user);


--
-- Name: classrooms salle_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.classrooms
    ADD CONSTRAINT salle_pkey PRIMARY KEY (id);


--
-- Name: slots slots_classroom_timerange_excl; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.slots
    ADD CONSTRAINT slots_classroom_timerange_excl EXCLUDE USING gist (classroom WITH =, timerange WITH &&);


--
-- Name: subjects subject_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.subjects
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- Name: subjects subject_unique; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.subjects
    ADD CONSTRAINT subject_unique UNIQUE (name);


--
-- Name: groups user_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.groups
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: admins_lower_email_key; Type: INDEX; Schema: lhd; Owner: -
--

CREATE UNIQUE INDEX admins_lower_email_key ON lhd.admins USING btree (lower((email)::text));


--
-- Name: group_slot_id_slot_idx; Type: INDEX; Schema: lhd; Owner: -
--

CREATE INDEX group_slot_id_slot_idx ON lhd.group_slot USING btree (id_slot);


--
-- Name: professors_lower_email_key; Type: INDEX; Schema: lhd; Owner: -
--

CREATE UNIQUE INDEX professors_lower_email_key ON lhd.professors USING btree (lower((email)::text));


--
-- Name: slots_timerange_idx; Type: INDEX; Schema: lhd; Owner: -
--

CREATE INDEX slots_timerange_idx ON lhd.slots USING btree (timerange);


--
-- Name: unqiue_promotion_name; Type: INDEX; Schema: lhd; Owner: -
--

CREATE UNIQUE INDEX unqiue_promotion_name ON lhd.groups USING btree (name);


--
-- Name: users_lower_email_key; Type: INDEX; Schema: lhd; Owner: -
--

CREATE UNIQUE INDEX users_lower_email_key ON lhd.users USING btree (lower((email)::text));


--
-- Name: group_slot check_timerange_overlap; Type: TRIGGER; Schema: lhd; Owner: -
--

CREATE TRIGGER check_timerange_overlap BEFORE INSERT OR UPDATE ON lhd.group_slot FOR EACH ROW EXECUTE FUNCTION lhd.check_date_overlap_trigger_function_group_slot();


--
-- Name: professor_slot ls_check_trigger; Type: TRIGGER; Schema: lhd; Owner: -
--

CREATE TRIGGER ls_check_trigger BEFORE INSERT OR UPDATE ON lhd.professor_slot FOR EACH ROW EXECUTE FUNCTION lhd.check_date_overlap_trigger_function_professor_slot();


--
-- Name: slots update_slot_check_group; Type: TRIGGER; Schema: lhd; Owner: -
--

CREATE TRIGGER update_slot_check_group BEFORE UPDATE ON lhd.slots FOR EACH ROW WHEN ((NOT (old.timerange @> new.timerange))) EXECUTE FUNCTION lhd.check_date_overlap_trigger_slot_update_group();


--
-- Name: slots update_slot_check_professor; Type: TRIGGER; Schema: lhd; Owner: -
--

CREATE TRIGGER update_slot_check_professor BEFORE UPDATE ON lhd.slots FOR EACH ROW WHEN ((NOT (old.timerange @> new.timerange))) EXECUTE FUNCTION lhd.check_date_overlap_trigger_slot_update_professor();


--
-- Name: group_slot fk_slot; Type: FK CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.group_slot
    ADD CONSTRAINT fk_slot FOREIGN KEY (id_slot) REFERENCES lhd.slots(id) ON DELETE CASCADE;


--
-- Name: professor_slot fk_slot; Type: FK CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.professor_slot
    ADD CONSTRAINT fk_slot FOREIGN KEY (id_slot) REFERENCES lhd.slots(id) ON DELETE CASCADE;


--
-- Name: group_user fk_promotions; Type: FK CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.group_user
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_group) REFERENCES lhd.groups(id) ON DELETE CASCADE;


--
-- Name: group_slot fk_promotions; Type: FK CONSTRAINT; Schema: lhd; Owner: -
--

ALTER TABLE ONLY lhd.group_slot
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_group) REFERENCES lhd.groups(id) ON DELETE CASCADE;




ALTER TABLE ONLY lhd.professor_slot
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_professor) REFERENCES lhd.professors(id) ON DELETE CASCADE;




ALTER TABLE ONLY lhd.group_user
    ADD CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES lhd.users(id) ON DELETE CASCADE;




ALTER TABLE ONLY lhd.slots
    ADD CONSTRAINT salle_fkey FOREIGN KEY (classroom) REFERENCES lhd.classrooms(id) ON DELETE CASCADE;




ALTER TABLE ONLY lhd.slots
    ADD CONSTRAINT subject_fkey FOREIGN KEY (subject) REFERENCES lhd.subjects(id) ON DELETE CASCADE;



SET session_replication_role = replica;
INSERT INTO lhd.classrooms (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'U001 - M1 Info-DID');
INSERT INTO lhd.classrooms (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'Z001 - L1 Physique');




INSERT INTO lhd.groups (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'M1 informatique DID');
INSERT INTO lhd.groups (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'L1');




INSERT INTO lhd.subjects (id, name, hour_count_max) OVERRIDING SYSTEM VALUE VALUES (6, 'I131 Protect donnees', 61.5);
INSERT INTO lhd.subjects (id, name, hour_count_max) OVERRIDING SYSTEM VALUE VALUES (7, 'I122 Base de l apprentissage', 33);
INSERT INTO lhd.subjects (id, name, hour_count_max) OVERRIDING SYSTEM VALUE VALUES (8, 'I815 Apprentissage', 33);



INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (10, '["2022-12-26 10:00:00.72463+01","2022-12-26 12:00:00.72463+01")', 1, NULL, 6, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (11, '["2022-12-26 13:00:00.72463+01","2022-12-26 16:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (12, '["2022-12-27 11:35:00.72463+01","2022-12-27 12:35:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (13, '["2022-12-28 09:00:00.72463+01","2022-12-28 11:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (14, '["2022-12-30 10:00:00.72463+01","2022-12-30 13:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (15, '["2023-01-03 08:00:00.72463+01","2023-01-03 11:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (16, '["2023-01-03 15:00:00.72463+01","2023-01-03 17:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (17, '["2023-01-05 08:00:00.72463+01","2023-01-05 10:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (18, '["2023-01-06 15:00:00.72463+01","2023-01-06 17:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (19, '["2023-01-02 10:00:00.72463+01","2023-01-02 12:00:00.72463+01")', 1, NULL, 6, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (20, '["2023-01-02 13:00:00.72463+01","2023-01-02 16:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (21, '["2023-01-03 11:35:00.72463+01","2023-01-03 12:35:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (22, '["2023-01-04 09:00:00.72463+01","2023-01-04 11:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (23, '["2023-01-06 10:00:00.72463+01","2023-01-06 13:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (24, '["2023-01-10 08:00:00.72463+01","2023-01-10 11:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (25, '["2023-01-10 15:00:00.72463+01","2023-01-10 17:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (26, '["2023-01-12 08:00:00.72463+01","2023-01-12 10:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (27, '["2023-01-13 15:00:00.72463+01","2023-01-13 17:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (28, '["2023-01-09 10:00:00.72463+01","2023-01-09 12:00:00.72463+01")', 1, NULL, 6, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (29, '["2023-01-09 13:00:00.72463+01","2023-01-09 16:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (30, '["2023-01-10 11:35:00.72463+01","2023-01-10 12:35:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (31, '["2023-01-11 09:00:00.72463+01","2023-01-11 11:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (32, '["2023-01-13 10:00:00.72463+01","2023-01-13 13:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (33, '["2023-01-17 08:00:00.72463+01","2023-01-17 11:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (34, '["2023-01-17 15:00:00.72463+01","2023-01-17 17:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (35, '["2023-01-19 08:00:00.72463+01","2023-01-19 10:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (36, '["2023-01-20 15:00:00.72463+01","2023-01-20 17:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (37, '["2023-01-16 10:00:00.72463+01","2023-01-16 12:00:00.72463+01")', 1, NULL, 6, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (38, '["2023-01-16 13:00:00.72463+01","2023-01-16 16:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (39, '["2023-01-17 11:35:00.72463+01","2023-01-17 12:35:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (40, '["2023-01-18 09:00:00.72463+01","2023-01-18 11:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (41, '["2023-01-20 10:00:00.72463+01","2023-01-20 13:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (42, '["2023-01-24 08:00:00.72463+01","2023-01-24 11:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (43, '["2023-01-24 15:00:00.72463+01","2023-01-24 17:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (44, '["2023-01-26 08:00:00.72463+01","2023-01-26 10:00:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (45, '["2023-01-27 15:00:00.72463+01","2023-01-27 17:00:00.72463+01")', 2, NULL, 7, 'TP');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (46, '["2023-01-23 10:00:00.72463+01","2023-01-23 12:00:00.72463+01")', 1, NULL, 6, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (47, '["2023-01-23 13:00:00.72463+01","2023-01-23 16:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (48, '["2023-01-24 11:35:00.72463+01","2023-01-24 12:35:00.72463+01")', 2, NULL, 7, 'TD');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (49, '["2023-01-25 09:00:00.72463+01","2023-01-25 11:00:00.72463+01")', 2, NULL, 7, 'CM');
INSERT INTO lhd.slots (id, timerange, classroom, memo, subject, type) OVERRIDING SYSTEM VALUE VALUES (50, '["2023-01-27 10:00:00.72463+01","2023-01-27 13:00:00.72463+01")', 2, NULL, 7, 'TP');


INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 10);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 11);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 12);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 13);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 14);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 15);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 16);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 17);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 18);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 19);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 20);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 21);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 22);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 23);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 24);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 25);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 26);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 27);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 28);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 29);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 30);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 31);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 32);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 33);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 34);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 35);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 36);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 37);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 38);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 39);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 40);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 41);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 42);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 43);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 44);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 45);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 46);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 47);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 48);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 49);
INSERT INTO lhd.group_slot (id_group, id_slot) VALUES (1, 50);



INSERT INTO lhd.users (id, name, fname, email, password) OVERRIDING SYSTEM VALUE VALUES (1, 'Hafsaoui', 'Théo', 'Theo.hafsaoui@superEmail.com', '$2a$06$f16jdGDx4U/hcrWKRt9PJO8plz3B0EpuWdF1Jnnnwn3QRaipQ3Wqe');
INSERT INTO lhd.users (id, name, fname, email, password) OVERRIDING SYSTEM VALUE VALUES (4, 'Dupont', 'Martin', 'martin.dupont@lhd.org', '$2a$06$Z4IaAiw1Cj92gjl3evW7dOlSYvYV2erfH4PZNeUmh1GQCG1dWahRy');
INSERT INTO lhd.admins VALUES (5,'TheTestAdmin','TheTestAdmin','test@test.lhd','$2a$06$.kkkbqpTkpwZgg3mZQOPB.hd0ptJNKM/oJ8aZ7LV5w3zvkhXOQZ3q','NO');


INSERT INTO lhd.group_user (id_group, id_user) VALUES (1, 1);



INSERT INTO lhd.professors (id, name, fname, email, password, title) VALUES (3, 'Mahe', 'Pierre', 'pierre-Mahe@univ-tln.fr', '$2a$06$BLL.KYHKUVJWrRHz2VuFGu34mO0qoN7IowmSJO3ifxj/SpTKa1C/y', 'Enseignant');
INSERT INTO lhd.professors (id, name, fname, email, password, title) VALUES (2, 'Robert', 'Jean-Marc smith', 'Jean-Marc@TobertSECU.fr', '$2a$06$PD3x2cqxwVugbgHZXvdJ6uCl4AkCcAwP2OyE5OzSGQ09IeIoh/jC6', 'Enseignant');



INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (2, 10);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 11);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 12);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 13);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 14);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 15);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 16);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 17);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 18);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 42);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 43);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 44);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 45);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 46);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 47);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 48);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 49);
INSERT INTO lhd.professor_slot (id_professor, id_slot) VALUES (3, 50);



SELECT pg_catalog.setval('lhd.classrooms_id_seq', 3, false);


SELECT pg_catalog.setval('lhd.groups_id_seq', 2, true);



SELECT pg_catalog.setval('lhd.slots_id_seq', 50, true);


SELECT pg_catalog.setval('lhd.subject_id_seq', 9, false);


SELECT pg_catalog.setval('lhd.users_id_seq', 4, true);

--
-- PostgreSQL database dump complete
--


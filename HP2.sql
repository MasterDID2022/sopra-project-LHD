--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

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

ALTER TABLE IF EXISTS ONLY public.slots DROP CONSTRAINT IF EXISTS subject_fkey;
ALTER TABLE IF EXISTS ONLY public.slots DROP CONSTRAINT IF EXISTS salle_fkey;
ALTER TABLE IF EXISTS ONLY public.group_user DROP CONSTRAINT IF EXISTS fk_user;
ALTER TABLE IF EXISTS ONLY public.slot_lecturer DROP CONSTRAINT IF EXISTS fk_promotions;
ALTER TABLE IF EXISTS ONLY public.group_slot DROP CONSTRAINT IF EXISTS fk_promotions;
ALTER TABLE IF EXISTS ONLY public.group_user DROP CONSTRAINT IF EXISTS fk_promotions;
ALTER TABLE IF EXISTS ONLY public.slot_lecturer DROP CONSTRAINT IF EXISTS fk_creneau;
ALTER TABLE IF EXISTS ONLY public.group_slot DROP CONSTRAINT IF EXISTS fk_creneau;
DROP TRIGGER IF EXISTS check_timerange_overlap ON public.group_slot;
DROP INDEX IF EXISTS public.users_lower_email_key;
DROP INDEX IF EXISTS public.unqiue_promotion_name;
DROP INDEX IF EXISTS public.slots_timerange_idx;
DROP INDEX IF EXISTS public.group_slot_id_trange_idx;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.groups DROP CONSTRAINT IF EXISTS user_pkey;
ALTER TABLE IF EXISTS ONLY public.subject DROP CONSTRAINT IF EXISTS subject_pkey;
ALTER TABLE IF EXISTS ONLY public.classrooms DROP CONSTRAINT IF EXISTS salle_pkey;
ALTER TABLE IF EXISTS ONLY public.group_user DROP CONSTRAINT IF EXISTS promotion_usager_pkey;
ALTER TABLE IF EXISTS ONLY public.slot_lecturer DROP CONSTRAINT IF EXISTS promotion_lecturers_pkey;
ALTER TABLE IF EXISTS ONLY public.group_slot DROP CONSTRAINT IF EXISTS promotion_creneau_pkey;
ALTER TABLE IF EXISTS ONLY public.lecturers DROP CONSTRAINT IF EXISTS lect_pkey;
ALTER TABLE IF EXISTS ONLY public.slots DROP CONSTRAINT IF EXISTS excl;
ALTER TABLE IF EXISTS ONLY public.slots DROP CONSTRAINT IF EXISTS creneau_pkey;
DROP TABLE IF EXISTS public.subject;
DROP TABLE IF EXISTS public.slot_lecturer;
DROP TABLE IF EXISTS public.managers;
DROP TABLE IF EXISTS public.lecturers;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.groups;
DROP TABLE IF EXISTS public.group_user;
DROP TABLE IF EXISTS public.group_slot;
DROP TABLE IF EXISTS public.slots;
DROP TABLE IF EXISTS public.classrooms;
DROP FUNCTION IF EXISTS public.check_date_overlap_trigger_hook();
DROP FUNCTION IF EXISTS public.check_date_overlap_trigger_function_group_slot();
DROP DOMAIN IF EXISTS public.email;
DROP SCHEMA IF EXISTS public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;
CREATE EXTENSION IF NOT EXISTS btree_gist;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: email; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.email AS character varying(254)
	CONSTRAINT email_check CHECK (((VALUE)::text ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::text));


--
-- Name: check_date_overlap_trigger_function_group_slot(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_date_overlap_trigger_function_group_slot() RETURNS trigger
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
                    and s.timerange && s2.timerange  for share--- we use for share to prevent any modification while the trigger is run. 
                       ) 
       for share) 

       THEN
            RAISE unique_violation USING MESSAGE = 'Overlapping timeranges for group : ' || new.id_group;
       END IF;
   RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_hook(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_date_overlap_trigger_hook() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF EXISTS(select from slots  s where id=new.id_slot and exists (select from group_slot gs2 join slots s2 on gs2.id_slot=s2.id  where gs2.id_group =new.id_group and s2.id <> new.id_slot and s.timerange && s2.timerange limit 1 for share) limit 1 for share) THEN
                   RAISE EXCEPTION 'Timerange is overlapping between two courses';
               END IF;
           RETURN NEW;
       end
       $$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: classrooms; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.classrooms (
    id bigint NOT NULL,
    name character varying NOT NULL
);


--
-- Name: slots; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.slots (
    id bigint NOT NULL,
    timerange tstzrange,
    classroom bigint,
    memo character varying,
    subject bigint NOT NULL
);


--
-- Name: creneau_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.slots ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.creneau_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: group_slot; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.group_slot (
    id_group bigint NOT NULL,
    id_slot bigint NOT NULL
);


--
-- Name: group_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.group_user (
    id_group bigint NOT NULL,
    id_user bigint NOT NULL
);


--
-- Name: groups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.groups (
    id bigint NOT NULL,
    name character varying NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying NOT NULL,
    fname character varying NOT NULL,
    email character varying NOT NULL,
    password character varying NOT NULL
);


--
-- Name: lecturers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lecturers (
    title character varying NOT NULL
)
INHERITS (public.users);


--
-- Name: lecturers_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.lecturers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.lecturers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: managers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.managers (
    dpt character varying
)
INHERITS (public.users);


--
-- Name: promotion_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.promotion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: salle_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.classrooms ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.salle_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: slot_lecturer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.slot_lecturer (
    id_lecturer bigint NOT NULL,
    id_trange bigint NOT NULL
);


--
-- Name: subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    name character varying NOT NULL
);


--
-- Name: subject_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.subject ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: usager_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.usager_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: slots creneau_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slots
    ADD CONSTRAINT creneau_pkey PRIMARY KEY (id);


--
-- Name: slots excl; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slots
    ADD CONSTRAINT excl EXCLUDE USING gist (classroom WITH =, timerange WITH &&);


--
-- Name: lecturers lect_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers
    ADD CONSTRAINT lect_pkey PRIMARY KEY (id);


--
-- Name: group_slot promotion_creneau_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_slot
    ADD CONSTRAINT promotion_creneau_pkey PRIMARY KEY (id_group, id_slot);


--
-- Name: slot_lecturer promotion_lecturers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slot_lecturer
    ADD CONSTRAINT promotion_lecturers_pkey PRIMARY KEY (id_lecturer, id_trange);


--
-- Name: group_user promotion_usager_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_user
    ADD CONSTRAINT promotion_usager_pkey PRIMARY KEY (id_group, id_user);


--
-- Name: classrooms salle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT salle_pkey PRIMARY KEY (id);


--
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- Name: groups user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: group_slot_id_trange_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX group_slot_id_trange_idx ON public.group_slot USING btree (id_slot);


--
-- Name: slots_timerange_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX slots_timerange_idx ON public.slots USING btree (timerange);


--
-- Name: unqiue_promotion_name; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX unqiue_promotion_name ON public.groups USING btree (name);


--
-- Name: users_lower_email_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX users_lower_email_key ON public.users USING btree (lower((email)::text));


--
-- Name: group_slot check_timerange_overlap; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_timerange_overlap BEFORE INSERT OR UPDATE ON public.group_slot FOR EACH ROW EXECUTE FUNCTION public.check_date_overlap_trigger_function_group_slot();


--
-- Name: group_slot fk_creneau; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_slot
    ADD CONSTRAINT fk_creneau FOREIGN KEY (id_slot) REFERENCES public.slots(id);


--
-- Name: slot_lecturer fk_creneau; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slot_lecturer
    ADD CONSTRAINT fk_creneau FOREIGN KEY (id_trange) REFERENCES public.slots(id);


--
-- Name: group_user fk_promotions; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_user
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_group) REFERENCES public.groups(id);


--
-- Name: group_slot fk_promotions; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_slot
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_group) REFERENCES public.groups(id);


--
-- Name: slot_lecturer fk_promotions; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slot_lecturer
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_lecturer) REFERENCES public.lecturers(id);


--
-- Name: group_user fk_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_user
    ADD CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES public.users(id);


--
-- Name: slots salle_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slots
    ADD CONSTRAINT salle_fkey FOREIGN KEY (classroom) REFERENCES public.classrooms(id);


--
-- Name: slots subject_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.slots
    ADD CONSTRAINT subject_fkey FOREIGN KEY (subject) REFERENCES public.subject(id);


CREATE OR REPLACE FUNCTION public.check_date_overlap_trigger_function_group_slot()
         RETURNS trigger
         LANGUAGE plpgsql
       AS $function$
       begin
       IF EXISTS(select from slots  s
           where id=new.id_slot--- we select the timerange from the id in the query
            and exists (
                    select from group_slot gs--- then we check whether there exists an overlapping one
                    join slots s2 on gs.id_slot=s2.id
                    where gs.id_group = new.id_group
                    and s2.id <> new.id_slot--- we don't wanna compare the slot specified in the query to itself
                    and s.timerange && s2.timerange  for share--- we use for share to prevent any modification while the trigger is run.
                       )
       for share)

       THEN
            RAISE unique_violation USING MESSAGE = 'Overlapping timeranges for group : ' || new.id_group;
       END IF;
   RETURN NEW;
       end
       $function$;

CREATE TRIGGER ON group_slot BEFORE INSERT OR UPDATE
FOR EACH ROW
EXECUTE FUNCTION check_date_overlap_trigger_function_group_slot();

--
-- PostgreSQL database dump complete
--

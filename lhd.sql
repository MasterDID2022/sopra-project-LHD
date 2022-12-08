--
-- PostgreSQL database dump
--

-- Dumped from database version 14.6
-- Dumped by pg_dump version 14.6

CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA public;

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
-- Name: check_date_overlap_trigger_function_professor_slot(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_date_overlap_trigger_function_professor_slot() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF EXISTS(select from slots  s
           where id=new.id_slot--- we select the timerange from the id in the query
            and exists (
                    select from professor_slot ls--- then we check whether there exists an overlapping one
                    join slots s2 on ls.id_slot=s2.id
                    where ls.id_professor = new.id_group
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


--
-- Name: check_date_overlap_trigger_slot_update_group(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_date_overlap_trigger_slot_update_group() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF OLD.TIMERANGE @> NEW.TIMERANGE THEN 
       RETURN NEW; 
       END IF; 
       
       IF EXISTS (select from slots s join group_slot gs  
       on s.id=gs.id_slot 
       where s.id=new.id 
     and exists (
        select * from slots s2 join group_slot gs2 
        on s2.id=gs2.id_slot 
        where gs2.id_group = gs.id_group 
            and s2.id <> new.id 
            and s2.timerange && new.timerange)
       for share)

       THEN
            RAISE unique_violation USING MESSAGE = 'Unavailable group on slot : ' || new.id_slot;
       END IF;
   RETURN NEW;
       end
       $$;


--
-- Name: check_date_overlap_trigger_slot_update_professor(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_date_overlap_trigger_slot_update_professor() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
       begin
       IF OLD.TIMERANGE @> NEW.TIMERANGE THEN 
       RETURN NEW; 
       END IF; 
       
       IF EXISTS (select from slots s join group_professor gl
       on s.id=gs.id_slot 
       where s.id=new.id 
     and exists (
        select * from slots s2 join group_professor gl2 
        on s2.id=gs2.id_slot 
        where gl2.id_professor = gl.id_professor
            and s2.id <> new.id 
            and s2.timerange && new.timerange)
       for share)

       THEN
            RAISE unique_violation USING MESSAGE = 'Unavailable group on slot : ' || new.id_slot;
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
    subject bigint NOT NULL,
    type character varying
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
-- Name: professor_slot; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.professor_slot (
    id_professor bigint NOT NULL,
    id_trange bigint NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying NOT NULL,
    fname character varying NOT NULL,
    email email NOT NULL,
    password character varying NOT NULL
);


--
-- Name: professors; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.professors (
    title character varying NOT NULL
)
INHERITS (public.users);


--
-- Name: professors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.professors ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.professors_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: admins; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.admins (
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
-- Name: subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    name character varying NOT NULL,
    hour_count_max double precision NOT NULL,
    nb_heure_max double precision NOT NULL
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
-- Name: professors lect_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professors
    ADD CONSTRAINT lect_pkey PRIMARY KEY (id);


--
-- Name: group_slot promotion_creneau_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_slot
    ADD CONSTRAINT promotion_creneau_pkey PRIMARY KEY (id_group, id_slot);


--
-- Name: professor_slot promotion_professors_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professor_slot
    ADD CONSTRAINT promotion_professors_pkey PRIMARY KEY (id_professor, id_trange);


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

CREATE UNIQUE INDEX professors_lower_email_key ON public.professors USING btree (lower((email)::text));

CREATE UNIQUE INDEX admins_lower_email_key ON public.admins USING btree (lower((email)::text));

--
-- Name: group_slot check_timerange_overlap; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_timerange_overlap BEFORE INSERT OR UPDATE ON public.group_slot FOR EACH ROW EXECUTE FUNCTION public.check_date_overlap_trigger_function_group_slot();


--
-- Name: professor_slot ls_check_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER ls_check_trigger BEFORE INSERT OR UPDATE ON public.professor_slot FOR EACH ROW EXECUTE FUNCTION public.check_date_overlap_trigger_function_professor_slot();


--
-- Name: slots update_slot_check_group; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_slot_check_group BEFORE UPDATE ON public.slots FOR EACH ROW EXECUTE FUNCTION public.check_date_overlap_trigger_slot_update_group();


--
-- Name: slots update_slot_check_professor; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_slot_check_professor BEFORE UPDATE ON public.slots FOR EACH ROW EXECUTE FUNCTION public.check_date_overlap_trigger_slot_update_professor();


--
-- Name: group_slot fk_creneau; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_slot
    ADD CONSTRAINT fk_creneau FOREIGN KEY (id_slot) REFERENCES public.slots(id);


--
-- Name: professor_slot fk_creneau; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professor_slot
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
-- Name: professor_slot fk_promotions; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professor_slot
    ADD CONSTRAINT fk_promotions FOREIGN KEY (id_professor) REFERENCES public.professors(id);


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


--
-- PostgreSQL database dump complete
--


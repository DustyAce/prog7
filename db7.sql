--
-- PostgreSQL database dump
--

\restrict fs3fz6HAEfQJaJYObwZYXbw0cIRK8OBOsgjhy3cfyTmFntxaXSiKFwL0xCg7Kce

-- Dumped from database version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: coordinates; Type: TABLE; Schema: public; Owner: dusty
--

CREATE TABLE public.coordinates (
    id integer NOT NULL,
    x integer,
    y double precision NOT NULL,
    CONSTRAINT coordinates_x_check CHECK ((x <= 926)),
    CONSTRAINT coordinates_y_check CHECK ((y > ('-974'::integer)::double precision))
);


ALTER TABLE public.coordinates OWNER TO dusty;

--
-- Name: coordinates_id_seq; Type: SEQUENCE; Schema: public; Owner: dusty
--

CREATE SEQUENCE public.coordinates_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.coordinates_id_seq OWNER TO dusty;

--
-- Name: coordinates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dusty
--

ALTER SEQUENCE public.coordinates_id_seq OWNED BY public.coordinates.id;


--
-- Name: location; Type: TABLE; Schema: public; Owner: dusty
--

CREATE TABLE public.location (
    id integer NOT NULL,
    x bigint,
    y double precision NOT NULL,
    z double precision NOT NULL,
    name character varying(50) NOT NULL,
    CONSTRAINT location_name_check CHECK ((length((name)::text) > 0))
);


ALTER TABLE public.location OWNER TO dusty;

--
-- Name: route; Type: TABLE; Schema: public; Owner: dusty
--

CREATE TABLE public.route (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    coordinates integer,
    creationdate timestamp without time zone DEFAULT now(),
    "from" integer,
    "to" integer,
    distance bigint,
    owner integer,
    CONSTRAINT route_distance_check CHECK ((distance > 1)),
    CONSTRAINT route_name_check CHECK ((length((name)::text) > 0))
);


ALTER TABLE public.route OWNER TO dusty;

--
-- Name: flatroute; Type: VIEW; Schema: public; Owner: dusty
--

CREATE VIEW public.flatroute AS
 SELECT r.id,
    r.name,
    r.coordinates,
    r."from",
    r."to",
    r.distance,
    c.x AS "c.x",
    c.y AS "c.y",
    f.x AS "from.x",
    f.y AS "from.y",
    f.z AS "from.z",
    f.name AS "from.name",
    t.x AS "to.x",
    t.y AS "to.y",
    t.z AS "to.z",
    t.name AS "to.name"
   FROM (((public.route r
     LEFT JOIN public.coordinates c ON ((r.coordinates = c.id)))
     LEFT JOIN public.location f ON ((r."from" = f.id)))
     LEFT JOIN public.location t ON ((r."to" = t.id)));


ALTER VIEW public.flatroute OWNER TO dusty;

--
-- Name: location_id_seq; Type: SEQUENCE; Schema: public; Owner: dusty
--

CREATE SEQUENCE public.location_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.location_id_seq OWNER TO dusty;

--
-- Name: location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dusty
--

ALTER SEQUENCE public.location_id_seq OWNED BY public.location.id;


--
-- Name: route_id_seq; Type: SEQUENCE; Schema: public; Owner: dusty
--

CREATE SEQUENCE public.route_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.route_id_seq OWNER TO dusty;

--
-- Name: route_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dusty
--

ALTER SEQUENCE public.route_id_seq OWNED BY public.route.id;


--
-- Name: test; Type: TABLE; Schema: public; Owner: dusty
--

CREATE TABLE public.test (
    id integer NOT NULL,
    x integer,
    y integer,
    name character varying(10)
);


ALTER TABLE public.test OWNER TO dusty;

--
-- Name: test_id_seq; Type: SEQUENCE; Schema: public; Owner: dusty
--

CREATE SEQUENCE public.test_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.test_id_seq OWNER TO dusty;

--
-- Name: test_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dusty
--

ALTER SEQUENCE public.test_id_seq OWNED BY public.test.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: dusty
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name character varying(50),
    pass character(37)
);


ALTER TABLE public.users OWNER TO dusty;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: dusty
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO dusty;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dusty
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: coordinates id; Type: DEFAULT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.coordinates ALTER COLUMN id SET DEFAULT nextval('public.coordinates_id_seq'::regclass);


--
-- Name: location id; Type: DEFAULT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.location ALTER COLUMN id SET DEFAULT nextval('public.location_id_seq'::regclass);


--
-- Name: route id; Type: DEFAULT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route ALTER COLUMN id SET DEFAULT nextval('public.route_id_seq'::regclass);


--
-- Name: test id; Type: DEFAULT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.test ALTER COLUMN id SET DEFAULT nextval('public.test_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: coordinates; Type: TABLE DATA; Schema: public; Owner: dusty
--

COPY public.coordinates (id, x, y) FROM stdin;
3	123	456
4	666	777
5	143	143
8	2	22
9	5	55
12	2	3
14	4	4
31	1	22
32	1	7
33	2	2
34	2	2
35	4	4
36	123	123
37	5	5
38	94	124
39	32	32
40	-1919	1919
43	2	2
44	1	4
45	36	36
46	37	37
47	38	38
48	39	39
50	27	27
51	27	27
52	2	2
53	2	2
54	27	27
55	1	1
56	2	2
57	2	2
58	1	1
59	4	4
60	15	35
61	4	4
62	2	2
63	3	3
64	1	1
65	1	22
\.


--
-- Data for Name: location; Type: TABLE DATA; Schema: public; Owner: dusty
--

COPY public.location (id, x, y, z, name) FROM stdin;
1	1	2	3	testcity
2	1	4	3	love
4	\N	123	-3	nene
13	987	876	765	numberhell
14	412	412	421	kooool
15	2	2	2	2
16	2	2	2	12321
17	5	5	5	t
18	7	7	7	t
19	7	77	8	too
20	7645	754	745	toloc
21	5	6	7	works
22	1	2	3	finally
23	1	9	1	nine-teeeen
24	91	9191	9991	nine-teeeee-one
25	123	32	1	lov
\.


--
-- Data for Name: route; Type: TABLE DATA; Schema: public; Owner: dusty
--

COPY public.route (id, name, coordinates, creationdate, "from", "to", distance, owner) FROM stdin;
1	testroute	3	2026-05-09 16:38:25.26086	\N	\N	2	\N
2	very cool route	4	2026-05-09 16:38:25.26086	1	\N	123	\N
4	this time fr	8	2026-05-09 22:02:21.216964	\N	\N	2	\N
5	cant see	9	2026-05-09 22:03:03.67705	\N	\N	5	\N
8	gon break this bitch	12	2026-05-09 22:05:51.875402	4	\N	55	\N
10	abc); CREATE TABLE gotyou (teehee INTEGER) ;--	14	2026-05-09 22:30:26.755609	\N	\N	4	\N
26	one boulion and 7	32	2026-05-10 16:01:19.942662	14	\N	23456	\N
28	fr	34	2026-05-10 16:05:39.855168	16	\N	2	\N
29	to	35	2026-05-10 16:06:08.508265	17	\N	5	\N
30	123	36	2026-05-10 16:08:26.016258	18	\N	123	\N
31	pls	37	2026-05-10 16:12:46.448059	19	\N	12	\N
32	wtf	38	2026-05-10 16:15:38.112871	20	\N	99	\N
33	I'm an idiot	39	2026-05-10 16:23:39.749761	22	21	98765	\N
34	am stupid 2.0	40	2026-05-10 19:16:45.187698	23	24	1991	\N
27	27	54	2026-05-10 16:02:21.489814	15	\N	27	\N
41	DUSTY ONLY	55	2026-05-12 20:18:54.573187	\N	\N	58585	22
43	2	57	2026-05-12 20:40:42.873255	\N	\N	2	25
47	abc's route	61	2026-05-13 08:20:49.540826	\N	\N	4	35
48	SHOW	62	2026-05-13 08:35:19.31098	\N	\N	22	36
49	3	63	2026-05-13 08:39:01.67174	\N	\N	3	36
51	tiny	65	2026-05-13 08:49:41.06156	\N	\N	5	36
\.


--
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: dusty
--

COPY public.test (id, x, y, name) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: dusty
--

COPY public.users (id, name, pass) FROM stdin;
22	dusty	1661df1d20a63bdfaf89316fcaad06f6.D1ZO
24	fast	45f3b4bdb0a0d88f07446a8ea38d1e23.M81W
25	tee-hee	8c022a8cbdbafe9156783695a568b5fd.GH72
26	1	c23846204df15247905b30f15a949f4a.Z3WM
28	22	b1e0d9f6f97e38776107f57a0360fab4.9Z3N
29	123	fa5d933c5b03c3501a532b2c98922eac.XPHV
30	78tr78he3f3r	4c33261a2bdd177ad268353c2200a7bc.D8MS
31	djifo	800aad29a07352f6b43747df3cd480b6.C3QQ
32	aaa	d14b9f7cde1fe25584f86168e3a08fe6.FB9X
35	abc	da6ce9f4a658def4704779916e25c1b3.V64U
36	mr. evilguy	c3c2940f21afa972b49c6a4594e3cb26.R5G7
37	2	e43e75bb9dd5812ec1fb188e050defa6.WTCC
38	3	bfdac28f1639db194af1f430f5e98457.26JE
\.


--
-- Name: coordinates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dusty
--

SELECT pg_catalog.setval('public.coordinates_id_seq', 65, true);


--
-- Name: location_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dusty
--

SELECT pg_catalog.setval('public.location_id_seq', 25, true);


--
-- Name: route_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dusty
--

SELECT pg_catalog.setval('public.route_id_seq', 51, true);


--
-- Name: test_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dusty
--

SELECT pg_catalog.setval('public.test_id_seq', 5, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dusty
--

SELECT pg_catalog.setval('public.users_id_seq', 38, true);


--
-- Name: coordinates coordinates_pkey; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.coordinates
    ADD CONSTRAINT coordinates_pkey PRIMARY KEY (id);


--
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);


--
-- Name: route route_pkey; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_pkey PRIMARY KEY (id);


--
-- Name: test test_pkey; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_pkey PRIMARY KEY (id);


--
-- Name: users users_name_key; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: route route_coordinates_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_coordinates_fkey FOREIGN KEY (coordinates) REFERENCES public.coordinates(id);


--
-- Name: route route_from_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_from_fkey FOREIGN KEY ("from") REFERENCES public.location(id);


--
-- Name: route route_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_owner_fkey FOREIGN KEY (owner) REFERENCES public.users(id);


--
-- Name: route route_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dusty
--

ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_to_fkey FOREIGN KEY ("to") REFERENCES public.location(id);


--
-- PostgreSQL database dump complete
--

\unrestrict fs3fz6HAEfQJaJYObwZYXbw0cIRK8OBOsgjhy3cfyTmFntxaXSiKFwL0xCg7Kce


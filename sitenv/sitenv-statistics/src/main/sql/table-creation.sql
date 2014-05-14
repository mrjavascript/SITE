--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.0
-- Dumped by pg_dump version 9.3.1
-- Started on 2014-05-14 12:35:27 EDT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 187 (class 3079 OID 12018)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2323 (class 0 OID 0)
-- Dependencies: 187
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 176 (class 1259 OID 47340)
-- Name: ccda_download; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE ccda_download (
    download_pk bigint NOT NULL,
    download_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.ccda_download OWNER TO "LifeRaySys";

--
-- TOC entry 175 (class 1259 OID 47338)
-- Name: ccda_download_download_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE ccda_download_download_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ccda_download_download_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2324 (class 0 OID 0)
-- Dependencies: 175
-- Name: ccda_download_download_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE ccda_download_download_pk_seq OWNED BY ccda_download.download_pk;


--
-- TOC entry 172 (class 1259 OID 47318)
-- Name: ccda_validations; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE ccda_validations (
    validation_pk bigint NOT NULL,
    validation_time timestamp without time zone DEFAULT now(),
    validation_errors boolean NOT NULL,
    validation_warnings boolean NOT NULL,
    validation_info boolean NOT NULL,
    validation_httperror boolean
);


ALTER TABLE public.ccda_validations OWNER TO "LifeRaySys";

--
-- TOC entry 171 (class 1259 OID 47316)
-- Name: ccda_validations_validation_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE ccda_validations_validation_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ccda_validations_validation_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2325 (class 0 OID 0)
-- Dependencies: 171
-- Name: ccda_validations_validation_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE ccda_validations_validation_pk_seq OWNED BY ccda_validations.validation_pk;


--
-- TOC entry 180 (class 1259 OID 55561)
-- Name: direct_receive; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE direct_receive (
    directreceive_pk bigint NOT NULL,
    directreceive_time timestamp without time zone DEFAULT now(),
    directreceive_errors boolean NOT NULL,
    directreceive_precanned boolean NOT NULL,
    directreceive_uploaded boolean NOT NULL
);


ALTER TABLE public.direct_receive OWNER TO "LifeRaySys";

--
-- TOC entry 179 (class 1259 OID 55559)
-- Name: direct_receive_directreceive_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE direct_receive_directreceive_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.direct_receive_directreceive_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2326 (class 0 OID 0)
-- Dependencies: 179
-- Name: direct_receive_directreceive_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE direct_receive_directreceive_pk_seq OWNED BY direct_receive.directreceive_pk;


--
-- TOC entry 182 (class 1259 OID 55570)
-- Name: direct_trustupload; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE direct_trustupload (
    directtrust_pk bigint NOT NULL,
    directtrust_time timestamp without time zone DEFAULT now(),
    directtrust_errors boolean NOT NULL
);


ALTER TABLE public.direct_trustupload OWNER TO "LifeRaySys";

--
-- TOC entry 181 (class 1259 OID 55568)
-- Name: direct_trustupload_directtrust_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE direct_trustupload_directtrust_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.direct_trustupload_directtrust_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2327 (class 0 OID 0)
-- Dependencies: 181
-- Name: direct_trustupload_directtrust_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE direct_trustupload_directtrust_pk_seq OWNED BY direct_trustupload.directtrust_pk;


--
-- TOC entry 170 (class 1259 OID 47314)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO "LifeRaySys";

--
-- TOC entry 184 (class 1259 OID 55585)
-- Name: pdti_testcasegroup; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE pdti_testcasegroup (
    group_pk bigint NOT NULL,
    group_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.pdti_testcasegroup OWNER TO "LifeRaySys";

--
-- TOC entry 183 (class 1259 OID 55583)
-- Name: pdti_testcasegroup_group_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE pdti_testcasegroup_group_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pdti_testcasegroup_group_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2328 (class 0 OID 0)
-- Dependencies: 183
-- Name: pdti_testcasegroup_group_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE pdti_testcasegroup_group_pk_seq OWNED BY pdti_testcasegroup.group_pk;


--
-- TOC entry 186 (class 1259 OID 55596)
-- Name: pdti_testcases; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE pdti_testcases (
    testcase_pk bigint NOT NULL,
    testcase_time timestamp without time zone DEFAULT now(),
    testcase_name character varying(100) NOT NULL,
    testcase_pass boolean NOT NULL,
    testcase_httperror boolean NOT NULL,
    testcase_group bigint
);


ALTER TABLE public.pdti_testcases OWNER TO "LifeRaySys";

--
-- TOC entry 185 (class 1259 OID 55594)
-- Name: pdti_testcases_testcase_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE pdti_testcases_testcase_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pdti_testcases_testcase_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2329 (class 0 OID 0)
-- Dependencies: 185
-- Name: pdti_testcases_testcase_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE pdti_testcases_testcase_pk_seq OWNED BY pdti_testcases.testcase_pk;


--
-- TOC entry 178 (class 1259 OID 47370)
-- Name: qrda_validations; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE qrda_validations (
    validation_pk bigint NOT NULL,
    validation_time timestamp without time zone DEFAULT now(),
    validation_schemaerrors boolean NOT NULL,
    validation_schematronwarnings boolean NOT NULL,
    validation_category integer NOT NULL,
    validation_httperror boolean NOT NULL,
    validation_schematronerrors boolean
);


ALTER TABLE public.qrda_validations OWNER TO "LifeRaySys";

--
-- TOC entry 177 (class 1259 OID 47368)
-- Name: qrda_validations_validation_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE qrda_validations_validation_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.qrda_validations_validation_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2330 (class 0 OID 0)
-- Dependencies: 177
-- Name: qrda_validations_validation_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE qrda_validations_validation_pk_seq OWNED BY qrda_validations.validation_pk;


--
-- TOC entry 174 (class 1259 OID 47331)
-- Name: smartccda_validations; Type: TABLE; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE TABLE smartccda_validations (
    smartvalidation_pk bigint NOT NULL,
    smartvalidation_time timestamp without time zone DEFAULT now(),
    smartvalidation_httperror boolean NOT NULL
);


ALTER TABLE public.smartccda_validations OWNER TO "LifeRaySys";

--
-- TOC entry 173 (class 1259 OID 47329)
-- Name: smartccda_validations_smartvalidation_pk_seq; Type: SEQUENCE; Schema: public; Owner: LifeRaySys
--

CREATE SEQUENCE smartccda_validations_smartvalidation_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.smartccda_validations_smartvalidation_pk_seq OWNER TO "LifeRaySys";

--
-- TOC entry 2331 (class 0 OID 0)
-- Dependencies: 173
-- Name: smartccda_validations_smartvalidation_pk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: LifeRaySys
--

ALTER SEQUENCE smartccda_validations_smartvalidation_pk_seq OWNED BY smartccda_validations.smartvalidation_pk;


--
-- TOC entry 2137 (class 2604 OID 47343)
-- Name: download_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY ccda_download ALTER COLUMN download_pk SET DEFAULT nextval('ccda_download_download_pk_seq'::regclass);


--
-- TOC entry 2133 (class 2604 OID 47321)
-- Name: validation_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY ccda_validations ALTER COLUMN validation_pk SET DEFAULT nextval('ccda_validations_validation_pk_seq'::regclass);


--
-- TOC entry 2141 (class 2604 OID 55564)
-- Name: directreceive_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY direct_receive ALTER COLUMN directreceive_pk SET DEFAULT nextval('direct_receive_directreceive_pk_seq'::regclass);


--
-- TOC entry 2143 (class 2604 OID 55573)
-- Name: directtrust_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY direct_trustupload ALTER COLUMN directtrust_pk SET DEFAULT nextval('direct_trustupload_directtrust_pk_seq'::regclass);


--
-- TOC entry 2145 (class 2604 OID 55588)
-- Name: group_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY pdti_testcasegroup ALTER COLUMN group_pk SET DEFAULT nextval('pdti_testcasegroup_group_pk_seq'::regclass);


--
-- TOC entry 2147 (class 2604 OID 55599)
-- Name: testcase_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY pdti_testcases ALTER COLUMN testcase_pk SET DEFAULT nextval('pdti_testcases_testcase_pk_seq'::regclass);


--
-- TOC entry 2139 (class 2604 OID 47373)
-- Name: validation_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY qrda_validations ALTER COLUMN validation_pk SET DEFAULT nextval('qrda_validations_validation_pk_seq'::regclass);


--
-- TOC entry 2135 (class 2604 OID 47334)
-- Name: smartvalidation_pk; Type: DEFAULT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY smartccda_validations ALTER COLUMN smartvalidation_pk SET DEFAULT nextval('smartccda_validations_smartvalidation_pk_seq'::regclass);


--
-- TOC entry 2305 (class 0 OID 47340)
-- Dependencies: 176
-- Data for Name: ccda_download; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY ccda_download (download_pk, download_time) FROM stdin;
1	2014-04-22 11:34:43.846263
2	2014-04-24 11:06:00.209532
\.


--
-- TOC entry 2332 (class 0 OID 0)
-- Dependencies: 175
-- Name: ccda_download_download_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('ccda_download_download_pk_seq', 2, true);


--
-- TOC entry 2301 (class 0 OID 47318)
-- Dependencies: 172
-- Data for Name: ccda_validations; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY ccda_validations (validation_pk, validation_time, validation_errors, validation_warnings, validation_info, validation_httperror) FROM stdin;
2	2014-04-22 11:34:03.904435	t	t	t	f
3	2014-04-22 12:06:55.517731	t	t	t	f
4	2014-04-22 12:07:04.723363	t	t	t	f
5	2014-04-22 12:51:51.609632	t	t	t	f
6	2014-04-23 14:34:51.554417	t	t	t	f
7	2014-04-23 15:15:42.611846	f	f	f	f
8	2014-04-24 09:16:56.212775	f	f	f	f
9	2014-04-24 09:42:24.914464	f	f	f	f
10	2014-04-24 09:43:44.949173	f	f	f	f
11	2014-04-24 09:44:10.754724	f	f	f	f
12	2014-04-24 09:52:44.902022	t	t	t	f
13	2014-04-24 09:55:34.001231	f	f	t	f
14	2014-04-24 10:18:54.863735	f	f	t	f
15	2014-04-24 10:25:40.834755	f	f	f	t
16	2014-04-24 10:33:18.018035	f	f	t	f
17	2014-04-24 10:37:28.399763	f	f	t	f
18	2014-04-24 10:39:45.39788	f	t	t	f
19	2014-04-24 10:40:23.906377	f	t	t	f
20	2014-04-24 11:05:06.429791	f	t	t	f
21	2014-04-29 18:53:28.177283	f	t	t	f
22	2014-04-29 19:17:03.661101	f	t	t	f
23	2014-05-06 13:30:00.931162	t	t	t	f
\.


--
-- TOC entry 2333 (class 0 OID 0)
-- Dependencies: 171
-- Name: ccda_validations_validation_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('ccda_validations_validation_pk_seq', 23, true);


--
-- TOC entry 2309 (class 0 OID 55561)
-- Dependencies: 180
-- Data for Name: direct_receive; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY direct_receive (directreceive_pk, directreceive_time, directreceive_errors, directreceive_precanned, directreceive_uploaded) FROM stdin;
1	2014-05-06 13:24:59.95019	f	t	f
2	2014-05-06 13:25:48.635127	f	f	t
\.


--
-- TOC entry 2334 (class 0 OID 0)
-- Dependencies: 179
-- Name: direct_receive_directreceive_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('direct_receive_directreceive_pk_seq', 2, true);


--
-- TOC entry 2311 (class 0 OID 55570)
-- Dependencies: 182
-- Data for Name: direct_trustupload; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY direct_trustupload (directtrust_pk, directtrust_time, directtrust_errors) FROM stdin;
1	2014-05-06 13:24:13.868671	f
\.


--
-- TOC entry 2335 (class 0 OID 0)
-- Dependencies: 181
-- Name: direct_trustupload_directtrust_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('direct_trustupload_directtrust_pk_seq', 1, true);


--
-- TOC entry 2336 (class 0 OID 0)
-- Dependencies: 170
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('hibernate_sequence', 19, true);


--
-- TOC entry 2313 (class 0 OID 55585)
-- Dependencies: 184
-- Data for Name: pdti_testcasegroup; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY pdti_testcasegroup (group_pk, group_time) FROM stdin;
1	2014-05-13 20:35:54.692001
11	2014-05-13 21:02:20.435211
12	2014-05-13 21:02:33.559356
13	2014-05-13 21:04:28.365311
14	2014-05-13 21:04:49.108689
\.


--
-- TOC entry 2337 (class 0 OID 0)
-- Dependencies: 183
-- Name: pdti_testcasegroup_group_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('pdti_testcasegroup_group_pk_seq', 14, true);


--
-- TOC entry 2315 (class 0 OID 55596)
-- Dependencies: 186
-- Data for Name: pdti_testcases; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY pdti_testcases (testcase_pk, testcase_time, testcase_name, testcase_pass, testcase_httperror, testcase_group) FROM stdin;
7	2014-05-13 21:02:20.435211	search_provider_by_name	t	f	11
8	2014-05-13 21:02:33.559356	search_provider_by_name	t	f	12
9	2014-05-13 21:02:33.559356	search_org_by_id	t	f	12
10	2014-05-13 21:02:33.559356	search_membership_by_provider	t	f	12
11	2014-05-13 21:02:33.559356	search_service_by_id	t	f	12
12	2014-05-13 21:02:33.559356	search_credential_by_id	t	f	12
13	2014-05-13 21:02:33.559356	Find_Individual	t	f	12
14	2014-05-13 21:02:33.559356	Find_Unique_Individual	t	f	12
15	2014-05-13 21:02:33.559356	Find_Organization	t	f	12
16	2014-05-13 21:02:33.559356	Find_Unique_Organization	t	f	12
17	2014-05-13 21:02:33.559356	Find_Organizations_for_Unique_Individual	t	f	12
18	2014-05-13 21:02:33.559356	Find_Individuals_for_Unique_Organization	t	f	12
19	2014-05-13 21:02:33.559356	Find_Individuals_and_Organizations	t	f	12
20	2014-05-13 21:02:33.559356	dup_req_id_federation_loop_test_hpdplus	t	f	12
21	2014-05-13 21:04:28.365311	search_provider_by_name	t	f	13
22	2014-05-13 21:04:49.108689	search_provider_by_name	t	f	14
23	2014-05-13 21:04:49.108689	search_org_by_id	t	f	14
24	2014-05-13 21:04:49.108689	search_membership_by_provider	t	f	14
25	2014-05-13 21:04:49.108689	search_service_by_id	t	f	14
26	2014-05-13 21:04:49.108689	search_credential_by_id	t	f	14
27	2014-05-13 21:04:49.108689	Find_Individual	t	f	14
28	2014-05-13 21:04:49.108689	Find_Unique_Individual	t	f	14
29	2014-05-13 21:04:49.108689	Find_Organization	t	f	14
30	2014-05-13 21:04:49.108689	Find_Unique_Organization	t	f	14
31	2014-05-13 21:04:49.108689	Find_Organizations_for_Unique_Individual	t	f	14
32	2014-05-13 21:04:49.108689	Find_Individuals_for_Unique_Organization	t	f	14
33	2014-05-13 21:04:49.108689	Find_Individuals_and_Organizations	t	f	14
34	2014-05-13 21:04:49.108689	dup_req_id_federation_loop_test_hpdplus	t	f	14
\.


--
-- TOC entry 2338 (class 0 OID 0)
-- Dependencies: 185
-- Name: pdti_testcases_testcase_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('pdti_testcases_testcase_pk_seq', 34, true);


--
-- TOC entry 2307 (class 0 OID 47370)
-- Dependencies: 178
-- Data for Name: qrda_validations; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY qrda_validations (validation_pk, validation_time, validation_schemaerrors, validation_schematronwarnings, validation_category, validation_httperror, validation_schematronerrors) FROM stdin;
1	2014-04-29 18:53:55.171748	f	t	1	f	t
2	2014-04-29 19:47:26.353934	f	t	1	f	t
3	2014-05-13 14:39:23.499545	f	f	1	f	f
4	2014-05-13 14:54:41.691532	f	f	1	f	f
\.


--
-- TOC entry 2339 (class 0 OID 0)
-- Dependencies: 177
-- Name: qrda_validations_validation_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('qrda_validations_validation_pk_seq', 4, true);


--
-- TOC entry 2303 (class 0 OID 47331)
-- Dependencies: 174
-- Data for Name: smartccda_validations; Type: TABLE DATA; Schema: public; Owner: LifeRaySys
--

COPY smartccda_validations (smartvalidation_pk, smartvalidation_time, smartvalidation_httperror) FROM stdin;
1	2014-04-22 11:34:34.902156	f
2	2014-04-24 11:05:32.772952	f
\.


--
-- TOC entry 2340 (class 0 OID 0)
-- Dependencies: 173
-- Name: smartccda_validations_smartvalidation_pk_seq; Type: SEQUENCE SET; Schema: public; Owner: LifeRaySys
--

SELECT pg_catalog.setval('smartccda_validations_smartvalidation_pk_seq', 2, true);


--
-- TOC entry 2162 (class 2606 OID 47346)
-- Name: ccda_download_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY ccda_download
    ADD CONSTRAINT ccda_download_pkey PRIMARY KEY (download_pk);


--
-- TOC entry 2153 (class 2606 OID 55578)
-- Name: ccda_validations_validation_pk; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY ccda_validations
    ADD CONSTRAINT ccda_validations_validation_pk PRIMARY KEY (validation_pk);


--
-- TOC entry 2176 (class 2606 OID 55567)
-- Name: direct_receive_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY direct_receive
    ADD CONSTRAINT direct_receive_pkey PRIMARY KEY (directreceive_pk);


--
-- TOC entry 2180 (class 2606 OID 55576)
-- Name: direct_trustupload_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY direct_trustupload
    ADD CONSTRAINT direct_trustupload_pkey PRIMARY KEY (directtrust_pk);


--
-- TOC entry 2183 (class 2606 OID 55591)
-- Name: pdti_testcasegroup_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY pdti_testcasegroup
    ADD CONSTRAINT pdti_testcasegroup_pkey PRIMARY KEY (group_pk);


--
-- TOC entry 2185 (class 2606 OID 55602)
-- Name: pdti_testcases_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY pdti_testcases
    ADD CONSTRAINT pdti_testcases_pkey PRIMARY KEY (testcase_pk);


--
-- TOC entry 2164 (class 2606 OID 47376)
-- Name: qrda_validations_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY qrda_validations
    ADD CONSTRAINT qrda_validations_pkey PRIMARY KEY (validation_pk);


--
-- TOC entry 2157 (class 2606 OID 47337)
-- Name: smartccda_validations_pkey; Type: CONSTRAINT; Schema: public; Owner: LifeRaySys; Tablespace: 
--

ALTER TABLE ONLY smartccda_validations
    ADD CONSTRAINT smartccda_validations_pkey PRIMARY KEY (smartvalidation_pk);


--
-- TOC entry 2160 (class 1259 OID 47347)
-- Name: ccda_download_download_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_download_download_time_idx ON ccda_download USING btree (download_time);


--
-- TOC entry 2149 (class 1259 OID 47324)
-- Name: ccda_validations_validation_errors_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_validations_validation_errors_idx ON ccda_validations USING btree (validation_errors);


--
-- TOC entry 2150 (class 1259 OID 47327)
-- Name: ccda_validations_validation_httperror_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_validations_validation_httperror_idx ON ccda_validations USING btree (validation_httperror);


--
-- TOC entry 2151 (class 1259 OID 47326)
-- Name: ccda_validations_validation_info_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_validations_validation_info_idx ON ccda_validations USING btree (validation_info);


--
-- TOC entry 2154 (class 1259 OID 47323)
-- Name: ccda_validations_validation_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_validations_validation_time_idx ON ccda_validations USING btree (validation_time);


--
-- TOC entry 2155 (class 1259 OID 47325)
-- Name: ccda_validations_validation_warnings_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX ccda_validations_validation_warnings_idx ON ccda_validations USING btree (validation_warnings);


--
-- TOC entry 2171 (class 1259 OID 55614)
-- Name: direct_receive_directreceive_errors_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_receive_directreceive_errors_idx ON direct_receive USING btree (directreceive_errors);


--
-- TOC entry 2172 (class 1259 OID 55615)
-- Name: direct_receive_directreceive_precanned_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_receive_directreceive_precanned_idx ON direct_receive USING btree (directreceive_precanned);


--
-- TOC entry 2173 (class 1259 OID 55613)
-- Name: direct_receive_directreceive_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_receive_directreceive_time_idx ON direct_receive USING btree (directreceive_time);


--
-- TOC entry 2174 (class 1259 OID 55616)
-- Name: direct_receive_directreceive_uploaded_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_receive_directreceive_uploaded_idx ON direct_receive USING btree (directreceive_uploaded);


--
-- TOC entry 2177 (class 1259 OID 55612)
-- Name: direct_trustupload_directtrust_errors_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_trustupload_directtrust_errors_idx ON direct_trustupload USING btree (directtrust_errors);


--
-- TOC entry 2178 (class 1259 OID 55611)
-- Name: direct_trustupload_directtrust_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX direct_trustupload_directtrust_time_idx ON direct_trustupload USING btree (directtrust_time);


--
-- TOC entry 2181 (class 1259 OID 55617)
-- Name: pdti_testcasegroup_group_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcasegroup_group_time_idx ON pdti_testcasegroup USING btree (group_time);


--
-- TOC entry 2186 (class 1259 OID 55622)
-- Name: pdti_testcases_testcase_group_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcases_testcase_group_idx ON pdti_testcases USING btree (testcase_group);


--
-- TOC entry 2187 (class 1259 OID 55621)
-- Name: pdti_testcases_testcase_httperror_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcases_testcase_httperror_idx ON pdti_testcases USING btree (testcase_httperror);


--
-- TOC entry 2188 (class 1259 OID 55619)
-- Name: pdti_testcases_testcase_name_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcases_testcase_name_idx ON pdti_testcases USING btree (testcase_name);


--
-- TOC entry 2189 (class 1259 OID 55620)
-- Name: pdti_testcases_testcase_pass_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcases_testcase_pass_idx ON pdti_testcases USING btree (testcase_pass);


--
-- TOC entry 2190 (class 1259 OID 55618)
-- Name: pdti_testcases_testcase_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX pdti_testcases_testcase_time_idx ON pdti_testcases USING btree (testcase_time);


--
-- TOC entry 2165 (class 1259 OID 47382)
-- Name: qrda_validations_validation_category_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_category_idx ON qrda_validations USING btree (validation_category);


--
-- TOC entry 2166 (class 1259 OID 47383)
-- Name: qrda_validations_validation_httperror_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_httperror_idx ON qrda_validations USING btree (validation_httperror);


--
-- TOC entry 2167 (class 1259 OID 47380)
-- Name: qrda_validations_validation_schemaerrors_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_schemaerrors_idx ON qrda_validations USING btree (validation_schemaerrors);


--
-- TOC entry 2168 (class 1259 OID 47384)
-- Name: qrda_validations_validation_schematronerrors_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_schematronerrors_idx ON qrda_validations USING btree (validation_schematronerrors);


--
-- TOC entry 2169 (class 1259 OID 47381)
-- Name: qrda_validations_validation_schematronwarnings_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_schematronwarnings_idx ON qrda_validations USING btree (validation_schematronwarnings);


--
-- TOC entry 2170 (class 1259 OID 47379)
-- Name: qrda_validations_validation_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX qrda_validations_validation_time_idx ON qrda_validations USING btree (validation_time);


--
-- TOC entry 2158 (class 1259 OID 47349)
-- Name: smartccda_validations_smartvalidation_httperror_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX smartccda_validations_smartvalidation_httperror_idx ON smartccda_validations USING btree (smartvalidation_httperror);


--
-- TOC entry 2159 (class 1259 OID 47348)
-- Name: smartccda_validations_smartvalidation_time_idx; Type: INDEX; Schema: public; Owner: LifeRaySys; Tablespace: 
--

CREATE INDEX smartccda_validations_smartvalidation_time_idx ON smartccda_validations USING btree (smartvalidation_time);


--
-- TOC entry 2191 (class 2606 OID 55603)
-- Name: pdti_testcases_group_fk; Type: FK CONSTRAINT; Schema: public; Owner: LifeRaySys
--

ALTER TABLE ONLY pdti_testcases
    ADD CONSTRAINT pdti_testcases_group_fk FOREIGN KEY (testcase_group) REFERENCES pdti_testcasegroup(group_pk);


--
-- TOC entry 2322 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: chris
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM chris;
GRANT ALL ON SCHEMA public TO chris;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-05-14 12:35:28 EDT

--
-- PostgreSQL database dump complete
--


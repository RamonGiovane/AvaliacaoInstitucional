PGDMP     9    ,                w            spadb    9.6.13    9.6.13 
    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            �            1259    18587    segmento    TABLE     c   CREATE TABLE public.segmento (
    codigo integer NOT NULL,
    descricao character varying(50)
);
    DROP TABLE public.segmento;
       public         spaadmin    false            �            1259    18590    segmento_codigo_seq    SEQUENCE     |   CREATE SEQUENCE public.segmento_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.segmento_codigo_seq;
       public       spaadmin    false    201            �           0    0    segmento_codigo_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.segmento_codigo_seq OWNED BY public.segmento.codigo;
            public       spaadmin    false    202                       2604    18599    segmento codigo    DEFAULT     r   ALTER TABLE ONLY public.segmento ALTER COLUMN codigo SET DEFAULT nextval('public.segmento_codigo_seq'::regclass);
 >   ALTER TABLE public.segmento ALTER COLUMN codigo DROP DEFAULT;
       public       spaadmin    false    202    201            |          0    18587    segmento 
   TABLE DATA               5   COPY public.segmento (codigo, descricao) FROM stdin;
    public       spaadmin    false    201   ?	       �           0    0    segmento_codigo_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.segmento_codigo_seq', 8, true);
            public       spaadmin    false    202                       2606    18631    segmento segmento_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.segmento
    ADD CONSTRAINT segmento_pkey PRIMARY KEY (codigo);
 @   ALTER TABLE ONLY public.segmento DROP CONSTRAINT segmento_pkey;
       public         spaadmin    false    201    201            |      x�3�t�,NN�+I��tɇ�b���� i�/     
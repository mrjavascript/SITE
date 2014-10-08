CREATE TABLE ccda_reference_download
(
  download_pk bigserial NOT NULL,
  download_time timestamp without time zone DEFAULT now(),
  download_file_name character varying(1000) NOT NULL,
  CONSTRAINT "Primary Key" PRIMARY KEY (download_pk)
)
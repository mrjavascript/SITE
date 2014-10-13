-- Table: ccda_service_calls

-- DROP TABLE ccda_service_calls;

CREATE TABLE ccda_service_calls
(
  validation_pk bigserial NOT NULL,
  validation_time timestamp without time zone DEFAULT now(),
  validation_errors boolean NOT NULL,
  validation_warnings boolean NOT NULL,
  validation_info boolean NOT NULL,
  validation_httperror boolean,
  validation_type character varying(100),
  validator character varying(100) NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ccda_service_calls
  OWNER TO "LifeRaySys";

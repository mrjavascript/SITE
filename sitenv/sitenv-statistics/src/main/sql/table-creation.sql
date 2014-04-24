
CREATE TABLE ccda_validations
(
	validation_pk	BIGSERIAL primary key not null,
	validation_time TIMESTAMP default current_timestamp,
	validation_errors boolean not null,
	validation_warnings boolean not null,
	validation_info boolean not null,
	validation_httperror boolean not null
);

CREATE TABLE smartccda_validations
(
	smartvalidation_pk	BIGSERIAL primary key not null,
	smartvalidation_time TIMESTAMP default current_timestamp,
	smartvalidation_httperror boolean not null
);

CREATE TABLE ccda_download
(
	download_pk	BIGSERIAL primary key not null,
	download_time TIMESTAMP default current_timestamp
);
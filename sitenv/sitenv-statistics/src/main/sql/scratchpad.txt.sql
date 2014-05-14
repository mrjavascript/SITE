CREATE TABLE pdti_testcasegroup
(
	group_pk	BIGSERIAL primary key not null,
	group_time TIMESTAMP default current_timestamp
);

CREATE TABLE pdti_testcases
(
	testcase_pk	BIGSERIAL primary key not null,
	testcase_time TIMESTAMP default current_timestamp,
	testcase_name varchar(100) not null,
	testcase_pass boolean not null,
	testcase_httperror boolean not null,
	testcase_group bigint not null, 
	constraint pdti_testcases_group_fk foreign key(testcase_group) references pdti_testcasegroup (group_pk) not null
);
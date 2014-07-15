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

CREATE TABLE direct_send
(
	directsend_pk	BIGSERIAL primary key not null,
	directsend_count BIGINT not null,
	directsend_date DATE not null,
	directsend_email varchar(1000) not null,
	directsend_domain varchar(1000) not null
);

CREATE OR REPLACE FUNCTION ccda_validation_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	total_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) total_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		ccda_validations
	WHERE
		validation_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM ccda_validation_weekly_counts(104);


CREATE OR REPLACE FUNCTION qrda_validation_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	category1_count BIGINT,
	category3_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) category1_count,
	coalesce(y.total_count, 0) category3_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		qrda_validations
	WHERE
		validation_category = 1 
		AND
		validation_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		qrda_validations 
	WHERE
		validation_category = 3
		AND validation_httperror = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM qrda_validation_weekly_counts(104);


CREATE OR REPLACE FUNCTION pdti_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	totaltest_count BIGINT,
	totalrequest_count BIGINT,
	totaluniqueendpoint_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) totaltest_count,
	coalesce(y.total_count, 0) totalrequest_count,
	coalesce(z.total_count, 0) totaluniqueendpoint_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', testcase_time) week_start,
		count(*) total_count
	FROM 
		pdti_testcases
	WHERE
		testcase_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', group_time) week_start,
		count(*) total_count
	FROM 
		pdti_testcasegroup 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', group_time) week_start,
		count(DISTINCT group_endpointUrl) total_count
	FROM 
		pdti_testcasegroup 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as z ON c.generated_date = z.week_start
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM pdti_weekly_counts(104);



CREATE OR REPLACE FUNCTION directreceive_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	total_count BIGINT,
	uniquedomain_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) total_count,
	coalesce(y.total_count, 0) uniquedomain_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directreceive_time) week_start,
		count(*) total_count
	FROM 
		direct_receive
	WHERE
		directreceive_errors = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directreceive_time) week_start,
		count(DISTINCT directreceive_domain) total_count
	FROM 
		direct_receive
	WHERE
		directreceive_errors = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM directreceive_weekly_counts(104);

CREATE OR REPLACE FUNCTION directsend_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	total_count BIGINT,
	uniquedomain_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) total_count,
	coalesce(y.total_count, 0) uniquedomain_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directsend_date) week_start,
		count(*) total_count
	FROM 
		direct_send
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directsend_date) week_start,
		count(DISTINCT directsend_domain) total_count
	FROM 
		direct_send	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM directsend_weekly_counts(104);


CREATE OR REPLACE FUNCTION aggregate_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	directsent_count BIGINT,
	ccda_count BIGINT,
	qrda_count BIGINT,
	pdtirequest_count BIGINT,
	directreceive_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date + '-1 days' AS DATE) start_date,
	CAST(c.generated_date + '5 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	CAST(coalesce(u.total_count, 0) AS BIGINT) directsent_count,
	coalesce(v.total_count, 0) ccda_count,
	coalesce(x.total_count, 0) qrda_count,
	coalesce(y.total_count, 0) pdtirequest_count,
	coalesce(z.total_count, 0) directreceive_count
FROM
	(SELECT 
		date_trunc('week', (current_date - offs)) as generated_date 
	 FROM generate_series(0,(limited*7)+1,7) as offs
	 ORDER BY generated_date DESC
	 LIMIT limited) as c

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directsend_date) week_start,
		SUM(directsend_count) total_count
	FROM 
		direct_send	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as u ON c.generated_date = u.week_start

	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		ccda_validations
	WHERE
		validation_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as v ON c.generated_date = v.week_start
	
	
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		qrda_validations
	WHERE
		validation_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', group_time) week_start,
		count(*) total_count
	FROM 
		pdti_testcasegroup
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directreceive_time) week_start,
		count(*) total_count
	FROM 
		direct_receive
	WHERE
		directreceive_errors = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as z ON c.generated_date = z.week_start
	
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM aggregate_weekly_counts(104);
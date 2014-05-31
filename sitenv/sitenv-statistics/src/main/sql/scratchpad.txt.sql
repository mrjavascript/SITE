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

CREATE OR REPLACE FUNCTION ccda_validation_weekly_counts(limited INT) 
RETURNS TABLE (
	start_date DATE,
	end_date DATE,
	range_interval INT,
	range_year INT,
	successful_count BIGINT,
	failed_count BIGINT,
	error_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date AS DATE) start_date,
	CAST(c.generated_date + '6 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) successful_count,
	coalesce(y.total_count, 0) failed_count,
	coalesce(z.total_count, 0) httperror_count
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
		validation_errors = FALSE
		AND validation_httperror = FALSE
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
		ccda_validations 
	WHERE
		validation_errors = TRUE
		AND validation_httperror = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		ccda_validations 
	WHERE
		validation_httperror = TRUE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as z ON c.generated_date = z.week_start
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
	successful_count BIGINT,
	failed_count BIGINT,
	error_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date AS DATE) start_date,
	CAST(c.generated_date + '6 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) successful_count,
	coalesce(y.total_count, 0) failed_count,
	coalesce(z.total_count, 0) httperror_count
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
		validation_schemaerrors = FALSE
		AND validation_schematronerrors = FALSE
		AND validation_httperror = FALSE
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
		(validation_schemaerrors = TRUE OR validation_schematronerrors = TRUE)
		AND validation_httperror = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', validation_time) week_start,
		count(*) total_count
	FROM 
		qrda_validations 
	WHERE
		validation_httperror = TRUE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as z ON c.generated_date = z.week_start
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
	successful_count BIGINT,
	failed_count BIGINT,
	error_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date AS DATE) start_date,
	CAST(c.generated_date + '6 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) successful_count,
	coalesce(y.total_count, 0) failed_count,
	coalesce(z.total_count, 0) httperror_count
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
		testcase_pass = TRUE
		AND testcase_httperror = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', testcase_time) week_start,
		count(*) total_count
	FROM 
		pdti_testcases
	WHERE
		testcase_pass  = FALSE
		AND testcase_httperror = FALSE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as y ON c.generated_date = y.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', testcase_time) week_start,
		count(*) total_count
	FROM 
		pdti_testcases 
	WHERE
		testcase_httperror = TRUE	 
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
	successful_count BIGINT,
	failed_count BIGINT,
	error_count BIGINT)
AS $$	
BEGIN
RETURN QUERY
SELECT
	CAST(c.generated_date AS DATE) start_date,
	CAST(c.generated_date + '6 days' AS DATE) end_date,
	CAST(extract('week' FROM c.generated_date) AS INT) range_interval,
	CAST(extract('isoyear' FROM c.generated_date) AS INT) range_year,
	coalesce(x.total_count, 0) successful_count,
	coalesce(y.total_count, 0) failed_count,
	coalesce(z.total_count, 0) httperror_count
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
		directreceive_precanned = TRUE
		AND directreceive_errors = FALSE
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as x ON c.generated_date = x.week_start
	LEFT OUTER JOIN	
	(SELECT 
		date_trunc('week', directreceive_time) week_start,
		count(*) total_count
	FROM 
		direct_receive
	WHERE
		directreceive_uploaded = TRUE
		and directreceive_errors = FALSE	 
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
		directreceive_errors = TRUE	 
	GROUP BY
		week_start
	ORDER BY
		week_start desc
	LIMIT limited) as z ON c.generated_date = z.week_start
ORDER BY start_date DESC;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM directreceive_weekly_counts(104);


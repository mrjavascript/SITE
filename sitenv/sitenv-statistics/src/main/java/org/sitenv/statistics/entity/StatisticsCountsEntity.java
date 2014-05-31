package org.sitenv.statistics.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name="ccdaValidationWeeklyCounts", query = "SELECT * FROM ccda_validation_weekly_counts(?)", resultClass = StatisticsCountsEntity.class),
	@NamedNativeQuery(name="qrdaValidationWeeklyCounts", query = "SELECT * FROM qrda_validation_weekly_counts(?)", resultClass = StatisticsCountsEntity.class),
	@NamedNativeQuery(name="directWeeklyCounts", query = "SELECT * FROM directreceive_weekly_counts(?)", resultClass = StatisticsCountsEntity.class),
	@NamedNativeQuery(name="pdtiWeeklyCounts", query = "SELECT * FROM pdti_weekly_counts(?)", resultClass = StatisticsCountsEntity.class)

})
public class StatisticsCountsEntity {
	
	@Id
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name="end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(name="range_interval")
	private Integer interval; // month number or week number
	
	@Column(name="range_year")
	private Integer year;
	
	@Column(name="successful_count")
	private Long successfulCount;
	
	@Column(name="failed_count")
	private Long failedCount;
	
	@Column(name="error_count")
	private Long errorCount;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((errorCount == null) ? 0 : errorCount.hashCode());
		result = prime * result
				+ ((failedCount == null) ? 0 : failedCount.hashCode());
		result = prime * result
				+ ((interval == null) ? 0 : interval.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((successfulCount == null) ? 0 : successfulCount.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatisticsCountsEntity other = (StatisticsCountsEntity) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (errorCount == null) {
			if (other.errorCount != null)
				return false;
		} else if (!errorCount.equals(other.errorCount))
			return false;
		if (failedCount == null) {
			if (other.failedCount != null)
				return false;
		} else if (!failedCount.equals(other.failedCount))
			return false;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (successfulCount == null) {
			if (other.successfulCount != null)
				return false;
		} else if (!successfulCount.equals(other.successfulCount))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getSuccessfulCount() {
		return successfulCount;
	}

	public void setSuccessfulCount(Long successfulCount) {
		this.successfulCount = successfulCount;
	}

	public Long getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Long failedCount) {
		this.failedCount = failedCount;
	}

	public Long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
	}


	
}

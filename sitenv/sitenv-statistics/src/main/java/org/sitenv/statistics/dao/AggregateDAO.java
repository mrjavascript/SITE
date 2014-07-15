package org.sitenv.statistics.dao;

import java.util.List;

import org.sitenv.statistics.dto.AggregateWeeklyCounts;

public interface AggregateDAO {

	public List<AggregateWeeklyCounts> getAggregateWeeklyCounts(Integer numOfWeeks);
	
}

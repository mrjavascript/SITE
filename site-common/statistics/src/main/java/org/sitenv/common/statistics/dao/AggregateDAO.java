package org.sitenv.common.statistics.dao;

import java.util.List;

import org.sitenv.common.statistics.dto.AggregateWeeklyCounts;

public interface AggregateDAO {

	public List<AggregateWeeklyCounts> getAggregateWeeklyCounts(Integer numOfWeeks);
	
}

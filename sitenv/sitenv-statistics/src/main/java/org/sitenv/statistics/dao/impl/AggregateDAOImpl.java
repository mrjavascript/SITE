package org.sitenv.statistics.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.sitenv.statistics.dao.AggregateDAO;
import org.sitenv.statistics.dto.AggregateWeeklyCounts;
import org.sitenv.statistics.entity.AggregateWeeklyCountsEntity;
import org.springframework.stereotype.Repository;

@Repository(value="AggregateValidationDAO")
public class AggregateDAOImpl extends BaseDAOImpl implements AggregateDAO {

	@SuppressWarnings("unchecked")
	public List<AggregateWeeklyCounts> getAggregateWeeklyCounts(Integer numOfWeeks) {
		
		List<AggregateWeeklyCounts> returnVal = null;
		
		Query query = entityManager.createNamedQuery("aggregateWeeklyCounts", AggregateWeeklyCountsEntity.class);
		query.setParameter(1, numOfWeeks);
		
		List<AggregateWeeklyCountsEntity> results = query.getResultList();
		
		if (results != null) {
			for(AggregateWeeklyCountsEntity result : results) {
				if (returnVal == null)
				{
					returnVal = new ArrayList<AggregateWeeklyCounts>();
				}
				
				AggregateWeeklyCounts count = new AggregateWeeklyCounts();
				count.setEndDate(result.getEndDate());
				count.setInterval(result.getInterval());
				count.setStartDate(result.getStartDate());
				count.setYear(result.getYear());
				count.setCcdaCount(result.getCcdaCount());
				count.setDirectReceiveCount(result.getDirectReceiveCount());
				count.setPdtiCount(result.getPdtiCount());
				count.setQrdaCount(result.getQrdaCount());
				count.setDirectSentCount(result.getDirectSentCount());
				
				returnVal.add(count);
				
			}
		}
		
		return returnVal;
	}
	
}

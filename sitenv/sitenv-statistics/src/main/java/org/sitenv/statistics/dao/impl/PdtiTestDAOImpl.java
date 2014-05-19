package org.sitenv.statistics.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.sitenv.statistics.dao.PdtiTestDAO;
import org.sitenv.statistics.dto.PdtiTestCase;
import org.sitenv.statistics.entity.PdtiTestCaseEntity;
import org.sitenv.statistics.entity.PdtiTestGroupEntity;
import org.springframework.stereotype.Repository;

@Repository(value="PdtiTestDAO")
public class PdtiTestDAOImpl extends BaseDAOImpl implements PdtiTestDAO {

	public void createPdtiTest(List<PdtiTestCase> testCases) {
		
		if (testCases != null)
		{
			PdtiTestGroupEntity entity = new PdtiTestGroupEntity();
			for (PdtiTestCase test : testCases)
			{
				if (entity.getTestCases() == null)
				{
					entity.setTestCases(new ArrayList<PdtiTestCaseEntity>());
				}
				PdtiTestCaseEntity entityCase = new PdtiTestCaseEntity();
				entityCase.setHttpError(test.getHttpError());
				entityCase.setName(test.getTestCaseName());
				entityCase.setPass(test.getPass());
				entityCase.setPdtiTestGroupEntity(entity);
				
				entity.getTestCases().add(entityCase);
			}
			
			entityManager.persist(entity);
		}
		
	}

	public Long getPdtiTestCaseCount(String testcaseName, Boolean pass, Integer numOfDays) {
		Long count;
		
		if (pass == null)
		{
			if (testcaseName == null)
			{
				if (numOfDays == null) {
					
					count = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.httpError = false").getSingleResult();
					
				} else {
					
					Date currentDbDate = this.getSystemDate();
					Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.httpError = false");
					query.setParameter("currentDate", currentDbDate);
					query.setParameter("prevDate", pastDate);
					
					count = (Long) query.getSingleResult();
					
				}
			} else {
				
				if (numOfDays == null) {
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.testCaseName = :testCaseName AND t.httpError = false");
					query.setParameter("testCaseName", testcaseName).getSingleResult();
					count = (Long) query.getSingleResult();
					
				} else {
					
					Date currentDbDate = this.getSystemDate();
					Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE AND t.testCaseName = :testCaseName AND t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.httpError = false");
					query.setParameter("testCaseName", testcaseName).getSingleResult();
					query.setParameter("currentDate", currentDbDate);
					query.setParameter("prevDate", pastDate);
					
					count = (Long) query.getSingleResult();
					
				}
			}
		
		}
		else 
		{
			if (testcaseName == null)
			{
				if (numOfDays == null) {
					
					count = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.pass = :boolval AND t.httpError = false").setParameter("boolval", pass).getSingleResult();
					
				} else {
					
					Date currentDbDate = this.getSystemDate();
					Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.pass = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.httpError = false");
					query.setParameter("boolval", pass);
					query.setParameter("currentDate", currentDbDate);
					query.setParameter("prevDate", pastDate);
					
					count = (Long) query.getSingleResult();
					
				}
			} else {
				
				if (numOfDays == null) {
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.pass = :boolval AND t.testCaseName = :testCaseName AND t.httpError = false");
					query.setParameter("boolval", pass).getSingleResult();
					query.setParameter("testCaseName", testcaseName).getSingleResult();
					count = (Long) query.getSingleResult();
					
				} else {
					
					Date currentDbDate = this.getSystemDate();
					Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);
					
					Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.pass = :boolval AND t.testCaseName = :testCaseName AND t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.httpError = false");
					query.setParameter("boolval", pass);
					query.setParameter("testCaseName", testcaseName).getSingleResult();
					query.setParameter("currentDate", currentDbDate);
					query.setParameter("prevDate", pastDate);
					
					count = (Long) query.getSingleResult();
					
				}
			}
		}
		
		return count;
	}

	public Long getHttpErrorCount(Boolean hasHttpError, Integer numOfDays) {
		Long errorCount;
		
		if (numOfDays == null) {
			
			errorCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.httpError = :boolval").setParameter("boolval", hasHttpError).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.PdtiTestCaseEntity t WHERE t.httpError = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasHttpError);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			errorCount = (Long) query.getSingleResult();
			
		}
		
		return errorCount;
	}

}

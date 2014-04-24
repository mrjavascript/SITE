package org.sitenv.statistics.dao.impl;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.sitenv.statistics.dao.CcdaValidationDAO;
import org.sitenv.statistics.entity.CcdaDownloadEntity;
import org.sitenv.statistics.entity.CcdaValidationEntity;
import org.sitenv.statistics.entity.DateItemEntity;
import org.sitenv.statistics.entity.SmartCcdaValidationEntity;
import org.springframework.stereotype.Repository;

@Repository(value="CcdaValidationDAO")
public class CcdaValidationDAOImpl implements CcdaValidationDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public void createCcdaValidation(Boolean hasErrors, Boolean hasWarnings,
			Boolean hasInfo, Boolean hasHttpError) {
		CcdaValidationEntity entity = new CcdaValidationEntity();
		
		entity.setErrors(hasErrors);
		entity.setWarnings(hasWarnings);
		entity.setInfo(hasInfo);
		entity.setHttpError(hasHttpError);
		
		entityManager.persist(entity);
	}
	
	public void createSmartCcdaValidation(Boolean hasHttpError) {
		SmartCcdaValidationEntity entity = new SmartCcdaValidationEntity();
		
		entity.setHttpError(hasHttpError);
		
		entityManager.persist(entity);
	}

	public void createCcdaDownload() {
		CcdaDownloadEntity entity = new CcdaDownloadEntity();
		
		entityManager.persist(entity);
	}
	
	/**
	 * @return System date on DB server
	 */
	private Date getSystemDate() {
	    Query query = entityManager.createNativeQuery(
	            "SELECT CURRENT_TIMESTAMP as currtime", DateItemEntity.class);
	    DateItemEntity dateItem = (DateItemEntity) query.getSingleResult();
	    return dateItem.getDate();
	}
	
	public Long getHttpErrorCount(Boolean hasHttpError, Integer numOfDays) {
		
		Long errorCount;
		
		if (numOfDays == null) {
			
			errorCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.httpError = :boolval").setParameter("boolval", hasHttpError).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.httpError = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasHttpError);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			errorCount = (Long) query.getSingleResult();
			
		}
		
		return errorCount;
		
	}

	public Long getErrorCount(Boolean hasErrors, Integer numOfDays) {
		
		Long errorCount;
		
		if (numOfDays == null) {
			
			errorCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.errors = :boolval").setParameter("boolval", hasErrors).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasErrors);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			errorCount = (Long) query.getSingleResult();
			
		}
		
		return errorCount;
		
	}

	public Long getWarningCount(Boolean hasWarnings, Integer numOfDays) {

		Long warnCount;
		
		if (numOfDays == null) {
			
			warnCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.warnings = :boolval").setParameter("boolval", hasWarnings).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.warnings = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasWarnings);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			warnCount = (Long) query.getSingleResult();
			
		}
		
		return warnCount;
	}

	public Long getInfoCount(Boolean hasInfo, Integer numOfDays) {

		Long infoCount;
		
		if (numOfDays == null) {
			
			infoCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.info = :boolval").setParameter("boolval", hasInfo).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.info = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasInfo);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			infoCount = (Long) query.getSingleResult();
			
		}
		
		return infoCount;
	}

	public Long getTotalCount(Integer numOfDays) {

		Long totalCount;
		
		if (numOfDays == null) {
			
			totalCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE t.httpError = false").getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaValidationEntity t WHERE  t.httpError = false AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			totalCount = (Long) query.getSingleResult();
			
		}
		
		return totalCount;
	}
	
	

	public Long getCcdaDownloadCount(Integer numOfDays) {
		
		Long downloadCount;
		
		if (numOfDays == null) {
			
			downloadCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaDownloadEntity t").getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.CcdaDownloadEntity t WHERE  t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			downloadCount = (Long) query.getSingleResult();
			
		}
		
		return downloadCount;
		
	}

	public Long getSmartCcdaCount(Boolean hasHttpError, Integer numOfDays) {
		
		Long totalCount;
		
		if (numOfDays == null) {
			
			totalCount = (Long) entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.SmartCcdaValidationEntity t WHERE t.httpError = :httperror").setParameter("httperror", hasHttpError).getSingleResult();
			
		} else {
			
			Date currentDbDate = this.getSystemDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDbDate);
			cal.add(Calendar.DAY_OF_YEAR, ((-1) * numOfDays));
			Date pastDate = cal.getTime();
			
			Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.SmartCcdaValidationEntity t WHERE  t.httpError = :httperror AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("httperror", hasHttpError);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);
			
			totalCount = (Long) query.getSingleResult();
			
		}
		
		return totalCount;
	
	}
	
	
	
	
	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	
	
	

}

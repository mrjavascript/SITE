package org.sitenv.statistics.dao.impl;

import java.util.Date;

import javax.persistence.Query;

import org.sitenv.statistics.dao.DirectTransmissionDAO;
import org.sitenv.statistics.entity.DirectReceiveEntity;
import org.sitenv.statistics.entity.DirectTrustUploadEntity;
import org.springframework.stereotype.Repository;

@Repository(value="DirectTransmissionDAO")
public class DirectTransmissionDAOImpl extends BaseDAOImpl implements DirectTransmissionDAO {

	public void createDirectReceive(Boolean precanned, Boolean uploaded,
			Boolean hasErrors) {
		DirectReceiveEntity entity = new DirectReceiveEntity();

		entity.setPrecanned(precanned);
		entity.setUploaded(uploaded);
		entity.setErrors(hasErrors);

		entityManager.persist(entity);
	}

	public void createDirectTrustUpload(Boolean hasErrors) {
		
		DirectTrustUploadEntity entity = new DirectTrustUploadEntity();

		entity.setErrors(hasErrors);

		entityManager.persist(entity);
		
	}

	public Long getDirectReceivePrecannedCount(Boolean precanned,
			Boolean hasErrors, Integer numOfDays) {
		
		Long errorCount;

		if (precanned == null) {

			if (numOfDays == null) {

				errorCount = (Long) entityManager
						.createQuery(
								"SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval")
						.setParameter("boolval", hasErrors).getSingleResult();

			} else {

				Date currentDbDate = this.getSystemDate();
				Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);

				Query query = entityManager
						.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
				query.setParameter("boolval", hasErrors);
				query.setParameter("currentDate", currentDbDate);
				query.setParameter("prevDate", pastDate);

				errorCount = (Long) query.getSingleResult();

			}
		} else {
			if (numOfDays == null) {

				Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval and t.precanned = :precanned");
				
				query.setParameter("boolval", hasErrors);
				query.setParameter("precanned", precanned);
				
				errorCount = (Long) query.getSingleResult();

			} else {

				Date currentDbDate = this.getSystemDate();
				Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);

				Query query = entityManager
						.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.precanned = :precanned");
				query.setParameter("boolval", hasErrors);
				query.setParameter("currentDate", currentDbDate);
				query.setParameter("prevDate", pastDate);
				query.setParameter("precanned", precanned);

				errorCount = (Long) query.getSingleResult();

			}
		}

		return errorCount;
	}

	public Long getDirectReceiveUploadCount(Boolean upload, Boolean hasErrors,
			Integer numOfDays) {
		Long errorCount;

		if (upload == null) {

			if (numOfDays == null) {

				errorCount = (Long) entityManager
						.createQuery(
								"SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval")
						.setParameter("boolval", hasErrors).getSingleResult();

			} else {

				Date currentDbDate = this.getSystemDate();
				Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);

				Query query = entityManager
						.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
				query.setParameter("boolval", hasErrors);
				query.setParameter("currentDate", currentDbDate);
				query.setParameter("prevDate", pastDate);

				errorCount = (Long) query.getSingleResult();

			}
		} else {
			if (numOfDays == null) {

				Query query = entityManager.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval and t.uploaded = :upload");
				
				query.setParameter("boolval", hasErrors);
				query.setParameter("upload", upload);
				
				errorCount = (Long) query.getSingleResult();

			} else {

				Date currentDbDate = this.getSystemDate();
				Date pastDate = this.getPreviousDate(currentDbDate, numOfDays);

				Query query = entityManager
						.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate AND t.uploaded = :upload");
				query.setParameter("boolval", hasErrors);
				query.setParameter("currentDate", currentDbDate);
				query.setParameter("prevDate", pastDate);
				query.setParameter("upload", upload);

				errorCount = (Long) query.getSingleResult();

			}
		}

		return errorCount;
	}

	public Long getDirectReceiveCount(Boolean hasErrors, Integer numberOfDays) {
		Long errorCount;

		if (numberOfDays == null) {

				errorCount = (Long) entityManager
						.createQuery(
								"SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval")
						.setParameter("boolval", hasErrors).getSingleResult();

			} else {

				Date currentDbDate = this.getSystemDate();
				Date pastDate = this.getPreviousDate(currentDbDate, numberOfDays);

				Query query = entityManager
						.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
				query.setParameter("boolval", hasErrors);
				query.setParameter("currentDate", currentDbDate);
				query.setParameter("prevDate", pastDate);

				errorCount = (Long) query.getSingleResult();

			}
		

		return errorCount;
	}

	public Long getDirectTrustUploadCount(Boolean hasErrors,
			Integer numberOfDays) {
		Long errorCount;

		if (numberOfDays == null) {

			errorCount = (Long) entityManager
					.createQuery(
							"SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval")
					.setParameter("boolval", hasErrors).getSingleResult();

		} else {

			Date currentDbDate = this.getSystemDate();
			Date pastDate = this.getPreviousDate(currentDbDate, numberOfDays);

			Query query = entityManager
					.createQuery("SELECT COUNT(t) FROM org.sitenv.statistics.entity.DirectReceiveEntity t WHERE t.errors = :boolval AND t.timestamp < :currentDate AND t.timestamp > :prevDate");
			query.setParameter("boolval", hasErrors);
			query.setParameter("currentDate", currentDbDate);
			query.setParameter("prevDate", pastDate);

			errorCount = (Long) query.getSingleResult();

		}
	

	return errorCount;
	}

}

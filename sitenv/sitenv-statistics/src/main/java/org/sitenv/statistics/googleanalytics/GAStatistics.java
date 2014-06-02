package org.sitenv.statistics.googleanalytics;


import java.io.IOException;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Calendar;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;


public class GAStatistics {
	
	
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	public GAStatistics() {}
	
	private static Analytics getAnalytics(Credential credential){
		
		Analytics.Builder builder = new Analytics.Builder(TRANSPORT, JSON_FACTORY, credential);
		builder.setApplicationName("SITE-Google-Analytics");
		Analytics analytics = builder.build();
		
		return analytics;
	}
	
	private static GoogleCredential getCredential() throws GeneralSecurityException, IOException {
		
	    GoogleCredential credential = new GoogleCredential.Builder()
	      .setTransport(TRANSPORT)
	      .setJsonFactory(JSON_FACTORY)
	      .setServiceAccountId("860345658841-4qhnghvlfknrmkfjcri2d58fmo99r9go@developer.gserviceaccount.com")
	      .setServiceAccountScopes(Arrays.asList(AnalyticsScopes.ANALYTICS_READONLY))
	      .setServiceAccountPrivateKeyFromP12File(new File("C:\\Google\\dfe392e5fc170841f90a5324f3cf2fc5bb0a1a34-privatekey.p12"))
	      .build();
	    
	    return credential;
	}
	
	private static String SITEProfileId(Analytics analytics) throws IOException {
	    String profileId = null;

	    // Query accounts collection.
	    com.google.api.services.analytics.model.Accounts accounts = analytics.management().accounts().list().execute();

	    if (accounts.getItems().isEmpty()) {
	      System.err.println("No accounts found");
	    } else {
	      String firstAccountId = accounts.getItems().get(0).getId();
	      
	      // Query webproperties collection.
	      Webproperties webproperties = analytics.management().webproperties()
	          .list(firstAccountId).execute();

	      if (webproperties.getItems().isEmpty()) {
	        System.err.println("No Webproperties found");
	      } else {
	        String firstWebpropertyId = webproperties.getItems().get(0).getId();
	        
	        // Query views (profiles) collection.
	        Profiles profiles = analytics.management().profiles()
	            .list(firstAccountId, firstWebpropertyId).execute();
	        
	        if (profiles.getItems().isEmpty()) {
	          System.err.println("No views (profiles) found");
	        } else {
	        	// 1 for "SITE All Projects" 
	          profileId = profiles.getItems().get(1).getId();
	        }
	      }
	    }
	    return profileId;
	}
	
	/**
	 * @param analytics
	 * @param profileId
	 * @param startDate format = YYYY-mm-DD
	 * @param endDate format = YYYY-mm-DD
	 * @return
	 * @throws IOException
	 */
	private static String sessionCount(Analytics analytics, 
			  String profileId,
			  String startDate,
			  String endDate) throws IOException 
	{
		GaData sessionData = analytics.data().ga()
				.get("ga:" + profileId, 		// Table Id. ga: + profile id.
						startDate,					// Start date.
						endDate,					// End date.
						"ga:sessions")				// Metrics.
		        .execute();
		    
		String sessionCount = sessionData.getRows().get(0).get(0);//.toString();
		return sessionCount;
	}
	  
	/**
	 * @param analytics
	 * @param profileId
	 * @param startDate format = YYYY-mm-DD
	 * @param endDate format = YYYY-mm-DD
	 * @return
	 * @throws IOException
	 */
	private static String pageViewCount(Analytics analytics, 
			String profileId,
			String startDate,
			String endDate) throws IOException 
	{
		GaData sessionData = analytics.data().ga()
				.get("ga:" + profileId, 		// Table Id. ga: + profile id.
						startDate,					// Start date
						endDate,					// End date.
			            "ga:pageviews")				// Metrics.
			    .execute();
			    
		String sessionCount = sessionData.getRows().get(0).get(0);//.toString();
		return sessionCount;
	}
		  
	private static String[] dateRange(int ndays){
		
		Calendar cal = Calendar.getInstance();
		String todayYear = String.valueOf(cal.get(Calendar.YEAR));
		String todayMonth = String.valueOf(cal.get(Calendar.MONTH) +1);
		String todayDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
				
		if (todayMonth.length() == 1){
			todayMonth = "0"+todayMonth;
		}
		if (todayDay.length() == 1){
			todayDay = "0"+todayDay;
		}
				
		String todayDate = todayYear + "-" + todayMonth + "-" + todayDay;
		
		cal.add(Calendar.DAY_OF_YEAR, -ndays);
		String nDaysAgoYear = String.valueOf(cal.get(Calendar.YEAR));
		String nDaysAgoMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		String nDaysAgoDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		
		if (nDaysAgoMonth.length() == 1){
			nDaysAgoMonth = "0"+nDaysAgoMonth;
		}
		if (nDaysAgoDay.length() == 1){
			nDaysAgoDay = "0"+nDaysAgoDay;
		}
		
		String nDaysAgoDate = nDaysAgoYear + "-" + nDaysAgoMonth + "-" + nDaysAgoDay;
		return new String[]{nDaysAgoDate, todayDate};
	}
	
	public static Long getSessionCount(int numOfDays) throws GeneralSecurityException, IOException {
		
		Analytics analytics = getAnalytics(getCredential());
		
		String profileId = SITEProfileId(analytics);
		
		String sessionCount = null;
		
		if (numOfDays == 0){
			
			sessionCount = sessionCount(analytics, profileId, 
					"2010-01-01", "today");
			
		} else {
			
			String[] formattedDates = dateRange(numOfDays);
			sessionCount = sessionCount(analytics, profileId, 
					formattedDates[0], formattedDates[1]);
		}
		return Long.valueOf(sessionCount);
	}
	
	public static Long getPageViewCount(int numOfDays) throws GeneralSecurityException, IOException {
		
		Analytics analytics = getAnalytics(getCredential());
		
		String profileId = SITEProfileId(analytics);
		
		String pageViewCount = null;
		
		if (numOfDays == 0){
			
			pageViewCount = pageViewCount(analytics, profileId, 
					"2010-01-01", "today");
			
		} else {
			
			String[] formattedDates = dateRange(numOfDays);
			pageViewCount = pageViewCount(analytics, profileId, 
					formattedDates[0], formattedDates[1]);
		}
		return Long.valueOf(pageViewCount);
	}
}

package org.sitenv.servlets.providerdirectory;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.eviware.soapui.SoapUI;

public class SingleTestServletListener implements ServletContextListener {

	private ServletContext context;

	private static Logger logger = Logger.getLogger(SingleTestServletListener.class);
	
	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@SuppressWarnings("deprecation")
	public void contextDestroyed(ServletContextEvent sce) {
		context = sce.getServletContext();

		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName() != null && t.getName().matches("(Thread-)\\d*")) {
				logger.info("Found Soap UI's MultiThreadedHttpConnectionManager.  Killing It");
				t.interrupt();
			}
			if (t.getName().equals("FileWatchdog")) {
				logger.info("Found FileWatchdog thread.  Killing it");
				t.stop();
			}
			if (t.getName().equals("Finalizer")) {
				logger.info("Found Finalizer thread.  Killing it");
				t.interrupt();
			}
		}

		SoapUI.getThreadPool().shutdownNow();
		try {
			SoapUI.getThreadPool().awaitTermination(120, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SoapUI.shutdown();

		System.out.println("contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		context = arg0.getServletContext();
	}
}

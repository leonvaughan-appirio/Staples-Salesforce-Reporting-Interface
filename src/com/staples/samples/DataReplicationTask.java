package com.staples.samples;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataReplicationTask {
	Timer timer;
	public static final String PROPSFILEPATH = "sample.properties";
	// Create a logger 
	private static final Logger LOGGER = LogManager.getLogger(DataReplicationTask.class.getName());

    public DataReplicationTask(int seconds) {
        timer = new Timer();
        timer.schedule(new DataReplicationTimerTask(), 0, seconds*1000);
	}

    class DataReplicationTimerTask extends TimerTask {
        public void run() {
            //System.out.println("Running Data Replication task");
        	LOGGER.info("Running Data Replication task");
            ReportingInterfaceSample sample = new ReportingInterfaceSample();
  	      	sample.run();
            //timer.cancel(); //Terminate the timer thread
        }
    }

    public static void main(String args[]) {
    	try {
    		String frequency;
	    	PropertiesConfiguration conf = new PropertiesConfiguration(PROPSFILEPATH);
	    	frequency = conf.getProperty("DATA_REPLICATION_FREQUENCY").toString();
	        new DataReplicationTask(Integer.parseInt(frequency));
	        System.out.println("Salesforce Data Replication task scheduled.");
    	}
	   catch (Exception e) {
		   LOGGER.error(e.getMessage());
	       e.printStackTrace();
	   }
    }
	
	

}

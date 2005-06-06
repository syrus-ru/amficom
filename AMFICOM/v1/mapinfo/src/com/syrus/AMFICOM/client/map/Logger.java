package com.syrus.AMFICOM.client.map;

/**
 * $Id: Logger.java,v 1.1.2.2 2005/06/06 13:16:59 krupenn Exp $ Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

public class Logger {
    private static long startTime = System.currentTimeMillis();
    
    private static boolean status = true;

	public static synchronized void logSeparator() {
        if (!status)
            return;
        System.out.println("\n");
	}

	public static synchronized void log(String msg) {
        if (!status)
            return;
        
		System.out.println((System.currentTimeMillis() - Logger.startTime) + "   " + msg);
	}
    /**
     * Is logging out information if status is true
     * @param status
     */
    public static void setStatus(boolean status) {
        Logger.status = status;
    }
}

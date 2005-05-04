import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * $Id: Logger.java,v 1.1 2005/05/04 14:53:45 krupenn Exp $ Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

class Logger {
	private static SimpleDateFormat sdFormat = new SimpleDateFormat(
			"E M d H:m:s:S");

	private static FileOutputStream logFOS = null;

	static {
		try {
			Logger.logFOS = new FileOutputStream("mcs_log.txt", true);
		} catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void logSeparator() {
		if(Logger.logFOS == null) {
			return;
		}

		try {
			Logger.logFOS.write("\n".getBytes());
			Logger.logFOS.flush();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void log(String msg) {
		if(Logger.logFOS == null) {
			return;
		}

		try {
			String dateString = Logger.sdFormat.format(
					new Date(System.currentTimeMillis()));
			Logger.logFOS.write(
					(dateString + "  MCS - " + msg + "\n").getBytes());
			Logger.logFOS.flush();
		} catch(IOException exc) {
			// Ошибка при выводе логов
			exc.printStackTrace();
		}
	}
}

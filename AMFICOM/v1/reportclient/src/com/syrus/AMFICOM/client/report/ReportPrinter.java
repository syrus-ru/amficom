/*
 * $Id: ReportPrinter.java,v 1.5 2005/09/23 12:10:59 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.report.ReportTemplate;

public class ReportPrinter {
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	private static final String PRINTABLE_FILE_NAME = TEMP_DIR + "print.html";
//	private static final String FILES_DIR_NAME = TEMP_DIR + "print.files";	
	
	public static void printReport(
			ReportRenderer reportPreviewRenderer,
			ReportTemplate reportTemplate) {
		reportPreviewRenderer.setPrintable(true);
		
		HTMLReportEncoder encoder = new HTMLReportEncoder(
				reportPreviewRenderer.getRenderingComponents(),
				reportTemplate);

		reportPreviewRenderer.setPrintable(false);		
		
		try {
			encoder.encodeToHTML(PRINTABLE_FILE_NAME);
			String command = "rundll32 MSHTML.DLL,PrintHTML \"" + PRINTABLE_FILE_NAME + "\"";			
			Runtime.getRuntime().exec(command);
		}
		catch (Exception e) {
		}

		//Временные файлы не удаляются, поскольку Runtime не ждёт завершения
		//процесса, а инициирует его и идёт дальше. И процесс печати получает
		//на вход удалённый HTML файл.
//		File printableFile = new File(PRINTABLE_FILE_NAME);		
//		if (printableFile.exists())
//			printableFile.delete();
//		File filesDir = new File(FILES_DIR_NAME);		
//		if (filesDir.exists())
//			filesDir.delete();
	}
}

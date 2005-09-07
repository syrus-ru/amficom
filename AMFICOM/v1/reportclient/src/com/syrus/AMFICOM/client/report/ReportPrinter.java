/*
 * $Id: ReportPrinter.java,v 1.1 2005/09/07 14:26:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

public class ReportPrinter {
//	public void printReport() {
//	String s = System.getProperty("java.io.tmpdir")
//			+ System.getProperty("file.separator") + "print.tmp";
//
//	File f = new File(s);
//	try {
//		for (ListIterator it = this.labels.listIterator(); it.hasNext();) {
//			FirmedTextPane ftp = (FirmedTextPane) it.next();
//			ftp.setSize(ftp.getWidth()
//					+ (int) ftp.getFont().getMaxCharBounds(
//							new FontRenderContext(null, true, true))
//							.getWidth(), ftp.getHeight());
//		}
//
//		if (this.saveToHTML(f.getAbsolutePath(), true) != 0)
//			return;
//
//		for (ListIterator it = this.labels.listIterator(); it.hasNext();) {
//			FirmedTextPane ftp = (FirmedTextPane) it.next();
//			ftp.setSize(ftp.getWidth()
//					- (int) ftp.getFont().getMaxCharBounds(
//							new FontRenderContext(null, true, true))
//							.getWidth(), ftp.getHeight());
//		}
//
//		/*
//		 //			DocFlavor psFlavor =  new DocFlavor.INPUT_STREAM("text/html; charset=windows-1251");
//		 DocFlavor psFlavor = DocFlavor.INPUT_STREAM.TEXT_HTML_HOST;
//		 //			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//		 //			aset.add(MediaSizeName.ISO_A4);
//		 Doc doc = null;
//		 try
//		 {
//		 FileInputStream fis = new FileInputStream(f.getPath());
//		 doc = new SimpleDoc(fis, psFlavor, null);
//		 }
//		 catch (IOException e)
//		 {
//		 System.err.println(e);
//		 }
//		 PrintService[] pservices = PrintServiceLookup.lookupPrintServices(psFlavor,
//		 null);
//		 //			PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
//		 if (pservices.length > 0)
//		 {
//		 DocPrintJob pj = pservices[0].createPrintJob();
//		 //        PrinterJob pj = PrinterJob.getPrinterJob();
//		 //        if (pj.printDialog())
//		 try
//		 {
//		 pj.print(doc,null);
//		 }
//		 catch (PrintException e)
//		 {
//		 System.err.println(e);
//		 }
//		 }
//		 */
//		/*
//		 String s = null;
//		 s = "file:"
//		 + System.getProperty("user.dir")
//		 + System.getProperty("file.separator")
//		 + f.getName();
//		 JEditorPane ep = new JEditorPane();
//		 ep.setPage(s);
//		 //		JDialog main = new JDialog();
//		 //			main.setModal(true);
//		 //			main.setSize(800,800);
//		 //			main.getContentPane().setLayout(new java.awt.BorderLayout());
//		 //			main.getContentPane().add(new JScrollPane(ep),java.awt.BorderLayout.CENTER);
//		 //			main.setVisible(true);
//		 DocumentRenderer dr = new DocumentRenderer();
//		 dr.print(ep);*/
//
//		String command = "rundll32 MSHTML.DLL,PrintHTML \"" + s + "\"";
//		Runtime.getRuntime().exec(command);
//	} catch (Exception e) {
//		if (f.exists()) {
//			f.delete();
//		}
//	}
//}
}

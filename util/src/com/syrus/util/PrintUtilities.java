/*-
 * $Id: PrintUtilities.java,v 1.4 2006/04/28 09:03:04 arseniy Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/28 09:03:04 $
 * @author $Author: arseniy $
 * @author Kholshin Stanislav
 * @module util
 */
public class PrintUtilities implements Printable {
	private static boolean fitToWidth = true;
	private static boolean useDoubleBuffering = false;

	private double scale = 1;
	private boolean scaleSet = false;
	private final JComponent c;

	public static void printComponent(final JComponent c, final double scale) {
		final PrintUtilities pu = new PrintUtilities(c);
		pu.setScale(scale);
		pu.print();
	}

	public static void printComponent(final JComponent c) {
		new PrintUtilities(c).print();
	}

	public PrintUtilities(final JComponent c) {
		this.c = c;
	}

	public void setScale(final double scale) {
		this.scale = scale;
		this.scaleSet = true;
	}

	public static void fitToWidth(final boolean b) {
		fitToWidth = b;
	}
	
	public static void useDoubleBuffering(final boolean b) {
		useDoubleBuffering = b;
	}

	public void print() {
		final PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog())
			try {
				printJob.print();
			} catch (PrinterException pe) {
				Log.errorMessage(pe);
			}
	}

  public int print(final Graphics g, final PageFormat pageFormat, final int pageIndex) {
		final Graphics2D g2d = (Graphics2D) g;

		final int fontHeight = g2d.getFontMetrics().getHeight();
		final int fontDesent = g2d.getFontMetrics().getDescent();
		final double pageHeight = pageFormat.getImageableHeight();

		final double compWidthOnPage = pageFormat.getImageableWidth();
		final double compHeightOnPage = pageHeight;
		if (!this.scaleSet && fitToWidth && this.c.getWidth() > compWidthOnPage) {
			this.scale = compWidthOnPage / this.c.getWidth();
		}

		final int totalPages = (int) Math.ceil(this.c.getHeight() * this.scale / compHeightOnPage);

		if (pageIndex >= totalPages) {
			return Printable.NO_SUCH_PAGE;
		}

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		if (totalPages > 1) {
			g2d.drawString(Integer.toString(pageIndex + 1),
					(int)pageFormat.getImageableWidth() / 2,
					(int)(pageHeight + fontHeight - fontDesent));
		}

		g2d.translate(0f, -pageIndex * compHeightOnPage);

		if (pageIndex + 1 == totalPages) {
			g2d.setClip(0,
					(int) (compHeightOnPage * pageIndex),
					(int) Math.ceil(compWidthOnPage),
					(int) Math.ceil(this.c.getSize().height * this.scale - pageIndex * compHeightOnPage));
		} else {
			g2d.setClip(0,
					(int) (compHeightOnPage * pageIndex),
					(int) Math.ceil(compWidthOnPage),
					(int) Math.ceil(compHeightOnPage));
		}
		g2d.scale(this.scale, this.scale);

		final RepaintManager currentManager = RepaintManager.currentManager(this.c);
		final boolean doubleBufferingEnabled = currentManager.isDoubleBufferingEnabled();
		currentManager.setDoubleBufferingEnabled(useDoubleBuffering);
		this.c.paint(g2d);
		currentManager.setDoubleBufferingEnabled(doubleBufferingEnabled);
		
		return Printable.PAGE_EXISTS;
	}
}

/*-
 * $Id: PrintUtilities.java,v 1.2 2006/04/24 05:52:33 arseniy Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/24 05:52:33 $
 * @author $Author: arseniy $
 * @author Kholshin Stanislav
 * @module util
 */
public class PrintUtilities implements Printable {
	private boolean fitToWidth = true;
	private final JComponent c;

	public static void printComponent(final JComponent c) {
		new PrintUtilities(c).print();
	}

	public PrintUtilities(final JComponent c) {
		this.c = c;
	}

	public void setFitToWidth(final boolean fitToWidth) {
		this.fitToWidth = fitToWidth;
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
		double pageHeight = pageFormat.getImageableHeight() - fontHeight;

		final double compWidthOnPage = pageFormat.getImageableWidth() + pageFormat.getImageableX();
		final double compHeightOnPage = pageHeight;
		double scale = 1.0;
		if (this.fitToWidth && this.c.getWidth() > compWidthOnPage) {
			scale = compWidthOnPage / this.c.getWidth();
		}

		final int totalPages = (int) Math.ceil(this.c.getHeight() * scale / compHeightOnPage);

		if (pageIndex >= totalPages) {
			return Printable.NO_SUCH_PAGE;
		}

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		if (totalPages > 1) {
			g2d.drawString("page: " + (pageIndex + 1),
					(int) pageFormat.getImageableWidth() / 2 - 35,
					(int) (pageHeight + fontHeight - fontDesent));
		}

		g2d.translate(0f, -pageIndex * compHeightOnPage);

		if (pageIndex + 1 == totalPages) {
			g2d.setClip(0,
					(int) (compHeightOnPage * pageIndex),
					(int) Math.ceil(compWidthOnPage),
					(int) Math.ceil(this.c.getSize().height * scale - pageIndex * compHeightOnPage));
		} else {
			g2d.setClip(0,
					(int) (compHeightOnPage * pageIndex),
					(int) Math.ceil(compWidthOnPage),
					(int) Math.ceil(compHeightOnPage));
		}
		g2d.scale(scale, scale);

		disableDoubleBuffering(this.c);
		this.c.paint(g2d);
		enableDoubleBuffering(this.c);

		return Printable.PAGE_EXISTS;
	}

	private static void disableDoubleBuffering(final Component c) {
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	private static void enableDoubleBuffering(final Component c) {
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}

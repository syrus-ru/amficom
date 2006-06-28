/*
 * UIGeneralStorage.java
 * Created on 10.06.2004 13:55:54
 * 
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @version $Revision: 1.18 $, $Date: 2006/05/18 19:44:36 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class CommonUIUtilities {

	private static final double SCALE_FACTOR = 1.25;  
	
	public static final Font	DIALOG_FONT	= new Font("Dialog", Font.PLAIN, 11);
	public static final Font	SMALL_FONT	= new Font("Dialog", Font.PLAIN, 10);

	private CommonUIUtilities() {
		// singleton
		throw new Error("UIGeneralStorage is just a container for static methods and constans");
	}

	public static void fixHorizontalSize(JComponent component) {
		Dimension minimumSize = component.getMinimumSize();
		Dimension maximumSize = component.getMaximumSize();
		maximumSize.width = (int) (SCALE_FACTOR * minimumSize.width);
		component.setMaximumSize(maximumSize);
		Dimension preferredSize = component.getPreferredSize();
		preferredSize.width = (int) (SCALE_FACTOR * minimumSize.width);
		component.setPreferredSize(preferredSize);
	}
	
	public static String getHTMLColor(final Color color) {
		final StringBuffer buffer = new StringBuffer('#');
		final String[] colors = new String[] {Integer.toHexString(color.getRed()), Integer.toHexString(color.getGreen()), Integer.toHexString(color.getBlue())};
		for(final String string : colors) {
			if (string.length() < 2) {
				buffer.append('0');
			}
			buffer.append(string);
		}
		return buffer.toString();
	}
	
	public static void setLocationRelativeTo(final Window window, final Component component) {
		window.setLocationRelativeTo(component);
		
		final GraphicsEnvironment localGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle maximumWindowBounds = 
			localGraphicsEnvironment.getMaximumWindowBounds();
		int dx = 0;
		final int mx2 = (int) (maximumWindowBounds.getX() + maximumWindowBounds.getWidth());
		final int x2 = window.getX() + window.getWidth();
		if (x2 > mx2) {
			dx = mx2 - x2; 
		}
		int dy = 0;
		final int my2 = (int) (maximumWindowBounds.getY() + maximumWindowBounds.getHeight());
		final int y2 = window.getY() + window.getHeight();
		if (y2 > my2) {
			dy = my2 - y2; 
		}
		if (dx != 0 || dy != 0) {
			window.setLocation(window.getX() + dx, window.getY() + dy);
		}
	}
	
	public static void arrangeTableColumns(JTable table) {

		TableModel model = table.getModel();
		int[] columnsWidth = new int[model.getColumnCount()];
		{
			JTableHeader tableHeader = table.getTableHeader();
			TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
			TableColumnModel columnModel = tableHeader.getColumnModel();
			for (int i = 0; i < columnModel.getColumnCount(); i++) {
				Component component = headerRenderer.getTableCellRendererComponent(null, columnModel.getColumn(i)
						.getHeaderValue(), false, false, 0, 0);
				columnsWidth[i] = component.getPreferredSize().width;
			}
		}
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < columnsWidth.length; j++) {
				Rectangle cellRect = table.getCellRect(i, j, true);
				int width = cellRect.width - cellRect.x;
				if (width > columnsWidth[j])
					columnsWidth[j] = width;
			}
		}

		for (int j = 0; j < columnsWidth.length; j++) {
			table.getColumnModel().getColumn(j).setPreferredWidth(columnsWidth[j]);
		}
	}
	
	/**
	 * convert string into HTML
	 * @param string input string 
	 * @return htmled string
	 */
	public static final String convertToHTMLString(final String string) {
		if (string.startsWith("<html>") || string.startsWith("<HTML>")) {
			return string;
		}
		
		final StringBuilder builder = new StringBuilder();
		builder.append("<html>");
		builder.append(string.replaceAll("&", "&amp;").
				replaceAll("\"", "&quot;").
				replaceAll("'", "&apos;").
				replaceAll("<", "&lt;").
				replaceAll(">", "&gt;").
				replaceAll("\n", "<br>\n"));			
		builder.append("</html>");
		return builder.toString();
	}

	/**
	 * @deprecated use {@link ProcessingDialog} instead of this method
	 */
	@Deprecated
	public static void invokeAsynchronously(final Runnable doRun,
											final String dialogTitle) {
		new ProcessingDialog(doRun, dialogTitle);
	}
}

/*
 * UIGeneralStorage.java
 * Created on 10.06.2004 13:55:54
 * 
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.model.Environment;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/15 08:33:57 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module generalclient_v1
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
	

	public static synchronized void invokeAsynchronously(final Runnable doRun,
											final String dialogTitle) {
		new Thread() {

			public void run() {
				/**
				 * @todo Set dialog's parent depending on app context.
				 */
				final JDialog jDialog = new JDialog(Environment.getActiveWindow(), dialogTitle, true);
				jDialog.setResizable(false);

				((JPanel) (jDialog.getContentPane())).setPreferredSize(new Dimension(400, 0));
				jDialog.pack();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				jDialog.setLocation((screenSize.width - jDialog.getWidth()) / 2,
					(screenSize.height - jDialog.getHeight()) / 2);

				Thread targetThread = new Thread() {

					public void run() {
						doRun.run();
					}
				};
				targetThread.start();

				Thread dialogThread = new Thread() {

					public void run() {
						jDialog.setVisible(true);
					}
				};
				dialogThread.start();

				try {
					targetThread.join();
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				jDialog.dispose();
			}
		}.start();
	}
}

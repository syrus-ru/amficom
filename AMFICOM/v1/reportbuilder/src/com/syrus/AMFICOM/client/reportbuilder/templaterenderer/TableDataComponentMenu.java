/*
 * $Id: TableDataComponentMenu.java,v 1.2 2005/09/30 08:13:22 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public class TableDataComponentMenu extends JPopupMenu {
	private static final long serialVersionUID = 1882326318246146612L;
	
	protected TableDataStorableElement tableDataElement = null;
	protected ApplicationContext applicationContext = null;

	public TableDataComponentMenu(
			TableDataStorableElement tableDataElement,
			ApplicationContext aContext) {
		this.tableDataElement = tableDataElement;
		this.applicationContext = aContext;
		
		//Установка числа вертикальных разбиений
		JMenuItem setVertDivisionsCountMenuItem = new JMenuItem();
		setVertDivisionsCountMenuItem.setText(LangModelReport.getString(
				"report.UI.TableComponentMenu.title"));
		setVertDivisionsCountMenuItem.addActionListener(
				new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				TableDataStorableElement element =
					TableDataComponentMenu.this.tableDataElement;
				
				String newVertDivCountString = JOptionPane.showInputDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString(
								"report.UI.TableComponentMenu.tableVertDivCount"));
				
				int newVertDivCount = element.getVerticalDivisionsCount();
				try {
					newVertDivCount = Integer.parseInt(newVertDivCountString);
				} catch (NumberFormatException e) {
					JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("report.Exception.valueMustBeIntNumber"),
							LangModelReport.getString("report.Exception.error"),
							JOptionPane.OK_OPTION,
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				element.setVerticalDivisionsCount(newVertDivCount);
				element.setModified(System.currentTimeMillis());				
			}
		});
		
		//Установка шрифта таблицы
		JMenuItem setFontMenuItem = new JMenuItem();
		setFontMenuItem.setText(LangModelReport.getString("report.UI.FontChooserDialog.font"));
		setFontMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				TableDataStorableElement element =
					TableDataComponentMenu.this.tableDataElement;
				
				FontChooserDialog fcDialog = FontChooserDialog.getInstance(element.getFont());
				TableDataComponentMenu.this.setVisible(false);				
				fcDialog.setVisible(true);
				if (FontChooserDialog.selectedFont == null)
					return;

				element.setFont(FontChooserDialog.selectedFont);
				element.setModified(System.currentTimeMillis());				
			}
		});
		
		this.add(setVertDivisionsCountMenuItem);
		this.add(setFontMenuItem);		
	}
}

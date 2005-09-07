/*
 * $Id: TextComponentMenu.java,v 1.4 2005/09/07 08:43:25 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class TextComponentMenu extends JPopupMenu {
	private static final long serialVersionUID = 1882326318246146612L;
	
	protected AttachedTextComponent compElement = null;
	protected ApplicationContext applicationContext = null;

	public TextComponentMenu(
			AttachedTextComponent textComponent,
			ApplicationContext aContext) {
		this.compElement = textComponent;
		this.applicationContext = aContext;
		
		//Установка шрифта надписи
		JMenuItem setFontMenuItem = new JMenuItem();
		setFontMenuItem.setText(LangModelReport.getString("report.FontChooserDialog.font"));
		setFontMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();
				
				FontChooserDialog fcDialog = FontChooserDialog.getInstance(element.getFont());
				TextComponentMenu.this.setVisible(false);				
				fcDialog.setVisible(true);
				if (FontChooserDialog.selectedFont == null)
					return;

				component.setFont(FontChooserDialog.selectedFont);
			}
		});

		//Установка вертикальной привязки		
		JMenuItem setVertAttachMenuItem = new JMenuItem();
		setVertAttachMenuItem.setText(LangModelReport.getString("report.TextComponentMenu.vertAttach"));
		setVertAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				//Возможные варианты вертикальной привязки
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_FIELDS_TOP));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_TOP));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_BOTTOM));

				//Значение вертикальной привязки, установленное для этой надписи
				String oldValue = element.getVerticalAttachType();

				String newAttachmentType = null;
				newAttachmentType = (String) JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.TextComponentMenu.attachChooseMessage"),
					LangModelReport.getString("report.TextComponentMenu.attachChooseHeader"),
					JOptionPane.QUESTION_MESSAGE,
					null,
					selectItems.toArray(),
					LangModelReport.getString(oldValue));

				if (newAttachmentType == null)
					return;

				// Если мы выбрали привязку по полю
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_FIELDS_TOP)))
					newAttachmentType = TextAttachingType.TO_FIELDS_TOP;

				else if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_TOP)))
					newAttachmentType = TextAttachingType.TO_TOP;

				else if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_BOTTOM)))
					newAttachmentType = TextAttachingType.TO_BOTTOM;

				// ждём пока пользователь выберет объект
				TextComponentMenu.this.applicationContext.getDispatcher()
					.firePropertyChange(new AttachLabelEvent(
							this,
							component,
							newAttachmentType));
				
				TextComponentMenu.this.setVisible(false);				
			}
		});

		//Установка горизонтальной привязки		
		JMenuItem setHorizAttachMenuItem = new JMenuItem();
		setHorizAttachMenuItem.setText(LangModelReport.getString("report.TextComponentMenu.horizAttach"));
		setHorizAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				//Возможные варианты горизонтальной привязки
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_FIELDS_LEFT));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_LEFT));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_WIDTH_CENTER));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.TO_RIGHT));
	
				//Значение горизонтальной привязки, установленное для этой надписи
				String oldValue = element.getHorizontalAttachType();
	
				String newAttachmentType = null;
				newAttachmentType = (String) JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.TextComponentMenu.attachChooseMessage"),
					LangModelReport.getString("report.TextComponentMenu.attachChooseHeader"),
					JOptionPane.QUESTION_MESSAGE,
					null,
					selectItems.toArray(),
					LangModelReport.getString(oldValue));
	
				if (newAttachmentType == null)
					return;
	
				// Если мы выбрали привязку по полю
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_FIELDS_LEFT)))
					newAttachmentType = TextAttachingType.TO_FIELDS_LEFT;
	
				else if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_LEFT)))
					newAttachmentType = TextAttachingType.TO_LEFT;

				else if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_WIDTH_CENTER)))
					newAttachmentType = TextAttachingType.TO_WIDTH_CENTER;
				
				else if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_RIGHT)))
					newAttachmentType = TextAttachingType.TO_RIGHT;
	
				// ждём пока пользователь выберет объект
				TextComponentMenu.this.applicationContext.getDispatcher()
					.firePropertyChange(new AttachLabelEvent(
							this,
							component,
							newAttachmentType));
				
				TextComponentMenu.this.setVisible(false);				
			}
		});

		//Отмена привязок		
		JMenuItem deleteAttachMenuItem = new JMenuItem();
		deleteAttachMenuItem.setText(LangModelReport.getString("report.TextComponentMenu.removeAttach"));
		deleteAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				element.setAttachment(null, TextAttachingType.TO_FIELDS_LEFT);
				element.setAttachment(null, TextAttachingType.TO_FIELDS_TOP);
				element.setModified(System.currentTimeMillis());
				
				TextComponentMenu.this.setVisible(false);
			}
		});
		this.add(setFontMenuItem);
		this.add(setVertAttachMenuItem);
		this.add(setHorizAttachMenuItem);
		this.add(deleteAttachMenuItem);

	}
}

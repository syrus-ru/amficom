/*
 * $Id: TextComponentMenu.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.resource.I18N;
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
		setFontMenuItem.setText(I18N.getString("report.UI.FontChooserDialog.font"));
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
				element.setFont(FontChooserDialog.selectedFont);
	
				component.setSize(component.getTextSize());
				element.setSize(component.getWidth(),component.getHeight());
			}
		});

		//Установка вертикальной привязки		
		JMenuItem setVertAttachMenuItem = new JMenuItem();
		setVertAttachMenuItem.setText(I18N.getString("report.TextComponentMenu.vertAttach"));
		setVertAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				//Возможные варианты вертикальной привязки
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(I18N
						.getString(TextAttachingType.TO_FIELDS_TOP.stringValue()));
				selectItems.add(I18N
						.getString(TextAttachingType.TO_TOP.stringValue()));
				selectItems.add(I18N
						.getString(TextAttachingType.TO_BOTTOM.stringValue()));

				//Значение вертикальной привязки, установленное для этой надписи
				String oldValue = element.getVerticalAttachType().stringValue();

				TextAttachingType newAttachmentType = null;
				String newAttachmentTypeString = (String) JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					I18N.getString("report.TextComponentMenu.attachChooseMessage"),
					I18N.getString("report.TextComponentMenu.attachChooseHeader"),
					JOptionPane.QUESTION_MESSAGE,
					null,
					selectItems.toArray(),
					I18N.getString(oldValue));

				if (newAttachmentTypeString == null)
					return;

				// Если мы выбрали привязку по полю
				if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_FIELDS_TOP.stringValue())))
					newAttachmentType = TextAttachingType.TO_FIELDS_TOP;

				else if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_TOP.stringValue())))
					newAttachmentType = TextAttachingType.TO_TOP;

				else if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_BOTTOM.stringValue())))
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
		setHorizAttachMenuItem.setText(I18N.getString("report.TextComponentMenu.horizAttach"));
		setHorizAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				//Возможные варианты горизонтальной привязки
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(I18N
						.getString(TextAttachingType.TO_FIELDS_LEFT.stringValue()));
				selectItems.add(I18N
						.getString(TextAttachingType.TO_LEFT.stringValue()));
				selectItems.add(I18N
						.getString(TextAttachingType.TO_WIDTH_CENTER.stringValue()));
				selectItems.add(I18N
						.getString(TextAttachingType.TO_RIGHT.stringValue()));
	
				//Значение горизонтальной привязки, установленное для этой надписи
				String oldValue = element.getHorizontalAttachType().stringValue();
	
				TextAttachingType newAttachmentType = null;
				String newAttachmentTypeString = (String) JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					I18N.getString("report.TextComponentMenu.attachChooseMessage"),
					I18N.getString("report.TextComponentMenu.attachChooseHeader"),
					JOptionPane.QUESTION_MESSAGE,
					null,
					selectItems.toArray(),
					I18N.getString(oldValue));
	
				if (newAttachmentTypeString == null)
					return;
	
				// Если мы выбрали привязку по полю
				if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_FIELDS_LEFT.stringValue())))
					newAttachmentType = TextAttachingType.TO_FIELDS_LEFT;
	
				else if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_LEFT.stringValue())))
					newAttachmentType = TextAttachingType.TO_LEFT;

				else if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_WIDTH_CENTER.stringValue())))
					newAttachmentType = TextAttachingType.TO_WIDTH_CENTER;
				
				else if (newAttachmentTypeString.equals(I18N
						.getString(TextAttachingType.TO_RIGHT.stringValue())))
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
		deleteAttachMenuItem.setText(I18N.getString("report.TextComponentMenu.removeAttach"));
		deleteAttachMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				element.setAttachment(null, TextAttachingType.TO_FIELDS_LEFT);
				element.setAttachment(null, TextAttachingType.TO_FIELDS_TOP);
				
				TextComponentMenu.this.setVisible(false);
			}
		});
		this.add(setFontMenuItem);
		this.add(setVertAttachMenuItem);
		this.add(setHorizAttachMenuItem);
		this.add(deleteAttachMenuItem);

	}
}

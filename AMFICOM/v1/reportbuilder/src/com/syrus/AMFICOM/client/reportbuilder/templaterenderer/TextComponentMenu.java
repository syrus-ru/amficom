/*
 * $Id: TextComponentMenu.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
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
		JMenuItem mi1 = new JMenuItem();
		mi1.setText(LangModelReport.getString("report.FontChooserDialog.font"));
		mi1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();
				
				FontChooserDialog fcDialog = FontChooserDialog.getInstance(element.getFont());
				fcDialog.setVisible(true);
				if (FontChooserDialog.selectedFont == null)
					return;

				component.setFont(FontChooserDialog.selectedFont);
			}
		});

		//Установка вертикальной привязки		
		JMenuItem mi2 = new JMenuItem();
		mi2.setText(LangModelReport.getString("report.TextComponentMenu.vertAttach"));
		mi2.addActionListener(new java.awt.event.ActionListener() {
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

				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_TOP)))
					newAttachmentType = TextAttachingType.TO_TOP;

				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_BOTTOM)))
					newAttachmentType = TextAttachingType.TO_BOTTOM;

				// ждём пока пользователь выберет объект
				TextComponentMenu.this.applicationContext.getDispatcher()
					.firePropertyChange(new AttachLabelEvent(
							this,
							component,
							newAttachmentType));
				
				element.setModified(System.currentTimeMillis());
			}
		});

		//Установка горизонтальной привязки		
		JMenuItem mi3 = new JMenuItem();
		mi3.setText(LangModelReport.getString("report.TextComponentMenu.horizAttach"));
		mi3.addActionListener(new java.awt.event.ActionListener() {
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
	
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_LEFT)))
					newAttachmentType = TextAttachingType.TO_LEFT;
	
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.TO_RIGHT)))
					newAttachmentType = TextAttachingType.TO_RIGHT;
	
				// ждём пока пользователь выберет объект
				TextComponentMenu.this.applicationContext.getDispatcher()
					.firePropertyChange(new AttachLabelEvent(
							this,
							component,
							newAttachmentType));
				element.setModified(System.currentTimeMillis());
			}
		});

		//Отмена привязок		
		JMenuItem mi4 = new JMenuItem();
		mi4.setText(LangModelReport.getString("report.TextComponentMenu.removeAttach"));
		mi4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextComponent component = TextComponentMenu.this.compElement;
				AttachedTextStorableElement element =
					(AttachedTextStorableElement)component.getElement();

				element.setHorizontalAttachment(null, TextAttachingType.TO_FIELDS_LEFT);
				element.setVerticalAttachment(null, TextAttachingType.TO_FIELDS_TOP);
				element.setModified(System.currentTimeMillis());
			}
		});
		this.add(mi1);
		this.add(mi2);
		this.add(mi3);
		this.add(mi4);

	}
}

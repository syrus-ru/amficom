/*
 * $Id: TextComponentMenu.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.report.AttachedTextRenderingElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class TextComponentMenu extends JPopupMenu {
	private static final long serialVersionUID = 1882326318246146612L;
	
	private AttachedTextRenderingElement compElement = null;

	public TextComponentMenu(
			AttachedTextComponent textComponent)
	{
		this.compElement = (AttachedTextRenderingElement)textComponent.getElement();
		
		//��������� ������ �������
		JMenuItem mi1 = new JMenuItem();
		mi1.setText(LangModelReport.getString("report.TextComponentMenu.FontChooserDialog.font"));
		mi1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextRenderingElement element =
					TextComponentMenu.this.compElement;
				
				FontChooserDialog fcDialog = FontChooserDialog.getInstance(element.getFont());
				fcDialog.setVisible(true);
				if (FontChooserDialog.selectedFont == null)
					return;

				element.setFont(FontChooserDialog.selectedFont);
				element.setModified(System.currentTimeMillis());
			}
		});

		//��������� ������������ ��������		
		JMenuItem mi2 = new JMenuItem();
		mi2.setText(LangModelReport.getString("report.TextComponentMenu.FontChooserDialog.vertAttach"));
		mi2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextRenderingElement element =
					TextComponentMenu.this.compElement;

				//��������� �������� ������������ ��������
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toFieldsTop));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toTop));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toBottom));

				//�������� ������������ ��������, ������������� ��� ���� �������
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

				// ���� �� ������� �������� �� ����
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toFieldsTop)))
					newAttachmentType = TextAttachingType.toFieldsTop;

				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toTop)))
					newAttachmentType = TextAttachingType.toTop;

				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toBottom)))
					newAttachmentType = TextAttachingType.toBottom;

				if (newAttachmentType.equals(TextAttachingType.toFieldsTop)) {
					element.setVertAttachment(null, newAttachmentType);
					return;
				}

				// ����� ��� ���� ������������ ������� ������
				//TODO ���������� ��� ������� - ���������� �������� �
				//������� � ����� ��������� ��������
				element.setModified(System.currentTimeMillis());
			}
		});

		//��������� �������������� ��������		
		JMenuItem mi3 = new JMenuItem();
		mi3.setText(LangModelReport.getString("report.TextComponentMenu.FontChooserDialog.horizAttach"));
		mi3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextRenderingElement element =
				TextComponentMenu.this.compElement;

				//��������� �������� �������������� ��������
				List<String> selectItems = new ArrayList<String>();
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toFieldsLeft));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toLeft));
				selectItems.add(LangModelReport
						.getString(TextAttachingType.toRight));
	
				//�������� �������������� ��������, ������������� ��� ���� �������
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
	
				// ���� �� ������� �������� �� ����
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toFieldsLeft)))
					newAttachmentType = TextAttachingType.toFieldsLeft;
	
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toLeft)))
					newAttachmentType = TextAttachingType.toLeft;
	
				if (newAttachmentType.equals(LangModelReport
						.getString(TextAttachingType.toRight)))
					newAttachmentType = TextAttachingType.toRight;
	
				if (newAttachmentType.equals(TextAttachingType.toFieldsLeft)) {
					element.setVertAttachment(null, newAttachmentType);
					return;
				}
	
				// ����� ��� ���� ������������ ������� ������
				//TODO ���������� ��� ������� - ���������� �������� �
				//������� � ����� ��������� ��������
				element.setModified(System.currentTimeMillis());
			}
		});

		//������ ��������		
		JMenuItem mi4 = new JMenuItem();
		mi4.setText(LangModelReport.getString("report.TextComponentMenu.removeAttach"));
		mi4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent el) {
				AttachedTextRenderingElement element = TextComponentMenu.this.compElement;
				element.setHorizAttachment(null, TextAttachingType.toFieldsLeft);
				element.setVertAttachment(null, TextAttachingType.toFieldsTop);
				element.setModified(System.currentTimeMillis());
			}
		});
		this.add(mi1);
		this.add(mi2);
		this.add(mi3);
		this.add(mi4);

	}
}

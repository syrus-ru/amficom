/*
 * $Id: TemplateParametersDialog.java,v 1.2 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.ReportTemplate.ORIENTATION;
import com.syrus.AMFICOM.resource.IntDimension;

public class TemplateParametersDialog extends JDialog {
	private static final String TITLE = LangModelReport.getString("report.UI.Menubar.menuTemplateParameters");	
	private static final String TEMPLATE_SIZE = LangModelReport.getString("report.UI.TemplateParametersDialog.templateSize");
	private static final String MARGIN_SIZE = LangModelReport.getString("report.UI.TemplateParametersDialog.marginSize");
	private static final String ORIENTATION_LABEL = LangModelReport.getString("report.UI.TemplateParametersDialog.orientation");
	private static final String PORTRAIT = LangModelReport.getString("report.UI.TemplateParametersDialog.portraitOrientation");
	private static final String LANDSCAPE = LangModelReport.getString("report.UI.TemplateParametersDialog.landscapeOrientation");	
	private static final String APPLY = LangModelReport.getString("report.UI.apply");
	private static final String CANCEL = LangModelReport.getString("report.UI.cancel");
	
	private static final String A0 = "A0";
	private static final String A1 = "A1";
	private static final String A2 = "A2";	
	private static final String A3 = "A3";
	private static final String A4 = "A4";
	
	private final JComboBox templateSizeComboBox = new JComboBox();
	private final JTextField marginSizeTextField = new JTextField();
	final JRadioButton portraitRadioButton = new JRadioButton(PORTRAIT);
	final JRadioButton landscapeRadioButton = new JRadioButton(LANDSCAPE);	
	
	private ReportTemplate reportTemplate = null;
	
	private static TemplateParametersDialog instance = null;
	
	public static TemplateParametersDialog getInstance(ReportTemplate template) {
		if (instance == null)
			instance = new TemplateParametersDialog();
		
		instance.setReportTemplate(template);
		return instance;
	}
	
	private TemplateParametersDialog() {
		jbInit();
		this.setModal(true);
	}
	
	private void jbInit() {
		this.setSize(350,200);
		this.setTitle(TITLE);		
		this.setLayout(new BorderLayout());
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		
		final JLabel templateSizeLabel = new JLabel(TEMPLATE_SIZE);	
		contentPanel.add(templateSizeLabel,		new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(10,10,10,10),0,0));
		contentPanel.add(this.templateSizeComboBox,	new GridBagConstraints(1,0,2,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,10),0,0));

		final JLabel orientationLabel = new JLabel(ORIENTATION_LABEL);		
		contentPanel.add(orientationLabel,			new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(10,10,10,10),0,0));

		ButtonGroup buttonGroup = new ButtonGroup();

		contentPanel.add(this.portraitRadioButton,		new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,10,10,10),0,0));
		buttonGroup.add(this.portraitRadioButton);
		
		contentPanel.add(this.landscapeRadioButton,		new GridBagConstraints(2,1,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,10,10,10),0,0));		
		buttonGroup.add(this.landscapeRadioButton);
		
		final JLabel marginSizeLabel = new JLabel(MARGIN_SIZE);		
		contentPanel.add(marginSizeLabel,		new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(10,10,10,10),0,0));
		contentPanel.add(this.marginSizeTextField,	new GridBagConstraints(1,2,2,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,10),0,0));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());		
		
		final JButton applyButton = new JButton(APPLY);
		applyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				TemplateParametersDialog.this.applyButtonPressed();
			}
		});
		final JButton cancelButton = new JButton(CANCEL);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				TemplateParametersDialog.this.cancelButtonPressed();
			}
		});
		buttonsPanel.add(applyButton,	new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,10,10,30),0,0));
		buttonsPanel.add(cancelButton,	new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,30,10,10),0,0));
		
		this.add(contentPanel,BorderLayout.CENTER);
		this.add(buttonsPanel,BorderLayout.SOUTH);
		
		this.setDefaultData();
	}
	
	private void setDefaultData() {
		this.templateSizeComboBox.addItem(A0);
		this.templateSizeComboBox.addItem(A1);
		this.templateSizeComboBox.addItem(A2);
		this.templateSizeComboBox.addItem(A3);
		this.templateSizeComboBox.addItem(A4);
		this.templateSizeComboBox.setSelectedItem(A4);
		
		this.marginSizeTextField.setText(Integer.toString(ReportTemplate.STANDART_MARGIN_SIZE));
		
		this.portraitRadioButton.setSelected(true);		
	}
	
	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
		this.templateSizeComboBox.setSelectedItem(reportTemplate.getSize());
		this.marginSizeTextField.setText(Integer.toString(reportTemplate.getMarginSize()));
		if (reportTemplate.getOrientation().equals(ORIENTATION.LANDSCAPE))
			this.landscapeRadioButton.setSelected(true);
		else
			this.portraitRadioButton.setSelected(true);			
	}
	
	protected void applyButtonPressed() {
		IntDimension newSize = null;
		Object sizeItemSelected =
			this.templateSizeComboBox.getSelectedItem();
		if (sizeItemSelected.equals(A0))
			newSize = ReportTemplate.A0;
		else if (sizeItemSelected.equals(A1))
			newSize = ReportTemplate.A1;
		else if (sizeItemSelected.equals(A2))
			newSize = ReportTemplate.A2;
		else if (sizeItemSelected.equals(A3))
			newSize = ReportTemplate.A3;
		else if (sizeItemSelected.equals(A4))
			newSize = ReportTemplate.A4;
		
		int marginSize;
		try {
			marginSize = Integer.parseInt(this.marginSizeTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.Exception.marginSizeMustBeNumber"),
					LangModelReport.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.reportTemplate.setSize(newSize);		
		this.reportTemplate.setMarginSize(marginSize);
		if (this.portraitRadioButton.isSelected())
			this.reportTemplate.setOrientation(ORIENTATION.PORTRAIT);
		else
			this.reportTemplate.setOrientation(ORIENTATION.LANDSCAPE);
		
		this.setVisible(false);
	}

	protected void cancelButtonPressed() {
		this.setVisible(false);
	}
	
	public static void main (String[] args) {
		TemplateParametersDialog dialog = new TemplateParametersDialog();
		dialog.setVisible(true);
	}
}

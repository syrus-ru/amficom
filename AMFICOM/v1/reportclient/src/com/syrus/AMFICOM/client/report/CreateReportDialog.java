/*
 * $Id: CreateReportDialog.java,v 1.4 2005/09/23 12:10:59 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * ������, ������������ ������� �������� ���
 * �������� �������� ������������ ����.
 *
 * @author $Author: peskovsky $
 * @version $Revision: 1.4 $, $Date: 2005/09/23 12:10:59 $
 * @module generalclient_v1
 */
public class CreateReportDialog extends JDialog {
	private String moduleName = "";
	private Map<Object,Object> reportData = null;
	protected ReportTemplate selectedTemplate = null;

	private ApplicationContext aContext = null;

	private JScrollPane templatesListScrollPane = new JScrollPane();
	protected WrapperedList<ReportTemplate> templatesList = null;
	
	protected JButton saveButton = new JButton();
	protected JButton printButton = new JButton();
	protected JButton viewButton = new JButton();
	protected JButton cancelButton = new JButton();
	
	protected JDialog openedViewDialog = null;

	public CreateReportDialog(
			ApplicationContext aContext,
			String moduleName,
			Map<Object,Object> reportData) throws ApplicationException {
		super(Environment.getActiveWindow(),"",true);
		
		this.aContext = aContext;
		this.moduleName = moduleName;
		this.reportData = reportData;

		jbInit();
		setListContents();
	}

	private void jbInit() {
		this.setSize(new Dimension(293, 255));
		this.setTitle(LangModelReport.getString(
				"report.UI.CreateReportDialog.title"));
		this.setResizable(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		Insets defaultMargins = new Insets(2, 5, 2, 5);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		this.viewButton.setText(LangModelReport.getString(
				"report.UI.CreateReportDialog.viewReport"));
		this.viewButton.setEnabled(false);
		buttonPanel.add(this.viewButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewButton_actionPerformed();
			}
		});

		this.cancelButton.setText(LangModelReport.getString("report.UI.cancel"));
		buttonPanel.add(this.cancelButton,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		this.saveButton.setText(LangModelReport.getString(
				"report.UI.InnerToolbar.saveReport"));
		this.saveButton.setEnabled(false);
		buttonPanel.add(this.saveButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSelectedTemplate();
			}
		});
		
		this.printButton.setText(LangModelReport.getString(
				"report.UI.InnerToolbar.printReport"));
		this.printButton.setEnabled(false);
		buttonPanel.add(this.printButton, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSelectedTemplate();
			}
		});

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.templatesList.addListSelectionListener(new ListSelectionListener()	{
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					CreateReportDialog.this.selectedTemplate =
						(ReportTemplate)CreateReportDialog.this.templatesList.getSelectedValue();
					CreateReportDialog.this.saveButton.setEnabled(true);
					CreateReportDialog.this.printButton.setEnabled(true);
					CreateReportDialog.this.viewButton.setEnabled(true);
				}
			}
		});
		this.templatesListScrollPane.getViewport().add(this.templatesList);
		mainPanel.add(this.templatesListScrollPane, BorderLayout.CENTER);
		
		this.getContentPane().add(mainPanel);
	}


	private void setListContents() throws ApplicationException {
		//TODO ���������� ���������� EntityCode
		StorableObjectCondition condition = new EquivalentCondition(
				ObjectEntities.ANALYSIS_CODE);
		Set<ReportTemplate> allReportTemplates =
			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		
		Set<ReportTemplate> reportTemplatesForModule = new HashSet<ReportTemplate>();
		for (ReportTemplate reportTemplate : allReportTemplates)
			if (reportTemplate.getDestinationModule().equals(this.moduleName))
				reportTemplatesForModule.add(reportTemplate);
		
		this.templatesList.addElements(reportTemplatesForModule);				
	}

	void viewButton_actionPerformed() {
		ReportRenderer reportRenderer = this.createReportRendererForSelectedTemplate();
		if (reportRenderer == null)
			return;
		
		this.openedViewDialog = new JDialog(Environment.getActiveWindow(),"",true);
		this.openedViewDialog.setResizable(true);
		this.openedViewDialog.getContentPane().setLayout(new BorderLayout());
		this.openedViewDialog.getContentPane().add(
				new JScrollPane (reportRenderer),
				BorderLayout.CENTER);

		JButton closeButton = new JButton (LangModelReport.getString("report.UI.close"));
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateReportDialog.this.openedViewDialog.dispose();
			}
		});

		JButton vSaveButton = new JButton (LangModelReport.getString(
				"report.UI.InnerToolbar.saveReport"));
		vSaveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateReportDialog.this.saveSelectedTemplate();
				CreateReportDialog.this.openedViewDialog.dispose();
			}
		});

		JButton vPrintButton = new JButton (LangModelReport.getString(
				"report.UI.InnerToolbar.printReport"));
		vPrintButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateReportDialog.this.printSelectedTemplate();
				CreateReportDialog.this.openedViewDialog.dispose();
			}
		});


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		buttonPanel.add(vSaveButton);
		buttonPanel.add(vPrintButton);
		buttonPanel.add(closeButton);
		this.openedViewDialog.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		IntDimension a4Size = ReportTemplate.A4;
		this.openedViewDialog.setSize(a4Size.getWidth(),a4Size.getHeight());

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.openedViewDialog.setLocation(
				(int)(dim.getWidth() - this.openedViewDialog.getWidth()) / 2,
				(int)(dim.getHeight() - this.openedViewDialog.getHeight()) / 2);

		this.openedViewDialog.setVisible(true);
	}

	void saveSelectedTemplate() {
		ReportRenderer reportRenderer = this.createReportRendererForSelectedTemplate();
		if (reportRenderer == null)
			return;
		
		HTMLReportEncoder encoder =	new HTMLReportEncoder(
			reportRenderer.getRenderingComponents(),
			this.selectedTemplate);
		try {
			encoder.encodeToHTML();
		} catch (IOException e) {
			Log.errorMessage("CreateReportDialog.saveSelectedTemplate | " + e.getMessage());
			Log.errorException(e);
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.Exception.errorSavingHTML"),
					LangModelReport.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void printSelectedTemplate() {
		ReportRenderer reportRenderer = this.createReportRendererForSelectedTemplate();
		if (reportRenderer == null)
			return;

		ReportPrinter.printReport(
				reportRenderer,
				this.selectedTemplate);
	}

	void exit()	{
		dispose();
	}
	
	private ReportRenderer createReportRendererForSelectedTemplate() {
		if (this.selectedTemplate == null)
			return null;

		ReportRenderer reportRenderer = null;
		try {
			reportRenderer = new ReportRenderer(this.aContext);
			reportRenderer.setReportTemplate(this.selectedTemplate);
			reportRenderer.setData(this.reportData);
		}
		catch (ReportException cre) {
			Log.errorMessage("CreateReportDialog.createReportRendererForSelectedTemplate | " 
					+ cre.getMessage());
			Log.errorException(cre);
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					LangModelReport.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		return reportRenderer;		
	}
}

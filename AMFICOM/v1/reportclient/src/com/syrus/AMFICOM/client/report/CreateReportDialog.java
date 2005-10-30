/*
 * $Id: CreateReportDialog.java,v 1.9 2005/10/30 14:48:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.SheetSize;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * Диалог, используемый другими модулями для
 * открытия шаблонов определённого типа.
 *
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/30 14:48:46 $
 * @module reportclient
 */
public class CreateReportDialog extends JDialog {
	private static final long serialVersionUID = -2916240754709895883L;

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
		this.setTitle(I18N.getString(
				"report.UI.CreateReportDialog.title"));
		this.setResizable(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		Insets defaultMargins = new Insets(2, 5, 2, 5);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		this.viewButton.setText(I18N.getString(
				"report.UI.CreateReportDialog.viewReport"));
		this.viewButton.setEnabled(false);
		buttonPanel.add(this.viewButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewButton_actionPerformed();
			}
		});

		this.cancelButton.setText(I18N.getString("report.UI.cancel"));
		buttonPanel.add(this.cancelButton,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		this.saveButton.setText(I18N.getString(
				"report.UI.InnerToolbar.saveReport"));
		this.saveButton.setEnabled(false);
		buttonPanel.add(this.saveButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, defaultMargins, 0, 0));
		this.saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSelectedTemplate();
			}
		});
		
		this.printButton.setText(I18N.getString(
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
		
		this.templatesList = new WrapperedList<ReportTemplate>(
				ReportTemplateWrapper.getInstance(),
				ReportTemplateWrapper.COLUMN_NAME,
				ReportTemplateWrapper.COLUMN_NAME);

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
		StorableObjectCondition condition = new EquivalentCondition(
				ObjectEntities.REPORTTEMPLATE_CODE);
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

		JButton closeButton = new JButton (I18N.getString("report.UI.close"));
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateReportDialog.this.openedViewDialog.dispose();
			}
		});

		JButton vSaveButton = new JButton (I18N.getString(
				"report.UI.InnerToolbar.saveReport"));
		vSaveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateReportDialog.this.saveSelectedTemplate();
				CreateReportDialog.this.openedViewDialog.dispose();
			}
		});

		JButton vPrintButton = new JButton (I18N.getString(
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

		IntDimension a4Size = SheetSize.A4.getSize();
		this.openedViewDialog.setSize(a4Size.getWidth() + 20,a4Size.getHeight() / 2);

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
			Log.errorMessage(e.getMessage());
			Log.errorMessage(e);
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					I18N.getString("report.Exception.errorSavingHTML"),
					I18N.getString("report.Exception.error"),
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
		catch (Exception cre) {
			Log.errorMessage(cre.getMessage());
			Log.errorMessage(cre);
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					I18N.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return reportRenderer;		
	}
}

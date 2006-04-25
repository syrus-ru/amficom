/*
 * $Id: CreateReportDialog.java,v 1.13 2006/04/25 11:02:27 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.SheetSize;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;
import com.syrus.util.PrintUtilities;

/**
 * Диалог, используемый другими модулями для
 * открытия шаблонов определённого типа.
 *
 * @author $Author: stas $
 * @version $Revision: 1.13 $, $Date: 2006/04/25 11:02:27 $
 * @module reportclient
 */
public class CreateReportDialog {
	private ApplicationContext aContext;
	
	private String moduleName = "";
	private Map<Object,Object> reportData = null;

	protected WrapperedList<ReportTemplate> templatesList = null;
	protected String okButton = I18N.getString(ResourceKeys.I18N_CHOOSE);
	protected String cancelButton = I18N.getString(ResourceKeys.I18N_CANCEL);

	public CreateReportDialog(
			ApplicationContext aContext,
			String moduleName,
			Map<Object,Object> reportData) {
		
		this.aContext = aContext;
		this.moduleName = moduleName;
		this.reportData = reportData;

		initUI();
	}

	private void initUI() {
		if (this.templatesList == null) {
			this.templatesList = new WrapperedList<ReportTemplate>(
					ReportTemplateWrapper.getInstance(),
					StorableObjectWrapper.COLUMN_NAME,
					StorableObjectWrapper.COLUMN_ID);
		}
		
		try {
			initTemplatesList();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		
		int result = JOptionPane.showOptionDialog(AbstractMainFrame.getActiveMainFrame(),
				new JScrollPane(this.templatesList),
				I18N.getString("report.UI.CreateReportDialog.title"),
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				new Object[] {this.okButton, this.cancelButton},
				this.okButton); 
		if (result == JOptionPane.OK_OPTION) {
			ReportTemplate selectedTemplate = (ReportTemplate)this.templatesList.getSelectedValue();
			if (selectedTemplate != null) {
				showReportTemplate(selectedTemplate);
			}
		}
	}

	private void initTemplatesList() throws ApplicationException {
		StorableObjectCondition condition = new EquivalentCondition(
				ObjectEntities.REPORTTEMPLATE_CODE);
		Set<ReportTemplate> allReportTemplates =
			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		
		Set<ReportTemplate> reportTemplatesForModule = new HashSet<ReportTemplate>();
		for (ReportTemplate reportTemplate : allReportTemplates)
			if (reportTemplate.getDestinationModule().equals(this.moduleName))
				reportTemplatesForModule.add(reportTemplate);
		
		this.templatesList.removeAll();
		this.templatesList.addElements(reportTemplatesForModule);				
	}

	void showReportTemplate(final ReportTemplate reportTemplate) {
		try {
			final ReportRenderer reportRenderer = createReportRenderer(reportTemplate);
			
			final JDialog openedViewDialog = new JDialog(AbstractMainFrame.getActiveMainFrame(), "", true);
			openedViewDialog.setResizable(true);
			
			final JScrollPane scrollPane = new JScrollPane (reportRenderer);
			openedViewDialog.add(scrollPane, BorderLayout.CENTER);

			JButton closeButton = new JButton (I18N.getString("report.UI.close"));
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openedViewDialog.dispose();
				}
			});

			JButton vSaveButton = new JButton (I18N.getString("report.UI.InnerToolbar.saveReport"));
			vSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveTemplate(reportTemplate, reportRenderer);
				}
			});
			
			JButton vPrintButton = new JButton (I18N.getString("report.UI.InnerToolbar.printReport"));
			vPrintButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scrollPane.getViewport().setPreferredSize(reportRenderer.getPreferredSize());
//					openedViewDialog.setVisible(false);
					printTemplate(reportRenderer, reportRenderer);
					openedViewDialog.dispose();
				}
			});

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1,3));
			buttonPanel.add(vSaveButton);
			buttonPanel.add(vPrintButton);
			buttonPanel.add(closeButton);
			
			openedViewDialog.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

			IntDimension a4Size = SheetSize.A4.getSize();
			openedViewDialog.setSize(a4Size.getWidth() + 20,a4Size.getHeight() / 2);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			openedViewDialog.setLocation(
					(int)(dim.getWidth() - openedViewDialog.getWidth()) / 2,
					(int)(dim.getHeight() - openedViewDialog.getHeight()) / 2);

			openedViewDialog.setVisible(true);
			
		} catch (Exception cre) {
			Log.errorMessage(cre);
			JOptionPane.showMessageDialog(
					AbstractMainFrame.getActiveMainFrame(),
					cre.getMessage(),
					I18N.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void saveTemplate(ReportTemplate reportTemplate, ReportRenderer reportRenderer) {
		HTMLReportEncoder encoder =	new HTMLReportEncoder(
			reportRenderer.getRenderingComponents(),
			reportTemplate);
		try {
			encoder.encodeToHTML();
		} catch (IOException e) {
			Log.errorMessage(e.getMessage());
			Log.errorMessage(e);
			JOptionPane.showMessageDialog(
					AbstractMainFrame.getActiveMainFrame(),
					I18N.getString("report.Exception.errorSavingHTML"),
					I18N.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void printTemplate(JComponent componentToPrint, ReportRenderer reportRenderer) {
		reportRenderer.setPrintable(true);
		PrintUtilities.printComponent(componentToPrint, 1 / ReportTemplate.SCALE_FACTOR);
		reportRenderer.setPrintable(false);		
	}
	
	ReportRenderer createReportRenderer(ReportTemplate reportTemplate) throws CreateReportException, CreateModelException, ElementsIntersectException, ApplicationException, IOException {
		ReportRenderer reportRenderer = new ReportRenderer(this.aContext);
		reportRenderer.setReportTemplate(reportTemplate);
		reportRenderer.setData(this.reportData);
		return reportRenderer;
	}
}

/*
 * $Id: SelectTypeTemplateWindow.java,v 1.6 2004/09/27 08:22:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Диалог, используемый другими модулями для
 * открытия шаблонов определённого типа.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/09/27 08:22:56 $
 * @module generalclient_v1
 */
public class SelectTypeTemplateWindow extends JDialog
{
	public ReportTemplateImplementationPanel rtiPanel = null;

	private String templateType = "";
	private AMTReport reportData = null;

	private ReportTemplate selectedTemplate = null;
	private JDialog rtiWindow = null;

	private ApplicationContext aContext = null;

	private JScrollPane templatesListScrollPane = new JScrollPane();
	private ObjectResourceListBox templatesList = new ObjectResourceListBox();
	private JButton saveButton = new JButton();
	private JButton printButton = new JButton();
	private JButton viewButton = new JButton();
	private JButton cancelButton = new JButton();

	private SelectTypeTemplateWindow() throws HeadlessException
	{
		super(Environment.getActiveWindow());
		try
		{
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public SelectTypeTemplateWindow(
			ApplicationContext aC,
			String templateType,
			AMTReport reportData)
	{
		this();

		this.aContext = aC;
		this.templateType = templateType;
		this.reportData = reportData;

		setListContents();
	}

	private void jbInit() throws Exception
	{
		this.setSize(new Dimension(293, 255));
		this.setTitle(LangModelReport.getString("label_chooseReport"));
		this.setResizable(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});
		printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printButton_actionPerformed(e);
			}
		});
		viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewButton_actionPerformed(e);
			}
		});
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});

		templatesList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting())
				{
					selectedTemplate = (ReportTemplate)templatesList.getSelectedObjectResource();
					saveButton.setEnabled(true);
					printButton.setEnabled(true);
					viewButton.setEnabled(true);
				}
			}
		});

		saveButton.setMargin(new Insets(2, 2, 2, 2));
		printButton.setMargin(new Insets(2, 2, 2, 2));
		viewButton.setMargin(new Insets(2, 2, 2, 2));
		mainPanel.add(templatesListScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		viewButton.setText(LangModelReport.getString("label_viewReport"));
		viewButton.setEnabled(false);
		buttonPanel.add(viewButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));

		cancelButton.setText(LangModelReport.getString("label_cancel"));
		buttonPanel.add(cancelButton,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));


		saveButton.setText(LangModelReport.getString("label_saveReport"));
		saveButton.setEnabled(false);
		buttonPanel.add(saveButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));

		printButton.setText(LangModelReport.getString("label_print"));
		printButton.setEnabled(false);
		buttonPanel.add(printButton, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		templatesListScrollPane.getViewport().add(templatesList);

		this.getContentPane().add(mainPanel);
	}


	private void setListContents()
	{
		Pool.removeMap(ReportTemplate.typ);
		new ReportDataSourceImage(aContext.getDataSource()).LoadReportTemplates();
		Map map = Pool.getMap(ReportTemplate.typ);
		if (map != null)
			for (Iterator iterator = map.values().iterator(); iterator.hasNext();)
			{
				ReportTemplate curRT = (ReportTemplate) iterator.next();
				if (curRT.templateType.equals(this.templateType))
					this.templatesList.add(curRT);
			}
	}

	void viewButton_actionPerformed(ActionEvent e)
	{
		if (rtiWindow != null)
			rtiWindow.dispose();
		try
		{
			RenderingObject obj = (RenderingObject)selectedTemplate.objectRenderers.get(0);
			obj.getReportToRender().model.setData(selectedTemplate,reportData);

			rtiPanel = new ReportTemplateImplementationPanel(aContext,selectedTemplate,true);

			dispose();

			rtiWindow = new JDialog(Environment.getActiveWindow());
			rtiWindow.setModal(true);
			rtiWindow.setResizable(true);
			rtiWindow.getContentPane().setLayout(new BorderLayout());
			JScrollPane scrPane = new JScrollPane (rtiPanel);
			rtiWindow.getContentPane().add(scrPane,BorderLayout.CENTER);

			JButton closeButton = new JButton (LangModelReport.getString("label_close"));
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					rtiWindow.dispose();
				}
			});

			JButton vSaveButton = new JButton (LangModelReport.getString("label_saveReport"));
			vSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if (rtiPanel.saveToHTML(null,false) != -7777)
						rtiWindow.dispose();
				}
			});

			JButton vPrintButton = new JButton (LangModelReport.getString("label_print"));
			vPrintButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					rtiPanel.printReport();
					rtiWindow.dispose();
				}
			});


			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1,3));
			buttonPanel.add(vSaveButton);
			buttonPanel.add(vPrintButton);
			buttonPanel.add(closeButton);
			rtiWindow.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

			rtiWindow.setSize(new Dimension (860,600));

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			rtiWindow.setLocation((int)(dim.getWidth() - rtiWindow.getWidth()) / 2,
														(int)(dim.getHeight() - rtiWindow.getHeight()) / 2);

			rtiWindow.setVisible(true);
		}
		catch (CreateReportException cre)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					LangModelReport.getString("label_error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	boolean saveButton_actionPerformed(ActionEvent e)
	{
		try
		{
			RenderingObject obj = (RenderingObject)selectedTemplate.objectRenderers.get(0);
			obj.getReportToRender().model.setData(selectedTemplate,reportData);

			rtiPanel = new ReportTemplateImplementationPanel(aContext,selectedTemplate,true);
			if (rtiPanel.saveToHTML(null,false) != -7777)
			{
				dispose();
				return true;
			}
			else
				return false;
		}
		catch (CreateReportException cre)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					LangModelReport.getString("label_error"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	void printButton_actionPerformed(ActionEvent e)
	{
		try
		{
			RenderingObject obj = (RenderingObject)selectedTemplate.objectRenderers.get(0);
			obj.getReportToRender().model.setData(selectedTemplate,reportData);

			rtiPanel = new ReportTemplateImplementationPanel(aContext,selectedTemplate,true);
			rtiPanel.printReport();

			dispose();
		}
		catch (CreateReportException cre)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					LangModelReport.getString("label_error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}
}

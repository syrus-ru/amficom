package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;

import com.syrus.AMFICOM.Client.General.Report.
	ReportTemplateImplementationPanel;

import com.syrus.AMFICOM.Client.General.Report.FirmedTextPane;
import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.Resource.ReportDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.ReportBuilder.SelectTemplate;

/**
 * <p>Description: Панель инструментов для работы с шаблонами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportToolBar
		extends JToolBar
		implements ApplicationModelListener
{
	ReportMDIMain mainWindow = null;

	private ApplicationModel aModel;
	private Dispatcher pDisp;

	JButton buttonOpenSession = new JButton();

	JButton newTemplateButton = new JButton();
	JButton openTemplateButton = new JButton();
	JButton saveTemplateButton = new JButton();

	JButton importTemplateButton = new JButton();
	JButton exportTemplateButton = new JButton();


	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ReportToolBar()
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ReportToolBar(ReportMDIMain mainWindow)
	{
		super();

		this.mainWindow = mainWindow;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		TemplateMainToolBar_this_actionAdapter actionAdapter =
				new TemplateMainToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		buttonOpenSession = new JButton();
		buttonOpenSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						getImage("images/open_session.gif").
																						getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		buttonOpenSession.setText("");
		buttonOpenSession.setMaximumSize(buttonSize);
		buttonOpenSession.setPreferredSize(buttonSize);
		buttonOpenSession.setToolTipText(LangModelSurvey.getString("NewSession"));
		buttonOpenSession.setName("menuSessionNew");
		buttonOpenSession.addActionListener(actionAdapter);
		importTemplateButton.addActionListener(new ReportToolBar_importTemplateButton_actionAdapter(this));
		exportTemplateButton.addActionListener(new ReportToolBar_exportTemplateButton_actionAdapter(this));
		add(buttonOpenSession);

		saveTemplateButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveTemplateButton_actionPerformed(e);
			}
		});
		openTemplateButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openTemplateButton_actionPerformed(e);
			}
		});
		newTemplateButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newTemplateButton_actionPerformed(e);
			}
		});

		newTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						getImage("images/new.gif").
																						getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		newTemplateButton.setToolTipText(LangModelReport.getString("label_newTemplate"));
		newTemplateButton.setMaximumSize(buttonSize);
		newTemplateButton.setPreferredSize(buttonSize);

		openTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						 getImage(
				"images/load_template.gif").getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		openTemplateButton.setToolTipText(LangModelReport.getString(
				"label_openTemplate"));
		openTemplateButton.setMaximumSize(buttonSize);
		openTemplateButton.setPreferredSize(buttonSize);

		saveTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						 getImage(
				"images/save_template.gif").getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		saveTemplateButton.setToolTipText(LangModelReport.getString(
				"label_saveTemplate"));
		saveTemplateButton.setMaximumSize(buttonSize);
		saveTemplateButton.setPreferredSize(buttonSize);

		importTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						 getImage(
				"images/openfile.gif").getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		importTemplateButton.setToolTipText(LangModelReport.getString(
				"label_importTemplate"));
		importTemplateButton.setMaximumSize(buttonSize);
		importTemplateButton.setPreferredSize(buttonSize);

		exportTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																						 getImage(
				"images/save.gif").getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));
		exportTemplateButton.setToolTipText(LangModelReport.getString(
				"label_exportTemplate"));
		exportTemplateButton.setMaximumSize(buttonSize);
		exportTemplateButton.setPreferredSize(buttonSize);


		addSeparator();
		add(newTemplateButton);
		add(openTemplateButton);
		add(saveTemplateButton);
		addSeparator();
		add(importTemplateButton);
		add(exportTemplateButton);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void setDisp(Dispatcher p)
	{
		pDisp = p;
	}

	public Dispatcher getDisp()
	{
		return pDisp;
	}

	public void modelChanged(String e[])
	{
		buttonOpenSession.setVisible(aModel.isVisible("menuSessionNew"));
		buttonOpenSession.setEnabled(aModel.isEnabled("menuSessionNew"));

	}

	public void this_actionPerformed(ActionEvent e)
	{
		if (aModel == null)
			return;
		AbstractButton jb = (AbstractButton) e.getSource();
		String s = jb.getName();

		Command command = aModel.getCommand(s);
		command = (Command) command.clone();
		command.execute();
	}

	public void setTemplateToolBarState(boolean newState)
	{
		newTemplateButton.setEnabled(newState);
		openTemplateButton.setEnabled(newState);
		saveTemplateButton.setEnabled(newState);
		importTemplateButton.setEnabled(newState);
		exportTemplateButton.setEnabled(newState);
	}


	private void saveTemplateButton_actionPerformed(ActionEvent e)
	{
		if (!mainWindow.layoutWOCPanel.checkTemplatesScheme())
		{
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("label_crossesExist"),
				LangModelReport.getString("label_error"),
				JOptionPane.ERROR_MESSAGE);

			return;
		}

		SelectTemplate selectTemplateDialog = new SelectTemplate(
				mainWindow,
				SelectTemplate.SAVE,
				mainWindow.layoutWOCPanel.reportTemplate);

    selectTemplateDialog.setSize(400,300);
		selectTemplateDialog.setLocation(
				mainWindow.getX() + mainWindow.getWidth() / 2 -
				selectTemplateDialog.getWidth() / 2,
				mainWindow.getY() + mainWindow.getHeight() / 2 -
				selectTemplateDialog.getHeight() / 2);

		selectTemplateDialog.setVisible(true);
	}

	private void openTemplateButton_actionPerformed(ActionEvent e)
	{
		if (mainWindow.layoutWOCPanel.reportTemplate.modified !=
				mainWindow.layoutWOCPanel.reportTemplate.curModified)
		{
			int resultValue = JOptionPane.showConfirmDialog(
					mainWindow,
					LangModelReport.getString("label_saveChanges"),
					LangModelReport.getString("label_confirm"),
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (resultValue == JOptionPane.CANCEL_OPTION)
				return;

			if (resultValue == JOptionPane.YES_OPTION)
				saveTemplateButton_actionPerformed(e);
		}

		SelectTemplate selectTemplateDialog = new SelectTemplate(
				mainWindow,
				SelectTemplate.OPEN,
				null);

    selectTemplateDialog.setSize(400,300);
		selectTemplateDialog.setLocation(
				mainWindow.getX() + mainWindow.getWidth() / 2 -
				selectTemplateDialog.getWidth() / 2,
				mainWindow.getY() + mainWindow.getHeight() / 2 -
				selectTemplateDialog.getHeight() / 2);

		selectTemplateDialog.setVisible(true);

		if (SelectTemplate.selectedTemplate == null)
			return;

		mainWindow.setTemplate(SelectTemplate.selectedTemplate);
	}

	private void newTemplateButton_actionPerformed(ActionEvent e)
	{
		if ( (mainWindow.layoutWOCPanel != null)
				&& (mainWindow.layoutWOCPanel.reportTemplate.modified !=
						mainWindow.layoutWOCPanel.reportTemplate.curModified))
		{
			int resultValue = JOptionPane.showConfirmDialog(
					mainWindow,
					LangModelReport.getString("label_saveChanges"),
					LangModelReport.getString("label_confirm"),
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (resultValue == JOptionPane.CANCEL_OPTION)
				return;

			if (resultValue == JOptionPane.YES_OPTION)
				saveTemplateButton_actionPerformed(e);
		}

		try
		{
			mainWindow.setTemplate(
				new ReportTemplate(mainWindow.aContext.
				getDataSourceInterface()));
		}
		catch (Exception exc)
		{
		}
	}

	void importTemplateButton_actionPerformed(ActionEvent e)
	{
		if (JOptionPane.showConfirmDialog(
			Environment.getActiveWindow(),
			LangModelReport.getString("label_confImportTemplates"),
			"",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;


		File dirToLoad = new File("resources//reporttemplates");

		int templNumber = 0;
		System.out.println("Importing report templates.");

		File[] repTemplFiles = dirToLoad.listFiles(new RTFileFilter());
		String[] reportIDs = new String[repTemplFiles.length];

		for (int i = 0; i < repTemplFiles.length; i++)
		{
			try
			{
				String curRTFileName = repTemplFiles[i].getPath();
				FileInputStream in = new FileInputStream(curRTFileName);
				ObjectInputStream ois = new ObjectInputStream (in);

				ReportTemplate curRT = (ReportTemplate) ois.readObject();
				curRT.id = mainWindow.aContext.getDataSourceInterface().
					GetUId(ReportTemplate.typ);

				ois.close();
				in.close();

				Pool.put(ReportTemplate.typ,curRT.id,curRT);

				reportIDs[templNumber] = curRT.id;
				templNumber++;
			}
			catch (Exception exc)
			{
				System.out.println("Error occured while importing report template " +
					"from file " + repTemplFiles[templNumber].getName());

				return;
			}
		}

		mainWindow.aContext.getDataSourceInterface().SaveReportTemplates(reportIDs);

		System.out.println("Import of " + Integer.toString(templNumber) +
			" report templates successully finished.");
	}

	void exportTemplateButton_actionPerformed(ActionEvent e)
	{
		File dirToSave = new File("resources//reporttemplates");
		dirToSave.mkdir();

		new ReportDataSourceImage(
				mainWindow.aContext.getDataSourceInterface()).LoadReportTemplates();

		Hashtable rtHash = Pool.getHash(ReportTemplate.typ);
		if (rtHash == null)
			return;

		int templNumber = 0;
		ReportTemplate curRT = null;
		System.out.println("Exporting report templates.");
		Enumeration rtEnum = rtHash.elements();
		while (rtEnum.hasMoreElements())
		{
			curRT = (ReportTemplate) rtEnum.nextElement();
			String curRTFileName = dirToSave + "//" + curRT.id + ".tpl";

			try
			{
				templNumber++;
				FileOutputStream out = new FileOutputStream(curRTFileName);
				ObjectOutputStream oos = new ObjectOutputStream (out);
				oos.writeObject(curRT);
				oos.close();
				out.close();

			}
			catch (Exception exc)
			{
				templNumber = -1;
				break;
			}
		}
		if (templNumber != -1)
			System.out.println("Export of " + Integer.toString(templNumber) +
				" report templates successully finished.");
		else
			System.out.println("Error occured while exporting report template, " +
				"id = " + curRT.id);
	}
}

class TemplateMainToolBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	ReportToolBar adaptee;

	TemplateMainToolBar_this_actionAdapter(ReportToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
//    System.out.println("TemplateToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}

class ReportToolBar_importTemplateButton_actionAdapter implements java.awt.event.ActionListener
{
	ReportToolBar adaptee;

	ReportToolBar_importTemplateButton_actionAdapter(ReportToolBar adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.importTemplateButton_actionPerformed(e);
	}
}

class ReportToolBar_exportTemplateButton_actionAdapter implements java.awt.event.ActionListener
{
	ReportToolBar adaptee;

	ReportToolBar_exportTemplateButton_actionAdapter(ReportToolBar adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.exportTemplateButton_actionPerformed(e);
	}
}

class RTFileFilter implements FileFilter
{
	public boolean accept(File theFile)
	{
		if (theFile.getName().endsWith(".tpl"))
			return true;

		return false;
	}
}

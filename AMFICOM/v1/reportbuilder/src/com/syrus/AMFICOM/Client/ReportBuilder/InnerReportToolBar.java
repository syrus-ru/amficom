package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.util.Vector;

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
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.SelectReportsPanel;

import com.syrus.AMFICOM.Client.General.Filter.SetRestrictionsWindow;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;

/**
 * <p>Description: Панель инструментов для работы со схемой
 * шаблона и его реализацией</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class InnerReportToolBar extends JToolBar implements
	ApplicationModelListener
{
	ReportMDIMain mainWindow = null;

	private ApplicationModel aModel;

	private Dispatcher pDisp;

	JToggleButton addLabelButton = new JToggleButton();

	JToggleButton addImageButton = new JToggleButton();

	JButton deleteObjectButton = new JButton();

	JButton changeViewButton = new JButton();

	JButton saveReportButton = new JButton();

	JButton printReportButton = new JButton();

	public final static int img_siz = 16;

	public final static int btn_siz = 24;

	public InnerReportToolBar()
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

	public InnerReportToolBar(ReportMDIMain mainWindow)
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
		TemplateInnerToolBar_this_actionAdapter actionAdapter =
			new TemplateInnerToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		addLabelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addLabelButton_actionPerformed(e);
			}
		});

		addImageButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addImageButton_actionPerformed(e);
			}
		});

		changeViewButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeViewButton_actionPerformed();
			}
		});
		deleteObjectButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteObjectButton_actionPerformed(e);
			}
		});

		saveReportButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveReportButton_actionPerformed(e);
			}
		});
		saveReportButton.setEnabled(false);
		printReportButton.setEnabled(false);
		printReportButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				printReportButton_actionPerformed(e);
			}
		});

		addLabelButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/addtext.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		addLabelButton.setToolTipText(LangModelReport.String("label_addLabel"));
		addLabelButton.setMaximumSize(buttonSize);
		addLabelButton.setPreferredSize(buttonSize);

		addImageButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/graph_report.gif").getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		addImageButton.setToolTipText(LangModelReport.String("label_addImage"));
		addImageButton.setMaximumSize(buttonSize);
		addImageButton.setPreferredSize(buttonSize);

		deleteObjectButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															  getImage(
			"images/delete.gif").getScaledInstance(16, 16,
																Image.SCALE_SMOOTH)));
		deleteObjectButton.setToolTipText(LangModelReport.String(
			"label_deleteObject"));
		deleteObjectButton.setMaximumSize(buttonSize);
		deleteObjectButton.setPreferredSize(buttonSize);

		changeViewButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															getImage(
			"images/view_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		changeViewButton.setToolTipText(LangModelReport.String("label_viewReport"));
		changeViewButton.setMaximumSize(buttonSize);
		changeViewButton.setPreferredSize(buttonSize);

		saveReportButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															getImage(
			"images/save_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		saveReportButton.setToolTipText(LangModelReport.String("label_saveReport"));
		saveReportButton.setMaximumSize(buttonSize);
		saveReportButton.setPreferredSize(buttonSize);

		printReportButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															 getImage(
			"images/print_report.gif").
															 getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		printReportButton.setToolTipText(LangModelReport.String("label_print"));
		printReportButton.setMaximumSize(buttonSize);
		printReportButton.setPreferredSize(buttonSize);

		add(addLabelButton);
		add(addImageButton);
		addSeparator();
		add(deleteObjectButton);
		addSeparator();
		add(saveReportButton);
		add(printReportButton);
		addSeparator();
		add(changeViewButton);

		addLabelButton.setMargin(new Insets(2, 2, 2, 2));
		deleteObjectButton.setMargin(new Insets(2, 2, 2, 2));
		changeViewButton.setMargin(new Insets(2, 2, 2, 2));
		saveReportButton.setMargin(new Insets(2, 2, 2, 2));
		printReportButton.setMargin(new Insets(2, 2, 2, 2));
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

	public void clearToggles()
	{
		this.addLabelButton.setSelected(false);
		this.addImageButton.setSelected(false);
	}

	public void setEditToolBarState(boolean newState)
	{
		addLabelButton.setEnabled(newState);
		addImageButton.setEnabled(newState);
		deleteObjectButton.setEnabled(newState);
		changeViewButton.setEnabled(newState);
	}

	private void addLabelButton_actionPerformed(ActionEvent e)
	{
		if (addLabelButton.isSelected())
		{
			mainWindow.layoutWOCPanel.itemToAdd = LangModelReport.String(
				"label_lb");
			addImageButton.setSelected(false);
		}
else
			mainWindow.layoutWOCPanel.itemToAdd = "";
	}

	private void addImageButton_actionPerformed(ActionEvent e)
	{
		if (addImageButton.isSelected())
		{
			mainWindow.layoutWOCPanel.itemToAdd = LangModelReport.String(
				"label_im");
			addLabelButton.setSelected(false);
		}
else
			mainWindow.layoutWOCPanel.itemToAdd = "";
	}

	public void changeViewButton_actionPerformed()
	{
		Vector objectRenderers = mainWindow.layoutWOCPanel.reportTemplate.
										 objectRenderers;
		Vector labels = mainWindow.layoutWOCPanel.reportTemplate.labels;

		if (mainWindow.isTemplateSchemeMode)
		{
			try
			{
				if (!mainWindow.layoutWOCPanel.checkTemplatesScheme())
				{
					JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelReport.String("label_crossesExist"),
						LangModelReport.String("label_error"),
						JOptionPane.ERROR_MESSAGE);

					return;
				}

				mainWindow.layoutWCPanel = new ReportTemplateImplementationPanel(
					mainWindow.aContext,
					mainWindow.layoutWOCPanel.reportTemplate,
					false,
					mainWindow.layoutWOCPanel.imagableRect);

				changeViewButton.setToolTipText(
					LangModelReport.String("label_viewTemplatesScheme"));
				mainWindow.layoutScrollPane.setTitle(LangModelReport.String(
					"label_reportForTemplate"));
				changeViewButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
					getImage(
					"images/open_templ_scheme.gif").getScaledInstance(16, 16,
					Image.SCALE_SMOOTH)));

				mainWindow.layoutScrollPane.getContentPane().removeAll();

				mainWindow.layoutScrollPane.getContentPane().add(mainWindow.
					innerToolBar,
					BorderLayout.NORTH);
				mainWindow.layoutScrollPane.getContentPane().add(new JScrollPane(
					mainWindow.layoutWCPanel), BorderLayout.CENTER);

				// В конструкторе была инициализирована ширина таблицы чтобы вместить
				// все столбцы, ширины которых здесь выставляются
				for (int i = 0; i < objectRenderers.size(); i++)
					((RenderingObject) objectRenderers.get(i)).setColumnWidths();

				// у надписей меняются owner теперь они на новой панели
				for (int i = 0; i < labels.size(); i++)
					mainWindow.layoutWOCPanel.remove((FirmedTextPane) labels.get(i));

				setEditToolBarState(false);
				saveReportButton.setEnabled(true);
				printReportButton.setEnabled(true);
				changeViewButton.setEnabled(true);
				mainWindow.selectReportsPanel.setEnabled(false);

				Dispatcher theDisp = mainWindow.aContext.getDispatcher();
				if (mainWindow.additionalPanel != null)
				{
					if (mainWindow.additionalPanel instanceof SetRestrictionsWindow)
					{
						SetRestrictionsWindow addPanel =
							(SetRestrictionsWindow) mainWindow.additionalPanel;
						theDisp.notify(
							new OperationEvent(addPanel.orfp.getFilter(), 0,
													 ObjectResourceFilterPane.
													 state_filterClosed));
						addPanel.closeSchemeWindow();
					}
					else
						theDisp.notify(
							new OperationEvent("", 0,
													 SelectReportsPanel.ev_closingAdditionalPanel));

					mainWindow.additionalPanel.dispose();
				}

				mainWindow.isTemplateSchemeMode = false;
			}
			catch (CreateReportException cre)
			{
				JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					cre.getMessage(),
					LangModelReport.String("label_error"),
					JOptionPane.ERROR_MESSAGE);

				mainWindow.layoutWOCPanel.repaint();
				return;
			}
		}
		else
		{
			// Сохраняем информацию о колонках таблиц
			for (int i = 0; i < objectRenderers.size(); i++)
				((RenderingObject) objectRenderers.get(i)).saveColumnWidths();

			changeViewButton.setToolTipText(LangModelReport.String(
				"label_viewReport"));
			changeViewButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
																getImage(
				"images/view_report.gif").
																getScaledInstance(16, 16,
				Image.SCALE_SMOOTH)));

			mainWindow.layoutScrollPane.setTitle(LangModelReport.String(
				"label_templateScheme"));
			mainWindow.layoutScrollPane.getContentPane().removeAll();
			mainWindow.layoutScrollPane.getContentPane().add(mainWindow.
				innerToolBar,
				BorderLayout.NORTH);
			mainWindow.layoutScrollPane.getContentPane().add(new JScrollPane(
				mainWindow.layoutWOCPanel), BorderLayout.CENTER);
			// Преобразовываем графические отображения объектов к элементам схемы
			mainWindow.layoutWOCPanel.setSchemeObjectsNewParameters();

			mainWindow.layoutWCPanel = null;
			// Это требуется в FirmedTextPane для правильного нахождения координат
			// привязанных надписей
			for (int i = 0; i < objectRenderers.size(); i++)
				((RenderingObject) objectRenderers.get(i)).rendererPanel = null;

			setEditToolBarState(true);
			saveReportButton.setEnabled(false);
			printReportButton.setEnabled(false);
			mainWindow.selectReportsPanel.setEnabled(true);

			mainWindow.isTemplateSchemeMode = true;
		}
	}

	private void deleteObjectButton_actionPerformed(ActionEvent e)
	{
		mainWindow.layoutWOCPanel.deleteselectedObject();
	}

	private void saveReportButton_actionPerformed(ActionEvent e)
	{
		mainWindow.layoutWCPanel.saveToHTML(null, false);
	}

	private void printReportButton_actionPerformed(ActionEvent e)
	{
		mainWindow.layoutWCPanel.printReport();
	}
}

class TemplateInnerToolBar_this_actionAdapter implements java.awt.event.
	ActionListener
{
	InnerReportToolBar adaptee;

	TemplateInnerToolBar_this_actionAdapter(InnerReportToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
//    System.out.println("TemplateToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}
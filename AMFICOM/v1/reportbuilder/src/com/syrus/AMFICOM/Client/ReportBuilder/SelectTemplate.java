package com.syrus.AMFICOM.Client.ReportBuilder;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ReportDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
//import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.syrus.AMFICOM.Client.ReportBuilder.ReportMDIMain;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.Report.*;

import java.awt.event.*;
import java.awt.*;
/**
 * <p>Description: Окно для работы с шаблонами (загрузка,
 * сохранение, удаление) при удалении и сохранении -
 * информация тут же сохраняется на сервер. После этого
 * информация загружается в окно с сервера заново.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */


public class SelectTemplate extends JDialog
{
/**
 * Для режима открытия шаблона - выбранный шаблон
 */
	public static ReportTemplate selectedTemplate = null;

/**
 * Режим сохранения шаблона
 */
	public static int SAVE = 0;

/**
 * Режим открытия шаблона
 */
	public static int OPEN = 1;

	private JPanel contentPanel = new JPanel();
	private JScrollPane templatesScrollPanel = new JScrollPane();

	private JButton openSaveButton = new JButton();
	private JButton cancelButton = new JButton();
	private JButton removeButton = new JButton();

	private ObjectResourceListBox templatesList = new ObjectResourceListBox();
	private AComboBox templateTypesComboBox = new AComboBox();

	private String[] comboData = null;

	private JTextField selectedTemplateNameField = new JTextField();

	private int mode = 0;
	private ReportMDIMain owner = null;
	private ReportTemplate templateToSave = null;

	/**
	 * <p> Конструктор с указанием режима работы</p>
	 * @param frame родительское окно;
	 * @param mode режим окна (открытие/сохранение)
	 * @param rt для режима сохранения - отчёт, подлежащий сохранению
	 */
	public SelectTemplate(
			ReportMDIMain frame,
			int mode,
			ReportTemplate rt)
	{
		super(frame,"",true);

		if (mode == SelectTemplate.OPEN)
			this.setTitle(LangModelReport.getString("label_openTemplate"));
		if (mode == SelectTemplate.SAVE)
			this.setTitle(LangModelReport.getString("label_saveTemplate"));

		this.owner = frame;
		this.mode = mode;
		this.templateToSave = rt;

		try
		{
			selectedTemplate = null;
			jbInit();

			setComboBoxData();
			setListData();

			pack();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public SelectTemplate()
	{
		this(null,0,null);
	}

	private void jbInit() throws Exception
	{
		templatesScrollPanel.setBorder(BorderFactory.createEtchedBorder());

		openSaveButton.setMargin(new Insets(2, 2, 2, 2));
		if (mode == OPEN)
			openSaveButton.setText(LangModelReport.getString("label_open"));
		if (mode == SAVE)
			openSaveButton.setText(LangModelReport.getString("label_save"));

		openSaveButton.setEnabled(false);

		openSaveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openSaveButton_actionPerformed(e);
			}
		});
		cancelButton.setText(LangModelReport.getString("label_cancel"));
		cancelButton.setMargin(new Insets(2, 2, 2, 2));

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		removeButton.setMargin(new Insets(2, 2, 2, 2));
		removeButton.setText(LangModelReport.getString("label_delete"));
		removeButton.setEnabled(false);

		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				removeButton_actionPerformed(e);
			}
		});

		selectedTemplateNameField.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				selectedTemplateNameField_keyPressed(e);
			}
		});
		templateTypesComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				templateTypesComboBox_actionPerformed(e);
			}
		});

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel,BorderLayout.CENTER);

		contentPanel.setLayout(new GridBagLayout());
		contentPanel.add(templateTypesComboBox,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		contentPanel.add(templatesScrollPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		templatesScrollPanel.getViewport().add(templatesList, null);

		contentPanel.add(selectedTemplateNameField,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		buttonPanel.add(openSaveButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		buttonPanel.add(removeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		buttonPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		contentPanel.add(buttonPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		templatesList.addListSelectionListener(new ListSelectionListener()
		{
			 public void valueChanged(ListSelectionEvent e)
			 {
				 templatesList_ItemChanged();
			 }
		});
	}

	private void setListData()
	{
		this.templatesList.removeAll();

		Pool.removeHash(ReportTemplate.typ);

		new ReportDataSourceImage(
				owner.aContext.getDataSourceInterface()).LoadReportTemplates();

		Hashtable rtHash = Pool.getHash(ReportTemplate.typ);
		if (rtHash == null)
			return;

		Enumeration rtEnum = rtHash.elements();
		while (rtEnum.hasMoreElements())
		{
			ReportTemplate curRT = (ReportTemplate) rtEnum.nextElement();
			String curType = comboData[templateTypesComboBox.getSelectedIndex()];
			if (curRT.templateType.equals(curType) || curType.equals(ReportTemplate.rtt_AllTemplates))
				this.templatesList.add(curRT);
		}
	}

	private void setComboBoxData()
	{
		comboData = new String[9];
		comboData[0] = ReportTemplate.rtt_AllTemplates;
		comboData[1] = ReportTemplate.rtt_Evaluation;
		comboData[2] = ReportTemplate.rtt_Analysis;
		comboData[3] = ReportTemplate.rtt_Prediction;
		comboData[4] = ReportTemplate.rtt_Modeling;
		comboData[5] = ReportTemplate.rtt_Survey;
		comboData[6] = ReportTemplate.rtt_Optimization;
		comboData[7] = ReportTemplate.rtt_Scheme;
		comboData[8] = ReportTemplate.rtt_Map;    

		for (int i = 0; i < comboData.length; i++)
			templateTypesComboBox.addItem(LangModelReport.getString(comboData[i]));
	}

	private void openSaveButton_actionPerformed(ActionEvent e)
	{
		if (mode == OPEN)
		{
			selectedTemplate = nameExists(selectedTemplateNameField.getText());

			if (selectedTemplate == null)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("error_noTemplate"),
						LangModelReport.getString("label_error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Это всё для правильного отображения на схеме
			ReportBuilder.loadRequiredObjects(owner.aContext,selectedTemplate);

			for (int j = 0; j < selectedTemplate.objectRenderers.size(); j++)
			{
				ObjectsReport report =
					((RenderingObject)selectedTemplate.objectRenderers.get(j)).getReportToRender();
				try
				{
					report.setReserve(report.getReserve());
				}
				catch (CreateReportException cre){}
			}
			//


			this.setVisible(false);
		}

		if (mode == SAVE)
		{
			if (selectedTemplateNameField.getText().equals(""))
				return;

			ReportTemplate rt1 = nameExists(selectedTemplateNameField.getText());
			if (rt1 != null)
			{
				int replace = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("label_templateExists"),
						LangModelReport.getString("label_confirm"),
						JOptionPane.YES_NO_OPTION);
				if (replace == JOptionPane.NO_OPTION)
					return;
				else
				{
					this.owner.aContext.getDataSourceInterface().RemoveReportTemplates(
							new String[] {rt1.id});
					Pool.remove(ReportTemplate.typ,rt1.id);
				}
			}

			templateToSave.name = selectedTemplateNameField.getText();

			//меняем ID
			DataSourceInterface dsi = this.owner.aContext.getDataSourceInterface();
			templateToSave.id = dsi.GetUId(ReportTemplate.typ);
			for (int i = 0; i < templateToSave.objectRenderers.size(); i++)
			{
				RenderingObject curRO = (RenderingObject) templateToSave.objectRenderers.get(i);
				curRO.id = this.owner.aContext.getDataSourceInterface().GetUId("reporttemplatefield");
			}

			templateToSave.templateType = comboData[templateTypesComboBox.getSelectedIndex()];

			Pool.put(ReportTemplate.typ,
							 templateToSave.id,
							 templateToSave);

			dsi.SaveReportTemplates(new String[] {templateToSave.id});

			this.setVisible(false);
		}
	}

	private void cancelButton_actionPerformed(ActionEvent e)
	{
		selectedTemplate = null;
		this.setVisible(false);
	}

	private void removeButton_actionPerformed(ActionEvent e)
	{
		int[] selIndices = this.templatesList.getSelectedIndices();
		Vector selTemplates = new Vector();

		for (int i = 0; i < selIndices.length; i++)
			selTemplates.add((ReportTemplate)this.templatesList.
				getModel().getElementAt(selIndices[i]));


		for (int i = 0; i < selTemplates.size(); i++)
		{
			ReportTemplate rt = (ReportTemplate) selTemplates.get(i);
			if (rt != null)
			{
				this.owner.aContext.getDataSourceInterface().RemoveReportTemplates(
					new String[]{rt.id});

				Pool.remove(ReportTemplate.typ, rt.id);
				this.templatesList.remove(rt);
			}
		}
	}

	private void templatesList_ItemChanged()
	{
		if (this.templatesList.getSelectedIndex() != -1)

		{
			selectedTemplateNameField.setText(
					((ReportTemplate)this.templatesList.getSelectedObjectResource()).name);

			openSaveButton.setEnabled(
					!((this.mode == SelectTemplate.SAVE) &&
					(templateTypesComboBox.getSelectedIndex() == 0)));

			removeButton.setEnabled(true);
		}
		else
		{
			removeButton.setEnabled(false);
			openSaveButton.setEnabled(false);
		}
	}

	private ReportTemplate nameExists (String name)
	{
		for (int i = 0; i < templatesList.getModel().getSize(); i++)
		{
			ReportTemplate rt =
								 (ReportTemplate) templatesList.getModel().getElementAt(i);
			if (rt.name.equals(name))
				return rt;
		}

		return null;
	}

	private void selectedTemplateNameField_keyPressed(KeyEvent e)
	{
		int curLength = selectedTemplateNameField.getText().length();

		if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE) ||
				(e.getKeyCode() == KeyEvent.VK_DELETE))
			curLength--;
		else
			if (!(e.isActionKey() ||
					 (e.getKeyCode() == KeyEvent.VK_ALT) ||
					 (e.getKeyCode() == KeyEvent.VK_CONTROL) ||
					 (e.getKeyCode() == KeyEvent.VK_SHIFT)))
				curLength++;

		if (curLength > 0)
			openSaveButton.setEnabled(true);
		else
			openSaveButton.setEnabled(false);

		if ((this.mode == SelectTemplate.SAVE) &&
				(templateTypesComboBox.getSelectedIndex() == 0))
			openSaveButton.setEnabled(false);
	}

	void templateTypesComboBox_actionPerformed(ActionEvent e)
	{
		setListData();
		selectedTemplateNameField.setText("");
	}
}

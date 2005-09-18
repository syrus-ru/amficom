package com.syrus.AMFICOM.client.reportbuilder;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportTemplateWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.ReportTemplate;

import java.util.HashSet;
import java.util.Set;

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

public class TemplateOpenSaveDialog extends JDialog {
	public static final String ALL_TEMPLATES = "report.Modules.allTemplates";
	/**
	 * Список всех модулей
	 */
	public static final String[] MODULES_ARRAY = new String[] {
		DestinationModules.ANALYSIS,
		DestinationModules.EVALUATION,
		DestinationModules.MAP,
		DestinationModules.MODELING,
		DestinationModules.OBSERVE,
		DestinationModules.OPTIMIZATION,
		DestinationModules.PREDICTION,
		DestinationModules.SCHEDULER,
		DestinationModules.SCHEME,
		DestinationModules.SURVEY,
		DestinationModules.COMBINED,		
		ALL_TEMPLATES};		
	
	/**
	 * Режим сохранения шаблона
	 */
	public static final int SAVE = 0;
	/**
	 * Режим открытия шаблона
	 */
	public static final int OPEN = 1;
	/**
	 * Список всех шаблонов отчётов
	 */
	private Set<ReportTemplate> allReportTemplates = null;
	
	private JPanel contentPanel = new JPanel();
	private JScrollPane templatesScrollPanel = new JScrollPane();

	private JButton openSaveButton = new JButton();
	private JButton cancelButton = new JButton();
	private JButton removeButton = new JButton();

	private WrapperedList<ReportTemplate> templatesList = null;
	private AComboBox moduleNamesComboBox = new AComboBox();

	private JTextField selectedTemplateNameField = new JTextField();

	private int mode = 0;
	private ReportTemplate templateProcessed = null;

	public static ReportTemplate openTemplate() {
		TemplateOpenSaveDialog dialog = new TemplateOpenSaveDialog(TemplateOpenSaveDialog.OPEN);
		dialog.setVisible(true);
		
		return dialog.getTemplateProcessed();
	}

	public static void saveTemplate(ReportTemplate template) {
		TemplateOpenSaveDialog dialog = new TemplateOpenSaveDialog(TemplateOpenSaveDialog.SAVE);
		dialog.setTemplateProcessed(template);
		dialog.setVisible(true);
	}
	
	private TemplateOpenSaveDialog(int mode) {
		super(Environment.getActiveWindow(),"",true);

		this.mode = mode;
		try	{
			this.jbInit();
			
			this.getReportTemplatesFromPool();

			this.setComboBoxData();
			this.setListData();

			this.pack();
		}
		catch(Exception ex)	{
			//Should be empty
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception{
		this.templatesList = new WrapperedList<ReportTemplate>(
				ReportTemplateWrapper.getInstance(),
				ReportTemplateWrapper.COLUMN_NAME,
				ReportTemplateWrapper.COLUMN_NAME);
		
		if (this.mode == TemplateOpenSaveDialog.OPEN)
			this.setTitle(LangModelReport.getString("report.UI.TemplateSaveOpenDialog.openTemplate"));
		if (this.mode == TemplateOpenSaveDialog.SAVE)
			this.setTitle(LangModelReport.getString("report.UI.TemplateSaveOpenDialog.saveTemplate"));
		
		this.templatesScrollPanel.setBorder(BorderFactory.createEtchedBorder());

		this.openSaveButton.setMargin(new Insets(2, 2, 2, 2));
		if (this.mode == OPEN)
			this.openSaveButton.setText(LangModelReport.getString("report.UI.open"));
		if (this.mode == SAVE)
			this.openSaveButton.setText(LangModelReport.getString("report.UI.save"));
		this.openSaveButton.setEnabled(false);
		this.openSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				openSaveButton_actionPerformed(e);
			}
		});
		
		this.cancelButton.setText(LangModelReport.getString("report.UI.cancel"));
		this.cancelButton.setMargin(new Insets(2, 2, 2, 2));

		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		
		this.removeButton.setMargin(new Insets(2, 2, 2, 2));
		this.removeButton.setText(LangModelReport.getString("report.UI.remove"));
		this.removeButton.setEnabled(false);

		this.removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButton_actionPerformed(e);
			}
		});

		this.selectedTemplateNameField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				selectedTemplateNameField_keyPressed(e);
			}
		});
		
		this.moduleNamesComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				templateTypesComboBox_actionPerformed(e);
			}
		});

		this.templatesList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				templatesList_ItemChanged();
			}
		});

		this.getContentPane().setLayout(new BorderLayout());

		this.contentPanel.setLayout(new GridBagLayout());
		this.contentPanel.add(this.moduleNamesComboBox,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.templatesScrollPanel.getViewport().add(this.templatesList, null);
		this.contentPanel.add(this.templatesScrollPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.contentPanel.add(this.selectedTemplateNameField,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		buttonPanel.add(this.openSaveButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		buttonPanel.add(this.removeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		buttonPanel.add(this.cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.contentPanel.add(buttonPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.getContentPane().add(this.contentPanel,BorderLayout.CENTER);
		
		this.setSize(300,500);
		this.setPreferredSize(this.getSize());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(
				(int)(screenSize.getWidth() - this.getWidth()) / 2,
				(int)(screenSize.getHeight() - this.getHeight()) / 2);
	}

	private void setListData() {
		this.templatesList.removeAll();

		if (this.allReportTemplates == null)
			return;

		String moduleSelected = MODULES_ARRAY[this.moduleNamesComboBox.getSelectedIndex()];
		Set<ReportTemplate> reportTemplatesForModule = new HashSet<ReportTemplate>();
		for (ReportTemplate reportTemplate : this.allReportTemplates) {
			if (	reportTemplate.getDestinationModule().equals(moduleSelected)
				||	moduleSelected.equals(ALL_TEMPLATES))
				reportTemplatesForModule.add(reportTemplate);
		}
		this.templatesList.addElements(reportTemplatesForModule);
	}

	private void getReportTemplatesFromPool() throws ApplicationException {
		//TODO Подставить правильный EntityCode
		StorableObjectCondition condition = new EquivalentCondition(
				ObjectEntities.ANALYSIS_CODE);
		this.allReportTemplates = StorableObjectPool.getStorableObjectsByCondition(condition, true);
	}
	
	private void setComboBoxData() {
		for (int i = 0; i < MODULES_ARRAY.length; i++)
			this.moduleNamesComboBox.addItem(
					LangModelReport.getString(MODULES_ARRAY[i]));
		if (this.mode == OPEN)
			this.moduleNamesComboBox.setSelectedItem(
					LangModelReport.getString(ALL_TEMPLATES));
		else
			this.moduleNamesComboBox.setSelectedItem(
					LangModelReport.getString(DestinationModules.COMBINED));
	}

	protected void openSaveButton_actionPerformed(ActionEvent e) {
		ReportTemplate selectedTemplate =
			this.templateForName(this.selectedTemplateNameField.getText());
		
		if (this.mode == OPEN) {
			if (selectedTemplate == null) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("report.Exception.noTemplateForName"),
						LangModelReport.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.templateProcessed = selectedTemplate;
			this.setVisible(false);
		}
		else if (this.mode == SAVE) {
			if (this.selectedTemplateNameField.getText().equals(""))
				return;
			if (selectedTemplate != null) {
				int replace = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString(
								"report.Command.SaveTemplate.templateExists"),
						LangModelReport.getString("report.File.confirm"),
						JOptionPane.YES_NO_OPTION);
				if (replace == JOptionPane.NO_OPTION)
					return;

				//TODO Здесь должно быть удаление шаблона с указанным именем
				StorableObjectPool.delete(selectedTemplate.getId());
			}
			
			this.templateProcessed.setName(this.selectedTemplateNameField.getText());
			this.templateProcessed.setDestinationModule(
					MODULES_ARRAY[this.moduleNamesComboBox.getSelectedIndex()]);

			//TODO Здесь должно быть сохранение шаблона с указанным именем
			try {
				StorableObjectPool.putStorableObject(selectedTemplate);
			} catch (IllegalObjectEntityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			selectedTemplate.refreshModified();
			this.setVisible(false);
		}
	}

	protected void cancelButton_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	protected void removeButton_actionPerformed(ActionEvent e) {
		int[] selectedIndices = this.templatesList.getSelectedIndices();
		Set<ReportTemplate> selectedTemplates = new HashSet<ReportTemplate>();

		for (int i = 0; i < selectedIndices.length; i++)
			selectedTemplates.add((ReportTemplate)this.templatesList.
				getModel().getElementAt(selectedIndices[i]));

		//TODO Здесь должно быть удаление списка шаблонов
		StorableObjectPool.delete(selectedTemplates);
	}

	protected void templatesList_ItemChanged() {
		if (this.templatesList.getSelectedIndex() != -1) {
			this.selectedTemplateNameField.setText(
					((ReportTemplate)this.templatesList.getSelectedValue()).getName());

			if (!	(	(this.mode == TemplateOpenSaveDialog.SAVE)
					&&	(MODULES_ARRAY[this.moduleNamesComboBox.getSelectedIndex()].equals(ALL_TEMPLATES))))
				this.openSaveButton.setEnabled(true);

			this.removeButton.setEnabled(true);
		}
		else {
			this.removeButton.setEnabled(false);
			this.openSaveButton.setEnabled(false);
		}
	}

	private ReportTemplate templateForName (String name) {
		String moduleSelected = MODULES_ARRAY[this.moduleNamesComboBox.getSelectedIndex()];
		for (ReportTemplate reportTemplate : this.allReportTemplates) {
			if (	reportTemplate.getName().equals(name)
				&&	reportTemplate.getDestinationModule().equals(moduleSelected))
				return reportTemplate;
		}

		return null;
	}

	protected void selectedTemplateNameField_keyPressed(KeyEvent e) {
		String textToProcess = this.selectedTemplateNameField.getText();
		
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.CHAR_UNDEFINED)
			textToProcess += keyChar;

		int textLength = textToProcess.length();
		
		this.openSaveButton.setEnabled(textLength > 0);
		if ((	(this.mode == TemplateOpenSaveDialog.SAVE)
			&&	(MODULES_ARRAY[this.moduleNamesComboBox.getSelectedIndex()].equals(ALL_TEMPLATES))))
			this.openSaveButton.setEnabled(false);
	}

	protected void templateTypesComboBox_actionPerformed(ActionEvent e) {
		this.setListData();
		this.selectedTemplateNameField.setText("");
	}

	public ReportTemplate getTemplateProcessed() {
		return this.templateProcessed;
	}

	public void setTemplateProcessed(ReportTemplate templateProcessed) {
		this.templateProcessed = templateProcessed;
	}
}

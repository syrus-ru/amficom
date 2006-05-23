package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.ReportTemplateWrapper;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateRenderer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;
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
	protected int mode = 0;
	
	private Set<ReportTemplate> allReportTemplates = null;
	
	private JPanel contentPanel = new JPanel();
	private JScrollPane templatesScrollPanel = new JScrollPane();

	private JButton openSaveButton = new JButton();
	private JButton cancelButton = new JButton();
	private JButton removeButton = new JButton();

	private WrapperedList<ReportTemplate> templatesList = null;
	private AComboBox moduleNamesComboBox = new AComboBox();

	private JTextField selectedTemplateNameField = new JTextField();

	static private ReportTemplate templateProcessed = null;

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
			jbInit();
			
			setComboBoxData();
			setListData();
			enableSelectedTemplateNameField();

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
				StorableObjectWrapper.COLUMN_NAME,
				StorableObjectWrapper.COLUMN_NAME);
		
		if (this.mode == TemplateOpenSaveDialog.OPEN)
			this.setTitle(I18N.getString("report.UI.TemplateSaveOpenDialog.openTemplate"));
		if (this.mode == TemplateOpenSaveDialog.SAVE)
			this.setTitle(I18N.getString("report.UI.TemplateSaveOpenDialog.saveTemplate"));
		
		this.templatesScrollPanel.setBorder(BorderFactory.createEtchedBorder());

		this.openSaveButton.setMargin(new Insets(2, 2, 2, 2));
		if (this.mode == OPEN)
			this.openSaveButton.setText(I18N.getString("report.UI.open"));
		if (this.mode == SAVE)
			this.openSaveButton.setText(I18N.getString("report.UI.save"));
		this.openSaveButton.setEnabled(false);
		this.openSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try {
					openSaveButton_actionPerformed();
				} catch (ApplicationException e1) {
					Log.errorMessage("TemplateOpenSaveDialog.actionPerformed | " + e1.getMessage());
					Log.errorMessage(e1);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString(
									TemplateOpenSaveDialog.this.mode == SAVE 
									? "report.Exception.saveTemplateError" 
											: "report.Exception.openTemplateError")
								+ " ("
								+ e1.getMessage()
								+ ").",
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		this.cancelButton.setText(I18N.getString("report.UI.cancel"));
		this.cancelButton.setMargin(new Insets(2, 2, 2, 2));

		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed();
			}
		});
		
		this.removeButton.setMargin(new Insets(2, 2, 2, 2));
		this.removeButton.setText(I18N.getString("report.UI.remove"));
		this.removeButton.setEnabled(false);

		this.removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeButton_actionPerformed();
				} catch (ApplicationException e1) {
					Log.errorMessage("TemplateOpenSaveDialog.actionPerformed | " + e1.getMessage());
					Log.errorMessage(e1);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Exception.deleteTemplateError"),
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		this.selectedTemplateNameField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				selectedTemplateNameField_keyPressed(e);
			}
		});
		
		this.moduleNamesComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					templateTypesComboBox_actionPerformed();
				} catch (ApplicationException e1) {
					Log.errorMessage("TemplateOpenSaveDialog.actionPerformed | " + e1.getMessage());
					Log.errorMessage(e1);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Exception.deleteTemplateError"),
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
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

	private void setListData() throws ApplicationException {
		this.templatesList.removeAll();

		this.getReportTemplatesFromPool();
		if (this.allReportTemplates == null)
			return;

		int index = this.moduleNamesComboBox.getSelectedIndex();
		if (index == 0 && this.mode == OPEN) {
			this.templatesList.addElements(this.allReportTemplates);
			return;
		}
		int modelIndex;
		if (this.mode == OPEN) {
			modelIndex = index-1;
		} else {
			modelIndex = index;
		}
		Set<ReportTemplate> reportTemplatesForModule = new HashSet<ReportTemplate>();
		String selectedModelName = ReportModels.values()[modelIndex].getName(); 
		for (ReportTemplate reportTemplate : this.allReportTemplates) {
			if (	reportTemplate.getDestinationModule().equals(selectedModelName)) {
				reportTemplatesForModule.add(reportTemplate);
			}
				
		}
		this.templatesList.addElements(reportTemplatesForModule);
	}
	
	private void enableSelectedTemplateNameField() {
		if(this.mode == OPEN) {
			this.selectedTemplateNameField.setVisible(false);
		} else {
			this.selectedTemplateNameField.setVisible(true);
		}
	}

	private void getReportTemplatesFromPool() throws ApplicationException {
		StorableObjectCondition condition = new EquivalentCondition(
				ObjectEntities.REPORTTEMPLATE_CODE);
		this.allReportTemplates = StorableObjectPool.getStorableObjectsByCondition(condition, true);
	}
	
	private void setComboBoxData() {
		if (this.mode == OPEN) {
			this.moduleNamesComboBox.addItem(I18N.getString(ALL_TEMPLATES));
			for (int i = 0; i < ReportModels.values().length; i++) {
				this.moduleNamesComboBox.addItem(
						I18N.getString(ReportModels.values()[i].getName()));
			}
			this.moduleNamesComboBox.setEnabled(true);
		} else {
			for (int i = 0; i < ReportModels.values().length; i++) {
				this.moduleNamesComboBox.addItem(
						I18N.getString(ReportModels.values()[i].getName()));
			}
			int modelIndex = TemplateTypeChooser.getSelectedModuleIndex();
			this.moduleNamesComboBox.setSelectedIndex(modelIndex);
			this.moduleNamesComboBox.setEnabled(false);
		}
		
	}

	protected void openSaveButton_actionPerformed() throws ApplicationException {
		ReportTemplate selectedTemplate =
			this.templateForName(this.selectedTemplateNameField.getText());
		
		if (this.mode == OPEN) {
			if (selectedTemplate == null) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						I18N.getString("report.Exception.noTemplateForName"),
						I18N.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.templateProcessed = selectedTemplate;
			this.setVisible(false);
		}
		else if (this.mode == SAVE) {
			if (this.selectedTemplateNameField.getText().length() == 0)
				return;
			if (selectedTemplate != null) {
				int replace = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						I18N.getString(
								"report.Command.SaveTemplate.templateExists"),
						I18N.getString("report.File.confirm"),
						JOptionPane.YES_NO_OPTION);
				if (replace == JOptionPane.NO_OPTION)
					return;
			} else {
				if (!this.templateProcessed.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
					try {
						ReportTemplate oldTemplate = this.templateProcessed;
						this.templateProcessed = this.templateProcessed.clone();
						StorableObjectPool.cleanChangedStorableObjects(oldTemplate.getReverseDependencies(false));
					} catch (CloneNotSupportedException e) {
						Log.errorMessage(e);
					}
					
				}
					
			}
						
			this.templateProcessed.setName(this.selectedTemplateNameField.getText());
			
			StorableObjectPool.flush(this.templateProcessed,LoginManager.getUserId(),true);
			StorableObjectPool.flush(
					this.templateProcessed.getReverseDependencies(false),
					LoginManager.getUserId(),
					true);			
			this.templateProcessed.setNew(false);
			this.setVisible(false);
		}
	}

	protected void cancelButton_actionPerformed() {
		this.setVisible(false);
	}

	protected void removeButton_actionPerformed() throws ApplicationException {
		int[] selectedIndices = this.templatesList.getSelectedIndices();
		Set<ReportTemplate> selectedTemplates = new HashSet<ReportTemplate>();

		for (int i = 0; i < selectedIndices.length; i++) {
			selectedTemplates.add((ReportTemplate)this.templatesList.
				getModel().getElementAt(selectedIndices[i]));
		}
		
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		for (ReportTemplate reportTemplate : selectedTemplates) {
			dependencies.addAll(reportTemplate.getReverseDependencies(true));
		}
		
		StorableObjectPool.delete(selectedTemplates);
		StorableObjectPool.delete(dependencies);		
		StorableObjectPool.flush(selectedTemplates,LoginManager.getUserId(),true);
		StorableObjectPool.flush(dependencies,LoginManager.getUserId(),true);			

		this.setListData();
	}

	protected void templatesList_ItemChanged() {
		if (this.templatesList.getSelectedIndex() != -1) {
			this.selectedTemplateNameField.setText(
					((ReportTemplate)this.templatesList.getSelectedValue()).getName());

			this.openSaveButton.setEnabled(true);

			this.removeButton.setEnabled(true);
		}
		else {
			this.removeButton.setEnabled(false);
			this.openSaveButton.setEnabled(false);
		}
	}

	private ReportTemplate templateForName (String name) {
		for (ReportTemplate reportTemplate : this.allReportTemplates) {
			if (reportTemplate.getName().equals(name)) {
				return reportTemplate;
			}
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
	}

	protected void templateTypesComboBox_actionPerformed() throws ApplicationException{
		this.setListData();
		this.selectedTemplateNameField.setText("");
	}

	public ReportTemplate getTemplateProcessed() {
		return templateProcessed;
	}

	public void setTemplateProcessed(ReportTemplate templateProcessed) {
		TemplateOpenSaveDialog.templateProcessed = templateProcessed;
	}
	
	public static ReportTemplate getReportTemplate() {
		return templateProcessed;
	}
}

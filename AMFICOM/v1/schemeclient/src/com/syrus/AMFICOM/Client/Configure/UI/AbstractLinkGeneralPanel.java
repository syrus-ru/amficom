package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.*;

public abstract class AbstractLinkGeneralPanel extends GeneralPanel
{
	protected AbstractSchemeLink link;

	private JLabel typeLabel = new JLabel();
	protected ObjComboBox typeBox;
	private JLabel rnLabel1 = new JLabel();
	private JLabel rnLabel2 = new JLabel();
	protected JTextField rnField = new JTextField();
	private JLabel nameLabel = new JLabel();
	protected JTextField nameField = new JTextField();
	private JLabel manufacturerLabel = new JLabel();
	private JLabel manufacturerCodeLabel = new JLabel();
	protected JTextField manufacturerField = new JTextField();
	protected JTextField manufacturerCodeField = new JTextField();
	private JLabel supplierLabel = new JLabel();
	private JLabel supplierCodeLabel = new JLabel();
	protected JTextField supplierField = new JTextField();
	protected JTextField supplierCodeField = new JTextField();
	private JLabel start_equipmentLabel = new JLabel();
	private JLabel start_equipmentPortLabel1 = new JLabel();
	private JLabel start_equipmentPortLabel2 = new JLabel();
	protected JTextField startEquipmentPortBox = new JTextField();
	protected JTextField startEquipmentBox = new JTextField();
	private JLabel end_equipmentLabel = new JLabel();
	private JLabel end_equipmentPortLabel2 = new JLabel();
	private JLabel end_equipmentPortLabel1 = new JLabel();
	protected JTextField endEquipmentBox = new JTextField();
	protected JTextField endEquipmentPortBox = new JTextField();
	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	protected JTextField modifyField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	protected JScrollPane descriptionScrollPane = new JScrollPane();
	protected JTextPane descTextArea = new JTextPane();
	private JPanel mainPanel = new JPanel();
	protected JTextField idField = new JTextField();
	private JLabel idLabel = new JLabel();
	private JLabel optLabel = new JLabel();
	private JLabel physLabel = new JLabel();
	protected JTextField optLengthField = new JTextField();
	protected JTextField physLengthField = new JTextField();

	protected AbstractLinkGeneralPanel()
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

	protected AbstractLinkGeneralPanel(AbstractSchemeLink link)
	{
		this();
		setObject(link);
	}

	private void jbInit() throws Exception
	{
		this.typeBox = new ObjComboBox(
				LinkTypeController.getInstance(),
				StorableObjectWrapper.COLUMN_NAME);

		this.typeBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					typeBox_actionPerformed();
			}
		});

		this.typeLabel.setText(LangModelConfig.getString("label_type")); //$NON-NLS-1$
		this.typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.rnLabel1.setText(LangModelConfig.getString("label_inventory_nr1")); //$NON-NLS-1$
		this.rnLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.rnLabel2.setText(LangModelConfig.getString("label_inventory_nr2")); //$NON-NLS-1$
		this.rnLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.nameLabel.setText(LangModelConfig.getString("label_name")); //$NON-NLS-1$
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.manufacturerLabel.setText(LangModelConfig.getString("label_manufacter")); //$NON-NLS-1$
		this.manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode")); //$NON-NLS-1$
		this.manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.manufacturerField.setEnabled(false);
		this.manufacturerCodeField.setEnabled(false);
		this.supplierLabel.setText(LangModelConfig.getString("label_supplier")); //$NON-NLS-1$
		this.supplierLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.supplierCodeLabel.setText(""); //$NON-NLS-1$
		this.supplierCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.start_equipmentLabel.setText(LangModelConfig.getString("link_start_equipment_id")); //$NON-NLS-1$
		this.start_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.start_equipmentPortLabel1.setText(LangModelConfig.getString("label_port")); //$NON-NLS-1$
		this.start_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.start_equipmentPortLabel2.setText(LangModelConfig.getString("link_start_equipment_id1")); //$NON-NLS-1$
		this.start_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.end_equipmentLabel.setText(LangModelConfig.getString("link_end_equipment_id")); //$NON-NLS-1$
		this.end_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.end_equipmentPortLabel1.setText(LangModelConfig.getString("label_port")); //$NON-NLS-1$
		this.end_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.end_equipmentPortLabel2.setText(LangModelConfig.getString("link_end_equipment_id1")); //$NON-NLS-1$
		this.end_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.modifyLabel1.setText(LangModelConfig.getString("label_modified1")); //$NON-NLS-1$
		this.modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.modifyLabel2.setText(LangModelConfig.getString("label_modified2")); //$NON-NLS-1$
		this.modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		this.modifyField.setEnabled(false);

		this.descLabel.setText(LangModelConfig.getString("label_description")); //$NON-NLS-1$
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.idLabel.setText(LangModelConfig.getString("label_id")); //$NON-NLS-1$
		this.idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.idField.setEnabled(false);
		this.optLabel.setText(LangModelConfig.getString("link_optical_length")); //$NON-NLS-1$
		this.optLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.physLabel.setText(LangModelConfig.getString("link_physical_length")); //$NON-NLS-1$
		this.physLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.typeBox.setEnabled(false);
		this.startEquipmentBox.setEnabled(false);
		this.startEquipmentPortBox.setEnabled(false);
		this.endEquipmentBox.setEnabled(false);
		this.endEquipmentPortBox.setEnabled(false);

		this.descriptionPanel.setLayout(new BorderLayout());
		this.descriptionScrollPane.getViewport().add(this.descTextArea, null);
		this.descriptionPanel.add(this.descriptionScrollPane, BorderLayout.CENTER);

		this.setLayout(new GridBagLayout());

		this.add(this.typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.nameLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.physLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.optLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.rnLabel1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.rnLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.manufacturerLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.manufacturerCodeLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.supplierLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.supplierCodeLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.start_equipmentLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.start_equipmentPortLabel1, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.start_equipmentPortLabel2, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.end_equipmentLabel, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.end_equipmentPortLabel1, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.end_equipmentPortLabel2, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.modifyLabel1, new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.modifyLabel2, new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.descLabel, new GridBagConstraints(0, 19, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			this.mainPanel.add(this.idLabel, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(this.typeBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.nameField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.physLengthField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.optLengthField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.rnField, new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.manufacturerField, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.manufacturerCodeField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.supplierField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.supplierCodeField, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.startEquipmentBox, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.startEquipmentPortBox, new GridBagConstraints(1, 12, 1, 2, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.endEquipmentBox, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.endEquipmentPortBox, new GridBagConstraints(1, 15, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.modifyField, new GridBagConstraints(1, 17, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.descriptionPanel, new GridBagConstraints(1, 19, 1, 1, 1.0, 1.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			this.mainPanel.add(this.idField, new GridBagConstraints(1, 20, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton, BorderLayout.SOUTH);
	}

	public Object getObject()
	{
		return this.link;
	}

	public void setObject(Object or)
	{
		this.link = (AbstractSchemeLink) or;

		this.idField.setText(this.link.id().identifierString());
		this.nameField.setText(this.link.name());
		this.physLengthField.setText(String.valueOf(this.link.physicalLength()));
		this.optLengthField.setText(String.valueOf(this.link.opticalLength()));

		AbstractSchemePort sport = this.link.sourceAbstractSchemePort();
		SchemeDevice sdev = sport.schemeDevice();
		this.startEquipmentBox.setText(sdev.name());
		this.startEquipmentPortBox.setText(sport.name());

		AbstractSchemePort eport = this.link.targetAbstractSchemePort();
		SchemeDevice edev = eport.schemeDevice();
		this.endEquipmentBox.setText(edev.name());
		this.startEquipmentPortBox.setText(eport.name());

		this.descTextArea.setText(this.link.description());

		if(this.link.link() != null)
		{
			this.rnField.setText(this.link.link().inventoryNo);
			this.supplierField.setText(this.link.link().supplier);
			this.supplierCodeField.setText(this.link.link().supplierCode);
		}
		else
		{
			this.rnField.setText(""); //$NON-NLS-1$
			this.supplierField.setText(""); //$NON-NLS-1$
			this.supplierCodeField.setText(""); //$NON-NLS-1$
		}
	}

	public boolean modify()
	{
		try
		{
			double d1 = Double.parseDouble(this.physLengthField.getText());
			double d2 = Double.parseDouble(this.optLengthField.getText());

			this.link.physicalLength(d1);
			this.link.opticalLength(d2);

			if (MiscUtil.validName(this.nameField.getText()))
				this.link.name(this.nameField.getText());
			else
				return false;

			this.link.description(this.descTextArea.getText());

			if (this.link.link() != null)
			{
				this.link.link().inventoryNo = this.rnField.getText();
				this.link.link().supplier = this.supplierField.getText();
				this.link.link().supplierCode = this.supplierCodeField.getText();
			}
		}
		catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		if (modify())
		{
			if (this.link.link() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(this.link.linkImpl());
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	protected abstract void typeBox_actionPerformed();

	public boolean delete()
	{
		return false;
	}
}
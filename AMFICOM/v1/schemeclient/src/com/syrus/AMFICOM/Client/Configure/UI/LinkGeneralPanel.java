package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class LinkGeneralPanel extends GeneralPanel
{
	SchemeLink link;

	JLabel typeLabel = new JLabel();
	ObjectResourceComboBox typeBox = new ObjectResourceComboBox(SchemeLink.typ, true);

	JLabel rnLabel1 = new JLabel();
	private JLabel rnLabel2 = new JLabel();
	JTextField rnField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private JLabel manufacturerLabel = new JLabel();
	private JLabel manufacturerCodeLabel = new JLabel();
	private JTextField manufacturerField = new JTextField();
	private JTextField manufacturerCodeField = new JTextField();

	private JLabel supplierLabel = new JLabel();
	private JLabel supplierCodeLabel = new JLabel();

	private JTextField supplierField = new JTextField();
	private JTextField supplierCodeField = new JTextField();

	private JLabel start_equipmentLabel = new JLabel();
	private JLabel start_equipmentPortLabel1 = new JLabel();
	private JLabel start_equipmentPortLabel2 = new JLabel();
	private JTextField startEquipmentPortBox = new JTextField();
	private JTextField startEquipmentBox = new JTextField();

	private JLabel end_equipmentLabel = new JLabel();
	private JLabel end_equipmentPortLabel2 = new JLabel();
	private JLabel end_equipmentPortLabel1 = new JLabel();
	private JTextField endEquipmentBox = new JTextField();
	private JTextField endEquipmentPortBox = new JTextField();

	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField modifyField = new JTextField();

	private JLabel descLabel = new JLabel();
	JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();

	private JPanel mainPanel = new JPanel();

	JTextField idField = new JTextField();
	JLabel idLabel = new JLabel();

	private JLabel optLabel = new JLabel();
	private JLabel physLabel = new JLabel();
	private JTextField optLengthField = new JTextField();
	private JTextField physLengthField = new JTextField();

	public LinkGeneralPanel()
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

	public LinkGeneralPanel(SchemeLink link)
	{
		this();
		setObjectResource(link);
	}

	private void jbInit() throws Exception
	{
		typeLabel.setText(LangModelConfig.getString("label_type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		rnLabel1.setText(LangModelConfig.getString("label_inventory_nr1"));
		rnLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		rnLabel2.setText(LangModelConfig.getString("label_inventory_nr2"));
		rnLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerLabel.setText(LangModelConfig.getString("label_manufacter"));
		manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode"));
		manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerField.setEnabled(false);
		manufacturerCodeField.setEnabled(false);
		supplierLabel.setText(LangModelConfig.getString("label_supplier"));
		supplierLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		supplierCodeLabel.setText(LangModelConfig.getString("label_supCode"));
		supplierCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		start_equipmentLabel.setText(LangModelConfig.getString("link_start_equipment_id"));
		start_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		start_equipmentPortLabel1.setText(LangModelConfig.getString("label_port"));
		start_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		start_equipmentPortLabel2.setText(LangModelConfig.getString("link_start_equipment_id1"));
		start_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		end_equipmentLabel.setText(LangModelConfig.getString("link_end_equipment_id"));
		end_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		end_equipmentPortLabel1.setText(LangModelConfig.getString("label_port"));
		end_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		end_equipmentPortLabel2.setText(LangModelConfig.getString("link_end_equipment_id1"));
		end_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyField.setEnabled(false);

		descLabel.setText(LangModelConfig.getString("label_description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		idField.setEnabled(false);
		optLabel.setText(LangModelConfig.getString("link_optical_length"));
		optLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		physLabel.setText(LangModelConfig.getString("link_physical_length"));
		physLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		typeBox.setEnabled(false);
		startEquipmentBox.setEnabled(false);
		startEquipmentPortBox.setEnabled(false);
		endEquipmentBox.setEnabled(false);
		endEquipmentPortBox.setEnabled(false);

		descriptionPanel.setLayout(new BorderLayout());
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.setLayout(new GridBagLayout());

		this.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(nameLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(physLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(optLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(rnLabel1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(rnLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(supplierLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(supplierCodeLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(start_equipmentLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(start_equipmentPortLabel1, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(start_equipmentPortLabel2, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(end_equipmentLabel, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(end_equipmentPortLabel1, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(end_equipmentPortLabel2, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel1, new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel2, new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descLabel, new GridBagConstraints(0, 19, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			mainPanel.add(idLabel, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(typeBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(nameField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(physLengthField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(optLengthField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(rnField, new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerField, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(supplierField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(supplierCodeField, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(startEquipmentBox, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(startEquipmentPortBox, new GridBagConstraints(1, 12, 1, 2, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(endEquipmentBox, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(endEquipmentPortBox, new GridBagConstraints(1, 15, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyField, new GridBagConstraints(1, 17, 1, 2, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel, new GridBagConstraints(1, 19, 1, 1, 1.0, 1.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			mainPanel.add(idField, new GridBagConstraints(1, 20, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton, BorderLayout.SOUTH);
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public void setObjectResource(ObjectResource or)
	{
		link = (SchemeLink) or;

		idField.setText(link.getId());
		nameField.setText(link.getName());
		physLengthField.setText(String.valueOf(link.physicalLength));
		optLengthField.setText(String.valueOf(link.opticalLength));

		SchemePort sport = (SchemePort)Pool.get(SchemePort.typ, link.sourcePortId);
		SchemeDevice sdev = (SchemeDevice)Pool.get(SchemeDevice.typ, sport.deviceId);
		startEquipmentBox.setText(sdev.getName());
		startEquipmentPortBox.setText(sport.getName());

		SchemePort eport = (SchemePort)Pool.get(SchemePort.typ, link.sourcePortId);
		SchemeDevice edev = (SchemeDevice)Pool.get(SchemeDevice.typ, sport.deviceId);
		endEquipmentBox.setText(edev.getName());
		startEquipmentPortBox.setText(eport.getName());

		if(link.link != null)
		{
//			System.out.println("set prop pane to " + cl.name);
			descTextArea.setText(link.link.description);
			typeBox.setSelected(link.link.typeId);
			rnField.setText(link.link.inventoryNr);
			manufacturerField.setText(link.link.manufacturer);
			manufacturerCodeField.setText(link.link.manufacturerCode);
			supplierField.setText(link.link.supplier);
			supplierCodeField.setText(link.link.supplierCode);
		}
		else
		{
			typeBox.setSelected("");
			descTextArea.setText("");
			rnField.setText("");
			manufacturerField.setText("");
			manufacturerCodeField.setText("");
			supplierField.setText("");
			supplierCodeField.setText("");
			modifyField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			double d1 = Double.parseDouble(physLengthField.getText());
			double d2 = Double.parseDouble(optLengthField.getText());

			link.physicalLength = d1;
			link.opticalLength = d2;

			if (MiscUtil.validName(nameField.getText()))
				link.name = nameField.getText();
			else
				return false;

			if (link.link != null)
			{
				link.link.name = nameField.getText();
				link.link.typeId = (String)typeBox.getSelected();
				link.link.description = descTextArea.getText();
				link.link.inventoryNr = rnField.getText();
				link.link.manufacturer = manufacturerField.getText();
				link.link.manufacturerCode = manufacturerCodeField.getText();
				link.link.supplier = supplierField.getText();
				link.link.supplierCode = supplierCodeField.getText();
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
		if (!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
		{
			return;
		}

		if (modify())
		{
			if (link.link != null)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();
				dataSource.SaveLink(link.link.getId());
			}
		}
	}

	public boolean delete()
	{
		if (!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		if (link.link != null)
		{
			String[] s = new String[1];
			s[0] = link.link.getId();
			aContext.getDataSourceInterface().RemoveLinks(s);
		}
		return true;
	}
}
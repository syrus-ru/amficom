package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class EquipmentGeneralPanel extends GeneralPanel
{
	SchemeElement element;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private JButton saveButton = new JButton();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private JLabel portsNumberLabel = new JLabel();
	private JTextField portsNumberField = new JTextField();

	private JLabel domainLabel = new JLabel();
	private JTextField domainField = new JTextField();

	JLabel longitudeLabel = new JLabel();
	private JTextField longitudeField = new JTextField();
	private JLabel latitudeLabel = new JLabel();
	private JTextField latitudeField = new JTextField();

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	private JLabel modifyLabel1 = new JLabel();
	private JLabel modifyLabel2 = new JLabel();
	private JTextField modifyField = new JTextField();

	private JLabel cabelPortsNumberLabel1 = new JLabel();
	private JLabel cabelPortsNumberLabel2 = new JLabel();
	private JTextField cabelPortsNumberField = new JTextField();

	JLabel typeLabel = new JLabel();
	ObjComboBox typeBox;

	private JLabel agentLabel = new JLabel();
	private JTextField agentField = new JTextField();

	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout4 = new BorderLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel descLabel = new JLabel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	JPanel descriptionPanel = new JPanel();
	public JTextPane descTextArea = new JTextPane();

	public EquipmentGeneralPanel()
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

	public EquipmentGeneralPanel(Equipment equipment)
	{
		this();
		setObject(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("menuNetCatEquipmentText"));

		this.setLayout(gridBagLayout1);

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});
		latitudeLabel.setText(LangModelConfig.getString("equip_latitude"));
		latitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		longitudeLabel.setText(LangModelConfig.getString("equip_longitude"));
		longitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		portsNumberLabel.setText(LangModelConfig.getString("equip_portsNumber"));
		portsNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		 portsNumberField.setEnabled(false);

		domainLabel.setText(LangModelConfig.getString("equip_domen"));
		domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		domainField.setEnabled(false);

		modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyField.setEnabled(false);

		typeLabel.setText(LangModelConfig.getString("label_type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		typeBox.setEnabled(false);

		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idField.setEnabled(false);

		cabelPortsNumberLabel1.setText(LangModelConfig.getString("equip_cablePortsNumber1"));
		cabelPortsNumberLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		cabelPortsNumberLabel2.setText(LangModelConfig.getString("equip_cablePortsNumber2"));
		cabelPortsNumberLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		cabelPortsNumberField.setEnabled(false);

		agentLabel.setText(LangModelConfig.getString("equip_agent"));
		agentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelConfig.getString("label_description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descriptionPanel.setLayout(borderLayout1);
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(longitudeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(latitudeLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel1,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel2,           new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(portsNumberLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(cabelPortsNumberLabel1,      new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(cabelPortsNumberLabel2,           new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(agentLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descLabel,  new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(typeBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(longitudeField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(latitudeField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainField, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyField,       new GridBagConstraints(1, 6, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(portsNumberField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(cabelPortsNumberField, new GridBagConstraints(1, 9, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(agentField, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel,  new GridBagConstraints(1, 12, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 13, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton, new XYConstraints(200, 380, -1, -1));
	}

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		this.element = (SchemeElement)or;

		idField.setText(element.id().identifierString());
		nameField.setText(element.name());
		descTextArea.setText(element.description());

		typeBox.setSelectedItem(element.equipmentTypeImpl());
		portsNumberField.setText(Long.toString(SchemeUtils.getPorts(element).size()));
		cabelPortsNumberField.setText(Long.toString(SchemeUtils.getCablePorts(element).size()));

		if(element.equipment() != null)
		{
			double d1 = element.equipmentImpl().getLongitude();
			d1 = MiscUtil.fourdigits(d1);
			longitudeField.setText(String.valueOf(d1));

			double d2 = element.equipmentImpl().getLatitude();
			d2 = MiscUtil.fourdigits(d1);
			latitudeField.setText(String.valueOf(d2));

			domainField.setText(element.equipmentImpl().getDomainId().getIdentifierString());
			modifyField.setText(sdf.format(element.equipmentImpl().getModified()));
		}
		else
		{
			longitudeField.setText("");
			latitudeField.setText("");
			domainField.setText("");
			modifyField.setText("");
		}

		if(element.rtu() != null)
		{
			agentField.setEnabled(true);
			agentField.setText(element.rtuImpl().getMCMId().getIdentifierString());
			agentField.setVisible(true);
			agentLabel.setVisible(true);
		}
		else
		{
			agentField.setEnabled(false);
			agentField.setText("");
			agentField.setVisible(false);
			agentLabel.setVisible(false);
		}
	}

	public boolean modify()
	{
		try
		{
			if (element.equipment() != null)
			{
				float d1 = Float.parseFloat(this.longitudeField.getText());
				d1 = MiscUtil.fourdigits(d1);
				float d2 = Float.parseFloat(this.latitudeField.getText());
				d2 = MiscUtil.fourdigits(d2);
				element.equipmentImpl().setLongitude(d1);
				element.equipmentImpl().setLatitude(d2);
			}

			if(MiscUtil.validName(nameField.getText()))
				element.name(nameField.getText());
			else
				return false;

			element.description(descTextArea.getText());
			element.equipmentTypeImpl((EquipmentType)typeBox.getSelectedItem());
			if (element.rtu() != null)
			{
//				element.rtuImpl().setMcmId(new Identifier(agentField.getText()));
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		if(modify())
		{
			if (element.equipment() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(element.equipmentImpl());
				}
				catch (ApplicationException ex) {
				}
			}
		}
	}

}
package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class MeasurementPortGeneralPanel extends GeneralPanel
{
	protected MeasurementPort port;
	protected MonitoredElement me;

	private JPanel mainPanel = new JPanel();
	private JButton saveButton = new JButton();
	private JLabel localLabel1 = new JLabel();
	private JLabel localLabel = new JLabel();
	private JTextField localField = new JTextField();
	private JLabel kisLabel = new JLabel();
	private JTextField KISField = new JTextField();
	private JLabel portLabel = new JLabel();
	private JTextField portField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private ObjComboBox typeBox;
	private JLabel nameLabel = new JLabel();
	private JTextField nameField = new JTextField();
	private JLabel idLabel = new JLabel();
	private JTextField idField = new JTextField();

	protected MeasurementPortGeneralPanel()
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

	protected MeasurementPortGeneralPanel(MeasurementPort ap)
	{
		this();
		setObject(ap);
	}

	private void jbInit() throws Exception
	{
		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
				domain_id, true);
		DomainCondition condition = new DomainCondition(domain, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);

		typeBox = new ObjComboBox(
				MeasurementPortTypeController.getInstance(),
				ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
				StorableObjectWrapper.COLUMN_NAME);


		this.setLayout(new BorderLayout());

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		localLabel.setText(LangModelConfig.getString("label_local_addr1"));
		localLabel.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		localLabel1.setText(LangModelConfig.getString("label_local_addr2"));
		localLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		kisLabel.setText(LangModelConfig.getString("menuNetCatEquipmentText"));
		kisLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		portLabel.setText(LangModelConfig.getString("label_port"));
		portLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		typeLabel.setText(LangModelConfig.getString("label_type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		idField.setEnabled(false);
		typeBox.setEnabled(false);
		portField.setEnabled(false);
		KISField.setEnabled(false);

		mainPanel.setLayout(new GridBagLayout());

		mainPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(kisLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(portLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(localLabel,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		mainPanel.add(localLabel1,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

		if(Environment.isDebugMode())
			mainPanel.add(idLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(nameField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(typeBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(KISField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(portField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(localField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
			,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			mainPanel.add(idField, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(mainPanel,BorderLayout.NORTH);
//		this.add(saveButton,      new XYConstraints(200, 210, -1, -1));
	}

	public Object getObject()
	{
		return port;
	}

	public void setObject(Object or)
	{
		this.port = (MeasurementPort)or;

		if(port != null)
		{
			try {
				LinkedIdsCondition condition = new LinkedIdsCondition(port.getId(), ObjectEntities.ME_ENTITY_CODE);

				List list = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (list.size() > 0) {
					me = (MonitoredElement)list.get(0);
				}
			}
			catch (ApplicationException ex) {
			}

			typeBox.setSelectedItem(port.getType());
			idField.setText(port.getId().getIdentifierString());
			nameField.setText(port.getName());
			portField.setText(port.getPortId().getIdentifierString());
			KISField.setText(port.getKISId().getIdentifierString());
			localField.setText(me.getLocalAddress());
		}
		else
		{
			nameField.setText("");
			idField.setText("");
			localField.setText("");
			portField.setText("");
			KISField.setText("");
			typeBox.setSelectedItem("");
//			imageLabel.setIcon(new ImageIcon());
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				port.setName(nameField.getText());
			else
				return false;
			port.setType((MeasurementPortType)typeBox.getSelectedItem());
			me.setLocalAddress(localField.getText());
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		save();
	}

}
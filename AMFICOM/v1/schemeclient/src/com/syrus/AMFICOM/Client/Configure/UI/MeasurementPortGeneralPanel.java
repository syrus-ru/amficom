package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.MeasurementPortType;
import com.syrus.AMFICOM.Client.Resource.Network.Port;

public class MeasurementPortGeneralPanel extends GeneralPanel
{
	MeasurementPort ap;

	JPanel mainPanel = new JPanel();
	public JButton saveButton = new JButton();

	public JLabel localLabel1 = new JLabel();
	public JLabel localLabel = new JLabel();
	public JTextField localField = new JTextField();

	public JLabel kisLabel = new JLabel();
	public ObjectResourceComboBox KISBox = new ObjectResourceComboBox(KIS.typ, true);

	public JLabel portLabel = new JLabel();
	public ObjectResourceComboBox portBox = new ObjectResourceComboBox(Port.typ, true);

	public JLabel typeLabel = new JLabel();
	public ObjectResourceComboBox typeBox = new ObjectResourceComboBox(MeasurementPortType.typ, true);

	public JLabel nameLabel = new JLabel();
	public JTextField nameField = new JTextField();

	public JLabel idLabel = new JLabel();
	public JTextField idField = new JTextField();

	public MeasurementPortGeneralPanel()
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

	public MeasurementPortGeneralPanel(MeasurementPort ap)
	{
		this();
		setObjectResource(ap);
	}

	private void jbInit() throws Exception
	{
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
		portBox.setEnabled(false);
		KISBox.setEnabled(false);

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
		mainPanel.add(KISBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(portBox, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(localField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
			,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			mainPanel.add(idField, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(mainPanel,BorderLayout.NORTH);
//		this.add(saveButton,      new XYConstraints(200, 210, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
		return ap;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.ap = (MeasurementPort)or;

		if(ap != null)
		{
//			System.out.println("set prop pane to " + ap.name);

			typeBox.setSelected(ap.typeId);
			idField.setText(ap.getId());
			nameField.setText(ap.getName());
			portBox.setSelected(ap.portId);
			KISBox.setSelected(ap.kisId);
			localField.setText(ap.localId);
		}
		else
		{
			nameField.setText("");
			idField.setText("");
			localField.setText("");
			portBox.setSelected("");
			KISBox.setSelected("");
			typeBox.setSelected("");
//			imageLabel.setIcon(new ImageIcon());
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				ap.name = nameField.getText();
			else
				return false;
			ap.id = idField.getText();
			ap.portId = (String )portBox.getSelected();
			ap.typeId = (String )typeBox.getSelected();
			ap.kisId = (String )KISBox.getSelected();
//			ap.name = nameField.getText();
			ap.localId = localField.getText();
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveAccessPort(ap.getId());
		}
	}

}
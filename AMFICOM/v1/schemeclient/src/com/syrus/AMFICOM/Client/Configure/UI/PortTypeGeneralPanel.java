package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;

public class PortTypeGeneralPanel extends GeneralPanel
{
	protected PortType portType;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private static String[] typeSorts = new String[] {
		"Оптический",
		"Сварное соединение",
		"Электрический"
	};

	private JLabel idLabel = new JLabel();
	private JTextField idField = new JTextField();
	private JLabel nameLabel = new JLabel();
	private JTextField nameField = new JTextField();
	private JLabel classLabel = new JLabel();
	private JComboBox classBox = new JComboBox();
	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField ModifyField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	private JScrollPane descriptionScrollPane = new JScrollPane();
	private JTextPane descTextArea = new JTextPane();
	private JButton saveButton = new JButton();

	protected PortTypeGeneralPanel()
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

	protected PortTypeGeneralPanel(PortType portType)
	{
		this();
		setObject(portType);
	}

	private void jbInit() throws Exception
	{
		for (int i = 0; i < typeSorts.length; i++)
			classBox.addItem(typeSorts[i]);

		setName(LangModelConfig.getString("label_general"));

		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idField.setEnabled(false);

		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelConfig.getString("label_description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

//		interfaceLabel.setText(LangModelConfig.getString("port_interface_id"));
//		interfaceLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 // interfaceField.setEnabled(false);

		classLabel.setText(LangModelConfig.getString("port_class"));
		classLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 // classField.setEnabled(false);

		ModifyField.setEnabled(false);
		modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		this.setLayout(new GridBagLayout());

		descriptionPanel.setLayout(new BorderLayout());
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//		this.add(interfaceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(classLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel1,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel2,           new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(descLabel,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//		this.add(interfaceField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(classBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(ModifyField,       new GridBagConstraints(1, 3, 1, 2, 0.0, 0.0
						,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel,  new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Object getObject()
	{
		return portType;
	}

	public void setObject(Object or)
	{
		this.portType = (PortType)or;

		if(portType != null)
		{
			idField.setText(portType.getId().getIdentifierString());
			nameField.setText(portType.getName());
			this.descTextArea.setText(portType.getDescription());
//			this.interfaceField.setText(portType.interfaceId);
			switch (portType.getSort().value())
			{
				case PortTypeSort._PORTTYPESORT_OPTICAL:
					classBox.setSelectedItem(typeSorts[0]);
					break;
				case PortTypeSort._PORTTYPESORT_THERMAL:
					classBox.setSelectedItem(typeSorts[1]);
					break;
				default:
					classBox.setSelectedItem(typeSorts[2]);
			}

			this.ModifyField.setText(sdf.format(portType.getModified()));
		}
		else
		{
			idField.setText("");
			nameField.setText("");
			this.descTextArea.setText("");
//			this.interfaceField.setText("");

			this.ModifyField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				 portType.setName(nameField.getText());
			else
				return false;

			portType.setDescription(this.descTextArea.getText());

			if (classBox.getSelectedItem().equals(typeSorts[0]))
				portType.setSort(PortTypeSort.PORTTYPESORT_OPTICAL);
			else if (classBox.getSelectedItem().equals(typeSorts[1]))
				portType.setSort(PortTypeSort.PORTTYPESORT_THERMAL);
			else
				portType.setSort(PortTypeSort.PORTTYPESORT_ELECTRICAL);
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
			try {
				ConfigurationStorableObjectPool.putStorableObject(portType);
				ConfigurationStorableObjectPool.flush(true);
			}
			catch (ApplicationException ex) {
			}
		}
	}

	public boolean delete()
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = linkType.id;
		aContext.getDataSourceInterface().RemoveLinks(s);*/

		return true;
	}
}
package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
//import com.syrus.AMFICOM.Client.Resource.Test.TestType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;

public class AccessPortTypeGeneralPanel extends GeneralPanel
{
	AccessPortType apt;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private BorderLayout borderLayout1 = new BorderLayout();

	public JLabel idLabel = new JLabel();
	public JTextField idField = new JTextField();

	public JLabel nameLabel = new JLabel();
	public JTextField nameField = new JTextField();

	public JLabel typesLabel = new JLabel();
	public ObjectResourceListBox typeBox = new ObjectResourceListBox();

	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField modifyField = new JTextField();

	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();


	public JButton saveButton = new JButton();

	public AccessPortTypeGeneralPanel()
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

	public AccessPortTypeGeneralPanel(AccessPortType apt)
	{
		this();
		setObjectResource(apt);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);

		saveButton.setText("menuMapSaveText");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		typesLabel.setText(LangModelConfig.getString("label_test_types"));
		typesLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		typeBox.setEnabled(true);
	 typeBox.setBorder(new BevelBorder(BevelBorder.LOWERED));
	 typeBox.setPreferredSize(new Dimension(-1,40));

		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idField.setEnabled(false);

	 modifyField.setEnabled(false);
	 modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
	 modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
	 modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

	 descLabel.setText(LangModelConfig.getString("label_description"));
	 descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 descriptionPanel.setLayout(borderLayout1);
	 descriptionScrollPane.getViewport().add(descTextArea, null);
	 descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

	 this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typesLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel1,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(modifyLabel2,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(descLabel,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(nameField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typeBox,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(modifyField,       new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descriptionPanel,  new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idField, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton,      new XYConstraints(200, 210, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
		return apt;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.apt = (AccessPortType)or;

		if(apt != null)
		{
			idField.setText(apt.getId());
			nameField.setText(apt.getName());
/*
		for (int i = 0; i < apt.test_type_ids.size(); i++)
		{
			TestType tt = (TestType)Pool.get(TestType.typ,(String)apt.test_type_ids.get(i));
			if (tt != null)
			 typeBox.add(tt);
		}
*/
			this.descTextArea.setText(apt.description);
			this.modifyField.setText(sdf.format(new Date(apt.modified)));
		}
		else
		{
			idField.setText("");
			nameField.setText("");
			descTextArea.setText("");
			modifyField.setText("");
		}

	}

	public boolean modify()
	{
	 try
	 {
		if(MiscUtil.validName(nameField.getText()))
			apt.name = nameField.getText();
		else
			return false;

		apt.id = idField.getText();
		apt.description = this.descTextArea.getText();
/*
		apt.test_type_ids.removeAllElements();
		for (int i = 0; i < this.typeBox.getModel().getSize(); i++)
			apt.test_type_ids.add(((TestType)this.typeBox.getModel().getElementAt(i)).id);
*/
	 }
	 catch(Exception ex)
	 {
		return false;
	 }
	 return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveAccessPort(ap.getId());
		}*/
	}
}
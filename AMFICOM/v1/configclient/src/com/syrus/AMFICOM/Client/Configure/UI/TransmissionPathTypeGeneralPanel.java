package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;


public class TransmissionPathTypeGeneralPanel extends GeneralPanel
{
	TransmissionPathType tpt;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private BorderLayout borderLayout1 = new BorderLayout();

	public JLabel idLabel = new JLabel();
	public JTextField idField = new JTextField();

	public JLabel nameLabel = new JLabel();
	public JTextField nameField = new JTextField();

	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField modifyField = new JTextField();

	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();


	public JButton saveButton = new JButton();

	public TransmissionPathTypeGeneralPanel()
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

	public TransmissionPathTypeGeneralPanel(TransmissionPathType apt)
	{
	 this();
	 setObjectResource(apt);
	}

	private void jbInit() throws Exception
	{
	 this.setLayout(gridBagLayout1);

	 saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
	 saveButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			saveButton_actionPerformed(e);
		}
	 });

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

	 this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel1,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(modifyLabel2,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(descLabel,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
	 return tpt;
	}

	public void setObjectResource(ObjectResource or)
	{
	 this.tpt = (TransmissionPathType)or;

	 if(tpt != null)
	 {
		idField.setText(tpt.getId());
		nameField.setText(tpt.getName());

		this.descTextArea.setText(tpt.description);
		this.modifyField.setText(sdf.format(new Date(tpt.modified)));
	 }
	 else
	 {
		idField.setText("");
		nameField.setText("");

		this.descTextArea.setText("");
		this.modifyField.setText("");
	 }
	}

	public boolean modify()
	{
	 try
	 {
		if(MyUtil.validName(nameField.getText()))
			tpt.name = nameField.getText();
		else
			return false;

		tpt.id = idField.getText();
		tpt.description = this.descTextArea.getText();
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
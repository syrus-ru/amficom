package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import oracle.jdeveloper.layout.*;

public class CableLinkPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	CableLinkGeneralPanel gPanel = new CableLinkGeneralPanel();
	CableLinkFibrePanel fPanel = new CableLinkFibrePanel();
	CableLinkCharacteristicsPanel chPanel = new CableLinkCharacteristicsPanel();

	CableLink link;

	public JTabbedPane tabbedPane = new JTabbedPane();

	//private JButton saveButton = new JButton();
	//private JPanel buttonsPanel = new JPanel();

	public CableLinkPane()
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

	public CableLinkPane(CableLink l)
	{
		this();
		setObjectResource(l);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(fPanel.getName(), fPanel);
		tabbedPane.add(chPanel.getName(), chPanel);

	/*	saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		buttonsPanel.add(saveButton, new XYConstraints(200, 487, -1, -1));*/
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.link = (CableLink )or;

		gPanel.setObjectResource(link);
		fPanel.setObjectResource(link);
		chPanel.setObjectResource(link);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		fPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
	 if (gPanel.modify() &&
				fPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveCableLink(link.getId());
			return true;
		}
		else
		{
			new MessageBox(LangModelConfig.getString("err_incorrect_data_input")).show();
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = link.id;
		aContext.getDataSourceInterface().RemoveCableLinks(s);

		return true;
	}

	public boolean create()
	{
		return false;
	}

//	void saveButton_actionPerformed(ActionEvent e)
//	{
//	}
}
package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import oracle.jdeveloper.layout.*;

public class CablePortTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	CablePortTypeGeneralPanel gPanel = new CablePortTypeGeneralPanel();
	CablePortTypeCharacteristicsPanel chPanel = new CablePortTypeCharacteristicsPanel();

	CablePortType portType;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	public CablePortTypePane()
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

	public CablePortTypePane(CablePortType pt)
	{
		this();
		setObjectResource(pt);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);

		saveButton.setText(LangModelConfig.String("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		buttonsPanel.add(saveButton, new XYConstraints(200, 487, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
		return portType;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.portType = (CablePortType)or;

		gPanel.setObjectResource(portType);
		chPanel.setObjectResource(portType);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if (gPanel.modify() &&
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
			String[] s = new String[1];
			s[0] = portType.getId();
			dataSource.SaveCablePortTypes(s);
			return true;
		}
		else
		{
			new MessageBox(LangModelConfig.String("err_incorrect_data_input")).show();
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = port.id;
		aContext.getDataSourceInterface().RemoveCablePorts(s);*/

		return true;
	}

	public boolean create()
	{
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}
package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import oracle.jdeveloper.layout.XYConstraints;

public class LinkPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	LinkGeneralPanel gPanel = new LinkGeneralPanel();
	LinkCharacteristicsPanel chPanel = new LinkCharacteristicsPanel();

	SchemeLink link;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	public LinkPane()
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

	public LinkPane(SchemeLink l)
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
		tabbedPane.add(chPanel.getName(), chPanel);

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		buttonsPanel.add(saveButton, new XYConstraints(200, 487, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.link = (SchemeLink )or;

		gPanel.setObjectResource(link);
		chPanel.setObjectResource(link);
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
			if (link.link != null)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();
				dataSource.SaveLink(link.link.getId());
				return true;
			}
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

		if (link.link != null)
		{
			String []s = new String[1];
			s[0] = link.link.getId();
			aContext.getDataSourceInterface().RemoveLinks(s);
		}

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
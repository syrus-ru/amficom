package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;

import oracle.jdeveloper.layout.XYConstraints;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.General.Checker;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Network.Link;

public class LinkPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	LinkGeneralPanel gPanel = new LinkGeneralPanel();
	LinkCharacteristicsPanel chPanel = new LinkCharacteristicsPanel();

	Link link;

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

	public LinkPane(Link l)
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

	public boolean setObjectResource(ObjectResource or)
	{
		this.link = (Link )or;

		gPanel.setObjectResource(link);
		chPanel.setObjectResource(link);
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
			dataSource.SaveLink(link.getId());
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
		aContext.getDataSourceInterface().RemoveLinks(s);

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
package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import oracle.jdeveloper.layout.XYConstraints;

public class LinkTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	LinkTypeGeneralPanel gPanel = new LinkTypeGeneralPanel();
	LinkTypeCharacteristicsPanel chPanel = new LinkTypeCharacteristicsPanel();

	LinkType linkType;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	public LinkTypePane()
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

	public LinkTypePane(LinkType lT)
	{
		this();
		setObjectResource(lT);
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
		return linkType;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.linkType = (LinkType) or;

		gPanel.setObjectResource(linkType);
		chPanel.setObjectResource(linkType);
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
			dataSource.SaveLinkTypes(new String[] {linkType.getId()});
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
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = linkType.id;
		aContext.getDataSourceInterface().RemoveLinkTypes(s);*/

		return true;
	}

	public boolean create()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return false;

		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
		dialog.setSize(dialog.preferredSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialog.getPreferredSize().width) / 2,
											 (screenSize.height - dialog.getPreferredSize().height) / 2);
		dialog.setVisible(true);

		if (dialog.getStatus() == dialog.OK && !dialog.getName().equals(""))
		{
			String name = dialog.getName();
			LinkType new_type = new LinkType();
			new_type.is_modified = true;
			new_type.name = name;
			new_type.linkClass = "fibre";
			new_type.modified = System.currentTimeMillis();
			new_type.id = aContext.getDataSourceInterface().GetUId(LinkType.typ);

			setObjectResource(new_type);

			Pool.put(LinkType.typ, new_type.getId(), new_type);
			return true;
		}
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}
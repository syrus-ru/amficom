package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import oracle.jdeveloper.layout.XYConstraints;

public class CableLinkTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	CableLinkTypeGeneralPanel gPanel = new CableLinkTypeGeneralPanel();
	CableLinkTypeFibrePanel fPanel = new CableLinkTypeFibrePanel();
	CableLinkTypeCharacteristicsPanel chPanel = new CableLinkTypeCharacteristicsPanel();

	CableLinkType linkType;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	public CableLinkTypePane()
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

	public CableLinkTypePane(CableLinkType l)
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
		this.linkType = (CableLinkType)or;

		gPanel.setObjectResource(linkType);
		fPanel.setObjectResource(linkType);
		chPanel.setObjectResource(linkType);
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
			String[] ltId = new String[1];
			ltId[0] = linkType.getId();
			dataSource.SaveCableLinkTypes(ltId);
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
	/*	if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		aContext.getDataSourceInterface().RemoveCableLinks(new String[] {linkType.getId()});
		Pool.remove(CableLinkType.typ, linkType.getId());
*/
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
			CableLinkType new_type = new CableLinkType();
			new_type.setChanged(true);
			new_type.name = name;
			new_type.linkClass = "cable";
			new_type.modified = System.currentTimeMillis();
			new_type.id = aContext.getDataSourceInterface().GetUId(CableLinkType.typ);

			setObjectResource(new_type);

			Pool.put(CableLinkType.typ, new_type.getId(), new_type);
			return true;
		}
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}

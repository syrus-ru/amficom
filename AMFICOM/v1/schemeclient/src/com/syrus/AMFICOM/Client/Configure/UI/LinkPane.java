package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;

public abstract class LinkPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;

	protected LinkGeneralPanel gPanel;
	protected LinkCharacteristicsPanel chPanel;

	protected JTabbedPane tabbedPane = new JTabbedPane();

	protected SchemeLink link;

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

	public LinkPane(SchemeLink link)
	{
		this();
		setObject(link);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		gPanel = new LinkGeneralPanel();
		chPanel = new LinkCharacteristicsPanel();

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public Object getObject()
	{
		return link;
	}

	public void setObject(Object or)
	{
		link = (SchemeLink)or;

		gPanel.setObject(link);
		chPanel.setObject(link);
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
		if(modify())
		{
			if (link.link() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(link.linkImpl());
				}
				catch (ApplicationException ex) {
				}
				return true;
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean create()
	{
		return false;
	}
}
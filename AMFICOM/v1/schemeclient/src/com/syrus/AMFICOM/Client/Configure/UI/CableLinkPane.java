package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

public abstract class CableLinkPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;

	protected CableLinkGeneralPanel gPanel;
	protected CableLinkCharacteristicsPanel chPanel;
	protected CableLinkFibrePanel fPanel;

	protected JTabbedPane tabbedPane = new JTabbedPane();

	protected SchemeCableLink link;

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

	public CableLinkPane(SchemeCableLink link)
	{
		this();
		setObject(link);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		fPanel = new CableLinkFibrePanel();
		gPanel = new CableLinkGeneralPanel();
		chPanel = new CableLinkCharacteristicsPanel();

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(fPanel.getName(), fPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public Object getObject()
	{
		return link;
	}

	public void setObject(Object or)
	{
		link = (SchemeCableLink)or;

		gPanel.setObject(link);
		fPanel.setObject(link);
		chPanel.setObject(link);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
		fPanel.setContext(aContext);
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

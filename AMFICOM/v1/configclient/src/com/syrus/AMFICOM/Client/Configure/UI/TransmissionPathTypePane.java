package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.Checker;

public class TransmissionPathTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	TransmissionPathTypeGeneralPanel gPanel = new TransmissionPathTypeGeneralPanel();

	TransmissionPathType pathType;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public TransmissionPathTypePane()
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

	public TransmissionPathTypePane(TransmissionPathType tp)
	{
		this();
		setObjectResource(tp);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
	}

	public ObjectResource getObjectResource()
	{
		return pathType;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.pathType = (TransmissionPathType)or;

		gPanel.setObjectResource(pathType);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SavePath(pathType.getId());
			return true;
//			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, tp.monitored_element_id);
//			dataSource.savem
		}
		else
		{
			new MessageBox("Неправильно введены данные").show();
		}*/
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
				Checker.catalogCMediting))
			return false;

		String []s = new String[1];

		s[0] = path.id;
		aContext.getDataSourceInterface().RemovePaths(s);
*/
		return true;
	}

	public boolean create()
	{
		return false;
	}
}
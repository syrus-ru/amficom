package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;

public class TransmissionPathPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	TransmissionPathGeneralPanel gPanel = new TransmissionPathGeneralPanel();
	TransmissionPathFibrePanel fPanel = new TransmissionPathFibrePanel();

	TransmissionPath path;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public TransmissionPathPane()
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

	public TransmissionPathPane(TransmissionPath tp)
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
		tabbedPane.add(fPanel.getName(), fPanel);

		tabbedPane.setEnabledAt(1, false);
	}

	public ObjectResource getObjectResource()
	{
		return path;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.path = (TransmissionPath )or;

		gPanel.setObjectResource(path);
		fPanel.setObjectResource(path);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		fPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SavePath(path.getId());
			return true;
//			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, tp.monitored_element_id);
//			dataSource.savem
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
				Checker.catalogCMediting))
			return false;

		String []s = new String[1];

		s[0] = path.id;
		aContext.getDataSourceInterface().RemovePaths(s);

		return true;
	}

	public boolean create()
	{
		return false;
	}
}
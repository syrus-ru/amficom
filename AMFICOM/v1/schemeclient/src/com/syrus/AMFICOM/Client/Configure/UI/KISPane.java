package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

public class KISPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	KISGeneralPanel gPanel = new KISGeneralPanel();
	KISGeneralPanelAdd gaPanel = new KISGeneralPanelAdd();
	KISPortsPanel pPanel = new KISPortsPanel();
	KISCablePortsPanel cpPanel = new KISCablePortsPanel();
	KISCharacteristicsPanel chPanel = new KISCharacteristicsPanel();

	SchemeElement element;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public KISPane()
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

	public KISPane(SchemeElement element)
	{
		this();
		setObjectResource(element);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(gaPanel.getName(), gaPanel);
		tabbedPane.add(pPanel.getName(), pPanel);
		tabbedPane.add(cpPanel.getName(), cpPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return element;
	}

	public void setObjectResource(ObjectResource or)
	{
		element = (SchemeElement)or;

		gPanel.setObjectResource(element);
		gaPanel.setObjectResource(element);
		pPanel.setObjectResource(element);
		cpPanel.setObjectResource(element);
		chPanel.setObjectResource(element);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		gaPanel.setContext(aContext);
		pPanel.setContext(aContext);
		cpPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(	gPanel.modify() &&
				gaPanel.modify() &&
				pPanel.modify() &&
				cpPanel.modify() &&
				chPanel.modify())
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
			if (element.kis != null)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();
				dataSource.SaveKIS(element.kis.getId());
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
				Checker.catalogCMediting))
			return false;
/*
		Collection measurementPorts = element.getMeasurementPorts();
		if (measurementPorts.size() != 0)
		{
			String []sa = new String[measurementPorts.size()];
			Iterator it = measurementPorts.iterator();
			for(int i = 0 ; i < sa.length; i++)
				sa[i] = ((MeasurementPort)it.next()).getId();
			aContext.getDataSourceInterface().RemoveAccessPorts(sa);
		}


		Collection ports = element.getPorts();
		if (ports.size() != 0)
		{
			String []sa = new String[ports.size()];
			Iterator it = ports.iterator();
			for(int i = 0 ; i < sa.length; i++)
				sa[i] = ((SchemePort)it.next()).getId();
			aContext.getDataSourceInterface().RemoveAccessPorts(sa);
		}

		aContext.getDataSourceInterface().RemovePorts(sp);

		i = 0;
		String []sc = new String[equipment.cports.size()];
		for(Iterator it = equipment.cports.iterator(); it.hasNext();)
		{
			CablePort port = (CablePort)it.next();
			sc[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemoveCablePorts(sc);

		String []se = new String[1];
		se[0] = equipment.id;
		aContext.getDataSourceInterface().RemoveKISs(se);
*/
		return true;
	}

	public boolean create()
	{
		return false;
	}
}
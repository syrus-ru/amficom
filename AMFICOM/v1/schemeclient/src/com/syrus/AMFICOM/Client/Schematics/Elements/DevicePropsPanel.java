package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.Iterator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class DevicePropsPanel extends JPanel
{
	SchemeDevice dev;
	ApplicationContext aContext;

	public DevicePropsPanel(ApplicationContext aContext)
	{
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception
	{

	}

	public void setEditable(boolean b)
	{
	}

	public void init(SchemeDevice dev, DataSourceInterface dataSource, boolean show_is_kis)
	{
		this.dev = dev;
	}

	public void undo()
	{
	}

}

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTablePane;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JPanel;

import oracle.jdeveloper.layout.XYConstraints;

public class MapEquipmentAttributesPanel extends GeneralPanel 
{
	MapEquipmentNodeElement mapequipment;

	private JPanel jPanel1 = new JPanel();
	ObjectResourcePropertyTablePane charPane = new ObjectResourcePropertyTablePane();
	private BorderLayout borderLayout1 = new BorderLayout();
	
	public MapEquipmentAttributesPanel()
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
		charPane.initialize(new String[] {"Атрибут", "Значение"}, null);
	}

	public MapEquipmentAttributesPanel(MapEquipmentNodeElement mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}

	private void jbInit() throws Exception
	{
		setName("Атрибуты");
	
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setLayout(borderLayout1);
//		jPanel1.add(charPane, null);
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return mapequipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.mapequipment = (MapEquipmentNodeElement )or;

//		System.out.println("set prop pane to " + mapequipment.name);

		charPane.setSelected(or);
	}

	public boolean modify()
	{
		return false;
	}

}
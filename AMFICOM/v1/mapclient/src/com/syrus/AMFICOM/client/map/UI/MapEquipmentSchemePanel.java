package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.text.SimpleDateFormat;

import java.util.Date;

public class MapEquipmentSchemePanel extends GeneralPanel 
{
	MapEquipmentNodeElement mapequipment;

//	JScrollPane charScrollPane = new JScrollPane();
//	JTable charTable = new JTable();

//	PropertyTableModel tableModel = new PropertyTableModel();
//	SchemeElementsPanel schPane = new SchemeElementsPanel(aContext);
	
	public MapEquipmentSchemePanel()
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

	public MapEquipmentSchemePanel(MapEquipmentNodeElement mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}

	private void jbInit() throws Exception
	{
		setName("Компонент");
	
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
//		this.add(schPane, new XYConstraints(10, 20, 395, 430));
	}

	public ObjectResource getObjectResource()
	{
		return mapequipment;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mapequipment = (MapEquipmentNodeElement )or;

//		System.out.println("set prop pane to " + mapequipment.name);

		SchemeElement se = null;
		if(mapequipment.element_id != null && !mapequipment.element_id.equals(""))
			se = (SchemeElement )Pool.get(SchemeElement.typ, mapequipment.element_id);

//		schPane.setElement(se);
		return true;
	}

	public boolean modify()
	{
		return false;
	}

}
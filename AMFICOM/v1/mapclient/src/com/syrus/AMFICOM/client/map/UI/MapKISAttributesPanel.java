package com.syrus.AMFICOM.Client.Configure.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTablePane;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

public class MapKISAttributesPanel extends GeneralPanel 
{
	MapKISNodeElement mapkis;

//	JScrollPane charScrollPane = new JScrollPane();
//	JTable charTable = new JTable();

//	PropertyTableModel tableModel = new PropertyTableModel();
	ObjectResourcePropertyTablePane charPane = new ObjectResourcePropertyTablePane();
	
	public MapKISAttributesPanel()
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

	public MapKISAttributesPanel(MapKISNodeElement mapkis)
	{
		this();
		setObjectResource(mapkis);
	}

	private void jbInit() throws Exception
	{
		setName("Атрибуты");
	
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
//		charScrollPane.getViewport().add(charTable, null);

		this.setLayout(new BorderLayout());
//		charScrollPane.getViewport().add(charTable);
//		this.add(charScrollPane, new XYConstraints(10, 20, 395, 430));
//		this.add(charPane, new XYConstraints(10, 20, 395, 430));
		this.add(charPane, BorderLayout.CENTER);
	}
/*
	public void setTableModel(PropertyTableModel myModel)
	{
		charTable.setModel( myModel);
		charTable.setDefaultEditor(
			myModel.getColumnClass(1),
			new PropertyTableEditor(myModel));
		charTable.setDefaultRenderer(
			myModel.getColumnClass(1),
			new PropertyTableRenderer(myModel));
		charTable.doLayout();
		charTable.getColumnModel().getColumn(0).setPreferredWidth(50);
	}

	void tableInit()
	{
		tableModel = new PropertyTableModel(this);
		setTableModel( tableModel);
	}
*/
	public ObjectResource getObjectResource()
	{
		return mapkis;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mapkis = (MapKISNodeElement )or;

//		System.out.println("set prop pane to " + mapkis.name);

//		tableInit();
//		tableModel.setObject(or);
		charPane.setSelected(or);
		return true;

	}

	public boolean modify()
	{
		return false;
	}

}
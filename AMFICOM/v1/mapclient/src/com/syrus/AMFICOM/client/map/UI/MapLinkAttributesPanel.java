package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTablePane;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

public class MapLinkAttributesPanel extends GeneralPanel 
{
	MapPhysicalLinkElement maplink;

//	JScrollPane charScrollPane = new JScrollPane();
//	JTable charTable = new JTable();

//	PropertyTableModel tableModel = new PropertyTableModel();
	ObjectResourcePropertyTablePane charPane = new ObjectResourcePropertyTablePane();
	
	public MapLinkAttributesPanel()
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

	public MapLinkAttributesPanel(MapPhysicalLinkElement maplink)
	{
		this();
		setObjectResource(maplink);
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
		return maplink;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.maplink = (MapPhysicalLinkElement )or;

//		System.out.println("set prop pane to " + maplink.name);

//		tableInit();
//		tableModel.setObject(or);
		charPane.setSelected(or);
	}

	public boolean modify()
	{
		return false;
	}

}
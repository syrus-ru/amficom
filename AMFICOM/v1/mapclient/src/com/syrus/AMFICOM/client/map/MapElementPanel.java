package com.syrus.AMFICOM.Client.Map;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import java.awt.event.*;

//Данный класс изображает панель на которой находится jComboBox1 со списком
//видов элементов и талица элементов с полями "Идентификатор" и "Название"

public class MapElementPanel extends JPanel
{
	MapContext mapContext;
	ApplicationContext aContext;

	JComboBox typeComboBox = new AComboBox();
	ObjectResourceTablePane tablePane;
//	JScrollPane jScrollPane1;
	boolean mouseSelect = true;
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();

	public boolean perform_processing = true;

	public MapElementPanel(MapContext myMapContext)
	{
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		setMapContext(myMapContext);
  }
  
	void jbInit() throws Exception
	{
		typeComboBox.addItem(MapEquipmentNodeElement.typ);
		typeComboBox.addItem(MapPhysicalLinkElement.typ);
		typeComboBox.addItem(MapPhysicalNodeElement.typ);
		typeComboBox.addItem(MapMarkElement.typ);
		typeComboBox.addItem(MapTransmissionPathElement.typ);
		typeComboBox.addItem(MapMarker.typ);

		typeComboBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jComboBox1_actionPerformed(e);
			}
		});

		typeComboBox.setRenderer(new MyRenderer());

		tablePane = new ObjectResourceTablePane();
		tablePane.initialize(
				new StubDisplayModel(
					new String[] {"name", "type_id"}, 
					new String[] {"Название", "Тип"} ), 
//					new String[] {"id", "name"}, 
//					new String[] {"Идентификатор", "Название"} ), 
				new LinkedList());

		setEnableDisable(false);//устанавливаем в режим false
		this.setLayout(borderLayout2);

		JTable myTable = tablePane.getTable();

		myTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		myTable.getSelectionModel().addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					myTable_selectionChanged();
				}
			}
		);

		jPanel1.setLayout(borderLayout1);
		jPanel1.add(typeComboBox,  BorderLayout.CENTER);
//		jScrollPane1 = new JScrollPane(tablePane);
//		this.add(jScrollPane1, BorderLayout.CENTER);
		this.add(tablePane, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.NORTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	void jComboBox1_actionPerformed(ActionEvent e)
	{
		updateTable();
	}

	public void setMapContext(MapContext myMapContext)
	{
		mapContext = myMapContext;
		setEnableDisable(true);
		updateTable();
	}

	void myTable_selectionChanged()
	{
		JTable myTable = tablePane.getTable();
		try
		{
		 //Если поле выбрано мышью
			if ( mouseSelect == true)
			{
					//Отоьражаем элементы
				MapElement mapElement = new VoidMapElement(mapContext.getLogicalNetLayer());

				Dispatcher disp = null;
				if(aContext != null)
					disp = aContext.getDispatcher();

				for (int i = 0; i < tablePane.getContents().size(); i++)
				{
					MapElement mapE = (MapElement )tablePane.getContents().get(i);
					if(disp != null)
					{
						perform_processing = false;
						disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
						perform_processing = true;
					}
//					mapE.deselect();
				}

				for (int i = 0; i < myTable.getSelectedRows().length; i++)
				{
					MapElement mapE = (MapElement )tablePane.getObjectAt(myTable.getSelectedRows()[i]);
					if(disp != null)
					{
						perform_processing = false;
						disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
						mapContext.notifySchemeEvent(mapE);
						mapContext.notifyCatalogueEvent(mapE);
						perform_processing = true;
					}
//					mapE.select();
				}
				mouseSelect = true;
			}
			mapContext.getLogicalNetLayer().postDirtyEvent();
			mapContext.getLogicalNetLayer().postPaintEvent();
		}
		catch(Exception e)
		{
		}
	}

	public void updateTable()
	{
		JTable myTable = tablePane.getTable();
		//Оновить таблицу
		mouseSelect = false;
		//Здесь очищаем выбранные элементы у табли
		myTable.clearSelection();
		mouseSelect = true;
		String selection = (String )typeComboBox.getSelectedItem();
		List dataSet = new LinkedList();

		if(mapContext != null)
		{
			if(selection.equals(MapEquipmentNodeElement.typ))
				dataSet = new DataSet(mapContext.getMapEquipmentNodeElements());
			if(selection.equals(MapPhysicalLinkElement.typ))
				dataSet = new DataSet(mapContext.getPhysicalLinks());
			if(selection.equals(MapTransmissionPathElement.typ))
				dataSet = new DataSet(mapContext.getTransmissionPath());
			if(selection.equals(MapPhysicalNodeElement.typ))
				dataSet = new DataSet(mapContext.getMapPhysicalNodeElements());
			if(selection.equals(MapMarkElement.typ))
				dataSet = new DataSet(mapContext.getMapMarkElements());
			if(selection.equals(MapMarker.typ))
				dataSet = new DataSet(mapContext.markers);
		}

		tablePane.setContents(dataSet);
		setSelectedObjects();
	}

//A0A
	public void setSelectedObjects()
	{
	try
	{
		JTable myTable = tablePane.getTable();
		//Оновить таблицу
		mouseSelect = false;
		//Здесь очищаем выбранные элементы у табли
		myTable.clearSelection();
		String selection = (String )typeComboBox.getSelectedItem();
		List dataSet = tablePane.getContents();
		for(int i = 0; i < dataSet.size(); i++)
		{
			MapElement me = (MapElement )dataSet.get(i);
			if(me.isSelected())
	            myTable.getSelectionModel().addSelectionInterval(i, i);
		}
		mouseSelect = true;
	}
    catch(Exception e)
    {
    }
	}

	public void setEnableDisable( boolean b)
	{
		JTable myTable = tablePane.getTable();

		typeComboBox.setEnabled( b);
		myTable.setEnabled( b);

		if ( b == false )
		{
			tablePane.setContents(new LinkedList());
		}
	}

}

class MyRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
	{
		String text = LangModel.String("node" + (String )value);
		return super.getListCellRendererComponent(
			list, text, index, isSelected, cellHasFocus);
	}
}

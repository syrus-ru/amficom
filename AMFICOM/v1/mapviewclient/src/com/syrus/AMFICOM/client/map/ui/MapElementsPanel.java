/**
 * $Id: MapElementsPanel.java,v 1.6 2004/11/01 15:40:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Данный класс изображает панель на которой находится ComboBox со списком
 * видов элементов и талица элементов с полями "Идентификатор" и "Название"
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/11/01 15:40:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapElementsPanel extends JPanel
{
	Map map;
	MapView mapView;
	ApplicationContext aContext;
	
	LogicalNetLayer logicalNetLayer;

	JComboBox typeComboBox = new AComboBox();

	/**
	 * модель отображения найденных элементов
	 */
	ObjectResourceController controller;

	/**
	 * таблица с найденными элементами
	 */
	ObjectResourceTable table;

	/**
	 * модель для таблицы найденных элементов
	 */
	ObjectResourceTableModel model;

	JScrollPane scrollPane = new JScrollPane();

	boolean mouseSelect = true;
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();

	protected boolean performProcessing = true;

	protected boolean doNotify = true;
	
	public MapElementsPanel(LogicalNetLayer logicalNetLayer)
	{
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		setLogicalNetLayer(logicalNetLayer);
  }
  
	void jbInit()
	{
		typeComboBox.addItem(MapSiteNodeElement.typ);
		typeComboBox.addItem("mapwellnode");
		typeComboBox.addItem("mappiquetnode");
		typeComboBox.addItem(MapPhysicalLinkElement.typ);
		typeComboBox.addItem(MapPipePathElement.typ);
		typeComboBox.addItem(MapCablePathElement.typ);
//		typeComboBox.addItem(SchemeElement.typ);
		typeComboBox.addItem(MapPhysicalNodeElement.typ);
		typeComboBox.addItem(MapMarkElement.typ);
		typeComboBox.addItem(MapMeasurementPathElement.typ);
		typeComboBox.addItem(MapMarker.typ);

		typeComboBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				listSelectionChanged();
			}
		});

		typeComboBox.setRenderer(new MapElementsPanel.MapElementPanelRenderer());

		controller = SimpleMapElementController.getInstance();
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

		scrollPane.getViewport().add(table);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

		setEnabled(false);
		this.setLayout(borderLayout2);

		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		table.getSelectionModel().addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					tableSelectionChanged();
				}
			}
		);

		jPanel1.setLayout(borderLayout1);
		jPanel1.add(typeComboBox,  BorderLayout.CENTER);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.NORTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	void listSelectionChanged()
	{
		updateTable();
	}

	public void setMap(Map map)
	{
		this.map = map;
		setEnabled(map != null);
		updateTable();
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
		setEnabled(mapView != null);
		updateTable();
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		if(logicalNetLayer != null)
		{
			mapView = logicalNetLayer.getMapView();
			map = mapView.getMap();
		}
		setEnabled(logicalNetLayer != null);
		updateTable();
	}

	void tableSelectionChanged()
	{
		try
		{
			Dispatcher disp = null;
			if(aContext != null)
				disp = aContext.getDispatcher();

			//Если поле выбрано мышью
			if (mouseSelect
				&& doNotify
				&& disp != null)
			{

				for(Iterator it = model.getContents().iterator(); it.hasNext();)
				{
					MapElement mapE = (MapElement )it.next();
					performProcessing = false;
					disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
					performProcessing = true;
				}

				for (int i = 0; i < table.getSelectedRows().length; i++)
				{
					MapElement mapE = (MapElement )model.getObject(table.getSelectedRows()[i]);

					performProcessing = false;
					disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					logicalNetLayer.notifySchemeEvent(mapE);
					performProcessing = true;
				}

				disp.notify(new MapEvent(this, MapNavigateEvent.MAP_CHANGED));
			}
		}
		catch(Exception e)
		{
		}
	}

	public void updateTable()
	{
		//Оновить таблицу
		mouseSelect = false;
		//Здесь очищаем выбранные элементы у табли
		table.clearSelection();

		String selection = (String )typeComboBox.getSelectedItem();
		List dataSet = new LinkedList();

		if(map != null)
		{
			if(selection.equals(MapSiteNodeElement.typ))
			{
				for(Iterator it = map.getMapSiteNodeElements().iterator(); it.hasNext();)
				{
					MapSiteNodeElement site = (MapSiteNodeElement )it.next();
					if(		!site.getMapProtoId().equals(MapNodeProtoElement.WELL)
						&&	!site.getMapProtoId().equals(MapNodeProtoElement.PIQUET)
						&&	!site.getMapProtoId().equals(MapNodeProtoElement.WELL) )
							dataSet.add(site);
				}
			}
			else
			if(selection.equals("mapwellnode"))
			{
				for(Iterator it = map.getMapSiteNodeElements().iterator(); it.hasNext();)
				{
					MapSiteNodeElement site = (MapSiteNodeElement )it.next();
					if(site.getMapProtoId().equals(MapNodeProtoElement.WELL))
						dataSet.add(site);
				}
			}
			else
			if(selection.equals("mappiquetnode"))
			{
				for(Iterator it = map.getMapSiteNodeElements().iterator(); it.hasNext();)
				{
					MapSiteNodeElement site = (MapSiteNodeElement )it.next();
					if(site.getMapProtoId().equals(MapNodeProtoElement.PIQUET))
						dataSet.add(site);
				}
			}
			else
			if(selection.equals(MapPhysicalLinkElement.typ))
				dataSet = map.getPhysicalLinks();
			else
			if(selection.equals(MapMeasurementPathElement.typ))
				dataSet = mapView.getMeasurementPaths();
			else
			if(selection.equals(MapPhysicalNodeElement.typ))
				dataSet = map.getMapPhysicalNodeElements();
			else
			if(selection.equals(MapMarkElement.typ))
				dataSet = map.getMapMarkElements();
			else
			if(selection.equals(MapMarker.typ))
				dataSet = mapView.getMarkers();
			else
			if(selection.equals(MapPipePathElement.typ))
				dataSet = map.getCollectors();
			else
			if(selection.equals(MapCablePathElement.typ))
				dataSet = mapView.getCablePaths();
//			else
//			if(selection.equals(SchemeElement.typ))
//			{
//				for(Iterator it = map.getMapSiteNodeElements().iterator(); it.hasNext();)
//				{
//					MapSiteNodeElement site = (MapSiteNodeElement )it.next();
//					if(
//				}
//				dataSet = mapView.getPaths();
//			}
		}

		model.setContents(dataSet);

		mouseSelect = true;

		setSelectedObjects();
	}

	public void setSelectedObjects()
	{
		try
		{
			//Оновить таблицу
			mouseSelect = false;
			//Здесь очищаем выбранные элементы у табли
			table.clearSelection();
	
			List dataSet = model.getContents();
			int i = 0;
			for(Iterator it = dataSet.iterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				if(me.isSelected())
					table.getSelectionModel().addSelectionInterval(i, i);
				i++;
			}
			mouseSelect = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setEnabled( boolean b)
	{
		typeComboBox.setEnabled(b);
		table.setEnabled(b);

		if (!b)
		{
			model.clear();
		}
	}

	private class MapElementPanelRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			String text = LangModelMap.getString((String )value);
			return super.getListCellRendererComponent(
				list, text, index, isSelected, cellHasFocus);
		}
	}
}


/**
 * $Id: MapElementsPanel.java,v 1.21 2005/04/22 11:37:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.Collection;
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

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Данный класс изображает панель на которой находится ComboBox со списком
 * видов элементов и талица элементов с полями "Идентификатор" и "Название"
 * 
 * 
 * @version $Revision: 1.21 $, $Date: 2005/04/22 11:37:27 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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
		this.typeComboBox.addItem(MapViewController.ELEMENT_SITENODE);
		this.typeComboBox.addItem(MapViewController.ELEMENT_WELL);
		this.typeComboBox.addItem(MapViewController.ELEMENT_PIQUET);
		this.typeComboBox.addItem(MapViewController.ELEMENT_PHYSICALLINK);
		this.typeComboBox.addItem(MapViewController.ELEMENT_COLLECTOR);
		this.typeComboBox.addItem(MapViewController.ELEMENT_CABLEPATH);
//		this.typeComboBox.addItem(SchemeElement.typ);
		this.typeComboBox.addItem(MapViewController.ELEMENT_TOPOLOGICALNODE);
		this.typeComboBox.addItem(MapViewController.ELEMENT_MARK);
		this.typeComboBox.addItem(MapViewController.ELEMENT_MEASUREMENTPATH);
		this.typeComboBox.addItem(MapViewController.ELEMENT_MARKER);

		this.typeComboBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				listSelectionChanged();
			}
		});

		this.typeComboBox.setRenderer(new MapElementsPanel.MapElementPanelRenderer());

		this.controller = SimpleMapElementController.getInstance();
		this.model = new ObjectResourceTableModel(this.controller);
		this.table = new ObjectResourceTable(this.model);

		this.scrollPane.getViewport().add(this.table);

		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);

		setEnabled(false);
		this.setLayout(this.borderLayout2);

		this.table.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.table.getSelectionModel().addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					tableSelectionChanged();
				}
			}
		);

		this.jPanel1.setLayout(this.borderLayout1);
		this.jPanel1.add(this.typeComboBox,  BorderLayout.CENTER);
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.add(this.jPanel1, BorderLayout.NORTH);
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
			this.mapView = logicalNetLayer.getMapView();
			this.map = this.mapView.getMap();
		}
		setEnabled(logicalNetLayer != null);
		updateTable();
	}

	void tableSelectionChanged()
	{
		try
		{
			Dispatcher dispatcher = null;
			if(this.aContext != null)
				dispatcher = this.aContext.getDispatcher();

			//Если поле выбрано мышью
			if (this.mouseSelect
				&& this.doNotify
				&& dispatcher != null)
			{

				for(Iterator it = this.model.getContents().iterator(); it.hasNext();)
				{
					MapElement mapElement = (MapElement)it.next();
					this.performProcessing = false;
					dispatcher.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
					this.performProcessing = true;
				}

				for (int i = 0; i < this.table.getSelectedRows().length; i++)
				{
					MapElement mapElement = (MapElement)this.model.getObject(this.table.getSelectedRows()[(i)]);

					this.performProcessing = false;
					dispatcher.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					this.logicalNetLayer.notifySchemeEvent(mapElement);
					this.performProcessing = true;
				}

				dispatcher.notify(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateTable()
	{
		//Оновить таблицу
		this.mouseSelect = false;
		//Здесь очищаем выбранные элементы у табли
		this.table.clearSelection();

		String selection = (String )this.typeComboBox.getSelectedItem();
		Collection elements = new LinkedList();

		if(this.map != null && this.logicalNetLayer != null)
		{
			SiteNodeType well = NodeTypeController.getSiteNodeType(SiteNodeType.WELL);
			SiteNodeType piquet = NodeTypeController.getSiteNodeType(SiteNodeType.PIQUET);
//			SiteNodeType cableinlet = NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.CABLE_INLET);

			if(selection.equals(MapViewController.ELEMENT_SITENODE))
			{
				for(Iterator it = this.map.getAllSiteNodes().iterator(); it.hasNext();)
				{
					SiteNode site = (SiteNode)it.next();
					if(		!site.getType().equals(well)
						&&	!site.getType().equals(piquet) )
							elements.add(site);
				}
			}
			else
			if(selection.equals(MapViewController.ELEMENT_WELL))
			{
				for(Iterator it = this.map.getAllSiteNodes().iterator(); it.hasNext();)
				{
					SiteNode site = (SiteNode)it.next();
					if(site.getType().equals(well))
						elements.add(site);
				}
			}
			else
			if(selection.equals(MapViewController.ELEMENT_PIQUET))
			{
				for(Iterator it = this.map.getAllSiteNodes().iterator(); it.hasNext();)
				{
					SiteNode site = (SiteNode)it.next();
					if(site.getType().equals(piquet))
						elements.add(site);
				}
			}
			else
			if(selection.equals(MapViewController.ELEMENT_PHYSICALLINK))
				elements = this.map.getAllPhysicalLinks();
			else
			if(selection.equals(MapViewController.ELEMENT_MEASUREMENTPATH))
				elements = this.logicalNetLayer.getMapView().getMeasurementPaths();
			else
			if(selection.equals(MapViewController.ELEMENT_TOPOLOGICALNODE))
				elements = this.map.getAllTopologicalNodes();
			else
			if(selection.equals(MapViewController.ELEMENT_MARK))
				elements = this.map.getAllMarks();
			else
			if(selection.equals(MapViewController.ELEMENT_MARKER))
				elements = this.logicalNetLayer.getMapView().getMarkers();
			else
			if(selection.equals(MapViewController.ELEMENT_COLLECTOR))
				elements = this.map.getAllCollectors();
			else
			if(selection.equals(MapViewController.ELEMENT_CABLEPATH))
				elements = this.logicalNetLayer.getMapView().getCablePaths();
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

		this.model.setContents(elements);

		this.mouseSelect = true;

		setSelectedObjects();
	}

	public void setSelectedObjects()
	{
		try
		{
			//Оновить таблицу
			this.mouseSelect = false;
			//Здесь очищаем выбранные элементы у табли
			this.table.clearSelection();
	
			List dataSet = this.model.getContents();
			int i = 0;
			for(Iterator it = dataSet.iterator(); it.hasNext();)
			{
				MapElement me = (MapElement)it.next();
				if(me.isSelected())
					this.table.getSelectionModel().addSelectionInterval(i, i);
				i++;
			}
			this.mouseSelect = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setEnabled( boolean b)
	{
		this.typeComboBox.setEnabled(b);
		this.table.setEnabled(b);

		if (!b)
		{
			this.model.clear();
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


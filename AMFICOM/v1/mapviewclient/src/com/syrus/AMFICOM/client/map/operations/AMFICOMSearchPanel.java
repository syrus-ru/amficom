/*
 * Название: $Id: AMFICOMSearchPanel.java,v 1.13 2005/06/21 11:30:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.operations;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.command.navigate.CenterSelectionCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Панель поиска элементов карты АМФИКОМ
 * @version $Revision: 1.13 $, $Date: 2005/06/21 11:30:33 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class AMFICOMSearchPanel extends JPanel
{
	JTextField searchField = new JTextField();
	JButton searchButton = new JButton();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane scrollPane = new JScrollPane();

	/**
	 * модель отображения найденных элементов
	 */
	SimpleMapElementController controller;

	/**
	 * таблица с найденными элементами
	 */
	WrapperedTable table;

	/**
	 * модель для таблицы найденных элементов
	 */
	WrapperedTableModel model;

	/**
	 * строка поиска
	 */
	String searchText = "";
	
	/**
	 * флаг поиска (осуществляется ли поиск в текущий момент)
	 */
	private boolean searching = false;
	
	private MapFrame mapFrame;
	private MapView mapView = null;
	
	JButton centerButton = new JButton();
	
	/**
	 * По умолчанию
	 */
	public AMFICOMSearchPanel()
	{
		this.controller = SimpleMapElementController.getInstance();
		this.model = new WrapperedTableModel(this.controller, this.controller.getKeysArray());
		this.table = new WrapperedTable(this.model);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit()
	{
		this.setToolTipText(LangModelMap.getString("SearchObjects"));

		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));

		this.searchButton.setText(LangModelMap.getString("Search"));
		this.searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doSearch();
				}
			});

		this.centerButton.setText(LangModelMap.getString("DoCenter"));
		this.centerButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doCenter();
				}
			});

		this.searchField.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						doSearch();
				}
				public void keyReleased(KeyEvent e) {/*empty*/}
				public void keyTyped(KeyEvent e) {/*empty*/}
			});

		JTableHeader jth = this.table.getTableHeader();
		this.scrollPane.getViewport().add(jth);
		this.scrollPane.getViewport().add(this.table);

		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);
		
		this.add(this.searchField, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.searchButton, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		this.add(jth, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.scrollPane, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.centerButton, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.table.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e)
			{
				if (e.getClickCount() > 1)
					AMFICOMSearchPanel.this.doCenter();
			}
		});
		
	}

	public void setMapView(MapView mv)
	{
		if(mv != null)
			this.mapView = mv;
	}

	public void setMapFrame(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
		if(mapFrame == null)
		{
			this.table.removeAll();
			this.mapView = null;
		}
		else
			this.mapView = this.mapFrame.getMapView();
	}
	
	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	/**
	 * запуск операции поиска по нажатию на кнопку. задача поиска запускается
	 * отдельным thread'ом
	 */
	void doSearch()
	{
		if(this.searching)
			return;
		if(this.mapFrame == null)
			return;
			
		this.searchText = this.searchField.getText();
		if(this.searchText.length() == 0)
			return;
		this.searching = true;
		this.table.removeAll();
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				search();
			}
		});
		t.start();
	}

	static class NodeTypeComparator implements Comparator {

		static NodeTypeComparator instance = new NodeTypeComparator();
		
		private NodeTypeComparator() {
			// empty
		}
		
		public int compare(Object o1, Object o2) {
			SiteNode site1 = (SiteNode )o1;
			SiteNode site2 = (SiteNode )o2;
			SiteNodeType type1 = (SiteNodeType )site1.getType();
			SiteNodeType type2 = (SiteNodeType )site2.getType();
			if(type1.equals(type2))
				return site1.getName().compareTo(site2.getName());
			return type1.getName().compareTo(type2.getName());
		}
	}
	
	static class LinkTypeComparator implements Comparator {

		static LinkTypeComparator instance = new LinkTypeComparator();
		
		private LinkTypeComparator() {
			// empty
		}
		
		public int compare(Object o1, Object o2) {
			PhysicalLink link1 = (PhysicalLink )o1;
			PhysicalLink link2 = (PhysicalLink )o2;
			PhysicalLinkType type1 = (PhysicalLinkType )link1.getType();
			PhysicalLinkType type2 = (PhysicalLinkType )link2.getType();
			if(type1.equals(type2))
				return link1.getName().compareTo(link2.getName());
			return type1.getName().compareTo(type2.getName());
		}
	}
	/**
	 * просмотр всех элементов карты и поиск среди них объектов,
	 * имя которых сооветствует заданному шаблону поиска
	 */	
	public void search()
	{
		String loweredSearchText = this.searchText.toLowerCase();
		this.searchButton.setEnabled(false);
		List foundElements = new LinkedList();
		List foundSites = new LinkedList();
		List foundLinks = new LinkedList();
		
		Map map = this.mapView.getMap();
		try
		{
			Iterator it;
			for(it = map.getNodes().iterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				
				if(me.getName().toLowerCase().indexOf(loweredSearchText) != -1) {
					if(me instanceof SiteNode)
						foundSites.add(me);
					else
						foundElements.add(me);
				}
				else if(me instanceof SiteNode)
				{
					SiteNode site = (SiteNode )me;
					if(site.getCity().toLowerCase().indexOf(loweredSearchText) != -1
						|| site.getStreet().toLowerCase().indexOf(loweredSearchText) != -1
						|| site.getBuilding().toLowerCase().indexOf(loweredSearchText) != -1)
							foundSites.add(me);
				}
			}
			
			for(it = map.getAllPhysicalLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink )it.next();
				if(link.getName().toLowerCase().indexOf(loweredSearchText) != -1
					|| link.getCity().toLowerCase().indexOf(loweredSearchText) != -1
					|| link.getStreet().toLowerCase().indexOf(loweredSearchText) != -1
					|| link.getBuilding().toLowerCase().indexOf(loweredSearchText) != -1)
						foundLinks.add(link);
			}
			
			Collections.sort(foundSites, NodeTypeComparator.instance);
			Collections.sort(foundLinks, LinkTypeComparator.instance);

			foundElements.addAll(foundSites);
			foundElements.addAll(foundLinks);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		this.model.setValues(foundElements);

		this.searchButton.setEnabled(true);
		this.searching = false;
	}

	/**
	 * центрирование на карте выделенных в таблице объектов
	 */
	void doCenter()
	{
		int[] selection = this.table.getSelectedRows();
		if (selection.length == 0)
			return;

		Map map = this.mapView.getMap();

		this.mapFrame.getMapViewer().getLogicalNetLayer().deselectAll();

		for (int i = 0; i < selection.length; i++)
		{
			MapElement mapE = (MapElement)this.model.getObject(selection[(i)]);
			map.setSelected(mapE, true);

			this.mapFrame.getContext().getDispatcher().firePropertyChange(
					new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));		
		}
		
		Command centerCommand = new CenterSelectionCommand(this.mapFrame.getContext().getApplicationModel(), this.mapFrame.getMapViewer());
		centerCommand.execute();
	}
}

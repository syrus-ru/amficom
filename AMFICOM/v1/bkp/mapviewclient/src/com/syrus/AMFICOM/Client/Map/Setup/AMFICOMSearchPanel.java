/*
 * Название: $Id: AMFICOMSearchPanel.java,v 1.7 2005/01/30 15:38:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.CenterSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

/**
 * Панель поиска элементов карты АМФИКОМ
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/30 15:38:18 $
 * @author $Author: krupenn $
 * @see
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
	ObjectResourceController controller;

	/**
	 * таблица с найденными элементами
	 */
	ObjectResourceTable table;

	/**
	 * модель для таблицы найденных элементов
	 */
	ObjectResourceTableModel model;

	/**
	 * строка поиска
	 */
	String searchText = "";
	
	/**
	 * флаг поиска (осуществляется ли поиск в текущий момент)
	 */
	private boolean searching = false;
	
	private MapFrame mmf;
	private MapView mapView = null;
	
	JButton centerButton = new JButton();
	
	/**
	 * По умолчанию
	 */
	public AMFICOMSearchPanel()
	{
		controller = SimpleMapElementController.getInstance();
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Метод jbInit
	 * 
	 * 
	 * @exception Exception
	 */
	private void jbInit()
	{
		this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(370, 629));

		searchButton.setText(LangModelMap.getString("Search"));
		searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doSearch();
				}
			});

		centerButton.setText(LangModelMap.getString("DoCenter"));
		centerButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doCenter();
				}
			});

		searchField.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						doSearch();
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});

		JTableHeader jth = table.getTableHeader();
		scrollPane.getViewport().add(jth);
		scrollPane.getViewport().add(table);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);
		
		this.add(searchField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(searchButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		this.add(jth, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(scrollPane, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(centerButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void setMapView(MapView mv)
	{
		if(mv != null)
//			this.map = mv.getMap();
			this.mapView = mv;
	}

	public void setMapFrame(MapFrame mmf)
	{
		this.mmf = mmf;
		if(mmf == null)
		{
			table.removeAll();
			mapView = null;
//			map = null;
		}
		else
			mapView = mmf.getMapView();
//			map = mmf.getMap();
	}
	
	public MapFrame getMapFrame() 
	{
		return mmf;
	}

	/**
	 * запуск операции поиска по нажатию на кнопку. задача поиска запускается
	 * отдельным thread'ом
	 */
	private void doSearch()
	{
		if(searching)
			return;
		if(mmf == null)
			return;
			
		searchText = searchField.getText();
		if(searchText.length() == 0)
			return;
		searching = true;
		table.removeAll();
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				search();
			}
		});
		t.start();
	}

	/**
	 * просмотр всех элементов карты и поиск среди них объектов,
	 * имя которых сооветствует заданному шаблону поиска
	 */	
	public void search()
	{
		searchButton.setEnabled(false);
		List vec = new LinkedList();
		
		Map map = mapView.getMap();
//		MapElementController controller;
//		LogicalNetLayer lnl = this.getMapFrame().getMapViewer().getLogicalNetLayer();
		try
		{
			Iterator it;
			for(it = map.getNodes().listIterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();

//				controller = lnl.getMapViewController().getController(me);
				
				if(me.getName().indexOf(searchText) != -1)
					vec.add(me);
			}
			
			for(it = map.getPhysicalLinks().listIterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				if(me.getName().indexOf(searchText) != -1)
					vec.add(me);
			}
/*			
			for(it = map.getTransmissionPath().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
*/			
/*			
			for(it = map.getNodeLinks().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		model.setContents(vec);

		searchButton.setEnabled(true);
		searching = false;
	}

	/**
	 * центрирование на карте выделенных в таблице объектов
	 */
	private void doCenter()
	{
		mmf.getMapViewer().getLogicalNetLayer().getMapViewController().deselectAll();

		int[] selection = table.getSelectedRows();
		for (int i = 0; i < selection.length; i++)
		{
			MapElement mapE = (MapElement)model.getObject(selection[(i)]);
			mapE.setSelected(true);
		}

		Command com = new CenterSelectionCommand(mmf.getMapViewer().getLogicalNetLayer());
		com.setParameter("applicationModel", mmf.getContext().getApplicationModel());
		com.setParameter("logicalNetLayer", mmf.getMapViewer().getLogicalNetLayer());
		com.execute();
		
	}
}

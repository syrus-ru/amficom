/*
 * ��������: $Id: AMFICOMSearchPanel.java,v 1.9 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.Client.Map.Setup;

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

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.CenterSelectionCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ������ ������ ��������� ����� �������
 * @version $Revision: 1.9 $, $Date: 2005/02/10 11:48:39 $
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
	 * ������ ����������� ��������� ���������
	 */
	ObjectResourceController controller;

	/**
	 * ������� � ���������� ����������
	 */
	ObjectResourceTable table;

	/**
	 * ������ ��� ������� ��������� ���������
	 */
	ObjectResourceTableModel model;

	/**
	 * ������ ������
	 */
	String searchText = "";
	
	/**
	 * ���� ������ (�������������� �� ����� � ������� ������)
	 */
	private boolean searching = false;
	
	private MapFrame mapFrame;
	private MapView mapView = null;
	
	JButton centerButton = new JButton();
	
	/**
	 * �� ���������
	 */
	public AMFICOMSearchPanel()
	{
		this.controller = SimpleMapElementController.getInstance();
		this.model = new ObjectResourceTableModel(this.controller);
		this.table = new ObjectResourceTable(this.model);

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
		
		this.add(this.searchField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.searchButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		this.add(jth, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.scrollPane, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.centerButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void setMapView(MapView mv)
	{
		if(mv != null)
//			this.map = mv.getMap();
			this.mapView = mv;
	}

	public void setMapFrame(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
		if(mapFrame == null)
		{
			this.table.removeAll();
			this.mapView = null;
//			this.map = null;
		}
		else
			this.mapView = this.mapFrame.getMapView();
//			this.map = mmf.getMap();
	}
	
	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	/**
	 * ������ �������� ������ �� ������� �� ������. ������ ������ �����������
	 * ��������� thread'��
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

	/**
	 * �������� ���� ��������� ����� � ����� ����� ��� ��������,
	 * ��� ������� ������������ ��������� ������� ������
	 */	
	public void search()
	{
		this.searchButton.setEnabled(false);
		List vec = new LinkedList();
		
		Map map = this.mapView.getMap();
//		MapElementController controller;
//		LogicalNetLayer lnl = this.getMapFrame().getMapViewer().getLogicalNetLayer();
		try
		{
			Iterator it;
			for(it = map.getNodes().listIterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();

//				controller = lnl.getMapViewController().getController(me);
				
				if(me.getName().indexOf(this.searchText) != -1)
					vec.add(me);
			}
			
			for(it = map.getPhysicalLinks().listIterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				if(me.getName().indexOf(this.searchText) != -1)
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

		this.model.setContents(vec);

		this.searchButton.setEnabled(true);
		this.searching = false;
	}

	/**
	 * ������������� �� ����� ���������� � ������� ��������
	 */
	void doCenter()
	{
		this.mapFrame.getMapViewer().getLogicalNetLayer().getMapView().deselectAll();

		int[] selection = this.table.getSelectedRows();
		for (int i = 0; i < selection.length; i++)
		{
			MapElement mapE = (MapElement)this.model.getObject(selection[(i)]);
			mapE.setSelected(true);
		}

		Command com = new CenterSelectionCommand(this.mapFrame.getMapViewer().getLogicalNetLayer());
		com.setParameter("applicationModel", this.mapFrame.getContext().getApplicationModel());
		com.setParameter("logicalNetLayer", this.mapFrame.getMapViewer().getLogicalNetLayer());
		com.execute();
		
	}
}

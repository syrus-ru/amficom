/*
 * ????????: $Id: SpatialSearchPanel.java,v 1.4 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
 */

package com.syrus.AMFICOM.Client.Map.Operations;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.SpatialObject;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;

/**
 * ?????? ?????? ?????????????? ????????
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:57 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class SpatialSearchPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	JTextField searchField = new JTextField();
	JButton searchButton = new JButton();

	/**
	 * ?????? ????????? ????????
	 */
	JList foundList = new JList();
	JButton centerButton = new JButton();

	String searchText = "";
	private boolean searching = false;
	
	private MapFrame mapFrame;

	public void setMapFrame(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
		if(mapFrame == null)
			this.foundList.removeAll();
	}
	
	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	/**
	 * ?? ?????????
	 */
	public SpatialSearchPanel()
	{
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
		this.setToolTipText(LangModelMap.getString("SearchTopologicalObjects"));
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
		this.jScrollPane.getViewport().add(this.foundList);
		this.jScrollPane.setAutoscrolls(true);
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

		this.add(this.searchField, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.searchButton, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.jScrollPane, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.centerButton, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.foundList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.foundList.setCellRenderer(new SpatialSearchPanel.SpatialObjectRenderer());
		this.foundList.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e)
			{
				if (e.getClickCount() > 1)				
					SpatialSearchPanel.this.doCenter();
			}
		});
	}

	/**
	 * ????????? ??????? ?? ?????? ??????. ??? ????? ??????????? ? ?????????
	 * thread'?
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
		this.foundList.removeAll();
		
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
	 * ?????????? ?????? ?????????????? ???????? ?? ???????
	 * ????????? ??????? ?????????? ? ?????? foundList
	 */
	public void search()
	{
		this.searchButton.setEnabled(false);
		try {
			List found = this.mapFrame.getMapViewer().getLogicalNetLayer().findSpatialObjects(this.searchText);
			this.foundList.setListData(found.toArray());
		} catch(MapConnectionException e) {
			this.foundList.setListData(new String[] {e.getMessage()});
			e.printStackTrace();
		} catch(MapDataException e) {
			this.foundList.setListData(new String[] {e.getMessage()});
			e.printStackTrace();
		}
		this.searchButton.setEnabled(true);
		this.searching = false;
	}

	/**
	 * ????????? ??????? ?????? ????????????. ??? ????? ???????????? ?? 
	 * ???????? ??????????????? ??????? ?????????? ? ?????? ????????
	 */
	void doCenter()
	{
		try {
			SpatialObject so = (SpatialObject )this.foundList.getSelectedValue();
			this.mapFrame.getMapViewer().getLogicalNetLayer().centerSpatialObject(so);
			this.mapFrame.getMapViewer().getLogicalNetLayer().repaint(true);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ????????? ?????????????? ???????? ? ?????? - ??????????? ?? ??????????
	 */
	protected class SpatialObjectRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			JLabel lbl = (JLabel )super.getListCellRendererComponent(
				list,
				value,
				index,
				isSelected,
				cellHasFocus);
				
			SpatialObject so = (SpatialObject )value;
			lbl.setText(so.getLabel());
			return lbl;
		}
	}
}

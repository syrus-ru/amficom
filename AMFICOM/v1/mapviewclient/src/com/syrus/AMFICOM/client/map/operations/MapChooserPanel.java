/*
 * Название: $Id: MapChooserPanel.java,v 1.3 2005/06/06 12:20:33 krupenn Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

/**
 * панель выбора вида карты
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:33 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapChooserPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton selectButton = new JButton();

	/**
	 * список возможных видов карты
	 */
	private JComboBox combo = new JComboBox();
	
	/**
	 * окно карты
	 */
	private MapFrame mapFrame;
	
	/**
	 * По умолчанию
	 */
	public MapChooserPanel()
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
		this.setToolTipText(LangModelMap.getString("ChooseMap"));
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		this.selectButton.setText(LangModelGeneral.getString("Ok"));
		this.selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mapSelected();
				}
			});
		this.add(this.combo, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.selectButton, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	}

	/**
	 * обработка выбора нового вида карты
	 * смена вида осуществляется в отдельном thread'е
	 */
	void mapSelected()
	{
		if(this.mapFrame == null)
			return;
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				changeMap();
			}
		});
		t.start();
	}

	/**
	 * обновить список возможных видов карты
	 */
	public synchronized void refreshMapList() 
	{
		this.combo.removeAllItems();
		if(this.mapFrame == null)
			return;

		List availableViews;
		try
		{
			availableViews = this.mapFrame.getMapViewer().getConnection().getAvailableViews();
		}
		catch(MapDataException e)
		{
			System.out.println("Cannot get views: " + e.getMessage());
			e.printStackTrace();
			return;
		} 

		for(Iterator it = availableViews.iterator(); it.hasNext();)
		{
			this.combo.addItem(it.next());
		}
		String currentMap = this.mapFrame.getMapViewer().getConnection().getView();
		if (null == currentMap) 
		{
			if(this.combo.getModel().getSize() != 0)
			{
				this.combo.setSelectedIndex(0);
				changeMap();
			}
		}
		else
			this.combo.setSelectedItem(currentMap);
	}
	
	public void setMapFrame(MapFrame mmf) 
	{
		this.mapFrame = mmf;
		refreshMapList();
	}

	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	void changeMap() 
	{
		String name = (String )this.combo.getSelectedItem();
		String previousView = this.mapFrame.getMapViewer().getConnection().getView(); 
		try
		{
			this.mapFrame.getMapViewer().getConnection().setView(name);
			this.mapFrame.getMapViewer().getConnection().connect();
		}
		catch(MapConnectionException e)
		{
			System.out.println("Cannot change view: " + e.getMessage());
			e.printStackTrace();
			this.mapFrame.getMapViewer().getConnection().setView(previousView);
		}
	}
}

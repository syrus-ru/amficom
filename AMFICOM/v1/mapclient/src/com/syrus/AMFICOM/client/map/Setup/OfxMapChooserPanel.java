/*
 * Название: $Id: OfxMapChooserPanel.java,v 1.1 2004/06/22 08:02:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Configure.Map.Setup;
import com.ofx.geocoding.*;
import com.ofx.mapViewer.*;
import com.ofx.component.*;
import com.ofx.base.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс $Name:  $ используется для 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/06/22 08:02:09 $
 * @author $Author: krupenn $
 * @see
 */
public class OfxMapChooserPanel extends JPanel
	implements MapListener
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton selectButton = new JButton();
	private JComboBox combo = new JComboBox();
	private MapMainFrame mmf;
	private MapViewer mapViewer;
	
	/**
	 * По умолчанию
	 */
	public OfxMapChooserPanel()
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

	/**
	 * Метод jbInit
	 * 
	 * 
	 * @exception Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		selectButton.setText("Принять");
		selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					selectButton_actionPerformed(e);
				}
			});
		this.add(combo, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(selectButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	}

	private void selectButton_actionPerformed(ActionEvent e)
	{
		if (!mapViewer.getMapName().equals((String )combo.getSelectedItem())) 
		{
			Thread t = new Thread(new Runnable() 
			{
				public void run() 
				{
					changeMap();
				}
			});
			t.start();
		}
	}

	public synchronized void refreshMapList() 
	{
		combo.removeAllItems();
		String currentMap = mapViewer.getMapName();
      
		Vector maps = mapViewer.getAvailableMaps();
		for (int i = 0; i < maps.size(); i++) 
		{
			String aMapName = (String )maps.elementAt(i);
			combo.addItem(aMapName);

			if (aMapName.equals(currentMap)) 
			{
				combo.setSelectedIndex(i);
			}
		}

		if (null == currentMap) 
		{
			combo.setSelectedIndex(0);
			changeMap();
		}
	}
	
	public void mapAction(MapEvent e) 
	{
		if ((e.getState() == SxState.UPDATE) && (e.getContext() == SxContext.MAP_NAME)) 
		{
			if (!mapViewer.getMapName().equals((String)combo.getSelectedItem())) 
			{
				combo.setSelectedItem(mapViewer.getMapName());
			}
		}
	}

	public void mapResult(MapEvent e) { }

	public void setMapMainFrame(MapMainFrame mmf) 
	{
		try 
		{
			mapViewer.removeMapListener(this);
		} 
		catch (Exception ex) { } 
		
		try 
		{
			this.mmf = mmf;
			mapViewer = mmf.myMapViewer.getMapViewer();
			
			mapViewer.addMapListener(this);
			
			refreshMapList();
		} 
		catch (Exception ex) { } 
	}

	public MapMainFrame getMapMainFrame() 
	{
		return mmf;
	}

	private void changeMap() 
	{
		String name = (String )combo.getSelectedItem();
		try 
		{
			mapViewer.setMapName(name);
		} 
		catch (SxInvalidNameException ine) { }
	}
}
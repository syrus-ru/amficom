/*
 * Название: $Id: AMFICOMSearchPanel.java,v 1.1 2004/06/22 08:02:09 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.MapNav.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Класс $Name:  $ используется для 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/06/22 08:02:09 $
 * @author $Author: krupenn $
 * @see
 */
 public class AMFICOMSearchPanel extends JPanel
{
	JTextField searchField = new JTextField();
	JButton searchButton = new JButton();
	ObjectResourceTablePane foundTable = new ObjectResourceTablePane();
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	String searchText = "";
	private boolean searching = false;
	
	private MapMainFrame mmf;
	JButton centerButton = new JButton();

	public void setMapMainFrame(MapMainFrame mmf)
	{
		this.mmf = mmf;
		if(mmf == null)
			foundTable.setContents(new DataSet());
	}
	
	public MapMainFrame getMapMainFrame() 
	{
		return mmf;
	}

	/**
	 * По умолчанию
	 */
	public AMFICOMSearchPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		foundTable.initialize(
				new StubDisplayModel(
					new String[] {"name", "type_id"}, 
					new String[] {"Название", "Тип"} ), 
				new DataSet());
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

		searchButton.setText("Искать");
		searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doSearch();
				}
			});

		centerButton.setText("Центрировать");
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

		this.add(searchField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(searchButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(foundTable, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(centerButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		foundTable.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

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
		foundTable.setContents(new DataSet());
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				search();
			}
		});
		t.start();
	}
	
	public void search()
	{
		searchButton.setEnabled(false);
		Vector vec = new Vector();
		try
		{
			MapContext mc = mmf.getMapContext();
			Iterator it;
			for(it = mc.getNodes().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
			
			for(it = mc.getPhysicalLinks().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
			
			for(it = mc.getTransmissionPath().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
/*			
			for(it = mc.getNodeLinks().listIterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getName().indexOf(searchText) != -1)
					vec.add(or);
			}
*/
		}
		catch(Exception ex)
		{
		}

		foundTable.setContents(new DataSet(vec));

		searchButton.setEnabled(true);
		searching = false;
	}

	private void doCenter()
	{
		mmf.getMapContext().deselectAll();
		
		JTable myTable = foundTable.getTable();
		int[] selection = myTable.getSelectedRows();
		for (int i = 0; i < selection.length; i++)
		{
			MapElement mapE = (MapElement )foundTable.getObjectAt(selection[i]);
			mapE.select();
		}
		Command com = new CenterSelectionCommand(mmf.lnl());
		com.setParameter("applicationModel", mmf.aContext.getApplicationModel());
		com.setParameter("logicalNetLayer", mmf.lnl());
		com.execute();
		
	}
}
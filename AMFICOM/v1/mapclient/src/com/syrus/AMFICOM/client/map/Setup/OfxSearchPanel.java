/*
 * Название: $Id: OfxSearchPanel.java,v 1.1 2004/06/22 08:02:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Configure.Map.Setup;

import com.ofx.base.*;
import com.ofx.component.*;
import com.ofx.component.swing.*;
import com.ofx.geometry.*;
import com.ofx.mapViewer.*;
import com.ofx.repository.*;
import com.ofx.query.*;

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
 public class OfxSearchPanel extends JPanel
{
	JTextField searchField = new JTextField();
	JButton searchButton = new JButton();
	JList foundList = new JList();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	String searchText = "";
	private boolean searching = false;
	
	private MapMainFrame mmf;
	JButton centerButton = new JButton();

	public void setMapMainFrame(MapMainFrame mmf)
	{
		this.mmf = mmf;
		if(mmf == null)
			foundList.removeAll();
	}
	
	public MapMainFrame getMapMainFrame() 
	{
		return mmf;
	}

	/**
	 * По умолчанию
	 */
	public OfxSearchPanel()
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
		searchButton.setText("Искать");
		searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doSearch();
				}
			});
		jScrollPane.getViewport().add(foundList);
		jScrollPane.setAutoscrolls(true);
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
		this.add(jScrollPane, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(centerButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		foundList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foundList.setCellRenderer(new OfxSearchPanel.SpatialObjectRenderer());
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
		foundList.removeAll();
		
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
		Vector vector;
		SxQueryResultInterface objects;
		Enumeration iter;
		int k;
		int l;
		String s;
		
		String searchTextLowCase = searchText.toLowerCase();
		String sampleLowCase;
		
		try
		{
			SxMapViewer sxMapViewer = mmf.myMapViewer.getSxMapViewer();

			vector = sxMapViewer.getForegroundClasses();
			k = vector.size();
			for(l = 0; l < k; l++)
			{
				s = (String )vector.elementAt(l);
				objects = sxMapViewer.getQuery().getObjects(s);
				iter = objects.elements();
				while(iter.hasMoreElements())
				{
					SxSpatialObject obj = (SxSpatialObject)iter.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						vec.add(new OfxSearchPanel.MySpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}

			vector = sxMapViewer.getBackgroundClasses();
			k = vector.size();
			for(l = 0; l < k; l++)
			{
				s = (String )vector.elementAt(l);
				objects = sxMapViewer.getQuery().getObjects(s);
				iter = objects.elements();
				while(iter.hasMoreElements())
				{
					SxSpatialObject obj = (SxSpatialObject)iter.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						vec.add(new OfxSearchPanel.MySpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}
		}
		catch(Exception ex)
		{
		}

		foundList.setListData(vec);

		searchButton.setEnabled(true);
		searching = false;
	}

	private void doCenter()
	{
		try 
		{
			OfxSearchPanel.MySpatialObject mso = (MySpatialObject )foundList.getSelectedValue();
			SxMapViewer sxMapViewer = mmf.myMapViewer.getSxMapViewer();
			SxDoublePoint center = mso.so.geometry.getCenter();
			System.out.print("Center " + center.x + ", " + center.y);
			center = sxMapViewer.convertLatLongToMap(center);
			System.out.println(" --> " + center.x + ", " + center.y);
			mmf.myMapViewer.setCenter(center.x, center.y);
//			sxMapViewer
			
		} 
		catch (Exception ex) 
		{
		} 
	}
	
	protected class MySpatialObject
	{
		public SxSpatialObject so;
		public String label;
		
		MySpatialObject(SxSpatialObject so, String label)
		{
			this.so = so;
			this.label = label;
		}
		
	}
	
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
				
			OfxSearchPanel.MySpatialObject mso = (MySpatialObject )value;
			lbl.setText(mso.label);
			return lbl;
		}
	}
}
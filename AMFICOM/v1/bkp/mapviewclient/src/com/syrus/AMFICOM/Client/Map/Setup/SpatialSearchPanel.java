/*
 * Название: $Id: SpatialSearchPanel.java,v 1.7 2005/02/22 11:00:15 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;

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

/**
 * панель поиска географических объектов
 * @version $Revision: 1.7 $, $Date: 2005/02/22 11:00:15 $
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
	 * список найденных объектов
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
	 * По умолчанию
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

		this.add(this.searchField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.searchButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.jScrollPane, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.centerButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.foundList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.foundList.setCellRenderer(new SpatialSearchPanel.SpatialObjectRenderer());
	}

	/**
	 * обработка нажатия на кнопку поиска. сам поиск запускается в отдельном
	 * thread'е
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
	 * выполнение поиска географических объектов по шаблону
	 * найденные объекты помещаются в список foundList
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
	 * обработка нажатия кнопки Центрировать. вид карты центрируется по 
	 * среднему геометрическому центров выделенных в списке объектов
	 */
	void doCenter()
	{
		try {
			SpatialObject so = (SpatialObject )this.foundList.getSelectedValue();
			this.mapFrame.getMapViewer().getLogicalNetLayer().centerSpatialObject(so);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * отрисовка географических объектов в списке - отображение их заголовков
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

/*
 * Название: $Id: SpatialSearchPanel.java,v 1.4 2005/01/21 16:19:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.SpatialObject;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

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

/**
 * панель поиска географических объектов
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/21 16:19:57 $
 * @author $Author: krupenn $
 * @see
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
	
	private MapFrame mmf;

	public void setMapFrame(MapFrame mmf)
	{
		this.mmf = mmf;
		if(mmf == null)
			foundList.removeAll();
	}
	
	public MapFrame getMapFrame() 
	{
		return mmf;
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
		jScrollPane.getViewport().add(foundList);
		jScrollPane.setAutoscrolls(true);
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

		this.add(searchField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(searchButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(jScrollPane, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.add(centerButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		foundList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foundList.setCellRenderer(new SpatialSearchPanel.SpatialObjectRenderer());
	}

	/**
	 * обработка нажатия на кнопку поиска. сам поиск запускается в отдельном
	 * thread'е
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
	
	/**
	 * выполнение поиска географических объектов по шаблону
	 * найденные объекты помещаются в список foundList
	 */
	public void search()
	{
		searchButton.setEnabled(false);
		List found = mmf.getMapViewer().getLogicalNetLayer().findSpatialObjects(searchText);

		foundList.setListData(found.toArray());

		searchButton.setEnabled(true);
		searching = false;
	}

	/**
	 * обработка нажатия кнопки Центрировать. вид карты центрируется по 
	 * среднему геометрическому центров выделенных в списке объектов
	 */
	private void doCenter()
	{
		SpatialObject so = (SpatialObject )foundList.getSelectedValue();
		mmf.getMapViewer().getLogicalNetLayer().centerSpatialObject(so);
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

/**
 * $Id: MapElementPanel.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * ������ ����� ���������� ������ �� ������� ��������� ComboBox �� �������
 * ����� ��������� � ������ ��������� � ������ "�������������" � "��������"
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapElementPanel extends JPanel
{
	Map map;
	ApplicationContext aContext;
	
	LogicalNetLayer logicalNetLayer;

	JComboBox typeComboBox = new AComboBox();

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

	JScrollPane scrollPane = new JScrollPane();

	boolean mouseSelect = true;
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();

	protected boolean performProcessing = true;
	
	public MapElementPanel(LogicalNetLayer logicalNetLayer, Map map)
	{
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		setLogicalNetLayer(logicalNetLayer);
		setMap(map);
  }
  
	void jbInit()
	{
		typeComboBox.addItem(MapSiteNodeElement.typ);
		typeComboBox.addItem(MapPhysicalLinkElement.typ);
		typeComboBox.addItem(MapPhysicalNodeElement.typ);
		typeComboBox.addItem(MapMarkElement.typ);
//		typeComboBox.addItem(MapTransmissionPathElement.typ);
		typeComboBox.addItem(com.syrus.AMFICOM.Client.Resource.MapView.MapMarker.typ);

		typeComboBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				listSelectionChanged();
			}
		});

		typeComboBox.setRenderer(new MapElementPanelRenderer());

		controller = SimpleMapElementController.getInstance();
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

		scrollPane.getViewport().add(table);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

		setEnabled(false);
		this.setLayout(borderLayout2);

		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		table.getSelectionModel().addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					tableSelectionChanged();
				}
			}
		);

		jPanel1.setLayout(borderLayout1);
		jPanel1.add(typeComboBox,  BorderLayout.CENTER);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.NORTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	void listSelectionChanged()
	{
		updateTable();
	}

	public void setMap(Map map)
	{
		this.map = map;
		setEnabled(map != null);
		updateTable();
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		setEnabled(logicalNetLayer != null);
		updateTable();
	}

	void tableSelectionChanged()
	{
		try
		{
			//���� ���� ������� �����
			if (mouseSelect)
			{
				Dispatcher disp = null;
				if(aContext != null)
					disp = aContext.getDispatcher();

				if(disp != null)
				{
					for(Iterator it = model.getContents().iterator(); it.hasNext();)
					{
						MapElement mapE = (MapElement )it.next();
						performProcessing = false;
						disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
						performProcessing = true;
					}
				}

				for (int i = 0; i < table.getSelectedRows().length; i++)
				{
					MapElement mapE = (MapElement )model.getObjectResource(table.getSelectedRows()[i]);
					if(disp != null)
					{
						performProcessing = false;
						disp.notify(new MapNavigateEvent(mapE, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
						logicalNetLayer.notifySchemeEvent(mapE);
						logicalNetLayer.notifyCatalogueEvent(mapE);
						performProcessing = true;
					}
				}
				mouseSelect = true;
			}
			logicalNetLayer.repaint();
		}
		catch(Exception e)
		{
		}
	}

	public void updateTable()
	{
		//������� �������
		mouseSelect = false;
		//����� ������� ��������� �������� � �����
		table.clearSelection();
		mouseSelect = true;
		String selection = (String )typeComboBox.getSelectedItem();
		List dataSet = new LinkedList();

		if(map != null)
		{
			if(selection.equals(MapSiteNodeElement.typ))
				dataSet = map.getMapSiteNodeElements();
			else
			if(selection.equals(MapPhysicalLinkElement.typ))
				dataSet = map.getPhysicalLinks();
			else
/*
			if(selection.equals(MapTransmissionPathElement.typ))
				dataSet = map.getTransmissionPath();
			else
*/				
			if(selection.equals(MapPhysicalNodeElement.typ))
				dataSet = map.getMapPhysicalNodeElements();
			else
			if(selection.equals(MapMarkElement.typ))
				dataSet = map.getMapMarkElements();
//			else
//			if(selection.equals(MapMarker.typ))
//				dataSet = map.getMarkers();
		}

		model.setContents(dataSet);
		setSelectedObjects();
	}

	public void setSelectedObjects()
	{
	try
	{
		//������� �������
		mouseSelect = false;
		//����� ������� ��������� �������� � �����
		table.clearSelection();
		List dataSet = model.getContents();
		int i = 0;
		for(Iterator it = dataSet.iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isSelected())
	            table.getSelectionModel().addSelectionInterval(i, i);
			i++;
		}
		mouseSelect = true;
	}
    catch(Exception e)
    {
		e.printStackTrace();
    }
	}

	public void setEnabled( boolean b)
	{
		typeComboBox.setEnabled(b);
		table.setEnabled(b);

		if (!b)
		{
			model.clear();
		}
	}

	private class MapElementPanelRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			String text = LangModel.getString("node" + (String )value);
			return super.getListCellRendererComponent(
				list, text, index, isSelected, cellHasFocus);
		}
	}
}


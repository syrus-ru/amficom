package com.syrus.AMFICOM.Client.Map.Popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import  com.syrus.AMFICOM.Client.General.Lang.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.Strategy.*;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class PhysicalLinkPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
	JMenuItem menuAddMark = new JMenuItem();
	JMenuItem menuSelect = new JMenuItem();
	JMenuItem menuDelete = new JMenuItem();
	JMenuItem menuView = new JMenuItem();
	JMenuItem menuCreatePath = new JMenuItem();
	JMenuItem menuProperties = new JMenuItem();
	JMenuItem menuShowPath = new JMenuItem();

	LogicalNetLayer logicalNetLayer;

	ApplicationContext aContext;

	JFrame mainFrame;
	MapPhysicalLinkElement myLink;

	Point where_shown = null;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public PhysicalLinkPopupMenu(JFrame myFrame, MapPhysicalLinkElement link, LogicalNetLayer logical)
	{
		super();
		try
		{
			mainFrame = myFrame;
			myLink = link;
			logicalNetLayer = logical;
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		menuAddMark = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuAddMark"));
		menuSelect = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuChoose"));
		menuDelete = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuDelete"));
		menuView = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuLook"));
		menuCreatePath = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuCreatetransmisionPath"));
		menuProperties = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuProperties"));
		menuShowPath = new JMenuItem(LangModelMap.Text("PhysicalLinkPopupMenuShowtransmisionPath"));

		menuCreatePath.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuCreatePath_actionPerformed(e);
			}
		});
		menuProperties.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuProperties_actionPerformed(e);
			}
		});
		menuShowPath.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuShowPath_actionPerformed(e);
			}
		});
//		menuAddMark.setText("jMenuItem1");
		menuAddMark.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuAddMark_actionPerformed(e);
				}
			});

//		this.add(menuSelect);
//		this.add(menuDelete);
		this.addSeparator();
		this.add(menuAddMark);
//		this.add(menuShowPath);
//		this.add(menuView);
		this.add(menuCreatePath);
		this.addSeparator();
		this.add(menuProperties);
	}

    public void show(Component invoker, int x, int y)
	{
		where_shown = new Point(x, y);
		super.show(invoker, x, y);
	}
	
//Создаём transmissionpath
	void menuCreatePath_actionPerformed(ActionEvent e)
	{
		if ( logicalNetLayer.mapMainFrame.aContext.getApplicationModel()
                 .isEnabled("mapActionCreatePath"))
		{
			DataSourceInterface dataSource = logicalNetLayer.mapMainFrame.getContext().getDataSourceInterface();

			Vector physicalLinkVector = new Vector();
			Vector nodes = new Vector();
			MapNodeElement startNode = null;
			MapNodeElement endNode = null;

			Iterator e1 = logicalNetLayer.getMapContext().getPhysicalLinks().iterator();
			while ( e1.hasNext())
			{
				MapPhysicalLinkElement myMapPhysicalLinkElement = (MapPhysicalLinkElement )e1.next();

				if ( myMapPhysicalLinkElement.isSelected() )
				{
					physicalLinkVector.add( myMapPhysicalLinkElement.getId());
					nodes.add( myMapPhysicalLinkElement.startNode );
					nodes.add( myMapPhysicalLinkElement.endNode );
		       }
			}

			Vector returnStartEndNode = isSelectionIsCorrect(nodes);
			startNode = (MapNodeElement)returnStartEndNode.get(0);
			endNode = (MapNodeElement)returnStartEndNode.get(1);

			if ( returnStartEndNode.size() != 2)
			{
				JOptionPane.showMessageDialog( logicalNetLayer.mainFrame, "Ошибка! Неправельно выбран элемент Пути");
				return;
			}

			MapTransmissionPathElement  myMapTransmissionPathElement = new MapTransmissionPathElement( 
				dataSource.GetUId( MapTransmissionPathElement.typ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapContext());
			Pool.put(MapTransmissionPathElement.typ, myMapTransmissionPathElement.getId(), myMapTransmissionPathElement);
			myMapTransmissionPathElement.physicalLink_ids = physicalLinkVector;
			logicalNetLayer.getMapContext().getTransmissionPath().add( myMapTransmissionPathElement);

//			logicalNetLayer.parent.myMapElementsPanel.updateTable();
			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
				disp.notify(new OperationEvent(myMapTransmissionPathElement, 0, "mapchangeevent"));
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new MapNavigateEvent(myMapTransmissionPathElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				logicalNetLayer.getMapContext().notifySchemeEvent(myMapTransmissionPathElement);
				logicalNetLayer.getMapContext().notifyCatalogueEvent(myMapTransmissionPathElement);
				logicalNetLayer.perform_processing = true;
			}

			logicalNetLayer.postDirtyEvent();
			logicalNetLayer.postPaintEvent();
		}
	}

	public Vector isSelectionIsCorrect(Vector vec)
	{
   //Проверка того что все MapPhysicalLinkElements связаны т.е.
   //должно что бы только для вах Node ф-ия logicalNetLayer.getMapContext().getPhysicalLinksContainingNodeByEndOrStart( myNode).size() == 1

		Vector start_end_nodes = new Vector();
	    Enumeration  e11 = vec.elements();

		while ( e11.hasMoreElements())
		{
			MapNodeElement myNode= (MapNodeElement)e11.nextElement();
			int first = vec.indexOf(myNode);

//A0A
			if ( vec.indexOf( myNode, first + 1) == -1)
			{
				start_end_nodes.add(myNode);
			}
		}
//   JOptionPane.showMessageDialog( logicalNetLayer.mainFrame, "-- " + start_end_nodes.size() );
		return start_end_nodes;
	}

//A0A
	void menuShowPath_actionPerformed(ActionEvent e)
	{
		try
		{
			MapElement mapElement = logicalNetLayer.getMapContext().getCurrentMapElement();
			if ( mapElement instanceof MapPhysicalLinkElement)
			{
				((MapTransmissionPathElement)logicalNetLayer.getMapContext().getTransmissionPathByPhysicalLink(mapElement.getId()).get(0)).select();
				logicalNetLayer.postDirtyEvent();
				logicalNetLayer.postPaintEvent();
			}
		}
		catch(Exception exeption)
		{
			exeption.printStackTrace();
		}
	}

	void menuProperties_actionPerformed(ActionEvent e)
	{
		//Вызываем диалоговое оено
		MapElementPropertiesDialog dialog = new MapElementPropertiesDialog(
			mainFrame, 
			aContext, 
			"Hello",
			true, 
			myLink);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.show();
	}

	void menuAddMark_actionPerformed(ActionEvent e)
	{
		if ( myLink.getLogicalNetLayer().mapMainFrame
			.aContext.getApplicationModel().isEnabled("mapActionMarkCreate"))
		{
			Point point = where_shown;

			double distance = 0.0;
			MapNodeElement node = myLink.startNode;
			for(Enumeration enum = myLink.sortNodeLinks().elements();
					enum.hasMoreElements();)
			{
				MapNodeLinkElement mnle = (MapNodeLinkElement )enum.nextElement();
					
				if(mnle.isMouseOnThisObject(point))
				{
					distance += mnle.getLength(node, point);
					break;
				}
				else
					distance += mnle.getLength();

				if(mnle.startNode.equals(node))
					node = mnle.endNode;
				else
					node = mnle.startNode;
			}

			MapMarkElement mark = new MapMarkElement(myLink.getMapContext(), myLink, distance);
			mark.id = aContext.getDataSourceInterface().GetUId(MapMarkElement.typ);
			mark.name = mark.id;
			mark.link_id = myLink.getId();

			myLink.getMapContext().getNodes().add(mark);
		}
	}
}
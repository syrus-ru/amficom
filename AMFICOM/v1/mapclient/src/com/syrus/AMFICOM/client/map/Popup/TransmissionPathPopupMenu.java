package com.syrus.AMFICOM.Client.Configure.Map.Popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.ofx.geometry.*;
import com.ofx.mapViewer.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class TransmissionPathPopupMenu
		extends JPopupMenu 
		implements MyPopupMenu
{
	JMenuItem menuSelect = new JMenuItem();
	JMenuItem menuDelete = new JMenuItem();
	JMenuItem menuAdd = new JMenuItem();
	JMenuItem menuProperties = new JMenuItem();

	JFrame mainFrame;
	MapTransmissionPathElement transmissionPath;
	ApplicationContext aContext;
	JMenuItem menuFlipStartEnd = new JMenuItem();

	Point where_shown = null;

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public TransmissionPathPopupMenu(JFrame myFrame, MapTransmissionPathElement path)
	{
		super();
		try
		{
			mainFrame = myFrame;
			transmissionPath = path;
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception 
	{
		menuSelect = new JMenuItem(LangModelMap.Text("TransmissionPathPopupMenuChoose"));
		menuDelete = new JMenuItem(LangModelMap.Text("TransmissionPathPopupMenuDelete"));
		menuAdd = new JMenuItem(LangModelMap.Text("TransmissionPathPopupMenuAdd"));
		menuProperties = new JMenuItem(LangModelMap.Text("TransmissionPathPopupMenuProperties"));

		menuAdd.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuAdd_actionPerformed(e);
			}
		});
		menuProperties.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuProperties_actionPerformed(e);
				}
			});
		menuFlipStartEnd.setText("Начало - Конец");
		menuFlipStartEnd.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuFlipStartEnd_actionPerformed(e);
				}
			});
		menuDelete.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuDelete_actionPerformed(e);
			}
		});

//		this.add(menuSelect);
//		this.add(menuFlipStartEnd);
		this.add(menuDelete);
		this.addSeparator();
		this.add(menuAdd);
		this.addSeparator();
		this.add(menuProperties);
	}

    public void show(Component invoker, int x, int y)
	{
		where_shown = new Point(x, y);
		super.show(invoker, x, y);
	}
	
//Создание маркера
	void menuAdd_actionPerformed(ActionEvent e)
	{
		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
			.aContext.getApplicationModel().isEnabled("mapActionMarkerCreate"))
		{
			if(transmissionPath.PATH_ID == null || transmissionPath.PATH_ID.equals(""))
				return;

			Point point = where_shown;

			double distance = transmissionPath.getDistanceFromStartLf(point);
/*
			double distance = 0.0;
			MapNodeElement node = transmissionPath.startNode;
			for(Enumeration enum = transmissionPath.sortNodeLinks().elements();
					enum.hasMoreElements();)
			{
				MapNodeLinkElement mnle = (MapNodeLinkElement )enum.nextElement();
				if(mnle.isMouseOnThisObject(point))
				{
					distance += mnle.getLengthLf(node, point);
					break;
				}
				else
					distance += mnle.getLengthLf();

				if(mnle.startNode.equals(node))
					node = mnle.endNode;
				else
					node = mnle.startNode;
			}
*/

			MapMarker marker = new MapMarker(
					"marker" + String.valueOf(System.currentTimeMillis()), 
					transmissionPath.getMapContext(),
                    new Rectangle(20, 20), 
					"",
//                    Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2 )) / 2,
					distance,
                    transmissionPath);
			marker.moveToFromStartLf(distance);
			transmissionPath.getMapContext().markers.add( marker);
			marker.sendMessage_Marker_Created();
  //  		JOptionPane.showMessageDialog(mainFrame, " MapMarker was created !!!");
		}
	}

//Создание маркера
	void menuAdd_actionPerformed111(ActionEvent e)
	{
		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
			.aContext.getApplicationModel().isEnabled("mapActionMarkerCreate"))
		{
/*
			MapNodeLinkElement nodeLink = (MapNodeLinkElement)transmissionPath
					.getMapContext().getNodeLinksInTransmissionPath(transmissionPath.getId()).get(0);

			double x1 = transmissionPath.getMapContext().getLogicalNetLayer().convertMapToScreen(nodeLink.startNode.getAnchor()).x;
			double y1 = transmissionPath.getMapContext().getLogicalNetLayer().convertMapToScreen(nodeLink.startNode.getAnchor()).y;
			double x2 = transmissionPath.getMapContext().getLogicalNetLayer().convertMapToScreen(nodeLink.endNode.getAnchor()).x;
			double y2 = transmissionPath.getMapContext().getLogicalNetLayer().convertMapToScreen(nodeLink.endNode.getAnchor()).y;
*/

//			Component invoker = this.getInvoker();
//			Point pt = invoker.getLocationOnScreen();
//			Point pt2 = this.getLocationOnScreen();

			if(transmissionPath.PATH_ID == null || transmissionPath.PATH_ID.equals(""))
				return;

			Point point = where_shown;

			double distance = 0.0;
			MapNodeElement node = transmissionPath.startNode;
			for(Enumeration enum = transmissionPath.sortNodeLinks().elements();
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

			MapMarker marker = new MapMarker(
					"marker" + String.valueOf(System.currentTimeMillis()), 
					transmissionPath.getMapContext(),
                    new Rectangle(14, 14), 
					"",
//                    Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2 )) / 2,
					distance,
                    transmissionPath);
//			marker.moveToFromStartKd(mylen);
			transmissionPath.getMapContext().markers.add( marker);
			marker.sendMessage_Marker_Created();
  //  		JOptionPane.showMessageDialog(mainFrame, " MapMarker was created !!!");
		}
	}

//Удаление transmissionPath
	void menuDelete_actionPerformed(ActionEvent e)
	{
		transmissionPath.getMapContext().removeTransmissionPath(transmissionPath);

		MapElement myMapElement = new VoidMapElement( transmissionPath.getMapContext().getLogicalNetLayer());

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
				disp.notify(new OperationEvent(myMapElement, 0, "mapchangeevent"));
			if(disp != null)
			{
				transmissionPath.getMapContext().getLogicalNetLayer().perform_processing = false;
				disp.notify(new MapNavigateEvent(myMapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				transmissionPath.getMapContext().getLogicalNetLayer().perform_processing = true;
				transmissionPath.getMapContext().notifySchemeEvent(myMapElement);
				transmissionPath.getMapContext().notifyCatalogueEvent(myMapElement);
			}
//		transmissionPath.getMapContext().getLogicalNetLayer().parent.myMapElementsPanel.updateTable();
//		transmissionPath.getMapContext()
//				.getLogicalNetLayer().parent.setContents((ObjectResource )myMapElement);

		transmissionPath.getMapContext().getLogicalNetLayer().postDirtyEvent();
		transmissionPath.getMapContext().getLogicalNetLayer().postPaintEvent();
	}

	void menuProperties_actionPerformed(ActionEvent e)
	{
		//Вызываем диалоговое оено
		MapElementPropertiesDialog dialog = new MapElementPropertiesDialog(
			mainFrame, 
			aContext, 
			"Hello",
			true, 
			transmissionPath);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.show();
	}

	void menuFlipStartEnd_actionPerformed(ActionEvent e)
	{
		MapNodeElement node = transmissionPath.startNode;
		transmissionPath.startNode = transmissionPath.endNode;
		transmissionPath.endNode = node;
	}

}

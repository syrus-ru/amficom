package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

public final class SelectionPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem insertSiteMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();
	
	private static SelectionPopupMenu instance = new SelectionPopupMenu();
	
	MapSelection selection;	

	private SelectionPopupMenu()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setMapElement(MapElement me)
	{
		this.selection = (MapSelection )me;

		insertSiteMenuItem.setVisible(selection.isPhysicalNodeSelection());
		generateMenuItem.setVisible(selection.isUnboundSelection());
		newCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
		addToCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
		removeCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
		removeFromCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
	}
	
	public static SelectionPopupMenu getInstance()
	{
		return instance;
	}
	
	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeSelection();
				}
			});
		insertSiteMenuItem.setText(LangModelMap.getString("PlaceSite"));
		insertSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					insertSite();
				}
			});
		generateMenuItem.setText(LangModelMap.getString("GenerateCabling"));
		generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateCabling();
				}
			});
		newCollectorMenuItem.setText(LangModelMap.getString("CreateCollector"));
		newCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newCollector();
				}
			});
		removeCollectorMenuItem.setText(LangModelMap.getString("RemoveCollector"));
		removeCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCollector();
				}
			});
		addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		addToCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToCollector();
				}
			});
		removeFromCollectorMenuItem.setText(LangModelMap.getString("RemoveFromCollector"));
		removeFromCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeFromCollector();
				}
			});
		this.add(removeMenuItem);
		this.add(insertSiteMenuItem);
		this.add(generateMenuItem);
//		this.addSeparator();
		this.add(addToCollectorMenuItem);
		this.add(removeFromCollectorMenuItem);
//		this.addSeparator();
		this.add(newCollectorMenuItem);
		this.add(removeCollectorMenuItem);
	}

	private void removeSelection()
	{
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint();
	}

	private void insertSite()
	{
		MapNodeProtoElement proto = super.selectNodeProto();
		if(proto != null)
		{
			for(Iterator it = selection.getElements().iterator(); it.hasNext();)
			{
				MapPhysicalNodeElement node = (MapPhysicalNodeElement )it.next();

				super.insertSiteInPlaceOfANode(node, proto);
			}

			getLogicalNetLayer().repaint();
		}
	}

	private void generateCabling()
	{
		MapNodeProtoElement proto = super.selectNodeProto();

		if(proto != null)
		{
			List nodesToBind = new LinkedList();
			for(Iterator it = selection.getElements().iterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				if(me instanceof MapUnboundNodeElement)
				{
					nodesToBind.add(me);
					it.remove();
				}
			}
	
			if(!nodesToBind.isEmpty())
			{
				for(Iterator it = nodesToBind.iterator(); it.hasNext();)
				{
					MapUnboundNodeElement un = (MapUnboundNodeElement )it.next();
					super.convertUnboundNodeToSite(un, proto);
				}
			}
	
			List alreadyBound = new LinkedList();
			for(Iterator it = selection.getElements().iterator(); it.hasNext();)
			{
				MapElement me = (MapElement )it.next();
				if(me instanceof MapCablePathElement)
				{
					MapCablePathElement path = (MapCablePathElement )me;
					if(!alreadyBound.contains(path))
					{
						super.generatePathCabling(path, proto);
						alreadyBound.add(path);
					}
				}
				else
				if(me instanceof MapUnboundLinkElement)
				{
					MapCablePathElement path = ((MapUnboundLinkElement )me).getCablePath();
					if(!alreadyBound.contains(path))
					{
						super.generatePathCabling(path, proto);
						alreadyBound.add(path);
					}
				}
			}
		}
	}

	private void newCollector()
	{
		MapPipePathElement collector = super.createCollector();
		if(collector != null)
		{
			super.addLinksToCollector(collector, selection.getElements());

			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	private void addToCollector()
	{
		MapPipePathElement collector = super.selectCollector();
		if(collector != null)
		{
			super.addLinksToCollector(collector, selection.getElements());
			
			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	private void removeFromCollector()
	{
		for(Iterator it = selection.getElements().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			MapPipePathElement collector = logicalNetLayer.getMapView().getMap().getCollector(link);
			if(collector != null)
			{
				super.removeLinkFromCollector(collector, link);
			}
		}
		
		getLogicalNetLayer().repaint();

		getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

	private void removeCollector()
	{
		for(Iterator it = selection.getElements().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			MapPipePathElement collector = logicalNetLayer.getMapView().getMap().getCollector(link);
			if(collector != null)
			{
				super.removeLinksFromCollector(collector, collector.getLinks());
				super.removeCollector(collector);
			}
		}

		getLogicalNetLayer().repaint();

		getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateMarkCommandAtomic;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;

public final class LinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();

	private PhysicalLink link;
	
	private static LinkPopupMenu instance = new LinkPopupMenu();

	private LinkPopupMenu()
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
	
	public static LinkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.link = (PhysicalLink)me;

		Collector collector = this.logicalNetLayer.getMapView().getMap().getCollector(this.link);
		this.addToCollectorMenuItem.setVisible(collector == null);
		this.newCollectorMenuItem.setVisible(collector == null);
		this.removeCollectorMenuItem.setVisible(collector != null);
		this.removeFromCollectorMenuItem.setVisible(collector != null);
		this.addMarkMenuItem.setVisible(
			getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
					MapApplicationModel.ACTION_EDIT_MAP));
		if(collector != null)
		{
			this.removeCollectorMenuItem.setText(
					LangModelMap.getString("RemoveCollector")
					+ " (" 
					+ collector.getName()
					+ ")");
		this.removeFromCollectorMenuItem.setText(
					LangModelMap.getString("RemoveFromCollector")
					+ " (" 
					+ collector.getName()
					+ ")");
		}
	}

	private void jbInit()
	{
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeLink();
				}
			});
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		this.propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		this.addMarkMenuItem.setText(LangModelMap.getString("AddMark"));
		this.addMarkMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addMark();
				}
			});
		this.newCollectorMenuItem.setText(LangModelMap.getString("CreateCollector"));
		this.newCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newCollector();
				}
			});
		this.removeCollectorMenuItem.setText(LangModelMap.getString("RemoveCollector"));
		this.removeCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCollector();
				}
			});

		this.addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		this.addToCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToCollector();
				}
			});
		this.removeFromCollectorMenuItem.setText(LangModelMap.getString("RemoveFromCollector"));
		this.removeFromCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeFromCollector();
				}
			});

		this.add(this.removeMenuItem);
		this.add(this.addMarkMenuItem);
//		this.addSeparator();
		this.add(this.addToCollectorMenuItem);
		this.add(this.removeFromCollectorMenuItem);
//		this.addSeparator();
		this.add(this.newCollectorMenuItem);
		this.add(this.removeCollectorMenuItem);
		this.addSeparator();
		this.add(this.propertiesMenuItem);
	}

	void showProperties()
	{
		super.showProperties(this.link);
	}

	void removeLink()
	{
		super.removeMapElement(this.link);

		try
		{
			getLogicalNetLayer().repaint(false);
		}
		catch(MapConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void addMark()
	{
		CreateMarkCommandAtomic command = new CreateMarkCommandAtomic(this.link, this.point);
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		try
		{
			getLogicalNetLayer().repaint(false);
		}
		catch(MapConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void newCollector()
	{
		Collector collector = super.createCollector();
		if(collector != null)
		{
			super.addLinkToCollector(collector, this.link);

			try
			{
				getLogicalNetLayer().repaint(false);
			}
			catch(MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	void addToCollector()
	{
		Collector collector = super.selectCollector();
		if(collector != null)
		{
			super.addLinkToCollector(collector, this.link);
			
			try
			{
				getLogicalNetLayer().repaint(false);
			}
			catch(MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	void removeFromCollector()
	{
		Collector collector = this.logicalNetLayer.getMapView().getMap().getCollector(this.link);
		if(collector != null)
		{
			super.removeLinkFromCollector(collector, this.link);
			
			try
			{
				getLogicalNetLayer().repaint(false);
			}
			catch(MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	void removeCollector()
	{
		getLogicalNetLayer().deselectAll();

		Collector collector = this.logicalNetLayer.getMapView().getMap().getCollector(this.link);
		if(collector != null)
		{
			List list = new LinkedList();
			list.addAll(collector.getPhysicalLinks());
			super.removeLinksFromCollector(collector, list);
			super.removeCollector(collector);

			try
			{
				getLogicalNetLayer().repaint(false);
			}
			catch(MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

}

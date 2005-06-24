package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

public final class SelectionPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem insertSiteMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();
	
	private static SelectionPopupMenu instance = new SelectionPopupMenu();
	
	Selection selection;	

	private SelectionPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setElement(Object me) {
		this.selection = (Selection)me;

		this.insertSiteMenuItem.setVisible(this.selection.isPhysicalNodeSelection());
		this.generateMenuItem.setVisible(this.selection.isUnboundSelection());
		this.newCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.addToCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.removeCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.removeFromCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
	}
	
	public static SelectionPopupMenu getInstance() {
		return instance;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelection();
			}
		});
		this.insertSiteMenuItem.setText(LangModelMap.getString("PlaceSite"));
		this.insertSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertSite();
			}
		});
		this.generateMenuItem.setText(LangModelMap.getString("GenerateCabling"));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCabling();
			}
		});
		this.newCollectorMenuItem.setText(LangModelMap.getString("CreateCollector"));
		this.newCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newCollector();
			}
		});
		this.removeCollectorMenuItem.setText(LangModelMap.getString("RemoveCollector"));
		this.removeCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeCollector();
			}
		});
		this.addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		this.addToCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToCollector();
			}
		});
		this.removeFromCollectorMenuItem.setText(LangModelMap.getString("RemoveFromCollector"));
		this.removeFromCollectorMenuItem
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeFromCollector();
					}
				});
		this.add(this.removeMenuItem);
		this.add(this.insertSiteMenuItem);
		this.add(this.generateMenuItem);
		//		this.addSeparator();
		this.add(this.addToCollectorMenuItem);
		this.add(this.removeFromCollectorMenuItem);
		//		this.addSeparator();
		this.add(this.newCollectorMenuItem);
		this.add(this.removeCollectorMenuItem);
	}

	void removeSelection() {
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setNetMapViewer(this.netMapViewer);
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void insertSite() {
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null) {
			for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
				TopologicalNode node = (TopologicalNode )it.next();
				super.insertSiteInPlaceOfANode(node, proto);
			}

			try {
				this.netMapViewer.repaint(false);
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void generateCabling() {
		SiteNodeType proto = super.selectNodeProto();

		if(proto != null) {
			List nodesToBind = new LinkedList();
			for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
				MapElement me = (MapElement )it.next();
				if(me instanceof UnboundNode) {
					nodesToBind.add(me);
					it.remove();
				}
			}

			if(!nodesToBind.isEmpty()) {
				for(Iterator it = nodesToBind.iterator(); it.hasNext();) {
					UnboundNode un = (UnboundNode )it.next();
					super.convertUnboundNodeToSite(un, proto);
				}
			}

			List alreadyBound = new LinkedList();
			for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
				MapElement me = (MapElement )it.next();
				if(me instanceof CablePath) {
					CablePath path = (CablePath )me;
					if(!alreadyBound.contains(path)) {
						super.generatePathCabling(path, proto);
						alreadyBound.add(path);
					}
				}
				else
					if(me instanceof UnboundLink) {
						CablePath path = ((UnboundLink )me).getCablePath();
						if(!alreadyBound.contains(path)) {
							super.generatePathCabling(path, proto);
							alreadyBound.add(path);
						}
					}
			}
		}
	}

	void newCollector() {
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			try {
				this.netMapViewer.repaint(false);
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() {
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			try {
				this.netMapViewer.repaint(false);
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() {
		for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			Collector collector = 
				this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(link);
			if(collector != null) {
				super.removeLinkFromCollector(collector, link);
			}
		}

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void removeCollector() {
		for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			Collector collector = 
				this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(link);
			if(collector != null) {
				super.removeLinksFromCollector(collector, collector
						.getPhysicalLinks());
				super.removeCollector(collector);
			}
		}

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

}

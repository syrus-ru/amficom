package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

public final class MapSiteBindPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private MapSiteNodeElement site;
	private JLabel titleLabel = new JLabel();
	private JTree elementsTree;

	private JPanel jPanel1 = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();

	private UgoPanel schemePanel = new UgoPanel(null);
	
	private List unboundElements = new LinkedList();

	public MapSiteBindPanel()
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
		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("SiteBinding"));
		titleLabel.setText(LangModelMap.getString("SiteBinding"));

		elementsTree = new JTree(createTree(null));
		elementsTree.expandRow(0);
		elementsTree.setCellRenderer(new SiteBindingRenderer());

		elementsTree.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);

		elementsTree.addTreeSelectionListener(new TreeSelectionListener()
			{
				public void valueChanged(TreeSelectionEvent e)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							elementsTree.getLastSelectedPathComponent();
					if(node == null)
						showElement(null);
					else
						showElement(node.getUserObject());
				}
			});

		bindButton.setText("Привязать");
		bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
//					ObjectResource or = elementsList.getSelectedObjectResource();
//					bind(or);
				}
			});
		unbindButton.setText("Отвязать");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							elementsTree.getLastSelectedPathComponent();
					unbindElement(node.getUserObject());
				}
			});
//		jPanel1.add(bindButton, null);
		jPanel1.add(unbindButton, null);

		schemePanel.getGraph().setGraphEditable(false);
		
		JScrollPane treeView = new JScrollPane(elementsTree);

		this.add(titleLabel, ReusedGridBagConstraints.get(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(treeView, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 150, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
		this.add(schemePanel, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
//		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(jPanel1, ReusedGridBagConstraints.get(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	private void unbindSchemeElement(SchemeElement se)
	{
		MapView mapView = ((LogicalNetLayer )(site.getMap().getConverter())).getMapView();

		se.siteId = "";
		for (int i = 0; i < elementsBranch.getChildCount(); i++) 
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode )elementsBranch.getChildAt(i);
			if(node.getUserObject().equals(se))
			{
				elementsBranch.remove(node);
				for (int j = 0; j < node.getChildCount(); j++) 
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode )node.getChildAt(j);
					SchemeCableLink scl = (SchemeCableLink )node2.getUserObject();
					MapCablePathElement cablePath = mapView.findCablePath(scl);

					UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(cablePath);
					command.setLogicalNetLayer(mapView.getLogicalNetLayer());
					command.execute();
				}
				break;
			}
		}
		
		unboundElements.add(se);

		elementsTree.updateUI();
	}

	private void unbindSchemeCableLink(SchemeCableLink scl)
	{
		MapView mapView = ((LogicalNetLayer )(site.getMap().getConverter())).getMapView();

		for (int i = 0; i < cablesBranch.getChildCount(); i++) 
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode )cablesBranch.getChildAt(i);
			if(node.getUserObject().equals(scl))
			{
				cablesBranch.remove(node);
				break;
			}
		}

		unboundElements.add(scl);
	
		MapCablePathElement cablePath = mapView.findCablePath(scl);

		List pathLinks = cablePath.getLinks();
		List siteLinks = mapView.getMap().getPhysicalLinksAt(site);

		MapPhysicalLinkElement linkRight = null;
		MapPhysicalLinkElement linkLeft = null;

		for(Iterator it = pathLinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			if(siteLinks.contains(mple))
			{
				if(linkLeft == null)
				{
					linkLeft = mple;
				}
				else
				{
					linkRight = mple;
					break;
				}
			}
		}

		cablePath.removeLink(linkRight);
		
		if(linkRight instanceof MapUnboundLinkElement)
		{
			RemoveUnboundLinkCommandBundle command = 
					new RemoveUnboundLinkCommandBundle(
						(MapUnboundLinkElement )linkRight);
			command.setLogicalNetLayer(mapView.getLogicalNetLayer());
			command.execute();
		}
		else
		{
			linkRight.getBinding().remove(cablePath);
		}

		if(linkLeft instanceof MapUnboundLinkElement)
		{
			if(linkLeft.getStartNode().equals(site))
				linkLeft.setStartNode(linkRight.getOtherNode(site));
			else
				linkLeft.setEndNode(linkRight.getOtherNode(site));
		}
		else
		{
			cablePath.removeLink(linkLeft);
			linkLeft.getBinding().remove(cablePath);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					linkLeft.getOtherNode(site),
					linkRight.getOtherNode(site));
			command.setLogicalNetLayer(mapView.getLogicalNetLayer());
			command.execute();

			MapUnboundLinkElement unbound = command.getUnbound();
			unbound.setCablePath(cablePath);
			cablePath.addLink(unbound);
		}
		
		elementsTree.updateUI();
	}

	private void unbindElement(Object or)
	{
		MapView mapView = ((LogicalNetLayer )(site.getMap().getConverter())).getMapView();
			
		if(or instanceof SchemeElement)
		{
			SchemeElement se = (SchemeElement )or;
			unbindSchemeElement(se);
		}
		else
		if(or instanceof SchemeCableLink)
		{
			SchemeCableLink scl = (SchemeCableLink )or;
			unbindSchemeCableLink(scl);
		}
	}

	private void showElement(Object element)
	{
		boolean sen = false;
		if(element != null)
		{
			if(element instanceof SchemeElement)
			{
//				schemePanel.openSchemeElement(se);
				sen = true;
			}
			else
			if(element instanceof SchemeCableLink)
			{
				SchemeCableLink scl = (SchemeCableLink )element;
				MapView mapView = ((LogicalNetLayer )(site.getMap().getConverter())).getMapView();
				MapNodeElement mne[] = mapView.getSideNodes(scl);
				sen = !(mne[0].equals(site)) && !(mne[1].equals(site));
			}
		}
		unbindButton.setEnabled(sen);
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		site = (MapSiteNodeElement )objectResource;
		createTree(site);
		if(site == null)
		{
			schemePanel.getGraph().removeAll();
		}
		else
		{
		}
		elementsTree.updateUI();
	}

	public void setContext(ApplicationContext aContext)
	{
//		schemePanel.setContext(aContext);
	}

	public boolean modify()
	{
		try 
		{
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}


	public List getUnboundElements()
	{
		return unboundElements;
	}

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Объекты в узле");
	DefaultMutableTreeNode elementsBranch = new DefaultMutableTreeNode("Элементы");
	DefaultMutableTreeNode cablesBranch = new DefaultMutableTreeNode("Кабели");
	
	{
		root.add(elementsBranch);
		root.add(cablesBranch);
	}

	private DefaultMutableTreeNode createTree(MapSiteNodeElement site)
	{
		elementsBranch.removeAllChildren();
		cablesBranch.removeAllChildren();
		if(site != null)
		{
			DefaultMutableTreeNode elementNode;
			DefaultMutableTreeNode cableNode;

			MapView mapView = ((LogicalNetLayer )(site.getMap().getConverter())).getMapView();

			List schemeElements = Pool.getList(SchemeElement.typ);
			List cableElementsTransit = mapView.getCablePaths(site);
			List cableElementsDropped = new LinkedList();
			for(Iterator it = cableElementsTransit.iterator(); it.hasNext();)
			{
				MapCablePathElement cablePath = (MapCablePathElement )it.next();
				if(cablePath.getStartNode().equals(site)
					|| cablePath.getEndNode().equals(site))
				{
					cableElementsDropped.add(cablePath);
					it.remove();
				}
			}

			if(schemeElements != null)
			{
				for(Iterator it = schemeElements.iterator(); it.hasNext();)
				{
					SchemeElement se = (SchemeElement )it.next();
					if(se.siteId.equals(site.getId()))
					{
						elementNode = new DefaultMutableTreeNode(se);
						elementsBranch.add(elementNode);
						for(Iterator it2 = cableElementsDropped.iterator(); it2.hasNext();)
						{
							MapCablePathElement cablePath = (MapCablePathElement )it2.next();
							if(startsAt(cablePath.getSchemeCableLink(), se))
							{
								cableNode = new DefaultMutableTreeNode(cablePath.getSchemeCableLink());
								elementNode.add(cableNode);
							}
						}
					}
				}
			}
			
			if(cableElementsTransit != null)
			{
				for(Iterator it = cableElementsTransit.iterator(); it.hasNext();)
				{
					MapCablePathElement cablePath = (MapCablePathElement )it.next();
					cableNode = new DefaultMutableTreeNode(cablePath.getSchemeCableLink());
					cablesBranch.add(cableNode);
				}
			}
		}
	
		return root;
	}
	
	private boolean startsAt(SchemeCableLink scl, SchemeElement se)
	{
		return se.getCablePort(scl.sourcePortId) != null
			|| se.getCablePort(scl.targetPortId) != null;
	}
}

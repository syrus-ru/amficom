package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

public final class MapSiteBindPanel 
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private SiteNode site;

	private LogicalNetLayer lnl;

	private JLabel titleLabel = new JLabel();
	private JTree elementsTree;

	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();

	private UgoPanel schemePanel = new UgoPanel(null);
	
	private SiteCrossingPanel crossingPanel = new SiteCrossingPanel();
	private JScrollPane crossingScrollPane = new JScrollPane();
	
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

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return lnl;
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
//					Object or = elementsList.getSelectedObjectResource();
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
		buttonsPanel.add(unbindButton, null);

		schemePanel.getGraph().setGraphEditable(false);
		
		JScrollPane treeView = new JScrollPane(elementsTree);
		
		crossingScrollPane.getViewport().add(crossingPanel);

		this.add(titleLabel, ReusedGridBagConstraints.get(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(treeView, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 150, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
		this.add(schemePanel, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(crossingScrollPane, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
//		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(buttonsPanel, ReusedGridBagConstraints.get(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		
		schemePanel.setVisible(false);
		crossingScrollPane.setVisible(true);
	}

	public Object getObject()
	{
		return null;
	}

	private void unbindSchemeElement(SchemeElement se)
	{
		MapView mapView = getLogicalNetLayer().getMapView();

		se.siteNodeImpl(null);
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
					CablePath cablePath = mapView.findCablePath(scl);

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
		MapView mapView = getLogicalNetLayer().getMapView();

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
	
		CablePath cablePath = mapView.findCablePath(scl);

		List pathLinks = cablePath.getLinks();
		List siteLinks = mapView.getMap().getPhysicalLinksAt(site);

		PhysicalLink linkRight = null;
		PhysicalLink linkLeft = null;

		for(Iterator it = pathLinks.iterator(); it.hasNext();)
		{
			PhysicalLink mple = (PhysicalLink)it.next();
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
		
		if(linkRight instanceof UnboundLink)
		{
			RemoveUnboundLinkCommandBundle command = 
					new RemoveUnboundLinkCommandBundle(
						(UnboundLink)linkRight);
			command.setLogicalNetLayer(mapView.getLogicalNetLayer());
			command.execute();
		}
		else
		{
			linkRight.getBinding().remove(cablePath);
		}

		if(linkLeft instanceof UnboundLink)
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

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(cablePath);
			cablePath.addLink(unbound);
		}
		
		elementsTree.updateUI();
	}

	private void unbindElement(Object or)
	{
		MapView mapView = getLogicalNetLayer().getMapView();
			
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
				SchemeElement se = (SchemeElement )element;
//				schemePanel.getGraph().setSchemeElement(se);
				crossingScrollPane.setVisible(false);
				schemePanel.setVisible(true);
				sen = true;
			}
			else
			if(element instanceof SchemeCableLink)
			{
				SchemeCableLink scl = (SchemeCableLink )element;

				schemePanel.setVisible(false);
				crossingScrollPane.setVisible(true);
				
				MapView mapView = getLogicalNetLayer().getMapView();

				crossingPanel.setCable(mapView.findCablePath(scl));

				AbstractNode mne[] = mapView.getSideNodes(scl);
				sen = !(mne[0].equals(site)) && !(mne[1].equals(site));
			}
			else
			{
				schemePanel.setVisible(false);
				crossingPanel.setCable(null);
				crossingScrollPane.setVisible(true);
			}
		}
		else
		{
			schemePanel.setVisible(false);
			crossingPanel.setCable(null);
			crossingScrollPane.setVisible(true);
		}
		unbindButton.setEnabled(sen);
	}

	public void setObject(Object objectResource)
	{
		site = (SiteNode)objectResource;
		createTree(site);
		schemePanel.getGraph().removeAll();
		crossingPanel.setSite(site);

		schemePanel.setVisible(false);
		crossingPanel.setCable(null);
		crossingScrollPane.setVisible(true);

		if(site == null)
		{
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

	private DefaultMutableTreeNode createTree(SiteNode site)
	{
		this.
		elementsBranch.removeAllChildren();
		cablesBranch.removeAllChildren();
		if(site != null)
		{
			DefaultMutableTreeNode elementNode;
			DefaultMutableTreeNode cableNode;

			MapView mapView = getLogicalNetLayer().getMapView();

			List schemeElements = new LinkedList();
			for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
			{
				Scheme scheme = (Scheme )it.next();
				schemeElements.addAll(SchemeUtils.getTopLevelElements(scheme));
			}
			List cableElementsTransit = mapView.getCablePaths(site);
			List cableElementsDropped = new LinkedList();
			for(Iterator it = cableElementsTransit.iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();
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
					if(se.siteNodeImpl().equals(site.getId()))
					{
						elementNode = new DefaultMutableTreeNode(se);
						elementsBranch.add(elementNode);
						for(Iterator it2 = cableElementsDropped.iterator(); it2.hasNext();)
						{
							CablePath cablePath = (CablePath)it2.next();
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
					CablePath cablePath = (CablePath)it.next();
					cableNode = new DefaultMutableTreeNode(cablePath.getSchemeCableLink());
					cablesBranch.add(cableNode);
				}
			}
		}
	
		return root;
	}
	
	private boolean startsAt(SchemeCableLink scl, SchemeElement se)
	{
		return SchemeUtils.getCablePorts(se).contains(scl.sourceSchemeCablePort())
			|| SchemeUtils.getCablePorts(se).contains(scl.targetSchemeCablePort());
	}
}

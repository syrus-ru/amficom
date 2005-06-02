package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public final class SiteNodeAddEditor extends DefaultStorableObjectEditor
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();
	private JScrollPane treeScrollPane = new JScrollPane();
	JTree elementsTree;
	
	private SiteCrossingPanel crossingPanel = new SiteCrossingPanel();
	private JScrollPane crossingScrollPane = new JScrollPane();

	private UgoPanel schemePanel = new UgoPanel(null);

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Объекты в узле");
	DefaultMutableTreeNode elementsBranch = new DefaultMutableTreeNode("Элементы");
	DefaultMutableTreeNode cablesBranch = new DefaultMutableTreeNode("Кабели");
	
	private List unboundElements = new LinkedList();

	private SiteNode site;

	private LogicalNetLayer logicalNetLayer;

	public SiteNodeAddEditor()
	{
		this.root.add(this.elementsBranch);
		this.root.add(this.cablesBranch);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.crossingPanel.setMap(logicalNetLayer.getMapView().getMap());
	}

	private void jbInit()
	{
		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelMap.getString("SiteBinding"));

		this.elementsTree = new JTree(createTree(null));
		this.elementsTree.expandRow(0);
		this.elementsTree.setCellRenderer(new SiteBindingRenderer());

		this.elementsTree.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);

		this.elementsTree.addTreeSelectionListener(new TreeSelectionListener()
			{
				public void valueChanged(TreeSelectionEvent e)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							SiteNodeAddEditor.this.elementsTree.getLastSelectedPathComponent();
					if(node == null)
						showElement(null);
					else
						showElement(node.getUserObject());
				}
			});

		this.bindButton.setText("Привязать");
		this.bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
//					Object or = elementsList.getSelectedObjectResource();
//					bind(or);
				}
			});
		this.unbindButton.setText("Отвязать");
		this.unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							SiteNodeAddEditor.this.elementsTree.getLastSelectedPathComponent();
					unbindElement(node.getUserObject());
				}
			});
//		jPanel1.add(bindButton, null);
		this.buttonsPanel.add(this.unbindButton, null);

		this.treeScrollPane.getViewport().add(this.elementsTree);
		this.crossingScrollPane.getViewport().add(this.crossingPanel);
		this.schemePanel.getGraph().setGraphEditable(false);

		this.jPanel.add(this.treeScrollPane, ReusedGridBagConstraints.get(0, 0, 1, 2, 1.0, 0.3, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.jPanel.add(this.buttonsPanel, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(Box.createVerticalStrut(5), ReusedGridBagConstraints.get(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.crossingScrollPane, ReusedGridBagConstraints.get(0, 3, 2, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.jPanel.add(this.schemePanel.getGraph(), ReusedGridBagConstraints.get(0, 3, 2, 1, 1.0, 0.7, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		
		this.schemePanel.getGraph().setVisible(false);
		this.crossingScrollPane.setVisible(true);
	}

	public Object getObject()
	{
		return this.site;
	}

	private void unbindSchemeElement(SchemeElement se)
	{
		MapView mapView = this.logicalNetLayer.getMapView();

		se.setSiteNode(null);
		for (int i = 0; i < this.elementsBranch.getChildCount(); i++) 
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode )this.elementsBranch.getChildAt(i);
			if(node.getUserObject().equals(se))
			{
				this.elementsBranch.remove(node);
				for (int j = 0; j < node.getChildCount(); j++) 
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode )node.getChildAt(j);
					SchemeCableLink scl = (SchemeCableLink )node2.getUserObject();
					CablePath cablePath = mapView.findCablePath(scl);

					UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(cablePath);
					command.setLogicalNetLayer(this.logicalNetLayer);
					command.execute();
				}
				break;
			}
		}
		
		this.unboundElements.add(se);

		this.elementsTree.updateUI();
	}

	private void unbindSchemeCableLink(SchemeCableLink scl)
	{
		MapView mapView = this.logicalNetLayer.getMapView();

		for (int i = 0; i < this.cablesBranch.getChildCount(); i++) 
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode )this.cablesBranch.getChildAt(i);
			if(node.getUserObject().equals(scl))
			{
				this.cablesBranch.remove(node);
				break;
			}
		}

		this.unboundElements.add(scl);
	
		CablePath cablePath = mapView.findCablePath(scl);

		List cablePathLinks = cablePath.getLinks();
		Collection siteLinks = mapView.getMap().getPhysicalLinksAt(this.site);

		PhysicalLink linkRight = null;
		PhysicalLink linkLeft = null;

		for(Iterator it = cablePathLinks.iterator(); it.hasNext();)
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
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();
		}
		else
		{
			linkRight.getBinding().remove(cablePath);
		}

		if(linkLeft instanceof UnboundLink)
		{
			if(linkLeft.getStartNode().equals(this.site))
				linkLeft.setStartNode(linkRight.getOtherNode(this.site));
			else
				linkLeft.setEndNode(linkRight.getOtherNode(this.site));
		}
		else
		{
			cablePath.removeLink(linkLeft);
			linkLeft.getBinding().remove(cablePath);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					linkLeft.getOtherNode(this.site),
					linkRight.getOtherNode(this.site));
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(cablePath);

			cablePath.addLink(unbound, CableController.generateCCI(cablePath, unbound, this.logicalNetLayer.getUserId()));
		}
		
		this.elementsTree.updateUI();
	}

	void unbindElement(Object or)
	{
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

	void showElement(Object element)
	{
		boolean sen = false;
		if(element != null)
		{
			if(element instanceof SchemeElement)
			{
				SchemeElement schemeElement = (SchemeElement )element;
				this.schemePanel.getSchemeResource().setSchemeElement(schemeElement);
				this.crossingScrollPane.setVisible(false);
				this.schemePanel.getGraph().setVisible(true);
				sen = true;
			}
			else
			if(element instanceof SchemeCableLink)
			{
				this.schemePanel.getGraph().setVisible(false);
				this.crossingScrollPane.setVisible(true);
				
				SchemeCableLink scl = (SchemeCableLink )element;

				MapView mapView = this.logicalNetLayer.getMapView();

				this.crossingPanel.setCable(mapView.findCablePath(scl));

				AbstractNode startNode = mapView.getStartNode(scl);
				AbstractNode endNode = mapView.getEndNode(scl);
				sen = !(startNode.equals(this.site)) && !(endNode.equals(this.site));
			}
			else
			{
				this.schemePanel.getGraph().setVisible(false);
				this.crossingPanel.setCable(null);
				this.crossingScrollPane.setVisible(true);
			}
		}
		else
		{
			this.schemePanel.getGraph().setVisible(false);
			this.crossingPanel.setCable(null);
			this.crossingScrollPane.setVisible(true);
		}
		this.unbindButton.setEnabled(sen);
	}

	public void setObject(Object objectResource)
	{
		this.site = (SiteNode)objectResource;
		createTree(this.site);
		this.schemePanel.getGraph().removeAll();
		this.crossingPanel.setSite(this.site);

		this.schemePanel.getGraph().setVisible(false);
		this.crossingPanel.setCable(null);
		this.crossingScrollPane.setVisible(true);

		this.elementsTree.updateUI();
	}

	public List getUnboundElements()
	{
		return this.unboundElements;
	}

	private DefaultMutableTreeNode createTree(SiteNode siteNode)
	{
		this.elementsBranch.removeAllChildren();
		this.cablesBranch.removeAllChildren();
		if(siteNode != null)
		{
			DefaultMutableTreeNode elementNode;
			DefaultMutableTreeNode cableNode;

			MapView mapView = this.logicalNetLayer.getMapView();

			List schemeElements = new LinkedList();
			for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
			{
				Scheme scheme = (Scheme )it.next();
				schemeElements.addAll(scheme.getSchemeElements());
			}
			List cableElementsTransit = mapView.getCablePaths(siteNode);
			List cableElementsDropped = new LinkedList();
			for(Iterator it = cableElementsTransit.iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();
				if(cablePath.getStartNode().equals(siteNode)
					|| cablePath.getEndNode().equals(siteNode))
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
					if(se.getSiteNode().equals(siteNode.getId()))
					{
						elementNode = new DefaultMutableTreeNode(se);
						this.elementsBranch.add(elementNode);
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
					this.cablesBranch.add(cableNode);
				}
			}
		}
	
		return this.root;
	}
	
	private boolean startsAt(final SchemeCableLink schemeCableLink, final SchemeElement schemeElement)
	{
		final Set schemeCablePorts = schemeElement.getSchemeCablePorts();
		return schemeCablePorts.contains(schemeCableLink.getSourceSchemeCablePort())
			|| schemeCablePorts.contains(schemeCableLink.getTargetSchemeCablePort());
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// nothing to commit
	}
}

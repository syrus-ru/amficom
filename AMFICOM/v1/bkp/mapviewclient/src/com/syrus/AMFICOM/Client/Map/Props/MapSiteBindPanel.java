package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public final class MapSiteBindPanel 
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private SiteNode site;

	private LogicalNetLayer lnl;

	private JLabel titleLabel = new JLabel();
	JTree elementsTree;

	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();

	private UgoPanel schemePanel = new UgoPanel(null);
	
	private SiteCrossingPanel crossingPanel = new SiteCrossingPanel();
	private JScrollPane crossingScrollPane = new JScrollPane();
	
	private List unboundElements = new LinkedList();

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Объекты в узле");
	DefaultMutableTreeNode elementsBranch = new DefaultMutableTreeNode("Элементы");
	DefaultMutableTreeNode cablesBranch = new DefaultMutableTreeNode("Кабели");
	
	public MapSiteBindPanel()
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

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	private void jbInit()
	{
		this.setLayout(this.gridBagLayout1);
		this.setName(LangModelMap.getString("SiteBinding"));
		this.titleLabel.setText(LangModelMap.getString("SiteBinding"));

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
							MapSiteBindPanel.this.elementsTree.getLastSelectedPathComponent();
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
							MapSiteBindPanel.this.elementsTree.getLastSelectedPathComponent();
					unbindElement(node.getUserObject());
				}
			});
//		jPanel1.add(bindButton, null);
		this.buttonsPanel.add(this.unbindButton, null);

		this.schemePanel.getGraph().setGraphEditable(false);
		
		JScrollPane treeView = new JScrollPane(this.elementsTree);
		
		this.crossingScrollPane.getViewport().add(this.crossingPanel);

		this.add(this.titleLabel, ReusedGridBagConstraints.get(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(treeView, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 150, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
		this.add(this.schemePanel, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(this.crossingScrollPane, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
//		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(this.buttonsPanel, ReusedGridBagConstraints.get(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		
		this.schemePanel.setVisible(false);
		this.crossingScrollPane.setVisible(true);
	}

	public Object getObject()
	{
		return null;
	}

	private void unbindSchemeElement(SchemeElement se)
	{
		MapView mapView = getLogicalNetLayer().getMapView();

		se.siteNodeImpl(null);
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
					command.setLogicalNetLayer(getLogicalNetLayer());
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
		MapView mapView = getLogicalNetLayer().getMapView();

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

		List pathLinks = cablePath.getLinks();
		List siteLinks = mapView.getMap().getPhysicalLinksAt(this.site);

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
			command.setLogicalNetLayer(getLogicalNetLayer());
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
			command.setLogicalNetLayer(getLogicalNetLayer());
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(cablePath);

			cablePath.addLink(unbound, CableController.generateCCI(unbound));
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
				SchemeElement se = (SchemeElement )element;
//				schemePanel.getGraph().setSchemeElement(se);
				this.crossingScrollPane.setVisible(false);
				this.schemePanel.setVisible(true);
				sen = true;
			}
			else
			if(element instanceof SchemeCableLink)
			{
				SchemeCableLink scl = (SchemeCableLink )element;

				this.schemePanel.setVisible(false);
				this.crossingScrollPane.setVisible(true);
				
				MapView mapView = getLogicalNetLayer().getMapView();

				this.crossingPanel.setCable(mapView.findCablePath(scl));

				AbstractNode startNode = mapView.getStartNode(scl);
				AbstractNode endNode = mapView.getEndNode(scl);
				sen = !(startNode.equals(this.site)) && !(endNode.equals(this.site));
			}
			else
			{
				this.schemePanel.setVisible(false);
				this.crossingPanel.setCable(null);
				this.crossingScrollPane.setVisible(true);
			}
		}
		else
		{
			this.schemePanel.setVisible(false);
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

		this.schemePanel.setVisible(false);
		this.crossingPanel.setCable(null);
		this.crossingScrollPane.setVisible(true);

		if(this.site == null)
		{
		}
		else
		{
		}

		this.elementsTree.updateUI();
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
		return this.unboundElements;
	}

	private DefaultMutableTreeNode createTree(SiteNode site)
	{
		this.elementsBranch.removeAllChildren();
		this.cablesBranch.removeAllChildren();
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
	
	private boolean startsAt(SchemeCableLink scl, SchemeElement se)
	{
		return SchemeUtils.getCablePorts(se).contains(scl.sourceSchemeCablePort())
			|| SchemeUtils.getCablePorts(se).contains(scl.targetSchemeCablePort());
	}
}

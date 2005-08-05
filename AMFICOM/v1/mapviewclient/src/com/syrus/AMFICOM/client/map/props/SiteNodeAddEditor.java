package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;

public final class SiteNodeAddEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();
	JButton selectButton = new JButton();

	private JScrollPane treeScrollPane = new JScrollPane();
	JTree elementsTree;
	
	private SiteCrossingPanel crossingPanel = new SiteCrossingPanel();
	private JScrollPane crossingScrollPane = new JScrollPane();
//			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
//			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

	private UgoTabbedPane schemePane = new UgoTabbedPane();

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("������� � ����");
	DefaultMutableTreeNode elementsBranch = new DefaultMutableTreeNode("��������");
	DefaultMutableTreeNode cablesBranch = new DefaultMutableTreeNode("������");
	
	private JButton commitButton = new JButton();

	private List unboundElements = new LinkedList();

	private SiteNode site;

	private LogicalNetLayer logicalNetLayer;

	private NetMapViewer netMapViewer;

	private static Dimension buttonSize = new Dimension(24, 24);

	public SiteNodeAddEditor() {
		this.root.add(this.elementsBranch);
		this.root.add(this.cablesBranch);

		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		this.crossingPanel.setMap(this.logicalNetLayer.getMapView().getMap());
	}

	private void jbInit() {
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
					SiteNodeAddEditor.this.selectButton.setEnabled(node != null);
				}
			});

		this.bindButton.setText("���������");
		this.bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
//					Object or = elementsList.getSelectedObjectResource();
//					bind(or);
				}
			});
		this.unbindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/delete.gif")));
		this.unbindButton.setToolTipText("������ ��������");
		this.unbindButton.setPreferredSize(buttonSize);
		this.unbindButton.setMaximumSize(buttonSize);
		this.unbindButton.setMinimumSize(buttonSize);
		this.unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							SiteNodeAddEditor.this.elementsTree.getLastSelectedPathComponent();
					try {
						unbindElement(node.getUserObject());
					} catch(ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		this.selectButton.setToolTipText("������� �������");
		this.selectButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/selectlink.gif")));
		this.selectButton.setPreferredSize(buttonSize);
		this.selectButton.setMaximumSize(buttonSize);
		this.selectButton.setMinimumSize(buttonSize);
		this.selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				SiteNodeAddEditor.this.elementsTree.getLastSelectedPathComponent();
				selectElement(node.getUserObject());
			}
		});
		this.buttonsPanel.setLayout(new GridBagLayout());

		this.treeScrollPane.getViewport().add(this.elementsTree);
		this.crossingScrollPane.getViewport().add(this.crossingPanel);
		this.schemePane.getGraph().setGraphEditable(false);
		
//		this.crossingScrollPane.setMinimumSize(new Dimension(50, 100));
		this.crossingPanel.setPreferredSize(new Dimension(50, 100));

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx =  0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.commitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.unbindButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.selectButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 1.0;
		constraints.weighty = 0.3;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.treeScrollPane, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.buttonsPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(Box.createVerticalStrut(5), constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.7;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.crossingScrollPane, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.7;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.schemePane.getGraph(), constraints);
		
		this.schemePane.getGraph().setVisible(false);
		this.crossingScrollPane.setVisible(true);
	}

	public Object getObject() {
		return this.site;
	}

	private void unbindSchemeElement(SchemeElement se) {
		MapView mapView = this.logicalNetLayer.getMapView();

		se.setSiteNode(null);
		for (int i = 0; i < this.elementsBranch.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode )this.elementsBranch.getChildAt(i);
			if(node.getUserObject().equals(se)) {
				this.elementsBranch.remove(node);
				for (int j = 0; j < node.getChildCount(); j++) {
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode )node.getChildAt(j);
					SchemeCableLink scl = (SchemeCableLink )node2.getUserObject();
					CablePath cablePath = mapView.findCablePath(scl);

					UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(cablePath);
					command.setNetMapViewer(this.netMapViewer);
					command.execute();
				}
				break;
			}
		}
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		
		this.unboundElements.add(se);

		this.elementsTree.updateUI();
	}

	private void unbindSchemeCableLink(SchemeCableLink scl) throws ApplicationException {
		MapView mapView = this.logicalNetLayer.getMapView();

		for(int i = 0; i < this.cablesBranch.getChildCount(); i++) {
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

		for(Iterator it = cablePathLinks.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			if(siteLinks.contains(link)) {
				if(linkLeft == null) {
					linkLeft = link;
				}
				else {
					linkRight = link;
					break;
				}
			}
		}

		CableChannelingItem leftCableChannelingItem = cablePath.getFirstCCI(linkLeft);
		CableChannelingItem rightCableChannelingItem = cablePath.getFirstCCI(linkRight);
		
		rightCableChannelingItem.setParentPathOwner(null, false);
		cablePath.removeLink(rightCableChannelingItem);

		if(linkRight instanceof UnboundLink) {
			RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(
					(UnboundLink )linkRight);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
		}
		else {
			linkRight.getBinding().remove(cablePath);
		}

		if(linkLeft instanceof UnboundLink) {
			if(linkLeft.getStartNode().equals(this.site))
				linkLeft.setStartNode(linkRight.getOtherNode(this.site));
			else
				linkLeft.setEndNode(linkRight.getOtherNode(this.site));
		}
		else {
			linkLeft.getBinding().remove(cablePath);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					linkLeft.getOtherNode(this.site),
					linkRight.getOtherNode(this.site));
			command.setNetMapViewer(this.netMapViewer);
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(cablePath);

			CableChannelingItem unboundCableChannelingItem = CableController.generateCCI(cablePath, unbound);
			unboundCableChannelingItem.insertSelfBefore(leftCableChannelingItem);
			leftCableChannelingItem.setParentPathOwner(null, false);
			cablePath.removeLink(leftCableChannelingItem);

			cablePath.addLink(unbound, unboundCableChannelingItem);
		}
		
		this.elementsTree.updateUI();
	}

	void unbindElement(Object or) throws ApplicationException {
		if(or instanceof SchemeElement) {
			SchemeElement se = (SchemeElement )or;
			unbindSchemeElement(se);
		}
		else
			if(or instanceof SchemeCableLink) {
				SchemeCableLink scl = (SchemeCableLink )or;
				unbindSchemeCableLink(scl);
			}
	}

	void showElement(Object element) {
		boolean sen = false;
		if(element != null) {
			if(element instanceof SchemeElement) {
				SchemeElement schemeElement = (SchemeElement )element;
				this.schemePane.openSchemeImageResource(schemeElement.getSchemeCell(), false);
				this.crossingScrollPane.setVisible(false);
				this.schemePane.getGraph().setVisible(true);
				sen = true;
			}
			else
			if(element instanceof SchemeCableLink) {
				this.schemePane.getGraph().setVisible(false);
				this.crossingScrollPane.setVisible(true);
				
				SchemeCableLink scl = (SchemeCableLink )element;

				MapView mapView = this.logicalNetLayer.getMapView();

				this.crossingPanel.setCable(mapView.findCablePath(scl));

				AbstractNode startNode = mapView.getStartNode(scl);
				AbstractNode endNode = mapView.getEndNode(scl);
				sen = !(startNode.equals(this.site))
						&& !(endNode.equals(this.site));
			}
			// TODO demo cheat
//			else
//			if(element.equals("�� 17-221")) {
//				this.schemePanel.getGraph().setVisible(false);
//				this.crossingScrollPane.setVisible(true);
//				
//				this.crossingPanel.setCable(null);
//			}
			//
			else {
				this.schemePane.getGraph().setVisible(false);
				this.crossingPanel.setCable(null);
				this.crossingScrollPane.setVisible(true);
			}
		}
		else {
			this.schemePane.getGraph().setVisible(false);
			this.crossingPanel.setCable(null);
			this.crossingScrollPane.setVisible(true);
		}
		this.unbindButton.setEnabled(sen);
		this.selectButton.setEnabled(sen);
	}

	void selectElement(Object or) {
		MapElement mapElement = (MapElement )or;
		this.netMapViewer.getLogicalNetLayer().getMapView().getMap().clearSelection();
		this.netMapViewer.getLogicalNetLayer().deselectAll();
		this.netMapViewer.getLogicalNetLayer().setCurrentMapElement(mapElement);
		this.netMapViewer.getLogicalNetLayer().getMapView().getMap().setSelected(mapElement, true);
		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}

	public void setObject(Object objectResource) {
		this.site = (SiteNode )objectResource;
		createTree(this.site);
		this.schemePane.getGraph().removeAll();
		this.crossingPanel.setSite(this.site);

		this.schemePane.getGraph().setVisible(false);
		this.crossingPanel.setCable(null);
		this.crossingScrollPane.setVisible(true);

		this.elementsTree.updateUI();
	}

	public List getUnboundElements() {
		return this.unboundElements;
	}

	private DefaultMutableTreeNode createTree(SiteNode siteNode) {
		this.elementsBranch.removeAllChildren();
		this.cablesBranch.removeAllChildren();
		if(siteNode != null) {
			DefaultMutableTreeNode elementNode;
			DefaultMutableTreeNode cableNode;

			MapView mapView = this.logicalNetLayer.getMapView();

			List schemeElements = new LinkedList();
			for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();) {
				Scheme scheme = (Scheme )it.next();
				schemeElements.addAll(scheme.getSchemeElements());
			}
			List cableElementsTransit = mapView.getCablePaths(siteNode);
			List cableElementsDropped = new LinkedList();
			for(Iterator it = cableElementsTransit.iterator(); it.hasNext();) {
				CablePath cablePath = (CablePath )it.next();
				if(cablePath.getStartNode().equals(siteNode)
						|| cablePath.getEndNode().equals(siteNode)) {
					cableElementsDropped.add(cablePath);
					it.remove();
				}
			}

			if(schemeElements != null) {
				for(Iterator it = schemeElements.iterator(); it.hasNext();) {
					SchemeElement se = (SchemeElement )it.next();
					SiteNode elementSiteNode = se.getSiteNode();
					if(elementSiteNode != null && elementSiteNode.equals(siteNode)) {
						elementNode = new DefaultMutableTreeNode(se);
						this.elementsBranch.add(elementNode);
						for(Iterator it2 = cableElementsDropped.iterator(); it2.hasNext();) {
							CablePath cablePath = (CablePath)it2.next();
							if(startsAt(cablePath.getSchemeCableLink(), se)) {
								cableNode = new DefaultMutableTreeNode(cablePath.getSchemeCableLink());
								elementNode.add(cableNode);
							}
						}
					}
				}
			}
			
			if(cableElementsTransit != null) {
				for(Iterator it = cableElementsTransit.iterator(); it.hasNext();) {
					CablePath cablePath = (CablePath )it.next();
					cableNode = new DefaultMutableTreeNode(cablePath
							.getSchemeCableLink());
					this.cablesBranch.add(cableNode);
				}
			}
			// TODO demo cheat!
//			this.cablesBranch.add(new DefaultMutableTreeNode("�� 17-221"));
			//
		}

		return this.root;
	}
	
	private boolean startsAt(
			final SchemeCableLink schemeCableLink,
			final SchemeElement schemeElement) {
		final Set schemeCablePorts = schemeElement.getSchemeCablePortsRecursively();
		return schemeCablePorts.contains(schemeCableLink.getSourceAbstractSchemePort())
			|| schemeCablePorts.contains(schemeCableLink.getTargetAbstractSchemePort());
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// nothing to commit
	}
}

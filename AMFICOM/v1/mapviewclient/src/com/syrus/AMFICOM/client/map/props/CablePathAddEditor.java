package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * @version $Revision: 1.16 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public final class CablePathAddEditor extends DefaultStorableObjectEditor {

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	GridBagLayout gridBagLayout3 = new GridBagLayout();

	private CablePath cablePath;

	LogicalNetLayer logicalNetLayer;
	
	/**
	 * таблица
	 */
	private CableBindingController controller;
	private WrapperedTableModel model;
	WrapperedTable table;

	JPanel jPanel = new JPanel();
	JLabel titleLabel = new JLabel();

	JScrollPane scrollPane = new JScrollPane();
	
	JPanel buttonsPanel = new JPanel();
	JButton bindButton = new JButton();
	JButton bindChainButton = new JButton();
	JButton unbindButton = new JButton();
	JButton clearBindingButton = new JButton();
	JButton selectButton = new JButton();

	JPanel startPanel = new JPanel();

	JLabel startNodeTitleLabel = new JLabel();
	JLabel startNodeLabel = new JLabel();
	JLabel startLinkLabel = new JLabel();
	JLabel startNodeToLabel = new JLabel();
	JTextField startNodeTextField = new JTextField();
	WrapperedComboBox startLinkComboBox = null;
	WrapperedComboBox startNodeToComboBox = null;

	JPanel endPanel = new JPanel();
	JLabel endNodeTitleLabel = new JLabel();
	JLabel endNodeLabel = new JLabel();
	JLabel endLinkLabel = new JLabel();
	JLabel endNodeToLabel = new JLabel();
	JTextField endNodeTextField = new JTextField();
	WrapperedComboBox endLinkComboBox = null;
	WrapperedComboBox endNodeToComboBox = null;

	AbstractNode startNode;
	AbstractNode endNode;
	
	private CableChannelingItem startLastBound;
	private int startAvailableLinksCount;
	private CableChannelingItem endLastBound;
	private int endAvailableLinksCount;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
	private String stubObject = "";
	private NetMapViewer netMapViewer;
	
	private static Dimension buttonSize = new Dimension(24, 24);

	public CablePathAddEditor()
	{
		this.controller = CableBindingController.getInstance();
		this.model = new WrapperedTableModel(this.controller, this.controller.getKeysArray());
		this.table = new WrapperedTable(this.model);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
	}

	boolean doChanges = true;

	private void jbInit()
	{
		SimpleMapElementController comboController = 
				SimpleMapElementController.getInstance();

		this.startLinkComboBox = new WrapperedComboBox(comboController, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.startNodeToComboBox = new WrapperedComboBox(comboController, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.endLinkComboBox = new WrapperedComboBox(comboController, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.endNodeToComboBox = new WrapperedComboBox(comboController, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelMap.getString("LinkBinding"));
		this.titleLabel.setText(LangModelMap.getString("LinkBinding"));
		
		ActionListener lcbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!CablePathAddEditor.this.doChanges)
						return;

					Object selectedStartLink = CablePathAddEditor.this.startLinkComboBox.getSelectedItem();
					Object selectedEndLink = CablePathAddEditor.this.endLinkComboBox.getSelectedItem();
					boolean flag = (selectedStartLink instanceof PhysicalLink) || (selectedEndLink instanceof PhysicalLink);
					CablePathAddEditor.this.bindButton.setEnabled(flag);
					CablePathAddEditor.this.bindChainButton.setEnabled(flag);
					
					CablePathAddEditor.this.doChanges = false;
					if(selectedStartLink instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)selectedStartLink;
						CablePathAddEditor.this.startNodeToComboBox.setSelectedItem(pl.getOtherNode(CablePathAddEditor.this.startNode));
					}
					if(selectedEndLink instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)selectedEndLink;
						CablePathAddEditor.this.endNodeToComboBox.setSelectedItem(pl.getOtherNode(CablePathAddEditor.this.endNode));
					}
					CablePathAddEditor.this.doChanges = true;
				}
			};
		this.startLinkComboBox.addActionListener(lcbal);
		this.endLinkComboBox.addActionListener(lcbal);

		ActionListener ncbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!CablePathAddEditor.this.doChanges)
						return;

					Object selectedStartNode = CablePathAddEditor.this.startNodeToComboBox.getSelectedItem();
					Object selectedEndNode = CablePathAddEditor.this.endNodeToComboBox.getSelectedItem();
					boolean flag = (selectedStartNode instanceof SiteNode) || (selectedEndNode instanceof SiteNode);
					CablePathAddEditor.this.bindButton.setEnabled(flag);
					CablePathAddEditor.this.bindChainButton.setEnabled(flag);

					CablePathAddEditor.this.doChanges = false;
					if(selectedStartNode instanceof SiteNode)
					{
						SiteNode s = (SiteNode)selectedStartNode;
						CablePathAddEditor.this.startLinkComboBox.setSelectedItem(
								CablePathAddEditor.this.logicalNetLayer.getMapView().getMap().getPhysicalLink(CablePathAddEditor.this.startNode, s));
					}
					if(selectedEndNode instanceof SiteNode)
					{
						SiteNode s = (SiteNode)selectedEndNode;
						CablePathAddEditor.this.endLinkComboBox.setSelectedItem(
								CablePathAddEditor.this.logicalNetLayer.getMapView().getMap().getPhysicalLink(CablePathAddEditor.this.endNode, s));
					}
					CablePathAddEditor.this.doChanges = true;
				}
			};
		this.startNodeToComboBox.addActionListener(ncbal);
		this.endNodeToComboBox.addActionListener(ncbal);

		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				boolean itemSelected = CablePathAddEditor.this.table.getSelectedRowCount() != 0;
				CablePathAddEditor.this.unbindButton.setEnabled(itemSelected);
				CablePathAddEditor.this.selectButton.setEnabled(itemSelected);
			}
		});

		this.bindButton.setToolTipText("Привязать");
		this.bindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindcable.gif")));
		this.bindButton.setPreferredSize(buttonSize);
		this.bindButton.setMaximumSize(buttonSize);
		this.bindButton.setMinimumSize(buttonSize);
		this.bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try {
						addBinding();
					} catch(ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		this.bindChainButton.setToolTipText("Привязать цепочку");
		this.bindChainButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindchain.gif")));
		this.bindChainButton.setPreferredSize(buttonSize);
		this.bindChainButton.setMaximumSize(buttonSize);
		this.bindChainButton.setMinimumSize(buttonSize);
		this.bindChainButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try {
						addChainBinding();
					} catch(ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		this.unbindButton.setToolTipText("Убрать связь");
		this.unbindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/unbindlink.gif")));
		this.unbindButton.setPreferredSize(buttonSize);
		this.unbindButton.setMaximumSize(buttonSize);
		this.unbindButton.setMinimumSize(buttonSize);
		this.unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try {
						removeBinding();
					} catch(ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		this.clearBindingButton.setToolTipText("Отвязать кабель");
		this.clearBindingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/delete.gif")));
		this.clearBindingButton.setPreferredSize(buttonSize);
		this.clearBindingButton.setMaximumSize(buttonSize);
		this.clearBindingButton.setMinimumSize(buttonSize);
		this.clearBindingButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					clearBinding();
				}
			});

		this.selectButton.setToolTipText("Выбрать связь");
		this.selectButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/selectlink.gif")));
		this.selectButton.setPreferredSize(buttonSize);
		this.selectButton.setMaximumSize(buttonSize);
		this.selectButton.setMinimumSize(buttonSize);
		this.selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					selectLink();
				}
			});

		this.startPanel.setLayout(this.gridBagLayout3);
		this.startNodeTitleLabel.setText(LangModelMap.getString("StartNodeTitle"));
		this.startNodeLabel.setText(LangModelMap.getString("StartNode"));
		this.startLinkLabel.setText(LangModelMap.getString("StartLink"));
		this.startNodeToLabel.setText(LangModelMap.getString("To"));

		this.endPanel.setLayout(this.gridBagLayout2);
		this.endNodeTitleLabel.setText(LangModelMap.getString("EndNodeTitle"));
		this.endNodeLabel.setText(LangModelMap.getString("EndNode"));
		this.endLinkLabel.setText(LangModelMap.getString("EndLink"));
		this.endNodeToLabel.setText(LangModelMap.getString("To"));

		this.buttonsPanel.setLayout(new GridBagLayout());
		this.buttonsPanel.add(this.bindButton);
		this.buttonsPanel.add(this.bindChainButton);
		this.buttonsPanel.add(this.unbindButton);
		this.buttonsPanel.add(this.clearBindingButton);

		this.scrollPane.getViewport().add(this.table);

		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.titleLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.startPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.endPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.scrollPane, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.buttonsPanel, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeTitleLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startLinkLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeToLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startLinkComboBox, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeToComboBox, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeTitleLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endLinkLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeToLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endLinkComboBox, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeToComboBox, constraints);

		this.startNodeTextField.setEnabled(false);
		this.endNodeTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return this.cablePath;
	}

	public void setObject(Object object)
	{
		this.cablePath = (CablePath)object;

		this.table.removeAll();
		this.links = new LinkedList();

		this.startNodeTextField.setText("");
		this.endNodeTextField.setText("");
		this.startLinkComboBox.removeAllItems();
		this.endLinkComboBox.removeAllItems();
		this.startNodeToComboBox.removeAllItems();
		this.endNodeToComboBox.removeAllItems();

		this.startLinkComboBox.setEnabled(true);
		this.endLinkComboBox.setEnabled(true);
		this.startNodeToComboBox.setEnabled(true);
		this.endNodeToComboBox.setEnabled(true);
		
		if(this.cablePath == null)
		{//empty
		}
		else
		{
			this.controller.setCablePath(this.cablePath);

			this.links.addAll(this.cablePath.getLinks());

			this.model.setValues(this.cablePath.getLinks());
			
			setBindingPanels();
		}
	}
	
	private void setBindingPanels() {
		PhysicalLinkType unboundType;
		try {
			unboundType = LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		this.startNode = this.cablePath.getStartUnboundNode();
		
		this.endNode = this.cablePath.getEndUnboundNode();

		if(this.startNode.equals(this.cablePath.getEndNode()) 
				&& this.endNode.equals(this.cablePath.getStartNode())) {
			// no unbound elements
			this.startLinkComboBox.setEnabled(false);
			this.endLinkComboBox.setEnabled(false);
			this.startNodeToComboBox.setEnabled(false);
			this.endNodeToComboBox.setEnabled(false);
			return;
		}

		this.startNodeTextField.setText(this.startNode.getName());
		this.endNodeTextField.setText(this.endNode.getName());

		this.startLastBound = this.cablePath.getStartLastBoundLink();
		this.endLastBound = this.cablePath.getEndLastBoundLink();
		
		Collection availableLinksFromStart = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.startNode);
		if(this.startLastBound != null)
			availableLinksFromStart.remove(this.startLastBound);

		List availableNodesFromStart = new LinkedList();

		this.startAvailableLinksCount = availableLinksFromStart.size();
		for(Iterator it = availableLinksFromStart.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			if(link.getType().equals(unboundType)) {
				it.remove();
				this.startAvailableLinksCount--;
			}
			else if(link.getStartNode() instanceof TopologicalNode
				|| link.getEndNode() instanceof TopologicalNode) {
					it.remove();
					this.startAvailableLinksCount--;
			}
			else {
				availableNodesFromStart.add(link.getOtherNode(this.startNode));
			}
		}

		Collection availableLinksFromEnd = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.endNode);
		if(this.endLastBound != null)
			availableLinksFromEnd.remove(this.endLastBound);

		List availableNodesFromEnd = new LinkedList();

		this.endAvailableLinksCount = availableLinksFromEnd.size();
		for(Iterator it = availableLinksFromEnd.iterator(); it.hasNext();) {
			PhysicalLink mle = (PhysicalLink)it.next();
			if(mle.getType().equals(unboundType)) {
				it.remove();
				this.endAvailableLinksCount--;
			}
			else if(mle.getStartNode() instanceof TopologicalNode
				|| mle.getEndNode() instanceof TopologicalNode)
					it.remove();
			else {
				availableNodesFromEnd.add(mle.getOtherNode(this.endNode));
			}
		}

		this.startLinkComboBox.removeAllItems();
		this.startLinkComboBox.addItem(this.stubObject);
		this.startLinkComboBox.addElements(availableLinksFromStart);
		this.startLinkComboBox.setSelectedItem(this.stubObject);

		this.endLinkComboBox.removeAllItems();
		this.endLinkComboBox.addItem(this.stubObject);
		this.endLinkComboBox.addElements(availableLinksFromEnd);
		this.endLinkComboBox.setSelectedItem(this.stubObject);

		this.startNodeToComboBox.removeAllItems();
		this.startNodeToComboBox.addItem(this.stubObject);
		this.startNodeToComboBox.addElements(availableNodesFromStart);
		this.startNodeToComboBox.setSelectedItem(this.stubObject);

		this.endNodeToComboBox.removeAllItems();
		this.endNodeToComboBox.addItem(this.stubObject);
		this.endNodeToComboBox.addElements(availableNodesFromEnd);
		this.endNodeToComboBox.setSelectedItem(this.stubObject);

		this.startLinkComboBox.setEnabled(true);
		this.endLinkComboBox.setEnabled(true);
		this.startNodeToComboBox.setEnabled(true);
		this.endNodeToComboBox.setEnabled(true);
	}

	void selectLink() {
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		this.logicalNetLayer.getMapView().getMap().clearSelection();
		this.logicalNetLayer.deselectAll();
		this.logicalNetLayer.setCurrentMapElement(link);
		this.logicalNetLayer.getMapView().getMap().setSelected(link, true);
		this.logicalNetLayer.sendSelectionChangeEvent();
	}

	/**
	 * @throws ApplicationException 
	 * @todo working woth nodelinks and creation/deletion of elements
	 */
	void removeBinding() throws ApplicationException
	{
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(link);
		if(link instanceof UnboundLink)
		{
			UnboundLink unbound = (UnboundLink)link;

			CableChannelingItem previousCbleChannelingItem = this.cablePath.previousLink(cableChannelingItem);
			PhysicalLink previous = this.cablePath.getBinding().get(previousCbleChannelingItem);

			if(unbound.getStartNode().equals(unbound.getEndNode()))
			{
				this.cablePath.removeLink(cableChannelingItem);
				cableChannelingItem.setParentPathOwner(null, false);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
								unbound);
				command.setNetMapViewer(this.netMapViewer);
				command.execute();
			}
			else
			if(previous != null
				&& previous instanceof UnboundLink)
			{
				UnboundLink unboundPrevious = (UnboundLink)previous;
				AbstractNode removedNode = cableChannelingItem.getStartSiteNode();
				SiteNode newEndNode = cableChannelingItem.getEndSiteNode();
			
				if(unboundPrevious.getEndNode().equals(removedNode))
					unboundPrevious.setEndNode(newEndNode);
				else
				if(unboundPrevious.getStartNode().equals(removedNode))
					unboundPrevious.setStartNode(newEndNode);
					
				for(Iterator it = unboundPrevious.getNodeLinksAt(removedNode).iterator(); it.hasNext();)
				{
					NodeLink nl = (NodeLink)it.next();
					if(nl.getEndNode().equals(removedNode))
						nl.setEndNode(newEndNode);
					else
					if(nl.getStartNode().equals(removedNode))
						nl.setStartNode(newEndNode);
				}

				previousCbleChannelingItem.setEndSiteNode(newEndNode);
				this.cablePath.removeLink(cableChannelingItem);
				cableChannelingItem.setParentPathOwner(null, false);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
								unbound);
				command.setNetMapViewer(this.netMapViewer);
				command.execute();
			}
		}
		else
		// replace binding to physical link with unbound link
		{
			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					link.getStartNode(),
					link.getEndNode());
			command.setNetMapViewer(this.netMapViewer);
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(this.cablePath);

			CableChannelingItem newCableChannelingItem = CableController.generateCCI(this.cablePath, unbound);
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);

			this.cablePath.removeLink(cableChannelingItem);
			this.cablePath.addLink(unbound, newCableChannelingItem);

			link.getBinding().remove(this.cablePath);
		}
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		setBindingPanels();
	}
	
	void clearBinding()
	{
		this.cablePath.clearLinks();
		this.cablePath.getSchemeCableLink().getPathMembers().iterator().next().setParentPathOwner(null, true);

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
			this.cablePath.getStartNode(), this.cablePath.getEndNode());
		command.setNetMapViewer(this.netMapViewer);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(this.cablePath);

		CableChannelingItem newCableChannelingItem = CableController.generateCCI(this.cablePath, unbound);
		this.cablePath.addLink(unbound, newCableChannelingItem);

		this.model.fireTableDataChanged();
		setBindingPanels();
	}
	
	private void addLinkBinding(
			PhysicalLink link,
			CableChannelingItem unboundCableChannelingItem,
			AbstractNode fromSite) throws ApplicationException {

		CableChannelingItem newCableChannelingItem = CableController.generateCCI(this.cablePath, link);
		newCableChannelingItem.insertSelfBefore(unboundCableChannelingItem);
		this.cablePath.addLink(link, newCableChannelingItem);
		link.getBinding().add(this.cablePath);

		UnboundLink unbound = (UnboundLink)this.cablePath.getBinding().get(unboundCableChannelingItem); 
		SiteNode otherNode = (SiteNode )link.getOtherNode(fromSite);
		if(otherNode.equals(unbound.getOtherNode(fromSite))) {
			RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unbound);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();

			unboundCableChannelingItem.setParentPathOwner(null, false);
			this.cablePath.removeLink(unboundCableChannelingItem);
		}
		else {
			if(unbound.getStartNode().equals(fromSite)) {
				unbound.setStartNode(otherNode);
			}
			else {
				unbound.setEndNode(otherNode);
			}

			for(Iterator it = unbound.getNodeLinksAt(fromSite).iterator(); it.hasNext();) {
				NodeLink nl = (NodeLink)it.next();
				if(nl.getStartNode().equals(fromSite))
					nl.setStartNode(otherNode);
				else
					nl.setEndNode(otherNode);
			}
			unboundCableChannelingItem.setStartSiteNode(otherNode);
		}
	}

	void addBinding() throws ApplicationException {
		PhysicalLink selectedStartLink;
		PhysicalLink selectedEndLink;
		
		Object selectedItem;
	
		selectedItem = this.startLinkComboBox.getSelectedItem();
		if(selectedItem instanceof PhysicalLink) {
			selectedStartLink = (PhysicalLink )selectedItem;
			
			CableChannelingItem unboundCableChannelingItem = this.cablePath.nextLink(this.startLastBound);
			
			if(unboundCableChannelingItem != null)
				addLinkBinding(selectedStartLink, unboundCableChannelingItem, this.startNode);
		}
		else {
			selectedStartLink = null;
		}

		selectedItem = this.endLinkComboBox.getSelectedItem();
		if(selectedItem instanceof PhysicalLink) {
			selectedEndLink = (PhysicalLink )selectedItem;
			
			if(!selectedEndLink.equals(selectedStartLink)) {
				CableChannelingItem unboundCableChannelingItem = this.cablePath.previousLink(this.endLastBound);
				
				if(unboundCableChannelingItem != null)
					addLinkBinding(selectedEndLink, unboundCableChannelingItem, this.endNode);
			}
		}
		
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		
		setBindingPanels();
	}

	void addChainBinding() throws ApplicationException
	{
		while(true)
		{
			addBinding();
			boolean proceed = false;

			if(this.startAvailableLinksCount == 1 && this.startLinkComboBox.isEnabled())
			{
				proceed = true;
				ComboBoxModel cbmodel = this.startLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					Object or = cbmodel.getElementAt(i);
					if(!or.equals(this.stubObject))
					{
						cbmodel.setSelectedItem(or);
						break;
					}
				}
			}

			if(this.endAvailableLinksCount == 1 && this.endLinkComboBox.isEnabled())
			{
				proceed = true;
				ComboBoxModel cbmodel = this.endLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					Object or = cbmodel.getElementAt(i);
					if(!or.equals(this.stubObject))
					{
						cbmodel.setSelectedItem(or);
						break;
					}
				}
			}

			if(!proceed)
				break;
		}
	}


	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// nothing to commet - all actions are autocommitted
	}
}

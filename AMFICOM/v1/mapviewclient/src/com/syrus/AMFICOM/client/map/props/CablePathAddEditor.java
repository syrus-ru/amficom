/*-
 * $$Id: CablePathAddEditor.java,v 1.34 2005/10/31 12:30:09 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

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
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.34 $, $Date: 2005/10/31 12:30:09 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
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

	SiteNode startNode;
	SiteNode endNode;
	
	private CableChannelingItem startLastBound;
	private int startAvailableLinksCount;
	private CableChannelingItem endLastBound;
	private int endAvailableLinksCount;
	private Collection availableLinksFromStart;
	private Collection availableLinksFromEnd;
	private List availableNodesFromStart;
	private List availableNodesFromEnd;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
	private String stubObject = ""; //$NON-NLS-1$
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
		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.LABEL_CABLE_BINDING));
		this.titleLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_CABLE_BINDING));
		
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
						for(PhysicalLink physicalLink : CablePathAddEditor.this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(CablePathAddEditor.this.startNode)) {
							if (!(physicalLink instanceof UnboundLink)
									&& (physicalLink.getEndNode().equals(s) || physicalLink.getStartNode().equals(s))) {
								CablePathAddEditor.this.startLinkComboBox.setSelectedItem(
										physicalLink);
								break;
							}
						}
					}
					if(selectedEndNode instanceof SiteNode)
					{
						SiteNode s = (SiteNode)selectedEndNode;
						for(PhysicalLink physicalLink : CablePathAddEditor.this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(CablePathAddEditor.this.endNode)) {
							if (!(physicalLink instanceof UnboundLink)
									&& (physicalLink.getEndNode().equals(s) || physicalLink.getStartNode().equals(s))) {
								CablePathAddEditor.this.endLinkComboBox.setSelectedItem(
										physicalLink);
								break;
							}
						}
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

		this.bindButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_BIND));
		this.bindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindcable.gif"))); //$NON-NLS-1$
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
		this.bindChainButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_BIND_CHAIN));
		this.bindChainButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindchain.gif"))); //$NON-NLS-1$
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
		this.unbindButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_UNBIND));
		this.unbindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/unbindlink.gif"))); //$NON-NLS-1$
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
		this.clearBindingButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_UNBIND_CABLE));
		this.clearBindingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/delete.gif"))); //$NON-NLS-1$
		this.clearBindingButton.setPreferredSize(buttonSize);
		this.clearBindingButton.setMaximumSize(buttonSize);
		this.clearBindingButton.setMinimumSize(buttonSize);
		this.clearBindingButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						clearBinding();
					} catch(ApplicationException e1) {
						Log.debugMessage(e1, Level.SEVERE);
					}
				}
			});

		this.selectButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_SELECT_ELEMENT));
		this.selectButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/selectlink.gif"))); //$NON-NLS-1$
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
		this.startNodeTitleLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_FROM_START));
		this.startNodeLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NODE));
		this.startLinkLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_TUNNEL));
		this.startNodeToLabel.setText(I18N.getString(MapEditorResourceKeys.TO_LOWERCASE));

		this.endPanel.setLayout(this.gridBagLayout2);
		this.endNodeTitleLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_FROM_END));
		this.endNodeLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NODE));
		this.endLinkLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_TUNNEL));
		this.endNodeToLabel.setText(I18N.getString(MapEditorResourceKeys.TO_LOWERCASE));

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

		this.startNodeTextField.setText(""); //$NON-NLS-1$
		this.endNodeTextField.setText(""); //$NON-NLS-1$
		this.startLinkComboBox.removeAllItems();
		this.endLinkComboBox.removeAllItems();
		this.startNodeToComboBox.removeAllItems();
		this.endNodeToComboBox.removeAllItems();

		this.startLinkComboBox.setEnabled(true);
		this.endLinkComboBox.setEnabled(true);
		this.startNodeToComboBox.setEnabled(true);
		this.endNodeToComboBox.setEnabled(true);
		
		this.bindButton.setEnabled(false);
		this.bindChainButton.setEnabled(false);
		this.unbindButton.setEnabled(false);
		if(this.cablePath == null)
		{//empty
		}
		else
		{
			this.controller.setCablePath(this.cablePath);

			try {
				this.links.addAll(this.cablePath.getLinks());
				this.model.setValues(this.cablePath.getLinks());
				setBindingValues();
			} catch(Exception e) {
				Log.debugMessage(e, Level.SEVERE);
			}
			
			setBindingPanels();
		}
	}

	private void setBindingValues() throws ApplicationException {
		PhysicalLinkType unboundType;
		try {
			unboundType = LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		this.startNode = (SiteNode) this.cablePath.getStartUnboundNode();
		if(this.startNode.equals(this.cablePath.getEndNode())) {
			this.startLastBound = null;
			this.availableLinksFromStart = Collections.emptySet();
			this.availableNodesFromStart = Collections.emptyList();
			this.startAvailableLinksCount = 0;
			
			this.endNode = (SiteNode) this.cablePath.getStartNode();
			this.endLastBound = null;
			this.availableLinksFromEnd = Collections.emptySet();
			this.availableNodesFromEnd = Collections.emptyList();
			this.endAvailableLinksCount = 0;
			return;
		}
		this.startLastBound = this.cablePath.getStartLastBoundLink();

		this.availableLinksFromStart = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.startNode);
		if(this.startLastBound != null)
			this.availableLinksFromStart.remove(this.startLastBound.getPhysicalLink());

		this.availableNodesFromStart = new LinkedList();

		this.startAvailableLinksCount = this.availableLinksFromStart.size();
		for(Iterator it = this.availableLinksFromStart.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			if (link.getType().equals(unboundType)) {
				it.remove();
				this.startAvailableLinksCount--;
			} else {
				final AbstractNode startNode2 = link.getStartNode();
				final AbstractNode endNode2 = link.getEndNode();
				if (startNode2 instanceof TopologicalNode
					|| endNode2 instanceof TopologicalNode) {
						it.remove();
						this.startAvailableLinksCount--;
				} else {
					this.availableNodesFromStart.add(link.getOtherNode(this.startNode));
				}
			}
		}

		this.endNode = (SiteNode) this.cablePath.getEndUnboundNode();
		if(this.endNode.equals(this.cablePath.getStartNode())) {
			this.endLastBound = null;
			this.availableLinksFromEnd = Collections.emptySet();
			this.availableNodesFromEnd = Collections.emptyList();
			this.endAvailableLinksCount = 0;
		}
		else {
			this.endLastBound = this.cablePath.getEndLastBoundLink();

			this.availableLinksFromEnd = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.endNode);
			if(this.endLastBound != null)
				this.availableLinksFromEnd.remove(this.endLastBound.getPhysicalLink());

			this.availableNodesFromEnd = new LinkedList();

			this.endAvailableLinksCount = this.availableLinksFromEnd.size();
			for(Iterator it = this.availableLinksFromEnd.iterator(); it.hasNext();) {
				PhysicalLink mle = (PhysicalLink)it.next();
				if (mle.getType().equals(unboundType)) {
					it.remove();
					this.endAvailableLinksCount--;
				} else {
					final AbstractNode startNode2 = mle.getStartNode();
					final AbstractNode endNode2 = mle.getEndNode();
					if (startNode2 instanceof TopologicalNode
						|| endNode2 instanceof TopologicalNode) {
							it.remove();
					} else {
						this.availableNodesFromEnd.add(mle.getOtherNode(this.endNode));
					}
				}
			}
		}

	}
	
	private void setBindingPanels() {
		if(this.startNode.equals(this.cablePath.getEndNode()) 
				&& this.endNode.equals(this.cablePath.getStartNode())) {
			// no unbound elements
			this.startLinkComboBox.setEnabled(false);
			this.endLinkComboBox.setEnabled(false);
			this.startNodeToComboBox.setEnabled(false);
			this.endNodeToComboBox.setEnabled(false);
		}
		else {
			this.startNodeTextField.setText(this.startNode.getName());
			this.endNodeTextField.setText(this.endNode.getName());
	
			this.startLinkComboBox.removeAllItems();
			this.startLinkComboBox.addItem(this.stubObject);
			this.startLinkComboBox.addElements(this.availableLinksFromStart);
			this.startLinkComboBox.setSelectedItem(this.stubObject);
	
			this.endLinkComboBox.removeAllItems();
			this.endLinkComboBox.addItem(this.stubObject);
			this.endLinkComboBox.addElements(this.availableLinksFromEnd);
			this.endLinkComboBox.setSelectedItem(this.stubObject);
	
			this.startNodeToComboBox.removeAllItems();
			this.startNodeToComboBox.addItem(this.stubObject);
			this.startNodeToComboBox.addElements(this.availableNodesFromStart);
			this.startNodeToComboBox.setSelectedItem(this.stubObject);
	
			this.endNodeToComboBox.removeAllItems();
			this.endNodeToComboBox.addItem(this.stubObject);
			this.endNodeToComboBox.addElements(this.availableNodesFromEnd);
			this.endNodeToComboBox.setSelectedItem(this.stubObject);
	
			this.startLinkComboBox.setEnabled(true);
			this.endLinkComboBox.setEnabled(true);
			this.startNodeToComboBox.setEnabled(true);
			this.endNodeToComboBox.setEnabled(true);
		}
	}

	void selectLink() {
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		this.logicalNetLayer.getMapView().getMap().clearSelection();
		this.logicalNetLayer.deselectAll();
		this.logicalNetLayer.setCurrentMapElement(link);
		this.logicalNetLayer.getMapView().getMap().setSelected(link, true);
		this.logicalNetLayer.sendSelectionChangeEvent();
	}

	void removeBinding() throws ApplicationException
	{
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(link);
		if(link instanceof UnboundLink)
		{
			UnboundLink unbound = (UnboundLink)link;

			CableChannelingItem previousCbleChannelingItem = this.cablePath.previousLink(cableChannelingItem);
			PhysicalLink previous = this.cablePath.getBinding().get(previousCbleChannelingItem);

			if(unbound.getStartNode().equals(unbound.getEndNodeId()))
			{
				this.cablePath.removeLink(cableChannelingItem);
				cableChannelingItem.setParentPathOwner(null, false);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
								unbound);
				command.setNetMapViewer(this.netMapViewer);
				command.execute();
				this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
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
				this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
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

			CableChannelingItem newCableChannelingItem = 
				CableController.generateCCI(
						this.cablePath, 
						unbound,
						cableChannelingItem.getStartSiteNode(),
						cableChannelingItem.getEndSiteNode());
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);

			this.cablePath.removeLink(cableChannelingItem);
			this.cablePath.addLink(unbound, newCableChannelingItem);

			link.getBinding().remove(this.cablePath);
			this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
		}
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();

		setBindingValues();
		setBindingPanels();
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	void clearBinding() throws ApplicationException {
		this.cablePath.clearLinks();
		final CableChannelingItem firstCCI = this.cablePath.getSchemeCableLink().getPathMembers().iterator().next();
		firstCCI.setParentPathOwner(null, true);

		AbstractNode cableStart = this.cablePath.getStartNode();
		AbstractNode cableEnd = this.cablePath.getEndNode();
		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
			cableStart, cableEnd);
		command.setNetMapViewer(this.netMapViewer);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(this.cablePath);

		CableChannelingItem newCableChannelingItem = CableController.generateCCI(
				this.cablePath, 
				unbound,
				cableStart,
				cableEnd);
		this.cablePath.addLink(unbound, newCableChannelingItem);

		this.netMapViewer.getLogicalNetLayer().getCommandList().flush();

		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();

		setBindingValues();
		setBindingPanels();
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	private void addLinkBinding(
			PhysicalLink link,
			CableChannelingItem unboundCableChannelingItem,
			SiteNode fromSite,
			SiteNode toSite,
			boolean insertBefore) throws ApplicationException {

		CableChannelingItem newCableChannelingItem = 
			CableController.generateCCI(
					this.cablePath, 
					link,
					fromSite,
					toSite);

		if(insertBefore) {
			newCableChannelingItem.insertSelfBefore(unboundCableChannelingItem);
		}
		else {
			newCableChannelingItem.insertSelfAfter(unboundCableChannelingItem);
		}

		int a = 0;
		
		this.cablePath.addLink(link, newCableChannelingItem);
		link.getBinding().add(this.cablePath);

		UnboundLink unbound = (UnboundLink)this.cablePath.getBinding().get(unboundCableChannelingItem); 

		if(unboundCableChannelingItem.getStartSiteNode().equals(fromSite)
				&& unboundCableChannelingItem.getEndSiteNode().equals(toSite)) {
			RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unbound);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();

			unboundCableChannelingItem.setParentPathOwner(null, false);
			this.cablePath.removeLink(unboundCableChannelingItem);
			this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
		}
		else {
			if(insertBefore) {
				if(unbound.getStartNode().equals(fromSite)) {
					unbound.setStartNode(toSite);
				}
				else {
					unbound.setEndNode(toSite);
				}
	
				for(Iterator it = unbound.getNodeLinksAt(fromSite).iterator(); it.hasNext();) {
					NodeLink nl = (NodeLink)it.next();
					if(nl.getStartNode().equals(fromSite)) {
						nl.setStartNode(toSite);
					}
					else {
						nl.setEndNode(toSite);
					}
				}
				unboundCableChannelingItem.setStartSiteNode(toSite);
			}
			else {
				if(unbound.getStartNode().equals(toSite)) {
					unbound.setStartNode(fromSite);
				}
				else {
					unbound.setEndNode(fromSite);
				}
	
				for(Iterator it = unbound.getNodeLinksAt(toSite).iterator(); it.hasNext();) {
					NodeLink nl = (NodeLink)it.next();
					if(nl.getStartNode().equals(toSite)) {
						nl.setStartNode(fromSite);
					}
					else {
						nl.setEndNode(fromSite);
					}
				}
				unboundCableChannelingItem.setEndSiteNode(fromSite);
			}
		}
	}

	void addBinding(
		PhysicalLink selectedStartLink,
		PhysicalLink selectedEndLink) throws ApplicationException {
		
		if(selectedStartLink != null) {
			CableChannelingItem unboundCableChannelingItem = this.cablePath.nextLink(this.startLastBound);
			if(unboundCableChannelingItem != null)
				addLinkBinding(
						selectedStartLink, 
						unboundCableChannelingItem, 
						this.startNode, 
						(SiteNode) selectedStartLink.getOtherNode(this.startNode), 
						true);
		}

		if(selectedEndLink != null) {
			if(!selectedEndLink.equals(selectedStartLink)) {
				CableChannelingItem unboundCableChannelingItem = this.cablePath.previousLink(this.endLastBound);
				if(unboundCableChannelingItem != null)
					addLinkBinding(
							selectedEndLink, 
							unboundCableChannelingItem, 
							(SiteNode) selectedEndLink.getOtherNode(this.endNode), 
							this.endNode, 
							false);
			}
		}
		setBindingValues();
	}
	
	PhysicalLink getSelectedPhysicalLink(WrapperedComboBox comboBox) {
		Object selectedItem = comboBox.getSelectedItem();
		if(selectedItem instanceof PhysicalLink) {
			return (PhysicalLink )selectedItem;
		}
		return null;
	}
	
	void addBinding() throws ApplicationException {
		PhysicalLink selectedStartLink = getSelectedPhysicalLink(this.startLinkComboBox);
		PhysicalLink selectedEndLink = getSelectedPhysicalLink(this.endLinkComboBox);
		
		addBinding(selectedStartLink, selectedEndLink);
		
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		
		setBindingPanels();
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void addChainBinding() throws ApplicationException {
		PhysicalLink selectedStartLink = getSelectedPhysicalLink(this.startLinkComboBox);
		PhysicalLink selectedEndLink = getSelectedPhysicalLink(this.endLinkComboBox);

		while(selectedStartLink != null || selectedEndLink != null) {
			addBinding(selectedStartLink, selectedEndLink);

			if(this.startAvailableLinksCount == 1 && this.startLinkComboBox.isEnabled()) {
				selectedStartLink = (PhysicalLink )this.availableLinksFromStart.iterator().next();
			}
			else {
				selectedStartLink = null;
			}

			if(this.endAvailableLinksCount == 1 && this.endLinkComboBox.isEnabled()) {
				selectedEndLink = (PhysicalLink )this.availableLinksFromEnd.iterator().next();
			}
			else {
				selectedEndLink = null;
			}
		}

		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		
		setBindingPanels();
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
	}
}

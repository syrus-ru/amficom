package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
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
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.LoginManager;
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
 * @version $Revision: 1.8 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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
	
	private PhysicalLink startLastBound;
	private int startAvailableLinksCount;
	private PhysicalLink endLastBound;
	private int endAvailableLinksCount;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
	private String stubObject = "";
	
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

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.logicalNetLayer = lnl;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
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

					Object mle = CablePathAddEditor.this.startLinkComboBox.getSelectedItem();
					Object mle2 = CablePathAddEditor.this.endLinkComboBox.getSelectedItem();
					boolean flag = (mle instanceof PhysicalLink) || (mle2 instanceof PhysicalLink);
					CablePathAddEditor.this.bindButton.setEnabled(flag);
					CablePathAddEditor.this.bindChainButton.setEnabled(flag);
					
					CablePathAddEditor.this.doChanges = false;
					if(mle instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle;
						CablePathAddEditor.this.startNodeToComboBox.setSelectedItem(pl.getOtherNode(CablePathAddEditor.this.startNode));
					}
					if(mle2 instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle2;
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

					Object mle = CablePathAddEditor.this.startNodeToComboBox.getSelectedItem();
					Object mle2 = CablePathAddEditor.this.endNodeToComboBox.getSelectedItem();
					boolean flag = (mle instanceof SiteNode) || (mle2 instanceof SiteNode);
					CablePathAddEditor.this.bindButton.setEnabled(flag);
					CablePathAddEditor.this.bindChainButton.setEnabled(flag);

					CablePathAddEditor.this.doChanges = false;
					if(mle instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle;
						CablePathAddEditor.this.startLinkComboBox.setSelectedItem(
								CablePathAddEditor.this.logicalNetLayer.getMapView().getMap().getPhysicalLink(CablePathAddEditor.this.startNode, s));
					}
					if(mle2 instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle2;
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
				CablePathAddEditor.this.unbindButton.setEnabled(CablePathAddEditor.this.table.getSelectedRowCount() != 0);
			}
		});

		this.bindButton.setText("Привязать");
		this.bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addBinding();
				}
			});
		this.bindChainButton.setText("Привязать цепочку");
		this.bindChainButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addChainBinding();
				}
			});
		this.unbindButton.setText("Убрать связь");
		this.unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeBinding();
				}
			});
		this.clearBindingButton.setText("Отвязать кабель");
		this.clearBindingButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					clearBinding();
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

		this.buttonsPanel.add(this.bindButton, null);
		this.buttonsPanel.add(this.bindChainButton, null);
		this.buttonsPanel.add(this.unbindButton, null);
		this.buttonsPanel.add(this.clearBindingButton, null);

		this.scrollPane.getViewport().add(this.table);

		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);

GridBagConstraints constraints = new GridBagConstraints();
//		this.this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.this.add(this.bindingPanel, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		

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
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
		constraints.ipady = 0;
		this.startPanel.add(this.startNodeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
		constraints.ipady = 0;
		this.startPanel.add(this.startLinkLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
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
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
		constraints.ipady = 0;
		this.endPanel.add(this.endNodeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
		constraints.ipady = 0;
		this.endPanel.add(this.endLinkLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 100;
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
	
	private void setBindingPanels()
	{
		PhysicalLinkType unboundType = LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND);

		this.startNode = this.cablePath.getStartUnboundNode();
		
		this.endNode = this.cablePath.getEndUnboundNode();

		if(this.startNode == null && this.endNode == null)
			return;// no unbound elements

		this.startNodeTextField.setText(this.startNode.getName());
		this.endNodeTextField.setText(this.endNode.getName());

		this.startLastBound = this.cablePath.getStartLastBoundLink();
		this.endLastBound = this.cablePath.getEndLastBoundLink();
		
		Collection smnelinks = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.startNode);
		if(this.startLastBound != null)
			smnelinks.remove(this.startLastBound);

		List smnenodes = new LinkedList();

		this.startAvailableLinksCount = smnelinks.size();
		for(Iterator it = smnelinks.iterator(); it.hasNext();)
		{
			PhysicalLink mle = (PhysicalLink)it.next();
			if(mle.getType().equals(unboundType))
			{
				it.remove();
				this.startAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof TopologicalNode
				|| mle.getEndNode() instanceof TopologicalNode)
					it.remove();
			else
			{
				smnenodes.add(mle.getOtherNode(this.startNode));
			}
		}

		Collection emnelinks = this.logicalNetLayer.getMapView().getMap().getPhysicalLinksAt(this.endNode);
		if(this.endLastBound != null)
			emnelinks.remove(this.endLastBound);

		List emnenodes = new LinkedList();

		this.endAvailableLinksCount = emnelinks.size();
		for(Iterator it = emnelinks.iterator(); it.hasNext();)
		{
			PhysicalLink mle = (PhysicalLink)it.next();
			if(mle.getType().equals(unboundType))
			{
				it.remove();
				this.endAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof TopologicalNode
				|| mle.getEndNode() instanceof TopologicalNode)
					it.remove();
			else
			{
				emnenodes.add(mle.getOtherNode(this.endNode));
			}
		}

		this.startLinkComboBox.removeAllItems();
		this.startLinkComboBox.addItem(this.stubObject);
		this.startLinkComboBox.addElements(smnelinks);
		this.startLinkComboBox.setSelectedItem(this.stubObject);

		this.endLinkComboBox.removeAllItems();
		this.endLinkComboBox.addItem(this.stubObject);
		this.endLinkComboBox.addElements(emnelinks);
		this.endLinkComboBox.setSelectedItem(this.stubObject);

		this.startNodeToComboBox.removeAllItems();
		this.startNodeToComboBox.addItem(this.stubObject);
		this.startNodeToComboBox.addElements(smnenodes);
		this.startNodeToComboBox.setSelectedItem(this.stubObject);

		this.endNodeToComboBox.removeAllItems();
		this.endNodeToComboBox.addItem(this.stubObject);
		this.endNodeToComboBox.addElements(emnenodes);
		this.endNodeToComboBox.setSelectedItem(this.stubObject);

		this.startLinkComboBox.setEnabled(!this.startNode.equals(this.cablePath.getEndNode()));
		this.endLinkComboBox.setEnabled(!this.endNode.equals(this.cablePath.getStartNode()));
		this.startNodeToComboBox.setEnabled(this.startLinkComboBox.isEnabled());
		this.endNodeToComboBox.setEnabled(this.endLinkComboBox.isEnabled());
	}

	/**
	 * @todo working woth nodelinks and creation/deletion of elements
	 */
	void removeBinding()
	{
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		PhysicalLink previous = this.cablePath.previousLink(link);
		if(link instanceof UnboundLink)
		{
			if(link.getStartNode().equals(link.getEndNode()))
			{
				this.cablePath.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(this.logicalNetLayer);
				command.execute();
			}
			else
			if(previous != null
				&& previous instanceof UnboundLink)
			{
				CableChannelingItem cci = (CableChannelingItem )this.cablePath.getBinding().get(link);
				AbstractNode removedNode = cci.getStartSiteNode();
			
				if(previous.getEndNode().equals(removedNode))
					previous.setEndNode(link.getOtherNode(removedNode));
				else
				if(previous.getStartNode().equals(removedNode))
					previous.setStartNode(link.getOtherNode(removedNode));
					
				for(Iterator it = previous.getNodeLinksAt(removedNode).iterator(); it.hasNext();)
				{
					NodeLink nl = (NodeLink)it.next();
					if(nl.getEndNode().equals(removedNode))
						nl.setEndNode(link.getOtherNode(removedNode));
					else
					if(nl.getStartNode().equals(removedNode))
						nl.setStartNode(link.getOtherNode(removedNode));
				}

				this.cablePath.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(this.logicalNetLayer);
				command.execute();
			}
		}
		else
		// replace binding to physical link with unbound link
		{
			this.cablePath.removeLink(link);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					link.getStartNode(),
					link.getEndNode());
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(this.cablePath);
			this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, LoginManager.getUserId()));
			link.getBinding().remove(this.cablePath);
		}
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		setBindingPanels();
	}
	
	void clearBinding()
	{
		this.cablePath.clearLinks();

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
			this.cablePath.getStartNode(), this.cablePath.getEndNode());
		command.setLogicalNetLayer(this.logicalNetLayer);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, LoginManager.getUserId()));
		unbound.setCablePath(this.cablePath);

		this.model.fireTableDataChanged();
		setBindingPanels();
	}
	
	private void addLinkBinding(
			PhysicalLink link, 
			UnboundLink unbound, 
			AbstractNode fromSite)
	{
		if(link.getOtherNode(fromSite).equals(unbound.getOtherNode(fromSite)))
		{
			if(unbound != null)
			{
				this.cablePath.removeLink(unbound);

				RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unbound);
				command.setLogicalNetLayer(this.logicalNetLayer);
				command.execute();
			}
		}
		else
		{
			if(unbound.getStartNode().equals(fromSite))
			{
				unbound.setStartNode(link.getOtherNode(fromSite));
			}
			else
			{
				unbound.setEndNode(link.getOtherNode(fromSite));
			}

			for(Iterator it = unbound.getNodeLinksAt(fromSite).iterator(); it.hasNext();)
			{
				NodeLink nl = (NodeLink)it.next();
				if(nl.getStartNode().equals(fromSite))
					nl.setStartNode(link.getOtherNode(fromSite));
				else
					nl.setEndNode(link.getOtherNode(fromSite));
			}
		}
		this.cablePath.addLink(link, CableController.generateCCI(this.cablePath, link, LoginManager.getUserId()));
		link.getBinding().add(this.cablePath);
	}

	void addBinding()
	{
		PhysicalLink selectedStartLink;
		PhysicalLink selectedEndLink;
		
		Object selectedItem;
	
		selectedItem = this.startLinkComboBox.getSelectedItem();
		if(selectedItem instanceof PhysicalLink)
		{
			selectedStartLink = (PhysicalLink )selectedItem;
			
			UnboundLink unbound = (UnboundLink)this.cablePath.nextLink(this.startLastBound);

			if(unbound != null)
				addLinkBinding(selectedStartLink, unbound, this.startNode);
		}
		else
		{
			selectedStartLink = null;
		}

		selectedItem = this.endLinkComboBox.getSelectedItem();
		if(selectedItem instanceof PhysicalLink)
		{
			selectedEndLink = (PhysicalLink )selectedItem;
			
			if(!selectedEndLink.equals(selectedStartLink))
			{
				UnboundLink unbound = (UnboundLink)this.cablePath.previousLink(this.endLastBound);

				if(unbound != null)
					addLinkBinding(selectedEndLink, unbound, this.endNode);
			}
		}
		
		this.model.setValues(this.cablePath.getLinks());
		this.model.fireTableDataChanged();
		
		setBindingPanels();
	}

	void addChainBinding()
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

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class MapCablePathBindPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	GridBagLayout gridBagLayout3 = new GridBagLayout();

	private CablePath path;

	private LogicalNetLayer lnl;
	
	/**
	 * таблица
	 */
	private CableBindingController controller;
	private ObjectResourceTableModel model;
	ObjectResourceTable table;

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
	ObjComboBox startLinkComboBox = null;
	ObjComboBox startNodeToComboBox = null;

	JPanel endPanel = new JPanel();
	JLabel endNodeTitleLabel = new JLabel();
	JLabel endNodeLabel = new JLabel();
	JLabel endLinkLabel = new JLabel();
	JLabel endNodeToLabel = new JLabel();
	JTextField endNodeTextField = new JTextField();
	ObjComboBox endLinkComboBox = null;
	ObjComboBox endNodeToComboBox = null;

	AbstractNode startNode;
	AbstractNode endNode;
	
	private PhysicalLink startLastBound;
	private int startAvailableLinksCount;
	private PhysicalLink endLastBound;
	private int endAvailableLinksCount;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
	private String stubObject = "";
	
	public MapCablePathBindPanel()
	{
		this.controller = CableBindingController.getInstance();
		this.model = new ObjectResourceTableModel(this.controller);
		this.table = new ObjectResourceTable(this.model);

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

	boolean doChanges = true;

	private void jbInit()
	{
		SimpleMapElementController comboController = 
				SimpleMapElementController.getInstance();

		this.startLinkComboBox = new ObjComboBox(comboController, SimpleMapElementController.KEY_NAME);
		this.startNodeToComboBox = new ObjComboBox(comboController, SimpleMapElementController.KEY_NAME);
		this.endLinkComboBox = new ObjComboBox(comboController, SimpleMapElementController.KEY_NAME);
		this.endNodeToComboBox = new ObjComboBox(comboController, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		this.titleLabel.setText(LangModelMap.getString("LinkBinding"));
		
		ActionListener lcbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!MapCablePathBindPanel.this.doChanges)
						return;

					Object mle = MapCablePathBindPanel.this.startLinkComboBox.getSelectedItem();
					Object mle2 = MapCablePathBindPanel.this.endLinkComboBox.getSelectedItem();
					boolean flag = (mle instanceof PhysicalLink) || (mle2 instanceof PhysicalLink);
					MapCablePathBindPanel.this.bindButton.setEnabled(flag);
					MapCablePathBindPanel.this.bindChainButton.setEnabled(flag);
					
					MapCablePathBindPanel.this.doChanges = false;
					if(mle instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle;
						MapCablePathBindPanel.this.startNodeToComboBox.setSelectedItem(pl.getOtherNode(MapCablePathBindPanel.this.startNode));
					}
					if(mle2 instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle2;
						MapCablePathBindPanel.this.endNodeToComboBox.setSelectedItem(pl.getOtherNode(MapCablePathBindPanel.this.endNode));
					}
					MapCablePathBindPanel.this.doChanges = true;
				}
			};
		this.startLinkComboBox.addActionListener(lcbal);
		this.endLinkComboBox.addActionListener(lcbal);

		ActionListener ncbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!MapCablePathBindPanel.this.doChanges)
						return;

					Object mle = MapCablePathBindPanel.this.startNodeToComboBox.getSelectedItem();
					Object mle2 = MapCablePathBindPanel.this.endNodeToComboBox.getSelectedItem();
					boolean flag = (mle instanceof SiteNode) || (mle2 instanceof SiteNode);
					MapCablePathBindPanel.this.bindButton.setEnabled(flag);
					MapCablePathBindPanel.this.bindChainButton.setEnabled(flag);

					MapCablePathBindPanel.this.doChanges = false;
					if(mle instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle;
						MapCablePathBindPanel.this.startLinkComboBox.setSelectedItem(s.getMap().getPhysicalLink(MapCablePathBindPanel.this.startNode, s));
					}
					if(mle2 instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle2;
						MapCablePathBindPanel.this.endLinkComboBox.setSelectedItem(s.getMap().getPhysicalLink(MapCablePathBindPanel.this.endNode, s));
					}
					MapCablePathBindPanel.this.doChanges = true;
				}
			};
		this.startNodeToComboBox.addActionListener(ncbal);
		this.endNodeToComboBox.addActionListener(ncbal);

		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				MapCablePathBindPanel.this.unbindButton.setEnabled(MapCablePathBindPanel.this.table.getSelectedRowCount() != 0);
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

//		this.this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.this.add(this.bindingPanel, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		
		this.add(this.titleLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.startPanel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.endPanel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.scrollPane, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		this.add(this.buttonsPanel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.startPanel.add(this.startNodeTitleLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.startPanel.add(this.startNodeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.startPanel.add(this.startLinkLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.startPanel.add(this.startNodeToLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.startPanel.add(this.startNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.startPanel.add(this.startLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.startPanel.add(this.startNodeToComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.endPanel.add(this.endNodeTitleLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.endPanel.add(this.endNodeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.endPanel.add(this.endLinkLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.endPanel.add(this.endNodeToLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		this.endPanel.add(this.endNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.endPanel.add(this.endLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.endPanel.add(this.endNodeToComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.startNodeTextField.setEnabled(false);
		this.endNodeTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.path = (CablePath)objectResource;

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
		
		if(this.path == null)
		{
		}
		else
		{
			this.controller.setCablePath(this.path);

			this.links.addAll(this.path.getLinks());

			this.model.setContents(this.path.getLinks());
			
			setBindingPanels();
		}
	}
	
	private void setBindingPanels()
	{
		Identifier creatorId = getLogicalNetLayer().getUserId();
			
		PhysicalLinkType unboundType = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.UNBOUND);

		this.startNode = this.path.getStartUnboundNode();
		
		this.endNode = this.path.getEndUnboundNode();

		if(this.startNode == null && this.endNode == null)
			return;// no unbound elements

		this.startNodeTextField.setText(this.startNode.getName());
		this.endNodeTextField.setText(this.endNode.getName());

		this.startLastBound = this.path.getStartLastBoundLink();
		this.endLastBound = this.path.getEndLastBoundLink();
		
		List smnelinks = this.path.getMap().getPhysicalLinksAt(this.startNode);
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

		List emnelinks = this.path.getMap().getPhysicalLinksAt(this.endNode);
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

		this.startLinkComboBox.setEnabled(!this.startNode.equals(this.path.getEndNode()));
		this.endLinkComboBox.setEnabled(!this.endNode.equals(this.path.getStartNode()));
		this.startNodeToComboBox.setEnabled(this.startLinkComboBox.isEnabled());
		this.endNodeToComboBox.setEnabled(this.endLinkComboBox.isEnabled());
	}

	/**
	 * @todo working woth nodelinks and creation/deletion of elements
	 */
	void removeBinding()
	{
		PhysicalLink link = (PhysicalLink)this.model.getObject(this.table.getSelectedRow());
		PhysicalLink previous = this.path.previousLink(link);
		if(link instanceof UnboundLink)
		{
			if(link.getStartNode().equals(link.getEndNode()))
			{
				this.path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(this.lnl);
				command.execute();
			}
			else
			if(previous != null
				&& previous instanceof UnboundLink)
			{
				CableChannelingItem cci = (CableChannelingItem )this.path.getBinding().get(link);
				AbstractNode removedNode = cci.startSiteNodeImpl();
			
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

				this.path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(this.lnl);
				command.execute();
			}
		}
		else
		// replace binding to physical link with unbound link
		{
			this.path.removeLink(link);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					link.getStartNode(),
					link.getEndNode());
			command.setLogicalNetLayer(this.lnl);
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(this.path);
			this.path.addLink(unbound, CableController.generateCCI(unbound));
			link.getBinding().remove(this.path);
		}
		this.model.setContents(this.path.getLinks());
		this.model.fireTableDataChanged();
		setBindingPanels();
	}
	
	void clearBinding()
	{
		this.path.clearLinks();

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
			this.path.getStartNode(), this.path.getEndNode());
		command.setLogicalNetLayer(this.lnl);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		this.path.addLink(unbound, CableController.generateCCI(unbound));
		unbound.setCablePath(this.path);

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
				this.path.removeLink(unbound);

				RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unbound);
				command.setLogicalNetLayer(this.lnl);
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
		this.path.addLink(link, CableController.generateCCI(link));
		link.getBinding().add(this.path);
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
			
			UnboundLink unbound = (UnboundLink)this.path.nextLink(this.startLastBound);

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
				UnboundLink unbound = (UnboundLink)this.path.previousLink(this.endLastBound);

				if(unbound != null)
					addLinkBinding(selectedEndLink, unbound, this.endNode);
			}
		}
		
		this.model.setContents(this.path.getLinks());
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

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		return true;
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
}

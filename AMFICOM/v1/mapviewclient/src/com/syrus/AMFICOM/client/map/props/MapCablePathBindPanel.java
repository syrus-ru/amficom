package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.Client.Map.mapview.CablePathBinding;

public final class MapCablePathBindPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	private CablePath path;

	private LogicalNetLayer lnl;
	
	/**
	 * таблица
	 */
	private CableBindingController controller;
	private ObjectResourceTableModel model;
	private ObjectResourceTable table;

	private JLabel titleLabel = new JLabel();

	private JScrollPane scrollPane = new JScrollPane();
	
	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton bindChainButton = new JButton();
	private JButton unbindButton = new JButton();
	private JButton clearBindingButton = new JButton();

	private JPanel startPanel = new JPanel();

	private JLabel startNodeTitleLabel = new JLabel();
	private JLabel startNodeLabel = new JLabel();
	private JLabel startLinkLabel = new JLabel();
	private JLabel startNodeToLabel = new JLabel();
	private JTextField startNodeTextField = new JTextField();
	private ObjComboBox startLinkComboBox = null;
	private ObjComboBox startNodeToComboBox = null;

	private JPanel endPanel = new JPanel();
	private JLabel endNodeTitleLabel = new JLabel();
	private JLabel endNodeLabel = new JLabel();
	private JLabel endLinkLabel = new JLabel();
	private JLabel endNodeToLabel = new JLabel();
	private JTextField endNodeTextField = new JTextField();
	private ObjComboBox endLinkComboBox = null;
	private ObjComboBox endNodeToComboBox = null;

	private AbstractNode startNode;
	private AbstractNode endNode;
	
	private PhysicalLink startLastBound;
	private int startAvailableLinksCount;
	private PhysicalLink endLastBound;
	private int endAvailableLinksCount;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
	private String stubObject = "";
	
	public MapCablePathBindPanel()
	{
		controller = CableBindingController.getInstance();
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

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

	private boolean doChanges = true;

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		startLinkComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		startNodeToComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		endLinkComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		endNodeToComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		titleLabel.setText(LangModelMap.getString("LinkBinding"));
		
		ActionListener lcbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!MapCablePathBindPanel.this.doChanges)
						return;

					Object mle = startLinkComboBox.getSelectedItem();
					Object mle2 = endLinkComboBox.getSelectedItem();
					boolean flag = (mle instanceof PhysicalLink) || (mle2 instanceof PhysicalLink);
					bindButton.setEnabled(flag);
					bindChainButton.setEnabled(flag);
					
					MapCablePathBindPanel.this.doChanges = false;
					if(mle instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle;
						startNodeToComboBox.setSelectedItem(pl.getOtherNode(startNode));
					}
					if(mle2 instanceof PhysicalLink)
					{
						PhysicalLink pl = (PhysicalLink)mle2;
						endNodeToComboBox.setSelectedItem(pl.getOtherNode(endNode));
					}
					MapCablePathBindPanel.this.doChanges = true;
				}
			};
		startLinkComboBox.addActionListener(lcbal);
		endLinkComboBox.addActionListener(lcbal);

		ActionListener ncbal = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!MapCablePathBindPanel.this.doChanges)
						return;

					Object mle = startNodeToComboBox.getSelectedItem();
					Object mle2 = endNodeToComboBox.getSelectedItem();
					boolean flag = (mle instanceof SiteNode) || (mle2 instanceof SiteNode);
					bindButton.setEnabled(flag);
					bindChainButton.setEnabled(flag);

					MapCablePathBindPanel.this.doChanges = false;
					if(mle instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle;
						startLinkComboBox.setSelectedItem(s.getMap().getPhysicalLink(startNode, s));
					}
					if(mle2 instanceof SiteNode)
					{
						SiteNode s = (SiteNode)mle2;
						endLinkComboBox.setSelectedItem(s.getMap().getPhysicalLink(endNode, s));
					}
					MapCablePathBindPanel.this.doChanges = true;
				}
			};
		startNodeToComboBox.addActionListener(ncbal);
		endNodeToComboBox.addActionListener(ncbal);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					unbindButton.setEnabled(table.getSelectedRowCount() != 0);
				}
			});

		bindButton.setText("Привязать");
		bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addBinding();
				}
			});
		bindChainButton.setText("Привязать цепочку");
		bindChainButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addChainBinding();
				}
			});
		unbindButton.setText("Убрать связь");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeBinding();
				}
			});
		clearBindingButton.setText("Отвязать кабель");
		clearBindingButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					clearBinding();
				}
			});

		startPanel.setLayout(gridBagLayout3);
		startNodeTitleLabel.setText(LangModelMap.getString("StartNodeTitle"));
		startNodeLabel.setText(LangModelMap.getString("StartNode"));
		startLinkLabel.setText(LangModelMap.getString("StartLink"));
		startNodeToLabel.setText(LangModelMap.getString("To"));

		endPanel.setLayout(gridBagLayout2);
		endNodeTitleLabel.setText(LangModelMap.getString("EndNodeTitle"));
		endNodeLabel.setText(LangModelMap.getString("EndNode"));
		endLinkLabel.setText(LangModelMap.getString("EndLink"));
		endNodeToLabel.setText(LangModelMap.getString("To"));

		buttonsPanel.add(bindButton, null);
		buttonsPanel.add(bindChainButton, null);
		buttonsPanel.add(unbindButton, null);
		buttonsPanel.add(clearBindingButton, null);

		scrollPane.getViewport().add(table);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

//		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.add(bindingPanel, ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		
		this.add(titleLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(startPanel, ReusedGridBagConstraints.get(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(endPanel, ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(scrollPane, ReusedGridBagConstraints.get(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		this.add(buttonsPanel, ReusedGridBagConstraints.get(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		startPanel.add(startNodeTitleLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		startPanel.add(startNodeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		startPanel.add(startLinkLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		startPanel.add(startNodeToLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		startPanel.add(startNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		startPanel.add(startLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		startPanel.add(startNodeToComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		endPanel.add(endNodeTitleLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		endPanel.add(endNodeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		endPanel.add(endLinkLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		endPanel.add(endNodeToLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		endPanel.add(endNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		endPanel.add(endLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		endPanel.add(endNodeToComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		startNodeTextField.setEnabled(false);
		endNodeTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		path = (CablePath)objectResource;

		table.removeAll();
		links = new LinkedList();

		startNodeTextField.setText("");
		endNodeTextField.setText("");
		startLinkComboBox.removeAll();
		endLinkComboBox.removeAll();
		startNodeToComboBox.removeAll();
		endNodeToComboBox.removeAll();

		startLinkComboBox.setEnabled(true);
		endLinkComboBox.setEnabled(true);
		startNodeToComboBox.setEnabled(true);
		endNodeToComboBox.setEnabled(true);
		
		if(path == null)
		{
		}
		else
		{
			controller.setCablePath(path);

			links.addAll(path.getLinks());

			model.setContents(path.getLinks());
			
			setBindingPanels();
		}
	}
	
	private void setBindingPanels()
	{
		startNode = path.getStartUnboundNode();
		
		endNode = path.getEndUnboundNode();

		if(startNode == null && endNode == null)
			return;// no unbound elements

		startNodeTextField.setText(startNode.getName());
		endNodeTextField.setText(endNode.getName());

		startLastBound = path.getStartLastBoundLink();
		endLastBound = path.getEndLastBoundLink();
		
		List smnelinks = path.getMap().getPhysicalLinksAt(startNode);
		if(startLastBound != null)
			smnelinks.remove(startLastBound);

		List smnenodes = new LinkedList();

		startAvailableLinksCount = smnelinks.size();
		for(Iterator it = smnelinks.iterator(); it.hasNext();)
		{
			PhysicalLink mle = (PhysicalLink)it.next();
			if(mle.getType().equals(getLogicalNetLayer().getPhysicalLinkType(PhysicalLinkType.UNBOUND)))
			{
				it.remove();
				startAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof TopologicalNode
				|| mle.getEndNode() instanceof TopologicalNode)
					it.remove();
			else
			{
				smnenodes.add(mle.getOtherNode(startNode));
			}
		}

		List emnelinks = path.getMap().getPhysicalLinksAt(endNode);
		if(endLastBound != null)
			emnelinks.remove(endLastBound);

		List emnenodes = new LinkedList();

		endAvailableLinksCount = emnelinks.size();
		for(Iterator it = emnelinks.iterator(); it.hasNext();)
		{
			PhysicalLink mle = (PhysicalLink)it.next();
			if(mle.getType().equals(getLogicalNetLayer().getPhysicalLinkType(PhysicalLinkType.UNBOUND)))
			{
				it.remove();
				endAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof TopologicalNode
				|| mle.getEndNode() instanceof TopologicalNode)
					it.remove();
			else
			{
				emnenodes.add(mle.getOtherNode(endNode));
			}
		}

		startLinkComboBox.removeAll();
		startLinkComboBox.addItem(stubObject);
		startLinkComboBox.addElements(smnelinks);
		startLinkComboBox.setSelectedItem(stubObject);

		endLinkComboBox.removeAll();
		endLinkComboBox.addItem(stubObject);
		endLinkComboBox.addElements(emnelinks);
		endLinkComboBox.setSelectedItem(stubObject);

		startNodeToComboBox.removeAll();
		startNodeToComboBox.addItem(stubObject);
		startNodeToComboBox.addElements(smnenodes);
		startNodeToComboBox.setSelectedItem(stubObject);

		endNodeToComboBox.removeAll();
		endNodeToComboBox.addItem(stubObject);
		endNodeToComboBox.addElements(emnenodes);
		endNodeToComboBox.setSelectedItem(stubObject);

		startLinkComboBox.setEnabled(!startNode.equals(path.getEndNode()));
		endLinkComboBox.setEnabled(!endNode.equals(path.getStartNode()));
		startNodeToComboBox.setEnabled(startLinkComboBox.isEnabled());
		endNodeToComboBox.setEnabled(endLinkComboBox.isEnabled());
	}

	/**
	 * @todo working woth nodelinks and creation/deletion of elements
	 */
	private void removeBinding()
	{
		PhysicalLink link = (PhysicalLink)model.getObject(table.getSelectedRow());
		PhysicalLink previous = path.previousLink(link);
		if(link instanceof UnboundLink)
		{
			if(link.getStartNode().equals(link.getEndNode()))
			{
				path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
				command.execute();
			}
			else
			if(previous != null
				&& previous instanceof UnboundLink)
			{
				CableChannelingItem cci = (CableChannelingItem )path.getBinding().get(link);
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

				path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(UnboundLink)link);
				command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
				command.execute();
			}
		}
		else
		// replace binding to physical link with unbound link
		{
			path.removeLink(link);

			CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
					link.getStartNode(),
					link.getEndNode());
			command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
			command.execute();

			UnboundLink unbound = command.getUnbound();
			unbound.setCablePath(path);
			path.addLink(unbound);
			link.getBinding().remove(path);
		}
		model.setContents(path.getLinks());
		model.fireTableDataChanged();
		setBindingPanels();
	}
	
	private void clearBinding()
	{
		path.clearLinks();

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
			path.getStartNode(), path.getEndNode());
		command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
		command.execute();

		UnboundLink unbound = command.getUnbound();
		path.addLink(unbound);
		unbound.setCablePath(path);

		model.fireTableDataChanged();
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
				path.removeLink(unbound);

				RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unbound);
				command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
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
		path.addLink(link);
		link.getBinding().add(path);
	}

	private void addBinding()
	{
		PhysicalLink selectedStartLink;
		PhysicalLink selectedEndLink;
	
		try
		{
			selectedStartLink = (PhysicalLink )startLinkComboBox.getSelectedItem();
			
			UnboundLink unbound = (UnboundLink)path.nextLink(startLastBound);

			if(unbound != null)
				addLinkBinding(selectedStartLink, unbound, startNode);
		}
		catch (ClassCastException e)
		{
//			System.out.println("Not binding from start node");
			selectedStartLink = null;
		}

		try
		{
			selectedEndLink = (PhysicalLink )endLinkComboBox.getSelectedItem();
			
			if(!selectedEndLink.equals(selectedStartLink))
			{
				UnboundLink unbound = (UnboundLink)path.previousLink(endLastBound);

				if(unbound != null)
					addLinkBinding(selectedEndLink, unbound, endNode);
			}
		}
		catch (ClassCastException e)
		{
//			System.out.println("Not binding from end node");
		}
		
		model.setContents(path.getLinks());
		model.fireTableDataChanged();
		
		setBindingPanels();
	}

	private void addChainBinding()
	{
		while(true)
		{
			addBinding();
			boolean proceed = false;

			if(startAvailableLinksCount == 1 && startLinkComboBox.isEnabled())
			{
				proceed = true;
				ComboBoxModel cbmodel = startLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					Object or = (Object )cbmodel.getElementAt(i);
					if(!or.equals(stubObject))
					{
						cbmodel.setSelectedItem(or);
						break;
					}
				}
			}

			if(endAvailableLinksCount == 1 && endLinkComboBox.isEnabled())
			{
				proceed = true;
				ComboBoxModel cbmodel = endLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					Object or = (Object )cbmodel.getElementAt(i);
					if(!or.equals(stubObject))
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

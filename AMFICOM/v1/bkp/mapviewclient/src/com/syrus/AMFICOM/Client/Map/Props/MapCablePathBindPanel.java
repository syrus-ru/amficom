package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
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

public final class MapCablePathBindPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	private MapCablePathElement path;
	
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
	private JTextField startNodeTextField = new JTextField();
	private ObjectResourceComboBox startLinkComboBox = new ObjectResourceComboBox(MapPhysicalLinkElement.typ, true);

	private JPanel endPanel = new JPanel();
	private JLabel endNodeTitleLabel = new JLabel();
	private JLabel endNodeLabel = new JLabel();
	private JLabel endLinkLabel = new JLabel();
	private JTextField endNodeTextField = new JTextField();
	private ObjectResourceComboBox endLinkComboBox = new ObjectResourceComboBox(MapPhysicalLinkElement.typ, true);

	private MapNodeElement startNode;
	private MapNodeElement endNode;
	
	private MapPhysicalLinkElement startLastBound;
	private int startAvailableLinksCount;
	private MapPhysicalLinkElement endLastBound;
	private int endAvailableLinksCount;
	
	// holds original list of cable path links
	private List links = new LinkedList();
	
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

	private void jbInit()
	{
		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		titleLabel.setText(LangModelMap.getString("LinkBinding"));
		
		ActionListener al = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource mle = (ObjectResource )startLinkComboBox.getSelectedObjectResource();
					ObjectResource mle2 = (ObjectResource )endLinkComboBox.getSelectedObjectResource();
					boolean flag = (mle instanceof MapPhysicalLinkElement) || (mle2 instanceof MapPhysicalLinkElement);
//							(mle != null && mle.getId() != null && mle.getId().length() != 0)
//							|| (mle2 != null && mle2.getId() != null && mle2.getId().length() != 0);
					bindButton.setEnabled(flag);
					bindChainButton.setEnabled(flag);
				}
			};
		startLinkComboBox.addActionListener(al);
		endLinkComboBox.addActionListener(al);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
//					MapPhysicalLinkElement link = (MapPhysicalLinkElement )model.getObjectResource(table.getSelectedRow());
//					boolean flag = false;
//					if(link instanceof MapUnboundLinkElement)
//						if(path.previousLink(link) != null)
//							if(path.previousLink(link) instanceof MapUnboundLinkElement)
//								flag = true;
//					unbindButton.setEnabled(flag);
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

		endPanel.setLayout(gridBagLayout2);
		endNodeTitleLabel.setText(LangModelMap.getString("EndNodeTitle"));
		endNodeLabel.setText(LangModelMap.getString("EndNode"));
		endLinkLabel.setText(LangModelMap.getString("EndLink"));

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
		startPanel.add(startNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		startPanel.add(startLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		endPanel.add(endNodeTitleLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		endPanel.add(endNodeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		endPanel.add(endLinkLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 100, 0));
		endPanel.add(endNodeTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		endPanel.add(endLinkComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		startNodeTextField.setEnabled(false);
		endNodeTextField.setEnabled(false);
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		path = (MapCablePathElement )objectResource;

		table.removeAll();
		links = new LinkedList();

		startNodeTextField.setText("");
		endNodeTextField.setText("");
		startLinkComboBox.removeAll();
		endLinkComboBox.removeAll();

		startLinkComboBox.setEnabled(true);
		endLinkComboBox.setEnabled(true);
		
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

		startAvailableLinksCount = smnelinks.size();
		for(Iterator it = smnelinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			if(mle.getMapProtoId().equals(MapLinkProtoElement.UNBOUND))
			{
				it.remove();
				startAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof MapPhysicalNodeElement
				|| mle.getEndNode() instanceof MapPhysicalNodeElement)
					it.remove();
		}

		List emnelinks = path.getMap().getPhysicalLinksAt(endNode);
		if(endLastBound != null)
			emnelinks.remove(endLastBound);
		endAvailableLinksCount = emnelinks.size();
		for(Iterator it = emnelinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			if(mle.getMapProtoId().equals(MapLinkProtoElement.UNBOUND))
			{
				it.remove();
				endAvailableLinksCount--;
			}
			else
			if(mle.getStartNode() instanceof MapPhysicalNodeElement
				|| mle.getEndNode() instanceof MapPhysicalNodeElement)
					it.remove();
		}

		startLinkComboBox.setContents(smnelinks, true);
		startLinkComboBox.setSelected(null);
		endLinkComboBox.setContents(emnelinks, true);
		endLinkComboBox.setSelected(null);
		
		startLinkComboBox.setEnabled(!startNode.equals(path.getEndNode()));
		endLinkComboBox.setEnabled(!endNode.equals(path.getStartNode()));
	}

	/**
	 * @todo working woth nodelinks and creation/deletion of elements
	 */
	private void removeBinding()
	{
		MapPhysicalLinkElement link = (MapPhysicalLinkElement )model.getObject(table.getSelectedRow());
		MapPhysicalLinkElement previous = path.previousLink(link);
		if(link instanceof MapUnboundLinkElement)
		{
			if(link.getStartNode().equals(link.getEndNode()))
			{
				path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(MapUnboundLinkElement )link);
				command.setLogicalNetLayer(path.getMapView().getLogicalNetLayer());
				command.execute();
			}
			else
			if(previous != null
				&& previous instanceof MapUnboundLinkElement)
			{
				CableChannelingItem cci = (CableChannelingItem )path.getBinding().get(link);
				MapNodeElement removedNode = link.getMap().getMapSiteNodeElement(cci.startSiteId);
			
				if(previous.getEndNode().equals(removedNode))
					previous.setEndNode(link.getOtherNode(removedNode));
				else
				if(previous.getStartNode().equals(removedNode))
					previous.setStartNode(link.getOtherNode(removedNode));
					
				for(Iterator it = previous.getNodeLinksAt(removedNode).iterator(); it.hasNext();)
				{
					MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
					if(nl.getEndNode().equals(removedNode))
						nl.setEndNode(link.getOtherNode(removedNode));
					else
					if(nl.getStartNode().equals(removedNode))
						nl.setStartNode(link.getOtherNode(removedNode));
				}

				path.removeLink(link);

				RemoveUnboundLinkCommandBundle command = 
						new RemoveUnboundLinkCommandBundle(
							(MapUnboundLinkElement )link);
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

			MapUnboundLinkElement unbound = command.getUnbound();
			unbound.setCablePath(path);
			path.addLink(unbound);
			link.getBinding().remove(path);
		}
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

		MapUnboundLinkElement unbound = command.getUnbound();
		path.addLink(unbound);
		unbound.setCablePath(path);

		model.fireTableDataChanged();
		setBindingPanels();
	}
	
	private void addLinkBinding(
			MapPhysicalLinkElement link, 
			MapUnboundLinkElement unbound, 
			MapNodeElement fromSite)
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
				MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
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
		MapPhysicalLinkElement selectedStartLink;
		MapPhysicalLinkElement selectedEndLink;
	
		try
		{
			selectedStartLink = (MapPhysicalLinkElement )startLinkComboBox.getSelectedObjectResource();
			
			MapUnboundLinkElement unbound = (MapUnboundLinkElement )path.nextLink(startLastBound);
			addLinkBinding(selectedStartLink, unbound, startNode);
/*
			if(selectedStartLink.getOtherNode(startNode).equals(unbound.getOtherNode(startNode)))
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
				if(unbound.getStartNode().equals(startNode))
				{
					unbound.setStartNode(selectedStartLink.getOtherNode(startNode));
				}
				else
				{
					unbound.setEndNode(selectedStartLink.getOtherNode(startNode));
				}

				for(Iterator it = unbound.getNodeLinksAt(startNode).iterator(); it.hasNext();)
				{
					MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
					if(nl.getStartNode().equals(startNode))
						nl.setStartNode(selectedStartLink.getOtherNode(startNode));
					else
						nl.setEndNode(selectedStartLink.getOtherNode(startNode));
				}
			}
			path.addLink(selectedStartLink);
			selectedStartLink.getBinding().add(path);
*/
		}
		catch (ClassCastException e)
		{
//			System.out.println("Not binding from start node");
			selectedStartLink = null;
		}

		try
		{
			selectedEndLink = (MapPhysicalLinkElement )endLinkComboBox.getSelectedObjectResource();
			
			if(!selectedEndLink.equals(selectedStartLink))
			{
				MapUnboundLinkElement unbound = (MapUnboundLinkElement )path.previousLink(endLastBound);

				addLinkBinding(selectedEndLink, unbound, endNode);
/*
				if(selectedEndLink.getOtherNode(startNode).equals(unbound.getOtherNode(startNode)))
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
					if(unbound != null)
					{
						if(unbound.getStartNode().equals(startNode))
							unbound.setStartNode(selectedEndLink.getOtherNode(startNode));
						else
							unbound.setEndNode(selectedEndLink.getOtherNode(startNode));

						for(Iterator it = unbound.getNodeLinksAt(startNode).iterator(); it.hasNext();)
						{
							MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
							if(nl.getStartNode().equals(startNode))
								nl.setStartNode(selectedEndLink.getOtherNode(startNode));
							else
								nl.setEndNode(selectedEndLink.getOtherNode(startNode));
						}
					}
				}
				path.addLink(selectedEndLink);
				selectedStartLink.getBinding().add(path);
*/
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

			if(startAvailableLinksCount == 1)
			{
				proceed = true;
				ComboBoxModel cbmodel = startLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					ObjectResource or = (ObjectResource )cbmodel.getElementAt(i);
					if(or.getId().length() != 0)
					{
						cbmodel.setSelectedItem(or);
						break;
					}
				}
			}

			if(endAvailableLinksCount == 1)
			{
				proceed = true;
				ComboBoxModel cbmodel = endLinkComboBox.getModel();
				for (int i = 0; i < cbmodel.getSize(); i++) 
				{
					ObjectResource or = (ObjectResource )cbmodel.getElementAt(i);
					if(or.getId().length() != 0)
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
//		path.setLinks(links);
		return false;
	}
}

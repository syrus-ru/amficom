package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.List;

import java.util.ListIterator;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.Iterator;

public final class MapCablePathBindPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	MapCablePathElement path;
	
	/**
	 * таблица
	 */
	CableBindingController controller;
	ObjectResourceTableModel model;
	ObjectResourceTable table;

	private JLabel titleLabel = new JLabel();

	JScrollPane scrollPane = new JScrollPane();
	
	private JPanel buttonsPanel = new JPanel();
	private JButton bindButton = new JButton();
	private JButton bindChainButton = new JButton();
	private JButton unbindButton = new JButton();

	private JPanel startPanel = new JPanel();

	private JLabel startNodeTitleLabel = new JLabel();
	private JLabel startNodeLabel = new JLabel();
	private JLabel startLinkLabel = new JLabel();
	private JTextField startNodeTextField = new JTextField();
	private ObjectResourceComboBox startLinkComboBox = new ObjectResourceComboBox();

	private JPanel endPanel = new JPanel();
	private JLabel endNodeTitleLabel = new JLabel();
	private JLabel endNodeLabel = new JLabel();
	private JLabel endLinkLabel = new JLabel();
	private JTextField endNodeTextField = new JTextField();
	private ObjectResourceComboBox endLinkComboBox = new ObjectResourceComboBox();

	MapNodeElement startNode;
	MapNodeElement endNode;
	
	List channelingItems;

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
		
		startLinkComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource mle = (ObjectResource )startLinkComboBox.getSelectedObjectResource();
					bindButton.setEnabled(mle.getId().length() != 0);
					bindChainButton.setEnabled(mle.getId().length() != 0);
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
		unbindButton.setText("Отвязать");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

		startNodeTextField.setText("");
		endNodeTextField.setText("");
		startLinkComboBox.removeAll();
		endLinkComboBox.removeAll();

		startLinkComboBox.setEditable(true);
		endLinkComboBox.setEditable(true);
		
		if(path == null)
		{
		}
		else
		{
			controller.setMap(path.getMap());
			channelingItems = new LinkedList();
			if(path.getSchemeCableLink().channelingItems == null)
				path.getSchemeCableLink().channelingItems = new LinkedList();
			channelingItems.addAll(path.getSchemeCableLink().channelingItems);
			
			model.setContents(channelingItems);
			
			setBindingPanels();
		}
	}
	
	private void setBindingPanels()
	{
		startNode = getStartUnbound();
		
		if(startNode == null)
			return;// no unbound elements

		endNode = getEndUnbound();

		startNodeTextField.setText(startNode.getName());
		endNodeTextField.setText(endNode.getName());

		MapPhysicalLinkElement smle = getStartLastBoundLink();
		MapPhysicalLinkElement emle = getEndLastBoundLink();
		
		List smnelinks = path.getMap().getPhysicalLinksAt(startNode);
		if(smle != null)
			smnelinks.remove(smle);
		for(Iterator it = smnelinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			if(mle.getMapProtoId().equals(MapLinkProtoElement.UNBOUND))
				it.remove();
		}

		List emnelinks = path.getMap().getPhysicalLinksAt(endNode);
		if(emle != null)
			emnelinks.remove(emle);
		for(Iterator it = emnelinks.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			if(mle.getMapProtoId().equals(MapLinkProtoElement.UNBOUND))
				it.remove();
		}

		startLinkComboBox.setContents(smnelinks, true);
		startLinkComboBox.setSelected(null);
		endLinkComboBox.setContents(emnelinks, true);
		endLinkComboBox.setSelected(null);
		
		startLinkComboBox.setEditable(startNode.equals(path.getEndNode()));
		endLinkComboBox.setEditable(endNode.equals(path.getStartNode()));
	}

	private void addBinding()
	{
		MapLinkElement mle = (MapLinkElement )startLinkComboBox.getSelectedObjectResource();

		CableChannelingItem cci = new CableChannelingItem(
			path
				.getMapView()
					.getLogicalNetLayer()
						.getContext()
							.getDataSourceInterface()
								.GetUId(CableChannelingItem.typ));
		cci.startSiteId = startNode.getId();
		cci.startSpare = MapPropertiesManager.getSpareLength();
		cci.physicalLinkId = mle.getId();
		cci.endSpare = MapPropertiesManager.getSpareLength();
		cci.endSiteId = mle.getOtherNode(startNode).getId();
		
		channelingItems.add(cci);
		
//		model.getContents().add(cci);
		model.fireTableDataChanged();
		
		setBindingPanels();

//		table.updateUI();
	}

	private void addChainBinding()
	{
		while(true)
		{
			addBinding();
			ComboBoxModel cbmodel = startLinkComboBox.getModel();
			if(cbmodel.getSize() == 2)
			{
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
			else
				break;
		}
	}

	public MapNodeElement getStartUnbound()
	{
		MapNodeElement bufferSite = path.getStartNode();
		
		for(Iterator it = channelingItems.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = path.getMap().getMapSiteNodeElement(cci.endSiteId);
		}
		return bufferSite;
	}
	
	public MapNodeElement getEndUnbound()
	{
		MapNodeElement bufferSite = path.getEndNode();
		
		for(ListIterator it = channelingItems.listIterator(channelingItems.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = path.getMap().getMapSiteNodeElement(cci.startSiteId);
		}
		return bufferSite;
	}

	public MapPhysicalLinkElement getStartLastBoundLink()
	{
		MapNodeElement bufferSite = path.getStartNode();
		MapPhysicalLinkElement link = null;
		
		for(Iterator it = channelingItems.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = path.getMap().getMapSiteNodeElement(cci.endSiteId);
			link = path.getMap().getPhysicalLink(cci.physicalLinkId);
		}
		return link;
	}

	public MapPhysicalLinkElement getEndLastBoundLink()
	{
		MapNodeElement bufferSite = path.getEndNode();
		MapPhysicalLinkElement link = null;

		List ccis = (List )channelingItems;
		
		for(ListIterator it = ccis.listIterator(ccis.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = path.getMap().getMapSiteNodeElement(cci.startSiteId);
			link = path.getMap().getPhysicalLink(cci.physicalLinkId);
		}
		return link;
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			path.getSchemeCableLink().channelingItems = this.channelingItems;
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
}

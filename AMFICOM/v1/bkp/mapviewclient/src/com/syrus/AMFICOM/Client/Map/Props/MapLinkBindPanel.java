package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;

public final class MapLinkBindPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	PhysicalLink link;
	
	private LogicalNetLayer lnl;

	private JLabel titleLabel = new JLabel();
	ObjList cableList = null;

	TunnelLayout tunnelLayout = null;

	private JPanel jPanel1 = new JPanel();
	JToggleButton bindButton = new JToggleButton();
	JButton unbindButton = new JButton();

	JLabel horvertLabel = new JLabel();
	JLabel topDownLabel = new JLabel();
	JLabel leftRightLabel = new JLabel();
	
	private List unboundElements = new LinkedList();

	boolean processSelection = true;

	static Icon horverticon;
	static Icon verthoricon;

	static Icon topdownicon;
	static Icon downtopicon;
	static Icon leftrighticon;
	static Icon rightlefticon;

	static
	{
		horverticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/horvert.gif"));
		verthoricon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/verthor.gif"));
		topdownicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/topdown.gif"));
		downtopicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/downtop.gif"));
		leftrighticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/leftright.gif"));
		rightlefticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rightleft.gif"));
	}

	public MapLinkBindPanel()
	{
		this.tunnelLayout = new TunnelLayout(this);
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
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.cableList = new ObjList(controller, SimpleMapElementController.KEY_NAME);


		this.setLayout(this.gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		this.titleLabel.setText(LangModelMap.getString("LinkBinding"));
		this.cableList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if(MapLinkBindPanel.this.processSelection)
					{
						MapLinkBindPanel.this.processSelection = false;
						Object or = MapLinkBindPanel.this.cableList.getSelectedValue();
						cableSelected(or);
						MapLinkBindPanel.this.bindButton.setEnabled(or != null);
						MapLinkBindPanel.this.unbindButton.setEnabled(or != null);
						MapLinkBindPanel.this.processSelection = true;
					}
				}
			});
		this.bindButton.setText("Привязать");
		this.bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setBindMode(MapLinkBindPanel.this.bindButton.isSelected());
//					Object or = cableList.getSelectedObjectResource();
//					bind(or);
				}
			});
		this.unbindButton.setText("Отвязать");
		this.unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Object or = MapLinkBindPanel.this.cableList.getSelectedValue();
					unbind(or);
				}
			});
		this.jPanel1.add(this.bindButton, null);
		this.jPanel1.add(this.unbindButton, null);

		this.horvertLabel.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					MapLinkBindPanel.this.link.getBinding().flipHorizontalVertical();
					MapLinkBindPanel.this.horvertLabel.setIcon(MapLinkBindPanel.this.link.getBinding().isHorizontalVertical() ?
						horverticon : verthoricon);
					MapLinkBindPanel.this.tunnelLayout.updateElements();
				}
			});

		this.topDownLabel.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					MapLinkBindPanel.this.link.getBinding().flipTopToBottom();
					MapLinkBindPanel.this.topDownLabel.setIcon(MapLinkBindPanel.this.link.getBinding().isTopToBottom() ?
						topdownicon : downtopicon);
					MapLinkBindPanel.this.tunnelLayout.updateElements();
				}
			});

		this.leftRightLabel.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e) 
				{
					MapLinkBindPanel.this.link.getBinding().flipLeftToRight();
					MapLinkBindPanel.this.leftRightLabel.setIcon(MapLinkBindPanel.this.link.getBinding().isLeftToRight() ?
						leftrighticon : rightlefticon);
					MapLinkBindPanel.this.tunnelLayout.updateElements();
				}
			});

		this.horvertLabel.setIcon(horverticon);
		this.topDownLabel.setIcon(topdownicon);
		this.leftRightLabel.setIcon(leftrighticon);
		
		this.add(this.titleLabel, ReusedGridBagConstraints.get(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.cableList, ReusedGridBagConstraints.get(0, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 100, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.add(Box.createGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.horvertLabel, ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.leftRightLabel, ReusedGridBagConstraints.get(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.topDownLabel, ReusedGridBagConstraints.get(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.tunnelLayout.getPanel(), ReusedGridBagConstraints.get(3, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		this.add(this.jPanel1, ReusedGridBagConstraints.get(0, 3, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.bindButton.setEnabled(false);
		this.unbindButton.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.cableList.removeAll();
		this.link = (PhysicalLink)objectResource;
		if(this.link == null)
		{
			this.cableList.setEnabled(false);
			this.tunnelLayout.setBinding(null);
		}
		else
		{
			this.cableList.setEnabled(true);
			PhysicalLinkBinding binding = this.link.getBinding();

			this.tunnelLayout.setBinding(binding);

			List list = binding.getBindObjects();
			if(list != null)
			{
				this.cableList.addElements(list);
			}

		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public void cableSelected(Object or)
	{
		this.tunnelLayout.setActiveElement(or);
	}

	public void cableBindingSelected(int col, int row)
	{
		if(this.processSelection)
		{
			this.processSelection = false;
			if(this.bindButton.isSelected())
			{
				Object or = this.cableList.getSelectedValue();
				bind(or);
				this.bindButton.setSelected(false);
				setBindMode(false);
			}
			else
			{
				PhysicalLinkBinding binding = this.link.getBinding();
				List list = binding.getBindObjects();
				if(list != null)
				{
					this.cableList.getSelectionModel().clearSelection();
					for(Iterator it = list.iterator(); it.hasNext();)
					{
						CablePath cp = (CablePath)it.next();
						IntPoint position = cp.getBindingPosition(this.link);
						if(position.x == col
							&& position.y == row)
						{
							this.cableList.setSelectedValue(cp, true);
						}
					}
				}
			}
			this.processSelection = true;
		}
	}

	public void bind(Object or)
	{
		PhysicalLinkBinding binding = this.link.getBinding();
		IntPoint pt = this.tunnelLayout.getActiveCoordinates();
		if(pt != null)
		{
			binding.bind(or, pt.x, pt.y);
			CablePath cp = (CablePath)or;
			CableChannelingItem cci = (CableChannelingItem )(cp.getBinding().get(this.link));
			cci.rowX(pt.x);
			cci.placeY(pt.y);
			this.tunnelLayout.updateElements();
		}
	}
	
	void setBindMode(boolean bindModeEnabled)
	{
		if(bindModeEnabled)
		{
			this.cableList.setEnabled(false);
			this.unbindButton.setEnabled(false);
		}
		else
		{
			this.cableList.setEnabled(true);
			this.unbindButton.setEnabled(true);
		}
	}

	public void unbind(Object or)
	{
		CablePath cablePath = (CablePath)or;

		cablePath.removeLink(this.link);

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
				this.link.getStartNode(),
				this.link.getEndNode());
		command.setLogicalNetLayer(this.lnl);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(cablePath);

		cablePath.addLink(unbound, CableController.generateCCI(unbound));
		this.link.getBinding().remove(cablePath);
		
		((ObjListModel )this.cableList.getModel()).removeElement(cablePath);

		this.tunnelLayout.updateElements();
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
}

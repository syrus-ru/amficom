package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;

import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.syrus.AMFICOM.mapview.CablePathBinding;

public final class MapLinkBindPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private PhysicalLink link;
	
	private LogicalNetLayer lnl;

	private JLabel titleLabel = new JLabel();
	private ObjList cableList = null;

	private TunnelLayout tunnelLayout = null;

	private JPanel jPanel1 = new JPanel();
	private JToggleButton bindButton = new JToggleButton();
	private JButton unbindButton = new JButton();

	private JLabel horvertLabel = new JLabel();
	private JLabel topDownLabel = new JLabel();
	private JLabel leftRightLabel = new JLabel();
	
	private List unboundElements = new LinkedList();

	private boolean processSelection = true;

	private static Icon horverticon;
	private static Icon verthoricon;

	private static Icon topdownicon;
	private static Icon downtopicon;
	private static Icon leftrighticon;
	private static Icon rightlefticon;

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
		tunnelLayout = new TunnelLayout(this);
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

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		cableList = new ObjList(controller, SimpleMapElementController.KEY_NAME);


		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		titleLabel.setText(LangModelMap.getString("LinkBinding"));
		cableList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if(processSelection)
					{
						processSelection = false;
						Object or = cableList.getSelectedValue();
						cableSelected(or);
						bindButton.setEnabled(or != null);
						unbindButton.setEnabled(or != null);
						processSelection = true;
					}
				}
			});
		bindButton.setText("Привязать");
		bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setBindMode(bindButton.isSelected());
//					Object or = cableList.getSelectedObjectResource();
//					bind(or);
				}
			});
		unbindButton.setText("Отвязать");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Object or = cableList.getSelectedValue();
					unbind(or);
				}
			});
		jPanel1.add(bindButton, null);
		jPanel1.add(unbindButton, null);

		horvertLabel.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e)
				{
					link.getBinding().flipHorizontalVertical();
					horvertLabel.setIcon(link.getBinding().isHorizontalVertical() ?
						horverticon : verthoricon);
					tunnelLayout.updateElements();
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});

		topDownLabel.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e)
				{
					link.getBinding().flipTopToBottom();
					topDownLabel.setIcon(link.getBinding().isTopToBottom() ?
						topdownicon : downtopicon);
					tunnelLayout.updateElements();
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});

		leftRightLabel.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e) 
				{
					link.getBinding().flipLeftToRight();
					leftRightLabel.setIcon(link.getBinding().isLeftToRight() ?
						leftrighticon : rightlefticon);
					tunnelLayout.updateElements();
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});

		horvertLabel.setIcon(horverticon);
		topDownLabel.setIcon(topdownicon);
		leftRightLabel.setIcon(leftrighticon);
		
		this.add(titleLabel, ReusedGridBagConstraints.get(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(cableList, ReusedGridBagConstraints.get(0, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 100, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.add(Box.createGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(horvertLabel, ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(leftRightLabel, ReusedGridBagConstraints.get(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(topDownLabel, ReusedGridBagConstraints.get(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(tunnelLayout.getPanel(), ReusedGridBagConstraints.get(3, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		this.add(jPanel1, ReusedGridBagConstraints.get(0, 3, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));

		bindButton.setEnabled(false);
		unbindButton.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		cableList.removeAll();
		link = (PhysicalLink)objectResource;
		if(link == null)
		{
			cableList.setEnabled(false);
			tunnelLayout.setBinding(null);
		}
		else
		{
			cableList.setEnabled(true);
			PhysicalLinkBinding binding = link.getBinding();

			tunnelLayout.setBinding(binding);

			List list = binding.getBindObjects();
			if(list != null)
			{
				cableList.addElements(list);
			}

		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public void cableSelected(Object or)
	{
		tunnelLayout.setActiveElement(or);
	}

	public void cableBindingSelected(int col, int row)
	{
		if(processSelection)
		{
			processSelection = false;
			if(bindButton.isSelected())
			{
				Object or = cableList.getSelectedValue();
				bind(or);
				bindButton.setSelected(false);
				setBindMode(false);
			}
			else
			{
				PhysicalLinkBinding binding = link.getBinding();
				List list = binding.getBindObjects();
				if(list != null)
				{
					cableList.getSelectionModel().clearSelection();
					for(Iterator it = list.iterator(); it.hasNext();)
					{
						CablePath cp = (CablePath)it.next();
						IntPoint position = cp.getBindingPosition(link);
						if(position.x == col
							&& position.y == row)
						{
							cableList.setSelectedValue(cp, true);
						}
					}
				}
			}
			processSelection = true;
		}
	}

	public void bind(Object or)
	{
		PhysicalLinkBinding binding = link.getBinding();
		IntPoint pt = tunnelLayout.getActiveCoordinates();
		if(pt != null)
		{
			binding.bind(or, pt.x, pt.y);
			CablePath cp = (CablePath)or;
			CableChannelingItem cci = (CableChannelingItem )(cp.getBinding().get(link));
			cci.rowX(pt.x);
			cci.placeY(pt.y);
			tunnelLayout.updateElements();
		}
	}
	
	private void setBindMode(boolean bindModeEnabled)
	{
		if(bindModeEnabled)
		{
			cableList.setEnabled(false);
			unbindButton.setEnabled(false);
		}
		else
		{
			cableList.setEnabled(true);
			unbindButton.setEnabled(true);
		}
	}

	public void unbind(Object or)
	{
		CablePath cablePath = (CablePath)or;

		cablePath.removeLink(link);

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
				link.getStartNode(),
				link.getEndNode());
		command.setLogicalNetLayer(lnl);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(cablePath);

		CableController cableController = (CableController )
			getLogicalNetLayer().getMapViewController().getController(cablePath);
		cablePath.addLink(unbound, cableController.generateCCI(unbound));
		link.getBinding().remove(cablePath);
		
		((ObjListModel )cableList.getModel()).removeElement(cablePath);

		tunnelLayout.updateElements();
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
		return unboundElements;
	}
}

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.Props.BindingLabel;
import com.syrus.AMFICOM.Client.Map.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkBinding;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class MapLinkBindPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	MapPhysicalLinkElement link;
	
	private JLabel titleLabel = new JLabel();
	private ObjectResourceListBox cableList = new ObjectResourceListBox();

	private TunnelLayout tunnelLayout = new TunnelLayout();

	JScrollPane scrollPane = new JScrollPane();
	
	private JPanel jPanel1 = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();

	private JLabel topDownLabel = new JLabel();
	private JLabel leftRightLabel = new JLabel();

	public MapLinkBindPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("LinkBinding"));
		titleLabel.setText(LangModelMap.getString("LinkBinding"));
		cableList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					ObjectResource or = cableList.getSelectedObjectResource();
					cableSelected(or);
				}
			});
/*			
		table.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e)
				{
					int col = table.columnAtPoint(e.getPoint());
					int row = table.rowAtPoint(e.getPoint());
					cableBindingSelected(col, row);
				}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});
*/
		bindButton.setText("Привязать");
		bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource or = cableList.getSelectedObjectResource();
					bind(or);
				}
			});
		unbindButton.setText("Отвязать");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource or = cableList.getSelectedObjectResource();
					unbind(or);
				}
			});
		jPanel1.add(bindButton, null);
		jPanel1.add(unbindButton, null);

		topDownLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/topdown.gif")));
		leftRightLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/leftright.gif")));
		
		this.add(titleLabel, ReusedGridBagConstraints.get(0, 0, 4, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(cableList, ReusedGridBagConstraints.get(0, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 100, 150));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(1, 1, 1, 2, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
//		this.add(Box.createGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(leftRightLabel, ReusedGridBagConstraints.get(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(topDownLabel, ReusedGridBagConstraints.get(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(tunnelLayout.getPanel(), ReusedGridBagConstraints.get(3, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
		this.add(jPanel1, ReusedGridBagConstraints.get(0, 3, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		cableList.removeAll();
		link = (MapPhysicalLinkElement )objectResource;
		if(link == null)
		{
			cableList.setEnabled(false);
			tunnelLayout.setBinding(null);
		}
		else
		{
			cableList.setEnabled(true);
			MapPhysicalLinkBinding binding = link.getBinding();

			tunnelLayout.setBinding(binding);

			List list = binding.getBindObjects();
			if(list != null)
			{
				for(Iterator it = list.iterator(); it.hasNext();)
				{
					cableList.add((ObjectResource )it.next());
				}
			}

		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public void cableSelected(ObjectResource or)
	{
		tunnelLayout.setActiveElement(or);
	}

//	public void cableBindingSelected(int col, int row)
//	{
//		model.setActiveCoordinates(new Point(col, row));
//	}

	public void bind(ObjectResource or)
	{
		MapPhysicalLinkBinding binding = link.getBinding();
		Point pt = tunnelLayout.getActiveCoordinates();
		if(pt != null)
		{
			binding.bind(or, pt.x, pt.y);
			tunnelLayout.updateElements();
		}
	}

	public void unbind(ObjectResource or)
	{
		MapPhysicalLinkBinding binding = link.getBinding();
		binding.unbind(or);
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
		finally 
		{
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

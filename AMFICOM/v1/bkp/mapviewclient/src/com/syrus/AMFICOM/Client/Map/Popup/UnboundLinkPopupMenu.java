package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundLinkToPhysicalLinkCommandBundle;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;

import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class UnboundLinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();

	private UnboundLink unbound;

	private static UnboundLinkPopupMenu instance = new UnboundLinkPopupMenu();

	private UnboundLinkPopupMenu()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static UnboundLinkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.unbound = (UnboundLink)me;
		generateMenuItem.setVisible( !(unbound.getStartNode() instanceof UnboundNode)
			&& !(unbound.getEndNode() instanceof UnboundNode));
	}

	private void jbInit() 
	{
		bindMenuItem.setText(LangModelMap.getString("Bind"));
		bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		generateMenuItem.setText(LangModelMap.getString("GenerateCabling"));
		generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateCabling();
				}
			});
		this.add(bindMenuItem);
		this.add(generateMenuItem);
	}

	private void bind()
	{
		PhysicalLink link = super.selectPhysicalLinkAt(unbound);
		if(link != null)
		{
			BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unbound, link);
			command.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();

			getLogicalNetLayer().repaint(false);
		}
	}

	private void generateCabling()
	{
		super.convertUnboundLinkToPhysicalLink(unbound);
	}
}


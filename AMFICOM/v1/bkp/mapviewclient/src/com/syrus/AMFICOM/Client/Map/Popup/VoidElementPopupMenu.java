package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.VoidMapElement;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JMenuItem;

public class VoidElementPopupMenu extends MapPopupMenu 
{

	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addSiteMenuItem = new JMenuItem();

	private static VoidElementPopupMenu instance = new VoidElementPopupMenu();

	private VoidElementPopupMenu()
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
	
	public static VoidElementPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
	}
	
	private void jbInit()
	{
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		addSiteMenuItem.setText(LangModelMap.getString("AddSite"));
		addSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addSite();
				}
			});
		this.add(addSiteMenuItem);
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(VoidMapElement.getInstance(getLogicalNetLayer().getMapView().getMap()));
	}

	private void addSite()
	{
		MapNodeProtoElement proto;
		
		List list = logicalNetLayer.getTopologicalProtos();
		
		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			proto = (MapNodeProtoElement )dialog.getSelected();
			if(proto != null)
			{
				CreateSiteCommand command = new CreateSiteCommand(proto, point);
				command.setLogicalNetLayer(logicalNetLayer);
				getLogicalNetLayer().getCommandList().add(command);
				getLogicalNetLayer().getCommandList().execute();
				
				getLogicalNetLayer().repaint();
			}
		}
	}
}
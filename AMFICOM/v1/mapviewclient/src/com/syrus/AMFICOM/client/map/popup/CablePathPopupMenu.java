package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindCablePathCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JMenuItem;
import java.util.Iterator;

public class CablePathPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	
	private MapCablePathElement path;

	private static CablePathPopupMenu instance = new CablePathPopupMenu();

	private CablePathPopupMenu()
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
	
	public static CablePathPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.path = (MapCablePathElement )me;
	}

	private void jbInit() 
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCablePath();
				}
			});
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
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
		this.add(removeMenuItem);
		this.add(propertiesMenuItem);
		this.add(bindMenuItem);
		this.add(generateMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(path);
	}

	private void removeCablePath()
	{
		super.removeMapElement(path);

		getLogicalNetLayer().repaint();
	}

	private void bind()
	{
		MapCablePathPane prop = (MapCablePathPane )MapCablePathPane.getInstance();
		prop.setContext(logicalNetLayer.getContext());
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				path,
				prop);

		prop.showBindPanel();

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
		}
	}

	private void generateCabling()
	{
		MapNodeProtoElement proto = (MapNodeProtoElement )Pool.get(
				MapNodeProtoElement.typ, 
				MapNodeProtoElement.WELL);

		BindCablePathCommandBundle command = 
				new BindCablePathCommandBundle(path, proto);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint();
	}
}

package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public void setElement(Object me)
	{
		this.path = (MapCablePathElement )me;
		
		boolean canGenerate = false;
		for(Iterator it = path.getLinks().iterator(); it.hasNext();)
		{
			Object link = it.next();
			if(link instanceof MapUnboundLinkElement)
				canGenerate = true;
		}
		generateMenuItem.setVisible(canGenerate);
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
		this.add(bindMenuItem);
		this.add(generateMenuItem);
		this.addSeparator();
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(path);
	}

	private void removeCablePath()
	{
		super.removeMapElement(path);

		getLogicalNetLayer().repaint(false);
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
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null)
		{
			super.generatePathCabling(path, proto);
			getLogicalNetLayer().repaint(false);
		}
	}
	
}

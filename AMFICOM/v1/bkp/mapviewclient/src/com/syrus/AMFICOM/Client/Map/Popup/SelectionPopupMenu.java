package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.InsertSiteCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.MapView.*;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import javax.swing.JMenuItem;
import java.util.Iterator;

public final class SelectionPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem insertSiteMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	
	private static SelectionPopupMenu instance = new SelectionPopupMenu();
	
	MapSelection selection;	

	private SelectionPopupMenu()
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
	
	public void setMapElement(MapElement me)
	{
		this.selection = (MapSelection )me;

		insertSiteMenuItem.setVisible(selection.isPhysicalNodeSelection());
		generateMenuItem.setVisible(selection.isUnboundSelection());
		newCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
		addToCollectorMenuItem.setVisible(selection.isPhysicalLinkSelection());
	}
	
	public static SelectionPopupMenu getInstance()
	{
		return instance;
	}
	
	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeSelection();
				}
			});
		insertSiteMenuItem.setText(LangModelMap.getString("PlaceSite"));
		insertSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					insertSite();
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
		newCollectorMenuItem.setText(LangModelMap.getString("CreateCollector"));
		newCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newCollector();
				}
			});
		addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		addToCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToCollector();
				}
			});
		this.add(removeMenuItem);
		this.add(insertSiteMenuItem);
		this.add(generateMenuItem);
		this.add(newCollectorMenuItem);
		this.add(addToCollectorMenuItem);
	}

	private void removeSelection()
	{
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint();
	}

	private void insertSite()
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
				for(Iterator it = selection.getElements().iterator(); it.hasNext();)
				{
					MapPhysicalNodeElement node = (MapPhysicalNodeElement )it.next();

					InsertSiteCommand command = new InsertSiteCommand(node, proto);
					command.setLogicalNetLayer(logicalNetLayer);
					getLogicalNetLayer().getCommandList().add(command);
					getLogicalNetLayer().getCommandList().execute();
				}

				getLogicalNetLayer().repaint();
			}
		}
	}

	private void generateCabling()
	{
	}

	private void newCollector()
	{
	}

	private void addToCollector()
	{
	}
}

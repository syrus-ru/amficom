package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import oracle.jdeveloper.layout.*;

public class MapProtoNavigatorPanel extends JPanel implements OperationListener
{
	JButton createMapGroupButton;
	JButton delMapGroupButton;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	ObjectResourceTreeModel model;
	UniTreePanel utp;

	Object selectedObject;

	public MapProtoNavigatorPanel(ApplicationContext aContext, Dispatcher dispatcher, ObjectResourceTreeModel model)
	{
		this.aContext = aContext;
		this.dispatcher = dispatcher;
		this.model = model;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		init_module(dispatcher);
	}

	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());

		//Toolbar
		createMapGroupButton = new JButton();
		createMapGroupButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newfolder.gif")));
		createMapGroupButton.setToolTipText("Новая папка");
		createMapGroupButton.setPreferredSize(new Dimension(24, 24));
		createMapGroupButton.setPreferredSize(new Dimension(24, 24));
		createMapGroupButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		createMapGroupButton.setFocusPainted(false);
		createMapGroupButton.setEnabled(false);
		createMapGroupButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				createMapGroupButton_actionPerformed();
			}
		});

		delMapGroupButton = new JButton();
		delMapGroupButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")));
		delMapGroupButton.setToolTipText("Удалить");
		delMapGroupButton.setPreferredSize(new Dimension(24, 24));
		delMapGroupButton.setPreferredSize(new Dimension(24, 24));
		delMapGroupButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		delMapGroupButton.setFocusPainted(false);
		delMapGroupButton.setEnabled(false);
		delMapGroupButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				delMapGroupButton_actionPerformed();
			}
		});

		JPanel toolBar = new JPanel();
		toolBar.setLayout(new XYLayout());
		toolBar.add(createMapGroupButton, new XYConstraints(0, 0, 24, 24));
		toolBar.add(delMapGroupButton, new XYConstraints(24, 0, 24, 24));
		add(toolBar, BorderLayout.NORTH);

		// TREE
		utp = new UniTreePanel(dispatcher, aContext, model);

		JScrollPane scroll_pane = new JScrollPane();
		scroll_pane.getViewport().add(utp);
		add(scroll_pane, BorderLayout.CENTER);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public JTree getTree()
	{
		return utp.getTree();
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent)oe;
			selectedObject = dse.getSelectedObject();
			Class creating_class = dse.getDataClass();

			createMapGroupButton.setEnabled(false);
			if (creating_class.equals(MapProtoGroup.class))
			{
				if (dse.getSelectionNumber() != -1)
				{
					MapProtoGroup group = (MapProtoGroup)dse.getList().get(dse.getSelectionNumber());
					if (group.mapproto_ids.isEmpty())
						createMapGroupButton.setEnabled(true);
				}
				else
					createMapGroupButton.setEnabled(true);
			}
			else if (creating_class.equals(MapProtoElement.class))
			{
				createMapGroupButton.setEnabled(false);
			}

			delMapGroupButton.setEnabled(false);
			if (selectedObject instanceof MapProtoGroup &&
					((MapProtoGroup)selectedObject).group_ids.isEmpty() &&
					((MapProtoGroup)selectedObject).mapproto_ids.isEmpty())
				delMapGroupButton.setEnabled(true);
			else if (selectedObject instanceof MapProtoElement &&
							 ((MapProtoElement)selectedObject).pe_ids.isEmpty())
				delMapGroupButton.setEnabled(true);
		}
	}

	void createMapGroupButton_actionPerformed()
	{
		String ret = JOptionPane.showInputDialog(Environment.getActiveWindow(), "Название:", "Новая папка", JOptionPane.QUESTION_MESSAGE);
		if (ret == null || ret.equals(""))
			return;

		MapProtoGroup new_group = new MapProtoGroup();
		new_group.id = aContext.getDataSourceInterface().GetUId(MapProtoGroup.typ);
		new_group.name = ret;
		if (selectedObject instanceof MapProtoGroup)
		{
			MapProtoGroup parent_group = (MapProtoGroup)selectedObject;
			new_group.parent_id = parent_group.getId();
			parent_group.group_ids.add(new_group.getId());
		}

		Pool.put(MapProtoGroup.typ, new_group.getId(), new_group);
		dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
		aContext.getDataSourceInterface().SaveMapProtoGroups(new String[] {new_group.getId()});
	}

	void delMapGroupButton_actionPerformed()
	{
		if (selectedObject instanceof MapProtoGroup)
		{
			MapProtoGroup group = (MapProtoGroup)selectedObject;
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранную папку?",
					"Удаление папки",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				aContext.getDataSourceInterface().RemoveMapProtoGroups(new String[] {group.getId()});
				Pool.remove(MapProtoGroup.typ, group.getId());
				if (group.parent_id != null && !group.parent_id.equals(""))
				{
					MapProtoGroup parent_group = (MapProtoGroup)Pool.get(MapProtoGroup.typ, group.parent_id);
					parent_group.group_ids.remove(group.getId());
					aContext.getDataSourceInterface().SaveMapProtoGroups(new String[] {parent_group.getId()});
				}
			}
			else
				return;
		}
		else if (selectedObject instanceof MapProtoElement)
		{
			MapProtoElement map_proto = (MapProtoElement)selectedObject;
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранную группу?",
					"Удаление группы",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				String id = map_proto.getId();
				aContext.getDataSourceInterface().RemoveMapProtoElements(new String[] {id});
				Pool.remove(MapProtoElement.typ, id);

				ArrayList groups = new ArrayList();
				for (Enumeration enum = Pool.getHash(MapProtoGroup.typ).elements(); enum.hasMoreElements();)
				{
					MapProtoGroup group = (MapProtoGroup)enum.nextElement();
					if (group.mapproto_ids.contains(id));
					{
						group.mapproto_ids.remove(id);
						groups.add(group);
					}
				}
				if (!groups.isEmpty())
				{
					String[] ids = new String[groups.size()];
					for (int i = 0; i < groups.size(); i++)
						ids[i] = ((MapProtoGroup)groups.get(i)).getId();

					aContext.getDataSourceInterface().SaveMapProtoGroups(ids);
					dispatcher.notify(new TreeListSelectionEvent(
							((MapProtoGroup)groups.get(0)).getTyp(),
							TreeListSelectionEvent.REFRESH_EVENT));
				}
			}
			else
				return;
		}
	}

}

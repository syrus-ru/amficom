package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeProtoGroup;
import oracle.jdeveloper.layout.*;

public class SchemeProtoGroupNavigatorPanel extends JPanel implements OperationListener
{
	JButton createMapGroupButton;
	JButton delMapGroupButton;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	ObjectResourceTreeModel model;
	UniTreePanel utp;

	Object selectedObject;

	public SchemeProtoGroupNavigatorPanel(ApplicationContext aContext, Dispatcher dispatcher, ObjectResourceTreeModel model)
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
		createMapGroupButton.setToolTipText("Новая группа");
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
			if (creating_class.equals(SchemeProtoGroup.class))
			{
				if (dse.getSelectionNumber() != -1)
				{
					SchemeProtoGroup group = (SchemeProtoGroup)dse.getList().get(dse.getSelectionNumber());
					if (group.getProtoIds().isEmpty())
						createMapGroupButton.setEnabled(true);
				}
				else
					createMapGroupButton.setEnabled(true);
			}
			else if (creating_class.equals(SchemeProtoGroup.class))
			{
				createMapGroupButton.setEnabled(false);
			}

			delMapGroupButton.setEnabled(false);
			if (selectedObject instanceof SchemeProtoGroup &&
					((SchemeProtoGroup)selectedObject).groupIds.isEmpty() &&
					((SchemeProtoGroup)selectedObject).getProtoIds().isEmpty())
				delMapGroupButton.setEnabled(true);
		}
	}

	void createMapGroupButton_actionPerformed()
	{
		String ret = JOptionPane.showInputDialog(Environment.getActiveWindow(), "Название:", "Новая группа", JOptionPane.QUESTION_MESSAGE);
		if (ret == null || ret.equals(""))
			return;

		SchemeProtoGroup new_group = new SchemeProtoGroup();
		new_group.id = aContext.getDataSourceInterface().GetUId(SchemeProtoGroup.typ);
		new_group.name = ret;
		if (selectedObject instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)selectedObject;
			new_group.parentId = parent_group.getId();
			parent_group.groupIds.add(new_group.getId());
		}

		Pool.put(SchemeProtoGroup.typ, new_group.getId(), new_group);
		dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
//		aContext.getDataSourceInterface().SaveMapProtoGroups(new String[] {new_group.getId()});
	}

	void delMapGroupButton_actionPerformed()
	{
		if (selectedObject instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup group = (SchemeProtoGroup)selectedObject;
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранную папку?",
					"Удаление папки",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
//				aContext.getDataSourceInterface().RemoveMapProtoGroups(new String[] {group.getId()});
				Pool.remove(SchemeProtoGroup.typ, group.getId());
				if (group.parentId != null && !group.parentId.equals(""))
				{
					SchemeProtoGroup parent_group = (SchemeProtoGroup)Pool.get(SchemeProtoGroup.typ, group.parentId);
					parent_group.groupIds.remove(group.getId());
//					aContext.getDataSourceInterface().SaveMapProtoGroups(new String[] {parent_group.getId()});
				}
			}
			else
				return;
		}
		else if (selectedObject instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup map_proto = (SchemeProtoGroup)selectedObject;
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранную группу?",
					"Удаление группы",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				String id = map_proto.getId();
//				aContext.getDataSourceInterface().RemoveMapProtoGroups(new String[] {id});
				Pool.remove(SchemeProtoGroup.typ, id);

				ArrayList groups = new ArrayList();
				for(Iterator it = Pool.getMap(SchemeProtoGroup.typ).values().iterator(); it.hasNext();)
				{
					SchemeProtoGroup group = (SchemeProtoGroup)it.next();
					if (group.getProtoIds().contains(id));
					{
						group.getProtoIds().remove(id);
						groups.add(group);
					}
				}
				if (!groups.isEmpty())
				{
					String[] ids = new String[groups.size()];
					for (int i = 0; i < groups.size(); i++)
						ids[i] = ((SchemeProtoGroup)groups.get(i)).getId();

//					aContext.getDataSourceInterface().SaveMapProtoGroups(ids);
					dispatcher.notify(new TreeListSelectionEvent(
							((SchemeProtoGroup)groups.get(0)).getTyp(),
							TreeListSelectionEvent.REFRESH_EVENT));
				}
			}
			else
				return;
		}
	}

}

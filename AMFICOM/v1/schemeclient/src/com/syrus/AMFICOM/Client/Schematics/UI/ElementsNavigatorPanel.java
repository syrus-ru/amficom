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
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import oracle.jdeveloper.layout.*;

public class ElementsNavigatorPanel extends JPanel implements OperationListener
{
	JButton delMapGroupButton;
	JButton loadButton;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	ObjectResourceTreeModel model;
	UniTreePanel utp;

	Object selectedObject;

	public ElementsNavigatorPanel(ApplicationContext aContext, Dispatcher dispatcher, ObjectResourceTreeModel model)
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
		loadButton = new JButton();
		loadButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif")));
		loadButton.setToolTipText("Открыть");
		loadButton.setPreferredSize(new Dimension(24, 24));
		loadButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		loadButton.setFocusPainted(false);
		loadButton.setEnabled(false);
		loadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				loadButton_actionPerformed();
			}
		});

		delMapGroupButton = new JButton();
		delMapGroupButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")));
		delMapGroupButton.setToolTipText("Удалить");
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
		toolBar.add(loadButton, new XYConstraints(0, 0, 24, 24));
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
			if (dse.getDataClass() == null)
				return;
			selectedObject = dse.getSelectedObject();
			Class creating_class = dse.getDataClass();

			if (creating_class.equals(ProtoElement.class))
				loadButton.setEnabled(true);
			else
				loadButton.setEnabled(false);

			delMapGroupButton.setEnabled(false);
			if (selectedObject instanceof SchemeProtoGroup &&
					((SchemeProtoGroup)selectedObject).group_ids.isEmpty() &&
					((SchemeProtoGroup)selectedObject).getProtoIds().isEmpty())
				delMapGroupButton.setEnabled(true);
			else if (selectedObject instanceof SchemeProtoGroup &&
							 ((SchemeProtoGroup)selectedObject).getProtoIds().isEmpty())
				delMapGroupButton.setEnabled(true);
			else if (selectedObject instanceof ProtoElement)
				delMapGroupButton.setEnabled(true);

			if (creating_class.equals(Scheme.class))
			{
				if (dse.getSelectionNumber() != -1)
				{
					loadButton.setEnabled(true);
					delMapGroupButton.setEnabled(true);
				}
				else
				{
					loadButton.setEnabled(false);
					delMapGroupButton.setEnabled(false);
				}
			}
		}
	}

	void loadButton_actionPerformed()
	{
		if (selectedObject instanceof ProtoElement)
		{
			ProtoElement proto = (ProtoElement)selectedObject;
			dispatcher.notify(new SchemeElementsEvent(this, proto, SchemeElementsEvent.OPEN_ELEMENT_EVENT));
			Pool.removeMap("clonedids");
		}
		if (selectedObject instanceof Scheme)
		{
			Scheme scheme = (Scheme)selectedObject;
			dispatcher.notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_SCHEME_EVENT));
		}
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
				aContext.getDataSourceInterface().RemoveMapProtoGroups(new String[] {group.getId()});
				Pool.remove(SchemeProtoGroup.typ, group.getId());
				if (group.parent_id != null && !group.parent_id.equals(""))
				{
					SchemeProtoGroup parent_group = (SchemeProtoGroup)Pool.get(SchemeProtoGroup.typ, group.parent_id);
					parent_group.group_ids.remove(group.getId());
				}
				dispatcher.notify(new TreeListSelectionEvent(SchemeProtoGroup.typ, TreeListSelectionEvent.REFRESH_EVENT));
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
				aContext.getDataSourceInterface().RemoveMapProtoGroups(new String[] {id});
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

					aContext.getDataSourceInterface().SaveMapProtoGroups(ids);
					dispatcher.notify(new TreeListSelectionEvent(
							((SchemeProtoGroup)groups.get(0)).getTyp(),
							TreeListSelectionEvent.REFRESH_EVENT));
				}
			}
			else
				return;
		}
		else if (selectedObject instanceof ProtoElement)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранный элемент?",
					"Удаление группы",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				ProtoElement proto = (ProtoElement)selectedObject;
				String proto_id = proto.getId();

				aContext.getDataSourceInterface().RemoveSchemeProtos(new String[] {proto_id});

				Map hash = Pool.getMap(SchemeProtoGroup.typ);
				for (Iterator it = hash.values().iterator(); it.hasNext();)
				{
					SchemeProtoGroup map_proto = (SchemeProtoGroup)it.next();
					if (map_proto.getProtoIds().contains(proto_id))
					{
						map_proto.getProtoIds().remove(proto_id);
						dispatcher.notify(new TreeListSelectionEvent(map_proto.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
					}
				}
			}
		}
		else if (selectedObject instanceof Scheme)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранную схему?",
					"Удаление схемы",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				Scheme scheme = (Scheme)selectedObject;
				String scheme_id = scheme.getId();

				aContext.getDataSourceInterface().RemoveScheme(scheme_id);
				Pool.remove(Scheme.typ, scheme_id);

				dispatcher.notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.CLOSE_SCHEME_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(Scheme.typ, TreeListSelectionEvent.REFRESH_EVENT));
			}
		}
	}

}


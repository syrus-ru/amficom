package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import oracle.jdeveloper.layout.*;

public class ElementsNavigatorPanel extends JPanel implements OperationListener
{
	JButton delMapGroupButton;
	JButton loadButton;
	JButton refreshButton;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	SOTreeDataModel model;
	Tree utp;

	Object selectedObject;

	public ElementsNavigatorPanel(ApplicationContext aContext, Dispatcher dispatcher, SOTreeDataModel model)
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

		refreshButton = new JButton();
		refreshButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/refresh.gif")));
		refreshButton.setToolTipText("Обновить");
		refreshButton.setPreferredSize(new Dimension(24, 24));
		refreshButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		refreshButton.setFocusPainted(false);
		refreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				refreshButton_actionPerformed();
			}
		});

		JPanel toolBar = new JPanel();
		toolBar.setLayout(new XYLayout());
		toolBar.add(loadButton, new XYConstraints(0, 0, 24, 24));
		toolBar.add(delMapGroupButton, new XYConstraints(25, 0, 24, 24));
		toolBar.add(refreshButton, new XYConstraints(50, 0, 24, 24));
		add(toolBar, BorderLayout.NORTH);

		// TREE
		utp = new Tree(dispatcher, new SOMutableNode(model, "root"));

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
		return utp;
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

			if (creating_class.equals(SchemeProtoElement.class))
				loadButton.setEnabled(true);
			else
				loadButton.setEnabled(false);

			delMapGroupButton.setEnabled(false);
			if (selectedObject instanceof SchemeProtoGroup &&
					((SchemeProtoGroup)selectedObject).getSchemeProtoGroups().isEmpty() &&
					((SchemeProtoGroup)selectedObject).getSchemeProtoElements().isEmpty())
				delMapGroupButton.setEnabled(true);
			else if (selectedObject instanceof SchemeProtoGroup &&
							 ((SchemeProtoGroup)selectedObject).getSchemeProtoElements().isEmpty())
				delMapGroupButton.setEnabled(true);
			else if (selectedObject instanceof SchemeProtoElement)
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
		if (selectedObject instanceof SchemeProtoElement)
		{
			SchemeProtoElement proto = (SchemeProtoElement)selectedObject;
			dispatcher.notify(new SchemeElementsEvent(this, proto, SchemeElementsEvent.OPEN_ELEMENT_EVENT));
			Pool.removeMap("clonedids");
		}
		if (selectedObject instanceof Scheme)
		{
			Scheme scheme = (Scheme)selectedObject;
			dispatcher.notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_SCHEME_EVENT));
		}
	}

	void refreshButton_actionPerformed()
	{
		dispatcher.notify(new TreeListSelectionEvent(
					selectedObject instanceof String ? selectedObject : "", TreeListSelectionEvent.REFRESH_EVENT));
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
				SchemeStorableObjectPool.delete(group.getId());
				final SchemeProtoGroup parentSchemeProtoGroup = group.getParentSchemeProtoGroup();
				if (parentSchemeProtoGroup != null)
					parentSchemeProtoGroup.removeSchemeProtoGroup(group);
				dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
			}
			else
				return;
		}
		else if (selectedObject instanceof SchemeProtoElement)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Вы действительно хотите удалить выбранный элемент?",
					"Удаление группы",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION)
			{
				SchemeProtoElement proto = (SchemeProtoElement)selectedObject;
				SchemeStorableObjectPool.delete(proto.getId());

				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					LinkedIdsCondition condition = new LinkedIdsCondition(domain_id,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					List groups = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = groups.iterator(); it.hasNext(); ) {
						SchemeProtoGroup map_proto = (SchemeProtoGroup)it.next();
						if (map_proto.getSchemeProtoElements().contains(proto)) {
							map_proto.removeSchemeProtoElement(proto);
							dispatcher.notify(new TreeListSelectionEvent(map_proto,
									TreeListSelectionEvent.REFRESH_EVENT));
						}
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
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

				SchemeStorableObjectPool.delete(scheme.getId());
				dispatcher.notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.CLOSE_SCHEME_EVENT));
				dispatcher.notify(new TreeListSelectionEvent(scheme, TreeListSelectionEvent.REFRESH_EVENT));
			}
		}
	}

}


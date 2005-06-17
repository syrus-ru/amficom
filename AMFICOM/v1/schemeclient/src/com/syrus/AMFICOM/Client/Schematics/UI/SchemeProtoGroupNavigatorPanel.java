package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

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
		} catch(Exception e)
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
					if (group.getSchemeProtoElements().isEmpty())
						createMapGroupButton.setEnabled(true);
				} else
					createMapGroupButton.setEnabled(true);
			} else if (creating_class.equals(SchemeProtoGroup.class))
			{
				createMapGroupButton.setEnabled(false);
			}

			delMapGroupButton.setEnabled(false);
			if (selectedObject instanceof SchemeProtoGroup &&
					((SchemeProtoGroup)selectedObject).getSchemeProtoGroups().isEmpty() &&
					((SchemeProtoGroup)selectedObject).getSchemeProtoElements().isEmpty())
				delMapGroupButton.setEnabled(true);
		}
	}

	void createMapGroupButton_actionPerformed()
	{
		try
		{
			String ret = JOptionPane.showInputDialog(Environment
					.getActiveWindow(), "Название:",
					"Новая группа",
					JOptionPane.QUESTION_MESSAGE);
			if (ret == null || ret.length() == 0)
				return;

			Identifier userId = new Identifier(
					((RISDSessionInfo) aContext
							.getSessionInterface())
							.getAccessIdentifier().user_id);

			SchemeProtoGroup new_group;
			if (selectedObject instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup) selectedObject;
				new_group = SchemeProtoGroup.createInstance(
						userId, ret, "", null, parent_group);
				parent_group.addSchemeProtoGroup(new_group);
			} else
				new_group = SchemeProtoGroup.createInstance(
						userId, ret, "", null, null);

			StorableObjectPool.putStorableObject(new_group);
			dispatcher.notify(new TreeListSelectionEvent("",
					TreeListSelectionEvent.REFRESH_EVENT));
		} catch (final IllegalObjectEntityException ioee)
		{
			/**
			 * @todo Introduce more substantial error handling
			 *       instead of just printing stack trace.
			 */
			ioee.printStackTrace();
		} catch (final CreateObjectException coe)
		{
			/**
			 * @todo Introduce more substantial error handling
			 *       instead of just printing stack trace.
			 */
			coe.printStackTrace();
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
				SchemeStorableObjectPool.delete(group.getId());
				final SchemeProtoGroup parentSchemeProtoGroup = group.getParentSchemeProtoGroup();
				if (parentSchemeProtoGroup != null)
					parentSchemeProtoGroup.removeSchemeProtoGroup(group);
			} else
				return;
		}
	}
}

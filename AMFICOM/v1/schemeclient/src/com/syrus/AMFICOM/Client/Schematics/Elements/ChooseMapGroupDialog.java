package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.Client.Schematics.UI.*;

public class ChooseMapGroupDialog extends JDialog implements OperationListener
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	ApplicationContext aContext;
	Dispatcher dispatcher = new Dispatcher();
	Class creating_class;
	Object selectedObject;

	JButton okButton;
	JButton cancelButton;
	JButton saveButton;
	SchemeProtoGroupPropsPanel gpp;

	public int retCode = CANCEL;


	public ChooseMapGroupDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
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
		setTitle("Выбор группы");
		//setResizable(false);
		setModal(true);

		Dimension frameSize = new Dimension (400, 465);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		getContentPane().setLayout(new BorderLayout());

		SchemeProtoGroupsTreeModel model = new SchemeProtoGroupsTreeModel(aContext);
		SchemeProtoGroupNavigatorPanel north_panel = new SchemeProtoGroupNavigatorPanel(aContext, dispatcher, model);
		north_panel.setBorder(BorderFactory.createTitledBorder("Список"));
		this.getContentPane().add(north_panel, BorderLayout.CENTER);

		JPanel south_panel = new JPanel();
		south_panel.setLayout(new BorderLayout());

		// Group
		gpp = new SchemeProtoGroupPropsPanel(aContext);
		gpp.setBorder(BorderFactory.createTitledBorder("Группа"));
		gpp.setPreferredSize(new Dimension(400, 125));
		south_panel.add(gpp, BorderLayout.NORTH);

		// BUTTONS
		JPanel buttonPanel = new JPanel();
		JPanel r_buttonPanel = new JPanel();
		JPanel l_buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		r_buttonPanel.setLayout(new FlowLayout());
		l_buttonPanel.setLayout(new FlowLayout());

		okButton = new JButton("OK");
		cancelButton = new JButton("Отмена");
		saveButton = new JButton("Сохранить");

		r_buttonPanel.add(okButton, FlowLayout.LEFT);
		r_buttonPanel.add(cancelButton, FlowLayout.CENTER);
		l_buttonPanel.add(saveButton, FlowLayout.LEFT);
		buttonPanel.add(r_buttonPanel, BorderLayout.EAST);
		buttonPanel.add(l_buttonPanel, BorderLayout.WEST);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				okButton_actionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				dispose();
			}
		});
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				saveButton_actionPerformed();
			}
		});
		south_panel.add(buttonPanel, BorderLayout.CENTER);

		this.getContentPane().add (south_panel, BorderLayout.SOUTH);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public SchemeProtoGroup getSelectedElement()
	{
		if (selectedObject instanceof SchemeProtoGroup)
			return (SchemeProtoGroup)selectedObject;
		return null;
	}

	public int showDialog()
	{
		setVisible(true);
		return retCode;
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent)oe;
			selectedObject = dse.getSelectedObject();
			creating_class = dse.getDataClass();

			if (creating_class.equals(SchemeProtoGroup.class))
			{
				okButton.setEnabled(true);

				if (dse.getSelectionNumber() != -1)
				{
					SchemeProtoGroup map_proto = (SchemeProtoGroup)dse.getList().get(dse.getSelectionNumber());
					gpp.init(map_proto);
				}
			}

//			if (selectedObject instanceof SchemeProtoGroup &&
//					((SchemeProtoGroup)selectedObject).schemeProtoGroups().length == 0 &&
//					((SchemeProtoGroup)selectedObject).schemeProtoElements().length == 0)
//				;
		}
	}


	void okButton_actionPerformed()
	{
		if (selectedObject instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup group = (SchemeProtoGroup)selectedObject;
			if (group.schemeProtoGroups().length == 0)
			{
				retCode = OK;
				dispose();
			}
		}
	}

	void saveButton_actionPerformed()
	{
		if (selectedObject instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup group = gpp.getSchemeProtoGroup();
			try {
				SchemeStorableObjectPool.putStorableObject(group);
			} catch (IllegalObjectEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dispatcher.notify(new TreeListSelectionEvent("", TreeListSelectionEvent.REFRESH_EVENT));
		}
	}

}


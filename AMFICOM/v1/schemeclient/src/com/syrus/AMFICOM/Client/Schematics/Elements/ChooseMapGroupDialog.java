package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
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
	JButton newButton;
	MapProtoElementPropsPanel gpp;

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

		MapProtoGroupsTreeModel model = new MapProtoGroupsTreeModel(aContext.getDataSourceInterface());
		MapProtoNavigatorPanel north_panel = new MapProtoNavigatorPanel(aContext, dispatcher, model);
		north_panel.setBorder(BorderFactory.createTitledBorder("Список"));
		this.getContentPane().add(north_panel, BorderLayout.CENTER);

		JPanel south_panel = new JPanel();
		south_panel.setLayout(new BorderLayout());

		// Group
		gpp = new MapProtoElementPropsPanel(aContext);
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
		newButton = new JButton("Создать");

		r_buttonPanel.add(okButton, FlowLayout.LEFT);
		r_buttonPanel.add(cancelButton, FlowLayout.CENTER);
		l_buttonPanel.add(newButton, FlowLayout.LEFT);
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
		newButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				createButton_actionPerformed();
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

	public MapProtoElement getSelectedElement()
	{
		if (selectedObject instanceof MapProtoElement)
			return (MapProtoElement)selectedObject;
		else
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

			if (creating_class.equals(MapProtoGroup.class))
			{
				if (dse.getSelectionNumber() != -1)
				{
					MapProtoGroup group = (MapProtoGroup)dse.getList().get(dse.getSelectionNumber());
					if (group.mapproto_ids.isEmpty())
						;
				}
				else
					;
			}
			else if (creating_class.equals(MapProtoElement.class))
			{
				okButton.setEnabled(true);

				if (dse.getSelectionNumber() != -1)
				{
					MapProtoElement map_proto = (MapProtoElement)dse.getList().get(dse.getSelectionNumber());
					gpp.init(map_proto, aContext.getDataSourceInterface());
				}
			}

			if (selectedObject instanceof MapProtoGroup &&
					((MapProtoGroup)selectedObject).group_ids.isEmpty() &&
					((MapProtoGroup)selectedObject).mapproto_ids.isEmpty())
				;
			else if (selectedObject instanceof MapProtoElement &&
							 ((MapProtoElement)selectedObject).pe_ids.isEmpty())
				;
		}
	}


	void okButton_actionPerformed()
	{
		if (selectedObject instanceof MapProtoElement)
		{
			retCode = OK;
			dispose();
		}
	}

	void createButton_actionPerformed()
	{
		if (selectedObject instanceof MapProtoGroup)
		{
			MapProtoGroup group = (MapProtoGroup)selectedObject;
			if (group.group_ids.isEmpty())
			{
				MapProtoElement new_proto = gpp.createMapProtoElement();
				if (new_proto != null)
				{
					group.mapproto_ids.add(new_proto.getId());
					aContext.getDataSourceInterface().SaveMapProtoElements(new String[] {new_proto.getId()});
					aContext.getDataSourceInterface().SaveMapProtoGroups(new String[] {group.getId()});

					new_proto.owner_id = aContext.getSessionInterface().getUserId();
					dispatcher.notify(new TreeListSelectionEvent(group.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
				}
			}
		}
	}

}


package com.syrus.AMFICOM.client.observer.ui;

import javax.swing.JInternalFrame;

public class AlarmPopupFrame extends JInternalFrame
{
/*
	private JSplitPane splitPane = new JSplitPane();
	private JScrollPane scrollPane = new JScrollPane();
	private WrapperedList jList = new WrapperedList();
	private JTextPane textPane = new JTextPane();
	List texts = new LinkedList();

	ApplicationContext aContext;
	Alarm alarm;

	static int instances = 0;

	AlarmDescriptorEvent selected_ade = null;

	protected AlarmPopupFrame(String title)
	{
		super(title);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public AlarmPopupFrame(String title, Alarm alarm, ApplicationContext aContext)
	{
		this(title);
		this.alarm = alarm;
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	private void jbInit() throws Exception
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		instances++;

		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setClosable(true);
		this.setMaximizable(false);
		this.setIconifiable(true);
		this.setResizable(true);

		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().setBackground(SystemColor.window);

		splitPane.add(scrollPane, JSplitPane.RIGHT);
		scrollPane.getViewport().add(textPane, null);
		textPane.setEditable(false);
		splitPane.add(jList, JSplitPane.LEFT);
		splitPane.setDividerLocation(screenSize.width / 12);

//		jList.addListSelectionListener(new ItemListener()
//		{
//			public void itemStateChanged(ItemEvent ev)
//			{
//				listItemSelected();
//			}
//		});

		jList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if(e.getValueIsAdjusting())
						return;
					listItemSelected();
				}
			});
		this.addComponentListener(new ComponentAdapter()
			{
				public void componentHidden(ComponentEvent e)
				{
					instances--;
				}
			});
//		this.addWindowListener(new java.awt.event.WindowAdapter()
//		{
//			public void windowClosing(WindowEvent e)
//			{
//				instances--;
//			}
//		});
	}

	public void toFront()
	{
		if(jList.getModel().getSize() > 0)
		{
			jList.setSelectedIndex(0);
		}
//		listItemSelected();
//		super.toFront();
	}

	public void set(AlarmDescriptor ad)
	{
		jList.removeAll();
		for (int i = 0; i < ad.getCount(); i++)
			addMessage(ad.get(i));
	}

	public void addMessage(AlarmDescriptorEvent ade)
//	String title, String text)
	{
		jList.add(ade);
		texts.add(ade.getText());
	}

	void listItemSelected()
	{
		if(selected_ade != null)
		{
			MapNavigateEvent mne = null;
			if(selected_ade instanceof OpticalAlarmDescriptorEvent)
			{
				OpticalAlarmDescriptorEvent oe = (OpticalAlarmDescriptorEvent )selected_ade;
				mne = new MapNavigateEvent(
					this,
					MapNavigateEvent.DATA_ALARMMARKER_DELETED_EVENT,
					"amarker" + oe.ra.alarmPointCoord,
					oe.ra.alarmPointCoord * oe.delta_x,
					"",
					alarm.getMonitoredElementId(),
	//				oe.getLinkID(0.0035));//test node blinking
					oe.getLink(oe.ra.alarmPointCoord * oe.delta_x / 1000.).getObjectId());
			}
			if(aContext.getDispatcher() != null)
				if(mne != null)
					aContext.getDispatcher().notify(mne);
			selected_ade = null;
		}

		if(jList.getSelectedIndex() == -1)
		{
			textPane.setText("");
			textPane.setBackground(Color.white);
			textPane.setForeground(Color.black);

			return;
		}
		textPane.setText((String)texts.get(jList.getSelectedIndex()));

		selected_ade = (AlarmDescriptorEvent )jList.getSelectedObjectResource();

		textPane.setBackground(selected_ade.getColor());
		textPane.setForeground(selected_ade.getTextColor());

		MapNavigateEvent mne = null;

		if(aContext.getDispatcher() != null)
		{

			Pool.put("activecontext", "useractionselected", "alarm_selected");
			Pool.put("activecontext", "selected_id", alarm.getId());
			aContext.getDispatcher().notify(new OperationEvent(this, 0, "activecontextevent"));

//			Pool.put("activecontext", "useractionselected", "result_selected");
//			Pool.put("activecontext", "selected_id", alarm.getResult().getId());
//			aContext.getDispatcher().notify(new OperationEvent(this, 0, "activecontextevent"));
		}

		if(selected_ade instanceof OpticalAlarmDescriptorEvent)
		{
			OpticalAlarmDescriptorEvent oe = (OpticalAlarmDescriptorEvent )selected_ade;
			mne = new MapNavigateEvent (
				this,
				MapNavigateEvent.DATA_ALARMMARKER_CREATED_EVENT,
				"amarker" + oe.ra.alarmPointCoord,
				oe.ra.alarmPointCoord * oe.delta_x,
				"",
				alarm.getMonitoredElementId(),
//				oe.getLinkID(0.0035));//test node blinking
				oe.getLink(oe.ra.alarmPointCoord * oe.delta_x / 1000.).getObjectId());
		}
		if(aContext.getDispatcher() != null)
			if(mne != null)
				aContext.getDispatcher().notify(mne);
	}

	public JTextPane getDescriptionPane()
	{
		return this.textPane;
	}
*/
}


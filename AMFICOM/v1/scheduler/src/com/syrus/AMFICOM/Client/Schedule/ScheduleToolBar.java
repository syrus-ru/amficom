package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;


public class ScheduleToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton buttonOpenSession = new JButton();
	JButton buttonOpenAllWindows = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ScheduleToolBar()
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		ScheduleToolBar_this_actionAdapter actionAdapter =
			new ScheduleToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		buttonOpenSession = new JButton();
		buttonOpenSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonOpenSession.setText("");
		buttonOpenSession.setMaximumSize(buttonSize);
		buttonOpenSession.setPreferredSize(buttonSize);
		buttonOpenSession.setToolTipText(LangModelScheduleOld.ToolTip("menuSessionNew"));
		buttonOpenSession.setName("menuSessionNew");
		buttonOpenSession.addActionListener(actionAdapter);
		add(buttonOpenSession);

		add(new JLabel("  "));

		buttonOpenAllWindows = new JButton();
		buttonOpenAllWindows.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openall.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonOpenAllWindows.setText("");
		buttonOpenAllWindows.setMaximumSize(buttonSize);
		buttonOpenAllWindows.setPreferredSize(buttonSize);
		buttonOpenAllWindows.setToolTipText(LangModelScheduleOld.ToolTip("menuViewAll"));
		buttonOpenAllWindows.setName("menuViewAll");
		buttonOpenAllWindows.addActionListener(actionAdapter);
		add(buttonOpenAllWindows);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		buttonOpenSession.setVisible(aModel.isVisible("menuSessionNew"));
		buttonOpenSession.setEnabled(aModel.isEnabled("menuSessionNew"));

		buttonOpenAllWindows.setVisible(aModel.isVisible("menuViewAll"));
		buttonOpenAllWindows.setEnabled(aModel.isEnabled("menuViewAll"));

	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

class ScheduleToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ScheduleToolBar adaptee;

	ScheduleToolBar_this_actionAdapter(ScheduleToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}


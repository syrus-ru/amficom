package com.syrus.AMFICOM.Client.Map.Editor;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.*;

public class MapToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
	JButton buttonCloseSession = new JButton();
	JButton menuSessionDomain = new JButton();
	JButton menuMapNew = new JButton();
	JButton menuMapOpen = new JButton();
	JButton menuMapSave = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public MapToolBar()
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
		MapToolBar_this_actionAdapter actionAdapter =
				new MapToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.ToolTip("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		buttonCloseSession.setMaximumSize(buttonSize);
		buttonCloseSession.setPreferredSize(buttonSize);
		buttonCloseSession.setToolTipText(LangModel.ToolTip("menuSessionClose"));
		buttonCloseSession.setName("menuSessionClose");
//		buttonCloseSession.addActionListener(actionAdapter);

		menuSessionDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuSessionDomain.setMaximumSize(buttonSize);
		menuSessionDomain.setPreferredSize(buttonSize);
		menuSessionDomain.setToolTipText(LangModel.ToolTip("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
//		menuSessionDomain.addActionListener(actionAdapter);

		menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapNew.setMaximumSize(buttonSize);
		menuMapNew.setPreferredSize(buttonSize);
		menuMapNew.setToolTipText(LangModelMap.ToolTip("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);

		menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapOpen.setMaximumSize(buttonSize);
		menuMapOpen.setPreferredSize(buttonSize);
		menuMapOpen.setToolTipText(LangModelMap.ToolTip("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);

		menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapSave.setMaximumSize(buttonSize);
		menuMapSave.setPreferredSize(buttonSize);
		menuMapSave.setToolTipText(LangModelMap.ToolTip("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);

		add(sessionOpen);
//		add(buttonCloseSession);
//		addSeparator();
//		add(menuSessionDomain);
		addSeparator();
		add(menuMapNew);
		add(menuMapOpen);
		add(menuMapSave);
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
		sessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		sessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));
		buttonCloseSession.setVisible(aModel.isVisible("menuSessionClose"));
		buttonCloseSession.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));
		menuMapNew.setVisible(aModel.isVisible("menuMapNew"));
		menuMapNew.setEnabled(aModel.isEnabled("menuMapNew"));
		menuMapOpen.setVisible(aModel.isVisible("menuMapOpen"));
		menuMapOpen.setEnabled(aModel.isEnabled("menuMapOpen"));
		menuMapSave.setVisible(aModel.isVisible("menuMapSave"));
		menuMapSave.setEnabled(aModel.isEnabled("menuMapSave"));
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

class MapToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	MapToolBar adaptee;

	MapToolBar_this_actionAdapter(MapToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
//		System.out.println("MapToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}

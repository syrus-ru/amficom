package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

public class ElementsEditorToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();

	JButton componentNew = new JButton();
	JButton componentSave = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ElementsEditorToolBar()
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
		ElementsEditorToolBar_this_actionAdapter actionAdapter =
				new ElementsEditorToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.ToolTip("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		componentNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		componentNew.setMaximumSize(buttonSize);
		componentNew.setPreferredSize(buttonSize);
		componentNew.setToolTipText(LangModelSchematics.ToolTip("menuComponentNew"));
		componentNew.setName("menuComponentNew");
		componentNew.addActionListener(actionAdapter);

		componentSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		componentSave.setMaximumSize(buttonSize);
		componentSave.setPreferredSize(buttonSize);
		componentSave.setToolTipText(LangModelSchematics.ToolTip("menuComponentSave"));
		componentSave.setName("menuComponentSave");
		componentSave.addActionListener(actionAdapter);

		add(sessionOpen);
		addSeparator();
		add(componentNew);
		add(componentSave);

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
		componentNew.setVisible(aModel.isVisible("menuComponentNew"));
		componentNew.setEnabled(aModel.isEnabled("menuComponentNew"));
		componentSave.setVisible(aModel.isVisible("menuComponentSave"));
		componentSave.setEnabled(aModel.isEnabled("menuComponentSave"));
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

class ElementsEditorToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ElementsEditorToolBar adaptee;

	ElementsEditorToolBar_this_actionAdapter(ElementsEditorToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
package com.syrus.AMFICOM.Client.Schematics.Scheme;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class SchemeEditorToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;
	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	JButton sessionOpen = new JButton();

	JButton schemeNew = new JButton();
	JButton schemeLoad = new JButton();
	JButton schemeSave = new JButton();
	JButton catalog = new JButton();

	public SchemeEditorToolBar()
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
		SchemeEditorToolBar_this_actionAdapter actionAdapter =
				new SchemeEditorToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		schemeNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeNew.setMaximumSize(buttonSize);
		schemeNew.setPreferredSize(buttonSize);
		schemeNew.setToolTipText(LangModelSchematics.getString("menuSchemeNew"));
		schemeNew.setName("menuSchemeNew");
		schemeNew.addActionListener(actionAdapter);

		schemeSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeSave.setMaximumSize(buttonSize);
		schemeSave.setPreferredSize(buttonSize);
		schemeSave.setToolTipText(LangModelSchematics.getString("menuSchemeSave"));
		schemeSave.setName("menuSchemeSave");
		schemeSave.addActionListener(actionAdapter);

		schemeLoad.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeLoad.setMaximumSize(buttonSize);
		schemeLoad.setPreferredSize(buttonSize);
		schemeLoad.setToolTipText(LangModelSchematics.getString("menuSchemeLoad"));
		schemeLoad.setName("menuSchemeLoad");
		schemeLoad.addActionListener(actionAdapter);

		catalog.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/catalog.gif")
																		 .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		catalog.setMaximumSize(buttonSize);
		catalog.setPreferredSize(buttonSize);
		catalog.setToolTipText(LangModelSchematics.getString("menuInsertToCatalog"));
		catalog.setName("menuInsertToCatalog");
		catalog.addActionListener(actionAdapter);

		add(sessionOpen);
		addSeparator();
		add(schemeNew);
		add(schemeLoad);
		add(schemeSave);
		addSeparator();
		add(catalog);
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
		schemeNew.setVisible(aModel.isVisible("menuSchemeNew"));
		schemeNew.setEnabled(aModel.isEnabled("menuSchemeNew"));
		schemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
		schemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
		schemeLoad.setVisible(aModel.isVisible("menuSchemeLoad"));
		schemeLoad.setEnabled(aModel.isEnabled("menuSchemeLoad"));
		catalog.setVisible(aModel.isVisible("menuInsertToCatalog"));
		catalog.setEnabled(aModel.isEnabled("menuInsertToCatalog"));
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

class SchemeEditorToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	SchemeEditorToolBar adaptee;

	SchemeEditorToolBar_this_actionAdapter(SchemeEditorToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

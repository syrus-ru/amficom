/*-
 * $Id: ElementsEditorToolBar.java,v 1.3 2005/06/17 11:36:22 bass Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/17 11:36:22 $
 * @module schemeclient_v1
 */

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
		} catch (Exception e)
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
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		componentNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		componentNew.setMaximumSize(buttonSize);
		componentNew.setPreferredSize(buttonSize);
		componentNew.setToolTipText(LangModelSchematics.getString("menuComponentNew"));
		componentNew.setName("menuComponentNew");
		componentNew.addActionListener(actionAdapter);

		componentSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif")
																			.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		componentSave.setMaximumSize(buttonSize);
		componentSave.setPreferredSize(buttonSize);
		componentSave.setToolTipText(LangModelSchematics.getString("menuComponentSave"));
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
	
	public void modelChanged(String e) {
		this.modelChanged(new String[] {e});
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


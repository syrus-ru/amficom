/**
 * $Id: MapEditorToolBar.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * Панель инструментов модуля "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
	JButton buttonCloseSession = new JButton();
	JButton menuSessionDomain = new JButton();
	JButton menuMapNew = new JButton();
	JButton menuMapOpen = new JButton();
	JButton menuMapSave = new JButton();
	JButton menuMapViewNew = new JButton();
	JButton menuMapViewOpen = new JButton();
	JButton menuMapViewSave = new JButton();
	JButton menuSchemeAddToView = new JButton();
	JButton menuSchemeRemoveFromView = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public MapEditorToolBar()
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

	private void jbInit()
	{
		MapToolBar_this_actionAdapter actionAdapter =
				new MapToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		buttonCloseSession.setMaximumSize(buttonSize);
		buttonCloseSession.setPreferredSize(buttonSize);
		buttonCloseSession.setToolTipText(LangModel.getString("menuSessionClose"));
		buttonCloseSession.setName("menuSessionClose");
//		buttonCloseSession.addActionListener(actionAdapter);

		menuSessionDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuSessionDomain.setMaximumSize(buttonSize);
		menuSessionDomain.setPreferredSize(buttonSize);
		menuSessionDomain.setToolTipText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
//		menuSessionDomain.addActionListener(actionAdapter);

		menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapNew.setMaximumSize(buttonSize);
		menuMapNew.setPreferredSize(buttonSize);
		menuMapNew.setToolTipText(LangModelMap.getString("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);

		menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapOpen.setMaximumSize(buttonSize);
		menuMapOpen.setPreferredSize(buttonSize);
		menuMapOpen.setToolTipText(LangModelMap.getString("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);

		menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapSave.setMaximumSize(buttonSize);
		menuMapSave.setPreferredSize(buttonSize);
		menuMapSave.setToolTipText(LangModelMap.getString("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);

		menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newview.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapViewNew.setMaximumSize(buttonSize);
		menuMapViewNew.setPreferredSize(buttonSize);
		menuMapViewNew.setToolTipText(LangModelMap.getString("menuMapViewNew"));
		menuMapViewNew.setName("menuMapViewNew");
		menuMapViewNew.addActionListener(actionAdapter);

		menuMapViewOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openview.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapViewOpen.setMaximumSize(buttonSize);
		menuMapViewOpen.setPreferredSize(buttonSize);
		menuMapViewOpen.setToolTipText(LangModelMap.getString("menuMapViewOpen"));
		menuMapViewOpen.setName("menuMapViewOpen");
		menuMapViewOpen.addActionListener(actionAdapter);

		menuMapViewSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/saveview.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuMapViewSave.setMaximumSize(buttonSize);
		menuMapViewSave.setPreferredSize(buttonSize);
		menuMapViewSave.setToolTipText(LangModelMap.getString("menuMapViewSave"));
		menuMapViewSave.setName("menuMapViewSave");
		menuMapViewSave.addActionListener(actionAdapter);

		menuSchemeAddToView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuSchemeAddToView.setMaximumSize(buttonSize);
		menuSchemeAddToView.setPreferredSize(buttonSize);
		menuSchemeAddToView.setToolTipText(LangModelMap.getString("menuSchemeAddToView"));
		menuSchemeAddToView.setName("menuSchemeAddToView");
		menuSchemeAddToView.addActionListener(actionAdapter);

		menuSchemeRemoveFromView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuSchemeRemoveFromView.setMaximumSize(buttonSize);
		menuSchemeRemoveFromView.setPreferredSize(buttonSize);
		menuSchemeRemoveFromView.setToolTipText(LangModelMap.getString("menuSchemeRemoveFromView"));
		menuSchemeRemoveFromView.setName("menuSchemeRemoveFromView");
		menuSchemeRemoveFromView.addActionListener(actionAdapter);

		add(sessionOpen);
//		add(buttonCloseSession);
//		addSeparator();
//		add(menuSessionDomain);
		addSeparator();
		add(menuMapNew);
		add(menuMapOpen);
		add(menuMapSave);
		addSeparator();
		add(menuMapViewNew);
		add(menuMapViewOpen);
		add(menuMapViewSave);
		addSeparator();
		add(menuSchemeAddToView);
		add(menuSchemeRemoveFromView);
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

		menuMapViewNew.setVisible(aModel.isVisible("menuMapViewNew"));
		menuMapViewNew.setEnabled(aModel.isEnabled("menuMapViewNew"));
		menuMapViewOpen.setVisible(aModel.isVisible("menuMapViewOpen"));
		menuMapViewOpen.setEnabled(aModel.isEnabled("menuMapViewOpen"));
		menuMapViewSave.setVisible(aModel.isVisible("menuMapViewSave"));
		menuMapViewSave.setEnabled(aModel.isEnabled("menuMapViewSave"));

		menuSchemeAddToView.setVisible(aModel.isVisible("menuSchemeAddToView"));
		menuSchemeAddToView.setEnabled(aModel.isEnabled("menuSchemeAddToView"));
		menuSchemeRemoveFromView.setVisible(aModel.isVisible("menuSchemeRemoveFromView"));
		menuSchemeRemoveFromView.setEnabled(aModel.isEnabled("menuSchemeRemoveFromView"));
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
	MapEditorToolBar adaptee;

	MapToolBar_this_actionAdapter(MapEditorToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
//		System.out.println("MapToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}

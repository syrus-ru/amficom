/**
 * $Id: MapEditorToolBar.java,v 1.8 2005/01/26 16:26:14 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.event.ActionListener;

/**
 * Панель инструментов модуля "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2005/01/26 16:26:14 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	/**
	 * <img src="images/open_session.gif">
	 */
	JButton sessionOpen = new JButton();
	/**
	 * <img src="images/close_session.gif">
	 */
	JButton buttonCloseSession = new JButton();
	/**
	 * <img src="images/domains.gif">
	 */
	JButton menuSessionDomain = new JButton();
	/**
	 * <img src="images/new.gif">
	 */
	JButton menuMapNew = new JButton();
	/**
	 * <img src="images/map_mini.gif">
	 */
	JButton menuMapOpen = new JButton();
	/**
	 * <img src="images/save.gif">
	 */
	JButton menuMapSave = new JButton();
	/**
	 * <img src="images/newview.gif">
	 */
	JButton menuMapViewNew = new JButton();
	/**
	 * <img src="images/openview.gif">
	 */
	JButton menuMapViewOpen = new JButton();
	/**
	 * <img src="images/saveview.gif">
	 */
	JButton menuMapViewSave = new JButton();
	/**
	 * <img src="images/addtoview.gif">
	 */
	JButton menuMapViewAddScheme = new JButton();
	/**
	 * <img src="images/removefromview.gif">
	 */
	JButton menuMapViewRemoveScheme = new JButton();

	/**
	 * <img src="images/mapsetup.gif">
	 */
	JButton menuViewSetup = new JButton();

	public static final int IMG_SIZE = 16;
	public static final int BTN_SIZE = 24;

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
		ActionListener actionAdapter =
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonActionPerformed(e);
				}
			};

		Dimension buttonSize = new Dimension(BTN_SIZE, BTN_SIZE);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		buttonCloseSession.setMaximumSize(buttonSize);
		buttonCloseSession.setPreferredSize(buttonSize);
		buttonCloseSession.setToolTipText(LangModel.getString("menuSessionClose"));
		buttonCloseSession.setName("menuSessionClose");
//		buttonCloseSession.addActionListener(actionAdapter);

		menuSessionDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuSessionDomain.setMaximumSize(buttonSize);
		menuSessionDomain.setPreferredSize(buttonSize);
		menuSessionDomain.setToolTipText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
//		menuSessionDomain.addActionListener(actionAdapter);

		menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapNew.setMaximumSize(buttonSize);
		menuMapNew.setPreferredSize(buttonSize);
		menuMapNew.setToolTipText(LangModelMap.getString("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);

		menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapOpen.setMaximumSize(buttonSize);
		menuMapOpen.setPreferredSize(buttonSize);
		menuMapOpen.setToolTipText(LangModelMap.getString("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);

		menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapSave.setMaximumSize(buttonSize);
		menuMapSave.setPreferredSize(buttonSize);
		menuMapSave.setToolTipText(LangModelMap.getString("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);

		menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapViewNew.setMaximumSize(buttonSize);
		menuMapViewNew.setPreferredSize(buttonSize);
		menuMapViewNew.setToolTipText(LangModelMap.getString("menuMapViewNew"));
		menuMapViewNew.setName("menuMapViewNew");
		menuMapViewNew.addActionListener(actionAdapter);

		menuMapViewOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapViewOpen.setMaximumSize(buttonSize);
		menuMapViewOpen.setPreferredSize(buttonSize);
		menuMapViewOpen.setToolTipText(LangModelMap.getString("menuMapViewOpen"));
		menuMapViewOpen.setName("menuMapViewOpen");
		menuMapViewOpen.addActionListener(actionAdapter);

		menuMapViewSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/saveview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapViewSave.setMaximumSize(buttonSize);
		menuMapViewSave.setPreferredSize(buttonSize);
		menuMapViewSave.setToolTipText(LangModelMap.getString("menuMapViewSave"));
		menuMapViewSave.setName("menuMapViewSave");
		menuMapViewSave.addActionListener(actionAdapter);

		menuMapViewAddScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapViewAddScheme.setMaximumSize(buttonSize);
		menuMapViewAddScheme.setPreferredSize(buttonSize);
		menuMapViewAddScheme.setToolTipText(LangModelMap.getString("menuMapViewAddScheme"));
		menuMapViewAddScheme.setName("menuMapViewAddScheme");
		menuMapViewAddScheme.addActionListener(actionAdapter);

		menuMapViewRemoveScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuMapViewRemoveScheme.setMaximumSize(buttonSize);
		menuMapViewRemoveScheme.setPreferredSize(buttonSize);
		menuMapViewRemoveScheme.setToolTipText(LangModelMap.getString("menuMapViewRemoveScheme"));
		menuMapViewRemoveScheme.setName("menuMapViewRemoveScheme");
		menuMapViewRemoveScheme.addActionListener(actionAdapter);

		menuViewSetup.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/mapsetup.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		menuViewSetup.setMaximumSize(buttonSize);
		menuViewSetup.setPreferredSize(buttonSize);
		menuViewSetup.setToolTipText(LangModelMap.getString("menuViewSetup"));
		menuViewSetup.setName("menuViewSetup");
		menuViewSetup.addActionListener(actionAdapter);

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
		add(menuMapViewAddScheme);
		add(menuMapViewRemoveScheme);
		addSeparator();
		add(menuViewSetup);
		addSeparator();
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
		menuMapViewAddScheme.setVisible(aModel.isVisible("menuMapViewAddScheme"));
		menuMapViewAddScheme.setEnabled(aModel.isEnabled("menuMapViewAddScheme"));
		menuMapViewRemoveScheme.setVisible(aModel.isVisible("menuMapViewRemoveScheme"));
		menuMapViewRemoveScheme.setEnabled(aModel.isEnabled("menuMapViewRemoveScheme"));

		menuViewSetup.setVisible(aModel.isVisible("menuViewSetup"));
		menuViewSetup.setEnabled(aModel.isEnabled("menuViewSetup"));
	}

	public void buttonActionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command.execute();
	}
}

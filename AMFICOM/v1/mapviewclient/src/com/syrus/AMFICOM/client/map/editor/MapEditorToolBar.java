/**
 * $Id: MapEditorToolBar.java,v 1.9 2005/02/07 16:09:26 krupenn Exp $
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
 * Панель инструментов модуля "Редактор топологических схем". 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 */
public class MapEditorToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	/**
	 * <img src="images/open_session.gif">.
	 */
	JButton sessionOpen = new JButton();
	/**
	 * <img src="images/close_session.gif">.
	 */
	JButton buttonCloseSession = new JButton();
	/**
	 * <img src="images/domains.gif">.
	 */
	JButton menuSessionDomain = new JButton();
	/**
	 * <img src="images/new.gif">.
	 */
	JButton menuMapNew = new JButton();
	/**
	 * <img src="images/map_mini.gif">.
	 */
	JButton menuMapOpen = new JButton();
	/**
	 * <img src="images/save.gif">.
	 */
	JButton menuMapSave = new JButton();
	/**
	 * <img src="images/newview.gif">.
	 */
	JButton menuMapViewNew = new JButton();
	/**
	 * <img src="images/openview.gif">.
	 */
	JButton menuMapViewOpen = new JButton();
	/**
	 * <img src="images/saveview.gif">.
	 */
	JButton menuMapViewSave = new JButton();
	/**
	 * <img src="images/addtoview.gif">.
	 */
	JButton menuMapViewAddScheme = new JButton();
	/**
	 * <img src="images/removefromview.gif">.
	 */
	JButton menuMapViewRemoveScheme = new JButton();

	/**
	 * <img src="images/mapsetup.gif">.
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

		this.sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.sessionOpen.setMaximumSize(buttonSize);
		this.sessionOpen.setPreferredSize(buttonSize);
		this.sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		this.sessionOpen.setName("menuSessionNew");
		this.sessionOpen.addActionListener(actionAdapter);

		this.buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.buttonCloseSession.setMaximumSize(buttonSize);
		this.buttonCloseSession.setPreferredSize(buttonSize);
		this.buttonCloseSession.setToolTipText(LangModel.getString("menuSessionClose"));
		this.buttonCloseSession.setName("menuSessionClose");
//		this.buttonCloseSession.addActionListener(actionAdapter);

		this.menuSessionDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuSessionDomain.setMaximumSize(buttonSize);
		this.menuSessionDomain.setPreferredSize(buttonSize);
		this.menuSessionDomain.setToolTipText(LangModel.getString("menuSessionDomain"));
		this.menuSessionDomain.setName("menuSessionDomain");
//		this.menuSessionDomain.addActionListener(actionAdapter);

		this.menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapNew.setMaximumSize(buttonSize);
		this.menuMapNew.setPreferredSize(buttonSize);
		this.menuMapNew.setToolTipText(LangModelMap.getString("menuMapNew"));
		this.menuMapNew.setName("menuMapNew");
		this.menuMapNew.addActionListener(actionAdapter);

		this.menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapOpen.setMaximumSize(buttonSize);
		this.menuMapOpen.setPreferredSize(buttonSize);
		this.menuMapOpen.setToolTipText(LangModelMap.getString("menuMapOpen"));
		this.menuMapOpen.setName("menuMapOpen");
		this.menuMapOpen.addActionListener(actionAdapter);

		this.menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapSave.setMaximumSize(buttonSize);
		this.menuMapSave.setPreferredSize(buttonSize);
		this.menuMapSave.setToolTipText(LangModelMap.getString("menuMapSave"));
		this.menuMapSave.setName("menuMapSave");
		this.menuMapSave.addActionListener(actionAdapter);

		this.menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewNew.setMaximumSize(buttonSize);
		this.menuMapViewNew.setPreferredSize(buttonSize);
		this.menuMapViewNew.setToolTipText(LangModelMap.getString("menuMapViewNew"));
		this.menuMapViewNew.setName("menuMapViewNew");
		this.menuMapViewNew.addActionListener(actionAdapter);

		this.menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewNew.setMaximumSize(buttonSize);
		this.menuMapViewNew.setPreferredSize(buttonSize);
		this.menuMapViewNew.setToolTipText(LangModelMap.getString("menuMapViewOpen"));
		this.menuMapViewNew.setName("menuMapViewOpen");
		this.menuMapViewNew.addActionListener(actionAdapter);

		this.menuMapViewSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/saveview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewSave.setMaximumSize(buttonSize);
		this.menuMapViewSave.setPreferredSize(buttonSize);
		this.menuMapViewSave.setToolTipText(LangModelMap.getString("menuMapViewSave"));
		this.menuMapViewSave.setName("menuMapViewSave");
		this.menuMapViewSave.addActionListener(actionAdapter);

		this.menuMapViewAddScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewAddScheme.setMaximumSize(buttonSize);
		this.menuMapViewAddScheme.setPreferredSize(buttonSize);
		this.menuMapViewAddScheme.setToolTipText(LangModelMap.getString("menuMapViewAddScheme"));
		this.menuMapViewAddScheme.setName("menuMapViewAddScheme");
		this.menuMapViewAddScheme.addActionListener(actionAdapter);

		this.menuMapViewRemoveScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewRemoveScheme.setMaximumSize(buttonSize);
		this.menuMapViewRemoveScheme.setPreferredSize(buttonSize);
		this.menuMapViewRemoveScheme.setToolTipText(LangModelMap.getString("menuMapViewRemoveScheme"));
		this.menuMapViewRemoveScheme.setName("menuMapViewRemoveScheme");
		this.menuMapViewRemoveScheme.addActionListener(actionAdapter);

		this.menuViewSetup.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/mapsetup.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuViewSetup.setMaximumSize(buttonSize);
		this.menuViewSetup.setPreferredSize(buttonSize);
		this.menuViewSetup.setToolTipText(LangModelMap.getString("menuViewSetup"));
		this.menuViewSetup.setName("menuViewSetup");
		this.menuViewSetup.addActionListener(actionAdapter);

		add(this.sessionOpen);
//		add(this.buttonCloseSession);
//		addSeparator();
//		add(this.menuSessionDomain);
		addSeparator();
		add(this.menuMapNew);
		add(this.menuMapOpen);
		add(this.menuMapSave);
		addSeparator();
		add(this.menuMapViewNew);
		add(this.menuMapViewNew);
		add(this.menuMapViewSave);
		addSeparator();
		add(this.menuMapViewAddScheme);
		add(this.menuMapViewRemoveScheme);
		addSeparator();
		add(this.menuViewSetup);
		addSeparator();
	}

	public void setModel(ApplicationModel a)
	{
		this.aModel = a;
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String e[])
	{
		this.sessionOpen.setVisible(this.aModel.isVisible("menuSessionNew"));
		this.sessionOpen.setEnabled(this.aModel.isEnabled("menuSessionNew"));
		this.buttonCloseSession.setVisible(this.aModel.isVisible("menuSessionClose"));
		this.buttonCloseSession.setEnabled(this.aModel.isEnabled("menuSessionClose"));
		this.menuSessionDomain.setVisible(this.aModel.isVisible("menuSessionDomain"));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled("menuSessionDomain"));

		this.menuMapNew.setVisible(this.aModel.isVisible("menuMapNew"));
		this.menuMapNew.setEnabled(this.aModel.isEnabled("menuMapNew"));
		this.menuMapOpen.setVisible(this.aModel.isVisible("menuMapOpen"));
		this.menuMapOpen.setEnabled(this.aModel.isEnabled("menuMapOpen"));
		this.menuMapSave.setVisible(this.aModel.isVisible("menuMapSave"));
		this.menuMapSave.setEnabled(this.aModel.isEnabled("menuMapSave"));

		this.menuMapViewNew.setVisible(this.aModel.isVisible("menuMapViewNew"));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled("menuMapViewNew"));
		this.menuMapViewNew.setVisible(this.aModel.isVisible("menuMapViewOpen"));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled("menuMapViewOpen"));
		this.menuMapViewSave.setVisible(this.aModel.isVisible("menuMapViewSave"));
		this.menuMapViewSave.setEnabled(this.aModel.isEnabled("menuMapViewSave"));
		this.menuMapViewAddScheme.setVisible(this.aModel.isVisible("menuMapViewAddScheme"));
		this.menuMapViewAddScheme.setEnabled(this.aModel.isEnabled("menuMapViewAddScheme"));
		this.menuMapViewRemoveScheme.setVisible(this.aModel.isVisible("menuMapViewRemoveScheme"));
		this.menuMapViewRemoveScheme.setEnabled(this.aModel.isEnabled("menuMapViewRemoveScheme"));

		this.menuViewSetup.setVisible(this.aModel.isVisible("menuViewSetup"));
		this.menuViewSetup.setEnabled(this.aModel.isEnabled("menuViewSetup"));
	}

	public void buttonActionPerformed(ActionEvent e)
	{
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}
}

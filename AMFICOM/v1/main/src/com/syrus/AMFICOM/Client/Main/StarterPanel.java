//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Панель запуска модулей ПО АМФИКОМ                          * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        Main\StarterPanel.java                                        * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class StarterPanel
		extends JPanel
		implements ApplicationModelListener, MouseListener
{
	ApplicationModel aModel;

	JLabel labelAdmin = new JLabel();
	JLabel labelConfig = new JLabel();
	JLabel labelComponents = new JLabel();
	JLabel labelScheme = new JLabel();
	JLabel labelMap = new JLabel();
	JLabel labelTrace = new JLabel();
	JLabel labelSchedule = new JLabel();
	JLabel labelSurvey = new JLabel();
	JLabel labelModel = new JLabel();
	JLabel labelMonitor = new JLabel();
	JLabel labelAnalyse = new JLabel();
	JLabel labelNorms = new JLabel();
	JLabel labelMaintain = new JLabel();
	JLabel labelPrognosis = new JLabel();
	JLabel labelReportBuilder = new JLabel();

	XYLayout xYLayout1 = new XYLayout();

	Border normalBorder = null;
	/*
			BorderFactory.createBevelBorder(
					BevelBorder.RAISED,
					Color.white,
					Color.white,
					new Color(142, 142, 142),
					new Color(99, 99, 99));
*/
	Border highlightBorder =
			BorderFactory.createBevelBorder(
					BevelBorder.RAISED,
					Color.white,
					Color.white,
					new Color(142, 142, 142),
					new Color(99, 99, 99));

	protected static int ROW_OFFSET = 30;
	protected static int ROW_INTERVAL = 100;
	protected static int COL_COUNT = 5;
	protected static int COL_OFFSET = 30;
	protected static int COL_INTERVAL = 100;


	public StarterPanel()
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

	public StarterPanel(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		int img_siz = 48;
		int btn_siz = 52;
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);
		
		this.setLayout(xYLayout1);

		this.setPreferredSize(new Dimension(510, 300));
		this.setMinimumSize(new Dimension(510, 300));
		labelAdmin.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/administrate.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelAdmin.setText("");
		labelAdmin.setMaximumSize(buttonSize);
		labelAdmin.setPreferredSize(buttonSize);
		labelAdmin.setToolTipText(LangModelMain.getString("menuToolsAdmin"));
		labelAdmin.setName("menuToolsAdmin");
		labelAdmin.addMouseListener(this);
		labelAdmin.setBorder(normalBorder);

		labelConfig.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/configure.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelConfig.setText("");
		labelConfig.setMaximumSize(buttonSize);
		labelConfig.setPreferredSize(buttonSize);
		labelConfig.setToolTipText(LangModelMain.getString("menuToolsConfig"));
		labelConfig.setName("menuToolsConfig");
		labelConfig.addMouseListener(this);
		labelConfig.setBorder(normalBorder);

		labelComponents.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/components.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelComponents.setText("");
		labelComponents.setMaximumSize(buttonSize);
		labelComponents.setPreferredSize(buttonSize);
		labelComponents.setToolTipText(LangModelMain.getString("menuToolsComponents"));
		labelComponents.setName("menuToolsComponents");
		labelComponents.addMouseListener(this);
		labelComponents.setBorder(normalBorder);

		labelScheme.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/schematics.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelScheme.setText("");
		labelScheme.setMaximumSize(buttonSize);
		labelScheme.setPreferredSize(buttonSize);
		labelScheme.setToolTipText(LangModelMain.getString("menuToolsScheme"));
		labelScheme.setName("menuToolsScheme");
		labelScheme.addMouseListener(this);
		labelScheme.setBorder(normalBorder);

		labelMap.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/map.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelMap.setText("");
		labelMap.setMaximumSize(buttonSize);
		labelMap.setPreferredSize(buttonSize);
		labelMap.setToolTipText(LangModelMain.getString("menuToolsMap"));
		labelMap.setName("menuToolsMap");
		labelMap.addMouseListener(this);
		labelMap.setBorder(normalBorder);

		labelTrace.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/design.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelTrace.setText("");
		labelTrace.setMaximumSize(buttonSize);
		labelTrace.setPreferredSize(buttonSize);
		labelTrace.setToolTipText(LangModelMain.getString("menuToolsTrace"));
		labelTrace.setName("menuToolsTrace");
		labelTrace.addMouseListener(this);
		labelTrace.setBorder(normalBorder);

		labelSchedule.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/scheduling.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelSchedule.setText("");
		labelSchedule.setMaximumSize(buttonSize);
		labelSchedule.setPreferredSize(buttonSize);
		labelSchedule.setToolTipText(LangModelMain.getString("menuToolsSchedule"));
		labelSchedule.setName("menuToolsSchedule");
		labelSchedule.addMouseListener(this);
		labelSchedule.setBorder(normalBorder);

		labelSurvey.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/observe.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelSurvey.setText("");
		labelSurvey.setMaximumSize(buttonSize);
		labelSurvey.setPreferredSize(buttonSize);
		labelSurvey.setToolTipText(LangModelMain.getString("menuToolsSurvey"));
		labelSurvey.setName("menuToolsSurvey");
		labelSurvey.addMouseListener(this);
		labelSurvey.setBorder(normalBorder);

		labelModel.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/model.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelModel.setText("");
		labelModel.setMaximumSize(buttonSize);
		labelModel.setPreferredSize(buttonSize);
		labelModel.setToolTipText(LangModelMain.getString("menuToolsModel"));
		labelModel.setName("menuToolsModel");
		labelModel.addMouseListener(this);
		labelModel.setBorder(normalBorder);

		labelMonitor.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/analyse.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelMonitor.setText("");
		labelMonitor.setMaximumSize(buttonSize);
		labelMonitor.setPreferredSize(buttonSize);
		labelMonitor.setToolTipText(LangModelMain.getString("menuToolsMonitor"));
		labelMonitor.setName("menuToolsMonitor");
		labelMonitor.addMouseListener(this);
		labelMonitor.setBorder(normalBorder);

		labelAnalyse.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/survey.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelAnalyse.setText("");
		labelAnalyse.setMaximumSize(buttonSize);
		labelAnalyse.setPreferredSize(buttonSize);
		labelAnalyse.setToolTipText(LangModelMain.getString("menuToolsAnalyse"));
		labelAnalyse.setName("menuToolsAnalyse");
		labelAnalyse.addMouseListener(this);
		labelAnalyse.setBorder(normalBorder);

		labelNorms.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/evaluate.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelNorms.setText("");
		labelNorms.setMaximumSize(buttonSize);
		labelNorms.setPreferredSize(buttonSize);
		labelNorms.setToolTipText(LangModelMain.getString("menuToolsNorms"));
		labelNorms.setName("menuToolsNorms");
		labelNorms.addMouseListener(this);
		labelNorms.setBorder(normalBorder);

		labelMaintain.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/maintenance.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelMaintain.setText("");
		labelMaintain.setMaximumSize(buttonSize);
		labelMaintain.setPreferredSize(buttonSize);
		labelMaintain.setToolTipText(LangModelMain.getString("menuToolsMaintain"));
		labelMaintain.setName("menuToolsMaintain");
		labelMaintain.addMouseListener(this);
		labelMaintain.setBorder(normalBorder);

		labelPrognosis.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/prognosis.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelPrognosis.setText("");
		labelPrognosis.setMaximumSize(buttonSize);
		labelPrognosis.setPreferredSize(buttonSize);
		labelPrognosis.setToolTipText(LangModelMain.getString("menuToolsPrognosis"));
		labelPrognosis.setName("menuToolsPrognosis");
		labelPrognosis.addMouseListener(this);
		labelPrognosis.setBorder(normalBorder);

		labelReportBuilder.setIcon(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/report.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		labelReportBuilder.setText("");
		labelReportBuilder.setMaximumSize(buttonSize);
		labelReportBuilder.setPreferredSize(buttonSize);
		labelReportBuilder.setToolTipText(LangModelMain.getString("menuToolsReportBuilder"));
		labelReportBuilder.setName("menuToolsReportBuilder");
		labelReportBuilder.addMouseListener(this);
		labelReportBuilder.setBorder(normalBorder);

		addComponents();
	}

	public void mouseEntered(MouseEvent e)
	{
		if(!e.getComponent().isEnabled())
			return;
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		((JComponent)e.getComponent()).setBorder(highlightBorder);
	}

	public void mouseExited(MouseEvent e)
	{
		if(!e.getComponent().isEnabled())
			return;
		setCursor(Cursor.getDefaultCursor());

//		((JComponent)e.getComponent()).setBorder(null);
		((JComponent)e.getComponent()).setBorder(normalBorder);
	}

	public void mousePressed(MouseEvent e)
	{
		if(!e.getComponent().isEnabled())
			return;
		setCursor(Cursor.getDefaultCursor());
		((JComponent)e.getComponent()).setBorder(
				BorderFactory.createBevelBorder(
						BevelBorder.LOWERED,
						Color.white,
						Color.white,
						new Color(142, 142, 142),
						new Color(99, 99, 99)));
	}

	public void mouseReleased(MouseEvent e)
	{
		if(!e.getComponent().isEnabled())
			return;
		setCursor(Cursor.getDefaultCursor());
		((JComponent)e.getComponent()).setBorder(
				BorderFactory.createBevelBorder(
						BevelBorder.RAISED,
						Color.white,
						Color.white,
						new Color(142, 142, 142),
						new Color(99, 99, 99)));
	}

	public void mouseClicked(MouseEvent e)
	{
		if(!e.getComponent().isEnabled())
			return;
		if(aModel == null)
			return;
		JLabel jl = (JLabel )e.getSource();
		String s = jl.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
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
		int count = e.length;
		int i;

		labelAdmin.setVisible(aModel.isVisible("menuToolsAdmin"));
		labelAdmin.setEnabled(aModel.isEnabled("menuToolsAdmin"));

		labelConfig.setVisible(aModel.isVisible("menuToolsConfig"));
		labelConfig.setEnabled(aModel.isEnabled("menuToolsConfig"));

		labelComponents.setVisible(aModel.isVisible("menuToolsComponents"));
		labelComponents.setEnabled(aModel.isEnabled("menuToolsComponents"));

		labelScheme.setVisible(aModel.isVisible("menuToolsScheme"));
		labelScheme.setEnabled(aModel.isEnabled("menuToolsScheme"));

		labelMap.setVisible(aModel.isVisible("menuToolsMap"));
		labelMap.setEnabled(aModel.isEnabled("menuToolsMap"));

		labelTrace.setVisible(aModel.isVisible("menuToolsTrace"));
		labelTrace.setEnabled(aModel.isEnabled("menuToolsTrace"));

		labelSchedule.setVisible(aModel.isVisible("menuToolsSchedule"));
		labelSchedule.setEnabled(aModel.isEnabled("menuToolsSchedule"));

		labelSurvey.setVisible(aModel.isVisible("menuToolsSurvey"));
		labelSurvey.setEnabled(aModel.isEnabled("menuToolsSurvey"));

		labelModel.setVisible(aModel.isVisible("menuToolsModel"));
		labelModel.setEnabled(aModel.isEnabled("menuToolsModel"));

		labelMonitor.setVisible(aModel.isVisible("menuToolsMonitor"));
		labelMonitor.setEnabled(aModel.isEnabled("menuToolsMonitor"));

		labelAnalyse.setVisible(aModel.isVisible("menuToolsAnalyse"));
		labelAnalyse.setEnabled(aModel.isEnabled("menuToolsAnalyse"));

		labelNorms.setVisible(aModel.isVisible("menuToolsNorms"));
		labelNorms.setEnabled(aModel.isEnabled("menuToolsNorms"));

		labelMaintain.setVisible(aModel.isVisible("menuToolsMaintain"));
		labelMaintain.setEnabled(aModel.isEnabled("menuToolsMaintain"));

		labelPrognosis.setVisible(aModel.isVisible("menuToolsPrognosis"));
		labelPrognosis.setEnabled(aModel.isEnabled("menuToolsPrognosis"));

		labelReportBuilder.setVisible(aModel.isVisible("menuToolsReportBuilder"));
		labelReportBuilder.setEnabled(aModel.isEnabled("menuToolsReportBuilder"));

		addComponents();
	}
	
	protected void addComponents()
	{
		if(aModel == null)	
			return;
		this.removeAll();
		
		int counter = -1;
		if(aModel.isVisible("menuToolsAdmin"))
			add(labelAdmin, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsConfig"))
			add(labelConfig, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsComponents"))
			add(labelComponents, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsScheme"))
			add(labelScheme, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsMap"))
			add(labelMap, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsTrace"))
			add(labelTrace, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsModel"))
			add(labelModel, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsSchedule"))
			add(labelSchedule, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsMonitor"))
			add(labelMonitor, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsNorms"))
			add(labelNorms, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsAnalyse"))
			add(labelAnalyse, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsSurvey"))
			add(labelSurvey, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsMaintain"))
			add(labelMaintain, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsPrognosis"))
			add(labelPrognosis, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
		if(aModel.isVisible("menuToolsReportBuilder"))
			add(labelReportBuilder, new XYConstraints(
				ROW_OFFSET + (++counter % COL_COUNT) * ROW_INTERVAL, 
				COL_OFFSET + (counter / COL_COUNT) * COL_INTERVAL , 
				-1, 
				-1));
	}
}


package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;



//===================================================================================
public class OptimizeMainToolBar extends JToolBar implements ApplicationModelListener, OperationListener
{
	private ApplicationModel aModel;
	private ApplicationContext aContext;
	private Dispatcher dispatcher;
	public JButton start = new JButton();
	public JButton stop  = new JButton();

	public JButton view_scheme  = new JButton();
	public JButton view_map     = new JButton();
	public JButton view_all     = new JButton();
	public JButton print        = new JButton();
  public JButton open_scheme  = new JButton();
  public JButton open_map     = new JButton();
  public JButton new_session = new JButton();

	public final static int img_sz = 16;
	public final static int btn_sz = 24;
	//-----------------------------------------------------------------------
	public OptimizeMainToolBar()
	{	super();
		try
    { jbInit();
    }
		catch (Exception e)
    {	e.printStackTrace();
    }
	}
	//-----------------------------------------------------------------------
	public void init_module(ApplicationContext aContext)
	{	this.aContext = aContext;
		aModel = aContext.getApplicationModel();
		dispatcher = aContext.getDispatcher();
		dispatcher.register(this,"mapopened");
		dispatcher.register(this,"scheme_is_opened");
		dispatcher.register(this,"startevent");
		dispatcher.register(this,"stopevent");
    dispatcher.register(this,"close_all");
	}
	//-----------------------------------------------------------------------
	private void jbInit() throws Exception
	{	OptimizeMainToolBar_this_actionAdapter actionAdapter = new OptimizeMainToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_sz, btn_sz);

		start.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/opt_start.gif")));
		start.setMaximumSize(buttonSize);
		start.setPreferredSize(buttonSize);
		start.setToolTipText(LangModelOptimize.ToolTip("menuOptimizeStart"));
		start.setName("menuOptimizeStart");
		start.addActionListener(actionAdapter);
		start.setEnabled(false);

		stop.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/opt_stop.gif")));
		stop.setMaximumSize(buttonSize);
		stop.setPreferredSize(buttonSize);
		stop.setToolTipText(LangModelOptimize.ToolTip("menuOptimizeStop"));
		stop.setName("menuOptimizeStop");
		stop.addActionListener(actionAdapter);
		stop.setEnabled(false);

		view_scheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")));
		view_scheme.setMaximumSize(buttonSize);
		view_scheme.setPreferredSize(buttonSize);
		view_scheme.setToolTipText(LangModelOptimize.ToolTip("menuViewScheme"));
		view_scheme.setName("menuViewScheme");
		view_scheme.addActionListener(actionAdapter);
		view_scheme.setEnabled(false);

		view_map.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/map.gif")));
		view_map.setMaximumSize(buttonSize);
		view_map.setPreferredSize(buttonSize);
		view_map.setToolTipText(LangModelOptimize.ToolTip("menuViewMap"));
		view_map.setName("menuViewMap");
		view_map.addActionListener(actionAdapter);
		view_map.setEnabled(false);

		view_all.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/opt_view_all.gif")));
		view_all.setMaximumSize(buttonSize);
		view_all.setPreferredSize(buttonSize);
		view_all.setToolTipText(LangModelOptimize.ToolTip("menuViewShowall"));
		view_all.setName("menuViewShowall");
		view_all.addActionListener(actionAdapter);
		view_all.setEnabled(false);

//		print.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/opt_print.gif")));
//		print.setMaximumSize(buttonSize);
//		print.setPreferredSize(buttonSize);
//		print.setToolTipText("печать");//print.setToolTipText(LangModelOptimize.ToolTip("menuViewShowall"));
//		print.setName("menuServicePrintScheme");
//		print.addActionListener(actionAdapter);
//		print.setEnabled(false);

    open_scheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif")
                                      .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));//getImage("images/main/opt_open_scheme.gif")));
    open_scheme.setMaximumSize(buttonSize);
    open_scheme.setPreferredSize(buttonSize);
    open_scheme.setToolTipText(LangModelOptimize.ToolTip("menuSchemeOpen"));
    open_scheme.setName("menuSchemeOpen");
    open_scheme.addActionListener(actionAdapter);
    open_scheme.setEnabled(false);

    open_map.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")
                                   .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));//getImage("images/main/opt_open_map.gif")));
    open_map.setMaximumSize(buttonSize);
    open_map.setPreferredSize(buttonSize);
    open_map.setToolTipText(LangModelOptimize.ToolTip("menuMapOpen"));
    open_map.setName("menuMapOpen");
    open_map.addActionListener(actionAdapter);
    open_map.setEnabled(false);

    new_session.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif")
                                      .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));//getImage("images/main/opt_new_session.gif")));
    new_session.setMaximumSize(buttonSize);
    new_session.setPreferredSize(buttonSize);
    new_session.setToolTipText(LangModelOptimize.ToolTip("menuSessionNew"));
    new_session.setName("menuSessionNew");
    new_session.addActionListener(actionAdapter);
    new_session.setEnabled(true);

    add(new_session);
    addSeparator();//addSeparator(new Dimension(btn_sz,btn_sz));
    add(open_map);
    add(open_scheme);
    addSeparator();
    add(start);
		add(stop);
		addSeparator();
		add(view_scheme);
		add(view_map);
		//add(view_all);

    //add(print); печать не доделана и не работает
	}
	//-----------------------------------------------------------------------
	public void setModel(ApplicationModel a){	aModel = a; }
	//-----------------------------------------------------------------------
	public ApplicationModel getModel(){	return aModel; }
	//-----------------------------------------------------------------------
	public void modelChanged(String e[])
	{
//    // buttonFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
//    // buttonFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));
//		start.setVisible(aModel.isVisible("menuOptimizeStart"));
//		start.setEnabled(aModel.isEnabled("menuOptimizeStart"));
//		stop.setVisible(aModel.isVisible("menuOptimizeStop"));
//		stop.setEnabled(aModel.isEnabled("menuOptimizeStop"));
//		view_scheme.setVisible(aModel.isVisible("menuViewScheme)"));
//		view_scheme.setEnabled(aModel.isEnabled("menuViewScheme)"));
//		view_map.setVisible(aModel.isVisible("menuViewMap)"));
//		view_map.setEnabled(aModel.isEnabled("menuViewMap)"));
//		view_all.setVisible(aModel.isVisible("menuViewShowAll)"));
//		view_all.setEnabled(aModel.isEnabled("menuViewShowAll)"));
//    open_scheme.setVisible(aModel.isVisible("menuOpenScheme)"));
//    open_scheme.setEnabled(aModel.isEnabled("menuOpenScheme)"));
//		// кнoпка print пока не дублируетс в меню
	}
	//-----------------------------------------------------------------------
	public void this_actionPerformed(ActionEvent e)
	{	if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton)e.getSource();
		String s = jb.getName();

		// кнoпка print пока не дублируется в меню, поэтому обрабатывается несколько иначе
		if(s == "menuServicePrintScheme")
		{ dispatcher.notify(new OperationEvent(this, 0, "print_scheme"));
		}
		else
		{	Command command = aModel.getCommand(s);
			command = (Command)command.clone();
			command.execute();
		}
	}
	//-----------------------------------------------------------------------
	public void operationPerformed(OperationEvent ae)
	{	if(aModel == null)
    {
	return;
    }
    if(ae.getActionCommand().equals("close_all"))// команда "закрыть всё"
    { start.setEnabled(false);
      stop.setEnabled(false);
      view_scheme.setEnabled(false);
      view_map.setEnabled(false);
      view_all.setEnabled(false);
      print.setEnabled(false);
    }
		if(ae.getActionCommand().equals("mapopened"))// событие: открыта новая карта
		{ start.setEnabled(true);
			view_scheme.setEnabled(true);
			view_map.setEnabled(true);
			view_all.setEnabled(true);
			print.setEnabled(true);
		}
		if(ae.getActionCommand().equals("scheme_is_opened"))// событие: открыта новая cхема
		{ start.setEnabled(true);
			view_scheme.setEnabled(true);
			view_all.setEnabled(true);
			//print.setEnabled(true);
		}
		if(ae.getActionCommand().equals("startevent"))// событие: начать(продолжить) оптмизацию
		{ start.setEnabled(false);
			stop.setEnabled(true);
      open_scheme.setEnabled(false);// если начали оптимизировать, то больше схему открывать нельзя
		}
		if(ae.getActionCommand().equals("stopevent"))// событие: приостановить оптимизацию
		{ start.setEnabled(true);
			stop.setEnabled(false);
		}
	}
	//-----------------------------------------------------------------------
}
//===================================================================================
class OptimizeMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{ OptimizeMainToolBar adaptee;
	//-----------------------------------------------------------------------
	OptimizeMainToolBar_this_actionAdapter(OptimizeMainToolBar adaptee)
	{	this.adaptee = adaptee;
	}
	//-----------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{	System.out.println("AnalyseToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}
//===================================================================================

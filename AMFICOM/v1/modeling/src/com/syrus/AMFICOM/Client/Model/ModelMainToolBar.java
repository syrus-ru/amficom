package com.syrus.AMFICOM.Client.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ModelMainToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
//  JButton sessionClose= new JButton();

	JButton menuViewMapViewOpen = new JButton();
	JButton menuViewSchemeOpen = new JButton();
	JButton menuViewModelLoad = new JButton();

	JButton traceAddCompare = new JButton();
	JButton traceRemoveCompare = new JButton();
	JButton buttonFileOpen = new JButton();
	JButton fileAdd = new JButton();
	JButton fileRemove = new JButton();
	JButton saveModel = new JButton();
	JButton performModeling = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ModelMainToolBar()
	{

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
		ModelMainToolBar_this_actionAdapter actionAdapter =
				new ModelMainToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		this.sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.sessionOpen.setMaximumSize(buttonSize);
		this.sessionOpen.setPreferredSize(buttonSize);
		this.sessionOpen.setToolTipText(LangModelModel.getString("menuSessionOpen"));
		this.sessionOpen.setName("menuSessionOpen");
		this.sessionOpen.addActionListener(actionAdapter);

//    this.sessionClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session2.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
//    this.sessionClose.setMaximumSize(buttonSize);
//    this.sessionClose.setPreferredSize(buttonSize);
//    this.sessionClose.setToolTipText(LangModelModel.getString("menuSessionClose"));
//    this.sessionClose.setName("menuSessionClose");
//    this.sessionClose.addActionListener(actionAdapter);

		this.menuViewMapViewOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")
				.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.menuViewMapViewOpen.setMaximumSize(buttonSize);
		this.menuViewMapViewOpen.setPreferredSize(buttonSize);
		this.menuViewMapViewOpen.setToolTipText(LangModelModel.getString("menuViewMapViewOpen"));
		this.menuViewMapViewOpen.setName("menuViewMapViewOpen");
		this.menuViewMapViewOpen.addActionListener(actionAdapter);

		this.menuViewModelLoad.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_model.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.menuViewModelLoad.setMaximumSize(buttonSize);
		this.menuViewModelLoad.setPreferredSize(buttonSize);
		this.menuViewModelLoad.setToolTipText(LangModelModel.getString("menuViewModelLoad"));
		this.menuViewModelLoad.setName("menuViewModelLoad");
		this.menuViewModelLoad.addActionListener(actionAdapter);

		this.menuViewSchemeOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.menuViewSchemeOpen.setMaximumSize(buttonSize);
		this.menuViewSchemeOpen.setPreferredSize(buttonSize);
		this.menuViewSchemeOpen.setToolTipText(LangModelModel.getString("menuViewSchemeOpen"));
		this.menuViewSchemeOpen.setName("menuViewSchemeOpen");
		this.menuViewSchemeOpen.addActionListener(actionAdapter);

		this.traceAddCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_add.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.traceAddCompare.setMaximumSize(buttonSize);
		this.traceAddCompare.setPreferredSize(buttonSize);
		this.traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		this.traceAddCompare.setName("menuTraceAddCompare");
		this.traceAddCompare.addActionListener(actionAdapter);

		this.traceRemoveCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_remove.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.traceRemoveCompare.setMaximumSize(buttonSize);
		this.traceRemoveCompare.setPreferredSize(buttonSize);
		this.traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		this.traceRemoveCompare.setName("menuTraceRemoveCompare");
		this.traceRemoveCompare.addActionListener(actionAdapter);

		this.buttonFileOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.buttonFileOpen.setMaximumSize(buttonSize);
		this.buttonFileOpen.setPreferredSize(buttonSize);
		this.buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		this.buttonFileOpen.setName("menuFileOpen");
		this.buttonFileOpen.addActionListener(actionAdapter);

		this.fileAdd.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.fileAdd.setMaximumSize(buttonSize);
		this.fileAdd.setPreferredSize(buttonSize);
		this.fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		this.fileAdd.setName("menuFileAddCompare");
		this.fileAdd.addActionListener(actionAdapter);

		this.fileRemove.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.fileRemove.setMaximumSize(buttonSize);
		this.fileRemove.setPreferredSize(buttonSize);
		this.fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		this.fileRemove.setName("menuFileRemoveCompare");
		this.fileRemove.addActionListener(actionAdapter);

		this.saveModel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.saveModel.setMaximumSize(buttonSize);
		this.saveModel.setPreferredSize(buttonSize);
		this.saveModel.setToolTipText(LangModelModel.getString("menuViewModelSave"));
		this.saveModel.setName("menuViewModelSave");
		this.saveModel.addActionListener(actionAdapter);

		this.performModeling.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.performModeling.setMaximumSize(buttonSize);
		this.performModeling.setPreferredSize(buttonSize);
		this.performModeling.setToolTipText(LangModelModel.getString("menuViewPerformModeling"));
		this.performModeling.setName("menuViewPerformModeling");
		this.performModeling.addActionListener(actionAdapter);

		add(this.sessionOpen);
//    add(this.sessionClose);
		addSeparator();
		add(this.menuViewMapViewOpen);
		add(this.menuViewSchemeOpen);
		addSeparator();
		add(this.buttonFileOpen);
		add(this.fileAdd);
		add(this.fileRemove);
		addSeparator();
		add(this.menuViewModelLoad);
		add(this.traceAddCompare);
		add(this.traceRemoveCompare);
		addSeparator();
		add(this.performModeling);
		add(this.saveModel);

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
		this.sessionOpen.setVisible(this.aModel.isVisible("menuSessionOpen"));
		this.sessionOpen.setEnabled(this.aModel.isEnabled("menuSessionOpen"));
//    this.sessionClose.setVisible(this.aModel.isVisible("menuSessionClose"));
//    this.sessionClose.setEnabled(this.aModel.isEnabled("menuSessionClose"));
		this.menuViewMapViewOpen.setVisible(this.aModel.isVisible("menuViewMapViewOpen"));
		this.menuViewMapViewOpen.setEnabled(this.aModel.isEnabled("menuViewMapViewOpen"));
		this.menuViewSchemeOpen.setVisible(this.aModel.isVisible("menuViewSchemeOpen"));
		this.menuViewSchemeOpen.setEnabled(this.aModel.isEnabled("menuViewSchemeOpen"));
		this.menuViewModelLoad.setVisible(this.aModel.isVisible("menuViewModelLoad"));
		this.menuViewModelLoad.setEnabled(this.aModel.isEnabled("menuViewModelLoad"));
		this.traceAddCompare.setVisible(this.aModel.isVisible("menuTraceAddCompare"));
		this.traceAddCompare.setEnabled(this.aModel.isEnabled("menuTraceAddCompare"));
		this.traceRemoveCompare.setVisible(this.aModel.isVisible("menuTraceRemoveCompare"));
		this.traceRemoveCompare.setEnabled(this.aModel.isEnabled("menuTraceRemoveCompare"));
		this.buttonFileOpen.setVisible(this.aModel.isVisible("menuFileOpen"));
		this.buttonFileOpen.setEnabled(this.aModel.isEnabled("menuFileOpen"));
		this.fileAdd.setEnabled(this.aModel.isEnabled("menuFileAddCompare"));
		this.fileAdd.setVisible(this.aModel.isVisible("menuFileAddCompare"));
		this.fileRemove.setEnabled(this.aModel.isEnabled("menuFileRemoveCompare"));
		this.fileRemove.setVisible(this.aModel.isVisible("menuFileRemoveCompare"));
		this.saveModel.setVisible(this.aModel.isVisible("menuViewModelSave"));
		this.saveModel.setEnabled(this.aModel.isEnabled("menuViewModelSave"));
		this.performModeling.setVisible(this.aModel.isVisible("menuViewPerformModeling"));
		this.performModeling.setEnabled(this.aModel.isEnabled("menuViewPerformModeling"));
	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}
}

class ModelMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ModelMainToolBar adaptee;

	ModelMainToolBar_this_actionAdapter(ModelMainToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		this.adaptee.this_actionPerformed(e);
	}
}

package com.syrus.AMFICOM.Client.ReportBuilder;


import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
/**
 * <p>Title: </p>
 * <p>Description: Выбор схемы, а также топологии и решения для неё.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class SelectSolutionFrame extends JInternalFrame
{
	static public Scheme selectedScheme = null;
	static public MapContext selectedMap = null;
	static public SolutionCompact selectedSolution = null;

	private ApplicationContext aContext = null;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel schemePanel = new JPanel();
	JPanel otherParamsPanel = new JPanel();
	Border border2;
	JPanel buttonPanel = new JPanel();
	JButton applyButton = new JButton();
	JLabel schemeLabel = new JLabel();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JScrollPane schemeListScrollPane = new JScrollPane();
	ObjectResourceListBox schemeList = new ObjectResourceListBox();
	JLabel topologyLabel = new JLabel();
	JScrollPane topologyListScrollPane = new JScrollPane();
	GridBagLayout gridBagLayout3 = new GridBagLayout();
	ObjectResourceListBox topologyList = new ObjectResourceListBox();
	JLabel solutionLabel = new JLabel();
	JScrollPane solutionListScrollPane = new JScrollPane();
	ObjectResourceListBox solutionList = new ObjectResourceListBox();


	public SelectSolutionFrame(ApplicationContext aC)
	{
		aContext = aC;
		try
		{
			jbInit();
			setSchemeListData();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception
	{
		border2 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
		this.setTitle(LangModelReport.String("label_chooseParams"));
		this.getContentPane().setLayout(gridBagLayout1);
		schemePanel.setLayout(gridBagLayout2);
		otherParamsPanel.setBorder(border2);
		otherParamsPanel.setLayout(gridBagLayout3);
		applyButton.setSelectedIcon(null);
		applyButton.setText(LangModelReport.String("label_apply"));
		applyButton.addActionListener(new SelectSolutionFrame_applyButton_actionAdapter(this));
		schemeLabel.setText(LangModelReport.String("label_scheme"));
		topologyLabel.setText(LangModelReport.String("label_topology"));
		solutionLabel.setText(LangModelReport.String("label_solution"));
		schemeList.addListSelectionListener(new SelectSolutionFrame_schemeList_listSelectionAdapter(this));
		this.getContentPane().add(schemePanel,         new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 0, 0), 0, 0));
		schemePanel.add(schemeLabel,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		schemePanel.add(schemeListScrollPane,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 4), 0, 0));
		schemeListScrollPane.getViewport().add(schemeList, null);
		this.getContentPane().add(otherParamsPanel,         new GridBagConstraints(0, 1, 1, 1, 1.0, 2.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 2, 0, 2), 0, 0));
		otherParamsPanel.add(topologyLabel,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
		otherParamsPanel.add(topologyListScrollPane,         new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));
		otherParamsPanel.add(solutionLabel,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
		otherParamsPanel.add(solutionListScrollPane,      new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		solutionListScrollPane.getViewport().add(solutionList, null);
		topologyListScrollPane.getViewport().add(topologyList, null);
		this.getContentPane().add(buttonPanel,     new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		buttonPanel.add(applyButton, null);

		this.setVisible(true);
		this.setEnabled(true);
		this.setSize(400,400);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				 "images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.setResizable(true);
		this.setClosable(true);
	}

	private void setSchemeListData()
	{
		System.out.println(new Date(System.currentTimeMillis()).toString() +
								 " " + "Getting data from server...");
		new SchemeDataSourceImage(aContext.getDataSourceInterface()).
			LoadSchemes();
		aContext.getDataSourceInterface().LoadSchemeMonitoringSolutions();
		new MapDataSourceImage(aContext.getDataSourceInterface()).LoadMaps();

		System.out.println(new Date(System.currentTimeMillis()).toString() +
								 " " + "...done!");

		Hashtable selectValuesHash = Pool.getHash(Scheme.typ);

		if (selectValuesHash == null)
			dispose();

		Enumeration svEnum = selectValuesHash.elements();

		while (svEnum.hasMoreElements())
		{
			Scheme curScheme = (Scheme) svEnum.nextElement();
			schemeList.add(curScheme);
		}

		try
		{
			if (selectedScheme != null)
			{
				for (int i = 0; i < schemeList.getModel().getSize(); i++)
				{
					String cur_id =
									 ((Scheme)schemeList.getModel().getElementAt(i)).id;
					if (cur_id.equals(selectedScheme.id))
					{
						schemeList.setSelectedIndex(i);
						schemeList.scrollRectToVisible(schemeList.getCellBounds(i,i));
						break;
					}
				}

				schemeList_valueChanged();

				if (selectedMap != null)
				{
					for (int i = 0; i < topologyList.getModel().getSize(); i++)
					{
						String cur_id =
											((MapContext)topologyList.getModel().getElementAt(i)).id;

						if (cur_id.equals(selectedMap.id))
						{
							topologyList.setSelectedIndex(i);
							topologyList.scrollRectToVisible(topologyList.getCellBounds(i,i));
							break;
						}
					}
				}

				if (selectedSolution != null)
				{
					for (int i = 0; i < solutionList.getModel().getSize(); i++)
					{
						String cur_id =
							((SolutionCompact) solutionList.getModel().getElementAt(i)).
							id;

						if (cur_id.equals(selectedSolution.id))
						{
							solutionList.setSelectedIndex(i);
							solutionList.scrollRectToVisible(solutionList.getCellBounds(i,i));
							break;
						}
					}
				}
			}
		}
		catch (Exception exc)
		{
			System.out.println("Can't set current template settings in list!");
		}
	}

	void schemeList_valueChanged()
	{

		topologyList.removeAll();
		solutionList.removeAll();

		Scheme selScheme = (Scheme) schemeList.getSelectedObjectResource();

		//Загружаем в список доступные для схемы топологии

		Hashtable selectValuesHash = Pool.getHash(MapContext.typ);

		if (selectValuesHash != null)
		{
			Enumeration svEnum = selectValuesHash.elements();
			while (svEnum.hasMoreElements())
			{
				MapContext curMap = (MapContext) svEnum.nextElement();
				if (curMap.scheme_id.equals(selScheme.id))
					topologyList.add(curMap);
			}
		}
		else
		{
			topologyList.add(
						new JLabel(LangModelReport.String("label_noTopologiesForScheme")));
			topologyList.setEnabled(false);
			selectedMap = null;
		}

		//Загружаем в список доступные для схемы решения по оптимизации

		selectValuesHash = Pool.getHash(SolutionCompact.typ);
		if (selectValuesHash != null)
		{
			Enumeration svEnum = selectValuesHash.elements();
			Vector infoNames = new Vector();
			while (svEnum.hasMoreElements())
			{
				SolutionCompact curSC = (SolutionCompact) svEnum.
												nextElement();
				if (curSC.scheme_id.equals(selScheme.id))
					solutionList.add(curSC);
			}
		}
		else
		{
			solutionList.add(
						new JLabel(LangModelReport.String("label_noSolutions")));
			solutionList.setEnabled(false);
			selectedSolution = null;
		}
	}

	void applyButton_actionPerformed(ActionEvent e)
	{
		if (schemeList.getSelectedIndex() != -1)
			selectedScheme = (Scheme) schemeList.getSelectedObjectResource();
		else
			selectedScheme = null;

		if (topologyList.getSelectedIndex() != -1)
			selectedMap = (MapContext) topologyList.getSelectedObjectResource();
		else
			selectedMap = null;

		if (solutionList.getSelectedIndex() != -1)
			selectedSolution = (SolutionCompact) solutionList.getSelectedObjectResource();
		else
			selectedSolution = null;
	}
}

class SelectSolutionFrame_schemeList_listSelectionAdapter implements javax.swing.event.ListSelectionListener
{
	SelectSolutionFrame adaptee;

	SelectSolutionFrame_schemeList_listSelectionAdapter(SelectSolutionFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;

		adaptee.schemeList_valueChanged();
	}
}

class SelectSolutionFrame_applyButton_actionAdapter implements java.awt.event.ActionListener
{
	SelectSolutionFrame adaptee;

	SelectSolutionFrame_applyButton_actionAdapter(SelectSolutionFrame adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.applyButton_actionPerformed(e);
	}
}
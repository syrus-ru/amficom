// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.syrus.AMFICOM.Client.Map.Setup;

import com.ofx.base.*;
import com.ofx.component.*;
import com.ofx.component.swing.*;
import com.ofx.mapViewer.*;
import com.ofx.repository.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayerConfig extends JMapLegend
{
	private boolean stateChangeInProgress;

	public LayerConfig()
	{
		super();
		stateChangeInProgress = false;
		showLabelCheckbox = false;
	}

	public void setMapViewer(MapViewer mapviewer)
	{
		super.setMapViewer(mapviewer);
		titleLabel = new JLabel("Слои");
		titleLabel.setFont(getLegendFont());
	}
	
	public java.lang.String getTitle()
	{
		return "Слои";
	}

	public void stateChanged(SxEvent sxevent)
	{
		Vector vector;
		int k;
		int l;
		String s;
		Vector vector1;
		int i1;
		String s1;
		LayerConfig.MapLegendRow maplegendrow;

		int i = sxevent.getState();
		int j = sxevent.getContext();
		if(stateChangeInProgress)
			return;
		stateChangeInProgress = true;
		try
		{
			if(i == 104 && (j == 202 || j == 218 || j == 226 || j == 227 || j == 201))
			{
				tableData.removeAllElements();
				
				vector = getSxMapViewer().getForegroundClasses(sortOrder);
				k = vector.size();
				for(l = 0; l < k; l++)
				{
					s = (String )vector.elementAt(l);
					maplegendrow = new LayerConfig.MapLegendRow(s);
					tableData.addElement(maplegendrow);
					vector1 = getSxMapViewer().classBinNames(s);
					i1 = vector1.size();
					for(int j1 = 0; j1 < i1; j1++)
					{
						s1 = (String )vector1.elementAt(j1);
						tableData.addElement(new LayerConfig.MapLegendRow(s, s1));
					}
				}

				vector = getSxMapViewer().getBackgroundClasses();
				k = vector.size();
				for(l = 0; l < k; l++)
				{
					s = (String )vector.elementAt(l);
					maplegendrow = new LayerConfig.MapLegendRow(s);
					tableData.addElement(maplegendrow);
					vector1 = getSxMapViewer().classBinNames(s);
					i1 = vector1.size();
					for(int j1 = 0; j1 < i1; j1++)
					{
						s1 = (String )vector1.elementAt(j1);
						tableData.addElement(new LayerConfig.MapLegendRow(s, s1));
					}
				}

				layoutPanel();
				scrollPane.validate();
			}
		}
		finally
		{
			stateChangeInProgress = false;
		}
	}

	protected JPanel layoutPanel()
	{
		if(panel == null)
		{
			panel = new JPanel();
			gbl = new GridBagLayout();
			panel.setLayout(gbl);
		} else
		{
			panel.removeAll();
		}
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		int i1;
		int k = i1 = 0;
		Component component = Box.createHorizontalStrut(5);
		gridbagconstraints.gridx = k++;
		gridbagconstraints.gridy = i1;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.fill = 0;
		gbl.setConstraints(component, gridbagconstraints);
		panel.add(component);
		if(showSymbolCheckbox)
		{
			if(symbolIcon == null)
				symbolIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/seesymbs.gif"));
			JLabel jlabel = new JLabel(symbolIcon);
			gridbagconstraints.gridx = k++;
			gridbagconstraints.gridy = i1;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.fill = 0;
			gbl.setConstraints(jlabel, gridbagconstraints);
			panel.add(jlabel);
		}
		if(showLabelCheckbox)
		{
			if(labelIcon == null)
				labelIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/showlabl.gif"));
			javax.swing.JLabel jlabel1 = new JLabel(labelIcon);
			gridbagconstraints.gridx = k++;
			gridbagconstraints.gridy = i1;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.fill = 0;
			gbl.setConstraints(jlabel1, gridbagconstraints);
			panel.add(jlabel1);
		}
		javax.swing.JLabel jlabel2 = new JLabel("Слои");
		gridbagconstraints.gridx = ++k;
		gridbagconstraints.gridy = i1;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.fill = 2;
		gbl.setConstraints(jlabel2, gridbagconstraints);
		panel.add(jlabel2);
		i1++;
		k = 0;
		component = javax.swing.Box.createHorizontalStrut(5);
		gridbagconstraints.gridx = k++;
		gridbagconstraints.gridy = i1;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.fill = 0;
		gbl.setConstraints(component, gridbagconstraints);
		panel.add(component);
		JSeparator jseparator = new JSeparator();
		gridbagconstraints.gridx = k++;
		gridbagconstraints.gridy = i1;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.gridwidth = 4;
		gridbagconstraints.fill = 2;
		gbl.setConstraints(jseparator, gridbagconstraints);
		panel.add(jseparator);
		int j = tableData.size();
		i1++;
		for(int i = 0; i < j;)
		{
			LayerConfig.MapLegendRow maplegendrow = (MapLegendRow)tableData.elementAt(i);
			int l = 0;
			Component component1 = Box.createHorizontalStrut(5);
			gridbagconstraints.gridx = l++;
			gridbagconstraints.gridy = i1;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.fill = 0;
			gbl.setConstraints(component1, gridbagconstraints);
			panel.add(component1);
			if(maplegendrow.type == 0)
			{
				if(showSymbolCheckbox)
				{
					JCheckBox jcheckbox = new JCheckBox();
					jcheckbox.setSelected(maplegendrow.classVis);
					jcheckbox.addActionListener(new LayerConfig.ClassVisAction(maplegendrow, jcheckbox));
					jcheckbox.setBackground(panel.getBackground());
					jcheckbox.setAlignmentY(0.5F);
					jcheckbox.setAlignmentX(0.8F);
					gridbagconstraints.gridx = l++;
					gridbagconstraints.gridy = i1;
					gridbagconstraints.weightx = 0.0D;
					gridbagconstraints.gridwidth = 1;
					gridbagconstraints.fill = 0;
					gbl.setConstraints(jcheckbox, gridbagconstraints);
					panel.add(jcheckbox);
				}
				if(showLabelCheckbox)
				{
					JCheckBox jcheckbox1 = new JCheckBox();
					jcheckbox1.setSelected(maplegendrow.labelsVis);
					jcheckbox1.addActionListener(new LayerConfig.LabelsVisAction(maplegendrow, jcheckbox1));
					jcheckbox1.setBackground(panel.getBackground());
					jcheckbox1.setAlignmentY(0.5F);
					jcheckbox1.setAlignmentX(0.8F);
					gridbagconstraints.gridx = l++;
					gridbagconstraints.gridy = i1;
					gridbagconstraints.weightx = 0.0D;
					gridbagconstraints.gridwidth = 1;
					gridbagconstraints.fill = 0;
					gbl.setConstraints(jcheckbox1, gridbagconstraints);
					panel.add(jcheckbox1);
				}
			} else
			{
				if(showSymbolCheckbox)
					l++;
				if(showLabelCheckbox)
					l++;
			}
			SxSymbolCanvas sxsymbolcanvas = new SxSymbolCanvas(maplegendrow.getSymbology());
			sxsymbolcanvas.setRenderer(maplegendrow.getRenderer());
			sxsymbolcanvas.setBackground(panel.getBackground());
			sxsymbolcanvas.setDimension(maplegendrow.getDimension());
			gridbagconstraints.gridx = l++;
			gridbagconstraints.gridy = i1;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.fill = 0;
			gbl.setConstraints(sxsymbolcanvas, gridbagconstraints);
			panel.add(sxsymbolcanvas);
			jlabel2 = new JLabel(" " + maplegendrow.name);
			gridbagconstraints.gridx = l++;
			gridbagconstraints.gridy = i1;
			gridbagconstraints.fill = 2;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.gridwidth = 1;
			gbl.setConstraints(jlabel2, gridbagconstraints);
			panel.add(jlabel2);
			i++;
			i1++;
		}

		jlabel2 = new JLabel(" ");
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = i1 + 1;
		gridbagconstraints.fill = 3;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.gridwidth = 4;
		gbl.setConstraints(jlabel2, gridbagconstraints);
		panel.add(jlabel2);
		return panel;
	}

	private static SxEnvironment env()
	{
		return SxEnvironment.singleton();
	}

	protected static void log(Exception exception)
	{
		com.syrus.AMFICOM.Client.Map.Setup.LayerConfig.env();
		SxEnvironment.log().println(exception);
	}

	protected class MapLegendRow extends SxSymbolCanvas
	{

		public int getDimension()
		{
			if(sc != null)
				return sc.getDimension();
			else
				return 0;
		}

		public SxSymbology getSymbology()
		{
			try
			{
				if(sym == null)
					if(type == 0)
						sym = sc.getSymbology();
					else
					if(type == 1)
						sym = bin.getSymbology(className);
				return sym;
			}
			catch(java.lang.Exception exception)
			{
				com.syrus.AMFICOM.Client.Map.Setup.LayerConfig.log(exception);
			}
			return null;
		}

		public SxRendererInterface getRenderer()
		{
			try
			{
				if(ri == null)
					if(type == 0)
						ri = sc.getRenderer();
					else
					if(type == 1)
						ri = bin.getRenderer(className);
				return ri;
			}
			catch(java.lang.Exception exception)
			{
				com.syrus.AMFICOM.Client.Map.Setup.LayerConfig.log(exception);
			}
			return null;
		}

		public boolean getSymbolIsVisible()
		{
			SxMapLayerInterface sxmaplayerinterface = getSxMapViewer().getNamedLayer(className);
			if(sxmaplayerinterface != null)
				return sxmaplayerinterface.isEnabled();
			else
				return false;
		}

		public void setSymbolIsVisible(boolean flag)
		{
			SxMapLayerInterface sxmaplayerinterface = getSxMapViewer().getNamedLayer(className);
			if(sxmaplayerinterface != null)
				sxmaplayerinterface.setEnabled(flag);
		}

		public boolean getTextIsVisible()
		{
			SxMapLayerInterface sxmaplayerinterface = getSxMapViewer().getNamedLayer(className + "LABELS");
			if(sxmaplayerinterface != null)
				return sxmaplayerinterface.isEnabled();
			else
				return false;
		}

		public void setTextIsVisible(boolean flag)
		{
			SxMapLayerInterface sxmaplayerinterface = getSxMapViewer().getNamedLayer(className + "LABELS");
			if(sxmaplayerinterface != null)
				sxmaplayerinterface.setEnabled(flag);
		}

		String className;
		String binName;
		String name;
		boolean classVis;
		boolean labelsVis;
		int type;
		SxClass sc;
		SxHighlightBin bin;
		SxSymbology sym;
		SxRendererInterface ri;

		MapLegendRow(String s)
		{
			className = name = s;
			type = 0;
			try
			{
				sc = SxClass.retrieve(s, getSxMapViewer().getQuery());
			}
			catch(Exception exception)
			{
				com.syrus.AMFICOM.Client.Map.Setup.LayerConfig.log(exception);
			}
			classVis = getSymbolIsVisible();
			labelsVis = getTextIsVisible();
		}

		MapLegendRow(String s, String s1)
		{
			className = s;
			binName = name = s1;
			type = 1;
			try
			{
				sc = SxClass.retrieve(s, getSxMapViewer().getQuery());
				bin = getSxMapViewer().getBin(s1);
			}
			catch(java.lang.Exception exception)
			{
				com.syrus.AMFICOM.Client.Map.Setup.LayerConfig.log(exception);
			}
		}
	}

	protected class LabelsVisAction
		implements ActionListener
	{

		public void actionPerformed(ActionEvent actionevent)
		{
			mlr.setTextIsVisible(cbox.isSelected());
			notifyListeners();
		}

		LayerConfig.MapLegendRow mlr;
		JCheckBox cbox;

		LabelsVisAction(LayerConfig.MapLegendRow maplegendrow, JCheckBox jcheckbox)
		{
			mlr = maplegendrow;
			cbox = jcheckbox;
		}
	}

	protected class ClassVisAction
		implements ActionListener
	{

		public void actionPerformed(ActionEvent actionevent)
		{
			mlr.setSymbolIsVisible(cbox.isSelected());
			notifyListeners();
		}

		LayerConfig.MapLegendRow mlr;
		JCheckBox cbox;

		ClassVisAction(LayerConfig.MapLegendRow maplegendrow, JCheckBox jcheckbox)
		{
			mlr = maplegendrow;
			cbox = jcheckbox;
		}
	}
}

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Toolkit;
import java.awt.event.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class HistogrammLayeredPanel extends ScalableLayeredPanel implements OperationListener
{
	Dispatcher dispatcher;
	private boolean useMarkers = false;

	public HistogrammLayeredPanel(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	private void jbInit() throws Exception
	{
	}

	protected ToolBarPanel createToolBar()
	{
		return new HistogrammToolBar(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if (useMarkers)
			{
				for(int i=0; i<jLayeredPane.getComponentCount(); i++)
				{
					SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
					if (panel instanceof HistogrammPanel)
					{
						if(rue.MARKER_MOVED)
						{
							MarkersInfo mInfo = (MarkersInfo)rue.getSource();
							((HistogrammPanel)panel).updateHistogrammData(
									Math.min(mInfo.a_pos, mInfo.b_pos),
									Math.max(mInfo.a_pos, mInfo.b_pos));
							jLayeredPane.repaint();
						}
					}
				}
			}
		}
	}

	public void useMarkers(boolean b)
	{
		useMarkers = b;
	}
}

class HistogrammToolBar extends ScalableToolBar
{
	protected static final String bindMark = "bind2markers";

	JToggleButton markersTButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit, separator, bindMark
	};

	public HistogrammToolBar (HistogrammLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Hashtable createGraphButtons()
	{
		Hashtable buttons = new Hashtable();

		buttons.put(
				bindMark,
				createToolButton(
				markersTButton,
				btn_size,
				null,
				LangModelAnalyse.String("bindToMarker"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/marker.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						markersTButton_actionPerformed(e);
					}
				},
				true));

		buttons.putAll(super.createGraphButtons());
		return buttons;
	}

	void markersTButton_actionPerformed(ActionEvent e)
	{
		HistogrammLayeredPanel panel = (HistogrammLayeredPanel)super.panel;
		boolean b = markersTButton.isSelected();
		panel.useMarkers(b);
	}
}

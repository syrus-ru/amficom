package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class HistogrammLayeredPanel extends ScalableLayeredPanel implements PropertyChangeListener
{
	Dispatcher dispatcher;
	private boolean useMarkers = false;
	MarkersInfo mInfo;

	public HistogrammLayeredPanel(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		dispatcher1.addPropertyChangeListener(RefUpdateEvent.typ, this);
	}

	private void jbInit() throws Exception
	{ // empty
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new HistogrammToolBar(this);
	}

	public void propertyChange(PropertyChangeEvent ae)
	{
		if(ae.getPropertyName().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if(rue.markerMoved()) {
				this.mInfo = (MarkersInfo)rue.getNewValue();
			}

			if(rue.markerLocated())
			{
				if (this.useMarkers)
				{
					updateHistogrammData();
				}
			}
		}
	}
	
	protected void updateHistogrammData() {
		if (this.mInfo != null) {
			for (int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
				SimpleGraphPanel panel = (SimpleGraphPanel) this.jLayeredPane.getComponent(i);
				if (panel instanceof HistogrammPanel) {
					((HistogrammPanel) panel).updateHistogrammData(Math.min(this.mInfo.a_pos,
							this.mInfo.b_pos), Math.max(this.mInfo.a_pos, this.mInfo.b_pos));
					this.jLayeredPane.repaint();
				}
			}
		}
	}

	public void useMarkers(boolean b)
	{
		this.useMarkers = b;
	}
}

class HistogrammToolBar extends ScalableToolBar
{
	protected static final String BIND_MARK = "bind2markers";

	JToggleButton markersTButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, BIND_MARK
	};

	public HistogrammToolBar (HistogrammLayeredPanel panel)
	{
		super(panel);
	}

	@Override
	protected String[] getButtons()
	{
		return buttons;
	}

	@Override
	protected Map createGraphButtons()
	{
		Map buttons1 = new HashMap();

		buttons1.put(
				BIND_MARK,
				createToolButton(
				markersTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("bindToMarker"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_MARKER),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						markersTButton_actionPerformed(e);
					}
				},
				true));

		buttons1.putAll(super.createGraphButtons());
		return buttons1;
	}

	void markersTButton_actionPerformed(ActionEvent e)
	{
		HistogrammLayeredPanel panel1 = (HistogrammLayeredPanel)super.panel;
		boolean b = this.markersTButton.isSelected();
		panel1.useMarkers(b);
		if (b)
			panel1.updateHistogrammData();
	}
}

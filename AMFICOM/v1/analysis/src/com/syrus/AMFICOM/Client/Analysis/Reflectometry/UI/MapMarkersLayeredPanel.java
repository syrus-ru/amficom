package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

public class MapMarkersLayeredPanel extends TraceEventsLayeredPanel implements OperationListener
{
	Dispatcher dispatcher;

	public MapMarkersLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
	}

	protected ToolBarPanel createToolBar()
	{
		return new MapMarkersToolBar(this);
	}

	void init_module(Dispatcher dispatcher)
	{
		super.init_module(dispatcher);
		dispatcher.register(this, MapNavigateEvent.type);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(MapNavigateEvent.type))
		{
			MapNavigateEvent mne = (MapNavigateEvent)ae;
			for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof MapMarkersPanel)
				{
					if(mne.MAP_MARKER_CREATED)
					{
						if ( (mne.meID != null && mne.meID.equals(((MapMarkersPanel)panel).monitored_element_id) )||
								 (mne.mappathID != null && mne.mappathID.equals(((MapMarkersPanel)panel).map_path_id)) )
						{

//							double d = WorkWithReflectoArray.getDistanceTillLastSplash(panel.y, panel.delta_x, 1);
//							mne.spd.setMeasurement (new LengthParameters (((MapMarkersPanel)panel).ep, panel.delta_x, "", d));
//							double dist = mne.spd.getMeasuredDistance(mne.distance);
							double dist = mne.distance;
							((MapMarkersPanel)panel).createMarker("", mne.marker_id, dist);
							((MapMarkersPanel)panel).move_notify();
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
							jLayeredPane.repaint();
						}
					}
					if(mne.DATA_ALARMMARKER_CREATED)
					{
						if ( (mne.meID != null && mne.meID.equals(((MapMarkersPanel)panel).monitored_element_id) )||
								 (mne.mappathID != null && mne.mappathID.equals(((MapMarkersPanel)panel).map_path_id)) )
						{
							double dist = mne.distance;
							AlarmMarker am = ((MapMarkersPanel)panel).get_alarm_marker();
							if(am == null)
							{
								((MapMarkersPanel)panel).createAlarmMarker("", mne.marker_id, dist);
							}
							else
							{
								am.id = mne.marker_id;
								((MapMarkersPanel)panel).moveMarker(mne.marker_id, dist);
							}
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
							jLayeredPane.repaint();
						}
					}
					if(mne.MAP_MARKER_SELECTED)
					{
						((MapMarkersPanel)panel).activateMarker(mne.marker_id);
						jLayeredPane.repaint();
					}
					if(mne.MAP_MARKER_MOVED)
					{
						((MapMarkersPanel)panel).moveMarker(mne.marker_id, mne.distance);
						jLayeredPane.repaint();
					}
					if(mne.MAP_MARKER_DELETED)
					{
						if (((MapMarkersPanel)panel).deleteMarker(mne.marker_id) == null)
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(false);
						jLayeredPane.repaint();
					}
				}
			}
		}
		super.operationPerformed(ae);
	}

	public void removeAllGraphPanels()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof MapMarkersPanel)
			{
				((MapMarkersPanel)panel).removeAllMarkers();
			};
		}

		super.removeAllGraphPanels();
		((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(false);
	}

	public void createMarker (boolean b)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof MapMarkersPanel)
			{
				((MapMarkersPanel)panel).creating_marker = b;
				return;
			};
		}
	}

	public Marker deleteActiveMarker ()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof MapMarkersPanel)
			{
				Marker m = ((MapMarkersPanel)panel).deleteActiveMarker();
				if (((MapMarkersPanel)panel).markers.isEmpty())
				{
					((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(false);
				}
				repaint();
				return m;
			}
		}
		return null;
	}

	void setButtons()
	{
		((MapMarkersToolBar)toolbar).createMarkerTButton.setSelected(false);
		((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
		((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
	}
}

class MapMarkersToolBar extends TraceEventsToolBar
{
	protected static final String createMarker = "createMarker";
	protected static final String deleteMarker = "deleteMarker";

	JToggleButton createMarkerTButton = new JToggleButton();
	JButton deleteMarkerButton = new JButton();

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit, separator, events, separator, createMarker, deleteMarker
	};

	public MapMarkersToolBar(MapMarkersLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = new HashMap();

		buttons.put(
				createMarker,
				createToolButton(
				createMarkerTButton,
				btn_size,
				null,
				LangModelAnalyse.getString("addmarker"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/marker.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						createMarkerTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				deleteMarker,
				createToolButton(
				deleteMarkerButton,
				btn_size,
				null,
				LangModelAnalyse.getString("removemarker"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/marker_delete.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteMarkerButton_actionPerformed(e);
					}
				},
				true));

		buttons.putAll(super.createGraphButtons());
		return buttons;
	}

	void createMarkerTButton_actionPerformed(ActionEvent e)
	{
		MapMarkersLayeredPanel panel = (MapMarkersLayeredPanel)super.panel;
		panel.createMarker (createMarkerTButton.isSelected());
	}

	void deleteMarkerButton_actionPerformed(ActionEvent e)
	{
		MapMarkersLayeredPanel panel = (MapMarkersLayeredPanel)super.panel;
		Marker m = panel.deleteActiveMarker();
	}
}

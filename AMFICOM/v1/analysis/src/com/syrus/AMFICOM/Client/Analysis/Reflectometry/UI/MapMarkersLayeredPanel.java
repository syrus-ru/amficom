package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class MapMarkersLayeredPanel extends TraceEventsLayeredPanel implements PropertyChangeListener
{
	public MapMarkersLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);

		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{ // empty
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new MapMarkersToolBar(this);
	}

	@Override void init_module(Dispatcher dispatcher)
	{
		super.init_module(dispatcher);
		dispatcher.addPropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent ae)
	{
		if(ae.getPropertyName().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent)ae;
			for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof MapMarkersPanel)
				{
					if(mne.isMapMarkerCreated())
					{
						if ( (mne.getMeId() != null && mne.getMeId().equals(((MapMarkersPanel)panel).monitored_element_id)) ||
								 (mne.getSchemePathId() != null && mne.getSchemePathId().equals(((MapMarkersPanel)panel).scheme_path_id)))
						{

//							double d = WorkWithReflectoArray.getDistanceTillLastSplash(panel.y, panel.deltaX, 1);
//							mne.spd.setMeasurement (new LengthParameters (((MapMarkersPanel)panel).ep, panel.deltaX, "", d));
//							double dist = mne.spd.getMeasuredDistance(mne.distance);
							double dist = mne.getDistance();
							Marker m = ((MapMarkersPanel)panel).createMarker("", dist);
							m.setId(mne.getMarkerId());
							((MapMarkersPanel)panel).move_notify();
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
							jLayeredPane.repaint();
						}
					}
					if(mne.isDataAlarmMarkerCreated())
					{
						if ( (mne.getMeId() != null && mne.getMeId().equals(((MapMarkersPanel)panel).monitored_element_id)) ||
								 (mne.getSchemePathId() != null && mne.getSchemePathId().equals(((MapMarkersPanel)panel).scheme_path_id)))
						{
							double dist = mne.getDistance();
							AlarmMarker am = ((MapMarkersPanel)panel).get_alarm_marker();
							if(am == null)
							{
								((MapMarkersPanel)panel).createAlarmMarker("", mne.getMarkerId(), dist);
							} else
							{
								am.setId(mne.getMarkerId());
								((MapMarkersPanel)panel).moveMarker(mne.getMarkerId(), dist);
							}
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(true);
							jLayeredPane.repaint();
						}
					}
					if(mne.isMapMarkerSelected())
					{
						((MapMarkersPanel)panel).activateMarker(mne.getMarkerId());
						jLayeredPane.repaint();
					}
					if(mne.isMapMarkerMoved())
					{
						((MapMarkersPanel)panel).moveMarker(mne.getMarkerId(), mne.getDistance());
						jLayeredPane.repaint();
					}
					if(mne.isMapMarkerDeleted())
					{
						if (((MapMarkersPanel)panel).deleteMarker(mne.getMarkerId()) == null)
							((MapMarkersToolBar)toolbar).deleteMarkerButton.setEnabled(false);
						jLayeredPane.repaint();
					}
				}
			}
		}
		super.propertyChange(ae);
	}

	@Override
	public void removeAllGraphPanels()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof MapMarkersPanel)
			{
				((MapMarkersPanel)panel).removeAllMarkers();
			}
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
			}
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

	protected static String[] buttons1 = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, events, SEPARATOR, createMarker, deleteMarker
	};

	public MapMarkersToolBar(MapMarkersLayeredPanel panel)
	{
		super(panel);
	}

	@Override
	protected String[] getButtons()
	{
		return buttons1;
	}

	@Override
	protected Map createGraphButtons()
	{
		Map buttons2 = new HashMap();

		buttons2.put(
				createMarker,
				createToolButton(
				createMarkerTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("addmarker"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_MARKER),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						createMarkerTButton_actionPerformed(e);
					}
				},
				true));
		buttons2.put(
				deleteMarker,
				createToolButton(
				deleteMarkerButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("removemarker"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DELETE_MARKER),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteMarkerButton_actionPerformed(e);
					}
				},
				true));

		buttons2.putAll(super.createGraphButtons());
		return buttons2;
	}

	void createMarkerTButton_actionPerformed(ActionEvent e)
	{
		MapMarkersLayeredPanel panel1 = (MapMarkersLayeredPanel)super.panel;
		panel1.createMarker (createMarkerTButton.isSelected());
	}

	void deleteMarkerButton_actionPerformed(ActionEvent e)
	{
		MapMarkersLayeredPanel panel1 = (MapMarkersLayeredPanel)super.panel;
		panel1.deleteActiveMarker();
	}
}

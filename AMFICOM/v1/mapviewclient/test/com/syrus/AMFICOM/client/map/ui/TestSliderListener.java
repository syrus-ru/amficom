/*-
 * $Id: TestSliderListener.java,v 1.2 2006/06/16 10:19:56 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.test.SchemeSampleData;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;

/**
 * @author Andrey Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/06/16 10:19:56 $
 * @module mapviewclient
 */
final class TestSliderListener implements ChangeListener, PropertyChangeListener {
	Identifier markerId = null;
	final MapFrame mapFrame;
	private final JSlider slider;
	boolean notInitialized = true;
	
	public TestSliderListener(MapFrame mapFrame, JSlider slider) {
		this.mapFrame = mapFrame;
		this.slider = slider;
		this.mapFrame.getContext().getDispatcher().addPropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
		this.mapFrame.getContext().getDispatcher().addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
	}

	public void stateChanged(ChangeEvent e) {
		try {
			Dispatcher dispatcher = this.mapFrame.aContext.getDispatcher();
			if(this.markerId == null) {
				this.markerId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_CODE);
				dispatcher.firePropertyChange(new MarkerEvent(
						this, 
						MarkerEvent.MARKER_CREATED_EVENT, 
						this.markerId,
						this.slider.getValue(),
						null,
						SchemeSampleData.scheme1path0.getId(),
						null), false);
			}
			dispatcher.firePropertyChange(new MarkerEvent(
					this, 
					MarkerEvent.MARKER_SELECTED_EVENT, 
					this.markerId), false);
			dispatcher.firePropertyChange(new MarkerEvent(
					this, 
					MarkerEvent.MARKER_MOVED_EVENT, 
					this.markerId,
					((JSlider)(e.getSource())).getValue(),
					null,
					SchemeSampleData.scheme1path0.getId(),
					null), false);
		} catch(IdentifierGenerationException e1) {
			Log.errorMessage(e1);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent) evt;
			if(mapEvent.getMapEventType().equals(MapEvent.MAP_FRAME_SHOWN)
					&& false
					&& this.notInitialized) {
				new ProcessingDialog(new Runnable() {
				
					public void run() {
						Log.debugMessage("waiting for SchemeSampleData...", Log.DEBUGLEVEL09); //$NON-NLS-1$
						while(!SchemeSampleData.loaded) {
							try {
								Thread.sleep(100);
							} catch(InterruptedException e) {
								//nothing
							}
						}
						Log.debugMessage(" placing elements...", Log.DEBUGLEVEL09); //$NON-NLS-1$
						TestSliderListener.this.notInitialized = false;
						NetMapViewer netMapViewer = TestSliderListener.this.mapFrame.getMapViewer();

						TestSliderListener.this.mapFrame.getMapView().addScheme(SchemeSampleData.scheme1);

						PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, new Point(50, 200));
						startcommand.setNetMapViewer(netMapViewer);
						startcommand.execute();
				
						PlaceSchemeElementCommand intercommand1 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, new Point(200, 50));
						intercommand1.setNetMapViewer(netMapViewer);
						intercommand1.execute();
				
						PlaceSchemeElementCommand intercommand2 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element2, new Point(400, 50));
						intercommand2.setNetMapViewer(netMapViewer);
						intercommand2.execute();
				
						PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, new Point(300, 250));
						endcommand.setNetMapViewer(netMapViewer);
						endcommand.execute();
						Log.debugMessage("OK!", Log.DEBUGLEVEL09); //$NON-NLS-1$
					}
				
				}, "Ùà íàõóÿðþ õóÿðèêîâ..."); //$NON-NLS-1$
			}
		}
		else if(evt.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			MarkerEvent markerEvent = (MarkerEvent) evt;
//			if(markerEvent.getMarkerId() != this.markerId) {
//				return;
//			}
			Dispatcher dispatcher = this.mapFrame.aContext.getDispatcher();
			switch(markerEvent.getMarkerEventType()) {
				case MarkerEvent.MARKER_DELETED_EVENT:
					this.markerId = null;
					break;
				case MarkerEvent.MARKER_CREATED_EVENT:
					dispatcher.firePropertyChange(new MarkerEvent(
							this, 
							MarkerEvent.MARKER_DELETED_EVENT, 
							this.markerId), false);
					this.markerId = markerEvent.getMarkerId();
					break;
				case MarkerEvent.MARKER_MOVED_EVENT:
					this.slider.setValue((int) markerEvent.getOpticalDistance());
					break;
			}
		}
		
	}
}

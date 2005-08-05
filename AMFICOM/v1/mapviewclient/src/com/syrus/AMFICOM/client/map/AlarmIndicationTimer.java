/**
 * $Id: AlarmIndicationTimer.java,v 1.2 2005/08/05 07:37:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;

import javax.swing.Timer;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapTypedElementsContainer;
import com.syrus.util.Log;

public class AlarmIndicationTimer {

	ActionListener timerActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(!MapPropertiesManager.isShowAlarmIndication()) {
				// Indication ist ausgeschalten
				return;
			}
			try {
				boolean isVisible = false;
				Rectangle2D.Double visibleBounds = 
					AlarmIndicationTimer.this.netMapViewer.getVisibleBounds();
				for(MapElement mapElement : AlarmIndicationTimer.this.container.getElements()) {
					MapElementController controller = 
						AlarmIndicationTimer.this.logicalNetLayer
							.getMapViewController().getController(mapElement);
					if(controller.isElementVisible(mapElement, visibleBounds)) {
						isVisible = true;
						break;
					}
				}
				if(isVisible) {
					MapPropertiesManager.setDrawAlarmed(
							!MapPropertiesManager.isDrawAlarmed());
					AlarmIndicationTimer.this.dispatcher.firePropertyChange(
							new MapEvent(this, MapEvent.NEED_REPAINT));
				}
			} catch(MapException ex) {
				Log.debugException(ex, Level.SEVERE);
				ex.printStackTrace();
			}
		}
	};

	final Dispatcher dispatcher;

	final Timer animateTimer;
	final NetMapViewer netMapViewer;
	final LogicalNetLayer logicalNetLayer;
	final MapTypedElementsContainer container;

	public static final int DEFAULT_TIME_INTERVAL = 1000;

	public AlarmIndicationTimer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		this.dispatcher = this.logicalNetLayer.getContext().getDispatcher();
		this.container = new MapTypedElementsContainer();
		this.animateTimer = new Timer(
				AlarmIndicationTimer.DEFAULT_TIME_INTERVAL,
				this.timerActionListener);
		this.animateTimer.start();
	}

	public void dispose() {
		this.animateTimer.stop();
	}

}

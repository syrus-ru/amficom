/*-
 * $$Id: CenterSelectionCommand.java,v 1.27 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ÷ентрировать геометрическое место точек, €вл€ющих собой центры 
 * выделенных элементов карты
 * 
 * @version $Revision: 1.27 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CenterSelectionCommand extends MapNavigateCommand {
	public CenterSelectionCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void execute() {
		if(this.netMapViewer == null) {
			return;
		}

		MapElement me;
		
		MapView mapView = this.netMapViewer.getLogicalNetLayer().getMapView();

		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = mapView.getMap().getSelectedElements().iterator(); it.hasNext();) {
			me = (MapElement)it.next();
			DoublePoint an = me.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}

		if(count != 0) {
			x /= count;
			y /= count;

			point.setLocation(x, y);
		
			try {
				this.netMapViewer.setCenter(point);
			} catch(MapException e) {
				setException(e);
				setResult(Command.RESULT_NO);
				Log.errorMessage(e);
			}
		}
	}
}

/**
 * $Id: CenterSelectionCommand.java,v 1.19 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ������������ �������������� ����� �����, �������� ����� ������ 
 * ���������� ��������� �����
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public class CenterSelectionCommand extends MapNavigateCommand {
	public CenterSelectionCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

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

/*
		for(Iterator it = mapView.getMap().getNodes().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMarkers().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMap().getAllNodeLinks().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMap().getAllPhysicalLinks().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getCablePaths().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMeasurementPaths().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}
*/
		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		try {
			this.netMapViewer.getMapContext().setCenter(point);
			this.netMapViewer.repaint(true);
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}

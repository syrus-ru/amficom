/**
 * $Id: ViewMapWindowCommand.java,v 1.19 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ������� ���������� ���� ����� 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class ViewMapWindowCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapApplicationModelFactory factory;

	protected MapFrame mapFrame;

	public ViewMapWindowCommand(
			JDesktopPane desktop,
			ApplicationContext aContext,
			MapApplicationModelFactory factory) {
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void setParameter(String field, Object value) {
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		try {
			this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

			if(this.mapFrame == null) {
				ApplicationContext aC = new ApplicationContext();
				aC.setApplicationModel(this.factory.create());
				aC.setDispatcher(this.aContext.getDispatcher());

				this.mapFrame = new MapFrame();
				this.mapFrame.setContext(aC);

				this.desktop.add(this.mapFrame);
				Dimension dim = this.desktop.getSize();
				this.mapFrame.setLocation(0, 0);
				this.mapFrame.setSize(dim.width * 3 / 5, dim.height);

				setMapFrame();
			}

			this.mapFrame.setVisible(true);
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(this.mapFrame, MapEvent.MAP_FRAME_SHOWN));
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished")));
			setResult(Command.RESULT_OK);
		} catch(MapConnectionException e) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"��� ���������� � �������� ���������������� ����������",
					"",
					JOptionPane.ERROR_MESSAGE);
		} catch(MapDataException e) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"������ ������� � ���������������� ������",
					"",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void setMapFrame()
			throws MapConnectionException, MapDataException {
		MapView mapView = null;
		Map map = null;

		MapNewCommand mnc = new MapNewCommand(this.aContext);
		mnc.execute();
		if(mnc.getResult() == Command.RESULT_OK) {
			map = mnc.getMap();
		}
		else
			return;

		MapViewNewCommand mvnc = new MapViewNewCommand(map, this.aContext);
		mvnc.execute();
		if(mvnc.getResult() == Command.RESULT_OK) {
			mapView = mvnc.getMapView();
			mapView.setCenter(this.mapFrame.getMapViewer().getLogicalNetLayer().getCenter());
			mapView.setScale(this.mapFrame.getMapViewer().getLogicalNetLayer().getScale());
		}
		else
			return;

		mapView.setMap(map);

		this.mapFrame.setMapView(mapView);
	}

}

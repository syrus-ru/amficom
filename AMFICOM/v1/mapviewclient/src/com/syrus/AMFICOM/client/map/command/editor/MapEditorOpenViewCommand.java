/*-
 * $$Id: MapEditorOpenViewCommand.java,v 1.31 2005/10/03 10:35:01 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewOpenCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� MapEditorOpenViewCommand ������������ ��� �������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapOpenCommand, � ���� 
 * ������������ ������ MapContext, ����������� ���� ����� � ������������� ����
 * � MapContext ���������� � ���� �����
 * 
 * @version $Revision: 1.31 $, $Date: 2005/10/03 10:35:01 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.command.map.MapOpenCommand
 */
public class MapEditorOpenViewCommand extends AbstractCommand {
	protected ApplicationContext aContext;

	protected JDesktopPane desktop;

	protected MapFrame mapFrame = null;

	protected MapView mapView = null;

	/**
	 * � ������ �������������� �������������� ���� � ������������ ����
	 * ����������� ������� MapContext � ���� ���������� �������
	 */
	private boolean canDelete = true;

	private boolean checkSave = true;

	private final MapApplicationModelFactory factory;

	/**
	 * @param desktop ���� ������ ���� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorOpenViewCommand(
			JDesktopPane desktop,
			ApplicationContext aContext,
			MapApplicationModelFactory factory) {
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null) {
			if(isCheckSave()) {
				if(this.mapFrame.checkChangesPresent())
					return;
			}
		}

		MapViewOpenCommand moc = new MapViewOpenCommand(
				this.desktop,
				this.aContext);
		moc.setCanDelete(isCanDelete());
		moc.execute();

		if(moc.getResult() == Command.RESULT_OK) {
			this.mapView = moc.getMapView();

			this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			if(this.mapFrame == null) {
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
						this.desktop,
						this.aContext,
						this.factory);

				mapCommand.execute();
				this.mapFrame = mapCommand.mapFrame;
			}

			if(this.mapFrame == null) {
				return;
			}

			try {
				this.mapFrame.setMapView(this.mapView);
				ViewGeneralPropertiesCommand propCommand = new ViewGeneralPropertiesCommand(
						this.desktop,
						this.aContext);
				propCommand.execute();
				ViewAdditionalPropertiesCommand addCommand = new ViewAdditionalPropertiesCommand(
						this.desktop,
						this.aContext);
				addCommand.execute();
			} catch(MapException e) {
				this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelMap.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
				e.printStackTrace();
			}
		}
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean isCanDelete() {
		return this.canDelete;
	}

	public void setCheckSave(boolean checkSave) {
		this.checkSave = checkSave;
	}

	public boolean isCheckSave() {
		return this.checkSave;
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

}

/*
 * $Id: MapEditorOpenViewCommand.java,v 1.13 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapEditorOpenViewCommand.java,v $ используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.13 $, $Date: 2005/02/08 15:11:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenViewCommand extends VoidCommand
{
	protected ApplicationContext aContext;
	protected JDesktopPane desktop;
	
	protected MapFrame mapFrame = null;
	protected MapPropertyFrame propFrame = null;
	protected MapElementsFrame elementsFrame = null;
	
	protected MapView mapView = null;

	// в модуле редактирования топологических схем у пользователя есть
	// возможность удалять MapContext в окне управления схемами
	private boolean canDelete = true;

	private boolean checkSave = true;

	/**
	 * 
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapEditorOpenViewCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null)
		{
			if(isCheckSave())
			{
				if(!this.mapFrame.checkCanCloseMap())
					return;
				if(!this.mapFrame.checkCanCloseMapView())
					return;
			}
		}

		MapViewOpenCommand moc = new MapViewOpenCommand(this.desktop, this.aContext);
		moc.setCanDelete(isCanDelete());
		moc.execute();

		if (moc.getResult() == Command.RESULT_OK)
		{
			this.mapView = moc.getMapView();
		
			this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			if(this.mapFrame == null)
			{
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
					this.aContext.getDispatcher(), 
					this.desktop, 
					this.aContext, 
					new MapMapEditorApplicationModelFactory());

				mapCommand.execute();
				this.mapFrame = mapCommand.mapFrame;
			}

			if(this.mapFrame == null)
				return;

			this.mapFrame.setMapView(this.mapView);

			ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(this.desktop, this.aContext);
			propCommand.execute();
			this.propFrame = propCommand.frame;

			ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(this.desktop, this.aContext);
			elementsCommand.execute();
			this.elementsFrame = elementsCommand.frame;
		}
	}

	public MapFrame getMapFrame()
	{
		return this.mapFrame;
	}

	public MapPropertyFrame getPropertiesFrame()
	{
		return this.propFrame;
	}

	public MapElementsFrame getElementsFrame()
	{
		return this.elementsFrame;
	}


	public void setCanDelete(boolean canDelete)
	{
		this.canDelete = canDelete;
	}


	public boolean isCanDelete()
	{
		return this.canDelete;
	}


	public void setCheckSave(boolean checkSave)
	{
		this.checkSave = checkSave;
	}


	public boolean isCheckSave()
	{
		return this.checkSave;
	}

}

/**
 * $Id: MapEditorAddSchemeToViewCommand.java,v 1.5 2005/01/21 13:49:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import javax.swing.JDesktopPane;

/**
 * добавить в вид схему из списка
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorAddSchemeToViewCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected ObjectResource retObj;

	public MapEditorAddSchemeToViewCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public ObjectResource getReturnObject()
	{
		return this.retObj;
	}

	public void execute()
	{
/*
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(dataSource, Scheme.typ);

		List dataSet = Pool.getList(Scheme.typ);
		ObjectResourceDisplayModel odm = Scheme.getDefaultDisplayModel();
		mcd.setContents(odm, dataSet);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену
		ortm.fireTableDataChanged();

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == mcd.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			retObj = mcd.getReturnObject();

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				setResult(Command.RESULT_NO);
			}
			else
			{
				mapFrame.getMapView().addScheme((Scheme )retObj);
				mapFrame.getContext().getDispatcher().notify(new MapEvent(
						mapFrame.getMapView(),
						MapEvent.MAP_VIEW_CHANGED));
				setResult(Command.RESULT_OK);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
*/
	}

}

/**
 * $Id: MapPopupMenu.java,v 1.2 2004/09/16 12:00:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPopupMenu;

/**
 * Контекстное меню элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/16 12:00:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapPopupMenu extends JPopupMenu
{
	protected LogicalNetLayer logicalNetLayer;
	
	protected Point point;

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void setPoint(Point point)
	{
		this.point = point;
	}

	public Point getPoint()
	{
		return point;
	}

	protected void showProperties(MapElement me)
	{
		ObjectResourcePropertiesPane prop = MapPropsManager.getPropsPane(me);
		if(prop == null)
			return;
		prop.setContext(logicalNetLayer.getContext());
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				(ObjectResource )me,
				prop);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
		}
	}

	public abstract void setMapElement(MapElement me);
}

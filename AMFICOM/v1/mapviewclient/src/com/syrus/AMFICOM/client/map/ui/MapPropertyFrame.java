/**
 * $Id: MapPropertyFrame.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyFrame;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 *  Окно отображения свойств элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPropertyFrame extends ObjectResourcePropertyFrame
		implements OperationListener
{
	public MapPropertyFrame(String title)
	{
		super(title);
		jbInit();
	}

	public MapPropertyFrame(String title, ApplicationContext aContext)
	{
		super(title, aContext);
		jbInit();
	}

	private void jbInit()
	{
		this.setIconifiable(true);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setTitle(LangModelMap.getString("Properties"));
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_NAVIGATE);
				disp.unregister(this, MapEvent.MAP_DESELECTED);
			}
		super.setContext(aContext);
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
		disp.register(this, MapEvent.MAP_DESELECTED);
	}

	public void initialize()
	{
		super.initialize();
		panel.setTableModel(new MapPropertyTableModel(aContext));
	}

	public void setObjectResource(ObjectResource or)
	{
		panel.setSelected(or);
	}
/*
	public void operationPerformed(OperationEvent oe )
	{
		super.operationPerformed(oe);
		if(oe.getActionCommand().equals(MapEvent.MAP_DESELECTED))
		{
			panel.setSelected(null);
		}
		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.MAP_ELEMENT_SELECTED)
			{
				ObjectResource me = (ObjectResource )mne.getSource();
				panel.setSelected(me);
			}
		}
	}
*/

	private class MapPropertyTableModel extends ObjectResourcePropertyTableModel
	{
		ApplicationContext aContext;
		
		public MapPropertyTableModel(ApplicationContext aContext)
		{
			super(
				new String[] {
					LangModelMap.getString("Property"), 
					LangModelMap.getString("Value")}, 
				null);
			this.aContext = aContext;
		}
	
		public boolean isCellEditable(int p_row, int p_col)
		{
			
			if(!aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
				return false;
	
			return super.isCellEditable(p_row, p_col);
		}
	}
}

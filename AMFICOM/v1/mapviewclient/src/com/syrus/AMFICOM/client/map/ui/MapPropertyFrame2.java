/**
 * $Id: MapPropertyFrame2.java,v 1.1 2004/09/29 15:10:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Props.MapElementPropertiesTableModel;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *  Окно отображения свойств элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/29 15:10:09 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPropertyFrame2 extends JInternalFrame
		implements OperationListener, TableModelListener
{
	public ApplicationContext aContext;

	JTable table;
	MapElementPropertiesTableModel model = MapElementPropertiesTableModel.getEmpltyTableModel();
	
	public MapPropertyFrame2(String title)
	{
		this(title, new ApplicationContext());
	}

	public MapPropertyFrame2(String title, ApplicationContext aContext)
	{
		super(title);

		table = new JTable(model);

		jbInit();

		setContext(aContext);
	}

	private void jbInit()
	{
		this.setIconifiable(true);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setTitle(LangModelMap.getString("Properties"));

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
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
		this.aContext = aContext;
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
		disp.register(this, MapEvent.MAP_DESELECTED);
	}

	public void setObjectResource(ObjectResource or)
	{
		if(model != null)
		{
			model.removeTableModelListener(this);
		}

		if(or != null)
			model = MapPropsManager.getPropertiesTableModel((MapElement )or);
		else
			model = null;

		if(model == null)
			model = MapElementPropertiesTableModel.getEmpltyTableModel();
		else
		{
			model.addTableModelListener(this);
		}
		table.setModel(model);
		table.updateUI();
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.isMapElementSelected())
			{
				ObjectResource me = (ObjectResource )mne.getSource();
				setObjectResource(me);
			}
		}
	}

	public void tableChanged(TableModelEvent e)
	{
		Dispatcher disp = aContext.getDispatcher();
		if(disp != null)
			disp.notify(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}

/**
 * $Id: MapPropertyFrame.java,v 1.12 2005/01/30 15:38:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapElementPropertiesController;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;
import com.syrus.AMFICOM.client_.general.ui_.ObjPropertyTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjPropertyTableModel;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *  Окно отображения свойств элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/01/30 15:38:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPropertyFrame extends JInternalFrame
		implements OperationListener, TableModelListener
{
	public ApplicationContext aContext;

	ObjPropertyTable table = null;
	ObjPropertyTableModel model = null;
	ObjectResourcePropertiesController controller = null;
	
	Object or;

	JScrollPane scrollPane = new JScrollPane();

	private boolean doNotify = true;

	public MapPropertyFrame(String title)
	{
		this(title, new ApplicationContext());
	}

	public MapPropertyFrame(String title, ApplicationContext aContext)
	{
		super(title);

		initialize();

		jbInit();

		setContext(aContext);
	}

	private void jbInit()
	{
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(false);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setTitle(LangModelMap.getString("Properties"));

		getContentPane().setLayout(new BorderLayout());

		scrollPane.getViewport().add(table);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_NAVIGATE);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
	}

	public void initialize()
	{
		controller = new MapElementPropertiesController();
		model = new ObjPropertyTableModel(controller, null);
		table = new ObjPropertyTable(model);
		model.addTableModelListener(this);
	}

	public void setObject(Object or)
	{
		this.or = or;
		controller = MapPropsManager.getPropertiesController((MapElement )or);
		model.setObject(or);
		model.setController(controller);
		table.updateUI();
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.isMapElementSelected())
			{
				Object me = mne.getSource();
				doNotify = false;
				setObject(me);
				doNotify = true;
			}
		}
	}

	public void tableChanged(TableModelEvent e)
	{
		if(doNotify)
		{
			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				if(or instanceof Map)
					disp.notify(new MapEvent(or, MapEvent.MAP_CHANGED));
				else
				if(or instanceof MapView)
					disp.notify(new MapEvent(or, MapEvent.MAP_VIEW_CHANGED));
				else
					disp.notify(new MapEvent(or, MapEvent.MAP_ELEMENT_CHANGED));
			}
		}
	}
}

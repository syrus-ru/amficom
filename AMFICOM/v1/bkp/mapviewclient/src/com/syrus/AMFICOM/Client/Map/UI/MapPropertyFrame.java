/**
 * $Id: MapPropertyFrame.java,v 1.14 2005/04/19 15:50:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Props.MapElementPropertiesController;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.client_.general.ui_.ObjPropertyTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjPropertyTableModel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @deprecated
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.14 $, $Date: 2005/04/19 15:50:12 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapPropertyFrame extends JInternalFrame
		implements OperationListener, TableModelListener
{
	public ApplicationContext aContext;

	ObjPropertyTable table = null;
	ObjPropertyTableModel model = null;
	ObjectResourcePropertiesController controller = null;
	
	Object object;

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

		this.scrollPane.getViewport().add(this.table);

		this.scrollPane.setWheelScrollingEnabled(true);
		this.scrollPane.getViewport().setBackground(SystemColor.window);
		this.table.setBackground(SystemColor.window);

		getContentPane().add(this.scrollPane, BorderLayout.CENTER);
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
		this.controller = new MapElementPropertiesController();
		this.model = new ObjPropertyTableModel(this.controller, null);
		this.table = new ObjPropertyTable(this.model);
		this.model.addTableModelListener(this);
	}

	public void setObject(Object object)
	{
		this.object = object;
		this.controller = MapPropsManager.getPropertiesController((MapElement )this.object);
		this.model.setObject(this.object);
		this.model.setController(this.controller);
		this.table.updateUI();
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.isMapElementSelected())
			{
				Object selectedElement = mne.getSource();
				this.doNotify = false;
				setObject(selectedElement);
				this.doNotify = true;
			}
		}
	}

	public void tableChanged(TableModelEvent e)
	{
		if(this.doNotify)
		{
			Dispatcher dispatcher = this.aContext.getDispatcher();
			if(dispatcher != null)
			{
				if(this.object instanceof Map)
					dispatcher.notify(new MapEvent(this.object, MapEvent.MAP_CHANGED));
				else
				if(this.object instanceof MapView)
					dispatcher.notify(new MapEvent(this.object, MapEvent.MAP_VIEW_CHANGED));
				else
					dispatcher.notify(new MapEvent(this.object, MapEvent.MAP_ELEMENT_CHANGED));
			}
		}
	}
}

/**
 * $Id: MapPropertyFrame.java,v 1.4 2004/10/01 16:34:08 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesTableModel;

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
 * @version $Revision: 1.4 $, $Date: 2004/10/01 16:34:08 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPropertyFrame extends JInternalFrame
		implements OperationListener, TableModelListener
{
	public ApplicationContext aContext;

	ObjectResourcePropertiesTable table = null;
	ObjectResourcePropertiesTableModel model = null;
	ObjectResourcePropertiesController controller = null;
	
	ObjectResource or;

	JScrollPane scrollPane = new JScrollPane();

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
		this.setIconifiable(true);
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

	public void initialize()
	{
		model = new ObjectResourcePropertiesTableModel(controller);
		table = new ObjectResourcePropertiesTable(model);
		model.addTableModelListener(this);
	}

	public void setObjectResource(ObjectResource or)
	{
		controller = MapPropsManager.getPropertiesController((MapElement )or);
		model.setObjectResource(or);
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

/**
 * $Id: MapPropertyFrame.java,v 1.2 2004/09/17 11:39:25 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyFrame;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.awt.BorderLayout;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 *  Окно отображения свойств элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/17 11:39:25 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPropertyFrame extends JInternalFrame
		implements OperationListener
{
	public ApplicationContext aContext;

	ObjectResourceTable table;
	ObjectResourceTableModel model;
	ObjectResourceController controller;
	
	ObjectResource or;

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

//		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
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
//		controller = new ObjectResourcePropertiesController();
//		model = new ObjectResourceTableModel(controller);
//		table = new ObjectResourceTable(model);
	}

	public void setObjectResource(ObjectResource or)
	{
//		controler.setObjectResource(or);
		table.updateUI();
	}

	public void operationPerformed(OperationEvent oe )
	{
/*
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
*/
	}

/*
	private class ObjectResourcePropertiesController extends ObjectResourceController
	{
//		ApplicationContext aContext;
		public static final String KEY_PROPERTY = "property";
		public static final String KEY_VALUE = "value";
	
		private static ObjectResourcePropertiesController instance;
	
		private List keys;
		
		private ObjectResourcePropertiesController() 
		{
			// empty private constructor
			String[] keysArray = new String[] { KEY_PROPERTY, KEY_VALUE};
		
			this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
		}

		public static ObjectResourcePropertiesController getInstance() 
		{
			if (instance == null)
				instance = new ObjectResourcePropertiesController();
			return instance;
		}
	
		public List getKeys()
		{
			return this.keys;
		}
	
		public String getName(final String key)
		{
			String name = null;
			if (key.equals(KEY_PROPERTY))
				name = LangModelMap.getString("Property");
			else
			if (key.equals(KEY_VALUE))
				name = LangModelMap.getString("Value");
			return name;
		}

		public void setObjectResource(ObjectResource or)
		{
			controler.setObjectResource(or);
			table.updateUI();
		}

		public boolean isCellEditable(int p_row, int p_col)
		{
			if(!aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
				return false;
	
			return true;
//			return super.isCellEditable(p_row, p_col);
		}
	}
*/
}

package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;

public class MeasurementPortPane extends JPanel implements
		ObjectResourcePropertiesPane {
	protected MeasurementPort port;
	private static ObjectResourcePropertiesPane instance;

	private MeasurementPortGeneralPanel gPanel = new MeasurementPortGeneralPanel();
	private MeasurementPortCharacteristicsPanel chPanel = new MeasurementPortCharacteristicsPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();

	protected MeasurementPortPane() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected MeasurementPortPane(MeasurementPort p) {
		this();
		setObject(p);
	}

	public static ObjectResourcePropertiesPane getInstance() {
		if (instance == null)
			instance = new MeasurementPortPane();
		return instance;
	}

	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public Object getObject() {
		return port;
	}

	public void setObject(Object or) {
		this.port = (MeasurementPort) or;

		gPanel.setObject(port);
		chPanel.setObject(port);
	}

	public void setContext(ApplicationContext aContext) {
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify() {
		if (gPanel.modify() && chPanel.modify())
			return true;
		return false;
	}

	public boolean save() {
		if (modify()) {
			if (chPanel.save()) {
				try {
					StorableObjectPool.putStorableObject(port);
					ConfigurationStorableObjectPool.flush(true);
					return true;
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
		JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
		return false;
	}

	public boolean open() {
		return false;
	}

	public boolean delete() {
		return true;
	}

	public boolean create() {
		return false;
	}

	public boolean cancel() {
		return false;
	}
}

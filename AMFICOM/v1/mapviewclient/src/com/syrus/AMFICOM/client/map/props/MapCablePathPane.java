package com.syrus.AMFICOM.client.map.props;


public class MapCablePathPane {
	// @todo remove this class after checking that CablePathAddEditor works properly
}
//
//		extends JPanel 
//		implements ObjectResourcePropertiesPane, MapPropertiesPane
//{
//	public ApplicationContext aContext;
//	
//	MapCablePathBindPanel bPanel = new MapCablePathBindPanel();
//
//	CablePath path;
//
//	private LogicalNetLayer lnl;
//
//	private static MapCablePathPane instance = new MapCablePathPane();
//
//	private MapCablePathPane()
//	{
//		super();
//		try
//		{
//			jbInit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	public static ObjectResourcePropertiesPane getInstance()
//	{
//		return instance;
//	}
//
//	public MapCablePathPane(CablePath path)
//	{
//		this();
//		setObject(path);
//	}
//	
//	public void setLogicalNetLayer(LogicalNetLayer lnl)
//	{
//		this.lnl = lnl;
//		this.bPanel.setLogicalNetLayer(lnl);
//	}
//
//	public LogicalNetLayer getLogicalNetLayer()
//	{
//		return this.lnl;
//	}
//
//	private void jbInit()
//	{
//		this.setLayout(new BorderLayout());
//		this.add(this.bPanel, BorderLayout.CENTER);
//
//	}
//
//	public Object getObject()
//	{
//		return this.path;
//	}
//
//	public void setObject(Object or)
//	{
//		this.path = (CablePath)or;
//
//		this.bPanel.setObject(this.path);
//	}
//
//	public void setContext(ApplicationContext aContext)
//	{
//		this.aContext = aContext;
//		this.bPanel.setContext(aContext);
//	}
//
//	public void showBindPanel()
//	{
//	}
//
//	public boolean modify()
//	{
//		if(this.bPanel.modify())
//		{
//			return true;
//		}
//		return false;
//	}
//
//	public boolean cancel()
//	{
//		this.bPanel.cancel();
//		return true;
//	}
//
//	public boolean save()
//	{
//		return false;
//	}
//
//	public boolean open()
//	{
//		return false;
//	}
//
//	public boolean delete()
//	{
//		return false;
//	}
//
//	public boolean create()
//	{
//		return false;
//	}
//}

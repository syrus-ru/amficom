package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JList;

import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.CablePortCell;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Scheme.DefaultCableLink;
import com.syrus.AMFICOM.Client.General.Scheme.DefaultLink;
import com.syrus.AMFICOM.Client.General.Scheme.PortCell;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableThread;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class ElementsListFrame extends JInternalFrame implements OperationListener
{
	Dispatcher dispatcher;
	public static final int CREATING_PATH = 1;
	public static final int NORMAL = 0;

//	boolean editable_property = false;
	boolean can_be_editable = true;
	ApplicationContext aContext;

	int mode = NORMAL;
	PathPropsPanel pathpanel;

	public ElementsListFrame(ApplicationContext aContext, boolean can_be_editable)
	{
		this.aContext = aContext;
		this.can_be_editable = can_be_editable;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		init_module(aContext.getDispatcher());
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelSchematics.getString("elementsListTitle"));
		this.setMinimumSize(new Dimension(200, 150));
		showNoSelection();
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, CreatePathEvent.typ);
		dispatcher.register(this, SchemeNavigateEvent.type);
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(CreatePathEvent.typ))
		{
			CreatePathEvent ev = (CreatePathEvent)ae;
			if (ev.CREATE_PATH)
			{
				SchemePanel panel = (SchemePanel)ae.getSource();
				showPathCharacteristics(panel.getGraph().currentPath, true);
				mode = CREATING_PATH;
				pathpanel.setEditable(true);
			}
			if (ev.EDIT_PATH)
			{
				SchemePanel panel = (SchemePanel)ae.getSource();
				showPathCharacteristics(panel.getGraph().currentPath, true);
				mode = CREATING_PATH;
				pathpanel.setEditable(true);
			}
			if (ev.CANCEL_PATH_CREATION)
			{
				if (mode == CREATING_PATH)
				{
					mode = NORMAL;
					pathpanel.undo();
					pathpanel = null;
					showNoSelection();
				}
			}
			if (ev.SAVE_PATH)
			{
				if (mode == CREATING_PATH)
				{
					//pathpanel.savePath(((SchemeGraph)ev.getSource()).scheme);
					mode = NORMAL;
					pathpanel = null;
					showNoSelection();
				}
			}
			if (ev.ADD_LINK)
			{
				if (mode == CREATING_PATH)
				{
//					if (!pathpanel.links_to_add.isEmpty())
//						pathpanel.addSelectedLinks();

		 Scheme scheme = (Scheme)Pool.get("currentscheme", "currentscheme");
		 PathBuilder.addLink(pathpanel.path.links, (SchemeLink)pathpanel.links_to_add.get(0));
			boolean res = PathBuilder.explore(scheme, pathpanel.path);
			System.out.println("RES after add = " + res);

			dispatcher.notify(new SchemeNavigateEvent(
					new SchemePath[] {pathpanel.path},
					SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT));

//          else if (pathpanel.setting_obj instanceof Scheme)
//					{
//						Scheme scheme = (Scheme)pathpanel.setting_obj;
//						if (scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
//						{
//
//						}
//					}
				}
			}
			if (ev.REMOVE_LINK)
			{
				if (mode == CREATING_PATH)
				{
					pathpanel.removeLink();
				}
			}
			if (ev.UPDATE_LINK)
			{
				if (mode == CREATING_PATH)
				{
					pathpanel.updateLink();
				}
			}
			if (ev.SET_START)
			{
				if (mode == CREATING_PATH)
				{
					if (pathpanel.device_to_add != null)
					{
						String id = pathpanel.device_to_add.getId();
						pathpanel.setStartDevice(id);

					Scheme scheme = (Scheme)Pool.get("currentscheme", "currentscheme");
						boolean res = PathBuilder.explore(scheme, pathpanel.path);
//					PathBuilder.addSchemeElement(pathpanel.path.links,
//																			 (SchemeElement)Pool.get(SchemeElement.typ, id));
						System.out.println(res);
					}
				}
			}
			if (ev.SET_END)
			{
				if (mode == CREATING_PATH)
				{
					if (pathpanel.device_to_add != null)
						pathpanel.setEndDevice(pathpanel.device_to_add.getId());

//					Scheme scheme = (Scheme)Pool.get("currentscheme", "currentscheme");
//					PathBuilder.explore(scheme, pathpanel.path);
				}
			}
			if (ev.DELETE_PATH)
			{

			}
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;

			if (mode == CREATING_PATH)
			{
				Object[] objs = (Object[])ae.getSource();
				if (objs.length == 1 && objs[0] instanceof SchemeElement)
					pathpanel.device_to_add = (SchemeElement)objs[0];
				else
					pathpanel.device_to_add = null;

				for (int i = 0; i < objs.length; i++)
				{
					if (objs[i] instanceof SchemeLink || objs[i] instanceof SchemeCableLink)
					{
						if (!pathpanel.links_to_add.contains(objs[i]))
							pathpanel.links_to_add.add(objs[i]);
					}
					else
					{
						pathpanel.links_to_add = new ArrayList();
						break;
					}
				}

				if (ev.SCHEME_LINK_SELECTED)
				{
					Object[] links = (Object[])ev.getSource();
					if (links.length == 1)
						pathpanel.selectLink(((SchemeLink[])links)[0].getId());
				}
				if (ev.SCHEME_CABLE_LINK_SELECTED)
				{
					Object[] links = (Object[])ev.getSource();
					if (links.length == 1)
						pathpanel.selectLink(((SchemeCableLink[])links)[0].getId());
				}
				return;
			}

			if (ev.SCHEME_ALL_DESELECTED)
			{
				showNoSelection();
			}
			if (ev.SCHEME_ELEMENT_SELECTED)
			{
				SchemeElement element = (SchemeElement)((Object[])ev.getSource())[0];
				if (!element.scheme_id.equals(""))
				{
					Scheme sc = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
					if (sc == null)
						showNoSelection();
					else
						showSchemeCharacteristics(sc, can_be_editable ); //&& ev.isEditable);
				}
				else
					showSchemeElementCharacteristics(element, can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				showProtoCharacteristics((ProtoElement)((Object[])ev.getSource())[0], can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_SELECTED)
			{
				Scheme scheme = ((Scheme[])ev.getSource())[0];
				if (scheme == null)
					showNoSelection();
				else
					showSchemeCharacteristics(scheme, can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_PORT_SELECTED)
			{
				showPortsCharacteristics((SchemePort[])ev.getSource(), can_be_editable );//&& ev.isEditable
			}
			if (ev.SCHEME_CABLE_PORT_SELECTED)
			{
				showCablePortsCharacteristics((SchemeCablePort[])ev.getSource(), can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_LINK_SELECTED)
			{
				Object[] links = (Object[])ev.getSource();
				showLinksCharacteristics((SchemeLink[])links, can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_CABLE_LINK_SELECTED)
			{
				Object[] links = (Object[])ev.getSource();
				showCableLinksCharacteristics((SchemeCableLink[])links, can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_PATH_SELECTED)
			{
				showPathCharacteristics((SchemePath)((Object[])ev.getSource())[0], ev.isEditable);
			}
		}
		else if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (mode == CREATING_PATH)
					return;
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass() == null)
				return;
			if (ev.getDataClass().equals(ProtoElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					ProtoElement proto = (ProtoElement)ev.getDataSet().get(ev.getSelectionNumber());
					showProtoCharacteristics(proto, false);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(MapProtoElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					MapProtoElement map_proto = (MapProtoElement)ev.getDataSet().get(ev.getSelectionNumber());
					showGroupCharacteristics(map_proto, false);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(SchemeElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					SchemeElement element = (SchemeElement)ev.getDataSet().get(ev.getSelectionNumber());
					showSchemeElementCharacteristics(element, false);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(Scheme.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					Scheme scheme = (Scheme)ev.getDataSet().get(ev.getSelectionNumber());
					showSchemeCharacteristics(scheme, false);
				}
				else
					showNoSelection();
			}
			else
				showNoSelection();
		}
	}

	void showPathCharacteristics(SchemePath path, boolean isEditable)
	{
		if (path == null)
			return;
		this.getContentPane().removeAll();
		pathpanel = new PathPropsPanel(aContext);
		pathpanel.setEditable(isEditable);
		this.getContentPane().add(pathpanel, BorderLayout.CENTER);
		pathpanel.init(path, aContext.getDataSourceInterface());
		pathpanel.setEditable(false);
	}

	void showPortsCharacteristics (SchemePort[] ports, boolean isEditable)
	{
		this.getContentPane().removeAll();
		PortPropsPanel panel = new PortPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(ports);
	}

	void showCablePortsCharacteristics (SchemeCablePort[] ports, boolean isEditable)
	{
		this.getContentPane().removeAll();
		CablePortPropsPanel panel = new CablePortPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(ports);
	}

	void showProtoCharacteristics (ProtoElement proto, boolean isEditable)
	{
		this.getContentPane().removeAll();
		ProtoElementPropsPanel panel = new ProtoElementPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(proto, aContext.getDataSourceInterface(), false);
	}

	void showSchemeElementCharacteristics(SchemeElement element, boolean isEditable)
	{
		this.getContentPane().removeAll();
		SchemeElementPropsPanel panel = new SchemeElementPropsPanel(aContext);
//		if (element.scheme_id.equals(""))
//			panel.setEditable(false);
//		else
			panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(element, aContext.getDataSourceInterface(), false);
	}

	void showSchemeCharacteristics(Scheme scheme, boolean isEditable)
	{
		this.getContentPane().removeAll();
		SchemePropsPanel panel = new SchemePropsPanel(aContext, aContext.getDispatcher());
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(scheme);
	}

	void showGroupCharacteristics (MapProtoElement mapproto, boolean isEditable)
	{
		this.getContentPane().removeAll();
		MapProtoElementPropsPanel panel = new MapProtoElementPropsPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setEditable(isEditable);
		panel.init(mapproto, aContext.getDataSourceInterface());
		panel.setVisible(true);
	}

	void showLinksCharacteristics (SchemeLink[] links, boolean isEditable)
	{
		this.getContentPane().removeAll();
		LinkPropsPanel panel = new LinkPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(links);
	}

	void showCableLinksCharacteristics (SchemeCableLink[] links, boolean isEditable)
	{
		this.getContentPane().removeAll();
		CableLinkPropsPanel panel = new CableLinkPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(links);
	}

	void showMultipleSelection(Object[] objs, boolean isEditable)
	{
		if (objs[0] instanceof PortCell)
		{
			SchemePort[] ports = new SchemePort[objs.length];
			int counter = 0;
			for (int i = 0; i < objs.length; i++)
				if (objs[i] instanceof PortCell)
				{
					counter++;
					ports[i] = ((PortCell)objs[i]).getSchemePort();
				}
			if (counter == objs.length)
			{
				showPortsCharacteristics(ports, isEditable);
				return;
			}
		}
		else if (objs[0] instanceof CablePortCell)
		{
			SchemeCablePort[] ports = new SchemeCablePort[objs.length];
			int counter = 0;
			for (int i = 0; i < objs.length; i++)
				if (objs[i] instanceof CablePortCell)
				{
					counter++;
					ports[i] = ((CablePortCell)objs[i]).getSchemeCablePort();
				}
			if (counter == objs.length)
			{
				showCablePortsCharacteristics(ports, isEditable);
				return;
			}
		}
	/*	else if (objs[0] instanceof DefaultLink || objs[0] instanceof DefaultCableLink)
		{
			SchemePath path = new SchemePath("");
			int counter = 0;
			if (objs[0] instanceof DefaultLink)
				path = ((DefaultLink)objs[0]).getSchemePath();
			else if (objs[0] instanceof DefaultCableLink)
				path = ((DefaultCableLink)objs[0]).getSchemePath();

			ArrayList links = new ArrayList();
			for (int i = 0; i < path.links.size(); i++)
				links.add(((PathElement)path.links.get(i)).link_id);

			for (int i = 0; i < objs.length; i++)
			{
				if (objs[i] instanceof DefaultLink)
				{
					if (links.contains(((DefaultLink)objs[i]).getSchemeLinkId()))
						counter++;
				}
				else if (objs[i] instanceof DefaultCableLink)
				{
					if (links.contains(((DefaultCableLink)objs[i]).getSchemeCableLinkId()))
						counter++;
				}
			}
			if (counter == objs.length)
			{
				showPathCharacteristics(path, isEditable);
				return;
			}
		}*/
		if (objs[0] instanceof DefaultLink)
		{
			SchemeLink[] links = new SchemeLink[objs.length];
			int counter = 0;
			for (int i = 0; i < objs.length; i++)
				if (objs[i] instanceof DefaultLink)
				{
					counter++;
					links[i] = ((DefaultLink)objs[i]).getSchemeLink();
				}
			if (counter == objs.length)
			{
				showLinksCharacteristics(links, isEditable);
				return;
			}
		}
		else if (objs[0] instanceof DefaultCableLink)
		{
			SchemeCableLink[] links = new SchemeCableLink[objs.length];
			int counter = 0;
			for (int i = 0; i < objs.length; i++)
				if (objs[i] instanceof DefaultCableLink)
				{
					counter++;
					links[i] = ((DefaultCableLink)objs[i]).getSchemeCableLink();
				}
			if (counter == objs.length)
			{
				showCableLinksCharacteristics(links, isEditable);
				return;
			}
		}
		showNoSelection();
	}

	void showNoSelection()
	{
		this.getContentPane().removeAll();
		JList emptyList = new JList();
		emptyList.setBorder(BorderFactory.createLoweredBevelBorder());
		this.getContentPane().add(emptyList, BorderLayout.CENTER);
		updateUI();
	}
}


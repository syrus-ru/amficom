package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.general.*;

public class ElementsListFrame extends JInternalFrame implements OperationListener
{
	Dispatcher dispatcher;

//	boolean editable_property = false;
	boolean can_be_editable = true;
	ApplicationContext aContext;

	PathPropsPanel pathpanel;
	SchemePanel mainPathPanel;

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
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
				mainPathPanel = (SchemePanel)ae.getSource();
				showPathCharacteristics(mainPathPanel.getGraph().getCurrentPath(), true);
				SchemeGraph.path_creation_mode = Constants.CREATING_PATH_MODE;
				pathpanel.setEditable(true);
			}
			if (ev.EDIT_PATH)
			{
				mainPathPanel = (SchemePanel)ae.getSource();
				showPathCharacteristics(mainPathPanel.getGraph().getCurrentPath(), true);
				SchemeGraph.path_creation_mode = Constants.CREATING_PATH_MODE;
				pathpanel.setEditable(true);
			}
			if (ev.CANCEL_PATH_CREATION)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{
					SchemeGraph.path_creation_mode = Constants.NORMAL;
					pathpanel.undo();
					pathpanel = null;
					mainPathPanel = null;
					showNoSelection();
				}
			}
			if (ev.SAVE_PATH)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{

					SchemePath path = pathpanel.path;
					if (!MiscUtil.validName(path.getName()) ||
							path.endDevice() == null ||
							path.startDevice() == null ||
							path.links().length == 0)
					{
						String err = new String();
						err += "������������ ������ ��� ���������� ��������.\n";
						err += "���������� ������ ��������, ��������� �\n";
						err += "�������� ���������� � ���� �� ���� �����.";
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), err, "������",
								JOptionPane.OK_OPTION);
						return;
					}

					try {
						SchemeStorableObjectPool.putStorableObject(path);
					}
					catch (IllegalObjectEntityException ex) {
						ex.printStackTrace();
					}

					mainPathPanel.insertPathToScheme(path);
					mainPathPanel.getGraph().setGraphChanged(true);
					SchemeGraph.path_creation_mode = Constants.NORMAL;
					pathpanel = null;
					mainPathPanel = null;
					showNoSelection();
				}
			}
			if (ev.ADD_LINK)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{
					if (pathpanel.element_to_add != null)
						pathpanel.addSelectedElements();
				}
			}
			if (ev.REMOVE_LINK)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{
					pathpanel.removeLink();
				}
			}
			if (ev.SET_START)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{
					pathpanel.setStartDevice();
				}
			}
			if (ev.SET_END)
			{
				if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
				{
					pathpanel.setEndDevice();
				}
			}
			if (ev.DELETE_PATH)
			{

			}
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;

			if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
			{
				Object[] objs = (Object[])ae.getSource();

				if (objs.length > 0)
				{
					if (objs[0] instanceof SchemeLink || objs[0] instanceof SchemeCableLink || objs[0] instanceof SchemeElement)
						pathpanel.element_to_add = objs[0];
					else
						pathpanel.element_to_add = null;
				}

				if (ev.SCHEME_LINK_SELECTED)
				{
					Object[] links = (Object[])ev.getSource();
					if (links.length == 1)
						pathpanel.setSelectedElement(((SchemeLink[])links)[0]);
				}
				if (ev.SCHEME_CABLE_LINK_SELECTED)
				{
					Object[] links = (Object[])ev.getSource();
					if (links.length == 1)
						pathpanel.setSelectedElement(((SchemeCableLink[])links)[0]);
				}
				if (ev.SCHEME_PATH_SELECTED)
				{
					pathpanel.updatePathElements();
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
				if (element.getScheme() != null)
					showSchemeCharacteristics(element.getScheme(), can_be_editable); //&& ev.isEditable);
				else
					showSchemeElementCharacteristics(element, can_be_editable ); //&& ev.isEditable);
			}
			if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				showProtoCharacteristics((SchemeProtoElement)((Object[])ev.getSource())[0], can_be_editable ); //&& ev.isEditable);
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
			if (ev.SCHEME_DEVICE_SELECTED)
			{
				showDeviceCharacteristics((SchemeDevice)((Object[])ev.getSource())[0], ev.isEditable);
			}
		}
		else if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
					return;
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass() == null)
				return;
			if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
			{
				Class cl = ev.getDataClass();
				java.util.List ds = ev.getList();
				if (ev.getSelectionNumber() != -1)
				{
					if (cl.equals(SchemePath.class))
					{
						SchemePath path = (SchemePath)ds.get(ev.getSelectionNumber());
						showPathCharacteristics(path, false);
					}
					else if (cl.equals(SchemeElement.class))
					{
						SchemeElement se = (SchemeElement)ds.get(ev.getSelectionNumber());
						showSchemeElementCharacteristics(se, false);
					}
					else if (cl.equals(SchemeLink.class))
					{
						SchemeLink l = (SchemeLink)ds.get(ev.getSelectionNumber());
						showLinksCharacteristics(new SchemeLink[] {l}, false);
					}
					else if (cl.equals(SchemeCableLink.class))
					{
						SchemeCableLink l = (SchemeCableLink)ds.get(ev.getSelectionNumber());
						showCableLinksCharacteristics(new SchemeCableLink[] {l}, false);
					}
					else if (ev.getDataClass().equals(SchemeProtoElement.class))
					{
						SchemeProtoElement proto = (SchemeProtoElement)ev.getList().get(ev.getSelectionNumber());
						showProtoCharacteristics(proto, false);
					}
					else if (ev.getDataClass().equals(SchemeProtoGroup.class))
					{
						SchemeProtoGroup map_proto = (SchemeProtoGroup)ev.getList().get(ev.getSelectionNumber());
						showGroupCharacteristics(map_proto, false);
					}
					else if (ev.getDataClass().equals(Scheme.class))
					{
						Scheme scheme = (Scheme)ev.getList().get(ev.getSelectionNumber());
						showSchemeCharacteristics(scheme, false);
					}
					else
						showNoSelection();
				}
			}
		}
	}

	void showDeviceCharacteristics(SchemeDevice dev, boolean isEditable)
	{
		if (dev == null)
			return;
		if (dev.getSchemePortsAsArray().length == 0 || dev.getSchemeCablePortsAsArray().length == 0)
		{
			showNoSelection();
			return;
		}
		this.getContentPane().removeAll();
		DevicePropsPanel devpanel = new DevicePropsPanel(aContext);
		devpanel.setEditable(isEditable);
		this.getContentPane().add(devpanel, BorderLayout.CENTER);
		devpanel.init(dev, isEditable);
	}

	void showPathCharacteristics(SchemePath path, boolean isEditable)
	{
		if (path == null)
			return;
		this.getContentPane().removeAll();
		pathpanel = new PathPropsPanel(aContext);
		pathpanel.setEditable(isEditable);
		this.getContentPane().add(pathpanel, BorderLayout.CENTER);
		pathpanel.init(path);
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

	void showProtoCharacteristics (SchemeProtoElement proto, boolean isEditable)
	{
		this.getContentPane().removeAll();
		ProtoElementPropsPanel panel = new ProtoElementPropsPanel(aContext);
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(proto, false);
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
		panel.init(element, false);
	}

	void showSchemeCharacteristics(Scheme scheme, boolean isEditable)
	{
		this.getContentPane().removeAll();
		SchemePropsPanel panel = new SchemePropsPanel(aContext, aContext.getDispatcher());
		panel.setEditable(isEditable);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.init(scheme);
	}

	void showGroupCharacteristics (SchemeProtoGroup mapproto, boolean isEditable)
	{
		this.getContentPane().removeAll();
		SchemeProtoGroupPropsPanel panel = new SchemeProtoGroupPropsPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setEditable(isEditable);
		panel.init(mapproto);
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


/*
 * $Id: CreateTopLevelElementAction.java,v 1.4 2005/05/18 14:59:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;


/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/18 14:59:44 $
 * @module schemeclient_v1
 */
/*
public class CreateTopLevelElementAction extends AbstractAction {
	UgoTabbedPane sourcePane;
	UgoTabbedPane targetPane;
	ApplicationContext aContext;
	private int counter = 1;

	public CreateTopLevelElementAction(UgoTabbedPane sourcePane, UgoTabbedPane targetPane, ApplicationContext aContext) {
		super(com.syrus.AMFICOM.client_.scheme.graph.Constants.createTopLevelElementKey);
		this.sourcePane = sourcePane;
		this.targetPane = targetPane;
		this.aContext = aContext;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = sourcePane.getGraph();
		Object[] cells = graph.getAll();
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);

		if (groups.length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					Constants.ERROR_COMPONENT_NOT_FOUND, Constants.ERROR, JOptionPane.OK_OPTION);
			return;
		}
		List blockports_in = new ArrayList();
		List blockports_out = new ArrayList();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++) {
			if (bpcs[i].isCablePort()) {
				SchemeCablePort port = bpcs[i].getSchemeCablePort();
				if (port.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
					blockports_in.add(bpcs[i]);
				else
					blockports_out.add(bpcs[i]);
			} else {
				SchemePort port = bpcs[i].getSchemePort();
				if (port.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
					blockports_in.add(bpcs[i]);
				else
					blockports_out.add(bpcs[i]);
			}
		}
		if (blockports_in.size() == 0 && blockports_out.size() == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					Constants.ERROR_HIERARCHY_PORT_NOT_FOUND, Constants.ERROR,
					JOptionPane.OK_OPTION);
			return;
		}

		Identifier userId = new Identifier(((RISDSessionInfo)this.aContext.getSessionInterface()).getAccessIdentifier().user_id);
		SchemeProtoElement proto;
		if (groups.length == 1) {
			proto = groups[0].getProtoElement();
		}
		else {
			proto = targetPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement();
			if (proto == null) {
				try {
					proto = SchemeProtoElement.createInstance(userId, Constants.COMPONENT + counter++);
				} catch (CreateObjectException e1) {
					Log.errorException(e1);
					return;
				}
			}
		}
		SchemeDevice device;
		if (proto.getSchemeDevices().isEmpty()) {
			try {
				device = SchemeDevice.createInstance(userId, Constants.DEVICE + System.currentTimeMillis());
			} catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
		} 
		else {
			device = (SchemeDevice) proto.getSchemeDevices().iterator().next();
		}
		//	set child protoElements
		Set childProtos = new HashSet(groups.length);
		for (int i = 0; i < groups.length; i++)
			childProtos.add(groups[i].getProtoElement());
		proto.setSchemeProtoElements(childProtos);
		//	set child schemeLinks
		DefaultLink[] links = GraphActions.findTopLevelLinks(graph, cells);
		Set childLinks = new HashSet(links.length);
		for (int i = 0; i < links.length; i++)
			childLinks.add(links[i].getSchemeLink());
		proto.setSchemeLinks(childLinks);
		//	set child schemeDevices
		Set childDevices = new HashSet(1);
		childDevices.add(device);
		proto.setSchemeDevices(childDevices);

		targetPane.getCurrentPanel().getSchemeResource().setSchemeProtoElement(proto);
		
		ImageIcon icon = null; 
		if (groups.length == 1) {
			BitmapImageResource ir = proto.getSymbol();
			if (ir != null) {
				icon = new ImageIcon(ir.getImage());
				if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
					icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20,
							Image.SCALE_SMOOTH));
			}
		}
		String label = (groups.length == 1) ? proto.getLabel() : ""; //$NON-NLS-1$
		
		CreateUgo.create(targetPane.getGraph(), icon, label, device, blockports_in, blockports_out);
		CreateGroup action = new CreateGroup(targetPane);
		action.actionPerformed(null);	

		device.setSchemePorts(action.getChildSchemePorts());
		device.setSchemeCablePorts(action.getChildSchemeCablePorts());
	}
}
*/
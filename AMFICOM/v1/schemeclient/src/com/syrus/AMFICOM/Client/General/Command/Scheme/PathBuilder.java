package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

public class PathBuilder {
	private static final int OK = 0;
	private static final int MULTIPLE_PORTS = 1;
	private static int state = OK;

	private PathBuilder() {
		// empty
	}
	
	private static boolean exploreSchemeElement(SchemePath path, SchemeElement scheme_element) throws ApplicationException {
		if (path.getPathMembers().isEmpty()) {
			assert Log.debugMessage("Can not explore path with no starting element", Level.FINER);
			return false;
		}
		
		while(true) {
			if (!exploreNext(path)) {
				assert Log.debugMessage("Can not explore next element", Level.FINER);
				return false;
			}
			
			PathElement lastPE = path.getPathMembers().last();
			
		
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				SchemeElement top = SchemeUtils.getTopLevelSchemeElement(se);
				if (!top.equals(scheme_element)) {
					path.removePathMember(lastPE, false);
					return true;
				}
				if (lastPE.getEndAbstractSchemePort() == null) {
					if (state == PathBuilder.MULTIPLE_PORTS)
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.path.unambigous")  //$NON-NLS-1$
								+ "\n'" + lastPE.getName() + "'\n" +  //$NON-NLS-1$//$NON-NLS-2$
								LangModelScheme.getString("Message.error.path.next_manually"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"), //$NON-NLS-1$
								JOptionPane.OK_OPTION);
					state = OK;
					return false;
				}
			}
		}
	}

	private static boolean exploreScheme(SchemePath path, Scheme scheme) throws ApplicationException {
		if (path.getPathMembers().isEmpty()) {
			assert Log.debugMessage("Can not explore path with no starting element", Level.FINER);
			return false;
		}
		
		while(true) {
			if (!exploreNext(path)) {
				assert Log.debugMessage("Can not explore next element", Level.FINER);
				return false;
			}
			
			PathElement lastPE = path.getPathMembers().last();
			
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				Scheme top = se.getParentScheme();
				if (!top.equals(scheme)) {
					path.removePathMember(lastPE, false);
					return true;
				}
				
				if (lastPE.getEndAbstractSchemePort() == null) {
					if (state == PathBuilder.MULTIPLE_PORTS)
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.path.unambigous")  //$NON-NLS-1$
								+ "\n'" + lastPE.getName() + "'\n" +  //$NON-NLS-1$//$NON-NLS-2$
								LangModelScheme.getString("Message.error.path.next_manually"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"), //$NON-NLS-1$
								JOptionPane.OK_OPTION);
					state = OK;
					return false;
				}
			}
		}
	}

	public static boolean explore(SchemePath path, Identifier endId) throws ApplicationException {
//		if (path.getPathMembers().isEmpty()) {
//			if (startId.getMajor() != ObjectEntities.SCHEMEELEMENT_CODE) {
//				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
//						"Путь должен начинаться с компонента", "Ошибка", JOptionPane.OK_OPTION);
//				return false;
//			}
//			try {
//				SchemeElement se = StorableObjectPool.getStorableObject(startId, true);
//				PathElement pe = createPEbySE(path, se);
//				if (pe == null)
//					return false;
//			} catch (ApplicationException e) {
//				Log.errorException(e);
//				return false;
//			}
//		}

		while(true) {
			PathElement lastPE = path.getPathMembers().last();
			if (lastPE.getAbstractSchemeElement().equals(endId)) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.information.path_created"),  //$NON-NLS-1$
						LangModelScheme.getString("Message.information"), //$NON-NLS-1$
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT && lastPE.getEndAbstractSchemePort() == null) {
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelScheme.getString("Message.error.path.unambigous")  //$NON-NLS-1$
							+ "\n'" + lastPE.getName() + "'\n" +  //$NON-NLS-1$//$NON-NLS-2$
							LangModelScheme.getString("Message.error.path.next_manually"), //$NON-NLS-1$
							LangModelScheme.getString("Message.error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
				state = OK;
				return false;
			}

			if (!exploreNext(path)) {
				assert Log.debugMessage("Can not explore next element", Level.FINER);
				return false;
			}
		}
	}
	
	private static boolean exploreNext(SchemePath path) throws ApplicationException {
		PathElement lastPE = path.getPathMembers().last();
		
		if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
			PathElement newPE = null;
//			SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
			AbstractSchemePort port = lastPE.getEndAbstractSchemePort();
			if (port instanceof SchemePort) {
				SchemeLink link = ((SchemePort)port).getAbstractSchemeLink();
				if (link == null) {
					SchemeElement se = port.getParentSchemeDevice().getParentSchemeElement();
					SchemeElement parentSchemeElement = se;
					Scheme parentScheme = null;
					while (parentScheme == null) {
						parentScheme = se.getParentScheme();
						if (parentScheme == null) {
							se = se.getParentSchemeElement();
						}
					}
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelScheme.getString("Message.error.path.next_element_1")  //$NON-NLS-1$
							+ " '" + port.getName() + "' "  //$NON-NLS-1$//$NON-NLS-2$
							+ LangModelScheme.getString("Message.error.path.next_element_2") //$NON-NLS-1$
							+ " '" + parentSchemeElement.getName() + "' "  //$NON-NLS-1$ //$NON-NLS-2$
							+ LangModelScheme.getString("Message.error.path.next_element_3") //$NON-NLS-1$
							+ " '" + parentScheme.getName() + "'\n"  //$NON-NLS-1$ //$NON-NLS-2$
							+ LangModelScheme.getString("Message.error.path.unable_continue"), //$NON-NLS-1$
							LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return false;
				}
				newPE = createPEbySL(path, link);
			} else if (port instanceof SchemeCablePort) {
				SchemeCablePort cport = (SchemeCablePort)port;
				SchemeCableLink clink = cport.getAbstractSchemeLink();
				if (clink == null) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelScheme.getString("Message.error.path.next_element_1")  //$NON-NLS-1$
							+ " '" + port.getName() + "' "  //$NON-NLS-1$//$NON-NLS-2$
							+ LangModelScheme.getString("Message.error.path.next_element_2") //$NON-NLS-1$
							+ " '" + port.getParentSchemeDevice().getParentSchemeElement().getName() + "'\n"  //$NON-NLS-1$ //$NON-NLS-2$
							+ LangModelScheme.getString("Message.error.path.unable_continue"), //$NON-NLS-1$
							LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return false;
				}
				newPE = createPEbySCL(path, clink);
			}
			if (newPE == null)
				return false;
		} else  {
			PathElement beforeLastPE = path.getPreviousPathElement(lastPE);
			if (beforeLastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement seBeforeLast = beforeLastPE.getSchemeElement();

				SchemeElement seToAdd = null;
				if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
					SchemeLink link = lastPE.getSchemeLink();
					if (link.getTargetAbstractSchemePort() != null) {
						SchemeElement endSE = link.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!endSE.equals(seBeforeLast))
							seToAdd = endSE;
					}
					if (seToAdd == null && link.getSourceAbstractSchemePort() != null) {
						SchemeElement startSE = link.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!startSE.equals(seBeforeLast))
							seToAdd = startSE;
					}
				} else if (lastPE.getKind() == IdlKind.SCHEME_CABLE_LINK) {
					SchemeCableLink link = lastPE.getSchemeCableLink();
					if (link.getTargetAbstractSchemePort() != null) {
						SchemeElement endSE = link.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!endSE.equals(seBeforeLast))
							seToAdd = endSE;
					}
					if (seToAdd == null && link.getSourceAbstractSchemePort() != null) {
						SchemeElement startSE = link.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!startSE.equals(seBeforeLast))
							seToAdd = startSE;
					}
				}
				if (seToAdd == null) {
					assert Log.debugMessage("Can not find SE to add", Level.FINER);
					return false;
				}
				PathElement newPE = createPEbySE(path, seToAdd);
				if (newPE == null)
					return false;
			}
		}
		return true;
	}

	public static PathElement createPEbySE(SchemePath path, SchemeElement se) throws ApplicationException {
		PathElement newPE = null;
		
		if (!path.getPathMembers().isEmpty()) {  // non fisrt element
			if (se.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
				Scheme scheme = se.getScheme(false);
				exploreScheme(path, scheme);
				return path.getPathMembers().last();
			} else if (!se.getSchemeElements(false).isEmpty()) {
				exploreSchemeElement(path, se);
				return path.getPathMembers().last();
			}

			PathElement lastPE = path.getPathMembers().last();

			// ищем общий порт
			AbstractSchemePort newStartPort = null;
			if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
				SchemeLink link = lastPE.getSchemeLink();
				SchemePort endPort = link.getTargetAbstractSchemePort();
				Set<SchemePort> sePorts = se.getSchemePortsRecursively(false);
				if (sePorts.contains(endPort)) {
					newStartPort = endPort;
				} else {
					SchemePort startPort = link.getSourceAbstractSchemePort();
					if (sePorts.contains(startPort)) {
						newStartPort = startPort;
					}
				}
			} else if (lastPE.getKind() == IdlKind.SCHEME_CABLE_LINK) {
				SchemeCableLink link = lastPE.getSchemeCableLink();
				SchemeCablePort endPort = link.getTargetAbstractSchemePort();
				Set<SchemeCablePort> sePorts = se.getSchemeCablePortsRecursively(false);
				if (sePorts.contains(endPort)) {
					newStartPort = endPort;
				} else {
					SchemeCablePort startPort = link.getSourceAbstractSchemePort();
					if (sePorts.contains(startPort)) {
						newStartPort = startPort;
					}
				}
			}
			
			//		нет общих портов
			if (newStartPort == null) {
				assert Log.debugMessage("No mutual ports found", Level.FINER);
				return null;
			}

			// searching for ports with opposite direction
			AbstractSchemePort newEndPort = null;
			SchemeDevice dev = newStartPort.getParentSchemeDevice();
			Set<SchemePort> ports = SchemeActions.findPorts(dev, newStartPort.getDirectionType() == IdlDirectionType._IN ? 
					IdlDirectionType._OUT : IdlDirectionType._IN);
			Set<SchemeCablePort> cports = SchemeActions.findCablePorts(dev, newStartPort.getDirectionType() == IdlDirectionType._IN ? 
							IdlDirectionType._OUT : IdlDirectionType._IN);
			
			// для предыдущего линка подходят варианты (для противоположный портов):
			// 1. 0 портов, 1 кабельный порт
			// 2. 0 портов, n кабельных портов
			// 3. 1 порт, 0 кабельных портов
			if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
				if (ports.size() == 0) {
					if (cports.size() == 1) { // 1st variant
						newEndPort = cports.iterator().next();
					} else { // 2nd variand - searching what thread start port routed with
						SchemeCableThread thread = ((SchemePort)newStartPort).getSchemeCableThread();
						if (thread != null) {
							SchemeCablePort port = getCablePortByThread(cports, thread);
							if (port != null) {
								newEndPort = port;
							}
						}
					}
				}
				// 3rd variant
				else if (ports.size() == 1 && cports.size() == 0) {
					newEndPort = ports.iterator().next();
				}
				else if (ports.size() > 1) { // else we couldn't go further
					state = MULTIPLE_PORTS;
				}
			} 
			// для предыдущего кабельного линка подходят варианты (для противоположный портов):
			//	1. 0 портов, 1 кабельный порт
			//	2. n портов
			else if (lastPE.getKind() == IdlKind.SCHEME_CABLE_LINK) {
				if (ports.size() == 0 && cports.size() == 1) { // 1st variant
					newEndPort = cports.iterator().next();
				} else if (ports.size() > 0 && cports.size() == 0) { // 2nd variant
					SchemePort port = lastPE.getSchemeCableThread().getSchemePort(dev);
					if (port != null) {
						newEndPort = port;
					}
				}
			}
			
			try {
				newPE = PathElement.createInstance(LoginManager.getUserId(), path, newStartPort, newEndPort);
			} 
			catch (CreateObjectException e) {
				assert Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
		} else {//first element
			// must be non scheme element
			if (se.getScheme(false) != null) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				LangModelScheme.getString("Message.error.path.starting_scheme"), 
				LangModelScheme.getString("Message.error"), 
				JOptionPane.OK_OPTION);
				return null;
			}

			// must be at least one access port
			int accessPorts = 0;
			SchemePort port = null;

			for (SchemePort p : se.getSchemePortsRecursively(false)) {
				MeasurementPort measurementPort = p.getMeasurementPort();
				if (measurementPort != null && measurementPort.getType() != null) {
					port = p;
					accessPorts++;
				}
			}
			if (accessPorts == 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "У начального устройства должен быть тестовый порт\nс которого должен начинаться маршрут тестирования", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}
			if (accessPorts == 1) {
				try {
					newPE = PathElement.createInstance(LoginManager.getUserId(), path, null, port);
				} 
				catch (CreateObjectException e) {
					assert Log.errorMessage("Can't create PathElement object " + e.getMessage());
					return null;
				}
			}
			else {
				// TODO create without end port
			}
		}
		return newPE;
	}

	public static PathElement createPEbySL(SchemePath path, SchemeLink link) throws ApplicationException {
		SortedSet<PathElement> pes = path.getPathMembers();
		if (!pes.isEmpty()) {
			PathElement lastPE = pes.last();
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				AbstractSchemePort lastEndPort = lastPE.getEndAbstractSchemePort();
				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (lastEndPort != null) {
					if (lastEndPort.equals(link.getSourceAbstractSchemePort()) ||
							lastEndPort.equals(link.getTargetAbstractSchemePort())) {
						return addLink(path, link);
					}
				} else { //в противном случае ищем по общему порту предыдущего эл-та и линка
					for (SchemePort port : se.getSchemePortsRecursively(false)) {
						if (port.equals(link.getSourceAbstractSchemePort()) ||
								port.equals(link.getTargetAbstractSchemePort())) {
							lastPE.setEndAbstractSchemePort(port);
							return addLink(path, link);
						}
					}
				}
			}
		}
		return null;
	}

	private static PathElement addLink(SchemePath path, SchemeLink link) {
		try {
			return PathElement.createInstance(LoginManager.getUserId(), path, link);
		} catch (CreateObjectException e) {
			assert Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
	}

	public static PathElement createPEbySCL(SchemePath path, SchemeCableLink link) throws ApplicationException {
		SortedSet<PathElement> pes = path.getPathMembers();
		if (!pes.isEmpty()) {
			PathElement lastPE = pes.last();
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				AbstractSchemePort lastEndPort = lastPE.getEndAbstractSchemePort();
				SchemeCablePort newStartPort = null;
				// если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (lastEndPort != null) {
					if (lastEndPort.equals(link.getSourceAbstractSchemePort()) ||
							lastEndPort.equals(link.getTargetAbstractSchemePort())) {
						newStartPort = (SchemeCablePort)lastEndPort; 
					}
				} else { //в противном случае ищем по общему порту предыдущего эл-та и линка
					for (SchemeCablePort port : se.getSchemeCablePortsRecursively(false)) {
						if (port.equals(link.getSourceAbstractSchemePort()) ||
								port.equals(link.getTargetAbstractSchemePort())) {
							lastPE.setEndAbstractSchemePort(port);
							newStartPort = port;
							break;
						}
					}
				}
				
				if (newStartPort != null) { // имеют общий порт
					AbstractSchemePort lastStartPort = lastPE.getStartAbstractSchemePort();
					if (lastStartPort instanceof SchemePort) {
						SchemeCableThread thread = ((SchemePort)lastStartPort).getSchemeCableThread();
						if (thread == null) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									LangModelScheme.getString("Message.error.path.commutation") //$NON-NLS-1$
									+ "\n'" + se.getName() + "'",   //$NON-NLS-1$//$NON-NLS-2$
									LangModelScheme.getString("Message.error"), //$NON-NLS-1$
									JOptionPane.OK_OPTION);
							return null;
						}
						if (!thread.getParentSchemeCableLink().equals(link)) {
							assert Log.debugMessage("Incorrect commutation at " + se.getName() + " - corresponds cable " + thread.getParentSchemeCableLink().getName(), Level.FINER);
							return null;
						}
						return addCableLink(path, thread);
					}
				}
			}
		}
		return null;
	}

	private static PathElement addCableLink(SchemePath path, SchemeCableThread thread) {
		if (thread == null) {
			PathElement pe;
			try {
				pe = path.getPathMembers().last();
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				LangModelScheme.getString("Message.error.path.commutation") //$NON-NLS-1$
							+ "\n'" + pe.getName() + "'",   //$NON-NLS-1$//$NON-NLS-2$
							LangModelScheme.getString("Message.error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
			} catch (ApplicationException e) {
				assert Log.errorMessage(e);
			}
			return null;
		}

		try {
			return PathElement.createInstance(LoginManager.getUserId(), path, thread);
		} 
		catch (CreateObjectException e) {
			assert Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
	}

	private static SchemeCablePort getCablePortByThread(Set cableports, SchemeCableThread thread) throws ApplicationException {
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.getAbstractSchemeLink() != null)
			{
				if (port.getAbstractSchemeLink().getSchemeCableThreads(false).contains(thread))
					return port;
			}
		}
		return null;
	}
}

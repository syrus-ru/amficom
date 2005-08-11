package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
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
import com.syrus.util.Log;

public class PathBuilder {
	private static final int OK = 0;
	private static final int MULTIPLE_PORTS = 1;
	private static int state = OK;

	private PathBuilder() {
		// empty
	}
	
	private static boolean exploreSchemeElement(SchemePath path, SchemeElement scheme_element) {
		if (path.getPathMembers().isEmpty()) {
			return false;
		}
		
		while(true) {
			if (!exploreNext(path)) {
				return false;
			}
			
			PathElement lastPE = path.getPathMembers().last();
			
		
			if (lastPE.getKind().value() == IdlKind._SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				SchemeElement top = SchemeUtils.getTopLevelScheemElement(se);
				if (!top.equals(scheme_element)) {
					path.removePathMember(lastPE, false);
					return true;
				}
				if (lastPE.getEndAbstractSchemePort() == null) {
					if (state == PathBuilder.MULTIPLE_PORTS)
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								"����� ������� " + lastPE.getName() +
								" ���������� ���������� �������� ����.\n����������, ������� ��������� ������� ���� �������.",
								"������", JOptionPane.OK_OPTION);
					state = OK;
					return false;
				}
			}
		}
	}

	private static boolean exploreScheme(SchemePath path, Scheme scheme) {
		if (path.getPathMembers().isEmpty()) {
			return false;
		}
		
		while(true) {
			if (!exploreNext(path)) {
				return false;
			}
			
			PathElement lastPE = path.getPathMembers().last();
			
			if (lastPE.getKind().value() == IdlKind._SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				Scheme top = se.getParentScheme();
				if (!top.equals(scheme)) {
					path.removePathMember(lastPE, false);
					return true;
				}
				
				if (lastPE.getEndAbstractSchemePort() == null) {
					if (state == PathBuilder.MULTIPLE_PORTS)
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								"����� ������� " + lastPE.getName() +
								" ���������� ���������� �������� ����.\n����������, ������� ��������� ������� ���� �������.",
								"������", JOptionPane.OK_OPTION);
					state = OK;
					return false;
				}
			}
		}
	}

	public static boolean explore(SchemePath path, Identifier startId, Identifier endId) {
		if (path.getPathMembers().isEmpty()) {
			if (startId.getMajor() != ObjectEntities.SCHEMEELEMENT_CODE) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"���� ������ ���������� � ����������", "������", JOptionPane.OK_OPTION);
				return false;
			}
			try {
				SchemeElement se = StorableObjectPool.getStorableObject(startId, true);
				PathElement pe = createPEbySE(path, se);
				if (pe == null)
					return false;
			} catch (ApplicationException e) {
				Log.errorException(e);
				return false;
			}
		}

		while(true) {
			PathElement lastPE = path.getPathMembers().last();
			if (lastPE.getAbstractSchemeElement().getId().equals(endId)) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"���������� ���� ������� ���������", "���������", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (lastPE.getKind().value() == IdlKind._SCHEME_ELEMENT && lastPE.getEndAbstractSchemePort() == null) {
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"����� ������� " + lastPE.getName() +
							" ���������� ���������� �������� ����.\n����������, ������� ��������� ������� ���� �������.",
							"������", JOptionPane.OK_OPTION);
				state = OK;
				return false;
			}

			if (!exploreNext(path))
				return false;
		}
	}
	
	private static boolean exploreNext(SchemePath path) {
		PathElement lastPE = path.getPathMembers().last();
		
		if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
			PathElement newPE = null;
//			SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
			AbstractSchemePort port = lastPE.getEndAbstractSchemePort();
			if (port instanceof SchemePort) {
				SchemeLink link = ((SchemePort)port).getAbstractSchemeLink();
				if (link == null)
					return false;
				newPE = createPEbySL(path, link);
			} else if (port instanceof SchemeCablePort) {
				SchemeCablePort cport = (SchemeCablePort)port;
				SchemeCableLink clink = cport.getAbstractSchemeLink();
				if (clink == null)
					return false;
				newPE = createPEbySCL(path, clink);
			}
			if (newPE == null)
				return false;
		} else  {
			PathElement beforeLastPE = path.getPreviousPathElement(lastPE);
			if (beforeLastPE.getKind().value() == IdlKind._SCHEME_ELEMENT) {
				SchemeElement seBeforeLast = beforeLastPE.getSchemeElement();

				SchemeElement seToAdd = null;
				if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
					SchemeLink link = lastPE.getSchemeLink();
					if (link.getTargetAbstractSchemePort() != null) {
						SchemeElement endSE = link.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!endSE.equals(seBeforeLast))
							seToAdd = endSE;
					}
					if (seToAdd != null && link.getSourceAbstractSchemePort() != null) {
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
					if (seToAdd != null && link.getSourceAbstractSchemePort() != null) {
						SchemeElement startSE = link.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						if (!startSE.equals(seBeforeLast))
							seToAdd = startSE;
					}
				}
				if (seToAdd == null) {
					return false;
				}
				PathElement newPE = createPEbySE(path, seToAdd);
				if (newPE == null)
					return false;
			}
		}
		return true;
	}

	public static PathElement createPEbySE(SchemePath path, SchemeElement se) {
		PathElement newPE = null;
		
		if (!path.getPathMembers().isEmpty()) {  // non fisrt element
			if (se.getScheme() != null) {
				Scheme scheme = se.getScheme();
				exploreScheme(path, scheme);
				return path.getPathMembers().last();
			} else if (!se.getSchemeElements().isEmpty()) {
				exploreSchemeElement(path, se);
				return path.getPathMembers().last();
			}

			PathElement lastPE = path.getPathMembers().last();

			// ���� ����� ����
			AbstractSchemePort newStartPort = null;
			if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
				SchemeLink link = lastPE.getSchemeLink();
				SchemePort endPort = link.getTargetAbstractSchemePort();
				Set<SchemePort> sePorts = se.getSchemePortsRecursively();
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
				Set<SchemeCablePort> sePorts = se.getSchemeCablePortsRecursively();
				if (sePorts.contains(endPort)) {
					newStartPort = endPort;
				} else {
					SchemeCablePort startPort = link.getSourceAbstractSchemePort();
					if (sePorts.contains(startPort)) {
						newStartPort = startPort;
					}
				}
			}
			
			//		��� ����� ������
			if (newStartPort == null) {
				return null;
			}

			// searching for ports with opposite direction
			AbstractSchemePort newEndPort = null;
			SchemeDevice dev = newStartPort.getParentSchemeDevice();
			List<SchemePort> ports = findPorts(dev, newStartPort.getDirectionType().equals(IdlDirectionType._IN) ? 
					IdlDirectionType._OUT : IdlDirectionType._IN);
			List<SchemeCablePort> cports = findCablePorts(dev, newStartPort.getDirectionType().equals(IdlDirectionType._IN) ? 
							IdlDirectionType._OUT : IdlDirectionType._IN);
			
			// ��� ����������� ����� �������� �������� (��� ��������������� ������):
			// 1. 0 ������, 1 ��������� ����
			// 2. 0 ������, n ��������� ������
			// 3. 1 ����, 0 ��������� ������
			if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
				if (ports.size() == 0) {
					if (cports.size() == 1) { // 1st variant
						newEndPort = cports.get(0);
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
					newEndPort = ports.get(0);
				}
				else if (ports.size() > 1) { // else we couldn't go further
					state = MULTIPLE_PORTS;
				}
			} 
			// ��� ����������� ���������� ����� �������� �������� (��� ��������������� ������):
			//	1. 0 ������, 1 ��������� ����
			//	2. n ������
			else if (lastPE.getKind() == IdlKind.SCHEME_LINK) {
				if (ports.size() == 0 && cports.size() == 1) { // 1st variant
					newEndPort = cports.get(0);
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
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
		} else {//first element
			// must be non scheme element
			if (se.getScheme() != null) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "��������� ����������� �� ����� ���� �����", "������", JOptionPane.OK_OPTION);
				return null;
			}

			// must be at least one access port
			int accessPorts = 0;
			SchemePort port = null;

			for (SchemePort p : se.getSchemePortsRecursively()) {
				MeasurementPort measurementPort = p.getMeasurementPort();
				if (measurementPort != null && measurementPort.getType() != null) {
					port = p;
					accessPorts++;
				}
			}
			if (accessPorts == 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "� ���������� ���������� ������ ���� �������� ����\n� �������� ������ ���������� ������� ������������", "������", JOptionPane.OK_OPTION);
				return null;
			}
			if (accessPorts == 1) {
				try {
					newPE = PathElement.createInstance(LoginManager.getUserId(), path, null, port);
				} 
				catch (CreateObjectException e) {
					Log.errorMessage("Can't create PathElement object " + e.getMessage());
					return null;
				}
			}
			else {
				// TODO create without end port
			}
		}
		return newPE;
	}

	public static PathElement createPEbySL(SchemePath path, SchemeLink link) {
		SortedSet<PathElement> pes = path.getPathMembers();
		if (!pes.isEmpty()) {
			PathElement lastPE = pes.last();
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				AbstractSchemePort lastEndPort = lastPE.getEndAbstractSchemePort();
				//���� � ����������� ��-�� ���������� endPortId, ���� �� ����
				if (lastEndPort != null) {
					if (lastEndPort.equals(link.getSourceAbstractSchemePort()) ||
							lastEndPort.equals(link.getTargetAbstractSchemePort())) {
						addLink(path, link);
					}
				} else { //� ��������� ������ ���� �� ������ ����� ����������� ��-�� � �����
					for (SchemePort port : se.getSchemePortsRecursively()) {
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
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
	}

	public static PathElement createPEbySCL(SchemePath path, SchemeCableLink link) {
		SortedSet<PathElement> pes = path.getPathMembers();
		if (!pes.isEmpty()) {
			PathElement lastPE = pes.last();
			if (lastPE.getKind() == IdlKind.SCHEME_ELEMENT) {
				SchemeElement se = lastPE.getSchemeElement();
				AbstractSchemePort lastEndPort = lastPE.getEndAbstractSchemePort();
				SchemeCablePort newStartPort = null;
				// ���� � ����������� ��-�� ���������� endPortId, ���� �� ����
				if (lastEndPort != null) {
					if (lastEndPort.equals(link.getSourceAbstractSchemePort()) ||
							lastEndPort.equals(link.getTargetAbstractSchemePort())) {
						newStartPort = (SchemeCablePort)lastEndPort; 
					}
				} else { //� ��������� ������ ���� �� ������ ����� ����������� ��-�� � �����
					for (SchemeCablePort port : se.getSchemeCablePortsRecursively()) {
						if (port.equals(link.getSourceAbstractSchemePort()) ||
								port.equals(link.getTargetAbstractSchemePort())) {
							lastPE.setEndAbstractSchemePort(port);
							newStartPort = port;
							break;
						}
					}
				}
				
				if (newStartPort != null) { // ����� ����� ����
					AbstractSchemePort lastStartPort = lastPE.getStartAbstractSchemePort();
					if (lastStartPort instanceof SchemePort) {
						SchemeCableThread thread = ((SchemePort)lastStartPort).getSchemeCableThread();
						if (thread == null) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									"��������� ���������� � �������� " + se.getName(), "������", JOptionPane.OK_OPTION);
							return null;
						}
						if (!thread.getParentSchemeCableLink().equals(link)) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									"���������� � �������� " + se.getName() + "������������� ����������� ������ " + thread.getParentSchemeCableLink().getName(), "������", JOptionPane.OK_OPTION);
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
			PathElement pe = path.getPathMembers().last();
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"����������, ��������� ���������� � �������� " + pe.getName(),
							"������", JOptionPane.OK_OPTION);
			return null;
		}

		try {
			return PathElement.createInstance(LoginManager.getUserId(), path, thread);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
	}

	private static SchemeCablePort getCablePortByThread(List cableports, SchemeCableThread thread)
	{
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.getAbstractSchemeLink() != null)
			{
				if (port.getAbstractSchemeLink().getSchemeCableThreads().contains(thread))
					return port;
			}
		}
		return null;
	}


	private static List<SchemePort> findPorts(SchemeDevice dev, IdlDirectionType direction) {
		List<SchemePort> ports = new ArrayList<SchemePort>();
		for (Iterator it = dev.getSchemePorts().iterator(); it.hasNext();) {
			SchemePort p = (SchemePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}

	private static List<SchemeCablePort> findCablePorts(SchemeDevice dev, IdlDirectionType direction) {
		List<SchemeCablePort> ports = new ArrayList<SchemeCablePort>();
		for (Iterator it = dev.getSchemeCablePorts().iterator(); it.hasNext();) {
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}
}

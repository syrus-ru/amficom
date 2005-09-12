/*-
 * $Id: SchemeImportCommand.java,v 1.11 2005/09/12 14:09:54 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.configuration.ui.CableLinkTypeLayout;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateBlockPortAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateTopLevelSchemeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCableLinkLayout;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlConfigurationLibrary;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentSeq;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSeq;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlComplementor;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;

public class SchemeImportCommand extends AbstractCommand {
	private Map<Integer, SchemeProtoElement> straightMuffs;
	private Map<Integer, SchemeProtoElement> inVrms;
	private Map<Integer, SchemeProtoElement> outVrms;
	static final String USER_DIR = "user.dir";
	SchemeTabbedPane pane;
	private static boolean inited = false;
	
	private static void init() {
		XmlComplementorRegistry.registerComplementor(SCHEME_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject scheme,
					final String importType)
			throws CreateObjectException, UpdateObjectException {
				((XmlScheme) scheme).setDomainId(LoginManager.getDomainId().getXmlTransferable(importType));
			}
		});
		
		XmlComplementorRegistry.registerComplementor(EQUIPMENT_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject equipment,
					final String importType)
			throws CreateObjectException, UpdateObjectException {
				((XmlEquipment) equipment).setDomainId(LoginManager.getDomainId().getXmlTransferable(importType));
			}
		});
		
		final TypicalCondition condition1 = new TypicalCondition(PortTypeKind._PORT_KIND_CABLE,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PORT_TYPE_CODE,
				PortTypeWrapper.COLUMN_KIND);
		try {
			final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			if (!portTypes.isEmpty()) {
				final PortType portType = portTypes.iterator().next(); 
				XmlComplementorRegistry.registerComplementor(SCHEMECABLEPORT_CODE, new XmlComplementor() {
					public void complementStorableObject(
							final XmlStorableObject schemeCablePort,
							final String importType)
					throws CreateObjectException, UpdateObjectException {
						XmlIdentifier portTypeId = portType.getId().getXmlTransferable(importType);
						((XmlSchemeCablePort) schemeCablePort).setCablePortTypeId(portTypeId);
					}
				});				
			}
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
	}

	public SchemeImportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		if (!inited) {
			init();
			inited = true;
		}
		
		String user_dir = System.getProperty(USER_DIR);
		
		final String fileName = openFileForReading(user_dir);
		if(fileName == null)
			return;

		String ext = fileName.substring(fileName.lastIndexOf("."));
		File f = new File(fileName);

		if(ext.equals(".xml")) {
			try {
				if (f.getName().startsWith("config")) {
					loadConfigXML(fileName);
					LocalXmlIdentifierPool.flush();
				}
				else if (f.getName().startsWith("scheme")) {
					Scheme scheme;
					try {
						scheme = loadSchemeXML(fileName);
					} catch (CreateObjectException e) {
						Log.errorException(e);
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.need_config_xml"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					} catch (XmlException e) {
						Log.errorException(e);
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.xml_format_incorrect"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// fix connection - connect threads
					for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
						SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
						if (sourcePort != null) {
							SchemeActions.connect(sourcePort, schemeCableLink, true);
						}
						SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
						if (targetPort != null) {
							SchemeActions.connect(targetPort, schemeCableLink, false);
						}
					}
					parseSchemeCableLinks(scheme);
					parseSchemeElements(scheme);
					putToGraph(scheme);
					LocalXmlIdentifierPool.flush();
				}
			} catch (ApplicationException e) {
				Log.errorException(e);
			} catch (XmlException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelScheme.getString("Message.error.parse_xml"),  //$NON-NLS-1$
						LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				Log.errorException(e);
			} catch (IOException e) {
				Log.errorException(e);
			}
		}
	}
	
	protected Scheme loadSchemeXML(String fileName) throws CreateObjectException, XmlException, IOException {
		Scheme scheme = null;
		File xmlfile = new File(fileName);
		
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		
//		if(!validateXml(doc)) {
//			throw new XmlException("Invalid XML");
//		}
		
//		make sure default types loaded
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		
		XmlSchemeSeq xmlSchemes = doc.getSchemes();
		XmlScheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			XmlScheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(LoginManager.getUserId(), xmlScheme);
			scheme.setName(scheme.getName()	+ "(imported " + " from \'" + xmlfile.getName() + "\')");
			break;
		}
		System.setProperty(USER_DIR,  user_dir);
		return scheme;
	}
	
	protected void loadConfigXML(String fileName) throws CreateObjectException, XmlException, IOException {
		File xmlfile = new File(fileName);
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.parse(xmlfile);
		
//		if(!validateXml(doc)) {
//			throw new XmlException("Invalid XML");
//		}
		
//		make sure default types loaded
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		Identifier userId = LoginManager.getUserId();
		String importType = doc.getImportType();
		
		XmlLinkTypeSeq xmlLinkTypes = doc.getLinkTypes();
		XmlLinkType[] xmlLinkTypesArray = xmlLinkTypes.getLinkTypeArray();
		for(int i = 0; i < xmlLinkTypesArray.length; i++) {
			XmlLinkType xmlLinkType = xmlLinkTypesArray[i];
			LinkType.createInstance(userId, importType, xmlLinkType);
		}
		for (final XmlCableLinkType cableLinkType : doc.getCableLinkTypes().getCableLinkTypeArray()) {
			CableLinkType.createInstance(userId, cableLinkType, importType);
		}
		XmlEquipmentTypeSeq xmlEquipmentTypes = doc.getEquipmentTypes();
		if (xmlEquipmentTypes != null) {
			XmlEquipmentType[] xmlEquipmentTypesArray = xmlEquipmentTypes.getEquipmentTypeArray();
			for(int i = 0; i < xmlEquipmentTypesArray.length; i++) {
				XmlEquipmentType xmlEquipmentType = xmlEquipmentTypesArray[i];
				EquipmentType.createInstance(userId, importType, xmlEquipmentType);
			}
		}
		XmlEquipmentSeq xmlEquipments = doc.getEquipments();
		if (xmlEquipments != null) {
			XmlEquipment[] xmlEquipmentsArray = xmlEquipments.getEquipmentArray();
			for(int i = 0; i < xmlEquipmentsArray.length; i++) {
				XmlEquipment xmlEquipment = xmlEquipmentsArray[i];
				Equipment.createInstance(userId, xmlEquipment, importType);
			}
		}
		System.setProperty(USER_DIR,  user_dir);
	}
	
	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
	
	private void parseSchemeCableLinks(Scheme scheme) throws ApplicationException {
		EquivalentCondition condition1 = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
		Set<CableLinkType> cableLinkTypes1 = StorableObjectPool.getStorableObjectsByCondition(condition1, true);

		if (cableLinkTypes1.size() == 0) {
			Log.debugMessage("no CableLinkTypes found", Level.WARNING);
			return;
		}
		
		Map<Integer, CableLinkType> cableLinkTypes = new HashMap<Integer, CableLinkType>();
		for (CableLinkType type : cableLinkTypes1) {
			cableLinkTypes.put(Integer.valueOf(type.getCableThreadTypes(false).size()), type);	
		}

		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
			CableLinkType cableLinkType = schemeCableLink.getAbstractLinkType();
			if (cableLinkType == null) {
				Log.debugMessage("No real CableLinkType for " + schemeCableLink.getName(), Level.FINEST);
				CableLinkType suitableType;
				Integer fibers = Integer.valueOf(schemeCableLink.getSchemeCableThreads().size());
				suitableType = cableLinkTypes.get(fibers);
				if (suitableType == null) {
					for (Integer i : cableLinkTypes.keySet()) {
						if (i.compareTo(fibers) > 0) {
							suitableType = cableLinkTypes.get(i);
							break;
						}
					}
					if (suitableType == null) {
						suitableType = cableLinkTypes.values().iterator().next();
					}
				}
				schemeCableLink.setAbstractLinkType(suitableType);
			} else { // fix colors
				Iterator<CableThreadType> it1 = CableLinkTypeLayout.getSortedThreadTypes(cableLinkType).iterator();
				Iterator<SchemeCableThread> it2 = SchemeCableLinkLayout.getSortedCableThreads(schemeCableLink).iterator();
				Identifier userId = LoginManager.getUserId();
				for(; it1.hasNext() && it2.hasNext();) {
					final CableThreadType cableThreadType = it1.next();
					final SchemeCableThread schemeCableThread = it2.next();
					
					final StorableObjectCondition condition = new TypicalCondition(CharacteristicTypeCodenames.COMMON_COLOUR, OperationSort.OPERATION_EQUALS, CHARACTERISTIC_TYPE_CODE, COLUMN_CODENAME);
					final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					final CharacteristicType characteristicType = characteristicTypes.isEmpty()
					? CharacteristicType.createInstance(userId, CharacteristicTypeCodenames.COMMON_COLOUR, "", "color", DataType.INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL)
							: characteristicTypes.iterator().next();
					final String name = characteristicType.getName();
					final String description = characteristicType.getDescription();
					
					assert characteristicType != null : NON_NULL_EXPECTED;
					Characteristic.createInstance(userId,
							characteristicType,
							name,
							description,
							Integer.toString(cableThreadType.getColor()),
							schemeCableThread,
							true,
							true);
				}
			}
		}
	}
	
	private void parseSchemeElements(Scheme scheme) throws ApplicationException {
		initMuffs();
		initVrms();
		
		ApplicationContext internalContext =  new ApplicationContext();
		internalContext.setDispatcher(new Dispatcher());
		UgoTabbedPane pane = new UgoTabbedPane(internalContext);
		SchemeGraph invisibleGraph = pane.getGraph();
		invisibleGraph.setMakeNotifications(false);
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		
		Map<SchemeElement, SchemeElement> schemeElementMapping = new HashMap<SchemeElement, SchemeElement>();
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
 			if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_CONTAINER) {
				// if no real Scheme associated create new scheme with internal VRM's
				if (schemeElement.getScheme(false) == null) {
					Log.debugMessage("No real scheme for " + schemeElement.getName(), Level.FINEST);
					
					Scheme internalScheme = Scheme.createInstance(userId, schemeElement.getName(), IdlKind.BUILDING, domainId);
					internalScheme.setDescription(schemeElement.getDescription());
					internalScheme.setLabel(schemeElement.getLabel());
					internalScheme.setSymbol(schemeElement.getSymbol());
					schemeElement.setScheme(internalScheme);

					ElementsTabbedPane schemePane = new ElementsTabbedPane(internalContext);
					SchemeGraph schemeGraph = schemePane.getGraph();
					schemePane.getCurrentPanel().getSchemeResource().setScheme(internalScheme);
					int grid = schemeGraph.getGridSize();
					schemeGraph.setMakeNotifications(false);
					SchemeImageResource res = internalScheme.getSchemeCell();
					if (res == null) {
						res = SchemeObjectsFactory.createSchemeImageResource();
						internalScheme.setSchemeCell(res);
					}
					
					//	next create vrm for every cable port
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively();
					int inCount = 0, outCount = 0;
					for (SchemeCablePort cablePort : existingCablePorts) {
						SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
						int fibers = 8;
						if (cableLink != null) {
							fibers = cableLink.getSchemeCableThreads().size();
						}
						boolean isInput = cablePort.getDirectionType().value() == IdlDirectionType.__IN;
						SchemeProtoElement suitableVrm = getSuitableProto(isInput ? this.inVrms : this.outVrms, Integer.valueOf(fibers));
						
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(internalScheme, suitableVrm);
						// substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, cablePort);

						// write it to cell
						SchemeImageResource res1 = newSchemeElement.getSchemeCell();
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res1.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						// place to scheme cell
						Point p = isInput 
								?	new Point(grid * 15, grid * (10 + 20 * inCount))
								: new Point(grid * 45, grid * (10 + 20 * outCount));
						clonedObjects = SchemeActions.openSchemeImageResource(schemeGraph, res1, true, p, false);
						// add top level port
						CablePortCell portCell = SchemeActions.findCablePortCellById(schemeGraph, cablePort.getId());
						CreateBlockPortAction.create(schemeGraph, portCell);

						if (isInput) {
							inCount++;
						} else {
							outCount++;
						}
					}
					res.setData((List)schemeGraph.getArchiveableState());
					// create UGO
					if (existingCablePorts.size() > 0) {
						new CreateTopLevelSchemeAction(schemePane).execute();
					}
					
				}
			} else if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_ELEMENT_CONTAINER) {
				// if no real EqT associated
				if (schemeElement.getEquipmentType() == null) {
					Log.debugMessage("No real eqt for " + schemeElement.getName(), Level.FINEST);
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively();
					if (existingCablePorts.size() < 3) { // straight muff
						// count how many threads in connected fibers
						int maxFibers = 8;
						for (SchemeCablePort cablePort : existingCablePorts) {
							SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
							if (cableLink != null) {
								maxFibers = Math.max(maxFibers, cableLink.getSchemeCableThreads().size());
							}
						}
						SchemeProtoElement suitableMuff = getSuitableProto(this.straightMuffs, Integer.valueOf(maxFibers));
												
						// next create SchemeElement from suitableMuff
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(scheme, suitableMuff);
						newSchemeElement.setName(schemeElement.getName());
						newSchemeElement.setDescription(schemeElement.getDescription());
						newSchemeElement.setSiteNode(schemeElement.getSiteNode());
						
						// and substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, existingCablePorts);
						
						// write it to cell
						SchemeImageResource res1 = newSchemeElement.getSchemeCell();
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res1.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						SchemeImageResource res2 = newSchemeElement.getUgoCell();
						clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res2, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res2.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						schemeElementMapping.put(schemeElement, newSchemeElement);
					} else if (existingCablePorts.size() > 2) { // split muff
						System.out.println();
					} else { // unknown device
						Log.debugMessage("Unknown object with " + existingCablePorts.size() + " cable ports", Level.FINE);
					}
				}
			}
		}
		for (SchemeElement schemeElement : schemeElementMapping.keySet()) {
			SchemeElement newSchemeElement = schemeElementMapping.get(schemeElement);
			scheme.removeSchemeElement(schemeElement);
			scheme.addSchemeElement(newSchemeElement);
		}
	}

	private void putToGraph(Scheme scheme) throws ApplicationException {
		ApplicationContext aContext =  this.pane.getContext();
		Dispatcher internalDispatcher = aContext.getDispatcher();

		internalDispatcher.firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));

		SchemeGraph schemeGraph = this.pane.getGraph();
		int grid = schemeGraph.getGridSize();
		schemeGraph.setMakeNotifications(false);
		
		// determine bounds
		double xmin = 0, ymin = 0, xmax = 0, ymax = 0; 
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
			Equipment equipment = schemeElement.getEquipment();
			if (equipment != null) {
				double x0 = equipment.getLatitude();
				double y0 = equipment.getLatitude();
				xmin = Math.min(xmin, x0);
				ymin = Math.min(ymin, y0);
				xmax = Math.max(xmax, x0);
				ymax = Math.max(ymax, y0);
			}
		}
		
		double kx = (xmax - xmin == 0) ? 1 : Constants.A0.width / (xmax - xmin);
		double ky = (ymax - ymin == 0) ? 1 : Constants.A0.height / (ymax - ymin);
		
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
			Equipment equipment = schemeElement.getEquipment();
			Point p;
			if (equipment != null) {
				double x0 = equipment.getLatitude();
				double y0 = equipment.getLatitude();
				p = new Point((int)((x0 - xmin) * kx), (int)((y0 - ymin) * ky)); 
			} else { 
				p = new Point((int)(10000 * Math.random()), (int)(10000 * Math.random()));
			}
			internalDispatcher.firePropertyChange(new SchemeEvent(this, schemeElement.getId(), p, SchemeEvent.INSERT_SCHEMEELEMENT));
		}
		
		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
			Point p;
			if (schemeCableLink.getSourceAbstractSchemePort() != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, schemeCableLink.getSourceAbstractSchemePort().getId());
				try {
					DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
					PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
					p = sourceView.getBounds().getLocation();
				} catch (CreateObjectException e) {
					Log.errorMessage(e.getMessage());
					return;
				}
			} else if (schemeCableLink.getTargetAbstractSchemePort() != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, schemeCableLink.getTargetAbstractSchemePort().getId());
				try {
					DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
					PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
					p = sourceView.getBounds().getLocation();
				} catch (CreateObjectException e) {
					Log.errorMessage(e.getMessage());
					return;
				}
			} else {
				p = new Point((int)(10000 * Math.random()), (int)(10000 * Math.random()));	
			}
			
			internalDispatcher.firePropertyChange(new SchemeEvent(this, schemeCableLink.getId(), p, SchemeEvent.INSERT_SCHEME_CABLELINK));
		}
	}

	private void initMuffs() throws ApplicationException {
		TypicalCondition condition1 = new TypicalCondition(EquipmentTypeCodename.MUFF.stringValue(), OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		Set<EquipmentType> muffTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> muffTypeIds = new HashSet<Identifier>();
		for (EquipmentType eqt : muffTypes) {
			muffTypeIds.add(eqt.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(muffTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> muffs = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (muffs.size() == 0) {
			Log.debugMessage("No muffs found", Level.WARNING);
			return;
		}

		// put <number of ports, muff>
		this.straightMuffs = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement muff : muffs) {
			Set<SchemeCablePort> cablePorts = muff.getSchemeCablePortsRecursively();
			if (cablePorts.size() == 2) {
				this.straightMuffs.put(Integer.valueOf(muff.getSchemePortsRecursively().size() / 2), muff);
			}
		}
	}
	
	private void initVrms() throws ApplicationException {
		TypicalCondition condition1 = new TypicalCondition(EquipmentTypeCodename.CABLE_PANEL.stringValue(), OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		Set<EquipmentType> vrmTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> vrmTypeIds = new HashSet<Identifier>();
		for (EquipmentType eqt : vrmTypes) {
			vrmTypeIds.add(eqt.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(vrmTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> vrms = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (vrms.size() == 0) {
			Log.debugMessage("No vrms found", Level.WARNING);
			return;
		}

		// put <number of ports, muff>
		this.inVrms = new HashMap<Integer, SchemeProtoElement>();
		this.outVrms = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement vrm : vrms) {
			Set<SchemePort> ports = vrm.getSchemePortsRecursively();
			if (ports.size() > 0) {
				SchemePort port = ports.iterator().next();
				if (port.getDirectionType().value() == IdlDirectionType.__OUT) {
					this.inVrms.put(Integer.valueOf(ports.size()), vrm);
				} else {
					this.outVrms.put(Integer.valueOf(ports.size()), vrm);
				}
			}
		}
	}
	
	private SchemeProtoElement getSuitableProto(Map<Integer, SchemeProtoElement> mapping, Integer num) {
		//  search for proto with corresponding number of ports, if nothing found search with larger number, 
		//  if not again - get any
		SchemeProtoElement suitableProto = this.straightMuffs.get(num);
		if (suitableProto == null) {
			for (Integer i : mapping.keySet()) {
				if (i.compareTo(num) > 0) {
					suitableProto = mapping.get(i);
					break;
				}
			}
			if (suitableProto == null) {
				suitableProto = mapping.values().iterator().next();
			}
		}
		return suitableProto;
	}
	
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, Set<SchemeCablePort> existingCablePorts) throws ApplicationException {
		Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false);
				for (SchemeCablePort existingPort : existingCablePorts) {
					if (cport.getDirectionType().equals(existingPort.getDirectionType())) {
						existingPortsMapping.put(id1, existingPort);
					}
				}
			}
		}
		for (Identifier id : existingPortsMapping.keySet()) {
			SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
			SchemeCablePort portToAdd = existingPortsMapping.get(id);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			portToAdd.setParentSchemeDevice(parent);
			portToAdd.setPortType(portToRemove.getPortType());
			clonedIds.put(id, portToAdd.getId());
			StorableObjectPool.delete(portToRemove.getId());
		}
	}
	
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, SchemeCablePort existingCablePort) throws ApplicationException {
		Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false);
				if (cport.getDirectionType().equals(existingCablePort.getDirectionType())) {
					existingPortsMapping.put(id1, existingCablePort);
					break;
				}
			}
		}
		for (Identifier id : existingPortsMapping.keySet()) {
			SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
			SchemeCablePort portToAdd = existingPortsMapping.get(id);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			portToAdd.setParentSchemeDevice(parent);
			portToAdd.setPortType(portToRemove.getPortType());
			clonedIds.put(id, portToAdd.getId());
			StorableObjectPool.delete(portToRemove.getId());
		}
	}
}

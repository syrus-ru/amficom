/*-
 * $Id: ImportExportCommand.java,v 1.17 2006/01/11 14:40:50 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JFileChooser;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.IdentifiableCell;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment.XmlEquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlComplementor;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.Identifier.XmlConversionMode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoElement;
import com.syrus.util.Log;

public abstract class ImportExportCommand extends AbstractCommand {
	protected static final String AMFICOM_IMPORT = "AMFICOM";
	protected static final String UCM_IMPORT = "ucm";
	protected static final String USER_DIR = "user.dir";
	protected static final String UCM_SPLIT_MUFF = "UCM.codename.split_muff";
	protected static final String UCM_STRAIGHT_MUFF = "UCM.codename.straight_muff";
	protected static final String UCM_SCHEMED_EQT = "UCM_SCHEMED";
	protected static final String UCM_ODF_EQT = "UCM_ODF";
	
	protected static final String UCM_SPLICE_PORT_TYPE = "splice";
	protected static final String UCM_OPTICAL_PORT_TYPE = "optical";
	
	protected static final String ID_PREFIX = "id";
	protected static File currentDirectory = new File("/export");
	protected static final String IMAGE_DIRECTORY = "image";
	
	protected Identifier userId;
	
	private static boolean inited = false;		
	
	private static void init() {
		final Map<XmlObject, SchemeImageResource> cashedSchemeCells 
				= new HashMap<XmlObject, SchemeImageResource>();
		final Map<XmlObject, SchemeImageResource> cashedUgoCells 
				= new HashMap<XmlObject, SchemeImageResource>();
		final Map<XmlObject, Map<Identifier, XmlIdentifier>> cashedSchemeIdentifiers 
				= new HashMap<XmlObject, Map<Identifier, XmlIdentifier>>();
		final Map<XmlObject, Map<Identifier, XmlIdentifier>> cashedUgoIdentifiers 
				= new HashMap<XmlObject, Map<Identifier, XmlIdentifier>>();
		ApplicationContext internalContext =  new ApplicationContext();
		internalContext.setDispatcher(new Dispatcher());
		final SchemeGraph invisibleGraph = new UgoTabbedPane(internalContext).getGraph();
		invisibleGraph.setMakeNotifications(false);
		
		XmlComplementorRegistry.registerComplementor(SCHEME_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				final XmlScheme xmlScheme = (XmlScheme) storableObject;
				switch (mode) {
				case PRE_IMPORT:
					// set current domainId
					if (xmlScheme.isSetDomainId()) {
						xmlScheme.unsetDomainId();
					}
					LoginManager.getDomainId().getXmlTransferable(xmlScheme.addNewDomainId(), importType);
					
					// substitute schemeCell and ugoCells from file
					try {
						Identifier schemeId = Identifier.fromXmlTransferable(xmlScheme.getId(), importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
						Scheme scheme = StorableObjectPool.getStorableObject(schemeId, true);
						if (xmlScheme.isSetSchemeCellFilename()) {
							String exportDirectory = currentDirectory.getPath();
							final SchemeImageResource schemeCell = SchemeObjectsFactory.createSchemeImageResource();
							schemeCell.setImage(readImageResource(exportDirectory + File.separatorChar + xmlScheme.getSchemeCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + xmlScheme.getSchemeCellFilename() + ID_PREFIX);
							
							xmlScheme.unsetSchemeCellFilename();
							schemeCell.getId().getXmlTransferable(xmlScheme.addNewSchemeCellId(), importType);
							cashedSchemeCells.put(xmlScheme, schemeCell);
							cashedSchemeIdentifiers.put(xmlScheme, identifierSeq);
						} else if (xmlScheme.isSetSchemeCellCodename()) {
//						 TODO set cell from codename
						} else if (!xmlScheme.isSetSchemeCellId()) { // if no cell imported 
							if (scheme != null) { // if exists scheme with cell - leave it
								SchemeImageResource schemeCell = scheme.getSchemeCell();
								if (schemeCell != null) {
									schemeCell.getId().getXmlTransferable(xmlScheme.addNewSchemeCellId(), importType);
								}	
							}
						}
						if (xmlScheme.isSetUgoCellFilename()) {
							String exportDirectory = currentDirectory.getPath();
							final SchemeImageResource ugoCell = SchemeObjectsFactory.createSchemeImageResource();
							ugoCell.setImage(readImageResource(exportDirectory + File.separatorChar + xmlScheme.getUgoCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + xmlScheme.getUgoCellFilename() + ID_PREFIX);
							
							xmlScheme.unsetUgoCellFilename();
							ugoCell.getId().getXmlTransferable(xmlScheme.addNewUgoCellId(), importType);
							cashedUgoCells.put(xmlScheme, ugoCell);
							cashedUgoIdentifiers.put(xmlScheme, identifierSeq);
						} else if (xmlScheme.isSetUgoCellCodename()) {
//						 TODO set cell from codename
						} else if (!xmlScheme.isSetUgoCellId()) { // no ugo
							if (scheme != null) {
								SchemeImageResource ugoCell = scheme.getUgoCell();
								if (ugoCell != null) {
									ugoCell.getId().getXmlTransferable(xmlScheme.addNewUgoCellId(), importType);
								}	
							}
						} else if (xmlScheme.isSetUgoCellCodename()) {
//						 TODO set cell from codename
						} else if (!xmlScheme.isSetUgoCellId()) { // no ugo
							if (scheme != null) {
								SchemeImageResource ugoCell = scheme.getUgoCell();
								if (ugoCell != null) {
								ugoCell.getId().getXmlTransferable(xmlScheme.addNewUgoCellId(), importType);
								}	
							}
						}
						// check parent, if scheme moved to another scheme - substitute it id to xmlScheme
						if (scheme != null) {
							SchemeElement se = scheme.getParentSchemeElement();
							if (se != null) {
								if (xmlScheme.isSetParentSchemeElementId()) {
									xmlScheme.unsetParentSchemeElementId();
								}
								se.getId().getXmlTransferable(xmlScheme.addNewParentSchemeElementId(), importType);
							}
						}
					} catch (ObjectNotFoundException e) {
						throw new UpdateObjectException(e);
					} catch (ApplicationException e) {
						throw new UpdateObjectException(e);
					} catch (IOException e) {
						throw new UpdateObjectException(e);
					} catch (ClassNotFoundException e) {
						throw new UpdateObjectException(e);
					}
					break;
				case POST_IMPORT:
					try {
						final SchemeImageResource ugoCell1 = cashedUgoCells.get(xmlScheme);
						final Map<Identifier, XmlIdentifier> ugoIdsSeq = cashedUgoIdentifiers.get(xmlScheme);
						if (ugoCell1 != null && ugoIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							List<Object> oldSerializable = ugoCell1.getData();
							Object[] cells = (Object[])oldSerializable.get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = ugoIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, ugoCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							ugoCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedUgoIdentifiers.remove(xmlScheme);
						}
						final SchemeImageResource schemeCell1 = cashedSchemeCells.get(xmlScheme);
						final Map<Identifier, XmlIdentifier> schemeIdsSeq = cashedSchemeIdentifiers.get(xmlScheme);
						if (schemeCell1 != null && schemeIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							Object[] cells = (Object[])schemeCell1.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = schemeIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, schemeCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							schemeCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedSchemeIdentifiers.remove(xmlScheme);
						}
					} catch (Exception e) {
						throw new UpdateObjectException(e);
					}
					break;
				case EXPORT:
					try {
						String exportDirectory = currentDirectory.getPath();
						new File(exportDirectory + File.separatorChar + IMAGE_DIRECTORY).mkdirs();
						
						if (xmlScheme.isSetUgoCellId()) {
							final Identifier ugoCellId = Identifier.fromXmlTransferable(xmlScheme.getUgoCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource ugoCell = StorableObjectPool.<SchemeImageResource> getStorableObject(ugoCellId, true);

							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])ugoCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlId, importType);
									cellIds.put(id, xmlId);
								}
							}
							final String fileName = IMAGE_DIRECTORY + File.separatorChar + ugoCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, ugoCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							xmlScheme.unsetUgoCellId();
							xmlScheme.setUgoCellFilename(fileName);
						}
						if (xmlScheme.isSetSchemeCellId()) {
							final Identifier schemeCellId = Identifier.fromXmlTransferable(xmlScheme.getSchemeCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource schemeCell = StorableObjectPool.<SchemeImageResource> getStorableObject(schemeCellId, true);

							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])schemeCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlIdentifier = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlIdentifier, importType);
									cellIds.put(id, xmlIdentifier);
								}
							}
							final String fileName = IMAGE_DIRECTORY + File.separatorChar + schemeCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, schemeCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							xmlScheme.unsetSchemeCellId();
							xmlScheme.setSchemeCellFilename(fileName);
						}
					} catch (final IOException ioe) {
						throw new UpdateObjectException(ioe);
					} catch (final CreateObjectException coe) {
						throw coe;
					} catch (final UpdateObjectException uoe) {
						throw uoe;
					} catch (final ApplicationException ae) {
						throw new UpdateObjectException(ae);
					}
					break;
				}
			}
		});
		
		XmlComplementorRegistry.registerComplementor(SCHEMEELEMENT_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				final XmlSchemeElement xmlSchemeElement = (XmlSchemeElement) storableObject;
				switch (mode) {
				case PRE_IMPORT:
					try {
						Identifier seId = Identifier.fromXmlTransferable(xmlSchemeElement.getId(), importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
						SchemeElement schemeElement = StorableObjectPool.getStorableObject(seId, true);
						if (xmlSchemeElement.isSetSchemeCellFilename()) {
							String exportDirectory = currentDirectory.getPath();
							final SchemeImageResource schemeCell = SchemeObjectsFactory.createSchemeImageResource();
							schemeCell.setImage(readImageResource(exportDirectory + File.separatorChar + xmlSchemeElement.getSchemeCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + xmlSchemeElement.getSchemeCellFilename() + ID_PREFIX);
							
							xmlSchemeElement.unsetSchemeCellFilename();
							schemeCell.getId().getXmlTransferable(xmlSchemeElement.addNewSchemeCellId(), importType);
							cashedSchemeCells.put(xmlSchemeElement, schemeCell);
							cashedSchemeIdentifiers.put(xmlSchemeElement, identifierSeq);
						} else if (xmlSchemeElement.isSetSchemeCellCodename()) {
//						 TODO set cell from codename
						} else if (!xmlSchemeElement.isSetSchemeCellId()) { // no cell
							if (schemeElement != null) {
								SchemeImageResource schemeCell = schemeElement.getSchemeCell();
								if (schemeCell != null) {
									schemeCell.getId().getXmlTransferable(xmlSchemeElement.addNewSchemeCellId(), importType);
								}	
							}
						}
						if (xmlSchemeElement.isSetUgoCellFilename()) {
							String exportDirectory = currentDirectory.getPath();
							final SchemeImageResource ugoCell = SchemeObjectsFactory.createSchemeImageResource();
							ugoCell.setImage(readImageResource(exportDirectory + File.separatorChar + xmlSchemeElement.getUgoCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + xmlSchemeElement.getUgoCellFilename() + ID_PREFIX);
							
							xmlSchemeElement.unsetUgoCellFilename();
							ugoCell.getId().getXmlTransferable(xmlSchemeElement.addNewUgoCellId(), importType);
							cashedUgoCells.put(xmlSchemeElement, ugoCell);
							cashedUgoIdentifiers.put(xmlSchemeElement, identifierSeq);
						} else if (xmlSchemeElement.isSetUgoCellCodename()) {
//						 TODO set cell from codename
						} else if (!xmlSchemeElement.isSetUgoCellId()) { // no ugo
							if (schemeElement != null) {
								SchemeImageResource ugoCell = schemeElement.getUgoCell();
								if (ugoCell != null) {
									ugoCell.getId().getXmlTransferable(xmlSchemeElement.addNewUgoCellId(), importType);
								}	
							}
						}
						
						if (xmlSchemeElement.isSetProtoEquipmentId()) {
							XmlIdentifier protoEqId = xmlSchemeElement.getProtoEquipmentId();
							if (protoEqId.getStringValue().equals(UCM_ODF_EQT)) {
								xmlSchemeElement.unsetProtoEquipmentId();
								
								final TypicalCondition condition = new TypicalCondition(
										EquipmentType.CABLE_PANEL, 
										OperationSort.OPERATION_EQUALS, 
										ObjectEntities.PROTOEQUIPMENT_CODE, 
										StorableObjectWrapper.COLUMN_TYPE_CODE);
								final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(condition, true);
								if (!protoEquipments.isEmpty()) {
									protoEquipments.iterator().next().getId().getXmlTransferable(xmlSchemeElement.addNewProtoEquipmentId(), importType);
								}
							}
						}
						
						// TODO if SE moved to another scheme - substitute it id to xmlSE
						// TODO do it for SCL, SL, SE, S
						if (schemeElement != null) {
							
						}
					} catch (ObjectNotFoundException e) {
						throw new UpdateObjectException(e);
					} catch (ApplicationException e) {
						throw new UpdateObjectException(e);
					} catch (IOException e) {
						throw new UpdateObjectException(e);
					} catch (ClassNotFoundException e) {
						throw new UpdateObjectException(e);
					}
					break;
				case POST_IMPORT:
					try {
						final SchemeImageResource ugoCell1 = cashedUgoCells.get(xmlSchemeElement);
						final Map<Identifier, XmlIdentifier> ugoIdsSeq = cashedUgoIdentifiers.get(xmlSchemeElement);
						if (ugoCell1 != null && ugoIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							List<Object> oldSerializable = ugoCell1.getData();
							Object[] cells = (Object[])oldSerializable.get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = ugoIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, ugoCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							ugoCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedUgoIdentifiers.remove(xmlSchemeElement);
						}
						final SchemeImageResource schemeCell1 = cashedSchemeCells.get(xmlSchemeElement);
						final Map<Identifier, XmlIdentifier> schemeIdsSeq = cashedSchemeIdentifiers.get(xmlSchemeElement);
						if (schemeCell1 != null && schemeIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							Object[] cells = (Object[])schemeCell1.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = schemeIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, schemeCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							schemeCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedSchemeIdentifiers.remove(xmlSchemeElement);
						}
					} catch (Exception e) {
						throw new UpdateObjectException(e);
					}
					break;
				case EXPORT:
					try {
						String exportDirectory = currentDirectory.getPath();
						new File(exportDirectory + File.separatorChar + IMAGE_DIRECTORY).mkdirs();
						
						if (xmlSchemeElement.isSetUgoCellId()) {
							final Identifier ugoCellId = Identifier.fromXmlTransferable(xmlSchemeElement.getUgoCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource ugoCell = StorableObjectPool.<SchemeImageResource> getStorableObject(ugoCellId, true);

							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])ugoCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlId, importType);
									cellIds.put(id, xmlId);
								}
							}
							final String fileName = IMAGE_DIRECTORY + File.separatorChar + ugoCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, ugoCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							xmlSchemeElement.unsetUgoCellId();
							xmlSchemeElement.setUgoCellFilename(fileName);
						}
						if (xmlSchemeElement.isSetSchemeCellId()) {
							final Identifier schemeCellId = Identifier.fromXmlTransferable(xmlSchemeElement.getSchemeCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource schemeCell = StorableObjectPool.<SchemeImageResource> getStorableObject(schemeCellId, true);
							
							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])schemeCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlIdentifier = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlIdentifier, importType);
									cellIds.put(id, xmlIdentifier);
								}
							}
							
							final String fileName = IMAGE_DIRECTORY + File.separatorChar + schemeCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, schemeCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							xmlSchemeElement.unsetSchemeCellId();
							xmlSchemeElement.setSchemeCellFilename(fileName);
						}
					} catch (final IOException ioe) {
						throw new UpdateObjectException(ioe);
					} catch (final CreateObjectException coe) {
						throw coe;
					} catch (final UpdateObjectException uoe) {
						throw uoe;
					} catch (final ApplicationException ae) {
						throw new UpdateObjectException(ae);
					}
					break;
				}
			}
		});

		XmlComplementorRegistry.registerComplementor(PROTOEQUIPMENT_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				switch (mode) {
				case PRE_IMPORT:
					if (importType.equals(UCM_IMPORT)) {
						final XmlProtoEquipment equipmentType = (XmlProtoEquipment) storableObject;
						final String name = equipmentType.getName();
						if (name.equals(LangModelScheme.getString(UCM_SPLIT_MUFF))) {
							equipmentType.setXmlEquipmentType(XmlEquipmentType.MUFF);
						} else if (name.equals(LangModelScheme.getString(UCM_STRAIGHT_MUFF))) {
							// TODO create split and straight Eqts
							equipmentType.setXmlEquipmentType(XmlEquipmentType.MUFF);
						}
					}
					break;
				case POST_IMPORT:
					break;
				case EXPORT:
					break;
				}
			}
		});
		
		ProtoEquipment stubProtoEquipment1 = null;
		try {
			final TypicalCondition condition = new TypicalCondition(EquipmentType.BUG_136, 
					OperationSort.OPERATION_EQUALS, 
					ObjectEntities.PROTOEQUIPMENT_CODE, 
					StorableObjectWrapper.COLUMN_TYPE_CODE);
			final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (protoEquipments.isEmpty()) {
				stubProtoEquipment1 = SchemeObjectsFactory.createProtoEquipment("", EquipmentType.BUG_136);
			} else {
				stubProtoEquipment1 = protoEquipments.iterator().next();
			}
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
		final ProtoEquipment stubProtoEquipment = stubProtoEquipment1;
		
		XmlComplementorRegistry.registerComplementor(EQUIPMENT_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				switch (mode) {
				case PRE_IMPORT:
					final XmlEquipment equipment = (XmlEquipment) storableObject;
					if (equipment.isSetDomainId()) {
						equipment.unsetDomainId();
					}
					LoginManager.getDomainId().getXmlTransferable(equipment.addNewDomainId(), importType);
					
					if (importType.equals(UCM_IMPORT)) {
						final XmlIdentifier protoEquipmentId = equipment.getProtoEquipmentId();

						// in case of ucm_scheme - change protoEquipmentId to stub
						if (protoEquipmentId.getStringValue().equals(UCM_SCHEMED_EQT)) {
							stubProtoEquipment.getId().getXmlTransferable(protoEquipmentId, importType);
						}
					}
					break;
				case POST_IMPORT:
					break;
				case EXPORT:
					break;
				}
			}
		});
		
		XmlComplementorRegistry.registerComplementor(SCHEMEPROTOELEMENT_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				final XmlSchemeProtoElement proto = (XmlSchemeProtoElement)storableObject;
				switch (mode) {
				case PRE_IMPORT:
					try {
						String exportDirectory = currentDirectory.getPath();
						if (proto.isSetSchemeCellFilename()) {
							final SchemeImageResource schemeCell = SchemeObjectsFactory.createSchemeImageResource();
							schemeCell.setImage(readImageResource(exportDirectory + File.separatorChar + proto.getSchemeCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + proto.getSchemeCellFilename() + ID_PREFIX);
							
							proto.unsetSchemeCellFilename();
							schemeCell.getId().getXmlTransferable(proto.addNewSchemeCellId(), importType);
							cashedSchemeCells.put(proto, schemeCell);
							cashedSchemeIdentifiers.put(proto, identifierSeq);
						}
						if (proto.isSetUgoCellFilename()) {
							final SchemeImageResource ugoCell = SchemeObjectsFactory.createSchemeImageResource();
							ugoCell.setImage(readImageResource(exportDirectory + File.separatorChar + proto.getUgoCellFilename()));
							
							final Map<Identifier, XmlIdentifier> identifierSeq = (Map<Identifier, XmlIdentifier>)
									readObject(exportDirectory + File.separatorChar + proto.getUgoCellFilename() + ID_PREFIX);
							
							proto.unsetUgoCellFilename();
							ugoCell.getId().getXmlTransferable(proto.addNewUgoCellId(), importType);
							cashedUgoCells.put(proto, ugoCell);
							cashedUgoIdentifiers.put(proto, identifierSeq);
						}
					} catch (final ClassNotFoundException e) {
						throw new UpdateObjectException(e);
					} catch (final IOException e) {
						throw new UpdateObjectException(e);
					}
					break;
				case POST_IMPORT:
					try {
						final SchemeImageResource ugoCell1 = cashedUgoCells.get(proto);
						final Map<Identifier, XmlIdentifier> ugoIdsSeq = cashedUgoIdentifiers.get(proto);
						if (ugoCell1 != null && ugoIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							List<Object> oldSerializable = ugoCell1.getData();
							Object[] cells = (Object[])oldSerializable.get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = ugoIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, ugoCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							ugoCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedUgoIdentifiers.remove(proto);
						}
						final SchemeImageResource schemeCell1 = cashedSchemeCells.get(proto);
						final Map<Identifier, XmlIdentifier> schemeIdsSeq = cashedSchemeIdentifiers.get(proto);
						if (schemeCell1 != null && schemeIdsSeq != null) {
							final Map<Identifier, Identifier> clonedIds = new HashMap<Identifier, Identifier>();
							Object[] cells = (Object[])schemeCell1.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									IdentifiableCell identifiableCell = (IdentifiableCell)cell;
									Identifier id = identifiableCell.getId();
									XmlIdentifier xmlId = schemeIdsSeq.get(id);
									try {
										Identifier newId = Identifier.fromXmlTransferable(xmlId, importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
										clonedIds.put(id, newId);
									} catch (ObjectNotFoundException e) {
										Log.debugMessage(e.getMessage() + " for " + id, Level.WARNING);
									}
								}
							}
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, schemeCell1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							schemeCell1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							cashedSchemeIdentifiers.remove(proto);
						}
					} catch (Exception e) {
						throw new UpdateObjectException(e);
					}
					
					break;
				case EXPORT:
					try {
						String exportDirectory = currentDirectory.getPath();
						new File(exportDirectory + File.separatorChar + IMAGE_DIRECTORY).mkdirs();
						
						if (proto.isSetUgoCellId()) {
							final Identifier ugoCellId = Identifier.fromXmlTransferable(proto.getUgoCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource ugoCell = StorableObjectPool.<SchemeImageResource> getStorableObject(ugoCellId, true);

							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])ugoCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlId, importType);
									cellIds.put(id, xmlId);
								}
							}
//							Set<Identifier> cellIds = new HashSet<Identifier>();
//							Object[] cells = (Object[])ugoCell.getData().get(0);
//							for (Object cell : cells) {
//								if (cell instanceof IdentifiableCell) {
//									cellIds.add(((IdentifiableCell)cell).getId());
//								}
//							}
//							cellIds.remove(Identifier.VOID_IDENTIFIER);
//							cellIds.remove(null);
//							final XmlIdentifierSeq identifierSeq = XmlIdentifierSeq.Factory.newInstance();
//							for (final Identifier cellId : cellIds) {
//								cellId.getXmlTransferable(identifierSeq.addNewId(), importType);
//							}

							final String fileName = IMAGE_DIRECTORY + File.separatorChar + ugoCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, ugoCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							proto.unsetUgoCellId();
							proto.setUgoCellFilename(fileName);
						}
						if (proto.isSetSchemeCellId()) {
							final Identifier schemeCellId = Identifier.fromXmlTransferable(proto.getSchemeCellId(), 
									importType, XmlConversionMode.MODE_THROW_IF_ABSENT);
							final SchemeImageResource schemeCell = StorableObjectPool.<SchemeImageResource> getStorableObject(schemeCellId, true);
							
							Map<Identifier, XmlIdentifier> cellIds = new HashMap<Identifier, XmlIdentifier>();
							Object[] cells = (Object[])schemeCell.getData().get(0);
							for (Object cell : SchemeGraph.getDescendants1(cells)) {
								if (cell instanceof IdentifiableCell) {
									final Identifier id = ((IdentifiableCell)cell).getId();
									final XmlIdentifier xmlIdentifier = XmlIdentifier.Factory.newInstance();
									id.getXmlTransferable(xmlIdentifier, importType);
									cellIds.put(id, xmlIdentifier);
								}
							}
							
							final String fileName = IMAGE_DIRECTORY + File.separatorChar + schemeCellId.getIdentifierString();
							writeImageResource(exportDirectory + File.separatorChar + fileName, schemeCell.getImage());
							writeObject(exportDirectory + File.separatorChar + fileName + ID_PREFIX, cellIds);
							
							proto.unsetSchemeCellId();
							
							proto.setSchemeCellFilename(fileName);
						}
					} catch (final IOException ioe) {
						throw new UpdateObjectException(ioe);
					} catch (final CreateObjectException coe) {
						throw coe;
					} catch (final UpdateObjectException uoe) {
						throw uoe;
					} catch (final ApplicationException ae) {
						throw new UpdateObjectException(ae);
					}
					break;
				}
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
							final XmlStorableObject storableObject,
							final String importType,
							final ComplementationMode mode)
					throws CreateObjectException, UpdateObjectException {
						switch (mode) {
						case PRE_IMPORT:
							if (importType.equals(UCM_IMPORT)) {
								final XmlSchemeCablePort schemeCablePort = (XmlSchemeCablePort) storableObject;
								if (schemeCablePort.isSetCablePortTypeId()) {
									schemeCablePort.unsetCablePortTypeId();
								}
								portType.getId().getXmlTransferable(schemeCablePort.addNewCablePortTypeId(), importType);
							}
							break;
						case POST_IMPORT:
							break;
						case EXPORT:
							break;
						}
					}
				});				
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		
		final TypicalCondition condition2 = new TypicalCondition(PortTypeKind._PORT_KIND_SIMPLE,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PORT_TYPE_CODE,
				PortTypeWrapper.COLUMN_KIND);
		try {
			final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			if (!portTypes.isEmpty()) {
				
				PortType portType = portTypes.iterator().next();
				for (PortType temp : portTypes) {
					if (temp.getSort() == PortTypeSort.PORTTYPESORT_THERMAL) {
						portType = temp;
						break;
					}
				}
				final PortType splicePortType = portType;
				for (PortType temp : portTypes) {
					if (temp.getSort() == PortTypeSort.PORTTYPESORT_OPTICAL) {
						portType = temp;
						break;
					}
				}
				final PortType opticalPortType = portType;
				
				XmlComplementorRegistry.registerComplementor(SCHEMEPORT_CODE, new XmlComplementor() {
					public void complementStorableObject(
							final XmlStorableObject storableObject,
							final String importType,
							final ComplementationMode mode)
					throws CreateObjectException, UpdateObjectException {
						switch (mode) {
						case PRE_IMPORT:
							if (importType.equals(UCM_IMPORT)) {
								final XmlSchemePort schemePort = (XmlSchemePort) storableObject;
								String xmlPortTypeId;  
								if (schemePort.isSetPortTypeId()) {
									xmlPortTypeId = schemePort.getPortTypeId().getStringValue(); 
									schemePort.unsetPortTypeId();
								} else {
									xmlPortTypeId = "";
								}
								if (UCM_SPLICE_PORT_TYPE.equals(xmlPortTypeId)) {
									splicePortType.getId().getXmlTransferable(schemePort.addNewPortTypeId(), importType);
								} else { // default is optical port
									opticalPortType.getId().getXmlTransferable(schemePort.addNewPortTypeId(), importType);
								}
							}
							break;
						case POST_IMPORT:
							break;
						case EXPORT:
							break;
						}
					}
				});				
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	protected static final String openFileForReading(String title) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"XML file");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(currentDirectory);
		fileChooser.setDialogTitle(title);
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		currentDirectory = fileChooser.getCurrentDirectory();
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
	
	protected static final String openFileForWriting(String title) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"XML file");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(currentDirectory);
		fileChooser.setDialogTitle(title);
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		currentDirectory = fileChooser.getCurrentDirectory();
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml"))) {
				fileName += ".xml";
			}
				
		}
		if(fileName == null)
			return null;
		return fileName;
	}
	
	protected boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions().setErrorListener(validationMessages));

		if(!isXmlValid) {
			Log.errorMessage("Invalid XML: ");
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				Log.debugMessage(xml.toString(), Level.WARNING);
				Log.errorMessage(error.getMessage());
				Log.errorMessage(String.valueOf(error.getObjectLocation()));
				Log.errorMessage("Column " + error.getColumn());
				Log.errorMessage("Line " + error.getLine());
				Log.errorMessage("Offset " + error.getOffset());
				Log.errorMessage("Object at cursor " + error.getCursorLocation().getObject());
				Log.errorMessage("Source name " + error.getSourceName());
			}
		}
		return isXmlValid;
	}
	
	static void writeObject(String name, Object obj) throws IOException {
		OutputStream out = createOutputStream(name);
		ObjectOutputStream objectOut = new ObjectOutputStream(out); 
		objectOut.writeObject(obj);
		out.flush();
		out.close();
	}
	
	static void writeImageResource(String name, byte[] b) throws IOException {
		OutputStream out = createOutputStream(name);
		out.write(b);
		out.flush();
		out.close();
	}
	
	static Object readObject(String name) throws IOException, ClassNotFoundException {
		InputStream in = createInputStream(name);
		ObjectInputStream objectIn = new ObjectInputStream(in);
		Object obj = objectIn.readObject();
		in.close();
		return obj;
	}

	static byte[] readImageResource(String name) throws IOException {
		InputStream in = createInputStream(name);
		byte[] b = new byte[(int) (new File(name).length())];
		in.read(b);
		in.close();
		return b;
	}

	private static InputStream createInputStream(String filename) throws IOException {
		InputStream f = new FileInputStream(filename);
		return f;
	}

	private static OutputStream createOutputStream(String filename) throws IOException {
		File file = new File(filename);
		if (file.exists())
			file.delete();
		file.createNewFile();

		OutputStream f = new FileOutputStream(filename);
		return f;
	}
	
	@Override
	public void execute() {
		this.userId = LoginManager.getUserId();
		if (!inited) {
			init();
			inited = true;
		}
	}
}

/*
* $Id: MapView.java,v 1.3 2004/12/30 16:16:41 krupenn Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.IdentifierDefaultFactory;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.corba.MapView_Transferable;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/30 16:16:41 $
 * @author $Author: krupenn $
 * @module mapview_v1
 */
public class MapView extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256722862181200184L;

	private Identifier domainId;
	private String name;
	private String description;

	private double longitude;
	private double latitude;
	private double scale;
	private double defaultScale;

	private Map map;
	/**
	 *  List&lt;{@link com.syrus.AMFICOM.scheme.corba.Scheme}&gt;
	 */
	private List schemes;

	private StorableObjectDatabase	mapViewDatabase;

	public MapView(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
		try {
			this.mapViewDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MapView(MapView_Transferable mvt) throws CreateObjectException {
		super(mvt.header);
		
		this.domainId = new Identifier(mvt.domain_id);		

		this.name = mvt.name;
		this.description = mvt.description;
		
		this.longitude = mvt.longitude;
		this.latitude = mvt.latitude;
		this.scale = mvt.scale;
		this.defaultScale = mvt.defaultScale;		

		IdentifierDefaultFactory identifierDefaultFactory = new IdentifierDefaultFactory();
		List schemeIds = new ArrayList(mvt.schemeIds.length);
		for (int i = 0; i < mvt.schemeIds.length; i++) 
			schemeIds.add(identifierDefaultFactory.newInstanceFromTransferable(mvt.schemeIds[i]));

		Identifier mapId = new Identifier(mvt.mapId);
		try{
			this.map = (Map) MapStorableObjectPool.getStorableObject(mapId, true);
		}catch(ApplicationException ae){
			throw new CreateObjectException("MapView.<init> | cannot get map " + mapId.toString(), ae);
		}			
		
		try{
			this.schemes = SchemeStorableObjectPool.getStorableObjects(schemeIds, true);
		}catch(ApplicationException ae){
			throw new CreateObjectException("MapView.<init> | cannot get schemes ", ae);
		}			

	}

	protected MapView(final Identifier id, 
				  final Identifier creatorId, 
				  final Identifier domainId,
				  final String name,
				  final String description,
				  final double longitude,
				  final double latitude,
				  final double scale,
				  final double defaultScale,
				  final Map map) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.scale = scale;
		this.defaultScale = defaultScale;
		this.map = map;

		this.schemes = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
	}

	
	public void insert() throws CreateObjectException {
		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
		try {
			if (this.mapViewDatabase != null)
				this.mapViewDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static MapView createInstance(
			final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final double scale,
			final double defaultScale,
			final Map map) 
		throws CreateObjectException {
		if (domainId == null || name == null || description == null || map == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			return new MapView(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAPVIEW_ENTITY_CODE),
					creatorId,
					domainId,
					name,
					description,
					longitude,
					latitude,
					scale,
					defaultScale,
					map);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MapView.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.domainId);
		dependencies.add(this.map);
		dependencies.addAll(this.schemes);		
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] schemeIdsTransferable = new Identifier_Transferable[this.schemes.size()];
		for (Iterator iterator = this.schemes.iterator(); iterator.hasNext();)
			schemeIdsTransferable[i++] = (((Scheme) iterator.next()).id()).transferable();		

		return new MapView_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable)this.domainId.getTransferable(),
				this.name,
				this.description,
				this.longitude,
				this.latitude,
				this.scale,
				this.defaultScale,
				(Identifier_Transferable)this.map.getId().getTransferable(),
				schemeIdsTransferable);
	}

	public List getSchemes() {
		return  Collections.unmodifiableList(this.schemes);
	}
	
	public void addScheme(Scheme scheme)
	{
		this.schemes.add(scheme);
		super.currentVersion = super.getNextVersion();
	}
	
	public void removeScheme(Scheme scheme)
	{
		this.schemes.remove(scheme);
		super.currentVersion = super.getNextVersion();
	}
	
	protected void setSchemes0(List schemes) {
		this.schemes.clear();
		if (schemes != null)
			this.schemes.addAll(schemes);
	}
	
	public void setSchemeIds(List schemeIds) {
		this.setSchemes0(schemeIds);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public Identifier getDomainId() {
		return this.domainId;
	}
	
	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}	
	
	protected synchronized void setAttributes(final Date created,
											  final Date modified,
											  final Identifier creatorId,
											  final Identifier modifierId,											  
											  final Identifier domainId,
											  final String name,
											  final String description,
											  final double longitude,
											  final double latitude,
											  final double scale,
											  final double defaultScale,
											  final Map map) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.domainId = domainId;
			this.name = name;
			this.description = description;
			this.longitude = longitude;
			this.latitude = latitude;
			this.scale = scale;
			this.defaultScale = defaultScale;
			this.map = map;
			
	}

	public double getDefaultScale() {
		return this.defaultScale;
	}
	
	public void setDefaultScale(double defaultScale) {
		this.defaultScale = defaultScale;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
		super.currentVersion = super.getNextVersion();
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public void setMap(Map map) {
		this.map = map;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		super.currentVersion = super.getNextVersion();
	}
}


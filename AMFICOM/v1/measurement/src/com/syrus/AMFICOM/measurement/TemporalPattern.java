package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;

public class TemporalPattern extends StorableObject {
	private String description;
	private ArrayList cronStrings;

	private StorableObject_Database temporalPatternDatabase;
	
	public TemporalPattern(Identifier id) throws RetrieveObjectException {
		super(id);

		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			this.temporalPatternDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public TemporalPattern(TemporalPattern_Transferable tpt) throws CreateObjectException {
		super(new Identifier(tpt.id),
					new Date(tpt.created),
					new Date(tpt.modified),
					new Identifier(tpt.creator_id),
					new Identifier(tpt.modifier_id));
		this.description = new String(tpt.description);
		this.cronStrings = new ArrayList(tpt.cron_strings.length);
		for (int i = 0; i < tpt.cron_strings.length; i++)
			this.cronStrings.add(new String(tpt.cron_strings[i]));

		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			this.temporalPatternDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private TemporalPattern(Identifier id,
													Date created,
													Date modified,
													Identifier creator_id,
													Identifier modifier_id,
													String description,
													ArrayList cronStrings) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id);
		this.description = description;
		this.cronStrings = cronStrings;
	}

	public static TemporalPattern create(Identifier id,
																			 Date created,
																			 Date modified,
																			 Identifier creator_id,
																			 Identifier modifier_id,
																			 String description,
																			 ArrayList cronStrings) {
		return new TemporalPattern(id,
															 created,
															 modified,
															 creator_id,
															 modifier_id,
															 description,
															 cronStrings);
	}

	public Object getTransferable() {
		int i = 0;
		String[] css = new String[this.cronStrings.size()];
		for (Iterator iterator = this.cronStrings.iterator(); iterator.hasNext();)
			css[i++] = new String((String)iterator.next());
		return new TemporalPattern_Transferable((Identifier_Transferable)this.id.getTransferable(),
																						this.created.getTime(),
																						this.modified.getTime(),
																						(Identifier_Transferable)this.creator_id.getTransferable(),
																						(Identifier_Transferable)this.modifier_id.getTransferable(),
																						new String(this.description),
																						css);
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList getCronStrings() {
		return this.cronStrings;
	}
}
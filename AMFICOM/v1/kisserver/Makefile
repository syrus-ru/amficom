#
# $Id: Makefile,v 1.8 2004/09/14 12:43:12 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = kisserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

.PHONY: export
export: $(LIBNAME)

.PHONY: all
all: .all

.all: .idl .sqlj
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/AMFICOMKIS.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/KIS.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		.idl

.PHONY: sqlj
sqlj: .sqlj

.sqlj: $(CLASSDIR)
	echo "sqlj.compiler-executable=$(JAVAC) $(JAVACFLAGS) -sourcepath $(SRCDIR)" > sqlj.properties
	$(SQLJ) \
		$(SQLJFLAGS) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/Alarm.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/AlarmType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/Alerting.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/Event.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/EventSource.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/event/EventSourceType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/AgentSettings.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Analysis.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/AnalysisType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/CriteriaSet.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Etalon.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/EtalonType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Evaluation.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/EvaluationType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Modeling.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/ModelingType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Result.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Test.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/TestArgumentSet.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/TestSetup.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/TestType.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/ThresholdSet.sqlj \
		$(SRCDIR)/com/syrus/AMFICOM/server/measurement/Action.java
	$(RM) \
		$(filter $(foreach source, $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj')), $(foreach src_suffix, $(SRC_SUFFIXES), $(source)$(src_suffix))), $(shell find $(SRCDIR) -name '*\.java')) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)')
	touch $@

.PHONY: sqljclean
sqljclean:
	$(RM) \
		$(foreach file, $(filter $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.sqlj

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: sqljclean idlclean
	$(RM) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.all

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(CLASSDIR) \
		SQLChecker.cache \
		sqlj.properties \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

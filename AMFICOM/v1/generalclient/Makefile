#
# $Id: Makefile,v 1.9 2004/09/14 14:07:03 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = generalclient_v1
MODULE_VERSION = $(shell date +%Y%m%d)

USER = AMFICOM
PASSWORD = amficom
HOST = RESEARCH
SERVICE_NAME = SHAITAN

LIBNAME = generalclient.jar
SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class \
	_SJProfile0.class \
	_SJProfileKeys.class

IDLDIR = ../idl_v1/idl
SRCDIR = src
CLASSDIR = classes
ifeq ($(shell hostname -s),bass)
LIBDIR = lib
else
LIBDIR = ../lib
endif
EXTLIBDIR = ../../extlib
EXTCLASSPATH = $(EXTLIBDIR)/jgraphpad.jar:$(EXTLIBDIR)/jfreechartK.jar:$(EXTLIBDIR)/ofx.spatialfx.jar:$(EXTLIBDIR)/kunststoff.jar
ENCODING = windows-1251
JAVACFLAGS = -source 1.4 -sourcepath $(SRCDIR) -deprecation -g -O
SUNJAVACFLAGS = -target 1.4.2
JIKESFLAGS = \
	-target 1.4.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	+E \
	+P \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pno-switchcheck \
	+Pnaming-convention \
	-nowarn \
	+T=8

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

.PHONY: export
export: $(LIBNAME)

.PHONY: all
all: .all

.all: .idl .sqlj
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(DEPRECATEDCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		.idl

.PHONY: sqlj
sqlj: .sqlj

.sqlj: $(CLASSDIR)
	echo "sqlj.compiler-executable=$(JAVAC) $(JAVACFLAGS) -sourcepath $(SRCDIR)" > sqlj.properties
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
	$(RM) \
		$(LIBNAME)

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(SRCDIR) \
		$(CLASSDIR) \
		SQLChecker.cache \
		sqlj.properties \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com


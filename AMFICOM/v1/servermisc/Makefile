#
# $Id: Makefile,v 1.1 2004/06/07 15:36:09 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = servermisc_v1
MODULE_VERSION = $(shell date +%Y%m%d)

USER = AMFICOM
PASSWORD = amficom
HOST = RESEARCH
SERVICE_NAME = SHAITAN

LIBNAME = serveradd.jar

SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class \
	_SJProfile0.class \
	_SJProfileKeys.class

SRCDIR = src
CLASSDIR = classes
LIBDIR = lib
ENCODING = windows-1251
JAVACFLAGS = -source 1.3 -sourcepath $(SRCDIR) -deprecation -g -O
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = \
	-target 1.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	+E \
	+P \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pno-switchcheck \
	+Pnaming-convention \
	+T=4
SQLJFLAGS = \
	-cache=true \
	-checkfilename=true \
	-checksource=true \
	-codegen=iso \
	-compile=true \
	-compiler-encoding-flag=true \
	-explain=true \
	-linemap=true \
	-parse=both \
	-password "$(PASSWORD)" \
	-profile=true \
	-ser2class=true \
	-status=false \
	-url "jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=$(HOST))(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=$(SERVICE_NAME))))" \
	-user "$(USER)" \
	-warn=all

include Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.PHONY: export
export: $(MODULE_NAME).jar

.PHONY: all
all: .all

.all: .sqlj
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: sqlj
sqlj: .sqlj

.sqlj: $(CLASSDIR)
	echo "sqlj.compiler-executable=$(JAVAC) $(JAVACFLAGS)" > sqlj.properties
	$(SQLJ) \
		$(SQLJFLAGS) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
		$(SRCDIR)/com/syrus/AMFICOM/server/ResourcedbInterface.sqlj
	$(RM) \
		$(filter $(foreach source, $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj')), $(foreach src_suffix, $(SRC_SUFFIXES), $(source)$(src_suffix))), $(shell find $(SRCDIR) -name '*\.java')) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)')
	touch $@

.PHONY: sqljclean
sqljclean:
	$(RM) \
		$(foreach file, $(filter $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.sqlj

$(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: sqljclean
	$(RM) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.all

$(MODULE_NAME).jar: .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

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

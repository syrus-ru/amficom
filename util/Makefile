#
# $Id: Makefile,v 1.16 2004/09/14 08:27:18 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = util

LIBNAME = util.jar

CLIENT_EXCLUDED_CLASSES = \
	com.syrus.util.ServerLogger \
	com.syrus.util.database.ByteArrayDatabase \
	com.syrus.util.database.JdbcBlobManager \
	com.syrus.util.database.Server \
	com.syrus.util.database.ServerProperties

SERVER_INCLUDED_CLASSES = \
	$(CLIENT_EXCLUDED_CLASSES) \
	com.syrus.io.Rewriter \
	com.syrus.util.ByteArray \
	com.syrus.util.Log \
	com.syrus.util.Logger \
	com.syrus.util.StolenLogger \
	com.syrus.util.corba.ORBUtil \
	com.syrus.util.corba.VisiBrokerORBUtil \
	com.syrus.util.corba.AuroraORBUtil \
	com.syrus.util.database.DatabaseConnection

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

.PHONY: export
export: $(LIBNAME) $(MODULE_NAME)client.jar $(MODULE_NAME)server.jar

.PHONY: all
all: .all

.all: .idl .sqlj
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH)$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
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

$(MODULE_NAME)client.jar: .all
	$(JAR) cf $@ \
		$(foreach file, $(filter-out $(foreach class, $(subst .,/, $(CLIENT_EXCLUDED_CLASSES)), $(foreach suffix, $(CLASS_SUFFIXES), $(class)$(suffix))), $(foreach classfile, $(shell find $(CLASSDIR) -name '*\.class'), $(subst $(CLASSDIR)/,,$(classfile)))), -C $(CLASSDIR) '$(file)')

$(MODULE_NAME)server.jar: .all
	$(JAR) cf $@ \
		$(foreach file, $(filter $(foreach class, $(subst .,/, $(SERVER_INCLUDED_CLASSES)), $(foreach suffix, $(CLASS_SUFFIXES), $(class)$(suffix))), $(foreach classfile, $(shell find $(CLASSDIR) -name '*\.class'), $(subst $(CLASSDIR)/,,$(classfile)))), -C $(CLASSDIR) '$(file)')

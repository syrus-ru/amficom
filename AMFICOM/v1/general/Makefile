#
# $Id: Makefile,v 1.39 2005/05/03 19:21:54 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = general_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/security/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general-vt.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/security/corba/*.java
	touch $@

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@


.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $$,\$$,$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/general/*.class))
	$(RM) $(subst $$,\$$,$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/security/*.class))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/security/corba
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/security/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java

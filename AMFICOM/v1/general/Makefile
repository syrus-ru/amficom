#
# $Id: Makefile,v 1.20 2004/11/22 12:51:55 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = general_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
		$(wildcard $(SRCDIR)/com/syrus/io/*.java) 
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(IDLJ) $(IDLJFLAGS) -DGENERAL_COMMON $(IDLDIR)/general.idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/*.class
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	cd $(SRCDIR)/com/syrus/AMFICOM/general/corba && $(RM) $(shell cat $(SRCDIR)/com/syrus/AMFICOM/general/corba/.cvsignore)
	$(RM) -r $(SCRDIR)/com/syrus/AMFICOM/CORBA/General
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/CORBA/General
	$(RM) .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/io/*.java
lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)	

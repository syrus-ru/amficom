#
# $Id: Makefile,v 1.16 2004/10/19 12:23:28 bob Exp $
#
# vim:set ft=make:
#

MODULE_NAME = general_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

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
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
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
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/util/*.java		

lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)	

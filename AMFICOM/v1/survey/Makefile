#
# $Id: Makefile,v 1.4 2005/10/09 12:58:13 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = survey_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar

#JAVACFLAGS = -source 1.4
#SUNJAVACFLAGS = -target 1.2
#JIKESFLAGS = -target 1.1

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME): .mach .loc
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.loc:	.mach
	cp -f \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/survey.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/survey_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/oldschedulerkey.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/oldsurveykey.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/scheduler.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/scheduler_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang
	touch $@

.mach:	$(CLASSDIR)
	$(JAVAC) -nowarn \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH):$(DEPRECATEDCLASSPATH):$(EXTCLASSPATH) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@


$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleanloc
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(shell find $(CLASSDIR) -name '*\.class')
	$(RM) .mach

.PHONY:	cleanloc
cleanloc:
	$(RM) \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/survey.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/survey_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/oldschedulerkey.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/oldsurveykey.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/scheduler.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/scheduler_ru.properties
	$(RM) .loc

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile

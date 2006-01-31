MODULE_NAME := scheduler
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

HOST=mongol
SERVICE_NAME=mongol

#ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

#ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
util \
extensions \
general \
administration \
configuration \
measurement \
filter \
reflectometry \
csbridge \
commonclient \
filterclient

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH)

SOURCES := $(shell find $(SRCDIR) -name *java)


$(LIBNAME): .mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/Schedule/UI/*.[gp][ir][fo]* \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/Schedule/UI
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com \
		-C $(CLASSDIR) xml

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		$(SOURCES)
	touch $@


.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach



#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/Schedule/UI/parametersPanel.properties \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/Client/General/lang/*.properties) \
		scheduler.properties Application.properties \
		scheduler.sh

.PHONY: pmd
pmd:
#	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . text rulesets/amficom-pmd-ruleset.xml
	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . html rulesets/dummystring.xml > pmdlog.html

#${JAVA} -cp ${PMD_JARS} net.sourceforge.pmd.PMD . html ${PMD_RULES} > pmdlog.html



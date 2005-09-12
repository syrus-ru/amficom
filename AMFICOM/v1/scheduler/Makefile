MODULE_NAME := scheduler
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

HOST=mongol
SERVICE_NAME=mongol

#ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

#ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar

SOURCES := $(shell find . -name *java)


$(LIBNAME): .mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/Schedule/UI/parametersPanel.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/Schedule/UI
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/Client/General/lang
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/Client/General/lang/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/lang
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

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



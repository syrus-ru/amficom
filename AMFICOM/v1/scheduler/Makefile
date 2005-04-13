MODULE_NAME = scheduler_v1
LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

#ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

#ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.inc

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/generalclient.jar:$(LIBDIR)/corbaportable.jar



$(call checkenve,JAVA_HOME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach

#.mach:	$(CLASSDIR)
#	$(JAVAC) $(JAVACFLAGS) \
#		-classpath $(ECLIPSE_CLASSPATH)$(ECLIPSE_PROJECT) \
#		$(shell find . -name *java)
.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(DEPRECATEDCLASSPATH) \
		$(shell find . -name *java)
	touch $@




#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

.PHONY: pmd
pmd:
#	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . text rulesets/amficom-pmd-ruleset.xml
	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . html rulesets/dummystring.xml > pmdlog.html

#${JAVA} -cp ${PMD_JARS} net.sourceforge.pmd.PMD . html ${PMD_RULES} > pmdlog.html



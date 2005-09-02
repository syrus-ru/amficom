MODULE_NAME := manager
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

HOST=mongol
SERVICE_NAME=mongol

#ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

#ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar:$(LIBDIR)/resource.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar:$(EXTLIBDIR)/jgraph.jar


$(call checkenve,JAVA_HOME)

$(LIBNAME): .mach
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/icons/*.[pg][ni][gf] \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/*.[pgp][nir][gfo]* \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		$(shell find . -name *java)
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


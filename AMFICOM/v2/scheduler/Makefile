MODULE_NAME = scheduler_v1
LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

LIBDIR=../lib

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4

ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	
	rm -f $(LIBNAME)

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(ECLIPSE_CLASSPATH)$(ECLIPSE_PROJECT) \
		$(shell find . -name *java)
	
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/measurement text $(PMD_RULES) 

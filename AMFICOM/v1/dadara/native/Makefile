include Makefile.inc

.PHONY:	clean common analyse fit gauss An2 JNI BreakL

$(OUT):	common analyse fit gauss An2 JNI BreakL
	$(LD) $(LDFLAGS) \
	$(COMMON_DIR)/*.$(OBJ_EXT) \
	$(ANALYSE_DIR)/*.$(OBJ_EXT) \
	$(AN2_DIR)/*.$(OBJ_EXT) \
	$(GAUSS_DIR)/*.$(OBJ_EXT) \
	$(FIT_DIR)/*.$(OBJ_EXT) \
	$(JNI_DIR)/*.$(OBJ_EXT) \
	$(BREAKL_DIR)/*.$(OBJ_EXT) \
	$(LD_OUTPUT_FLAG)$@

common:
	make -C ./common

analyse:
	make -C ./analyse

fit:
	make -C ./fit

gauss:
	make -C ./gauss

An2:
	make -C ./An2

JNI:
	make -C ./JNI

BreakL:
	make -C ./BreakL

clean:
	make -C common clean
	make -C analyse clean
	make -C fit clean
	make -C gauss clean
	make -C An2 clean
	make -C JNI clean
	make -C BreakL clean
	rm dadara.dll dadara.exp dadara.lib 

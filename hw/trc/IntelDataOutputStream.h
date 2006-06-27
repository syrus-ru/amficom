#ifndef INTEL_DATA_OUTPUT_STREAM
#define INTEL_DATA_OUTPUT_STREAM

class IntelDataOutputStream {
	public:
		IntelDataOutputStream(unsigned int size);
		virtual ~IntelDataOutputStream();
		int write_char(char c);
		int write_unsigned_char(unsigned char c);
		int write_short(short s);
		int write_unsigned_short(unsigned short s);
		int write_int(int i);
		int write_unsigned_int(unsigned int i);
		int write_string(const char* str);
		unsigned char* get_data() const;
		unsigned int get_real_size() const;

	private:
		unsigned int size;
		unsigned char* data;
		unsigned int offset;
};

#endif

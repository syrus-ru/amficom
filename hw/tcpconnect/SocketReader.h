// SocketReader.h: interface for the SocketReader class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SOCKETREADER_H__3B695E52_DD77_442E_9C97_5C87D7F85C61__INCLUDED_)
#define AFX_SOCKETREADER_H__3B695E52_DD77_442E_9C97_5C87D7F85C61__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"

class SocketReader {
	public:
		SocketReader(SOCKET sockfd, unsigned int bytes_to_read, char* buffer);
		virtual ~SocketReader();

		int has_more_data_to_read() const;
		void read();
		int is_failed() const;

	private:
		SOCKET sockfd;
		unsigned int bytes_left_to_read;
		char* buffer_pointer;
		int failed;
};

#endif // !defined(AFX_SOCKETREADER_H__3B695E52_DD77_442E_9C97_5C87D7F85C61__INCLUDED_)

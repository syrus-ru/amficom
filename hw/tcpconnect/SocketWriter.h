// SocketWriter.h: interface for the SocketWriter class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SOCKETWRITER_H__59C49340_6864_40EF_BAF1_47CAED7D7EBB__INCLUDED_)
#define AFX_SOCKETWRITER_H__59C49340_6864_40EF_BAF1_47CAED7D7EBB__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"

class SocketWriter {
	public:
		SocketWriter(SOCKET sockfd, unsigned int data_length, char* data);
		virtual ~SocketWriter();

		int has_more_data_to_write() const;
		void write();
		int is_failed() const;


	private:
		SOCKET sockfd;
		unsigned int bytes_left_to_write;
		char* buffer_pointer;
		int failed;

};

#endif // !defined(AFX_SOCKETWRITER_H__59C49340_6864_40EF_BAF1_47CAED7D7EBB__INCLUDED_)

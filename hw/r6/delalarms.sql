INSERT INTO problemresponse (problemreportidentity) SELECT identity FROM problemreport WHERE status != 3;


UPDATE problemresponse
	SET username = 'root',
	    type = 2,
	    responsedate = SYSDATE,
	    comments = 'auto'
	WHERE username IS NULL;


UPDATE problemreport SET status = 3 WHERE status = 1;


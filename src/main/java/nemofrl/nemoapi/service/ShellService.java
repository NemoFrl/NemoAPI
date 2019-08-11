package nemofrl.nemoapi.service;

import nemofrl.nemoapi.exception.NemoAPIException;

public interface ShellService {
	String shServer(String command,String ip,String userName,String passwd,int port) throws NemoAPIException;
}

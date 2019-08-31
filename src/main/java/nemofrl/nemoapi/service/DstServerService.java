package nemofrl.nemoapi.service;

import java.util.Map;

import nemofrl.nemoapi.entity.DstServerVO;
import nemofrl.nemoapi.exception.NemoAPIException;

public interface DstServerService {
	void getServerList() throws NemoAPIException;

	Map<String,Object> getServerListFromCache(int currentPage,int pageSize,String search) throws NemoAPIException;

	Map<String, Object> getServerInfo(String token, String rowId) throws NemoAPIException;

	DstServerVO searchDstPlayer(String userName) throws NemoAPIException;
}

package nemofrl.nemoapi.service;

import java.util.List;

import nemofrl.nemoapi.entity.StreamUserInfo;
import nemofrl.nemoapi.exception.NemoAPIException;

public interface StreamService {
	List<StreamUserInfo> getStreamUserInfo(String streamIds) throws NemoAPIException;
}

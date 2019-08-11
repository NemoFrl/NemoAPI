package nemofrl.nemoapi.service;

import java.util.List;
import java.util.Map;

import nemofrl.nemoapi.exception.NemoAPIException;

public interface GoogleService {

	 List<Map<String,Object>> getGoogleUrl(String search,Integer page) throws NemoAPIException;
	 
	 String translation(String to, String source) throws NemoAPIException;
}

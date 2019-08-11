package nemofrl.nemoapi.service;

import java.util.List;
import java.util.Map;

import nemofrl.nemoapi.exception.NemoAPIException;

public interface HaizeiService {

	List<Map<String,String>> getHaizeiList(String h) throws NemoAPIException;
}

package nemofrl.nemoapi.service;

import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import nemofrl.nemoapi.exception.NemoAPIException;

public interface EmojiService {

	List<LinkedTreeMap> getSogoEmoji(String search,Integer page) throws NemoAPIException;
}

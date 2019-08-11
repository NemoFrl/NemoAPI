package nemofrl.nemoapi.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public interface PostsService {

	void publish(String content,String title,Date publishedDate,long term) throws UnsupportedEncodingException;
}

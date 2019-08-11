package nemofrl.nemoapi.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import nemofrl.nemoapi.dao.WpPostsMapper;
import nemofrl.nemoapi.dao.WpTermRelationshipsMapper;
import nemofrl.nemoapi.entity.WpPostsWithBLOBs;
import nemofrl.nemoapi.entity.WpTermRelationships;
import nemofrl.nemoapi.service.PostsService;
@Service("postsService")
public class PostsServiceImpl implements PostsService{
	@Autowired
	private WpPostsMapper wpPostsMapper;
	@Autowired
	private WpTermRelationshipsMapper wpTermRelationshipsMapper;
	private static final Logger logger=LogManager.getLogger(PostsServiceImpl.class);
	
	public void publish(String content,String title,Date publishedDate,long term) throws UnsupportedEncodingException {
		int exist=wpPostsMapper.countByTitle(title);
		if(exist>0) {
			logger.debug("标题为："+title+"的文章已存在，忽略");
			return;
		}
		WpPostsWithBLOBs wpPostsWithBLOBs=new WpPostsWithBLOBs();
		wpPostsWithBLOBs.setPostAuthor(Long.valueOf(1));
		wpPostsWithBLOBs.setPostDate(publishedDate);
		wpPostsWithBLOBs.setPostDateGmt(publishedDate);
		wpPostsWithBLOBs.setPostContent(content);
		wpPostsWithBLOBs.setPostTitle(title);
		wpPostsWithBLOBs.setPostExcerpt("");
		wpPostsWithBLOBs.setPostStatus("publish");
		wpPostsWithBLOBs.setCommentStatus("open");
		wpPostsWithBLOBs.setPingStatus("open");
		wpPostsWithBLOBs.setPostPassword("");
		String encodeTitle=UriUtils.encode(title,StandardCharsets.UTF_8.name());
		wpPostsWithBLOBs.setPostName(encodeTitle);
		wpPostsWithBLOBs.setToPing("");
		wpPostsWithBLOBs.setPinged("");
		wpPostsWithBLOBs.setPostModified(publishedDate);
		wpPostsWithBLOBs.setPostModifiedGmt(publishedDate);
		wpPostsWithBLOBs.setPostContentFiltered("");
		wpPostsWithBLOBs.setPostParent(Long.valueOf(0));
		int p=wpPostsMapper.getId()+1;
		wpPostsWithBLOBs.setGuid("http://www.nemofrl.xyz:8000/?p="+p);
		wpPostsWithBLOBs.setMenuOrder(0);
		wpPostsWithBLOBs.setPostType("post");
		wpPostsWithBLOBs.setPostMimeType("");
		wpPostsWithBLOBs.setCommentCount(Long.valueOf(0));
		wpPostsMapper.insert(wpPostsWithBLOBs);
		WpTermRelationships relationships=new WpTermRelationships();
		relationships.setObjectId(Long.valueOf(p));
		relationships.setTermTaxonomyId(term);
		relationships.setTermOrder(0);
		wpTermRelationshipsMapper.insert(relationships);
	}
}

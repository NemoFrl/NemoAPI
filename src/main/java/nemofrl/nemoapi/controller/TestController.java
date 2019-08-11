package nemofrl.nemoapi.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.dao.WpPostsMapper;
import nemofrl.nemoapi.entity.WpPostsWithBLOBs;

@RestController
public class TestController {

	@Autowired
	private WpPostsMapper wpPostsMapper;
	
	@RequestMapping(value = "/test", produces = { "application/json;charset=UTF-8" })
	public void test() {
		int i=wpPostsMapper.countByTitle("shell操作测试");
		WpPostsWithBLOBs wpPostsWithBLOBs=new WpPostsWithBLOBs();
		wpPostsWithBLOBs.setPostAuthor(Long.valueOf(1));
		wpPostsWithBLOBs.setPostDate(new Date());
		wpPostsWithBLOBs.setPostDateGmt(new Date());
		wpPostsWithBLOBs.setPostContent("test");
		wpPostsWithBLOBs.setPostTitle("title");
		wpPostsWithBLOBs.setPostExcerpt("");
		wpPostsWithBLOBs.setPostStatus("publish");
		wpPostsWithBLOBs.setCommentStatus("open");
		wpPostsWithBLOBs.setPingStatus("open");
		wpPostsWithBLOBs.setPostPassword("");
		wpPostsWithBLOBs.setPostName("title");
		wpPostsWithBLOBs.setToPing("");
		wpPostsWithBLOBs.setPinged("");
		wpPostsWithBLOBs.setPostModified(new Date());
		wpPostsWithBLOBs.setPostModifiedGmt(new Date());
		wpPostsWithBLOBs.setPostContentFiltered("");
		wpPostsWithBLOBs.setPostParent(Long.valueOf(0));
		int p=wpPostsMapper.getId()+1;
		wpPostsWithBLOBs.setGuid("http://www.nemofrl.xyz:8000/?p="+p);
		wpPostsWithBLOBs.setMenuOrder(0);
		wpPostsWithBLOBs.setPostType("post");
		wpPostsWithBLOBs.setPostMimeType("");
		wpPostsWithBLOBs.setCommentCount(Long.valueOf(0));
		wpPostsMapper.insert(wpPostsWithBLOBs);
		
	}
}

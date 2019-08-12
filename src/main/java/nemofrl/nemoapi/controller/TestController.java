package nemofrl.nemoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.dao.WpPostsMapper;

@RestController
public class TestController {

	@Autowired
	private WpPostsMapper wpPostsMapper;
	
	@RequestMapping(value = "/test", produces = { "application/json;charset=UTF-8" })
	public void test() {
	
	}
}

package nemofrl.pixiv.config;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport  {
	@Autowired
	private PixivConfig pixivConfig;
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String location;
		File path=null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(pixivConfig.getSaveFilePath()))
			location="file:"+pixivConfig.getSaveFilePath();
		else location=path.getParentFile().getParentFile().getParent()+File.separator;
		registry.addResourceHandler("/image/**").addResourceLocations(location);
		super.addResourceHandlers(registry);
	}
}

package nemofrl.pixiv.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport  {
	@Autowired
	private PixivConfig pixivConfig;
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String location;
		if(StringUtils.isNotBlank(pixivConfig.getSaveFilePath()))
			location="file:"+pixivConfig.getSaveFilePath();
		else location="classpath:/pixiv/";
		registry.addResourceHandler("/image/**").addResourceLocations(location);
		super.addResourceHandlers(registry);
	}
}

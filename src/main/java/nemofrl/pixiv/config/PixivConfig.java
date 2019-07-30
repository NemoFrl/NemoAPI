package nemofrl.pixiv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PixivConfig {
	@Value("${saveFilePath:}")
    private String saveFilePath;

	@Value("${proxyIp:}")
	private String proxyIp;
	
	@Value("${proxyPort:}")
	private int proxyPort;
	
	@Value("${openProxy:}")
	private boolean openProxy;
	
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isOpenProxy() {
		return openProxy;
	}

	public void setOpenProxy(boolean openProxy) {
		this.openProxy = openProxy;
	}

	
	
}

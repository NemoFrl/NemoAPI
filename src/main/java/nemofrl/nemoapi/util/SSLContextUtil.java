package nemofrl.nemoapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLContextUtil {

//	static String keyStorePath;
//	static String keyStorePassword;
//	static String keyStoreType = "PKCS12";
	static String trustStorePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts";
	static String trustStorePassword = "changeit";
	static String trustStoreType = "JKS";

	public static SSLContext getSSLContext() throws Exception {

		// 初始化KeyManager
//		KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//
////			KeyStore keystore = KeyStore.getInstance(keyStoreType);
////			keystore.load(new FileInputStream(new File(keyStorePath)), null);
////			keyFactory.init(keystore, keyStorePassword.toCharArray());
////
//		KeyManager[] keyManagers = keyFactory.getKeyManagers();
//		// 初始化Trust Manager
//
//		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance("SunX509");

		KeyStore tsstore = KeyStore.getInstance(trustStoreType);
		tsstore.load(new FileInputStream(new File(trustStorePath)), trustStorePassword.toCharArray());
//		trustFactory.init(tsstore);
//
//		TrustManager[] trustManagers = trustFactory.getTrustManagers();

		// 注册HtpClient
//		SSLContext sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(keyManagers, trustManagers, null);

		// SSLContext sslContext =
		// SSLContexts.custom().setProtocol("TLS").loadTrustMaterial(tsstore, new
		// TrustSelfSignedStrategy()).build();
		SSLContext sc = SSLContext.getInstance("TLS");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;

//		return sslContext;

	}
}
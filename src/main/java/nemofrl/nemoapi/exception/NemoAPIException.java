package nemofrl.nemoapi.exception;

public class NemoAPIException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5338410697628827388L;
	
	public static final String ERROR_NETCONNECT="001";
	public static final String ERROR_NETSTATUS="002";
	public static final String ERROR_MATCHER="003";
	public static final String ERROR_CERTIFICATION="004";
	public static final String ERROR_SYSTEM="999";
	
	private String msg;
	private String code;
	private Throwable e;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Throwable getE() {
		return e;
	}
	public void setE(Throwable e) {
		this.e = e;
	}
	public NemoAPIException(String msg, String code, Throwable e) {
		super();
		this.msg = msg;
		this.code = code;
		this.e = e;
	}
	public NemoAPIException(String msg, String code) {
		super();
		this.msg = msg;
		this.code = code;
	}
	
}

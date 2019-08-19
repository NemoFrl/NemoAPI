package nemofrl.nemoapi.entity;

public class RespDTO {

	public static final String SUCCESS="000";
	public static final String ERROR_PARAMSLACK="010";
	public static final String ERROR_PARAMSERROR="011";
	
	private String msg;
	private String code;
	private Object data;
	
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public RespDTO(String msg, String code, Object data) {
		super();
		this.msg = msg;
		this.code = code;
		this.data = data;
	}
	public RespDTO() {
		
	}
	
	
}

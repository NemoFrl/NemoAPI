package nemofrl.nemoapi.entity;

public class DstServerVO {

	private boolean dedicated; //是否为专用服务器
	
	private int connected;
	
	private String host;
	
	private String intent;
	
	private String maxconnections;
	
	private String mode;
	
	private boolean mods;
	
	private String name;
	
	private boolean password;
	
	private int port;
	
	private boolean pvp;
	
	private String season;
	
	private Object slaves;
	
	private String tags;
	
	private int v;
	
	private String __addr;
	
	private int __lastPing;
	
	private String __rowId;

	private String data;
	
	private String players;
	
	public boolean isDedicated() {
		return dedicated;
	}

	public void setDedicated(boolean dedicated) {
		this.dedicated = dedicated;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getMaxconnections() {
		return maxconnections;
	}

	public void setMaxconnections(String maxconnections) {
		this.maxconnections = maxconnections;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isMods() {
		return mods;
	}

	public void setMods(boolean mods) {
		this.mods = mods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isPvp() {
		return pvp;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Object getSlaves() {
		return slaves;
	}

	public void setSlaves(Object slaves) {
		this.slaves = slaves;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public String get__addr() {
		return __addr;
	}

	public void set__addr(String __addr) {
		this.__addr = __addr;
	}

	public int get__lastPing() {
		return __lastPing;
	}

	public void set__lastPing(int __lastPing) {
		this.__lastPing = __lastPing;
	}

	public String get__rowId() {
		return __rowId;
	}

	public void set__rowId(String __rowId) {
		this.__rowId = __rowId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getPlayers() {
		return players;
	}

	public void setPlayers(String players) {
		this.players = players;
	}
	
	
}

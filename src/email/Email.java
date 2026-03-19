package email;

public class Email {
	private String smtpServer="";
	private String imapServer="";
	private int smtpPort=0;
	private int imapPort=0;
	private String security="";
	private String user="";
	private String password="";
	private String address="";
	private String phone="";
	
	public Email()
	{
		
	}
	
	public Email(String smtpServer, int smtpPort, String imapServer, int imapPort, String security, String user, String address, String password)
	{
		this.smtpServer=smtpServer;
		this.smtpPort=smtpPort;
		this.imapServer=imapServer;
		this.imapPort=imapPort;
		this.security=security;
		this.user=user;
		this.address=address;
		this.password=password;
	}

	public Email(String smtpServer, int smtpPort, String imapServer, int imapPort, String security, String user, String phone, String address, String password)
	{
		this.smtpServer=smtpServer;
		this.smtpPort=smtpPort;
		this.imapServer=imapServer;
		this.imapPort=imapPort;
		this.security=security;
		this.user=user;
		this.phone=phone;
		this.address=address;
		this.password=password;
	}
	
	
	public void show()
	{
		System.out.println("User: " + user + " address: " + address + " password: " + password);
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getImapServer() {
		return imapServer;
	}
	public void setImapServer(String imapServer) {
		this.imapServer = imapServer;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	public int getImapPort() {
		return imapPort;
	}
	public void setImapPort(int imapPort) {
		this.imapPort = imapPort;
	}
	public String getSecurity() {
		return security;
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}

package pixsantander;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bancos.DadosBancariosFundo;
import bancos.DadosFundo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import mysql.ConnectorMariaDB;

public class AuthenticationPixSantander 
{
	private static String pathRootCertificates= System.getProperty("user.home")+File.separator+"App"+File.separator+"SantanderCertificados";
	private int idAuthentication=0;
	private String clientId="";
	private String clientSecret="";
	private File certificateP12 = null;
	private String passwdP12 = "";
	private String workspaceId= "";
	private AccessToken accessToken=new AccessToken();
	private DadosBancariosFundo dadosBancariosFundo = new DadosBancariosFundo();
	public static String urlAuth="https://trust-open.api.santander.com.br/auth/oauth/v2/token";
	public static String urlWorkspace="https://trust-open.api.santander.com.br/management_payments_partners/v1/workspaces";
	
	public static void main(String []args) throws Exception
	{
	    ConnectorMariaDB.connect();
	    AuthenticationPixSantander authenticationPixSantander = new AuthenticationPixSantander(ConnectorMariaDB.conn, "24361690000172");
	    authenticationPixSantander.obtainAccessToken();	    
	    authenticationPixSantander.getAccessToken().show();
	    authenticationPixSantander.obtainWorkspaceId();
	    authenticationPixSantander.show();
	}
	
	public AuthenticationPixSantander()
	{
		
	}
		
	public AuthenticationPixSantander(Connection conn, int idAuthentication)
	{
		this.idAuthentication = idAuthentication;
			String query="select * from  auth_pix_santander where id_auth_pix_santander="+this.idAuthentication;
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.clientId = rs.getString("client_id");
				this.clientSecret = rs.getString("client_secret");
				String pathCertificadoP12 = rs.getString("path_cerfificado_p12");
				System.out.println("PathFileP12: " + pathCertificadoP12);
				this.certificateP12 = new File(pathCertificadoP12);
				this.passwdP12 = rs.getString("passwd_p12");
				this.workspaceId=rs.getString("workspace_id");
				this.dadosBancariosFundo = new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public AuthenticationPixSantander(Connection conn, DadosBancariosFundo dadosBancariosFundo)
	{
		this.dadosBancariosFundo=dadosBancariosFundo;
			String query="select * from  auth_pix_santander where dados_bancarios_fundo_id_dados_bancarios_fundo="+this.dadosBancariosFundo.getIdDadosBancariosFundo();
		System.out.println(query);
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idAuthentication=rs.getInt("id_auth_pix_santander");
				this.clientId = rs.getString("client_id");
				this.clientSecret = rs.getString("client_secret");
				String pathCertificadoP12 = rs.getString("path_cerfificado_p12");
				System.out.println("PathFileP12: " + pathCertificadoP12);
				this.certificateP12 = new File(pathCertificadoP12);
				this.passwdP12 = rs.getString("passwd_p12");
				this.workspaceId=rs.getString("workspace_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public AuthenticationPixSantander(Connection conn, String cnpj)
	{
		DadosFundo dadosFundo = new DadosFundo(conn, cnpj);
		this.dadosBancariosFundo = new DadosBancariosFundo(conn, "033", cnpj);
		String query="select * from  auth_pix_santander where dados_bancarios_fundo_id_dados_bancarios_fundo="+this.dadosBancariosFundo.getIdDadosBancariosFundo();
		System.out.println(query);
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idAuthentication=rs.getInt("id_auth_pix_santander");
				this.clientId = rs.getString("client_id");
				this.clientSecret = rs.getString("client_secret");
				String pathFileP12 = rs.getString("path_certificado_p12");
				System.out.println("PathFileP12: " + pathFileP12);
				this.certificateP12 = new File(pathFileP12);
				this.passwdP12 = rs.getString("passwd_p12");
				this.workspaceId=rs.getString("workspace_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public void show()
	{
		System.out.println("-----------");
		System.out.println("Empresa: "+this.dadosBancariosFundo.getDadosFundo().getNome()+" CNPJ: "+this.dadosBancariosFundo.getDadosFundo().getCnpj());
		System.out.println("Banco: "+this.dadosBancariosFundo.getDadosBanco().getNomeBanco()+ " CodigoCompe: "+this.dadosBancariosFundo.getDadosBanco().getCodigoCompe() + " CodigoISPB: "+this.dadosBancariosFundo.getDadosBanco().getCodigoIspb());
		this.dadosBancariosFundo.show();
		System.out.println("IdAuthentication: "+this.idAuthentication);
		System.out.println("ClientID: "+this.clientId);
		System.out.println("ClientSecret: "+this.clientSecret);
		System.out.println("FileP12: "+this.certificateP12.getAbsolutePath());
		System.out.println("WorkspaceId: "+this.workspaceId);
	}
	
	public void obtainAccessToken()
	{
		Unirest.config()
	    .clientCertificateStore(this.getCertificateP12().getAbsolutePath(), this.getPasswdP12());	    
	    HttpResponse<String> response = Unirest.post(urlAuth)
	      .header("Content-Type", "application/x-www-form-urlencoded")
	      .field("client_id", this.clientId)
	      .field("client_secret", this.clientSecret)
	      .field("grant_type", "client_credentials")
	      .asString();
	    
	    System.out.println(response.getBody());
	    this.parseJsonResponseAccesstoken(response.getBody());
	}
	
	public void obtainWorkspaceId()
	{
		if(this.workspaceId.length()==0)
		{
			Unirest.config()
		    .clientCertificateStore(this.getCertificateP12().getAbsolutePath(), this.getPasswdP12());
		    
		    HttpResponse<String> response = Unirest.post(urlWorkspace)
		      .header("X-Application-Key", this.clientId)
		      .header("Content-Type", "application/json")
		      .header("Authorization", "Bearer "+this.getAccessToken().getAccessToken())
		      .body("{\r\n    \"type\": \"PAYMENTS\",\r\n    "
		      		+ "\"description\": \"Producao\",\r\n    \"mainDebitAccount\": {\r\n        \""
		      		+ "branch\": \""
		      		+ this.getDadosBancariosFundo().getAgencia()
		      		+ "\",\r\n        \""
		      		+ "number\": \""
		      		+ this.getDadosBancariosFundo().getConta()+this.getDadosBancariosFundo().getDigitoConta()
		      		+ "\"\r\n    },\r\n    \"pixPaymentsActive\": true,\r\n    \"barCodePaymentsActive\": true,\r\n    \"bankSlipPaymentsActive\": true,\r\n    \"bankSlipAvailableActive\": false,\r\n    \"taxesByFieldPaymentsActive\": true,\r\n    \"vehicleTaxesPaymentsActive\": true\r\n}")
		      .asString();
		    
		    System.out.println(response.getBody());	  
		    parseJsonResponseWorkspace(response.getBody());
		}
	}
	

	private void parseJsonResponseAccesstoken(String response)
	{		
			JSONParser parser = new JSONParser();
			Object obj;
			try {
				obj = parser.parse(response);
				JSONObject jsonObject = (JSONObject) obj;
				String accessToken = (String) jsonObject.get("access_token");				
				String tokenType = (String) jsonObject.get("token_type");
				long expiresIn = (long) jsonObject.get("expires_in");
				this.accessToken=new AccessToken(accessToken, tokenType, expiresIn, "", "", true);						
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}
	
	private void parseJsonResponseWorkspace(String response)
	{		
			JSONParser parser = new JSONParser();
			Object obj;
			try {
				obj = parser.parse(response);
				JSONObject jsonObject = (JSONObject) obj;
				String id = (String) jsonObject.get("id");				
				this.workspaceId=id;				
				updateWorkspaceId();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}
	
	private void updateWorkspaceId()
	{
		String update="update auth_pix_santander set workspace_id="+"'"+this.workspaceId+"'"+ " where id_auth_pix_santander="+this.idAuthentication;
		Statement st=null;
		try {
			st=ConnectorMariaDB.conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			st.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPathRootCertificates() {
		return pathRootCertificates;
	}
	public static void setPathRootCertificates(String pathRootCertificates) {
		AuthenticationPixSantander.pathRootCertificates = pathRootCertificates;
	}
	public int getIdAuthentication() {
		return this.idAuthentication;
	}
	public void setIdAuthentication(int idAuthentication) {
		this.idAuthentication = idAuthentication;
	}
	public String getClientId() {
		return this.clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return this.clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public File getCertificateP12() {
		return this.certificateP12;
	}
	public void setCertificateP12(File certificateP12) {
		this.certificateP12 = certificateP12;
	}
	public String getPasswdP12() {
		return this.passwdP12;
	}
	public void setPasswdP12(String passwdP12) {
		this.passwdP12 = passwdP12;
	}
	public String getWorkspaceId() {
		return this.workspaceId;
	}
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}
	public AccessToken getAccessToken() {
		return this.accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	public DadosBancariosFundo getDadosBancariosFundo() {
		return this.dadosBancariosFundo;
	}
	public void setDadosBancariosFundo(DadosBancariosFundo dadosBancariosFundo) {
		this.dadosBancariosFundo = dadosBancariosFundo;
	}
	public static String getUrlAuth() {
		return urlAuth;
	}
	public static void setUrlAuth(String urlAuth) {
		AuthenticationPixSantander.urlAuth = urlAuth;
	}
}
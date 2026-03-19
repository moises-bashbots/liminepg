package pixsantander;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import mysql.ConnectorMariaDB;
import utils.Utils;

public class PixPaymentByReceiver 
{
	private static SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	public static String urlPrefixPayment="https://trust-open.api.santander.com.br/management_payments_partners/v1/workspaces/";
	public static String urlSufixPayment="/pix_payments";
	private String urlPaymentInitiate="";
	private String urlPaymentEfectivate="";
	private AuthenticationPixSantander authenticationPixSantander=new AuthenticationPixSantander();
	private PagamentoPIXDadosBancariosSantander pagamentoPIXDadosBancariosSantander = new PagamentoPIXDadosBancariosSantander();
	private String idPix="";
	private String status="";

	public static void main(String[] args) 
	{
		
		ConnectorMariaDB.connect();
	    AuthenticationPixSantander authenticationPixSantander = new AuthenticationPixSantander(ConnectorMariaDB.conn, "24361690000172");
	    authenticationPixSantander.obtainAccessToken();	    
	    authenticationPixSantander.getAccessToken().show();	    
	    authenticationPixSantander.obtainWorkspaceId();
	    authenticationPixSantander.show();
	    
	    double valorPagamento=1.03;
		Date dataPagamento=null;
		try {
			dataPagamento = sdf.parse("2024-07-05");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String compe="341";
		String ispb="60701190";
		String tipoIdentificacaoConta="CC";
		String agenciaRecebedor="2000";
		String contaRecebedor="771143";
		String tipoIdentificacaoRecebedor="J";
		String identificacaoRecebedor="24361690000172";
		String informacoesEntreUsuarios="transferencia para o João - referente a manutencao";
		String referenciaEmpresa="154785966666666";
		String identificacaoComprovante="transferencia para o João - referente a manutencao";
		String tipoConta="CC";
		String agencia="1500";
		String conta="00800550";
		String tipoPessoa="J";
		String documento="24361690000172";
		String moduloSispag="Fornecedores";
		
		PagamentoPIXDadosBancariosSantander pagamentoPIXDadosBancarios = new PagamentoPIXDadosBancariosSantander(
																																		valorPagamento, 
																																		dataPagamento, 
																																		compe, 
																																		ispb, 
																																		tipoIdentificacaoConta, 
																																		agenciaRecebedor, 
																																		contaRecebedor, 
																																		tipoIdentificacaoRecebedor, 
																																		identificacaoRecebedor, 
																																		informacoesEntreUsuarios, 
																																		referenciaEmpresa, 
																																		identificacaoComprovante, 
																																		tipoConta, 
																																		agencia, 
																																		conta, 
																																		tipoPessoa, 
																																		documento, 
																																		moduloSispag);
		PixPaymentByReceiver pixPaymentByReceiver = new PixPaymentByReceiver(ConnectorMariaDB.conn, authenticationPixSantander,pagamentoPIXDadosBancarios);
	    pixPaymentByReceiver.sendRequisitionInitiate();
	    Utils.waitv(600);
	    pixPaymentByReceiver.sendRequisitionEfectivate();
		
	}
	
	public PixPaymentByReceiver(Connection conn, AuthenticationPixSantander authenticationPixSantander)
	{
		this.authenticationPixSantander=authenticationPixSantander;
		this.urlPaymentInitiate=urlPrefixPayment+this.authenticationPixSantander.getWorkspaceId()+urlSufixPayment;		
	}
	
	public PixPaymentByReceiver(Connection conn, AuthenticationPixSantander authenticationPixSantander, PagamentoPIXDadosBancariosSantander pagamentoPIXDadosBancariosSantander)
	{
		this.authenticationPixSantander=authenticationPixSantander;
		this.urlPaymentInitiate=urlPrefixPayment+this.authenticationPixSantander.getWorkspaceId()+urlSufixPayment;
		this.pagamentoPIXDadosBancariosSantander=pagamentoPIXDadosBancariosSantander;
	}

	
	public void sendRequisitionInitiate()
	{
		Unirest.config()
	    .clientCertificateStore(this.getAuthenticationPixSantander().getCertificateP12().getAbsolutePath(), this.authenticationPixSantander.getPasswdP12());

		System.out.println("UrlPaymentInitiate: "+this.urlPaymentInitiate);
		
		HttpResponse<String> response = Unirest.post(this.urlPaymentInitiate)
			      .header("Content-Type", "application/json")
			      .header("Accept", "application/json")
			      .header("X-Application-Key", this.authenticationPixSantander.getClientId())
			      .header("Authorization", "Bearer "+this.authenticationPixSantander.getAccessToken().getAccessToken())
			      .body(jsonBodyInitiate())
			      .asString();
			    
			    System.out.println(response.getBody());
			    this.parseJsonResponseInitiatePixPayment(response.getBody());
	   System.out.println("idPix: "+this.idPix);
	}
	
	public String jsonBodyInitiate()
	{
		System.out.println("Identificacao Recebedor: "+this.getPagamentoPIXDadosBancariosSantander().getIdentificacaoRecebedor());
		String tipoConta="";
		String tipoDocumento="";
		if(this.getPagamentoPIXDadosBancariosSantander().getTipoConta().toLowerCase().contains("cc"))
		{
			tipoConta="CONTA_CORRENTE";
		}
		if(this.getPagamentoPIXDadosBancariosSantander().getTipoPessoa().toLowerCase().contains("j"))
		{
			tipoDocumento="CNPJ";
		}
		else {
			tipoDocumento="CPF";
		}
		
		String jsonBody=	"{\n    \"tags\": [\n        \""+ this.getPagamentoPIXDadosBancariosSantander().getIdentificacaoRecebedor()+ "\",\n        \"PagamentoPIX\"\n    ],\n    \"remittanceInformation\": \"string\","
				+ "\n    \"beneficiary\": {\n        \""
				+ "branch\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getAgenciaRecebedor()
				+ ","
				+ "\n        \"number\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getContaRecebedor()
				+ ","
				+ "\n        \"type\": \""
				+ tipoConta
				+ "\","
				+ "\n        \"documentType\": \""
				+ tipoDocumento
				+ "\","
				+ "\n        \"documentNumber\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getIdentificacaoRecebedor()
				+ ","
				+ "\n        \"name\": \""
				+ this.getPagamentoPIXDadosBancariosSantander().getIdentificacaoRecebedor()
				+ "\","
				+ "\n        \"bankCode\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getCompe()
				+ "\n    },"
				+ "\n    \"paymentValue\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getValorPagamento()
				+ "\n}\n";
		System.out.println(jsonBody);
		return jsonBody;
	}
	
	private void parseJsonResponseInitiatePixPayment(String response)
	{		
			JSONParser parser = new JSONParser();
			Object obj=null;

			try {
				obj = parser.parse(response);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
			JSONObject jsonObject = (JSONObject) obj;
			this.idPix = (String) jsonObject.get("id");				
	}
	
	public void sendRequisitionEfectivate()
	{
		Unirest.config()
	    .clientCertificateStore(this.getAuthenticationPixSantander().getCertificateP12().getAbsolutePath(), this.authenticationPixSantander.getPasswdP12());
		this.urlPaymentEfectivate="https://trust-open.api.santander.com.br/management_payments_partners/v1/workspaces/"
				+ this.authenticationPixSantander.getWorkspaceId()
				+ "/pix_payments/"
				+ this.idPix;
		System.out.println("UrlPaymentEfectivate: "+this.urlPaymentEfectivate);
		
		HttpResponse<String> response = Unirest.patch(this.urlPaymentEfectivate)
			      .header("Content-Type", "application/json")
			      .header("Accept", "application/json")
			      .header("X-Application-Key", this.authenticationPixSantander.getClientId())
			      .header("Authorization", "Bearer "+this.authenticationPixSantander.getAccessToken().getAccessToken())
			      .body(jsonBodyEfectivate())
			      .asString();
			    
		System.out.println(response.getBody());
	}
	
	public void sendRequisitionEfectivate(String idPix)
	{
		Unirest.config()
	    .clientCertificateStore(this.getAuthenticationPixSantander().getCertificateP12().getAbsolutePath(), this.authenticationPixSantander.getPasswdP12());
		this.urlPaymentEfectivate="https://trust-open.api.santander.com.br/management_payments_partners/v1/workspaces/"
				+ this.authenticationPixSantander.getWorkspaceId()
				+ "/pix_payments/"
				+ idPix;
		System.out.println("UrlPaymentEfectivate: "+this.urlPaymentEfectivate);
		
		HttpResponse<String> response = Unirest.patch(this.urlPaymentEfectivate)
			      .header("Content-Type", "application/json")
			      .header("Accept", "application/json")
			      .header("X-Application-Key", this.authenticationPixSantander.getClientId())
			      .header("Authorization", "Bearer "+this.authenticationPixSantander.getAccessToken().getAccessToken())
			      .body(jsonBodyEfectivate())
			      .asString();
			    
		System.out.println(response.getBody());
		parseJsonResponseEfectivatePixPayment(response.getBody());
	}
	
	private void parseJsonResponseEfectivatePixPayment(String response)
	{		
			JSONParser parser = new JSONParser();
			Object obj=null;

			try {
				obj = parser.parse(response);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
			JSONObject jsonObject = (JSONObject) obj;
			this.status = (String) jsonObject.get("status");				
	}

	public String jsonBodyEfectivate(double valorPagamento)
	{
		String jsonBody="{\n    \"paymentValue\": "
				+ valorPagamento
				+ ",\n    \"status\": \"AUTHORIZED\"\n}";
		System.out.println(jsonBody);
		return jsonBody;
	}

	
	public String jsonBodyEfectivate()
	{
		String jsonBody="{\n    \"paymentValue\": "
				+ this.getPagamentoPIXDadosBancariosSantander().getValorPagamento()
				+ ",\n    \"status\": \"AUTHORIZED\"\n}";
		System.out.println(jsonBody);
		return jsonBody;
	}
	

	public static String getUrlPrefixPayment() {
		return urlPrefixPayment;
	}

	public static void setUrlPrefixPayment(String urlPrefixPayment) {
		PixPaymentByReceiver.urlPrefixPayment = urlPrefixPayment;
	}

	public static String getUrlSufixPayment() {
		return urlSufixPayment;
	}

	public static void setUrlSufixPayment(String urlSufixPayment) {
		PixPaymentByReceiver.urlSufixPayment = urlSufixPayment;
	}

	public AuthenticationPixSantander getAuthenticationPixSantander() {
		return this.authenticationPixSantander;
	}

	public void setAuthenticationPixSantander(AuthenticationPixSantander authenticationPixSantander) {
		this.authenticationPixSantander = authenticationPixSantander;
	}

	public PagamentoPIXDadosBancariosSantander getPagamentoPIXDadosBancariosSantander() {
		return this.pagamentoPIXDadosBancariosSantander;
	}

	public void setPagamentoPIXDadosBancariosSantander(
			PagamentoPIXDadosBancariosSantander pagamentoPIXDadosBancariosSantander) {
		this.pagamentoPIXDadosBancariosSantander = pagamentoPIXDadosBancariosSantander;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		PixPaymentByReceiver.sdf = sdf;
	}

	public String getIdPix() {
		return this.idPix;
	}

	public void setIdPix(String idPix) {
		this.idPix = idPix;
	}

	public String getUrlPaymentInitiate() {
		return this.urlPaymentInitiate;
	}

	public void setUrlPaymentInitiate(String urlPaymentInitiate) {
		this.urlPaymentInitiate = urlPaymentInitiate;
	}

	public String getUrlPaymentEfectivate() {
		return this.urlPaymentEfectivate;
	}

	public void setUrlPaymentEfectivate(String urlPaymentEfectivate) {
		this.urlPaymentEfectivate = urlPaymentEfectivate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

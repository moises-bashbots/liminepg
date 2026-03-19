package sispag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import utils.Utils;

public class PagamentoPIXSispag {
	private static SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	private static String urlSendPayment = "https://api.itau.com.br/sispag/v1/transferencias";
	private static String urlRequestReport = "https://api.itau.com.br/sispag/v1/pagamentos_sispag";
	private static SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
	
	private Authentication authentication = new Authentication();

	
	public static void main(String[] args)
	{
		setupPIXSispag();
		Authentication authentication=new Authentication();
		PagamentoPIXDadosBancarios pagamentoPIXDadosBancarios=new PagamentoPIXDadosBancarios();
//		authentication.setClientId("6e7765ea-4cc8-4823-b783-392e1f5aef4c");
//		authentication.setClientSecret("6e43b0fc-c20f-45b4-ba1c-7cbbcf3b22eb");
		authentication.setClientId("de1e3635-6f35-42d6-b16b-8cc678b6afa7");
		authentication.setClientSecret("5a7262a6-34bd-4109-82c8-7161c599bdce");
//		File fileP12 = new File("/home/moises/CertificadoItau/RespostaItauAmplic.p12");
//		String fileName=System.getProperty("user.home")+File.separator+"App"+File.separator+"Conf"+File.separator+"pixsispag.conf";
//		fileName = "..\\Conf\\pixsispag.conf";
		File fileP12 = new File("/home/moises/Files/Clients/Limine/ItauCertificados/RespostaItauAmplic.p12");
		String passwdP12="amplic";
		authentication.setCertificateP12(fileP12);
		authentication.setPasswdP12(passwdP12);
		authentication.obtainAccessToken();
		authentication.getAccessToken().show();

		double valorPagamento=1.02;
		Date dataPagamento = Calendar.getInstance().getTime();

		String nomeArquivoString="3293319000103_098.txt";
		String ispb="90400888";
		String tipoIdentificacaoConta="CC";
		String agenciaRecebedor="2271";
		String contaRecebedor="130221748";
		String tipoIdentificacaoRecebedor="J";
		String identificacaoRecebedor="32933119000103";
		String informacoesEntreUsuarios="Transferência de teste PIX Amplic";
		String referenciaEmpresa=nomeArquivoString.substring(0,17)+"PIX";
		String identificacaoComprovante="Transferência de teste PIX Amplic";
		String txid=Utils.uniqueStringPIX(35, nomeArquivoString, agenciaRecebedor, contaRecebedor, identificacaoRecebedor, valorPagamento);
		String tipoConta="CC";
		String agencia="2937";
		String conta="00223416";
		String tipoPessoa="J";
		String documento="32933119000103";
		String moduloSispag="Fornecedores";
		String tipoChavePIX="cpfcnpj";
		String chavePIX=identificacaoRecebedor;
		
		pagamentoPIXDadosBancarios = new PagamentoPIXDadosBancarios(valorPagamento, dataPagamento, ispb, tipoIdentificacaoConta, agenciaRecebedor, contaRecebedor, tipoIdentificacaoRecebedor, identificacaoRecebedor, informacoesEntreUsuarios, referenciaEmpresa, identificacaoComprovante, txid, tipoConta, agencia, conta, tipoPessoa, documento, moduloSispag,tipoChavePIX,chavePIX);
		String jsonPagamento =pagamentoPIXDadosBancarios.toJSON();
//		jsonPagamento="{\"valor_pagamento\": \"35202.45\",\"data_pagamento\": \"2022-04-11\",\"ispb\": \"60701190\",\"tipo_identificacao_conta\": \"CC\",\"agencia_recebedor\": \"4848\",\"conta_recebedor\": \"163673\",\"tipo_de_identificacao_do_recebedor\": \"J\",\"identificacao_recebedor\": \"11303619000191\",\"informacoes_entre_usuarios\": \"Pagamento de operacao 32933119000103_001.txt\",\"referencia_empresa\": \"32933119000103_001_PIX\",\"identificacao_comprovante\": \"Pagamento de operacao 32933119000103_001.txt\",\"txid\": \"00103001484800163673000000003520200\",\"pagador\": {\"tipo_conta\": \"CC\",\"agencia\": \"2937\",\"conta\": \"00223416\",\"tipo_pessoa\": \"J\",\"documento\": \"32933119000103\",\"modulo_sispag\": \"Fornecedores\"}}";
		System.out.println(jsonPagamento);
//
		String responsePayment = sendPayment(authentication, jsonPagamento);
		System.out.println(responsePayment);
		
		Date inicioDate = null;
		try {
			inicioDate = sdf.parse("2022-03-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date fimDate = null;
		try {
			fimDate = sdf.parse("2022-03-29");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		inicioDate  = Calendar.getInstance().getTime();
		fimDate = Calendar.getInstance().getTime();
		String jsonConsulta = consulta(authentication, agencia, conta, documento, "Detalhada", inicioDate, fimDate);	
		ArrayList<DetalhePagamento> detalhesConsulta = parseJsonConsulta(jsonConsulta);
		for(DetalhePagamento detalhe:detalhesConsulta)
		{
			detalhe.show();
		}
		
	}
	
	public static void setupPIXSispag()
	{		
		List<Object> confLines = new ArrayList<>();
		String nameOS = System.getProperty("os.name");
		String fileName=System.getProperty("user.home")+File.separator+"App"+File.separator+"Conf"+File.separator+"pixsispag.conf";
		
		if(nameOS.toLowerCase().contains("windows"))
		{	
			fileName = "..\\Conf\\pixsispag.conf";
		}
		else
		{	
			fileName = "../Conf/pixsispag.conf";
		}
		System.out.println("Reading "  + fileName);
		try (Stream<String> stream = Files.lines(Paths.get(fileName))){			
			confLines = stream.collect(Collectors.toList());
			for(Object confLine:confLines)
			{
				String line = (String) confLine;
				if(line.startsWith("#"))
				{
					
				}
				else if(!line.isEmpty())
				{
					String[] words = line.split(";");
					String key = words[0];
					String value = words[1];
					
					switch(key)
					{
						case "urlsend":							
							urlSendPayment = value;
							System.out.println("Key: " + words[0] + " Value: " + words[1]);
							break;
						case "urlrequest":							
							urlRequestReport = value;
							System.out.println("Key: " + words[0] + " Value: " + words[1]);
							break;
						case "urlrauth":							
							Authentication.urlAuth = value;
							System.out.println("Key: " + words[0] + " Value: " + words[1]);
							break;
						default:break;
					}
				}
				else
				{
					System.out.println("Linha em branco!");
				}
				System.out.println(line);
			}
 		} catch (IOException e) {
			e.printStackTrace();
		}						
	}	
	
	public static ArrayList<DetalhePagamento> parseJsonConsulta(String jsonConsulta) 
	{
		ArrayList<DetalhePagamento> detalhesPagamento = new ArrayList<DetalhePagamento>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(jsonConsulta);
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject data = (JSONObject) jsonObject.get("data");
//			System.out.println("Data: "+data.toJSONString());
			JSONArray transferencias = (JSONArray) data.get("itens");
//			System.out.println("Itens: " +transferencias.toJSONString());
			Iterator<JSONObject> iterator = transferencias.iterator();		
			
			while(iterator.hasNext())
			{
				JSONObject transferencia = (JSONObject) iterator.next();
//				System.out.println(transferencia.toJSONString());
				detalhesPagamento.add(parseJsonTransferencia(transferencia));
			}			
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return detalhesPagamento;
	}
	
	private static DetalhePagamento parseJsonTransferencia(JSONObject transferencia)
	{
		DetalhePagamento detalhePagamento = new DetalhePagamento();
		detalhePagamento.setIdPagamento((String) transferencia.get("id_pagamento"));
		detalhePagamento.setStatus((String) transferencia.get("status"));
		detalhePagamento.setMotivo((String) transferencia.get("motivo"));
		detalhePagamento.setNomeFavorecido((String) transferencia.get("nome_favorecido"));
		detalhePagamento.setCpfCnpj((String) transferencia.get("cpf_cnpj"));
		detalhePagamento.setCodigoBanco((String) transferencia.get("cod_banco"));
		detalhePagamento.setNumeroAgencia((String) transferencia.get("numero_agencia"));
		detalhePagamento.setNumeroConta((String) transferencia.get("numero_conta"));
		detalhePagamento.setTipoPagamento((String) transferencia.get("tipo_pagamento"));
		detalhePagamento.setReferenciaEmpresa((String) transferencia.get("referencia_empresa"));
		try {
			detalhePagamento.setDataPagamento(sdf.parse((String) transferencia.get("data_pagamento")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String valorPagamentoString  = (String) transferencia.get("valor_pagamento");
		detalhePagamento.setValorPagamento(Double.parseDouble((String) transferencia.get("valor_pagamento")));
		detalhePagamento.setNumeroLancamento((String) transferencia.get("numero_lancamento"));
		
//		detalhePagamento.show();
		return detalhePagamento;
	}
	
	public static String sendPayment(Authentication authentication, String jsonPagamento) {
	    String responseString = "";
	    HttpURLConnection connection = null;
	    int status = -1;

	    try {
	        // Endpoint
	        URL url = new URL(urlSendPayment);
	        connection = (HttpURLConnection) url.openConnection();

	        // ---- TLS client certificate (PKCS12) ----
	        File p12 = authentication.getCertificateP12();
	        String p12password = authentication.getPasswdP12();
	        try (InputStream keyInput = new FileInputStream(p12)) {
	            KeyStore keyStore = KeyStore.getInstance("PKCS12");
	            keyStore.load(keyInput, p12password.toCharArray());

	            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	            kmf.init(keyStore, p12password.toCharArray());

	            SSLContext context = SSLContext.getInstance("TLS");
	            context.init(kmf.getKeyManagers(), null, new SecureRandom());

	            if (connection instanceof HttpsURLConnection) {
	                ((HttpsURLConnection) connection).setSSLSocketFactory(context.getSocketFactory());
	            }
	        }

	        // ---- Request setup ----
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        connection.setConnectTimeout(15_000);
	        connection.setReadTimeout(60_000);
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Accept-Encoding", "gzip");
	        connection.setRequestProperty("x-itau-apikey", authentication.getClientId());
	        connection.setRequestProperty("x-itau-correlationid", "1");
	        connection.setRequestProperty("Authorization", "Bearer " + authentication.getAccessToken().getAccessToken());

	        System.out.println("URL: " + urlSendPayment);
	        System.out.println("BODY\n" + jsonPagamento);

	        // ---- Send body (UTF-8) ----
	        byte[] payload = jsonPagamento.getBytes(java.nio.charset.StandardCharsets.UTF_8);
	        connection.setFixedLengthStreamingMode(payload.length); // or: connection.setChunkedStreamingMode(0);
	        try (OutputStream os = connection.getOutputStream()) {
	            os.write(payload);
	            os.flush();
	        }

	        // ---- Read response (handles error bodies + gzip) ----
	        status = connection.getResponseCode();
	        InputStream is = (status >= 400) ? connection.getErrorStream() : connection.getInputStream();

	        if (is != null) {
	            String enc = connection.getContentEncoding();
	            boolean gzip = enc != null && enc.equalsIgnoreCase("gzip");
	            try (InputStream bodyIn = gzip ? new java.util.zip.GZIPInputStream(is) : is;
	                 java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream()) {
	                byte[] buf = new byte[8192];
	                int r;
	                while ((r = bodyIn.read(buf)) != -1) bos.write(buf, 0, r);
	                responseString = bos.toString(java.nio.charset.StandardCharsets.UTF_8.name());
	            }
	        } else {
	            responseString = ""; // no body
	        }

	        System.out.println("HTTP " + status);
	        System.out.println("RESPONSE:\n" + responseString);

	    } catch (Exception e) {
	        // If something went wrong after the request was sent, try to read the error stream too
	        try {
	            if (connection != null) {
	                InputStream es = connection.getErrorStream();
	                if (es != null) {
	                    try (InputStream bodyIn = es;
	                         java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream()) {
	                        byte[] buf = new byte[8192];
	                        int r;
	                        while ((r = bodyIn.read(buf)) != -1) bos.write(buf, 0, r);
	                        responseString = bos.toString(java.nio.charset.StandardCharsets.UTF_8.name());
	                    }
	                }
	            }
	        } catch (Exception ignore) { /* best effort */ }
	        e.printStackTrace();
	    } finally {
	        if (connection != null) connection.disconnect();
	    }

	    // ---- Log to file (include status) ----
	    String logPagamentoPIXString = "";
	    logPagamentoPIXString += "ClientID: " + authentication.getClientId() + "\n";
	    logPagamentoPIXString += "EndPoint: " + urlSendPayment + "\n";
	    logPagamentoPIXString += "HTTP Status: " + status + "\n";
	    logPagamentoPIXString += "Request:\n" + jsonPagamento + "\n";
	    logPagamentoPIXString += "Response:\n" + responseString + "\n";
	    String pathLogFile = "/home/robot/App/Log/" + sdfmt.format(Calendar.getInstance().getTime()) + "_PIX.txt";
	    Utils.stringToFile(logPagamentoPIXString, pathLogFile);

	    return responseString;
	}

	
	public static String sendPaymentOld(Authentication authentication, String jsonPagamento)
	{
		String responseString="";
		HttpURLConnection connection = null;
		try {
			/*
			 * Uncomment line below for production
			 */
			URL url = new URL(urlSendPayment);
//			URL url = new URL("https://api.itau.com.br/sandbox/sispag/v1/transferencias");
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("x-itau-apikey", authentication.getClientId());
			connection.setRequestProperty("x-itau-correlationid", "1");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Encoding", "gzip");
//			connection.setRequestProperty("client_secret", authentication.getClientSecret());
//			connection.setRequestProperty("access_token", authentication.getAccessToken().getAccessToken());
			connection.setRequestProperty("Authorization", "Bearer "+authentication.getAccessToken().getAccessToken());
			connection.setDoOutput(true);
			
			System.out.println("URL: "+urlSendPayment);
			

			// Add certificate
			File p12 = authentication.getCertificateP12();
			String p12password = authentication.getPasswdP12();

			InputStream keyInput = new FileInputStream(p12);

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(keyInput, p12password.toCharArray());
			keyInput.close();

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keyStore, p12password.toCharArray());

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

			SSLSocketFactory socketFactory = context.getSocketFactory();
			if (connection instanceof HttpsURLConnection)
				((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
			//
			
			String body = jsonPagamento ;
			System.out.println("BODY");
			System.out.println(body);

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(body.toString().getBytes());
			outputStream.close();

			BufferedReader bufferedReader = 
		            new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder response = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}

			bufferedReader.close();
			System.out.println("RESPONSE: "+ responseString.toString());
			responseString=response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		String logPagamentoPIXString="";
		logPagamentoPIXString+="ClientID: "+authentication.getClientId()+"\n";
		logPagamentoPIXString+="EndPoint: "+urlSendPayment+"\n";
		logPagamentoPIXString+="Request: \n"+jsonPagamento+"\n";
		logPagamentoPIXString+="Response: \n"+responseString+"\n";
		String pathLogFile="/home/robot/App/Log/"+sdfmt.format(Calendar.getInstance().getTime())+"_PIX.txt";
		Utils.stringToFile(logPagamentoPIXString, pathLogFile);
		return responseString;
	}

	public static String consulta(Authentication authentication, String agenciaOperacao, String contaOperacao, String cnpjEmpresa, String tipoLista, Date dataInicial, Date dataFinal)
	{
		String responseString="";
		HttpURLConnection connection = null;
		try {
			/*
			 * Uncomment line below for production
			 */
			String requestString=urlRequestReport;
			
			requestString+="?"	+ "agencia_operacao="+agenciaOperacao
									+"&"+ "conta_operacao="+contaOperacao
									+"&"+ "cnpj_empresa="+cnpjEmpresa
									+"&"+ "tipo_lista="+tipoLista
									+"&"+ "data_inicial="+sdf.format(dataInicial)
									+"&"+ "data_final="+sdf.format(dataFinal)
									;
			
			System.out.println("RequestString: '"+requestString+"'");
			URL url = new URL(requestString);
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("x-itau-apikey", authentication.getClientId());
			connection.setRequestProperty("x-correlationID", "1");
			connection.setRequestProperty("x-simulation", "false");
			connection.setRequestProperty("Authorization", "Bearer "+authentication.getAccessToken().getAccessToken());
			connection.setDoOutput(true);

			// Add certificate
			File p12 = authentication.getCertificateP12();
			String p12password = authentication.getPasswdP12();

			InputStream keyInput = new FileInputStream(p12);

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(keyInput, p12password.toCharArray());
			keyInput.close();

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keyStore, p12password.toCharArray());

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

			SSLSocketFactory socketFactory = context.getSocketFactory();
			if (connection instanceof HttpsURLConnection)
				((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
			//
			
			OutputStream outputStream = connection.getOutputStream();
			outputStream.close();
			BufferedReader bufferedReader =null;
			try {
				bufferedReader = 
			            new BufferedReader(new InputStreamReader(connection.getInputStream()));
				System.out.println("RESPONSE_Consulta: "+bufferedReader.lines().collect(Collectors.joining()));				
			} catch (Exception e) {
				e.printStackTrace();
			}

			StringBuilder response = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}

			bufferedReader.close();

//			System.out.println(response.toString());
			responseString=response.toString();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return responseString;
	}
	
		
	public static String sendPaymentSandbox(Authentication authentication, String jsonPagamento)
	{
		String responseString="";
		HttpURLConnection connection = null;
		try {
			/*
			 * Uncomment line below for production
			 */
//			URL url = new URL("https://api.itau.com.br/sispag/v1/transferencias");
			URL url = new URL("https://api.itau.com.br/sandbox/sispag/v1/transferencias");
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("client_id", authentication.getClientId());
			connection.setRequestProperty("client_secret", authentication.getClientSecret());
			connection.setRequestProperty("access_token", authentication.getAccessToken().getAccessToken());
			connection.setRequestProperty("Authorization", "Bearer "+authentication.getAccessToken().getAccessToken());
			connection.setDoOutput(true);

//			// Add certificate
//			File p12 = new File("\\Dir\\file.p12");
//			String p12password = "YOUR_P12_PASSWORD";
//
//			InputStream keyInput = new FileInputStream(p12);
//
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			keyStore.load(keyInput, p12password.toCharArray());
//			keyInput.close();
//
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
//			keyManagerFactory.init(keyStore, p12password.toCharArray());
//
//			SSLContext context = SSLContext.getInstance("TLS");
//			context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
//
//			SSLSocketFactory socketFactory = context.getSocketFactory();
//			if (connection instanceof HttpsURLConnection)
//				((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
//			//
			
			String body = jsonPagamento ;

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(body.toString().getBytes());
			outputStream.close();

			BufferedReader bufferedReader = 
		            new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder response = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}

			bufferedReader.close();

//			System.out.println(response.toString());
			responseString=response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return responseString;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		PagamentoPIXSispag.sdf = sdf;
	}

	public static String getUrlSendPayment() {
		return urlSendPayment;
	}

	public static void setUrlSendPayment(String urlSendPayment) {
		PagamentoPIXSispag.urlSendPayment = urlSendPayment;
	}

	public static String getUrlRequestReport() {
		return urlRequestReport;
	}

	public static void setUrlRequestReport(String urlRequestReport) {
		PagamentoPIXSispag.urlRequestReport = urlRequestReport;
	}

	public static SimpleDateFormat getSdfmt() {
		return sdfmt;
	}

	public static void setSdfmt(SimpleDateFormat sdfmt) {
		PagamentoPIXSispag.sdfmt = sdfmt;
	}

	public Authentication getAuthentication() {
		return this.authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

}

package cnab.cnab240;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import multipag.OperacaoMultipag;
import multipag.SequenciaArquivo;
import mysql.ConnectorMariaDB;
import pixsantander.AuthenticationPixSantander;
import pixsantander.PagamentoPIXDadosBancariosSantander;
import pixsantander.PixPaymentByReceiver;
import sispag.Authentication;
import sispag.DetalhePagamento;
import sispag.PagamentoPIXDadosBancarios;
import sispag.PagamentoPIXSispag;
import utils.Utils;

public class BuilderPaymentsMultibanco  
{
	private static ArrayList<ArquivoCNAB240> cnabs240 = new ArrayList<ArquivoCNAB240>();
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat sdff = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfc = new SimpleDateFormat("yyMMdd");
	private static SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
	private static SimpleDateFormat sdft = new SimpleDateFormat("HHmmss");
	private static SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfsn = new SimpleDateFormat("yyMMddss");
	private static SimpleDateFormat sdfx = new SimpleDateFormat("dd/MM/yyyy");
	private static HashMap<String, String> versaoLayout = new HashMap<String,String>();
	private static HashMap<String, String> versaoLayoutLote = new HashMap<String,String>();
	private static HashMap<String, String> nomeBanco = new HashMap<String,String>();
	private static HashMap<String, String> sistemaBanco = new HashMap<String,String>();
	
	private static HashMap<String,ArrayList<PagamentoFornecedor>> fundosPagamentos = new HashMap<String,ArrayList<PagamentoFornecedor>>();
	
	private static HashMap<String,DadosBancarios> dadosCorretosContaFundo = new HashMap<String,DadosBancarios>(); 
	private static Date dataView=null;
	
	private static boolean pastProcess=false;
	private static boolean separateFiles=false;
	private static boolean approvedFiles=false;
	private static boolean endLinux=false;
	private static boolean blankLine=false;
	private static boolean test=false;
	private static boolean check=false;
	private static boolean readOnly=false;
	
	public static HashMap<String,String> outputPath = new HashMap<String,String>();
	private static String separator = File.separator;
	private static String companySender = "";
	private static String nameSender = "";
	private static String emailSender = "";
	private static String passwordSender = "";
	
	private static ArrayList<String> destinatarios = new ArrayList<String>();
	private static ArrayList<String> cnpjs = new ArrayList<String>();
	
	public static Connection connMSSQL = null;
	public static Connection connMySQL = null;
	public static String folderPagamentoEmLote = "";
	public static String csvPath="";
	public static boolean readCSV=false;
	
	public BuilderPaymentsMultibanco()
	{
		
	}
	
	public static void main(String[] args)
	{
		System.out.println("************************************************************************************"
						 + " Parameters accepted:"
						 + " Date, in format, to define a specific date to process: yyyy-mm-dd "
						 + " p: To process the past"
						 + " s: To process separate payments"
						 + " a: To process with pre-approval option"
						 + " b: Include a blank line in the end of file"
						 + " t: Run a test reading from multipag_teste"
						 + " c: Run a check to see the banks configured"			
						 + " f: Run a check to see the banks configured"			
						 + " Path, in the format: /folder/subfolder"
						 + " r: Only read payments without building the files");
		
		Utils.setTimeZone();
		BuilderPaymentsMultibanco builder = new BuilderPaymentsMultibanco();
		setupMultipag();
		PagamentoPIXSispag.setupPIXSispag();
		for(int i=0;i<args.length;i++)
		{
			if(args[i].matches("\\d{4}-\\d{2}-\\d{2}"))
			{
				try {
					dataView = sdfm.parse(args[i]);
					System.out.println("Data entrada não automática " + sdfm.format(dataView));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(args[i].length()==1)
			{
				if(args[i].toLowerCase().contains("p"))
				{
					pastProcess=true;
					System.out.println("****** Processando desde a data de entrada até agora!");
					
				}
				else if(args[i].toLowerCase().contains("s"))
				{
					separateFiles=true;
					System.out.println("****** Processando com a opção de geração de arquivos separados para cada pagamento!");						
				}
//				else if(args[i].toLowerCase().contains("a"))
//				{
//					approvedFiles=true;
//					System.out.println("Processando com a opção de geração de arquivos pré aprovados!");						
//				}
				else if(args[i].toLowerCase().contains("l"))
				{
					endLinux=true;
					System.out.println("****** Processando com a opção de geração de arquivos com final de linha ao estilo linux \\n!");						
				}
				else if(args[i].toLowerCase().contains("b"))
				{
					blankLine=true;
					System.out.println("****** Processando com a opção de geração de arquivos com uma linha em branco no final!");						
				}
				else if(args[i].toLowerCase().contains("c"))
				{
					check=true;
					System.out.println("****** Processando com a opção de checagem de fundos!");						
				}
				
				else if(args[i].toLowerCase().contains("t"))
				{
					test=true;
					System.out.println("****** Processando com a opção de geração de arquivos para teste!");						
				}
				
				else if(args[i].toLowerCase().contains("r"))
				{
					readOnly=true;
					System.out.println("****** Processando com a opção de geração de somente leitura!");						
				}
			}
			
			if(args[i].length()>1 && (args[i].contains(File.separator) && dataView==null) 
					&& 
					(args[i].toLowerCase().startsWith("csv"))
				)
			{
				readCSV=true;
				
				String[] fields=args[i].split("=");
				csvPath=fields[1];
				outputPath.put(fields[0], fields[1]);
				System.out.println("****** Processando com a opção de geração de arquivos de pagamento em lote por leitura de csv ou excel! ");
				System.out.println("****** File: "+fields[1]);
			}
			
			if(args[i].length()>1 && (args[i].contains("\\")||args[i].contains("/"))  && !readCSV && dataView==null)
			{
				String[] fields=args[i].split(":");
				outputPath.put(fields[0], fields[1]);
			}
		}
		
		for(String key:outputPath.keySet())
		{
			System.out.println("For bank "+ key + " outputPath " + outputPath.get(key));
		}

		Utils.waitv("Waiting just in case you are trying to read this!!",8);

		
		
		ConnectorMariaDB.connect();
		ConnectorMariaDB connectorMSSQL = ConnectorMariaDB.mssqlConnector();
			connectorMSSQL.connectUsingDefaultConf();
			connMSSQL=connectorMSSQL.getConnection();
		connMySQL=ConnectorMariaDB.getConn();
		setupDadosBancarios();
		if(!check)
		{
			sendPixSantanderEfectivate();
			if(readCSV)
			{
				setupPaymentsFromCSV(csvPath);
			}
			else 
			{
				searchForPaymentsInBatch();		
			}
			
			if(!readCSV)
			{
				setupPayments();		
			}
			
			if(!readOnly)
			{
				builder.build();
			}
		}
	}
	
	public static void sendPixSantanderEfectivate()
	{
		ArrayList<OperacaoMultipag> operacoesMultipag = new ArrayList<>();
		String query="select * from operacoes_multipag"
				+ " where id_pix is not null"
				+ " and data_entrada="+"'"+sdfm.format(Calendar.getInstance().getTime())+"'"
				+ " and pix_aprovado=0";
		Statement st=null;
		try {
			st=connMySQL.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs=null;
		
		try {
			rs=st.executeQuery(query);
			while(rs.next())
			{
				OperacaoMultipag operacaoMultipag=new OperacaoMultipag(connMySQL, rs.getInt("id_operacoes_multipag"));
				operacoesMultipag.add(operacaoMultipag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(OperacaoMultipag operacaoMultipag:operacoesMultipag)
		{
			AuthenticationPixSantander authenticationPixSantander = new AuthenticationPixSantander(connMySQL, operacaoMultipag.getDadosBancariosFundo());
			authenticationPixSantander.obtainAccessToken();
			PixPaymentByReceiver pixPaymentByReceiver = new PixPaymentByReceiver(connMySQL, authenticationPixSantander);
			pixPaymentByReceiver.jsonBodyEfectivate(operacaoMultipag.getValor());
			pixPaymentByReceiver.sendRequisitionEfectivate(operacaoMultipag.getIdPix());
			if(pixPaymentByReceiver.getStatus().toLowerCase().contains("payed"))
			{
				operacaoMultipag.updatePixEfetivado(connMySQL, true);
			}
		}
		
	}
	
	public static void searchForPaymentsInBatch()
	{
		System.out.println("Searching for payments in batch");
		List<File> files = Utils.listFilesOnly(folderPagamentoEmLote+File.separator+"Processar");
		for(File file:files)
		{
			System.out.println("File to be processed: "+ file.getAbsolutePath());
			if(file.getName().toLowerCase().endsWith(".xlsx"))
			{
				System.out.println("Building payments for: "+file.getAbsolutePath());
				setupPaymentsFromCSV(file.getAbsolutePath());
				readCSV=true;
				String destinyName = folderPagamentoEmLote+File.separator+"Processado"+File.separator+"Processado_"+file.getName();
				File destiny = new File(destinyName);
				Utils.copyFileAndDelete(file, destiny);
				break;
			}
		}		
	}
	
	public static HashMap<String, Integer> loadIndex(String lineHeader, String separator)
	{
		HashMap<String, Integer> indexOfHeader=new HashMap<>();
		String[] headers = lineHeader.split(separator);
		for(int i=0;i<headers.length;i++)
		{
			indexOfHeader.put(headers[i].toUpperCase(), i);
		}
		return indexOfHeader;
	}
	public static void setupPaymentsFromCSV(String csvPath)
	{
		ArrayList<String> lines = new ArrayList<String>();
//		ArrayList<String> lines =readCSVFile();
		if(csvPath.toLowerCase().endsWith("csv"))
		{
			lines=Utils.readLinesInFile(csvPath);			
		}
		else if(csvPath.toLowerCase().endsWith("xlsx"))
		{
			lines=Utils.extractLinesFromXLSX(csvPath);			
		}

		if(cnpjs.size()>0)
		{
			int i=0;
			for(String cnpj:cnpjs)
			{
				System.out.println(i+" "+cnpj);
				i++;				
			}

		}
		HashMap<String, Integer> index = new HashMap<>();	
		int iLine=0;
		for(String line:lines)
		{
			iLine++;
			if(iLine==1)
			{
				index=loadIndex(line, ";");
				continue;
			}
			System.out.println(line);
			String lineEmpty=line.replaceAll(";","");
			
			String[] fields=line.split(";");
			System.out.println(fields.length+" fields in this row!");
			if(lineEmpty.trim().length()<10)
			{				
				System.out.println("Skipping empty line!");
				continue;
			}
			String cnpjFundo=fields[index.get("CNPJ_FUNDO")];
			String codigoBancoFundo=fields[index.get("CODIGO_BANCO_FUNDO")];
			System.out.println(codigoBancoFundo);
			if(codigoBancoFundo.length()<3)
			{
				if(codigoBancoFundo.length()==2)
				{
					codigoBancoFundo="0"+codigoBancoFundo;
				}
				if(codigoBancoFundo.length()==1)
				{
					codigoBancoFundo="00"+codigoBancoFundo;
				}
			}
			if(codigoBancoFundo.length()>3)
			{
				codigoBancoFundo=codigoBancoFundo.substring(0,3);
			}
			System.out.println(codigoBancoFundo);

			String agenciaFundo=String.format("%04d",Integer.parseInt(fields[index.get("AGENCIA_FUNDO")].trim()));
			String contaFundo=Integer.toString((int)Double.parseDouble(Utils.cleanString(fields[index.get("CONTA_COM_DIGITO_FUNDO")].trim())));
			String nomeFavorecido=Utils.stringToANSI(fields[index.get("NOME_FAVORECIDO")]);			
			String cadastroFavorecido=fields[index.get("CADASTRO_FAVORECIDO")];
			cadastroFavorecido=cadastroFavorecido.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
			String tipoInscricaoCadastroFavorecido="";
			if(cadastroFavorecido.length()==11)
			{
				tipoInscricaoCadastroFavorecido="1";
			}
			else
			{
				tipoInscricaoCadastroFavorecido="2";
			}
			String codigoBancoFavorecido="" ;
			if(fields[index.get("CODIGO_BANCO_FAVORECIDO")].length()>0)
			{
				codigoBancoFavorecido=fields[index.get("CODIGO_BANCO_FAVORECIDO")];
				if(codigoBancoFavorecido.length()<3)
				{
					if(codigoBancoFavorecido.length()==2)
					{
						codigoBancoFavorecido="0"+codigoBancoFavorecido;
					}
					if(codigoBancoFavorecido.length()==1)
					{
						codigoBancoFavorecido="00"+codigoBancoFavorecido;
					}
				}
				if(codigoBancoFavorecido.length()>3)
				{
					codigoBancoFavorecido=codigoBancoFavorecido.substring(0,3);
				}
				if(codigoBancoFavorecido.length()==2)
				{
					codigoBancoFavorecido="0"+codigoBancoFavorecido;
				}
			}
			String agenciaFavorecido="";
			String contaFavorecido="";
			String textValorFavorecido="";
			String numeroBoletoDARF="";
			if(index.get("NUMERO_BOLETO")!=null)
			{
				System.out.println("Index for NUMERO_BOLETO is: "+index.get("NUMERO_BOLETO"));
				System.out.println("Size of fields is: "+fields.length);
				if(index.get("NUMERO_BOLETO")<fields.length)
				{
					numeroBoletoDARF=fields[index.get("NUMERO_BOLETO")];
				}
			}
		
			double valorFavorecido=0;
			if(fields[index.get("VALOR_FAVORECIDO")].length()>0 && numeroBoletoDARF.length()<47)
			{
				agenciaFavorecido=fields[index.get("AGENCIA_FAVORECIDO")];
				contaFavorecido=Utils.cleanString(fields[index.get("CONTA_COM_DIGITO_FAVORECIDO")]);
				textValorFavorecido=fields[index.get("VALOR_FAVORECIDO")];
				if(textValorFavorecido.matches(".*\\d.*"))
				{
					System.out.println("ValorFavorecido: " +textValorFavorecido);
					if(textValorFavorecido.length()>=3)
					{
						if(textValorFavorecido.substring(textValorFavorecido.length()-3,textValorFavorecido.length()-2).contains(","))
						{
							System.out.println("Value with comma " + textValorFavorecido);
							textValorFavorecido=textValorFavorecido.replaceAll("\\.", "").replaceAll(",", ".");
						}
					}
					valorFavorecido=Double.parseDouble(textValorFavorecido.replaceAll(",", ""));
				}
				else 
				{
					textValorFavorecido=textValorFavorecido.replaceAll("\\.", "").replace(",", ".");
		
					if(textValorFavorecido.matches(".*\\d.*"))
					{
						valorFavorecido=Double.parseDouble(textValorFavorecido);
					}
					if(valorFavorecido==0)
					{
						System.out.println("Valor zero! Skipping line: "+line);
						continue;
					}
				}
			}
			
			String logradouroFavorecido=Utils.stringToANSI(fields[index.get("LOGRADOURO_FAVORECIDO")]);
			String numeroFavorecido=Utils.stringToANSI(Integer.toString((int)Double.parseDouble(fields[index.get("NUMERO_FAVORECIDO")])));
			String complementoFavorecido=Utils.stringToANSI(fields[index.get("COMPLEMENTO_FAVORECIDO")]);
			String bairroFavorecido=Utils.stringToANSI(fields[index.get("BAIRRO_FAVORECIDO")]);
			String cepFavorecido=fields[index.get("CEP_FAVORECIDO")];
			cepFavorecido=cepFavorecido.replaceAll("\\.", "").replaceAll("-", "");
			String cidadeFavorecido=Utils.stringToANSI(fields[index.get("CIDADE_FAVORECIDO")]);
			String ufFavorecido=fields[index.get("UF_FAVORECIDO")];
			String stringDataPagamento=null;
			
//			if(fields.length==17)
//			{
				stringDataPagamento=fields[index.get("DATA_PAGAMENTO")];
				bairroFavorecido=Utils.stringToANSI(fields[index.get("BAIRRO_FAVORECIDO")]);
				cepFavorecido=fields[index.get("CEP_FAVORECIDO")];
				cepFavorecido=cepFavorecido.replaceAll("\\.", "").replaceAll("-", "");
				cidadeFavorecido=Utils.stringToANSI(fields[index.get("CIDADE_FAVORECIDO")]);
				ufFavorecido=fields[index.get("UF_FAVORECIDO")];
//			}
//			else
//			{
//				stringDataPagamento=fields[index.get("DATA_PAGAMENTO")];
//			}
			String chavePIX="";
			if(index.get("CHAVE_PIX")!=null)
			{
				if(index.get("CHAVE_PIX")<fields.length)
				{
					chavePIX=fields[index.get("CHAVE_PIX")];
				}
			}
			String stringDataVencimento="";
			
			
			Date dataVencimento=null;
			
			if(index.get("DATA_VENCIMENTO")!=null)
			{
				stringDataVencimento=fields[index.get("DATA_VENCIMENTO")];
				try {
					if(stringDataVencimento.contains("/"))
					{
						dataVencimento=sdfx.parse(stringDataVencimento);
					}
					if(stringDataPagamento.contains("-"))
					{
						dataVencimento=sdfm.parse(stringDataVencimento);
					}
					
					System.out.println("StringDataVencimento: "+stringDataVencimento + " DataVencimento: "+sdfm.format(dataVencimento));
				} catch (ParseException e) {
					System.out.println("StringDataVencimento: "+stringDataVencimento + " DataVencimento: "+dataVencimento);
					e.printStackTrace();
				}

			}

		
			Date dataPagamento = null; 
			try {
				if(stringDataPagamento.contains("/"))
				{
					dataPagamento=sdfx.parse(stringDataPagamento);
				}
				if(stringDataPagamento.contains("-"))
				{
					dataPagamento=sdfm.parse(stringDataPagamento);
				}
				
				System.out.println("StringDataPagamento: "+stringDataPagamento + " DataPagamento: "+sdfm.format(dataPagamento));
			} catch (ParseException e) {
				System.out.println("StringDataPagamento: "+stringDataPagamento + " DataPagamento: "+dataPagamento);
				e.printStackTrace();
			}
			if(dataPagamento==null)
			{
				continue;
			}
			
			DadosFundo dadosFundo = new DadosFundo(connMySQL, cnpjFundo);			
			DadosBancarios dadosBancariosFundo = new DadosBancarios(connMySQL, dadosFundo, codigoBancoFundo, agenciaFundo, contaFundo);
			if(dadosBancariosFundo.getIdDadosBancarios()==0)
			{
				System.out.println(" ******************** Account not registered at the database! Will be missing details! *****************");		
				System.out.println(" Account incomplete! Skipping line: "+line);
				continue;
			}
			else 
			{
				if(codigoBancoFundo.contains("033") && dadosBancariosFundo.getDsname()==null)
				{
					System.out.println(" ******************** Account not complete at the database! Missing DSNAME! *****************");		
					System.out.println(" Account incomplete! Skipping line: "+line);
					continue;
					
				}
				dadosBancariosFundo.show();
			}
//			DadosBancarios dadosBancariosFundo = new DadosBancarios(connMySQL, dadosFundo, codigoBancoFundo, agenciaFundo, contaFundo);
//			dadosBancariosFundo.show();
			DadosBancarios dadosBancariosFavorecido =null;
			if(codigoBancoFavorecido.length()>0 && numeroBoletoDARF.length()<47)
			{
				dadosBancariosFavorecido = new DadosBancarios(connMySQL, codigoBancoFavorecido, agenciaFavorecido, contaFavorecido);
			}
			 
//			DadosBancarios dadosBancariosTeste = new DadosBancarios(connMySQL, rs.getInt("default_destinatario_envio_teste"));
			EnderecoCompleto enderecoFundoPagador = new EnderecoCompleto(Utils.stringToANSI("Avenida Dr. Cardoso de Melo"), "1184", "Nono andar", "Vila Olimpia", "04548000", "", "Sao Paulo", "SP");
			EnderecoCompleto enderecoFavorecido = new EnderecoCompleto(logradouroFavorecido, numeroFavorecido, complementoFavorecido, bairroFavorecido, cepFavorecido, "", cidadeFavorecido, ufFavorecido);

			Favorecido favorecido = new Favorecido(tipoInscricaoCadastroFavorecido,cadastroFavorecido, nomeFavorecido, dadosBancariosFavorecido, enderecoFavorecido,chavePIX,numeroBoletoDARF,dataVencimento);
			Pagador fundoPagador=new Pagador("2", dadosFundo.getCnpj(), dadosFundo.getNome(), enderecoFundoPagador, dadosBancariosFundo);
			
//			fundoPagador.setDadosBancariosTeste(dadosBancariosTeste);
			String seuNumeroMultipag=sdfsn.format(Calendar.getInstance().getTime());
			Utils.waitv(1);
			Credito credito = new Credito(seuNumeroMultipag, dataPagamento, "BRL", 0, valorFavorecido, "", dataPagamento, valorFavorecido);
			PagamentoFornecedor pagamentoFornecedor= new PagamentoFornecedor(fundoPagador,favorecido,credito);
			if(pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF().length()>0)
			{
				pagamentoFornecedor.setNomeArquivo(pagamentoFornecedor.getPagador().getNumeroInscricao()+"_"+pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF());
			}
			pagamentoFornecedor.show();
//			
			if(fundosPagamentos.get(fundoPagador.getNumeroInscricao())!=null)
			{
				fundosPagamentos.get(fundoPagador.getNumeroInscricao()).add(pagamentoFornecedor);
			}
			else
			{
				ArrayList<PagamentoFornecedor> pagamentos=new ArrayList<PagamentoFornecedor>();
				pagamentos.add(pagamentoFornecedor);
				fundosPagamentos.put(fundoPagador.getNumeroInscricao(), pagamentos);
			}
				
		}
		
		for(String key: fundosPagamentos.keySet())
		{
			System.out.println("Pagamentos para o fundo de CNPJ "+key);
			double valorTotal=0;
			int iPagamento=0;
			for(PagamentoFornecedor pagamento:fundosPagamentos.get(key))
			{
				valorTotal+=pagamento.getCredito().getValorPagamento();
				iPagamento++;
			}
			System.out.println(iPagamento + " pagamentos, valor total " + Utils.formatValorBrazilCurrency(valorTotal));
		}
	}


//	public static void sendAlert()
//	{
//		Utils.setTimeZone();
//
//		ArrayList<String> pathFiles = new ArrayList<String>();
//
//		if(!separateFiles)
//		{
//			String messageBody = "";
//			if(cnabs240.size()==0)
//			{
//				return;
//			}
//			else if(cnabs240.size()==1)
//			{
//				
//				File file = new File(cnabs240.get(0).getFileName());
//				String parent = file.getAbsoluteFile().getParent();
//				String nomeEmpresa="";
//				
//				if(cnabs240.get(0).getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
//				{
//					nomeEmpresa=cnabs240.get(0).getHeaderArquivoBradesco().getNomeEmpresa().getContent();
//				}
//				if(cnabs240.get(0).getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
//				{
//					nomeEmpresa=cnabs240.get(0).getHeaderArquivoItau().getNomeEmpresa().getContent();
//				}
//				
//				messageBody += "<br>Foi gerado o seguinte arquivo CNAB240 MULTIPAG<br>"
//						+ "- " + file.getName() + " na pasta " + parent + ", do " + nomeEmpresa + "<br><br>"
//						+ "<br>";
//				pathFiles.add(cnabs240.get(0).getFileName());
//			}
//			else
//			{
//				messageBody += "<br>Foram gerados os seguinte arquivos CNAB240 MULTIPAG<br>";
//				
//				
//				for(ArquivoCNAB240 cnab:cnabs240)
//				{
//					String nomeEmpresa="";
//					
//					if(cnab.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
//					{
//						nomeEmpresa=cnab.getHeaderArquivoBradesco().getNomeEmpresa().getContent();
//					}
//					if(cnab.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
//					{
//						nomeEmpresa=cnab.getHeaderArquivoItau().getNomeEmpresa().getContent();
//					}
//					
//					File file = new File(cnab.getFileName());
//					String parent = file.getAbsoluteFile().getParent();
//					messageBody+= "- " + file.getName() + " na pasta " + parent + ", do " + nomeEmpresa + "<br>";
//					pathFiles.add(cnab.getFileName());
//				}
//			}
//			
//	
//			String subject = "Arquivos Gerados - MULTIPAG - " + sdfh.format(Calendar.getInstance().getTime());
//			String destinatariosPara = emailSender;
//			String destinatariosCC="";
//			int iDestinatario=0;
//			for(String destinatario:destinatarios)
//			{
//				if(iDestinatario==0)
//				{
//					destinatariosCC+=destinatario;
//				}
//				else
//				{
//					destinatariosCC+=","+destinatario;
//				}
//				iDestinatario++;
//			}
//			
//			String destinatariosCCO="";
//			
//			SendEmailGmail sendAlerta = new SendEmailGmail(messageBody, 
//															subject, 
//															emailSender,  
//															passwordSender, 
//															nameSender, 
//															destinatariosPara, 
//															destinatariosCC, 
//															destinatariosCCO);
//			sendAlerta.setPathFiles(pathFiles);
//			sendAlerta.SendWithAttachments(companySender, nameSender, pathFiles);
//		}
//		else
//		{
//			
//			if(cnabs240.size()==0)
//			{
//				return;
//			}
//			else
//			{
//				for(ArquivoCNAB240 cnab240:cnabs240)
//				{
//					pathFiles=new ArrayList<String>();
//					
//					String nomeEmpresa="";
//					
//					if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
//					{
//						nomeEmpresa=cnab240.getHeaderArquivoBradesco().getNomeEmpresa().getContent();
//					}
//					if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
//					{
//						nomeEmpresa=cnab240.getHeaderArquivoItau().getNomeEmpresa().getContent();
//					}
//					
//					String messageBody = "";				
//					File file = new File(cnab240.getFileName());
//					String parent = file.getAbsoluteFile().getParent();
//					messageBody += "<br>Foi gerado o seguinte arquivo CNAB240 "+nomeBanco.get(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe())+"<br>"
//							+ "- " + file.getName() + " na pasta " + parent + ", do " + nomeEmpresa + "<br><br>"
//							+ "<br>";
//					pathFiles.add(cnab240.getFileName());
//					
//					String subject = "";
//					if(readCSV)
//					{
//						subject = "Pagamento gerado - " + sistemaBanco.get(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe()) + " - " + sdfh.format(Calendar.getInstance().getTime());
//					}
//					else
//					{
//						subject = "Pagamento gerado - " + sistemaBanco.get(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe()) + " - " + sdfh.format(Calendar.getInstance().getTime());
//					}
//					
//					if(test)
//					{
//						subject = "TEST - "+subject;
//					}
//					String destinatariosPara = emailSender;
//					String destinatariosCC="";
//					int iDestinatario=0;
//					for(String destinatario:destinatarios)
//					{
//						if(iDestinatario==0)
//						{
//							destinatariosCC+=destinatario;
//						}
//						else
//						{
//							destinatariosCC+=","+destinatario;
//						}
//						iDestinatario++;
//					}
//					
//					String destinatariosCCO="";
//					
//					SendEmailGmail sendAlerta = new SendEmailGmail(messageBody, 
//																	subject, 
//																	emailSender,  
//																	passwordSender, 
//																	nameSender, 
//																	destinatariosPara, 
//																	destinatariosCC, 
//																	destinatariosCCO);
//					sendAlerta.setPathFiles(pathFiles);
//					sendAlerta.SendWithAttachments(companySender, nameSender, pathFiles);
//					Utils.waitv("Waiting 5 seconds for next email",5);
//				}
//
//			}
//		}
//	}	

	public void build()
	{
		Utils.setTimeZone();

		HashMap<String,ArrayList<PagamentoFornecedor>> fundosPagamentosNovos = new HashMap<String,ArrayList<PagamentoFornecedor>>();
		String keyPagamentoNovo="";
		if(readCSV)
		{
			for(String key:fundosPagamentos.keySet())
			{
				System.out.println("Building payments for CNPJ:  "+key);
				for(PagamentoFornecedor pagamento:fundosPagamentos.get(key))
				{
					OperacaoMultipag opMultipag =null;
					if(pagamento.getFavorecido().getNumeroBoletoDARF().length()>0)
					{
						if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==47)
						{
							opMultipag = new OperacaoMultipag(connMySQL,
									pagamento.getPagador().getDadosBancarios(),
									Calendar.getInstance().getTime(), 
									pagamento.getCredito().getDataPagamento(), 
									pagamento.getCredito().getSeuNumero(),
									pagamento.getNomeArquivo(), 
									pagamento.getPagador().getNome(), 
									pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
									pagamento.getPagador().getDadosBancarios().getDadosBanco().getNome(), 
									pagamento.getPagador().getDadosBancarios().getAgencia(), 
									pagamento.getPagador().getDadosBancarios().getNumeroConta(), 
									pagamento.getPagador().getDadosBancarios().getDigitoConta(), 
									pagamento.getFavorecido().getNumeroInscricao(), 
									pagamento.getFavorecido().getNome(), 
									pagamento.getFavorecido().getNumeroBoletoDARF()
									); 	
						}
						if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==48)
						{
							opMultipag = new OperacaoMultipag(connMySQL,
									pagamento.getPagador().getDadosBancarios(),
									Calendar.getInstance().getTime(), 
									pagamento.getCredito().getDataPagamento(), 
									pagamento.getCredito().getSeuNumero(),
									pagamento.getNomeArquivo(), 
									pagamento.getPagador().getNome(), 
									pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
									pagamento.getPagador().getDadosBancarios().getDadosBanco().getNome(), 
									pagamento.getPagador().getDadosBancarios().getAgencia(), 
									pagamento.getPagador().getDadosBancarios().getNumeroConta(), 
									pagamento.getPagador().getDadosBancarios().getDigitoConta(), 
									"", 
									"DARF", 
									pagamento.getFavorecido().getNumeroBoletoDARF()
									); 	
						}
					}
					else {
						opMultipag = new OperacaoMultipag(connMySQL,
								pagamento.getPagador().getDadosBancarios(),
								Calendar.getInstance().getTime(), 
								pagamento.getCredito().getDataPagamento(), 
								pagamento.getCredito().getSeuNumero(),
								pagamento.getNomeArquivo(), 
								pagamento.getPagador().getNome(), 
								pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
								pagamento.getPagador().getDadosBancarios().getDadosBanco().getNome(), 
								pagamento.getPagador().getDadosBancarios().getAgencia(), 
								pagamento.getPagador().getDadosBancarios().getNumeroConta(), 
								pagamento.getPagador().getDadosBancarios().getDigitoConta(), 
								pagamento.getFavorecido().getNumeroInscricao(), 
								pagamento.getFavorecido().getNome(), 
								pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getNome(), 
								pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
								pagamento.getFavorecido().getDadosBancarios().getAgencia(), 
								pagamento.getFavorecido().getDadosBancarios().getNumeroConta(), 
								pagamento.getFavorecido().getDadosBancarios().getDigitoConta(), 
								pagamento.getCredito().getValorPagamento(),
								pagamento.getFavorecido().getTipoChavePIX(),
								pagamento.getFavorecido().getChavePIX()
								); 	
					}
					 
					keyPagamentoNovo=pagamento.getNomeArquivo()+pagamento.getCredito().getSeuNumero();
					if(opMultipag.isNovo() || opMultipag.isProblema())
					{
						if(opMultipag.isProblema())
						{
							pagamento.setProblema(true);
							pagamento.setTentativa(opMultipag.getTentativa());
						}
						
						if(fundosPagamentosNovos.get(keyPagamentoNovo)!=null)
						{
							fundosPagamentosNovos.get(keyPagamentoNovo).add(pagamento);
						}
						else
						{
							ArrayList<PagamentoFornecedor> pagamentos=new ArrayList<PagamentoFornecedor>();
							pagamentos.add(pagamento);
							fundosPagamentosNovos.put(keyPagamentoNovo, pagamentos);
						}
					}
					
				}
			}
		}
		else 
		{
			for(String key:fundosPagamentos.keySet())
			{
				System.out.println("Building payments for CNPJ:  "+key);
				if(dadosCorretosContaFundo.get(key)!=null)
				{
					for(PagamentoFornecedor pagamento:fundosPagamentos.get(key))
					{
						pagamento.getPagador().setDadosBancarios(dadosCorretosContaFundo.get(key));
						OperacaoMultipag opMultipag = new OperacaoMultipag(connMySQL, 
														pagamento.getPagador().getDadosBancarios(),
														Calendar.getInstance().getTime(), 
														pagamento.getCredito().getDataPagamento(), 
														pagamento.getCredito().getSeuNumero(),
														pagamento.getNomeArquivo(), 
														pagamento.getPagador().getNome(), 
														pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
														pagamento.getPagador().getDadosBancarios().getDadosBanco().getNome(), 
														pagamento.getPagador().getDadosBancarios().getAgencia(), 
														pagamento.getPagador().getDadosBancarios().getNumeroConta(), 
														pagamento.getPagador().getDadosBancarios().getDigitoConta(), 
														pagamento.getFavorecido().getNumeroInscricao(), 
														pagamento.getFavorecido().getNome(), 
														pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getNome(), 
														pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe(), 
														pagamento.getFavorecido().getDadosBancarios().getAgencia(), 
														pagamento.getFavorecido().getDadosBancarios().getNumeroConta(), 
														pagamento.getFavorecido().getDadosBancarios().getDigitoConta(), 
														pagamento.getCredito().getValorPagamento(),
														pagamento.getFavorecido().getTipoChavePIX(),
														pagamento.getFavorecido().getChavePIX()
														); 
						keyPagamentoNovo=pagamento.getNomeArquivo()+pagamento.getCredito().getSeuNumero();
						if(opMultipag.isNovo() || opMultipag.isProblema())
						{
							if(opMultipag.isProblema())
							{
								pagamento.setProblema(true);
								pagamento.setTentativa(opMultipag.getTentativa());
							}
							
							if(fundosPagamentosNovos.get(keyPagamentoNovo)!=null)
							{
								fundosPagamentosNovos.get(key).add(pagamento);
							}
							else
							{
								ArrayList<PagamentoFornecedor> pagamentos=new ArrayList<PagamentoFornecedor>();
								pagamentos.add(pagamento);
								fundosPagamentosNovos.put(keyPagamentoNovo, pagamentos);
							}
						}
						
					}
				}
				else
				{
					System.out.println("Fundo com CNPJ "+key+" sem dados bancários cadastrados!!!!");
				}
			}			
		}
		
		for(String key:fundosPagamentosNovos.keySet())
		{
			if(separateFiles)
			{				
				if(fundosPagamentosNovos.get(key).size()>0)
				{
					HashMap<String,ArrayList<PagamentoFornecedor>> pagamentosRemessa = new HashMap<String,ArrayList<PagamentoFornecedor>>();
					
					for(PagamentoFornecedor pagamento:fundosPagamentosNovos.get(key))
					{
						String keyPagamento = pagamento.getNomeArquivo()+pagamento.getCredito().getSeuNumero();
						if(pagamentosRemessa.get(pagamento.getNomeArquivo())==null)
						{
							ArrayList<PagamentoFornecedor> pagamentos=new ArrayList<PagamentoFornecedor>();
							pagamentos.add(pagamento);
							if(test)
							{
								pagamentosRemessa.put(pagamento.getPagador().getNumeroInscricao(), pagamentos);
							}
							else
							{
								pagamentosRemessa.put(pagamento.getNomeArquivo(), pagamentos);
							}
						}
						else 
						{
							if(test)
							{
								pagamentosRemessa.get(pagamento.getPagador().getNumeroInscricao()).add(pagamento);
							}
							else
							{
								pagamentosRemessa.get(pagamento.getNomeArquivo()).add(pagamento);
							}
						}
					}
					
					System.out.println("Número de pagamentos: " + pagamentosRemessa.size());
					
					for(String keyArquivo:pagamentosRemessa.keySet())
					{
						for(PagamentoFornecedor pagamentoFornecedor:pagamentosRemessa.get(keyArquivo))
						{
							String cnpjFundo=pagamentoFornecedor.getPagador().getNumeroInscricao();
							String pathDate = sdff.format(Calendar.getInstance().getTime());
							
							String stringFile="";
													
							String outputPathFile = outputPath.get(pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
							
							
							System.out.println("KeyPayment: " + pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
							for(String keyOutput:outputPath.keySet())
							{
								System.out.println("KeyOutput: " + keyOutput  + " Path: "+outputPath.get(keyOutput));
							}
							
							if(readCSV)
							{
								System.out.println("CodigoCompePagador: "+pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
								outputPathFile = outputPath.get(pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
							}
							if(test)
							{
								outputPathFile = outputPath.get("000");
							}
							
							System.out.println("OutputPath: " + outputPathFile);
											
							String pathFund = cnpjFundo;
	//						System.out.println("OutputPath + datePath + cnpjFundo: " +pathFund);								
							
							String rootArquivo = "";
							if(test)
							{
								rootArquivo = cnpjFundo;
							}
							else if(keyArquivo.length()>=18)
							{
								rootArquivo = keyArquivo.substring(0,18);
							}
							else if(keyArquivo.length()>=5)
							{
								rootArquivo = keyArquivo.substring(0,keyArquivo.length()-4);
							}
							
							boolean PIXProcessed=false;
							if(pagamentoFornecedor.getPagador().getDadosBancarios().isPagamentoPIX())
							{
								System.out.println("---- Have to perfom a PIX payment ----");
								if(pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
								{
									System.out.println("********* We need to generate a PIX payment here!");
									double valorPagamento = pagamentoFornecedor.getCredito().getValorPagamento();
									String nomeArquivoString=keyArquivo;
									String ispb=pagamentoFornecedor.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoISPB();
									String tipoIdentificacaoConta="CC";
									String agenciaRecebedor=pagamentoFornecedor.getFavorecido().getDadosBancarios().getAgencia();
									String contaRecebedor=pagamentoFornecedor.getFavorecido().getDadosBancarios().getNumeroConta()+pagamentoFornecedor.getFavorecido().getDadosBancarios().getDigitoConta();
									String tipoIdentificacaoRecebedor="J";
									String identificacaoRecebedor=pagamentoFornecedor.getFavorecido().getNumeroInscricao();
									String informacoesEntreUsuarios="Pagamento de operacao "+keyArquivo;
									String referenciaEmpresa=nomeArquivoString.substring(0,14)+nomeArquivoString.substring(15,18)+"PIX";
									String identificacaoComprovante="Pagamento de operacao "+keyArquivo;
									String txid=Utils.uniqueStringPIX(35, nomeArquivoString, agenciaRecebedor, contaRecebedor, identificacaoRecebedor, valorPagamento);
									String tipoConta="CC";
									String agencia=pagamentoFornecedor.getPagador().getDadosBancarios().getAgencia();
									String conta=pagamentoFornecedor.getPagador().getDadosBancarios().getNumeroConta()+pagamentoFornecedor.getPagador().getDadosBancarios().getDigitoConta();
									String tipoChavePIX=pagamentoFornecedor.getFavorecido().getTipoChavePIX();
									String chavePIX=pagamentoFornecedor.getFavorecido().getChavePIX();
									System.out.println("ContaOriginal: " + conta);
									if(conta.length()<8)
									{
										int missingZeroes = 8 - conta.length();
										for(int i=0;i<missingZeroes;i++)
										{
											conta="0"+conta;
										}
									}
									
		//							conta="00223416";
									System.out.println("ContaModificada: " + conta);
									String tipoPessoa="J";
									if(identificacaoRecebedor.length()==11)
									{
										tipoIdentificacaoRecebedor="F";
										tipoPessoa="F";											
									}
	
									String documento=pagamentoFornecedor.getPagador().getNumeroInscricao();
									String moduloSispag="Fornecedores";
									Date dataPagamento = Calendar.getInstance().getTime();
		
									Authentication authentication=new Authentication(connMySQL,pagamentoFornecedor.getPagador().getDadosBancarios());
									authentication.obtainAccessToken();
									authentication.getAccessToken().show();
									
									Date inicioDate = null;
									try {
										inicioDate = sdfm.parse("2022-03-01");
									} catch (ParseException e) {
										e.printStackTrace();
									}
									Date fimDate = null;
									try {
										fimDate = sdfm.parse("2022-03-29");
									} catch (ParseException e) {
										e.printStackTrace();
									}
									
									inicioDate  = Calendar.getInstance().getTime();
									fimDate = Calendar.getInstance().getTime();
									String jsonConsulta = PagamentoPIXSispag.consulta(authentication, agencia, conta, documento, "Detalhada", inicioDate, fimDate);	
									System.out.println("jsonConsulta: "+jsonConsulta);
									System.out.println("jsonConsultaLen: " + jsonConsulta.length());
									ArrayList<DetalhePagamento> detalhesConsulta = PagamentoPIXSispag.parseJsonConsulta(jsonConsulta);
									PagamentoFornecedor pagamento=pagamentosRemessa.get(keyArquivo).get(0);
									boolean sentAlready=false;
									System.out.println("Transferencias consultadas: " + detalhesConsulta.size());
									for(DetalhePagamento detalhe:detalhesConsulta)
									{
		//								detalhe.show();
										if(detalhe.getReferenciaEmpresa()!=null)
										{
											if(detalhe.getReferenciaEmpresa().toLowerCase().contains(referenciaEmpresa.toLowerCase()))
											{
												detalhe.show();
												sentAlready=true;
												System.out.println("****************** Payment sent already - Remote prove! ****************");
												OperacaoMultipag opMultipag = new OperacaoMultipag(connMySQL,
														Calendar.getInstance().getTime(), 
														Calendar.getInstance().getTime(), 
														pagamento.getCredito().getSeuNumero(),
														pagamento.getNomeArquivo(), 
														pagamento.getFavorecido().getDadosBancarios().getNumeroConta(), 
														pagamento.getFavorecido().getDadosBancarios().getDigitoConta(), 
														pagamento.getCredito().getValorPagamento(),
														pagamento.getFavorecido().getTipoChavePIX(),
														pagamento.getFavorecido().getChavePIX()
														);
							
														if(opMultipag.getIdOperacoesMultipag()!=0)
														{
															opMultipag.setNomeCNAB(referenciaEmpresa);
															opMultipag.updateCnabGerado(connMySQL, true, test);
															opMultipag.updateProblema(connMySQL, false);
														}
												break;
											}
										}
									}
									if(!sentAlready)
									{
										System.out.println(" Payment not found in remote history!");
									}
									
									OperacaoMultipag operacaoMultipag = new OperacaoMultipag(connMySQL, Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), pagamento.getCredito().getSeuNumero(), pagamento.getNomeArquivo(), referenciaEmpresa);
									if(operacaoMultipag.getIdOperacoesMultipag()!=0)
									{
										sentAlready=true;
										System.out.println("****************** Payment sent already - Local register! *****************");
									}
									else
									{
										System.out.println(" Payment not found in local history!");
									}
									
									if(!sentAlready)
									{
										authentication.obtainAccessToken();
										PagamentoPIXDadosBancarios pagamentoPIXDadosBancarios=new PagamentoPIXDadosBancarios();
										dataPagamento=Calendar.getInstance().getTime();
										pagamentoPIXDadosBancarios = new PagamentoPIXDadosBancarios(valorPagamento, 
												dataPagamento, ispb, tipoIdentificacaoConta, agenciaRecebedor, contaRecebedor, 
												tipoIdentificacaoRecebedor, identificacaoRecebedor, informacoesEntreUsuarios, referenciaEmpresa, 
												identificacaoComprovante, txid, tipoConta, agencia, conta, tipoPessoa, documento, moduloSispag,tipoChavePIX,chavePIX);
										String jsonPagamento =pagamentoPIXDadosBancarios.toJSON();
										System.out.println(jsonPagamento);
										String responsePayment = PagamentoPIXSispag.sendPayment(authentication, jsonPagamento);
										System.out.println(responsePayment);
																	
										OperacaoMultipag opMultipag = new OperacaoMultipag(connMySQL,  
										Calendar.getInstance().getTime(),
										Calendar.getInstance().getTime(), 
										pagamento.getCredito().getSeuNumero(),
										pagamento.getNomeArquivo(), 
										pagamento.getFavorecido().getDadosBancarios().getNumeroConta(), 
										pagamento.getFavorecido().getDadosBancarios().getDigitoConta(), 
										pagamento.getCredito().getValorPagamento(),
										pagamento.getFavorecido().getTipoChavePIX(),
										pagamento.getFavorecido().getChavePIX()
												);
			
										if(opMultipag.getIdOperacoesMultipag()!=0)
										{
											opMultipag.setNomeCNAB(referenciaEmpresa);
											opMultipag.updateProblema(connMySQL, false);									
										}
										opMultipag.updateCnabGerado(connMySQL, true, test);
										opMultipag.updateCnabEnviado(connMSSQL, true, test);
									}
									PIXProcessed=true;
								}
								if(pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("033"))
								{
									
									String tipoIdentificacaoRecebedor="";
									String tipoPessoa="";
									if(pagamentoFornecedor.getFavorecido().getNumeroInscricao().length()==11)
									{
										tipoIdentificacaoRecebedor="F";
										tipoPessoa="F";
									}
									else
									{
										tipoIdentificacaoRecebedor="J";
										tipoPessoa="J";
									}
									double valorPagamento=pagamentoFornecedor.getCredito().getValorPagamento();
									Date dataPagamento=Calendar.getInstance().getTime();
									String compe=pagamentoFornecedor.getFavorecido().getDadosBancarios().getCodigoBanco();
									String ispb="";
									String tipoIdentificacaoConta="CC";
									String agenciaRecebedor=pagamentoFornecedor.getFavorecido().getDadosBancarios().getAgencia();
									String contaRecebedor=pagamentoFornecedor.getFavorecido().getDadosBancarios().getNumeroConta()+pagamentoFornecedor.getFavorecido().getDadosBancarios().getCodigoBanco();
									String identificacaoRecebedor=pagamentoFornecedor.getFavorecido().getNumeroInscricao();
									String informacoesEntreUsuarios="Nothing to put here";
									String referenciaEmpresa=pagamentoFornecedor.getCredito().getNossoNumero();
									String identificacaoComprovante=pagamentoFornecedor.getCredito().getNossoNumero();
									String tipoConta="CC";
									String agencia="";
									String conta="";
									String documento=pagamentoFornecedor.getFavorecido().getNumeroInscricao();
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
								AuthenticationPixSantander authenticationPixSantander = new AuthenticationPixSantander(connMySQL, pagamentoFornecedor.getPagador().getNumeroInscricao());
									authenticationPixSantander.obtainAccessToken();
									authenticationPixSantander.obtainWorkspaceId();
								PixPaymentByReceiver pixPaymentByReceiver = new PixPaymentByReceiver(connMySQL, null, pagamentoPIXDadosBancarios);
									pixPaymentByReceiver.sendRequisitionInitiate();
									OperacaoMultipag opMultipag = new OperacaoMultipag(connMySQL,  
											Calendar.getInstance().getTime(),
											Calendar.getInstance().getTime(), 
											pagamentoFornecedor.getCredito().getSeuNumero(),
											pagamentoFornecedor.getNomeArquivo(), 
											pagamentoFornecedor.getFavorecido().getDadosBancarios().getNumeroConta(), 
											pagamentoFornecedor.getFavorecido().getDadosBancarios().getDigitoConta(), 
											pagamentoFornecedor.getCredito().getValorPagamento(),
											pagamentoFornecedor.getFavorecido().getTipoChavePIX(),
											pagamentoFornecedor.getFavorecido().getChavePIX()
													);
				
									if(opMultipag.getIdOperacoesMultipag()!=0)
									{
										opMultipag.setNomeCNAB(referenciaEmpresa);
										opMultipag.updateCnabGerado(connMySQL, true, test);
										opMultipag.updateProblema(connMySQL, false);
										if(opMultipag.getDadosBancariosFundo().getDadosBanco().getCodigoCompe().contains("460"))
										{
											opMultipag.updateCnabEnviado(connMSSQL, true,test);
										}
									}
									if(pixPaymentByReceiver.getIdPix().length()>0)
									{
										opMultipag.updateIdPix(connMySQL,pixPaymentByReceiver.getIdPix());
										PIXProcessed=true;
									}																	
								}
							}
							
							if(PIXProcessed)
							{
								continue;
							}
							String pathFolder = outputPathFile+File.separator+pathDate + File.separator + pathFund + File.separator+rootArquivo;						
							String filePath = pathFolder+File.separator+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
	
							String codigoCompe=pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe();
							boolean noFileNeeded=false;
							
							switch (codigoCompe) 
							{
								case "033":
									filePath = pathFolder+File.separator+pagamentoFornecedor.getPagador().getDadosBancarios().getDsname()+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									System.out.println(" DSName included from pagamentosRemessa: "+pagamentoFornecedor.getPagador().getDadosBancarios().getDsname());
									break;
								case "341":
									filePath = pathFolder+File.separator+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									break;
								case "460":
									filePath = pathFolder+File.separator+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									break;
								case "435":
									filePath = "DelFinance_"+sdfc.format(Calendar.getInstance().getTime());
									noFileNeeded=true;
									break;
								default:
									break;
							}
							System.out.println("FilePath: " + filePath);
							
							File dir = new File(pathFolder);
							if(!dir.exists())
							{
								dir.mkdirs();
							}
							
							SequenciaArquivo sequenciaArquivo = new SequenciaArquivo(connMySQL, Calendar.getInstance().getTime(), filePath, pagamentoFornecedor.getPagador().getDadosBancarios().getConvenio());
							if(test)
							{
								sequenciaArquivo = new SequenciaArquivo(Calendar.getInstance().getTime(), 1, filePath);
							}
													
									
							ArquivoCNAB240 cnab240= null;			
							if(noFileNeeded)
							{
								System.out.println("No file will be generated!");
							}
							else {
								cnab240= new ArquivoCNAB240(pagamentoFornecedor.getPagador(), pagamentosRemessa.get(keyArquivo), sequenciaArquivo.getSequencia());
								filePath += Integer.toString(sequenciaArquivo.getSequencia())+".rem";
								sequenciaArquivo.updateNomeArquivo(filePath);	
							}
							
							File file = new File(filePath);
							System.out.println(" File to be generated " + file.getName() + " in path " + pathFolder);
							
	
							if(endLinux && !noFileNeeded)
							{
								switch (codigoCompe) {
								case "237":
									stringFile+=cnab240.getHeaderArquivoBradesco().lineRegister()+"\n";								
									break;
								case "341":
									stringFile+=cnab240.getHeaderArquivoItau().lineRegister()+"\n";
									break;
								case "033":
									stringFile+=cnab240.getHeaderArquivoSantander().lineRegister()+"\n";
									break;
								case "460":
									stringFile+=cnab240.getHeaderArquivoUnavanti().lineRegister()+"\n";
									break;
								default:
									break;
								}
	
								for(Lote240 lote:cnab240.getLotes())
								{
									switch (codigoCompe) {
									case "237":
										stringFile+=lote.getHeaderLoteBradesco().lineRegister()+"\n";
										for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
										{
											stringFile+=detalhe.getDetalheABradesco().lineRegister()+"\n";
											stringFile+=detalhe.getDetalheBBradesco().lineRegister()+"\n";										
										}
										stringFile+=lote.getTrailerLoteBradesco().lineRegister()+"\n";
										break;
									case "341":
										stringFile+=lote.getHeaderLoteItau().lineRegister()+"\n";
										for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
										{
											stringFile+=detalhe.getDetalheAItau().lineRegister()+"\n";
											stringFile+=detalhe.getDetalheBItau().lineRegister()+"\n";										
										}
										stringFile+=lote.getTrailerLoteItau().lineRegister()+"\n";
										break;
									case "033":
										stringFile+=lote.getHeaderLoteSantander().lineRegister()+"\n";
										for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
										{
											if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()>0)
											{
												if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==47)
												{
													stringFile+=detalhe.getDetalheJSantander().lineRegister()+"\n";
													stringFile+=detalhe.getDetalheJ52Santander().lineRegister()+"\n";
												}
												if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==48)
												{
													stringFile+=detalhe.getDetalheOSantander().lineRegister()+"\n";
												}
											}
											else {
												stringFile+=detalhe.getDetalheASantander().lineRegister()+"\n";
												stringFile+=detalhe.getDetalheBSantander().lineRegister()+"\n";																						
											}
										}
										stringFile+=lote.getTrailerLoteSantander().lineRegister()+"\n";
										break;
									case "460":
										stringFile+=lote.getHeaderLoteUnavanti().lineRegister()+"\n";
										for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
										{
											stringFile+=detalhe.getDetalheAUnavanti().lineRegister()+"\n";
											stringFile+=detalhe.getDetalheBUnavanti().lineRegister()+"\n";										
										}
										stringFile+=lote.getTrailerLoteUnavanti().lineRegister()+"\n";
										break;
									default:
										break;
									}
								}
								
								if(blankLine)
								{
									switch (codigoCompe) {
									case "237":									
										stringFile+=cnab240.getTrailerArquivoBradesco().lineRegister()+"\n";
										break;
									case "341":									
										stringFile+=cnab240.getTrailerArquivoItau().lineRegister()+"\n";
										break;
									case "033":									
										stringFile+=cnab240.getTrailerArquivoSantander().lineRegister()+"\n";
										break;
									case "460":									
										stringFile+=cnab240.getTrailerArquivoUnavanti().lineRegister()+"\n";
										break;
									default:
										break;
									}
								}
								else
								{
									switch (codigoCompe) {
									case "237":									
										stringFile+=cnab240.getTrailerArquivoBradesco().lineRegister();
										break;
									case "341":									
										stringFile+=cnab240.getTrailerArquivoItau().lineRegister();
										break;
									case "033":									
										stringFile+=cnab240.getTrailerArquivoSantander().lineRegister();
										break;
									case "460":									
										stringFile+=cnab240.getTrailerArquivoUnavanti().lineRegister();
										break;
									default:
										break;
									}
								}
	
							}
							else
							{
								if(noFileNeeded)
								{
									
								}
								else
								{
									switch (codigoCompe) {
									case "237":
										stringFile+=cnab240.getHeaderArquivoBradesco().lineRegister()+"\r\n";								
										break;
									case "341":
										stringFile+=cnab240.getHeaderArquivoItau().lineRegister()+"\r\n";
										break;
									case "033":
										stringFile+=cnab240.getHeaderArquivoSantander().lineRegister()+"\r\n";
										break;
									case "460":
										stringFile+=cnab240.getHeaderArquivoUnavanti().lineRegister()+"\r\n";
										break;
									default:
										break;
									}
								
									for(Lote240 lote:cnab240.getLotes())
									{
										switch (codigoCompe) {
										case "237":
											stringFile+=lote.getHeaderLoteBradesco().lineRegister()+"\r\n";
											for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
											{
												stringFile+=detalhe.getDetalheABradesco().lineRegister()+"\r\n";
												stringFile+=detalhe.getDetalheBBradesco().lineRegister()+"\r\n";										
											}
											stringFile+=lote.getTrailerLoteBradesco().lineRegister()+"\r\n";
											break;
										case "341":
											stringFile+=lote.getHeaderLoteItau().lineRegister()+"\r\n";
											for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
											{
												stringFile+=detalhe.getDetalheAItau().lineRegister()+"\r\n";
												stringFile+=detalhe.getDetalheBItau().lineRegister()+"\r\n";										
											}
											stringFile+=lote.getTrailerLoteItau().lineRegister()+"\r\n";
											break;
										case "033":
											stringFile+=lote.getHeaderLoteSantander().lineRegister()+"\r\n";
											for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
											{
												if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()>0)
												{
													if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==47)
													{
														stringFile+=detalhe.getDetalheJSantander().lineRegister()+"\n";
														stringFile+=detalhe.getDetalheJ52Santander().lineRegister()+"\n";
													}
													if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==48)
													{
														stringFile+=detalhe.getDetalheOSantander().lineRegister()+"\n";
													}
												}
												else {
													stringFile+=detalhe.getDetalheASantander().lineRegister()+"\n";
													stringFile+=detalhe.getDetalheBSantander().lineRegister()+"\n";																						
												}
											}
											stringFile+=lote.getTrailerLoteSantander().lineRegister()+"\r\n";
											break;
										case "460":
											stringFile+=lote.getHeaderLoteUnavanti().lineRegister()+"\r\n";
											for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
											{
												stringFile+=detalhe.getDetalheAUnavanti().lineRegister()+"\r\n";
												stringFile+=detalhe.getDetalheBUnavanti().lineRegister()+"\r\n";										
											}
											stringFile+=lote.getTrailerLoteUnavanti().lineRegister()+"\r\n";
											break;
										default:
											break;
										}								
									}
									if(blankLine)
									{
										switch (codigoCompe) {
										case "237":									
											stringFile+=cnab240.getTrailerArquivoBradesco().lineRegister()+"\r\n";
											break;
										case "341":									
											stringFile+=cnab240.getTrailerArquivoItau().lineRegister()+"\r\n";
											break;
										case "033":									
											stringFile+=cnab240.getTrailerArquivoSantander().lineRegister()+"\r\n";
											break;
										case "460":									
											stringFile+=cnab240.getTrailerArquivoUnavanti().lineRegister()+"\r\n";
											break;
										default:
											break;
										}
									}
									else
									{
										switch (codigoCompe) {
										case "237":									
											stringFile+=cnab240.getTrailerArquivoBradesco().lineRegister();
											break;
										case "341":									
											stringFile+=cnab240.getTrailerArquivoItau().lineRegister();
											break;
										case "033":									
											stringFile+=cnab240.getTrailerArquivoSantander().lineRegister();
											break;
										case "460":									
											stringFile+=cnab240.getTrailerArquivoUnavanti().lineRegister();
											break;
										default:
											break;
										}
									}
								}
							}
							/*
							 * 
							 */
							if(noFileNeeded)
							{
								
							}
							else
							{								
								System.out.println("Arquivo com sequencia"+ sequenciaArquivo.getSequencia() + filePath);
								System.out.println(stringFile);
								
								Utils.stringToFile(stringFile, filePath);
								
									System.out.println("Storing " + pagamentoFornecedor.getNomeArquivo() + " SeuNumero " +pagamentoFornecedor.getCredito().getSeuNumero());
									if(pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF().length()>0)
									{
										if(pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF().length()==47)
										{
											DadosBancarios dadosBancarios=new DadosBancarios();
											pagamentoFornecedor.getFavorecido().setDadosBancarios(dadosBancarios);
										}
									}
									OperacaoMultipag opMultipag = new OperacaoMultipag();
									if(pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF().length()==48)
									{
										DarfNumerado darf = new DarfNumerado(pagamentoFornecedor.getFavorecido().getNumeroBoletoDARF());
										opMultipag = new OperacaoMultipag(connMySQL,  
												Calendar.getInstance().getTime(),
												pagamentoFornecedor.getCredito().getDataPagamento(), 
												pagamentoFornecedor.getCredito().getSeuNumero(),
												pagamentoFornecedor.getNomeArquivo(), 
											    "", 
												"", 
												darf.getValorReais(),
												pagamentoFornecedor.getFavorecido().getTipoChavePIX(),
												pagamentoFornecedor.getFavorecido().getChavePIX()
												); 
									}
									else
									{
										opMultipag = new OperacaoMultipag(connMySQL,  
																Calendar.getInstance().getTime(),
																pagamentoFornecedor.getCredito().getDataPagamento(), 
																pagamentoFornecedor.getCredito().getSeuNumero(),
																pagamentoFornecedor.getNomeArquivo(), 
																pagamentoFornecedor.getFavorecido().getDadosBancarios().getNumeroConta(), 
																pagamentoFornecedor.getFavorecido().getDadosBancarios().getDigitoConta(), 
																pagamentoFornecedor.getCredito().getValorPagamento(),
																pagamentoFornecedor.getFavorecido().getTipoChavePIX(),
																pagamentoFornecedor.getFavorecido().getChavePIX()
											);
									}
									if(opMultipag.getIdOperacoesMultipag()!=0)
									{
										opMultipag.setNomeCNAB(file.getName());
										opMultipag.updateCnabGerado(connMySQL, true, test);
										opMultipag.updateProblema(connMySQL, false);
										if(opMultipag.getDadosBancariosFundo().getDadosBanco().getCodigoCompe().contains("460"))
										{
											opMultipag.updateCnabEnviado(connMSSQL, true, test);
										}
									}
								cnab240.setFileName(filePath);
								cnabs240.add(cnab240);
							}
						}
					}
				}
				else
				{
					if(fundosPagamentosNovos.get(key).size()>0)
					{
						for(PagamentoFornecedor pagamentoFornecedor:fundosPagamentosNovos.get(key))
						{
							String stringFile="";
							String pathFund = outputPath+key;
							String pathDate = sdff.format(Calendar.getInstance().getTime());
							String pathTime= sdft.format(Calendar.getInstance().getTime());
							
							String pathFolder = pathFund+separator+pathDate+separator+pathTime; 
							String filePath = pathFolder+separator+"CNAB"+sdff.format(Calendar.getInstance().getTime());
		
							String codigoCompe=pagamentoFornecedor.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe();
							switch (codigoCompe) 
							{
								case "033":
									filePath = pathFolder+separator+pagamentoFornecedor.getPagador().getDadosBancarios().getDsname()+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									System.out.println(" DSName included from fundosPagamentosNovos: "+pagamentoFornecedor.getPagador().getDadosBancarios().getDsname());
									break;
								case "341":
									filePath = pathFolder+separator+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									break;
								case "460":
									filePath = pathFolder+separator+"CVCB_"+sdfc.format(Calendar.getInstance().getTime());
									break;
								default:
									break;
							}
							
							File dir = new File(pathFolder);
							if(!dir.exists())
							{
								dir.mkdirs();
							}
							
							SequenciaArquivo sequenciaArquivo = new SequenciaArquivo(connMySQL, Calendar.getInstance().getTime(), filePath);
							
							
							ArquivoCNAB240 cnab240= new ArquivoCNAB240(pagamentoFornecedor.getPagador(), fundosPagamentosNovos.get(key), sequenciaArquivo.getSequencia());
			
							
							
							filePath += "_"+Integer.toString(sequenciaArquivo.getSequencia())+".rem";
							sequenciaArquivo.updateNomeArquivo(filePath);
							
							File file = new File(filePath);
							System.out.println(" File to be generated " + file.getName() + " in path " + pathFolder);
							
							if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
							{
								stringFile+=cnab240.getHeaderArquivoBradesco().lineRegister()+"\r\n";
							}
							
							if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
							{
								stringFile+=cnab240.getHeaderArquivoItau().lineRegister()+"\r\n";
							}
							
		
							for(Lote240 lote:cnab240.getLotes())
							{
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
								{
									stringFile+=lote.getHeaderLoteBradesco().lineRegister()+"\r\n";
								}
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
								{
									stringFile+=lote.getHeaderLoteItau().lineRegister()+"\r\n";
								}
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("033"))
								{
									stringFile+=lote.getHeaderLoteSantander().lineRegister()+"\r\n";
								}
								
								for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
								{
									if(detalhe.getPagamento().getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
									{
										stringFile+=detalhe.getDetalheABradesco().lineRegister()+"\r\n";
										stringFile+=detalhe.getDetalheBBradesco().lineRegister()+"\r\n";
									}
									
									if(detalhe.getPagamento().getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
									{
										stringFile+=detalhe.getDetalheAItau().lineRegister()+"\r\n";
										stringFile+=detalhe.getDetalheBItau().lineRegister()+"\r\n";
									}
									
									if(detalhe.getPagamento().getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("033"))
									{
										if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()>0)
										{
											if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==47)
											{
												stringFile+=detalhe.getDetalheJSantander().lineRegister()+"\r\n";
												stringFile+=detalhe.getDetalheJ52Santander().lineRegister()+"\r\n";
											}
											if(detalhe.getPagamento().getFavorecido().getNumeroBoletoDARF().length()==48)
											{
												stringFile+=detalhe.getDetalheOSantander().lineRegister()+"\r\n";
											}
										}
										else {
											stringFile+=detalhe.getDetalheASantander().lineRegister()+"\r\n";
											stringFile+=detalhe.getDetalheBSantander().lineRegister()+"\r\n";																				
										}
									}
								}
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
								{
									stringFile+=lote.getTrailerLoteBradesco().lineRegister()+"\r\n";
								}
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
								{
									stringFile+=lote.getTrailerLoteBradesco().lineRegister()+"\r\n";
								}
								if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("033"))
								{
									stringFile+=lote.getTrailerLoteBradesco().lineRegister()+"\r\n";
								}
							}
							if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("237"))
							{
								stringFile+=cnab240.getTrailerArquivoBradesco().lineRegister()+"\r\n";
							}
							if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("341"))
							{
								stringFile+=cnab240.getTrailerArquivoItau().lineRegister()+"\r\n";
							}
							if(cnab240.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe().contains("033"))
							{
								stringFile+=cnab240.getTrailerArquivoSantander().lineRegister()+"\r\n";
							}
							/*
							 * 
							 */
			
							System.out.println("Arquivo "+ sequenciaArquivo.getSequencia() + filePath);
							System.out.println(stringFile);
							
							Utils.stringToFile(stringFile, filePath);
							
//							for(PagamentoFornecedor pagamento:fundosPagamentosNovos.get(key))
//							{
								System.out.println("Storing " + pagamentoFornecedor.getNomeArquivo() + " SeuNumero " +pagamentoFornecedor.getCredito().getSeuNumero());
								OperacaoMultipag opMultipag = new OperacaoMultipag(connMySQL,  
															Calendar.getInstance().getTime(),
															pagamentoFornecedor.getCredito().getDataPagamento(), 
															pagamentoFornecedor.getCredito().getSeuNumero(),
															pagamentoFornecedor.getNomeArquivo(), 
															pagamentoFornecedor.getFavorecido().getDadosBancarios().getNumeroConta(), 
															pagamentoFornecedor.getFavorecido().getDadosBancarios().getDigitoConta(), 
															pagamentoFornecedor.getCredito().getValorPagamento(),
															pagamentoFornecedor.getFavorecido().getTipoChavePIX(),
															pagamentoFornecedor.getFavorecido().getChavePIX()
										);
								if(opMultipag.getIdOperacoesMultipag()!=0)
								{
									opMultipag.setNomeCNAB(file.getName());
									opMultipag.updateCnabGerado(connMySQL, true, test);
									opMultipag.updateProblema(connMySQL, false);
									if(opMultipag.getDadosBancariosFundo().getDadosBanco().getCodigoCompe().contains("460"))
									{
										opMultipag.updateCnabEnviado(connMSSQL, true, test);
									}
								}
//							}
							cnab240.setFileName(filePath);
							cnabs240.add(cnab240);
						}
					}
				}
			}
		}
	}

	public static void setupDadosBancarios()
	{
		String query = "select * "
					+ " from dados_bancarios_fundo where ativo=1";
		System.out.println(query);
		
		Statement st=null;
		
		try {
			st=connMySQL.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				DadosFundo dadosFundo = new DadosFundo(connMySQL, rs.getInt("dados_fundo_id_dados_fundo"));
				DadosBanco dadosBanco = new DadosBanco(connMySQL, rs.getInt("dados_banco_id_dados_banco"));
				
				String cnpjFundo = rs.getString("cnpj_fundo");
				DadosBancarios dadosBancarios = new DadosBancarios(rs.getInt("id_dados_bancarios_fundo")
												, dadosBanco
												, dadosFundo
												, rs.getString("convenio")
												, rs.getString("agencia")
												, rs.getString("digito_agencia")
												, rs.getString("conta")
												,rs.getString("digito_conta")
												, ""
												,rs.getString("dsname")
												,rs.getInt("ativo")
												, rs.getInt("aprovado")
												, rs.getInt("branco")
												, rs.getInt("pagamentos_pix")
												);
				dadosCorretosContaFundo.put(cnpjFundo, dadosBancarios);
				dadosBancarios.show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			query="select * from versao_layout_cnab240";
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				versaoLayout.put(rs.getString("codigo_banco"), rs.getString("versao"));
				versaoLayoutLote.put(rs.getString("codigo_banco"), rs.getString("versao_lote"));
				nomeBanco.put(rs.getString("codigo_banco"), rs.getString("nome_banco"));
				sistemaBanco.put(rs.getString("codigo_banco"), rs.getString("sistema"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String key:versaoLayout.keySet())
		{
			System.out.println(key + " Arquivo: " + versaoLayout.get(key) +  " Lote: " +versaoLayoutLote.get(key));
		}
	}
	
	public static void setupPayments()
	{
		HashMap<String, String> paymentProcessed=new HashMap<String,String>();
		String query = "select * "
				+ "from dbo.vw_multipag "
				+ "where data_entrada='"+sdfm.format(Calendar.getInstance().getTime())+"'"
//				+ " and descricao_situacao='Aguardando aprova��o do Gestor de TED'"
				;
		
		if(test)
		{
			query = "select * "
						+ "from multipag_teste "
					+ "where data_entrada='"+sdfm.format(Calendar.getInstance().getTime())+"'"
//					+ " and descricao_situacao='Aguardando aprova��o do Gestor de TED'"
					;
		}
		
		if(dataView!=null)
		{
		
			if(pastProcess)
			{
				query = "select * "
						+ "from vw_multipag "
						+ "where data_entrada>='"+sdfm.format(dataView)+"'"
						;
				if(test)
				{
					query = "select * "
								+ "from multipag_teste "
							+ "where data_entrada>='"+sdfm.format(dataView)+"'"
							;
				}
			}
			else
			{
				query = "select * "
						+ "from vw_multipag "
						+ "where data_entrada='"+sdfm.format(dataView)+"'"					
						;
				if(test)
				{
					query = "select * "
								+ "from multipag_teste "
							+ "where data_entrada='"+sdfm.format(dataView)+"'"					
							;
				}
			}
		}
		
		if(cnpjs.size()>0)
		{			
			for(int i=0;i<cnpjs.size();i++)
			{
				if(i==0)
				{
					query+=" and cnpj_fundo in ('"+cnpjs.get(0)+"'";
				}
				else
				{
					query+=",'"+cnpjs.get(0)+"'";
				}
			}
			query+=");";
		}
		
		System.out.println(query);
		Statement st=null;
		
		if(test)
		{
			try {
				st=connMySQL.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				st=connMSSQL.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ResultSet rs = st.getResultSet();
			
			while(rs.next())
			{
				String nomeArquivo=rs.getString("nome_arquivo");				
				String seuNumero=rs.getString("seu_numero_multipag");
				String seuNumero2="";				
				if(seuNumero.length()>11)
				{
					seuNumero2="2"+seuNumero.substring(11,seuNumero.length());
				}
				else
				{
					seuNumero2="2"+seuNumero;
				}
			
				if(paymentProcessed.get(nomeArquivo)!=null)
				{
					System.out.println("NomeArquivo: "+nomeArquivo+" já processado para pagamento");
					continue;
				}
				Date dataEntrada=rs.getDate("data_entrada");
				String nomeFundo=rs.getString("nome_fundo");
				nomeFundo = Utils.normalizeUri(nomeFundo);
				nomeFundo = Utils.cleanString(nomeFundo);
				String cnpjFundo=rs.getString("cnpj_fundo");
				String estadoFundo=rs.getString("estado_fundo");
				String cidadeFundo=rs.getString("cidade_fundo");
				cidadeFundo = Utils.normalizeUri(cidadeFundo);
				cidadeFundo = Utils.cleanString(cidadeFundo);
				String bairroFundo=rs.getString("bairro_fundo");
				bairroFundo = Utils.normalizeUri(bairroFundo);
				bairroFundo = Utils.cleanString(bairroFundo);
				String cepCompletoFundo=rs.getString("cep_fundo");
				String logradouro=rs.getString("logradouro_fundo");				
				logradouro = Utils.normalizeUri(logradouro);
				logradouro = Utils.cleanString(logradouro);
				String numeroLogradouroFundo=rs.getString("nu_logradouro");
				String complementoFundo=rs.getString("complemento_fundo");
				String nomeBancoFundo=rs.getString("banco_fundo");
				String codigoBancoFundo=rs.getString("nu_banco");
				String convenioBancoFundo=rs.getString("código_convênio_banco");
				String agenciaFundo=rs.getString("agencia_fundo");
				String contaFundo=rs.getString("conta_fundo");
				String nomeCedente=rs.getString("nome_cedente");
				nomeCedente = Utils.normalizeUri(nomeCedente);
				nomeCedente = Utils.cleanString(nomeCedente);
				String cnpjCedente=rs.getString("cnpj_cpf_cedente");
				String estadoCedente=rs.getString("estado_cedente");
				String cidadeCedente=rs.getString("cidade_cedente");
				if(cidadeCedente ==null)
				{
					cidadeCedente=" ";
				}
				cidadeCedente = Utils.normalizeUri(cidadeCedente);
				cidadeCedente = Utils.cleanString(cidadeCedente);
				
				String bairroCedente=rs.getString("bairro_cedente");
				if(bairroCedente==null)
				{
					bairroCedente=" ";
				}
				bairroCedente = Utils.normalizeUri(bairroCedente);
				bairroCedente = Utils.cleanString(bairroCedente);
				String cepCompletoCedente=rs.getString("cep_cedente");
				
				String logradouroCedente=rs.getString("logradouro_cedente");
				if(logradouroCedente==null)
				{
					logradouroCedente=" ";
				}
				
				logradouroCedente = Utils.normalizeUri(logradouroCedente);
				logradouroCedente = Utils.cleanString(logradouroCedente);
				String numeroLogradouroCedente=rs.getString("numero_logradouro");
				if(numeroLogradouroCedente==null)
				{
					numeroLogradouroCedente=" ";
				}
				String complementoCedente=rs.getString("complemento_cedente");
				
				String situacao=rs.getString("descricao_situacao");
				String codigo1BancoCedente=rs.getString("nu_banco1");
				String agencia1Cedente=rs.getString("nu_agencia_cedente1");
				String conta1Cedente=rs.getString("nu_conta_cedente1");
				String stringValor1CedenteConvertido=rs.getString("vl_conta_cedente1_convertido");				
				long valor1CedenteConvertido=0;
				if(stringValor1CedenteConvertido.length()>0)
				{
					valor1CedenteConvertido=Long.parseLong(stringValor1CedenteConvertido);
				}
				String codigo2BancoCedente=rs.getString("nu_banco2");
				String agencia2Cedente=rs.getString("nu_agencia_cedente2");
				String conta2Cedente=rs.getString("nu_conta_cedente2");
				String stringValor2CedenteConvertido=rs.getString("vl_conta_cedente2_convertido");
				
				String tipoChavePIX=rs.getString("TIPO_CHAVE_PIX");
				String chavePIX=rs.getString("CHAVE_PIX");
				long valor2CedenteConvertido=0;
				if(stringValor2CedenteConvertido!=null)
				{
					if(stringValor2CedenteConvertido.length()>0 )
					{
						valor2CedenteConvertido=Long.parseLong(stringValor2CedenteConvertido);
					}
				}
				double valor1Cedente=valor1CedenteConvertido/100.0;
				double valor2Cedente=valor2CedenteConvertido/100.0;
				String cepFundo="";
				String complementoCepFundo="";
				if(cepCompletoCedente!=null)
				{
					System.out.println("CEP completo cedente: " + cepCompletoCedente);
					cepFundo=cepCompletoFundo.substring(0,5);			
					if(cepCompletoFundo!=null)
					{
						
						complementoCepFundo=cepCompletoFundo.substring(5);						
					}
				}
				
				String cepCedente="";
				String complementoCepCedente="";
				System.out.println("CEP completo cedente: " + cepCompletoCedente);
				if(cepCompletoCedente!=null && cepCompletoCedente.length()>=8)
				{
					cepCedente=cepCompletoCedente.substring(0,5);
					System.out.println("CEP cedente: " + cepCedente);
					complementoCepCedente=cepCompletoCedente.substring(5);
					System.out.println("Complemento CEP cedente: " + complementoCepCedente);
				}

				System.out.println("Conta Fundo: "+contaFundo);
				String numeroContaFundo="";
				String digitoContaFundo="";

				if(contaFundo!=null && contaFundo.length()>0)
				{
					contaFundo=contaFundo.replaceAll("-", "").replaceAll(" ", "").trim();
					numeroContaFundo=contaFundo.substring(0,contaFundo.length()-1);
					digitoContaFundo=contaFundo.substring(contaFundo.length()-1, contaFundo.length());
				}
				conta1Cedente=conta1Cedente.replaceAll("-", "").replaceAll(" ", "").trim();

				
				String numeroConta1Cedente="";
				String digitoConta1Cedente="";
				String numeroConta2Cedente="";
				String digitoConta2Cedente="";

				if(conta1Cedente!=null && conta1Cedente.length()>0)
				{
					numeroConta1Cedente=conta1Cedente.substring(0,conta1Cedente.length()-1);
					digitoConta1Cedente=conta1Cedente.substring(conta1Cedente.length()-1, conta1Cedente.length());
				}
				if(conta2Cedente!=null && conta2Cedente.length()>0)
				{
					numeroConta2Cedente=conta2Cedente.substring(0,conta2Cedente.length()-1);
					digitoConta2Cedente=conta2Cedente.substring(conta2Cedente.length()-1, conta2Cedente.length());
				}
				
//				DadosBancarios dadosBancariosFundo = new DadosBancarios(codigoBancoFundo, nomeBancoFundo, convenioBancoFundo,agenciaFundo, "", numeroContaFundo, digitoContaFundo, "");
				DadosBancarios dadosBancariosFundo = null;				
				
				if(dadosCorretosContaFundo.get(cnpjFundo)!=null)
				{
					dadosBancariosFundo = dadosCorretosContaFundo.get(cnpjFundo);				
					dadosBancariosFundo.show();
				}
				else
				{
					System.out.println("Fundo sem cadastro no sistema!");
					continue;
				}
				
				
				EnderecoCompleto enderecoFundoPagador = new EnderecoCompleto(logradouro, numeroLogradouroFundo, complementoFundo, bairroFundo, cepFundo, complementoCepFundo, cidadeFundo, estadoFundo);
				Pagador fundoPagador=new Pagador("2", cnpjFundo, nomeFundo, enderecoFundoPagador, dadosBancariosFundo);
				
				DadosBancarios dadosBancariosConta1Cedente = new DadosBancarios(connMySQL,codigo1BancoCedente,agencia1Cedente,"",numeroConta1Cedente,digitoConta1Cedente,"");
				dadosBancariosConta1Cedente.show();
				DadosBancarios dadosBancariosConta2Cedente = new DadosBancarios(connMySQL,codigo2BancoCedente,agencia2Cedente,"",numeroConta2Cedente,digitoConta2Cedente,"");
				dadosBancariosConta2Cedente.show();
				
				EnderecoCompleto enderecoCedente = new EnderecoCompleto(logradouroCedente, numeroLogradouroCedente, complementoCedente, bairroCedente, cepCedente, complementoCepCedente, cidadeCedente, estadoCedente);
				
				Credito credito1 = new Credito(seuNumero, Calendar.getInstance().getTime(), "BRL", 0, valor1Cedente, "", Calendar.getInstance().getTime(), 0.0);
				Credito credito2 = new Credito(seuNumero2, Calendar.getInstance().getTime(), "BRL", 0, valor2Cedente, "", Calendar.getInstance().getTime(), 0.0);
				String tipoInscricao="2";
				if(cnpjCedente.length()==11)
				{
					tipoInscricao="1";
				}
			   
			
				
				Favorecido cedente1 = new Favorecido(tipoInscricao, cnpjCedente, nomeCedente, dadosBancariosConta1Cedente, enderecoCedente,chavePIX);
				Favorecido cedente2 = new Favorecido(tipoInscricao, cnpjCedente, nomeCedente, dadosBancariosConta2Cedente, enderecoCedente,chavePIX);
				
				PagamentoFornecedor pagamento1 = new PagamentoFornecedor(nomeArquivo, fundoPagador, cedente1, credito1, Calendar.getInstance().getTime(), credito1.getValorPagamento(), 0, 0, 0, 0,(int)valor1CedenteConvertido);
				PagamentoFornecedor pagamento2 = new PagamentoFornecedor(nomeArquivo, fundoPagador, cedente2, credito2, Calendar.getInstance().getTime(), credito2.getValorPagamento(), 0, 0, 0, 0,(int)valor2CedenteConvertido);
				
				
				if(fundosPagamentos.get(cnpjFundo)!=null)
				{
					System.out.println(" *************** Pagamentos para o fundo: " + fundoPagador.getNome() + " NomeArquivo: " + nomeArquivo);
					if(pagamento1.getCredito().getValorPagamento()>0.0)
					{
						pagamento1.show();
						fundosPagamentos.get(cnpjFundo).add(pagamento1);
					}
					if(pagamento2.getCredito().getValorPagamento()>0.0)
					{
						pagamento2.show();
						fundosPagamentos.get(cnpjFundo).add(pagamento2);
					}
					System.out.println(" *************** ");
					System.out.println("Adicionando pagamentos do NomeArquivo: "+nomeArquivo+" "+pagamento1.getCredito().getValorPagamento() +" e " +pagamento2.getCredito().getValorPagamento());
					paymentProcessed.put(nomeArquivo,nomeArquivo);
				}
				else
				{
					System.out.println(" *************** Pagamentos para o fundo: " + fundoPagador.getNome() + " NomeArquivo: " + nomeArquivo);
					ArrayList<PagamentoFornecedor> pagamentos=new ArrayList<PagamentoFornecedor>();
					if(pagamento1.getCredito().getValorPagamento()>0.0)
					{
						pagamento1.show();
						pagamentos.add(pagamento1);
					}
					if(pagamento2.getCredito().getValorPagamento()>0.0)
					{
						pagamento2.show();
						pagamentos.add(pagamento2);
					}
					fundosPagamentos.put(cnpjFundo, pagamentos);
					System.out.println(" *************** ");
					System.out.println("Adicionando pagamentos do NomeArquivo: "+nomeArquivo+" "+pagamento1.getCredito().getValorPagamento() +" e " +pagamento2.getCredito().getValorPagamento());
					paymentProcessed.put(nomeArquivo,nomeArquivo);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(String key: fundosPagamentos.keySet())
		{
			System.out.println("------------------------------------------------------------------------------------------------");
			System.out.println("Pagamentos para o fundo de CNPJ "+key + fundosPagamentos.get(key).get(0).getPagador().getNome());
			double valorTotal=0;
			int iPagamento=0;
			for(PagamentoFornecedor pagamento:fundosPagamentos.get(key))
			{
				pagamento.show();
				valorTotal+=pagamento.getCredito().getValorPagamento();
				iPagamento++;
			}
			System.out.println(iPagamento + " pagamentos, valor total " + Utils.formatValorBrazilCurrency(valorTotal));
			System.out.println("------------------------------------------------------------------------------------------------");
		}
	}

	public static void setupMultipag()
	{		
		List<Object> confLines = new ArrayList<>();
		String nameOS = System.getProperty("os.name");
		String fileName=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"App"+File.separator+"Conf"+File.separator+"multipag.conf";
		
		separator="/";
		if(nameOS.toLowerCase().contains("windows"))
		{			
			fileName = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"App"+File.separator+"Conf"+File.separator+"multipag.conf";
			folderPagamentoEmLote=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"App"+File.separator+"PagamentoEmLote";
		}
		else
		{
			fileName = System.getProperty("user.home")+File.separator+"App"+File.separator+"Conf"+File.separator+"multipag.conf";
			folderPagamentoEmLote=System.getProperty("user.home")+File.separator+"PagamentoEmLote";
			separator="\\";
		}
		System.out.println("Reading "  + fileName);
		System.out.println("Separator '"+separator+"'");
		System.out.println("PathPagamentoEmLote: "+folderPagamentoEmLote);
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
						case "output": 
							outputPath.put(words[1],words[2]);
							System.out.println("Key: " + words[1] + " Value: " + words[2]);
							break;
						case "companysender": companySender=value; break;
						case "namesender": nameSender=value; break;
						case "emailsender": emailSender=value; break;
						case "passwordsender": passwordSender=value; break;
						case "destinatario": destinatarios.add(value); break;
						case "cnpjfundo": cnpjs.add(value); break;
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
						
		System.out.println("List of outputPaths");
		for(String key:outputPath.keySet())
		{
			System.out.println("key: "  + key + " value: " + outputPath.get(key) );
		}
	}	
	
	
	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		BuilderPaymentsMultibanco.sdf = sdf;
	}

	public static SimpleDateFormat getSdfm() {
		return sdfm;
	}

	public static void setSdfm(SimpleDateFormat sdfm) {
		BuilderPaymentsMultibanco.sdfm = sdfm;
	}

	public static SimpleDateFormat getSdft() {
		return sdft;
	}

	public static void setSdft(SimpleDateFormat sdft) {
		BuilderPaymentsMultibanco.sdft = sdft;
	}

	public HashMap<String, ArrayList<PagamentoFornecedor>> getFundosPagamentos() {
		return fundosPagamentos;
	}

	public void setFundosPagamentos(HashMap<String, ArrayList<PagamentoFornecedor>> fundosPagamentos) {
		this.fundosPagamentos = fundosPagamentos;
	}


	public ArrayList<ArquivoCNAB240> getCnabs240() {
		return cnabs240;
	}

	public void setCnabs240(ArrayList<ArquivoCNAB240> cnabs240) {
		this.cnabs240 = cnabs240;
	}

	public static Connection getConnMSSQL() {
		return connMSSQL;
	}

	public static void setConnMSSQL(Connection connMSSQL) {
		BuilderPaymentsMultibanco.connMSSQL = connMSSQL;
	}

	public static Connection getConnMySQL() {
		return connMySQL;
	}

	public static void setConnMySQL(Connection connMySQL) {
		BuilderPaymentsMultibanco.connMySQL = connMySQL;
	}

	public static SimpleDateFormat getSdff() {
		return sdff;
	}

	public static void setSdff(SimpleDateFormat sdff) {
		BuilderPaymentsMultibanco.sdff = sdff;
	}

	public static HashMap<String, DadosBancarios> getDadosCorretosContaFundo() {
		return dadosCorretosContaFundo;
	}

	public static void setDadosCorretosContaFundo(HashMap<String, DadosBancarios> dadosCorretosContaFundo) {
		BuilderPaymentsMultibanco.dadosCorretosContaFundo = dadosCorretosContaFundo;
	}

	public static Date getDataView() {
		return dataView;
	}

	public static void setDataView(Date dataView) {
		BuilderPaymentsMultibanco.dataView = dataView;
	}

	public static boolean isPastProcess() {
		return pastProcess;
	}

	public static void setPastProcess(boolean pastProcess) {
		BuilderPaymentsMultibanco.pastProcess = pastProcess;
	}

	public static String getSeparator() {
		return separator;
	}

	public static void setSeparator(String separator) {
		BuilderPaymentsMultibanco.separator = separator;
	}

	public static String getEmailSender() {
		return emailSender;
	}

	public static void setEmailSender(String emailSender) {
		BuilderPaymentsMultibanco.emailSender = emailSender;
	}

	public static String getPasswordSender() {
		return passwordSender;
	}

	public static void setPasswordSender(String passwordSender) {
		BuilderPaymentsMultibanco.passwordSender = passwordSender;
	}

	public static ArrayList<String> getDestinatarios() {
		return destinatarios;
	}

	public static void setDestinatarios(ArrayList<String> destinatarios) {
		BuilderPaymentsMultibanco.destinatarios = destinatarios;
	}

	public static String getCompanySender() {
		return companySender;
	}

	public static void setCompanySender(String companySender) {
		BuilderPaymentsMultibanco.companySender = companySender;
	}

	public static String getNameSender() {
		return nameSender;
	}

	public static void setNameSender(String nameSender) {
		BuilderPaymentsMultibanco.nameSender = nameSender;
	}

	public static boolean isApprovedFiles() {
		return approvedFiles;
	}

	public static void setApprovedFiles(boolean approvedFiles) {
		BuilderPaymentsMultibanco.approvedFiles = approvedFiles;
	}

	public static SimpleDateFormat getSdfc() {
		return sdfc;
	}

	public static void setSdfc(SimpleDateFormat sdfc) {
		BuilderPaymentsMultibanco.sdfc = sdfc;
	}

	public static SimpleDateFormat getSdfh() {
		return sdfh;
	}

	public static void setSdfh(SimpleDateFormat sdfh) {
		BuilderPaymentsMultibanco.sdfh = sdfh;
	}

	public static HashMap<String, String> getVersaoLayout() {
		return versaoLayout;
	}

	public static void setVersaoLayout(HashMap<String, String> versaoLayout) {
		BuilderPaymentsMultibanco.versaoLayout = versaoLayout;
	}

	public static boolean isSeparateFiles() {
		return separateFiles;
	}

	public static void setSeparateFiles(boolean separateFiles) {
		BuilderPaymentsMultibanco.separateFiles = separateFiles;
	}

	public static boolean isEndLinux() {
		return endLinux;
	}

	public static void setEndLinux(boolean endLinux) {
		BuilderPaymentsMultibanco.endLinux = endLinux;
	}

	public static boolean isBlankLine() {
		return blankLine;
	}

	public static void setBlankLine(boolean blankLine) {
		BuilderPaymentsMultibanco.blankLine = blankLine;
	}

	public static ArrayList<String> getCnpjs() {
		return cnpjs;
	}

	public static void setCnpjs(ArrayList<String> cnpjs) {
		BuilderPaymentsMultibanco.cnpjs = cnpjs;
	}

	public static HashMap<String, String> getVersaoLayoutLote() {
		return versaoLayoutLote;
	}

	public static void setVersaoLayoutLote(HashMap<String, String> versaoLayoutLote) {
		BuilderPaymentsMultibanco.versaoLayoutLote = versaoLayoutLote;
	}

	public static HashMap<String, String> getOutputPath() {
		return outputPath;
	}

	public static void setOutputPath(HashMap<String, String> outputPath) {
		BuilderPaymentsMultibanco.outputPath = outputPath;
	}

	public static boolean isTest() {
		return test;
	}

	public static void setTest(boolean test) {
		BuilderPaymentsMultibanco.test = test;
	}

	public static HashMap<String, String> getNomeBanco() {
		return nomeBanco;
	}

	public static void setNomeBanco(HashMap<String, String> nomeBanco) {
		BuilderPaymentsMultibanco.nomeBanco = nomeBanco;
	}

	public static HashMap<String, String> getSistemaBanco() {
		return sistemaBanco;
	}

	public static void setSistemaBanco(HashMap<String, String> sistemaBanco) {
		BuilderPaymentsMultibanco.sistemaBanco = sistemaBanco;
	}

	public static SimpleDateFormat getSdfsn() {
		return sdfsn;
	}

	public static void setSdfsn(SimpleDateFormat sdfsn) {
		BuilderPaymentsMultibanco.sdfsn = sdfsn;
	}

	public static boolean isCheck() {
		return check;
	}

	public static void setCheck(boolean check) {
		BuilderPaymentsMultibanco.check = check;
	}

	public static boolean isReadOnly() {
		return readOnly;
	}

	public static void setReadOnly(boolean readOnly) {
		BuilderPaymentsMultibanco.readOnly = readOnly;
	}

	public static String getCsvPath() {
		return csvPath;
	}

	public static void setCsvPath(String csvPath) {
		BuilderPaymentsMultibanco.csvPath = csvPath;
	}

	public static boolean isReadCSV() {
		return readCSV;
	}

	public static void setReadCSV(boolean readCSV) {
		BuilderPaymentsMultibanco.readCSV = readCSV;
	}

	public static SimpleDateFormat getSdfmt() {
		return sdfmt;
	}

	public static void setSdfmt(SimpleDateFormat sdfmt) {
		BuilderPaymentsMultibanco.sdfmt = sdfmt;
	}

	public static SimpleDateFormat getSdfx() {
		return sdfx;
	}

	public static void setSdfx(SimpleDateFormat sdfx) {
		BuilderPaymentsMultibanco.sdfx = sdfx;
	}

	public static String getFolderPagamentoEmLote() {
		return folderPagamentoEmLote;
	}

	public static void setFolderPagamentoEmLote(String folderPagamentoEmLote) {
		BuilderPaymentsMultibanco.folderPagamentoEmLote = folderPagamentoEmLote;
	}

}

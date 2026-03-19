package cnab.cnab240;

import java.util.ArrayList;
import java.util.HashMap;

public class ArquivoCNAB240 
{
	private String fileName="";
	private HeaderArquivo240Bradesco headerArquivoBradesco = new HeaderArquivo240Bradesco();
	private HeaderArquivo240Itau headerArquivoItau = new HeaderArquivo240Itau();
	private HeaderArquivo240Santander headerArquivoSantander = new HeaderArquivo240Santander();
	private HeaderArquivo240Unavanti headerArquivoUnavanti = new HeaderArquivo240Unavanti();
	private ArrayList<Lote240> lotes = new ArrayList<Lote240>();
	private TrailerArquivo240Bradesco trailerArquivoBradesco = new TrailerArquivo240Bradesco();
	private TrailerArquivo240Itau trailerArquivoItau = new TrailerArquivo240Itau();
	private TrailerArquivo240Santander trailerArquivoSantander = new TrailerArquivo240Santander();
	private TrailerArquivo240Unavanti trailerArquivoUnavanti = new TrailerArquivo240Unavanti();
	private int iArquivo=0;
	private Pagador pagador = new Pagador();
	
	public ArquivoCNAB240()
	{
		
	}

	public ArquivoCNAB240(Pagador pagador, ArrayList<PagamentoTituloCobranca> pagamentos, int iArquivo, String cobranca)	
	{
		this.pagador=pagador;
		String codigoCompe=pagador.getDadosBancarios().getDadosBanco().getCodigoCompe();
		switch (codigoCompe) {
		case "237":
			this.headerArquivoBradesco=new HeaderArquivo240Bradesco(pagador, iArquivo);
			break;
		case "341":
			this.headerArquivoItau=new HeaderArquivo240Itau(pagador, iArquivo);	
			break;
		case "033":
			this.headerArquivoSantander=new HeaderArquivo240Santander(pagador, iArquivo);
			break;
		case "460":
			this.headerArquivoUnavanti=new HeaderArquivo240Unavanti(pagador, iArquivo);	
			break;
		default:
			break;
		}
		
		String tipoMovimentacao="";

		HashMap<String, ArrayList<PagamentoTituloCobranca>> tipoPagamentos = new HashMap<String, ArrayList<PagamentoTituloCobranca>>();

		for(PagamentoTituloCobranca pagamento:pagamentos)
		{
//			System.out.println("Pagamento de titulo com codigo de barras");
//			System.out.println("Favorecido: " + pagamento.getFavorecido().getNome());
//			System.out.println("Banco Favorecido: " + pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe());
//			System.out.println("Banco Pagador   : " + pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
//			if(pagamento.getFavorecido().isCadastrar())
//			{
//				tipoMovimentacao="CADASTRO";
//			}
//			else if(pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe().equals(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe()))
//			{
//				tipoMovimentacao="TITULOSMESMOBANCO";					
//			}			
//			else
//			{
//				tipoMovimentacao="TITULOSOUTROBANCO";
//			}
//			
//			if(tipoPagamentos.get(tipoMovimentacao)!=null)
//			{
//				tipoPagamentos.get(tipoMovimentacao).add(pagamento);
//			}
//			else
//			{
//				ArrayList<PagamentoTituloCobranca> pagamentosTipo = new ArrayList<PagamentoTituloCobranca>();
//				pagamentosTipo.add(pagamento);
//				tipoPagamentos.put(tipoMovimentacao, pagamentosTipo);
//			}
		}
		
		int iLote=1;
		for(String tipoMov:tipoPagamentos.keySet())
		{
			this.lotes.add(new Lote240(pagador, tipoMov, tipoPagamentos.get(tipoMov), iLote, "cobranca"));
			iLote++;
		}
		switch (codigoCompe) {
		case "237":			
			this.trailerArquivoBradesco=new TrailerArquivo240Bradesco(pagador, this.lotes);
			break;
		case "341":			
			this.trailerArquivoItau=new TrailerArquivo240Itau(pagador, this.lotes);
			break;
		case "033":			
			this.trailerArquivoSantander=new TrailerArquivo240Santander(pagador, this.lotes);
			break;
		case "460":			
			this.trailerArquivoUnavanti=new TrailerArquivo240Unavanti(pagador, this.lotes);
			break;
		default:
			break;
		}		
	}
	
	public ArquivoCNAB240(Pagador pagador, ArrayList<PagamentoFornecedor> pagamentos, int iArquivo)	
	{
		this.pagador=pagador;
		String codigoCompe=pagador.getDadosBancarios().getDadosBanco().getCodigoCompe();
		switch (codigoCompe) {
		case "237":
			this.headerArquivoBradesco=new HeaderArquivo240Bradesco(pagador, iArquivo);
			break;
		case "341":
			this.headerArquivoItau=new HeaderArquivo240Itau(pagador, iArquivo);	
			break;
		case "033":
			this.headerArquivoSantander=new HeaderArquivo240Santander(pagador, iArquivo);
			break;
		case "460":
			this.headerArquivoUnavanti=new HeaderArquivo240Unavanti(pagador, iArquivo);	
			break;
		default:
			break;
		}
		String tipoMovimentacao="";

		HashMap<String, ArrayList<PagamentoFornecedor>> tipoPagamentos = new HashMap<String, ArrayList<PagamentoFornecedor>>();

		for(PagamentoFornecedor pagamento:pagamentos)
		{
			if(pagamento.getFavorecido().getChavePIX().length()>0)
			{
				System.out.println("Pagamento por chave PIX");
			}
			else if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==47)
			{
				System.out.println("Pagamento de boleto!");
			}
			else if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==48)
			{
				System.out.println("Pagamento de tributo ou convenio");
			}
			else {
				System.out.println("TEF OR TED");
				System.out.println("Favorecido: " + pagamento.getFavorecido().getNome());				
			}
			
			if(BuilderPaymentsMultibanco.readCSV)
			{
				if(pagamento.getFavorecido().getNumeroBoletoDARF().length()>0)
				{
					
				}
				else
				{
					System.out.println("Banco Favorecido: " + pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe());					
				}
				System.out.println("Banco Pagador   : " + pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
				System.out.println("Banco Pagador   : " + pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
			}
			else {
				if(pagamento.getFavorecido().getNumeroBoletoDARF().length()>0)
				{
					
				}
				else
				{
					System.out.println("Banco Favorecido: " + pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe());					
				}
				System.out.println("Banco Pagador   : " + pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
				System.out.println("Banco Pagador   : " + pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());				
			}
			
			if(pagamento.getFavorecido().isCadastrar())
			{
				tipoMovimentacao="CADASTRO";
			}
			else if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==47)
			{
				String codigoBancoDestino=pagamento.getFavorecido().getNumeroBoletoDARF().substring(0,3);
				if(pagamento.getPagador().getDadosBancarios().getCodigoBanco().contains(codigoBancoDestino))
				{
					tipoMovimentacao="boletoMesmoBanco";
				}
				else
				{
					tipoMovimentacao="boletoOutrosBancos";
				}
					
			}
			else if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==48)
			{
				
				tipoMovimentacao="contasetributos";	
			}
			else if(pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe().equals(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe())
					|| pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe().equals(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe())
					)
			{
				tipoMovimentacao="TEF";					
			}			
			else if(pagamento.getFavorecido().getChavePIX().length()>0)
			{
				tipoMovimentacao="PIX";
			}
			else
			{
				tipoMovimentacao="TED";
			}
			
			if(tipoPagamentos.get(tipoMovimentacao)!=null)
			{
				tipoPagamentos.get(tipoMovimentacao).add(pagamento);
			}
			else
			{
				ArrayList<PagamentoFornecedor> pagamentosTipo = new ArrayList<PagamentoFornecedor>();
				pagamentosTipo.add(pagamento);
				tipoPagamentos.put(tipoMovimentacao, pagamentosTipo);
			}
		}

		for(String tipoMov:tipoPagamentos.keySet())
		{
			System.out.println("*******************************************************");
			System.out.println("****  Tipo de movimentacao: " + tipoMov+ " ****");
			System.out.println("*******************************************************");
			for(PagamentoFornecedor pagamento:tipoPagamentos.get(tipoMov))
			{
				pagamento.show();
			}
		}
		
		int iLote=1;
		for(String tipoMov:tipoPagamentos.keySet())
		{
			this.lotes.add(new Lote240(pagador, tipoMov, tipoPagamentos.get(tipoMov), iLote));
			iLote++;
		}
		

		if(BuilderPaymentsMultibanco.readCSV)
		{
			switch (codigoCompe) {
			case "237":			
				this.trailerArquivoBradesco=new TrailerArquivo240Bradesco(pagador, this.lotes);
				break;
			case "341":			
				this.trailerArquivoItau=new TrailerArquivo240Itau(pagador, this.lotes);
				break;
			case "033":			
				this.trailerArquivoSantander=new TrailerArquivo240Santander(pagador, this.lotes);
				break;
			case "460":			
				this.trailerArquivoUnavanti=new TrailerArquivo240Unavanti(pagador, this.lotes);
				break;
			default:
				break;
			}		
		}
		else {
			switch (codigoCompe) {
			case "237":			
				this.trailerArquivoBradesco=new TrailerArquivo240Bradesco(pagador, this.lotes);
				break;
			case "341":			
				this.trailerArquivoItau=new TrailerArquivo240Itau(pagador, this.lotes);
				break;
			case "033":			
				this.trailerArquivoSantander=new TrailerArquivo240Santander(pagador, this.lotes);
				break;
			case "460":			
				this.trailerArquivoUnavanti=new TrailerArquivo240Unavanti(pagador, this.lotes);
				break;
			default:
				break;
			}		
		}				
	}
	
	public ArrayList<Lote240> getLotes() {
		return lotes;
	}

	public void setLotes(ArrayList<Lote240> lotes) {
		this.lotes = lotes;
	}

	public int getiArquivo() {
		return iArquivo;
	}

	public void setiArquivo(int iArquivo) {
		this.iArquivo = iArquivo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HeaderArquivo240Bradesco getHeaderArquivoBradesco() {
		return headerArquivoBradesco;
	}

	public void setHeaderArquivoBradesco(HeaderArquivo240Bradesco headerArquivoBradesco) {
		this.headerArquivoBradesco = headerArquivoBradesco;
	}

	public HeaderArquivo240Itau getHeaderArquivoItau() {
		return headerArquivoItau;
	}

	public void setHeaderArquivoItau(HeaderArquivo240Itau headerArquivoItau) {
		this.headerArquivoItau = headerArquivoItau;
	}

	public Pagador getPagador() {
		return pagador;
	}

	public void setPagador(Pagador pagador) {
		this.pagador = pagador;
	}

	public HeaderArquivo240Santander getHeaderArquivoSantander() {
		return headerArquivoSantander;
	}

	public void setHeaderArquivoSantander(HeaderArquivo240Santander headerArquivoSantander) {
		this.headerArquivoSantander = headerArquivoSantander;
	}

	public TrailerArquivo240Bradesco getTrailerArquivoBradesco() {
		return trailerArquivoBradesco;
	}

	public void setTrailerArquivoBradesco(TrailerArquivo240Bradesco trailerArquivoBradesco) {
		this.trailerArquivoBradesco = trailerArquivoBradesco;
	}

	public TrailerArquivo240Itau getTrailerArquivoItau() {
		return trailerArquivoItau;
	}

	public void setTrailerArquivoItau(TrailerArquivo240Itau trailerArquivoItau) {
		this.trailerArquivoItau = trailerArquivoItau;
	}

	public TrailerArquivo240Santander getTrailerArquivoSantander() {
		return trailerArquivoSantander;
	}

	public void setTrailerArquivoSantander(TrailerArquivo240Santander trailerArquivoSantander) {
		this.trailerArquivoSantander = trailerArquivoSantander;
	}

	public HeaderArquivo240Unavanti getHeaderArquivoUnavanti() {
		return headerArquivoUnavanti;
	}

	public void setHeaderArquivoUnavanti(HeaderArquivo240Unavanti headerArquivoUnavanti) {
		this.headerArquivoUnavanti = headerArquivoUnavanti;
	}

	public TrailerArquivo240Unavanti getTrailerArquivoUnavanti() {
		return trailerArquivoUnavanti;
	}

	public void setTrailerArquivoUnavanti(TrailerArquivo240Unavanti trailerArquivoUnavanti) {
		this.trailerArquivoUnavanti = trailerArquivoUnavanti;
	}
}

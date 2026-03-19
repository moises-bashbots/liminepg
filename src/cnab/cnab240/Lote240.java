package cnab.cnab240;

import java.util.ArrayList;

public class Lote240
{
	private HeaderLote240Bradesco headerLoteBradesco = new HeaderLote240Bradesco();
	private HeaderLote240Itau headerLoteItau = new HeaderLote240Itau();
	private HeaderLote240Santander headerLoteSantander = new HeaderLote240Santander();
	private HeaderLote240Unavanti headerLoteUnavanti = new HeaderLote240Unavanti();
	
	private ArrayList<DetalhePagamentoAB240> detalhes = new ArrayList<DetalhePagamentoAB240>();
	
	private TrailerLote240Bradesco trailerLoteBradesco = new TrailerLote240Bradesco();
	private TrailerLote240Itau trailerLoteItau = new TrailerLote240Itau();
	private TrailerLote240Santander trailerLoteSantander = new TrailerLote240Santander();
	private TrailerLote240Unavanti trailerLoteUnavanti = new TrailerLote240Unavanti();
	private int iLote=0;
	
	public Lote240()
	{
	}
	
	public Lote240(Pagador pagador, HeaderLote240Bradesco headerLote, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		this.iLote=iLote;
		this.headerLoteBradesco=headerLote;
		this.detalhes=detalhes;
		this.trailerLoteBradesco=new TrailerLote240Bradesco(pagador, detalhes, iLote);
		this.numberSequence();
	}
	
	public Lote240(Pagador pagador, HeaderLote240Itau headerLote, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		this.iLote=iLote;
		this.headerLoteItau=headerLote;
		this.detalhes=detalhes;
		this.trailerLoteItau=new TrailerLote240Itau(pagador, detalhes, iLote);
		this.numberSequence();
	}
	
	
	public Lote240(Pagador pagador, HeaderLote240Santander headerLote, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		this.iLote=iLote;
		this.headerLoteSantander=headerLote;
		this.detalhes=detalhes;
		this.trailerLoteSantander=new TrailerLote240Santander(pagador, detalhes, iLote);
		this.numberSequence();
	}
	
	public Lote240(Pagador pagador, HeaderLote240Unavanti headerLote, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		this.iLote=iLote;
		this.headerLoteUnavanti=headerLote;
		this.detalhes=detalhes;
		this.trailerLoteUnavanti=new TrailerLote240Unavanti(pagador, detalhes, iLote);
		this.numberSequence();
	}
	
	public Lote240(Pagador pagador, String tipoMovimentacao, ArrayList<PagamentoTituloCobranca> pagamentos, int iLote, String cobranca)
	{
		this.iLote=iLote;
		String codigoCompe= pagador.getDadosBancarios().getDadosBanco().getCodigoCompe();
		switch (codigoCompe) {
		case "237":
			this.headerLoteBradesco = new HeaderLote240Bradesco(pagador, iLote, tipoMovimentacao);
			break;
		case "341":
			this.headerLoteItau = new HeaderLote240Itau(pagador, iLote, tipoMovimentacao);
			break;
		case "033":
			this.headerLoteSantander = new HeaderLote240Santander(pagador, iLote, tipoMovimentacao);
			break;
		case "460":
			this.headerLoteUnavanti = new HeaderLote240Unavanti(pagador, iLote, tipoMovimentacao);
			break;			
		default:
			break;
		}
				
		for(PagamentoTituloCobranca pagamento:pagamentos)
		{
			this.detalhes.add(new DetalhePagamentoAB240(pagamento, iLote));
		}
		
		
		switch (codigoCompe) {
		case "237":
			this.trailerLoteBradesco=new TrailerLote240Bradesco(pagador, detalhes, iLote);
			break;
		case "341":
			this.trailerLoteItau=new TrailerLote240Itau(pagador, detalhes, iLote);
			break;
		case "033":
			this.trailerLoteSantander=new TrailerLote240Santander(pagador, detalhes, iLote);
			break;
		case "460":
			this.trailerLoteUnavanti = new TrailerLote240Unavanti(pagador, detalhes, iLote);
			break;			
		default:
			break;
		}
		this.numberSequence();
	}
	
	
	public Lote240(Pagador pagador, String tipoMovimentacao, ArrayList<PagamentoFornecedor> pagamentos, int iLote)
	{
		this.iLote=iLote;
		System.out.println("Creating a new lote for: " + tipoMovimentacao + " Payments: "+pagamentos.size() + " NumberLote: " +iLote);
		String codigoCompe= pagador.getDadosBancarios().getDadosBanco().getCodigoCompe();		
		if(BuilderPaymentsMultibanco.readCSV)
		{
			switch (codigoCompe) {
			case "237":
				this.headerLoteBradesco = new HeaderLote240Bradesco(pagador, iLote, tipoMovimentacao);				
				break;
			case "341":
				this.headerLoteItau = new HeaderLote240Itau(pagador, iLote, tipoMovimentacao);
				break;
			case "033":
				this.headerLoteSantander = new HeaderLote240Santander(pagador, iLote, tipoMovimentacao);
				break;
			case "460":
				this.headerLoteUnavanti = new HeaderLote240Unavanti(pagador, iLote, tipoMovimentacao);
				break;
			default:
				break;
			}	
			
			for(PagamentoFornecedor pagamento:pagamentos)
			{
				this.detalhes.add(new DetalhePagamentoAB240(pagamento, iLote));
			}
			
			switch (codigoCompe) {
			case "237":
				this.trailerLoteBradesco=new TrailerLote240Bradesco(pagador, detalhes, iLote);				
				break;
			case "341":
				this.trailerLoteItau=new TrailerLote240Itau(pagador, detalhes, iLote);
				break;
			case "033":
				this.trailerLoteSantander=new TrailerLote240Santander(pagador, detalhes, iLote);
				break;
			case "460":
				this.trailerLoteUnavanti=new TrailerLote240Unavanti(pagador, detalhes, iLote);
				break;
			default:
				break;
			}
		}
		else
		{
			switch (codigoCompe) {
			case "237":
				this.headerLoteBradesco = new HeaderLote240Bradesco(pagador, iLote, tipoMovimentacao);				
				break;
			case "341":
				this.headerLoteItau = new HeaderLote240Itau(pagador, iLote, tipoMovimentacao);
				break;
			case "033":
				this.headerLoteSantander = new HeaderLote240Santander(pagador, iLote, tipoMovimentacao);
				break;
			case "460":
				this.headerLoteUnavanti = new HeaderLote240Unavanti(pagador, iLote, tipoMovimentacao);
				break;
			default:
				break;
			}

			for(PagamentoFornecedor pagamento:pagamentos)
			{
				System.out.println("-- BancoPagador: " + pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
				this.detalhes.add(new DetalhePagamentoAB240(pagamento, iLote));
			}
			
			switch (codigoCompe) {
			case "237":
				this.trailerLoteBradesco=new TrailerLote240Bradesco(pagador, detalhes, iLote);				
				break;
			case "341":
				this.trailerLoteItau=new TrailerLote240Itau(pagador, detalhes, iLote);
				break;
			case "033":
				this.trailerLoteSantander=new TrailerLote240Santander(pagador, detalhes, iLote);
				break;
			case "460":
				this.trailerLoteUnavanti=new TrailerLote240Unavanti(pagador, detalhes, iLote);
				break;
			default:
				break;
			}
		}
		this.numberSequence();
	}
	
	private void numberSequence()
	{
		int sequenceNumber=1;
		for(DetalhePagamentoAB240 detalhe:this.detalhes)
		{
			String codigoCompe= detalhe.getPagamento().getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe();
			System.out.println("Numbering details for bank code: '"+detalhe.getPagamento().getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe()+"'");
			switch (codigoCompe) {
			case "237":
				detalhe.getDetalheABradesco().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				detalhe.getDetalheBBradesco().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;				
				break;
			case "341":
				detalhe.getDetalheAItau().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				detalhe.getDetalheBItau().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				break;
			case "033":
				detalhe.getDetalheASantander().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				detalhe.getDetalheBSantander().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				break;
			case "460":
				detalhe.getDetalheAUnavanti().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				detalhe.getDetalheBUnavanti().getNumeroRegistro().setContent(Integer.toString(sequenceNumber));
				sequenceNumber++;
				break;
			default:
				break;
			}
		}
	}


	public ArrayList<DetalhePagamentoAB240> getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(ArrayList<DetalhePagamentoAB240> detalhes) {
		this.detalhes = detalhes;
	}

	public int getiLote() {
		return iLote;
	}

	public void setiLote(int iLote) {
		this.iLote = iLote;
	}

	public HeaderLote240Bradesco getHeaderLoteBradesco() {
		return headerLoteBradesco;
	}

	public void setHeaderLoteBradesco(HeaderLote240Bradesco headerLoteBradesco) {
		this.headerLoteBradesco = headerLoteBradesco;
	}

	public HeaderLote240Itau getHeaderLoteItau() {
		return headerLoteItau;
	}

	public void setHeaderLoteItau(HeaderLote240Itau headerLoteItau) {
		this.headerLoteItau = headerLoteItau;
	}

	public HeaderLote240Santander getHeaderLoteSantander() {
		return headerLoteSantander;
	}

	public void setHeaderLoteSantander(HeaderLote240Santander headerLoteSantander) {
		this.headerLoteSantander = headerLoteSantander;
	}

	public TrailerLote240Bradesco getTrailerLoteBradesco() {
		return trailerLoteBradesco;
	}

	public void setTrailerLoteBradesco(TrailerLote240Bradesco trailerLoteBradesco) {
		this.trailerLoteBradesco = trailerLoteBradesco;
	}

	public TrailerLote240Itau getTrailerLoteItau() {
		return trailerLoteItau;
	}

	public void setTrailerLoteItau(TrailerLote240Itau trailerLoteItau) {
		this.trailerLoteItau = trailerLoteItau;
	}

	public TrailerLote240Santander getTrailerLoteSantander() {
		return trailerLoteSantander;
	}

	public void setTrailerLoteSantander(TrailerLote240Santander trailerLoteSantander) {
		this.trailerLoteSantander = trailerLoteSantander;
	}

	public HeaderLote240Unavanti getHeaderLoteUnavanti() {
		return headerLoteUnavanti;
	}

	public void setHeaderLoteUnavanti(HeaderLote240Unavanti headerLoteUnavanti) {
		this.headerLoteUnavanti = headerLoteUnavanti;
	}

	public TrailerLote240Unavanti getTrailerLoteUnavanti() {
		return trailerLoteUnavanti;
	}

	public void setTrailerLoteUnavanti(TrailerLote240Unavanti trailerLoteUnavanti) {
		this.trailerLoteUnavanti = trailerLoteUnavanti;
	}

}

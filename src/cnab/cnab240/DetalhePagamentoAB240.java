package cnab.cnab240;

public class DetalhePagamentoAB240 {
	private DetalhePagamentoSegmentoA240Bradesco detalheABradesco = new DetalhePagamentoSegmentoA240Bradesco();
	private DetalhePagamentoSegmentoB240Bradesco detalheBBradesco = new DetalhePagamentoSegmentoB240Bradesco();
	private DetalhePagamentoSegmentoA240Itau detalheAItau = new DetalhePagamentoSegmentoA240Itau();
	private DetalhePagamentoSegmentoB240Itau detalheBItau = new DetalhePagamentoSegmentoB240Itau();
	private DetalhePagamentoSegmentoA240Santander detalheASantander = new DetalhePagamentoSegmentoA240Santander();
	private DetalhePagamentoSegmentoB240Santander detalheBSantander = new DetalhePagamentoSegmentoB240Santander();
	private DetalhePagamentoSegmentoJ240Santander detalheJSantander = new DetalhePagamentoSegmentoJ240Santander();
	private DetalhePagamentoSegmentoJ52240Santander detalheJ52Santander = new DetalhePagamentoSegmentoJ52240Santander();
	private DetalhePagamentoSegmentoO240Santander detalheOSantander = new DetalhePagamentoSegmentoO240Santander();
	
	private int numberRegistrosDetalhes=0;
	
	public int getNumberRegistrosDetalhes() {
		return numberRegistrosDetalhes;
	}

	public void setNumberRegistrosDetalhes(int numberRegistrosDetalhes) {
		this.numberRegistrosDetalhes = numberRegistrosDetalhes;
	}

	public DetalhePagamentoSegmentoO240Santander getDetalheOSantander() {
		return detalheOSantander;
	}

	public void setDetalheOSantander(DetalhePagamentoSegmentoO240Santander detalheOSantander) {
		this.detalheOSantander = detalheOSantander;
	}

	private DetalhePagamentoSegmentoA240Unavanti detalheAUnavanti = new DetalhePagamentoSegmentoA240Unavanti();
	private DetalhePagamentoSegmentoB240Unavanti detalheBUnavanti = new DetalhePagamentoSegmentoB240Unavanti();

	private PagamentoFornecedor pagamentoFornecedor = new PagamentoFornecedor();
	private PagamentoTituloCobranca pagamentoTituloCobranca = new PagamentoTituloCobranca();
	
	
	public DetalhePagamentoAB240()
	{
		
	}

	public DetalhePagamentoAB240(PagamentoFornecedor pagamento, int iLote)
	{
		this.pagamentoFornecedor=pagamento;
		System.out.println("Building detalheAB240 for bank: "+pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		String codigoCompe=pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe();
		switch (codigoCompe) 
		{
		case "237":
			this.detalheABradesco=new DetalhePagamentoSegmentoA240Bradesco(pagamento, iLote);
			this.detalheBBradesco=new DetalhePagamentoSegmentoB240Bradesco(pagamento, iLote);
			this.numberRegistrosDetalhes=2;
			break;
		case "341":
			this.detalheAItau=new DetalhePagamentoSegmentoA240Itau(pagamento, iLote);
			this.detalheBItau=new DetalhePagamentoSegmentoB240Itau(pagamento, iLote);
			this.numberRegistrosDetalhes=2;
			break;
		case "033":
			if(pagamento.getFavorecido().getNumeroBoletoDARF().length()>0)
			{
				if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==47)
				{
					this.detalheJSantander=new DetalhePagamentoSegmentoJ240Santander(pagamento,iLote,1);
					this.detalheJ52Santander=new DetalhePagamentoSegmentoJ52240Santander(pagamento,iLote,2);
					this.numberRegistrosDetalhes=2;
				}
				else if(pagamento.getFavorecido().getNumeroBoletoDARF().length()==48)
				{
					this.detalheOSantander = new DetalhePagamentoSegmentoO240Santander(pagamento, iLote, 1);
					this.numberRegistrosDetalhes=1;
				}
			}
			else
			{
				this.detalheASantander=new DetalhePagamentoSegmentoA240Santander(pagamento, iLote);
				this.detalheBSantander=new DetalhePagamentoSegmentoB240Santander(pagamento, iLote);
				this.numberRegistrosDetalhes=2;
			}
			break;
		case "460":
			this.detalheAUnavanti=new DetalhePagamentoSegmentoA240Unavanti(pagamento, iLote);
			this.detalheBUnavanti=new DetalhePagamentoSegmentoB240Unavanti(pagamento, iLote);
			this.numberRegistrosDetalhes=2;
			break;			
		default:
			break;
		}
	}
	

	public DetalhePagamentoAB240(PagamentoTituloCobranca pagamento, int iLote)
	{
		this.pagamentoTituloCobranca=pagamento;
//		if(pagamento.getPagador().getDadosBancarios().getCodigoBanco().contains("237"))
//		{
////			this.detalheABradesco=new DetalhePagamentoSegmentoA240Bradesco(pagamento, iLote);
////			this.detalheBBradesco=new DetalhePagamentoSegmentoB240Bradesco(pagamento, iLote);
//		}
//		
//		if(pagamento.getPagador().getDadosBancarios().getCodigoBanco().contains("341"))
//		{
////			this.detalheAItau=new DetalhePagamentoSegmentoA240Itau(pagamento, iLote);
////			this.detalheBItau=new DetalhePagamentoSegmentoB240Itau(pagamento, iLote);
//		}
//		
//		if(pagamento.getPagador().getDadosBancarios().getCodigoBanco().contains("033"))
//		{
////			this.detalheASantander=new DetalhePagamentoSegmentoA240Santander(pagamento, iLote);
////			this.detalheBSantander=new DetalhePagamentoSegmentoB240Santander(pagamento, iLote);
//		}
	}

	public DetalhePagamentoSegmentoA240Bradesco getDetalheABradesco() {
		return detalheABradesco;
	}

	public void setDetalheABradesco(DetalhePagamentoSegmentoA240Bradesco detalheABradesco) {
		this.detalheABradesco = detalheABradesco;
	}

	public DetalhePagamentoSegmentoB240Bradesco getDetalheBBradesco() {
		return detalheBBradesco;
	}

	public void setDetalheBBradesco(DetalhePagamentoSegmentoB240Bradesco detalheBBradesco) {
		this.detalheBBradesco = detalheBBradesco;
	}

	public DetalhePagamentoSegmentoA240Itau getDetalheAItau() {
		return detalheAItau;
	}

	public void setDetalheAItau(DetalhePagamentoSegmentoA240Itau detalheAItau) {
		this.detalheAItau = detalheAItau;
	}

	public DetalhePagamentoSegmentoB240Itau getDetalheBItau() {
		return detalheBItau;
	}

	public void setDetalheBItau(DetalhePagamentoSegmentoB240Itau detalheBItau) {
		this.detalheBItau = detalheBItau;
	}

	public DetalhePagamentoSegmentoA240Santander getDetalheASantander() {
		return detalheASantander;
	}

	public void setDetalheASantander(DetalhePagamentoSegmentoA240Santander detalheASantander) {
		this.detalheASantander = detalheASantander;
	}

	public DetalhePagamentoSegmentoB240Santander getDetalheBSantander() {
		return detalheBSantander;
	}

	public void setDetalheBSantander(DetalhePagamentoSegmentoB240Santander detalheBSantander) {
		this.detalheBSantander = detalheBSantander;
	}

	public PagamentoFornecedor getPagamento() {
		return pagamentoFornecedor;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamentoFornecedor = pagamento;
	}

	public PagamentoFornecedor getPagamentoFornecedor() {
		return pagamentoFornecedor;
	}

	public void setPagamentoFornecedor(PagamentoFornecedor pagamentoFornecedor) {
		this.pagamentoFornecedor = pagamentoFornecedor;
	}

	public PagamentoTituloCobranca getPagamentoTituloCobranca() {
		return pagamentoTituloCobranca;
	}

	public void setPagamentoTituloCobranca(PagamentoTituloCobranca pagamentoTituloCobranca) {
		this.pagamentoTituloCobranca = pagamentoTituloCobranca;
	}

	public DetalhePagamentoSegmentoA240Unavanti getDetalheAUnavanti() {
		return detalheAUnavanti;
	}

	public void setDetalheAUnavanti(DetalhePagamentoSegmentoA240Unavanti detalheAUnavanti) {
		this.detalheAUnavanti = detalheAUnavanti;
	}

	public DetalhePagamentoSegmentoB240Unavanti getDetalheBUnavanti() {
		return detalheBUnavanti;
	}

	public void setDetalheBUnavanti(DetalhePagamentoSegmentoB240Unavanti detalheBUnavanti) {
		this.detalheBUnavanti = detalheBUnavanti;
	}

	public DetalhePagamentoSegmentoJ240Santander getDetalheJSantander() {
		return this.detalheJSantander;
	}

	public void setDetalheJSantander(DetalhePagamentoSegmentoJ240Santander detalheJSantander) {
		this.detalheJSantander = detalheJSantander;
	}

	public DetalhePagamentoSegmentoJ52240Santander getDetalheJ52Santander() {
		return this.detalheJ52Santander;
	}

	public void setDetalheJ52Santander(DetalhePagamentoSegmentoJ52240Santander detalheJ52Santander) {
		this.detalheJ52Santander = detalheJ52Santander;
	}
}

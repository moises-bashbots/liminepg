package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class DetalhePagamentoSegmentoO240Santander extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy");
	private DarfNumerado darf = null;
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N"); 
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","O","A");
	private FieldCNAB tipoDeMovimento=new FieldCNAB(6,15,15,1,"Tipo de movimento","0","N");
	private FieldCNAB codigoMovimentoRemessa=new FieldCNAB(7,16,17,2,"Codigo de instrucao para movimento","00","N");
	private FieldCNAB codigoDeBarras=new FieldCNAB(8,18,61,44,"Codigo de barras","","N");	
	private FieldCNAB nomeConcessionaria=new FieldCNAB(9,62,91,30,"Nome concessinaria - Orgao publico","","A");
	private FieldCNAB dataDeVencimento=new FieldCNAB(10,92,99,8,"Data de vencimento","","N");
	private FieldCNAB dataDoPagamento=new FieldCNAB(11,100,107,8,"Data do pagamento","","N");
	private FieldCNAB valorTotalDoPagamento=new FieldCNAB(12,108,122,15,"Valor total do pagamento","","N",2);
	private FieldCNAB numeroDocumentoCliente=new FieldCNAB(13,123,142,20,"Numero documento cliente","","A");
	private FieldCNAB numeroDocumentoBanco=new FieldCNAB(14,143,162,20,"Numero documento banco","","A");
	private FieldCNAB filler=new FieldCNAB(15,163,230,68,"Filler brancos","","A");
	private FieldCNAB ocorrenciasParaRetorno=new FieldCNAB(16,231,240,10,"Ocorrencias para retorno","","A");
	
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	public DetalhePagamentoSegmentoO240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoJ");
	}
	
	public DetalhePagamentoSegmentoO240Santander(PagamentoFornecedor pagamento, int iLote, int iRegistro)
	{
		super();
		this.darf = new DarfNumerado(pagamento.getFavorecido().getNumeroBoletoDARF());
		this.setSize(240);
		this.setTipo("SegmentoJ");
		this.pagamento=pagamento;
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getNumeroRegistro().setContent(Integer.toString(iRegistro));
		this.getCodigoDeBarras().setContent(this.darf.getCodigoBarras44());
		this.getNomeConcessionaria().setContent(this.darf.getOrgao());
		this.getDataDeVencimento().setContent(sdf.format(pagamento.getFavorecido().getDataVencimento()));
		this.getDataDoPagamento().setContent(sdf.format(Calendar.getInstance().getTime()));
		this.getValorTotalDoPagamento().setContent(String.valueOf(this.darf.getValorCentavos()));
		this.buildSegmento();		
	}
	
	public void buildSegmento()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosDetalhe = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB servico = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB aviso = new GrupoRegistrosCNAB();		
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		servico.getFieldsCNAB().put("numeroRegistro", this.numeroRegistro);
		servico.getFieldsCNAB().put("segmento", this.segmento);
		servico.getFieldsCNAB().put("tipoDeMovimento", this.tipoDeMovimento);
		servico.getFieldsCNAB().put("codigoMovimentoRemessa", this.codigoMovimentoRemessa);
		servico.getFieldsCNAB().put("codigoDeBarras", this.codigoDeBarras);
		servico.getFieldsCNAB().put("nomeConcessionaria", this.nomeConcessionaria);
		servico.getFieldsCNAB().put("dataDeVencimento", this.dataDeVencimento);
		servico.getFieldsCNAB().put("dataDoPagamento", this.dataDoPagamento);
		servico.getFieldsCNAB().put("valorTotalDoPagamento", this.valorTotalDoPagamento);
		servico.getFieldsCNAB().put("numeroDocumentoCliente",this.numeroDocumentoCliente);
		servico.getFieldsCNAB().put("numeroDocumentoBanco", this.numeroDocumentoBanco);			
		aviso.getFieldsCNAB().put("filler", this.filler);
		aviso.getFieldsCNAB().put("ocorrenciasParaRetorno", this.ocorrenciasParaRetorno);
		
		segmentosDetalhe.put("controle",controle);
		segmentosDetalhe.put("servico",servico);
		segmentosDetalhe.put("aviso", aviso);
		
		this.segmentosCNAB=segmentosDetalhe;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoO240Santander.sdf = sdf;
	}

	public DarfNumerado getDarf() {
		return darf;
	}

	public void setDarf(DarfNumerado darf) {
		this.darf = darf;
	}

	public FieldCNAB getBanco() {
		return banco;
	}

	public void setBanco(FieldCNAB banco) {
		this.banco = banco;
	}

	public FieldCNAB getLote() {
		return lote;
	}

	public void setLote(FieldCNAB lote) {
		this.lote = lote;
	}

	public FieldCNAB getRegistro() {
		return registro;
	}

	public void setRegistro(FieldCNAB registro) {
		this.registro = registro;
	}

	public FieldCNAB getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(FieldCNAB numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public FieldCNAB getSegmento() {
		return segmento;
	}

	public void setSegmento(FieldCNAB segmento) {
		this.segmento = segmento;
	}

	public FieldCNAB getTipoDeMovimento() {
		return tipoDeMovimento;
	}

	public void setTipoDeMovimento(FieldCNAB tipoDeMovimento) {
		this.tipoDeMovimento = tipoDeMovimento;
	}

	public FieldCNAB getCodigoMovimentoRemessa() {
		return codigoMovimentoRemessa;
	}

	public void setCodigoMovimentoRemessa(FieldCNAB codigoMovimentoRemessa) {
		this.codigoMovimentoRemessa = codigoMovimentoRemessa;
	}

	public FieldCNAB getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(FieldCNAB codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public FieldCNAB getNomeConcessionaria() {
		return nomeConcessionaria;
	}

	public void setNomeConcessionaria(FieldCNAB nomeConcessionaria) {
		this.nomeConcessionaria = nomeConcessionaria;
	}

	public FieldCNAB getDataDeVencimento() {
		return dataDeVencimento;
	}

	public void setDataDeVencimento(FieldCNAB dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public FieldCNAB getDataDoPagamento() {
		return dataDoPagamento;
	}

	public void setDataDoPagamento(FieldCNAB dataDoPagamento) {
		this.dataDoPagamento = dataDoPagamento;
	}

	public FieldCNAB getValorTotalDoPagamento() {
		return valorTotalDoPagamento;
	}

	public void setValorTotalDoPagamento(FieldCNAB valorTotalDoPagamento) {
		this.valorTotalDoPagamento = valorTotalDoPagamento;
	}

	public FieldCNAB getNumeroDocumentoCliente() {
		return numeroDocumentoCliente;
	}

	public void setNumeroDocumentoCliente(FieldCNAB numeroDocumentoCliente) {
		this.numeroDocumentoCliente = numeroDocumentoCliente;
	}

	public FieldCNAB getNumeroDocumentoBanco() {
		return numeroDocumentoBanco;
	}

	public void setNumeroDocumentoBanco(FieldCNAB numeroDocumentoBanco) {
		this.numeroDocumentoBanco = numeroDocumentoBanco;
	}

	public FieldCNAB getFiller() {
		return filler;
	}

	public void setFiller(FieldCNAB filler) {
		this.filler = filler;
	}

	public FieldCNAB getOcorrenciasParaRetorno() {
		return ocorrenciasParaRetorno;
	}

	public void setOcorrenciasParaRetorno(FieldCNAB ocorrenciasParaRetorno) {
		this.ocorrenciasParaRetorno = ocorrenciasParaRetorno;
	}

	public PagamentoFornecedor getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}


}

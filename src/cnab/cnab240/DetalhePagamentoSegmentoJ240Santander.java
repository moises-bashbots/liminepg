package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class DetalhePagamentoSegmentoJ240Santander extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy");
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N"); 
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","J","A");
	private FieldCNAB tipoMovimento=new FieldCNAB(6,15,15,1,"Tipo de movimento 0 inclusao, 2 estorno, 5 alteracao, 8 inclusao compror, 9 exclusao","0","N");
	private FieldCNAB codigoInstrucaoMovimento=new FieldCNAB(7,16,17,2,"Codigo da instrucao para movimento 00 inclusao, 09 inclusao bloqueado, 10 alteracao bloqueio, 11 alteracao desbloqueio, 14 autorizacao pagamento, 33 estorno","00","N");
	private FieldCNAB codigoDeBarras=new FieldCNAB(8,18,61,44,"Codigo de barras","","A");
	private FieldCNAB nomeDoBeneficiario=new FieldCNAB(9,62,91,30,"Nome do beneficiario","","A");
	private FieldCNAB vencimento=new FieldCNAB(10,92,99,8,"Data do Vencimento Nominal","","N");
	private FieldCNAB valorDocumento=new FieldCNAB(11,100,114,15,"Valor do Documento Nominal","","N",2);
	private FieldCNAB descontoMaisAbatimento=new FieldCNAB(12,115,129,15,"Valor do Abatimento","","N",2);
	private FieldCNAB multaMaisJuros=new FieldCNAB(13,130,144,15,"Valor da Multa","","N",2);
	private FieldCNAB dataPagamento=new FieldCNAB(14,145,152,8,"Data do Pagamento","","N");
	private FieldCNAB valorPagamento=new FieldCNAB(15,153,167,15,"Valor do Pagamento","","N",2);
	private FieldCNAB quantidadeMoeda=new FieldCNAB(16,168,182,15,"Quantidade da Moeda","","N",5);
	private FieldCNAB numeroDocumentoCliente=new FieldCNAB(17,183,202,20,"Numero Documento Cliente","","A");
	private FieldCNAB numeroDocumentoBanco=new FieldCNAB(18,203,222,20,"Numero Documento Banco","","A");
	private FieldCNAB codigoMoeda=new FieldCNAB(19,223,224,2,"Codigo Moeda","","N");
	private FieldCNAB filler=new FieldCNAB(20,225,230,6,"Filler","","A");
	private FieldCNAB ocorrenciasRetorno=new FieldCNAB(21,231,240,10,"Ocorrencias Retorno","","A");
	
	
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	public DetalhePagamentoSegmentoJ240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoJ");
	}
	
	public DetalhePagamentoSegmentoJ240Santander(PagamentoFornecedor pagamento, int iLote, int iRegistro)
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoJ");
		this.pagamento=pagamento;
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());		
		this.getLote().setContent(Integer.toString(iLote));
		this.getNumeroRegistro().setContent(Integer.toString(iRegistro));
		this.getCodigoDeBarras().setContent(
				this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(0,4)
				+this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(32,33)
				+this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(33,47)
				+this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(4,9)
				+this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(10,20)
				+this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(21,31)
				);
		this.getNomeDoBeneficiario().setContent(this.pagamento.getFavorecido().getNome());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1997);
		cal.set(Calendar.MONTH, 10-1);
		cal.set(Calendar.DAY_OF_MONTH, 7);
		int fatorVencimento=Integer.parseInt(this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(33,37));
		System.out.println(fatorVencimento);
		cal.add(Calendar.DAY_OF_MONTH, fatorVencimento);
		System.out.println(cal.getTime());
		this.getVencimento().setContent(sdf.format(cal.getTime()));
		this.getValorDocumento().setContent(this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(37,47));
		this.getDataPagamento().setContent(sdf.format(this.pagamento.getCredito().getDataPagamento()));
		this.getValorPagamento().setContent(this.pagamento.getFavorecido().getNumeroBoletoDARF().substring(37,47));
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
		servico.getFieldsCNAB().put("tipoMovimento",this.tipoMovimento);
		servico.getFieldsCNAB().put("codigoInstrucaoMovimento", this.codigoInstrucaoMovimento);
		servico.getFieldsCNAB().put("codigoDeBarras", this.codigoDeBarras);
		servico.getFieldsCNAB().put("nomeDoBeneficiario", this.nomeDoBeneficiario);
		servico.getFieldsCNAB().put("vencimento", this.vencimento);
		servico.getFieldsCNAB().put("valorDocumento", this.valorDocumento);
		servico.getFieldsCNAB().put("descontoMaisAbatimento", this.descontoMaisAbatimento);
		servico.getFieldsCNAB().put("multaMaisJuros", this.multaMaisJuros);
		servico.getFieldsCNAB().put("dataPagamento", this.dataPagamento);
		servico.getFieldsCNAB().put("valorPagamento", this.valorPagamento);
		servico.getFieldsCNAB().put("quantidadeMoeda", this.quantidadeMoeda);
		servico.getFieldsCNAB().put("numeroDocumentoCliente", this.numeroDocumentoCliente);
		servico.getFieldsCNAB().put("numeroDocumentoBanco", this.numeroDocumentoBanco);
		servico.getFieldsCNAB().put("codigoMoeda", this.codigoMoeda);
		aviso.getFieldsCNAB().put("filler", this.filler);
		aviso.getFieldsCNAB().put("ocorrenciasRetorno", this.ocorrenciasRetorno);
		segmentosDetalhe.put("controle",controle);
		segmentosDetalhe.put("servico",servico);
		segmentosDetalhe.put("aviso", aviso);
		
		this.segmentosCNAB=segmentosDetalhe;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoJ240Santander.sdf = sdf;
	}

	public FieldCNAB getBanco() {
		return this.banco;
	}

	public void setBanco(FieldCNAB banco) {
		this.banco = banco;
	}

	public FieldCNAB getLote() {
		return this.lote;
	}

	public void setLote(FieldCNAB lote) {
		this.lote = lote;
	}

	public FieldCNAB getRegistro() {
		return this.registro;
	}

	public void setRegistro(FieldCNAB registro) {
		this.registro = registro;
	}

	public FieldCNAB getNumeroRegistro() {
		return this.numeroRegistro;
	}

	public void setNumeroRegistro(FieldCNAB numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public FieldCNAB getSegmento() {
		return this.segmento;
	}

	public void setSegmento(FieldCNAB segmento) {
		this.segmento = segmento;
	}

	public FieldCNAB getTipoMovimento() {
		return this.tipoMovimento;
	}

	public void setTipoMovimento(FieldCNAB tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public FieldCNAB getCodigoInstrucaoMovimento() {
		return this.codigoInstrucaoMovimento;
	}

	public void setCodigoInstrucaoMovimento(FieldCNAB codigoInstrucaoMovimento) {
		this.codigoInstrucaoMovimento = codigoInstrucaoMovimento;
	}

	public FieldCNAB getCodigoDeBarras() {
		return this.codigoDeBarras;
	}

	public void setCodigoDeBarras(FieldCNAB codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public FieldCNAB getNomeDoBeneficiario() {
		return this.nomeDoBeneficiario;
	}

	public void setNomeDoBeneficiario(FieldCNAB nomeDoBeneficiario) {
		this.nomeDoBeneficiario = nomeDoBeneficiario;
	}

	public FieldCNAB getVencimento() {
		return this.vencimento;
	}

	public void setVencimento(FieldCNAB vencimento) {
		this.vencimento = vencimento;
	}

	public FieldCNAB getValorDocumento() {
		return this.valorDocumento;
	}

	public void setValorDocumento(FieldCNAB valorDocumento) {
		this.valorDocumento = valorDocumento;
	}

	public FieldCNAB getDescontoMaisAbatimento() {
		return this.descontoMaisAbatimento;
	}

	public void setDescontoMaisAbatimento(FieldCNAB descontoMaisAbatimento) {
		this.descontoMaisAbatimento = descontoMaisAbatimento;
	}

	public FieldCNAB getMultaMaisJuros() {
		return this.multaMaisJuros;
	}

	public void setMultaMaisJuros(FieldCNAB multaMaisJuros) {
		this.multaMaisJuros = multaMaisJuros;
	}

	public FieldCNAB getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(FieldCNAB dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public FieldCNAB getValorPagamento() {
		return this.valorPagamento;
	}

	public void setValorPagamento(FieldCNAB valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public FieldCNAB getQuantidadeMoeda() {
		return this.quantidadeMoeda;
	}

	public void setQuantidadeMoeda(FieldCNAB quantidadeMoeda) {
		this.quantidadeMoeda = quantidadeMoeda;
	}

	public FieldCNAB getNumeroDocumentoCliente() {
		return this.numeroDocumentoCliente;
	}

	public void setNumeroDocumentoCliente(FieldCNAB numeroDocumentoCliente) {
		this.numeroDocumentoCliente = numeroDocumentoCliente;
	}

	public FieldCNAB getNumeroDocumentoBanco() {
		return this.numeroDocumentoBanco;
	}

	public void setNumeroDocumentoBanco(FieldCNAB numeroDocumentoBanco) {
		this.numeroDocumentoBanco = numeroDocumentoBanco;
	}

	public FieldCNAB getCodigoMoeda() {
		return this.codigoMoeda;
	}

	public void setCodigoMoeda(FieldCNAB codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public FieldCNAB getFiller() {
		return this.filler;
	}

	public void setFiller(FieldCNAB filler) {
		this.filler = filler;
	}

	public FieldCNAB getOcorrenciasRetorno() {
		return this.ocorrenciasRetorno;
	}

	public void setOcorrenciasRetorno(FieldCNAB ocorrenciasRetorno) {
		this.ocorrenciasRetorno = ocorrenciasRetorno;
	}

	public PagamentoFornecedor getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}

}

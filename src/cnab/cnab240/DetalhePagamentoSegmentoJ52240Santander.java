package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class DetalhePagamentoSegmentoJ52240Santander extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy");
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N"); 
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","J","A");
	private FieldCNAB filler=new FieldCNAB(6,15,15,1,"Filler","","A");
	private FieldCNAB codigoMovimentoRemessa=new FieldCNAB(7,16,17,2,"Brancos","00","N");
	private FieldCNAB identificacaoRegistro=new FieldCNAB(8,18,19,2,"Identificacao Registro","52","N");
	private FieldCNAB tipoInscricaoPagador=new FieldCNAB(9,20,20,1,"Tipo inscricao Pagador ","","N");
	private FieldCNAB cpfCnpjPagador=new FieldCNAB(10,21,35,15,"CPF CNPJ Pagador ","","N");
	private FieldCNAB nomePagador=new FieldCNAB(11,36,75,40,"Nome Pagador ","","A");
	private FieldCNAB tipoInscricaoBeneficiario=new FieldCNAB(12,76,76,1,"Tipo inscricao Beneficiario ","","N");
	private FieldCNAB cpfCnpjBeneficiario=new FieldCNAB(13,77,91,15,"CPF CNPJ Beneficiario ","","N");
	private FieldCNAB nomeBeneficiario=new FieldCNAB(14,92,131,40,"Nome Beneficiario","","A");
	private FieldCNAB tipoInscricaoSacador=new FieldCNAB(15,132,132,1,"Tipo inscricao Sacador","","N");
	private FieldCNAB cpfCnpjSacador=new FieldCNAB(16,133,147,15,"CPF CNPJ Sacador","","N");
	private FieldCNAB nomeSacador=new FieldCNAB(17,148,187,40,"Nome Sacador","","A");
	private FieldCNAB filler2=new FieldCNAB(18,188,240,53,"Filler","","A");
		
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	public DetalhePagamentoSegmentoJ52240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoJ");
	}
	
	public DetalhePagamentoSegmentoJ52240Santander(PagamentoFornecedor pagamento, int iLote, int iRegistro)
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoJ");
		this.pagamento=pagamento;
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getNumeroRegistro().setContent(Integer.toString(iRegistro));
		this.getTipoInscricaoBeneficiario().setContent(this.pagamento.getFavorecido().getTipoDeInscricao());
		this.getCpfCnpjBeneficiario().setContent(this.pagamento.getFavorecido().getNumeroInscricao());
		this.getNomeBeneficiario().setContent(this.pagamento.getFavorecido().getNome());
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
		servico.getFieldsCNAB().put("filler", this.filler);
		servico.getFieldsCNAB().put("codigoMovimentoRemessa", this.codigoMovimentoRemessa);
		servico.getFieldsCNAB().put("identificacaoRegistro",this.identificacaoRegistro);
		servico.getFieldsCNAB().put("tipoInscricaoPagador", this.tipoInscricaoPagador);
		servico.getFieldsCNAB().put("cpfCnpjPagador", this.cpfCnpjPagador);
		servico.getFieldsCNAB().put("nomePagador",this.nomePagador);
		servico.getFieldsCNAB().put("tipoInscricaoBeneficiario", this.tipoInscricaoBeneficiario);
		servico.getFieldsCNAB().put("cpfCnpjBeneficiario", this.cpfCnpjBeneficiario);
		servico.getFieldsCNAB().put("nomeBeneficiario",this.nomeBeneficiario);
		servico.getFieldsCNAB().put("tipoInscricaoSacador", this.tipoInscricaoSacador);
		servico.getFieldsCNAB().put("cpfCnpjSacador", this.cpfCnpjSacador);
		servico.getFieldsCNAB().put("nomeSacador",this.nomeSacador);	
		aviso.getFieldsCNAB().put("filler2", this.filler2);
		
		segmentosDetalhe.put("controle",controle);
		segmentosDetalhe.put("servico",servico);
		segmentosDetalhe.put("aviso", aviso);
		
		this.segmentosCNAB=segmentosDetalhe;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoJ52240Santander.sdf = sdf;
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

	public FieldCNAB getFiller() {
		return this.filler;
	}

	public void setFiller(FieldCNAB filler) {
		this.filler = filler;
	}

	public FieldCNAB getCodigoMovimentoRemessa() {
		return this.codigoMovimentoRemessa;
	}

	public void setCodigoMovimentoRemessa(FieldCNAB codigoMovimentoRemessa) {
		this.codigoMovimentoRemessa = codigoMovimentoRemessa;
	}

	public FieldCNAB getIdentificacaoRegistro() {
		return this.identificacaoRegistro;
	}

	public void setIdentificacaoRegistro(FieldCNAB identificacaoRegistro) {
		this.identificacaoRegistro = identificacaoRegistro;
	}

	public FieldCNAB getTipoInscricaoPagador() {
		return this.tipoInscricaoPagador;
	}

	public void setTipoInscricaoPagador(FieldCNAB tipoInscricaoPagador) {
		this.tipoInscricaoPagador = tipoInscricaoPagador;
	}

	public FieldCNAB getCpfCnpjPagador() {
		return this.cpfCnpjPagador;
	}

	public void setCpfCnpjPagador(FieldCNAB cpfCnpjPagador) {
		this.cpfCnpjPagador = cpfCnpjPagador;
	}

	public FieldCNAB getNomePagador() {
		return this.nomePagador;
	}

	public void setNomePagador(FieldCNAB nomePagador) {
		this.nomePagador = nomePagador;
	}

	public FieldCNAB getTipoInscricaoBeneficiario() {
		return this.tipoInscricaoBeneficiario;
	}

	public void setTipoInscricaoBeneficiario(FieldCNAB tipoInscricaoBeneficiario) {
		this.tipoInscricaoBeneficiario = tipoInscricaoBeneficiario;
	}

	public FieldCNAB getCpfCnpjBeneficiario() {
		return this.cpfCnpjBeneficiario;
	}

	public void setCpfCnpjBeneficiario(FieldCNAB cpfCnpjBeneficiario) {
		this.cpfCnpjBeneficiario = cpfCnpjBeneficiario;
	}

	public FieldCNAB getNomeBeneficiario() {
		return this.nomeBeneficiario;
	}

	public void setNomeBeneficiario(FieldCNAB nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public FieldCNAB getTipoInscricaoSacador() {
		return this.tipoInscricaoSacador;
	}

	public void setTipoInscricaoSacador(FieldCNAB tipoInscricaoSacador) {
		this.tipoInscricaoSacador = tipoInscricaoSacador;
	}

	public FieldCNAB getCpfCnpjSacador() {
		return this.cpfCnpjSacador;
	}

	public void setCpfCnpjSacador(FieldCNAB cpfCnpjSacador) {
		this.cpfCnpjSacador = cpfCnpjSacador;
	}

	public FieldCNAB getNomeSacador() {
		return this.nomeSacador;
	}

	public void setNomeSacador(FieldCNAB nomeSacador) {
		this.nomeSacador = nomeSacador;
	}

	public FieldCNAB getFiller2() {
		return this.filler2;
	}

	public void setFiller2(FieldCNAB filler2) {
		this.filler2 = filler2;
	}

	public PagamentoFornecedor getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}
}

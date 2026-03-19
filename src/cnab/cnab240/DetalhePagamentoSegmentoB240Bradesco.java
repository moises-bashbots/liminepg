package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;
import utils.Utils;

public class DetalhePagamentoSegmentoB240Bradesco extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy");
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N"); 
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","B","A");
	private FieldCNAB cnab=new FieldCNAB(6,15,17,3,"Uso exclusivo FEBRABAN/CNAB cnab","","A");
	private FieldCNAB tipoInscricao=new FieldCNAB(7,18,18,1,"Tipo de Inscricao do Favorecido","","N");	
	private FieldCNAB numeroInscricao=new FieldCNAB(8,19,32,14,"Numero de Inscricao do Favorecido","","N");
	private FieldCNAB logradouro=new FieldCNAB(9,33,62,30,"Nome da Rua,Av,Pca,etc","","A");
	private FieldCNAB numero=new FieldCNAB(10,63,67,5,"Numero do Local","","N");
	private FieldCNAB complemento=new FieldCNAB(11,68,82,15,"Casa, Apto, Etc","","A");
	private FieldCNAB bairro=new FieldCNAB(12,83,97,15,"Bairro","","A");
	private FieldCNAB cidade=new FieldCNAB(13,98,117,20,"Nome da Cidade","","A");
	private FieldCNAB cep=new FieldCNAB(14,118,122,5,"CEP","","N");
	private FieldCNAB complementoCEP=new FieldCNAB(15,123,125,3,"Complemento CEP","","A");	
	private FieldCNAB estado=new FieldCNAB(16,126,127,2,"Sigla do Estado","","A");
	private FieldCNAB vencimento=new FieldCNAB(17,128,135,8,"Data do Vencimento Nominal","","N");
	private FieldCNAB valorDocumento=new FieldCNAB(18,136,150,15,"Valor do Documento Nominal","","N",2);
	private FieldCNAB abatimento=new FieldCNAB(19,151,165,15,"Valor do Abatimento","","N",2);
	private FieldCNAB desconto=new FieldCNAB(20,166,180,15,"Valor do Desconto","","N",2);
	private FieldCNAB mora=new FieldCNAB(21,181,195,15,"Valor da Mora","","N",2);
	private FieldCNAB multa=new FieldCNAB(22,196,210,15,"Valor da Multa","","N",2);
	private FieldCNAB codigoDocumentoFavorecido=new FieldCNAB(23,211,225,15,"Codigo/Documento do Favorecido","","A");	
	private FieldCNAB aviso=new FieldCNAB(24,226,226,1,"Aviso ao Favorecido","","N");
	private FieldCNAB codigoUGCentralizadora=new FieldCNAB(25,227,232,6,"Uso Exclusivo para o SIAPE","","N");
	private FieldCNAB identificacaoDoBancoNoSPB=new FieldCNAB(26,233,240,8,"Codigo ISPB","","N");
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	public DetalhePagamentoSegmentoB240Bradesco()
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoB");
	}
	
	public DetalhePagamentoSegmentoB240Bradesco(PagamentoFornecedor pagamento, int iLote)
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoB");
		this.pagamento=pagamento;
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getTipoInscricao().setContent(pagamento.getFavorecido().getTipoDeInscricao());
		this.getNumeroInscricao().setContent(pagamento.getFavorecido().getNumeroInscricao());
		this.getLogradouro().setContent(pagamento.getFavorecido().getEndereco().getLogradouro());
		this.getNumero().setContent(pagamento.getFavorecido().getEndereco().getNumero());
		this.getComplemento().setContent(pagamento.getFavorecido().getEndereco().getComplemento());
		this.getBairro().setContent(pagamento.getFavorecido().getEndereco().getBairro());
		this.getCidade().setContent(pagamento.getFavorecido().getEndereco().getCidade());
		this.getCep().setContent(pagamento.getFavorecido().getEndereco().getCep());
		this.getComplementoCEP().setContent(pagamento.getFavorecido().getEndereco().getComplementoCep());
		this.getEstado().setContent(pagamento.getFavorecido().getEndereco().getEstado());
		this.getVencimento().setContent(sdf.format(pagamento.getDataVencimento()));
		this.getValorDocumento().setContent(Long.toString(pagamento.getValorInteiro()));
//		Utils.doubleToCNABValue(this.getValorDocumento(), pagamento.getValorInteiro()/100.0);
		Utils.doubleToCNABValue(this.getAbatimento(), pagamento.getValorAbatimento());
		Utils.doubleToCNABValue(this.getDesconto(), pagamento.getValorDesconto());
		Utils.doubleToCNABValue(this.getMora(), pagamento.getValorMora());
		Utils.doubleToCNABValue(this.getMulta(), pagamento.getValorMulta());
		
		this.buildSegmento();
		
	}
	
	public void buildSegmento()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosDetalhe = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB servico = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB dadosComplementares = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB aviso = new GrupoRegistrosCNAB();		
		GrupoRegistrosCNAB codigoUGCentralizadora = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB identificacaoDoBancoNoSPB = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		servico.getFieldsCNAB().put("numeroRegistro", this.numeroRegistro);
		servico.getFieldsCNAB().put("segmento", this.segmento);
		cnab.getFieldsCNAB().put("cnab", this.cnab);
		dadosComplementares.getFieldsCNAB().put("tipoInscricao", this.tipoInscricao);
		dadosComplementares.getFieldsCNAB().put("numeroInscricao", this.numeroInscricao);
		dadosComplementares.getFieldsCNAB().put("logradouro", this.logradouro);
		dadosComplementares.getFieldsCNAB().put("numero", this.numero);
		dadosComplementares.getFieldsCNAB().put("complemento", this.complemento);
		dadosComplementares.getFieldsCNAB().put("bairro", this.bairro);
		dadosComplementares.getFieldsCNAB().put("cidade", this.cidade);
		dadosComplementares.getFieldsCNAB().put("cep", this.cep);
		dadosComplementares.getFieldsCNAB().put("complementoCEP", this.complementoCEP);
		dadosComplementares.getFieldsCNAB().put("estado", this.estado);
		dadosComplementares.getFieldsCNAB().put("vencimento", this.vencimento);
		dadosComplementares.getFieldsCNAB().put("valorDocumento", this.valorDocumento);
		dadosComplementares.getFieldsCNAB().put("abatimento", this.abatimento);
		dadosComplementares.getFieldsCNAB().put("desconto", this.desconto);
		dadosComplementares.getFieldsCNAB().put("mora", this.mora);
		dadosComplementares.getFieldsCNAB().put("multa", this.multa);
		dadosComplementares.getFieldsCNAB().put("codigoDocumentoFavorecido", this.codigoDocumentoFavorecido);
		aviso.getFieldsCNAB().put("aviso", this.aviso);
		codigoUGCentralizadora.getFieldsCNAB().put("codigoUGCentralizadora", this.codigoUGCentralizadora);
		identificacaoDoBancoNoSPB.getFieldsCNAB().put("identificacaoDoBancoNoSPB", this.identificacaoDoBancoNoSPB);
		
		segmentosDetalhe.put("controle",controle);
		segmentosDetalhe.put("servico",servico);
		segmentosDetalhe.put("cnab", cnab);
		segmentosDetalhe.put("dadosComplementares", dadosComplementares);
		segmentosDetalhe.put("aviso", aviso);
		segmentosDetalhe.put("codigoUGCentralizadora", codigoUGCentralizadora);
		segmentosDetalhe.put("identificacaoDoBancoNoSPB", identificacaoDoBancoNoSPB);		
		
		this.segmentosCNAB=segmentosDetalhe;
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

	public FieldCNAB getCnab() {
		return cnab;
	}

	public void setCnab(FieldCNAB cnab) {
		this.cnab = cnab;
	}

	public FieldCNAB getTipoInscricao() {
		return tipoInscricao;
	}

	public void setTipoInscricao(FieldCNAB tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}

	public FieldCNAB getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(FieldCNAB numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public FieldCNAB getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(FieldCNAB logradouro) {
		this.logradouro = logradouro;
	}

	public FieldCNAB getNumero() {
		return numero;
	}

	public void setNumero(FieldCNAB numero) {
		this.numero = numero;
	}

	public FieldCNAB getComplemento() {
		return complemento;
	}

	public void setComplemento(FieldCNAB complemento) {
		this.complemento = complemento;
	}

	public FieldCNAB getBairro() {
		return bairro;
	}

	public void setBairro(FieldCNAB bairro) {
		this.bairro = bairro;
	}

	public FieldCNAB getCidade() {
		return cidade;
	}

	public void setCidade(FieldCNAB cidade) {
		this.cidade = cidade;
	}

	public FieldCNAB getCep() {
		return cep;
	}

	public void setCep(FieldCNAB cep) {
		this.cep = cep;
	}

	public FieldCNAB getComplementoCEP() {
		return complementoCEP;
	}

	public void setComplementoCEP(FieldCNAB complementoCEP) {
		this.complementoCEP = complementoCEP;
	}

	public FieldCNAB getEstado() {
		return estado;
	}

	public void setEstado(FieldCNAB estado) {
		this.estado = estado;
	}

	public FieldCNAB getVencimento() {
		return vencimento;
	}

	public void setVencimento(FieldCNAB vencimento) {
		this.vencimento = vencimento;
	}

	public FieldCNAB getValorDocumento() {
		return valorDocumento;
	}

	public void setValorDocumento(FieldCNAB valorDocumento) {
		this.valorDocumento = valorDocumento;
	}

	public FieldCNAB getAbatimento() {
		return abatimento;
	}

	public void setAbatimento(FieldCNAB abatimento) {
		this.abatimento = abatimento;
	}

	public FieldCNAB getDesconto() {
		return desconto;
	}

	public void setDesconto(FieldCNAB desconto) {
		this.desconto = desconto;
	}

	public FieldCNAB getMora() {
		return mora;
	}

	public void setMora(FieldCNAB mora) {
		this.mora = mora;
	}

	public FieldCNAB getMulta() {
		return multa;
	}

	public void setMulta(FieldCNAB multa) {
		this.multa = multa;
	}

	public FieldCNAB getCodigoDocumentoFavorecido() {
		return codigoDocumentoFavorecido;
	}

	public void setCodigoDocumentoFavorecido(FieldCNAB codigoDocumentoFavorecido) {
		this.codigoDocumentoFavorecido = codigoDocumentoFavorecido;
	}

	public FieldCNAB getAviso() {
		return aviso;
	}

	public void setAviso(FieldCNAB aviso) {
		this.aviso = aviso;
	}

	public FieldCNAB getCodigoUGCentralizadora() {
		return codigoUGCentralizadora;
	}

	public void setCodigoUGCentralizadora(FieldCNAB codigoUGCentralizadora) {
		this.codigoUGCentralizadora = codigoUGCentralizadora;
	}

	public FieldCNAB getIdentificacaoDoBancoNoSPB() {
		return identificacaoDoBancoNoSPB;
	}

	public void setIdentificacaoDoBancoNoSPB(FieldCNAB identificacaoDoBancoNoSPB) {
		this.identificacaoDoBancoNoSPB = identificacaoDoBancoNoSPB;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoB240Bradesco.sdf = sdf;
	}

	public PagamentoFornecedor getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}
}

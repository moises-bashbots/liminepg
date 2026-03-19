package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;
import utils.Utils;

public class DetalhePagamentoSegmentoB240Santander extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy");
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N"); 
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","B","A");
	private FieldCNAB formaIniciacao=new FieldCNAB(6,15,16,2,"Forma de iniciacao 01 pix telefone, 02 pix email, 03 pix cpfcnpj, 04 pix aleatoria, 05 pis dados bancarios ","","A");
	private FieldCNAB filler=new FieldCNAB(7,17,17,1,"Uso exclusivo FEBRABAN/CNAB cnab","","A");
	private FieldCNAB tipoInscricao=new FieldCNAB(8,18,18,1,"Tipo de Inscricao do Favorecido","","N");	
	private FieldCNAB numeroInscricao=new FieldCNAB(9,19,32,14,"Numero de Inscricao do Favorecido","","N");
	private FieldCNAB informacao10= new FieldCNAB(10,33,67,35,"TXID","","A");
	private FieldCNAB informacao11= new FieldCNAB(11,68,127,60,"Informacoes para o recebedor","","A");
	private FieldCNAB informacao12= new FieldCNAB(12,128,226,99,"Chave PIX","","A");
	private FieldCNAB usoReservadoBanco= new FieldCNAB(13,227,232,6,"Uso reservado banco","","A");
	private FieldCNAB logradouro=new FieldCNAB(10,33,62,30,"Nome da Rua,Av,Pca,etc","","A");
	private FieldCNAB numero=new FieldCNAB(11,63,67,5,"Numero do Local","","N");
	private FieldCNAB complemento=new FieldCNAB(12,68,82,15,"Casa, Apto, Etc","","A");
	private FieldCNAB bairro=new FieldCNAB(13,83,97,15,"Bairro","","A");
	private FieldCNAB cidade=new FieldCNAB(14,98,117,20,"Nome da Cidade","","A");
	private FieldCNAB cep=new FieldCNAB(15,118,122,5,"CEP","","N");
	private FieldCNAB complementoCEP=new FieldCNAB(16,123,125,3,"Complemento CEP","","A");	
	private FieldCNAB estado=new FieldCNAB(17,126,127,2,"Sigla do Estado","","A");
	private FieldCNAB vencimento=new FieldCNAB(18,128,135,8,"Data do Vencimento Nominal","","N");
	private FieldCNAB valorDocumento=new FieldCNAB(19,136,150,15,"Valor do Documento Nominal","","N",2);
	private FieldCNAB abatimento=new FieldCNAB(20,151,165,15,"Valor do Abatimento","","N",2);
	private FieldCNAB desconto=new FieldCNAB(21,166,180,15,"Valor do Desconto","","N",2);
	private FieldCNAB mora=new FieldCNAB(22,181,195,15,"Valor da Mora","","N",2);
	private FieldCNAB multa=new FieldCNAB(23,196,210,15,"Valor da Multa","","N",2);
	private FieldCNAB horarioEnvioTED=new FieldCNAB(24,211,214,4,"Horario de envio da TED","","N");
	private FieldCNAB filler2=new FieldCNAB(25,215,225,11,"Uso exclusivo FEBRABAN/CNAB cnab","","A");
	private FieldCNAB codigoHistoricoParaCredito=new FieldCNAB(26,226,229,4,"Codigo Historico para Credito","","N");	
	private FieldCNAB aviso=new FieldCNAB(27,230,230,1,"Aviso ao Favorecido","","N");
	private FieldCNAB filler3=new FieldCNAB(28,231,231,1,"Uso Exclusivo para o SIAPE","","A");
	private FieldCNAB tedParaInstituicaoFinanceira=new FieldCNAB(29,232,232,1,"Se TED vai ser para Instituicao Financeira S, se nao N","N","A");
	private FieldCNAB identificacaoDaIFnoSPB=new FieldCNAB(30,233,240,8,"Codigo ISPB","","A");
	
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	public DetalhePagamentoSegmentoB240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoB");
	}
	
	public DetalhePagamentoSegmentoB240Santander(PagamentoFornecedor pagamento, int iLote)
	{
		super();
		this.setSize(240);
		this.setTipo("SegmentoB");
		this.pagamento=pagamento;
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		String formaIniciacaoPIX="";
		switch (pagamento.getFavorecido().getTipoChavePIX()) {
		case "telefone":
			formaIniciacaoPIX="01";
			break;
		case "email":
			formaIniciacaoPIX="02";
			break;
		case "cpfcnpj":
			formaIniciacaoPIX="02";
			break;
		case "aleatoria":
			formaIniciacaoPIX="04";
			break;
		default:
			formaIniciacaoPIX="";
			break;
		}
		if(formaIniciacaoPIX.length()>0)		
		{
			this.getFormaIniciacao().setContent(formaIniciacaoPIX);
			this.getInformacao12().setContent(pagamento.getFavorecido().getChavePIX());
		}
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
		GrupoRegistrosCNAB dadosComplementares = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB aviso = new GrupoRegistrosCNAB();		
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		servico.getFieldsCNAB().put("numeroRegistro", this.numeroRegistro);
		servico.getFieldsCNAB().put("segmento", this.segmento);
		servico.getFieldsCNAB().put("filler", this.filler);			
		servico.getFieldsCNAB().put("formaIniciacao", this.formaIniciacao);
		dadosComplementares.getFieldsCNAB().put("tipoInscricao", this.tipoInscricao);
		dadosComplementares.getFieldsCNAB().put("numeroInscricao", this.numeroInscricao);
		if(this.formaIniciacao.getContent().trim().length()>0)
		{
			dadosComplementares.getFieldsCNAB().put("informacao10", this.informacao10);
			dadosComplementares.getFieldsCNAB().put("informacao11", this.informacao11);
			dadosComplementares.getFieldsCNAB().put("informacao12", this.informacao12);
			dadosComplementares.getFieldsCNAB().put("usoReverbadoBanco", this.usoReservadoBanco);
		}
		else {
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
			dadosComplementares.getFieldsCNAB().put("horarioEnvioTED", this.horarioEnvioTED);
			dadosComplementares.getFieldsCNAB().put("filler2", this.filler2);
			aviso.getFieldsCNAB().put("codigoHistoricoParaCredito", this.codigoHistoricoParaCredito);
			aviso.getFieldsCNAB().put("aviso", this.aviso);			
			aviso.getFieldsCNAB().put("filler3", this.filler3);
			aviso.getFieldsCNAB().put("tedParaInstituicaoFinanceira", this.tedParaInstituicaoFinanceira);
		}
		aviso.getFieldsCNAB().put("identificacaoDaIFnoSPB", this.identificacaoDaIFnoSPB);
		
		segmentosDetalhe.put("controle",controle);
		segmentosDetalhe.put("servico",servico);
		segmentosDetalhe.put("dadosComplementares", dadosComplementares);
		segmentosDetalhe.put("aviso", aviso);
		
		this.segmentosCNAB=segmentosDetalhe;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoB240Santander.sdf = sdf;
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

	public FieldCNAB getFiller() {
		return filler;
	}

	public void setFiller(FieldCNAB filler) {
		this.filler = filler;
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

	public FieldCNAB getHorarioEnvioTED() {
		return horarioEnvioTED;
	}

	public void setHorarioEnvioTED(FieldCNAB horarioEnvioTED) {
		this.horarioEnvioTED = horarioEnvioTED;
	}

	public FieldCNAB getFiller2() {
		return filler2;
	}

	public void setFiller2(FieldCNAB filler2) {
		this.filler2 = filler2;
	}

	public FieldCNAB getCodigoHistoricoParaCredito() {
		return codigoHistoricoParaCredito;
	}

	public void setCodigoHistoricoParaCredito(FieldCNAB codigoHistoricoParaCredito) {
		this.codigoHistoricoParaCredito = codigoHistoricoParaCredito;
	}

	public FieldCNAB getAviso() {
		return aviso;
	}

	public void setAviso(FieldCNAB aviso) {
		this.aviso = aviso;
	}

	public FieldCNAB getFiller3() {
		return filler3;
	}

	public void setFiller3(FieldCNAB filler3) {
		this.filler3 = filler3;
	}

	public FieldCNAB getTedParaInstituicaoFinanceira() {
		return tedParaInstituicaoFinanceira;
	}

	public void setTedParaInstituicaoFinanceira(FieldCNAB tedParaInstituicaoFinanceira) {
		this.tedParaInstituicaoFinanceira = tedParaInstituicaoFinanceira;
	}

	public FieldCNAB getIdentificacaoDaIFnoSPB() {
		return identificacaoDaIFnoSPB;
	}

	public void setIdentificacaoDaIFnoSPB(FieldCNAB identificacaoDaIFnoSPB) {
		this.identificacaoDaIFnoSPB = identificacaoDaIFnoSPB;
	}

	public PagamentoFornecedor getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}

	public FieldCNAB getFormaIniciacao() {
		return this.formaIniciacao;
	}

	public void setFormaIniciacao(FieldCNAB formaIniciacao) {
		this.formaIniciacao = formaIniciacao;
	}

	public FieldCNAB getInformacao10() {
		return this.informacao10;
	}

	public void setInformacao10(FieldCNAB informacao10) {
		this.informacao10 = informacao10;
	}

	public FieldCNAB getInformacao11() {
		return this.informacao11;
	}

	public void setInformacao11(FieldCNAB informacao11) {
		this.informacao11 = informacao11;
	}

	public FieldCNAB getInformacao12() {
		return this.informacao12;
	}

	public void setInformacao12(FieldCNAB informacao12) {
		this.informacao12 = informacao12;
	}

	public FieldCNAB getUsoReservadoBanco() {
		return this.usoReservadoBanco;
	}

	public void setUsoReservadoBanco(FieldCNAB usoReservadoBanco) {
		this.usoReservadoBanco = usoReservadoBanco;
	}

}

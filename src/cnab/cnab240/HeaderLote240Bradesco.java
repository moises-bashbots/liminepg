package cnab.cnab240;

import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class HeaderLote240Bradesco extends RegistroCNAB
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","1","N");
	
	private FieldCNAB operacao=new FieldCNAB(4,9,9,1,"Tipo da Operacao","C","A");
	private FieldCNAB servico=new FieldCNAB(5,10,11,2,"Tipo de Servico","","N");
	private FieldCNAB formaLancamento=new FieldCNAB(6,12,13,2,"Forma de Lancamento","","N");
	private FieldCNAB layoutDoLote=new FieldCNAB(7,14,16,3,"Numero Versao Layout do Lote","045","N");
	
	private FieldCNAB cnab0=new FieldCNAB(8,17,17,1,"Uso exclusivo FEBRABAN/CNAB cnab0","","A");
	
	private FieldCNAB tipoInscricao=new FieldCNAB(9,18,18,1,"Tipo de Inscricao da Empresa","","N");
	private FieldCNAB numeroInscricao=new FieldCNAB(10,19,32,14,"Numero de Inscricao da Empresa","","N");
	private FieldCNAB convenio=new FieldCNAB(11,33,52,20,"Codigo do Convenio no Banco","","A");
	private FieldCNAB codigoAgencia=new FieldCNAB(12,53,57,5,"Agencia Mantenedora da Conta","","N");
	private FieldCNAB digitoVerificadorAgencia=new FieldCNAB(13,58,58,1,"Digito Verificador da Agencia","","A");
	private FieldCNAB numeroConta=new FieldCNAB(14,59,70,12,"Numero da Conta Corrente","","N");
	private FieldCNAB digitoVerificadorConta=new FieldCNAB(15,71,71,1,"Digito Verificador da Conta","","A");
	private FieldCNAB digitoVerificadorAgenciaConta=new FieldCNAB(16,72,72,1,"Digito Verificador da Agencia/Conta","","A");
	private FieldCNAB nomeEmpresa=new FieldCNAB(17,73,102,30,"Nome da Empresa","","A");
	
	private FieldCNAB informacao1=new FieldCNAB(18,103,142,40,"Mensagem","","A");
	
	private FieldCNAB logradouro=new FieldCNAB(19,143,172,30,"Nome da Rua, Av, Pca, etc","","A");
	private FieldCNAB numero=new FieldCNAB(20,173,177,5,"Numero do local","","N");
	private FieldCNAB complemento=new FieldCNAB(21,178,192,15,"Casa, Apto, Sala, etc","","A");
	private FieldCNAB cidade=new FieldCNAB(22,193,212,20,"Nome da cidade","","A");
	private FieldCNAB cep=new FieldCNAB(23,213,217,5,"CEP","","N");
	private FieldCNAB complementoCep=new FieldCNAB(24,218,220,3,"Complemento CEP","","A");
	private FieldCNAB estado=new FieldCNAB(25,221,222,2,"Sigla do Estado","","A");
	private FieldCNAB indicativoFormaPagamento=new FieldCNAB(26,223,224,2,"Indicativo de Forma de Pagamento do Servico","01","N");
	
	
	private FieldCNAB cnab1=new FieldCNAB(27,225,230,6,"Uso Exclusivo FEBRABAN/CNAB cnab1","","A");
	
	private FieldCNAB ocorrencias=new FieldCNAB(28,231,240,10,"Codigos das ocorrencias para Retorno","","A");
	
	public HeaderLote240Bradesco()
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderLote240");
	}
	
	public HeaderLote240Bradesco(Pagador pagador, int iLote, String tipoMovimentacao)
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderLote240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		if(tipoMovimentacao.toLowerCase().contains("boleto"))
		{
			this.getServico().setContent("03");  // Pagamento de bloqueto eletrônico
		}
		else if(tipoMovimentacao.toLowerCase().contains("pix"))
		{
			this.getServico().setContent("45");  // Pagamento Fornecedor
		}
		else
		{
			this.getServico().setContent("20");  // Pagamento Fornecedor
		}
		
		if(tipoMovimentacao.toLowerCase().contains("cadastro"))
		{
			this.getFormaLancamento().setContent("99"); // 99 cadastro de favorecido
		}
		else if(tipoMovimentacao.toLowerCase().contains("ted"))
		{
			this.getFormaLancamento().setContent("03"); // 03 DOC/TED ; 41 TED outra titularidade; 01 credito em CC 
		}
		else if(tipoMovimentacao.toLowerCase().contains("titulosmesmobanco"))
		{
			this.getFormaLancamento().setContent("30");
		}
		else if(tipoMovimentacao.toLowerCase().contains("titulosoutrobanco"))
		{
			this.getFormaLancamento().setContent("31");
		}
		else
		{
			this.getFormaLancamento().setContent("01"); // DOC/TED
		}
		this.getTipoInscricao().setContent("2"); // 01 PF, 02 PJ
		this.getNumeroInscricao().setContent(pagador.getNumeroInscricao());
		this.getConvenio().setContent(pagador.getDadosBancarios().getConvenio());
		this.getCodigoAgencia().setContent(pagador.getDadosBancarios().getAgencia());
		this.getDigitoVerificadorAgencia().setContent(pagador.getDadosBancarios().getDigitoAgencia());
		this.getNumeroConta().setContent(pagador.getDadosBancarios().getNumeroConta());
		this.getDigitoVerificadorConta().setContent(pagador.getDadosBancarios().getDigitoConta());
		this.getDigitoVerificadorAgenciaConta().setContent(pagador.getDadosBancarios().getDigitoAgenciaConta());
		this.getNomeEmpresa().setContent(pagador.getNome());
		this.getLogradouro().setContent(pagador.getEndereco().getLogradouro());
		this.getNumero().setContent(pagador.getEndereco().getNumero());
		this.getComplemento().setContent(pagador.getEndereco().getComplemento());
		this.getCidade().setContent(pagador.getEndereco().getCidade());
		this.getCep().setContent(pagador.getEndereco().getCep());
		this.getComplementoCep().setContent(pagador.getEndereco().getComplementoCep());
		this.getEstado().setContent(pagador.getEndereco().getEstado());
		
		this.buildHeader();
		this.show();		
	}
	
	
	public void buildHeader()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosHeader = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB servico = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab0 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB empresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB informacao1 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB enderecoEmpresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB indicativoFormaPagamento = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab1 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB ocorrencias = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		servico.getFieldsCNAB().put("operacao", this.operacao);
		servico.getFieldsCNAB().put("servico", this.servico);
		servico.getFieldsCNAB().put("formaLancamento", this.formaLancamento);
		servico.getFieldsCNAB().put("layoutDoLote", this.layoutDoLote);
		cnab0.getFieldsCNAB().put("cnab0",this.cnab0);
		empresa.getFieldsCNAB().put("tipoInscricao",this.tipoInscricao);
		empresa.getFieldsCNAB().put("numeroInscricao",this.numeroInscricao);
		empresa.getFieldsCNAB().put("convenio",this.convenio);
		empresa.getFieldsCNAB().put("codigoAgencia",this.codigoAgencia);
		empresa.getFieldsCNAB().put("digitoVerificadorAgencia",this.digitoVerificadorAgencia);
		empresa.getFieldsCNAB().put("numeroConta",this.numeroConta);
		empresa.getFieldsCNAB().put("digitoVerificadorConta",this.digitoVerificadorConta);
		empresa.getFieldsCNAB().put("digitoVerificadorAgenciaConta",this.digitoVerificadorAgenciaConta);
		empresa.getFieldsCNAB().put("nomeEmpresa",this.nomeEmpresa);
		informacao1.getFieldsCNAB().put("informacao1", this.informacao1);		
		enderecoEmpresa.getFieldsCNAB().put("logradouro", this.logradouro);
		enderecoEmpresa.getFieldsCNAB().put("numero", this.numero);
		enderecoEmpresa.getFieldsCNAB().put("complemento", this.complemento);
		enderecoEmpresa.getFieldsCNAB().put("cidade", this.cidade);
		enderecoEmpresa.getFieldsCNAB().put("cep", this.cep);
		enderecoEmpresa.getFieldsCNAB().put("complementoCep", this.complementoCep);
		enderecoEmpresa.getFieldsCNAB().put("estado", this.estado);
		indicativoFormaPagamento.getFieldsCNAB().put("indicativoFormaPagamento",this.indicativoFormaPagamento);
		cnab1.getFieldsCNAB().put("cnab1",this.cnab1);
		ocorrencias.getFieldsCNAB().put("ocorrencias",this.ocorrencias);
		
	
		segmentosHeader.put("controle", controle);
		segmentosHeader.put("servico", servico);
		segmentosHeader.put("cnab0", cnab0);
		segmentosHeader.put("empresa", empresa);
		segmentosHeader.put("informacao1", informacao1);
		segmentosHeader.put("enderecoEmpresa", enderecoEmpresa);
		segmentosHeader.put("indicativoFormaPagamento", indicativoFormaPagamento);
		segmentosHeader.put("cnab1", cnab1);
		segmentosHeader.put("ocorrencias", ocorrencias);
		
		this.segmentosCNAB=segmentosHeader;
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

	public FieldCNAB getOperacao() {
		return operacao;
	}

	public void setOperacao(FieldCNAB operacao) {
		this.operacao = operacao;
	}

	public FieldCNAB getServico() {
		return servico;
	}

	public void setServico(FieldCNAB servico) {
		this.servico = servico;
	}

	public FieldCNAB getFormaLancamento() {
		return formaLancamento;
	}

	public void setFormaLancamento(FieldCNAB formaLancamento) {
		this.formaLancamento = formaLancamento;
	}

	public FieldCNAB getLayoutDoLote() {
		return layoutDoLote;
	}

	public void setLayoutDoLote(FieldCNAB layoutDoLote) {
		this.layoutDoLote = layoutDoLote;
	}

	public FieldCNAB getCnab0() {
		return cnab0;
	}

	public void setCnab0(FieldCNAB cnab0) {
		this.cnab0 = cnab0;
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

	public FieldCNAB getConvenio() {
		return convenio;
	}

	public void setConvenio(FieldCNAB convenio) {
		this.convenio = convenio;
	}

	public FieldCNAB getCodigoAgencia() {
		return codigoAgencia;
	}

	public void setCodigoAgencia(FieldCNAB codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}

	public FieldCNAB getDigitoVerificadorAgencia() {
		return digitoVerificadorAgencia;
	}

	public void setDigitoVerificadorAgencia(FieldCNAB digitoVerificadorAgencia) {
		this.digitoVerificadorAgencia = digitoVerificadorAgencia;
	}

	public FieldCNAB getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(FieldCNAB numeroConta) {
		this.numeroConta = numeroConta;
	}

	public FieldCNAB getDigitoVerificadorConta() {
		return digitoVerificadorConta;
	}

	public void setDigitoVerificadorConta(FieldCNAB digitoVerificadorConta) {
		this.digitoVerificadorConta = digitoVerificadorConta;
	}

	public FieldCNAB getDigitoVerificadorAgenciaConta() {
		return digitoVerificadorAgenciaConta;
	}

	public void setDigitoVerificadorAgenciaConta(FieldCNAB digitoVerificadorAgenciaConta) {
		this.digitoVerificadorAgenciaConta = digitoVerificadorAgenciaConta;
	}

	public FieldCNAB getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(FieldCNAB nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public FieldCNAB getInformacao1() {
		return informacao1;
	}

	public void setInformacao1(FieldCNAB informacao1) {
		this.informacao1 = informacao1;
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

	public FieldCNAB getComplementoCep() {
		return complementoCep;
	}

	public void setComplementoCep(FieldCNAB complementoCep) {
		this.complementoCep = complementoCep;
	}

	public FieldCNAB getEstado() {
		return estado;
	}

	public void setEstado(FieldCNAB estado) {
		this.estado = estado;
	}

	public FieldCNAB getIndicativoFormaPagamento() {
		return indicativoFormaPagamento;
	}

	public void setIndicativoFormaPagamento(FieldCNAB indicativoFormaPagamento) {
		this.indicativoFormaPagamento = indicativoFormaPagamento;
	}

	public FieldCNAB getCnab1() {
		return cnab1;
	}

	public void setCnab1(FieldCNAB cnab1) {
		this.cnab1 = cnab1;
	}

	public FieldCNAB getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(FieldCNAB ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public FieldCNAB getBanco() {
		return banco;
	}

	public void setBanco(FieldCNAB banco) {
		this.banco = banco;
	}

}

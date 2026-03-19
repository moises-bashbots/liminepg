package cnab.cnab240;

import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class HeaderLote240Itau extends RegistroCNAB
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","1","N");
	private FieldCNAB operacao=new FieldCNAB(4,9,9,1,"Tipo da Operacao","C","A");
	private FieldCNAB servico=new FieldCNAB(5,10,11,2,"Tipo de Servico","1","N");
	private FieldCNAB formaLancamento=new FieldCNAB(6,12,13,2,"Forma de Lancamento","","N");
	private FieldCNAB layoutDoLote=new FieldCNAB(7,14,16,3,"Numero Versao Layout do Lote",BuilderPaymentsMultibanco.getVersaoLayoutLote().get("341"),"N");
	private FieldCNAB brancos=new FieldCNAB(8,17,17,1,"Compmplemento de registro brancos","","A");
	
	private FieldCNAB tipoInscricao=new FieldCNAB(9,18,18,1,"Tipo de Inscricao da Empresa","","N");
	private FieldCNAB numeroInscricao=new FieldCNAB(10,19,32,14,"Numero de Inscricao da Empresa","","N");
	private FieldCNAB identificacaoLancamento=new FieldCNAB(11,33,36,4,"Identificacao do lancamento no extrato do favorecido","1707","A");
	private FieldCNAB brancos2=new FieldCNAB(12,37,52,20,"Complemento de registro brancos2","","A");
	private FieldCNAB codigoAgencia=new FieldCNAB(13,53,57,5,"Agencia Mantenedora da Conta","","N");
	private FieldCNAB brancos3=new FieldCNAB(14,58,58,1,"Compmplemento de registro brancos3","","A");
	private FieldCNAB numeroConta=new FieldCNAB(15,59,70,12,"Numero da Conta Corrente","","N");
	private FieldCNAB brancos4=new FieldCNAB(16,71,71,1,"Complemento de registro brancos4","","A");
	private FieldCNAB digitoVerificadorAgenciaConta=new FieldCNAB(17,72,72,1,"Digito Verificador da Agencia/Conta","","N");
	private FieldCNAB nomeEmpresa=new FieldCNAB(18,73,102,30,"Nome da Empresa","","A");
	private FieldCNAB finalidadeDoLote=new FieldCNAB(19,103,132,30,"Finalidade dos pagamentos do lote","","A");
	private FieldCNAB historicoDeCC=new FieldCNAB(20,133,142,10,"Complemento Historico CC Debitada","","A");
	
	private FieldCNAB logradouro=new FieldCNAB(21,143,172,30,"Nome da Rua, Av, Pca, etc","","A");
	private FieldCNAB numero=new FieldCNAB(22,173,177,5,"Numero do local","","N");
	private FieldCNAB complemento=new FieldCNAB(23,178,192,15,"Casa, Apto, Sala, etc","","A");
	private FieldCNAB cidade=new FieldCNAB(24,193,212,20,"Nome da cidade","","A");
	private FieldCNAB cep=new FieldCNAB(25,213,217,5,"CEP","","N");
	private FieldCNAB complementoCep=new FieldCNAB(26,218,220,3,"Complemento CEP","","A");
	private FieldCNAB estado=new FieldCNAB(27,221,222,2,"Sigla do Estado","","A");
	private FieldCNAB brancos5=new FieldCNAB(28,223,230,8,"Complemento de registro brancos5","","A");
	
	private FieldCNAB ocorrencias=new FieldCNAB(29,231,240,10,"Codigos das ocorrencias para Retorno","","A");
	
	public HeaderLote240Itau()
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderLote240");
	}
	
	public HeaderLote240Itau(Pagador pagador, int iLote, String formaLancamento)
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderLote240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getServico().setContent("20");  // Pagamento Fornecedor
		if(formaLancamento.toLowerCase().contains("cadastro"))
		{
			this.getFormaLancamento().setContent("99"); // 99 cadastro de favorecido
		}
		else if(formaLancamento.toLowerCase().contains("ted"))
		{
			this.getFormaLancamento().setContent("41"); // 03 DOC/TED ; 41 TED outra titularidade; 01 credito em CC 
		}
		else
		{
			this.getFormaLancamento().setContent("01"); // TEF CREDITO EM CC NO ITAU
		}
		this.getTipoInscricao().setContent("2"); // 01 PF, 02 PJ
		this.getNumeroInscricao().setContent(pagador.getNumeroInscricao());
		this.getCodigoAgencia().setContent(pagador.getDadosBancarios().getAgencia());
		this.getNumeroConta().setContent(pagador.getDadosBancarios().getNumeroConta()+pagador.getDadosBancarios().getDigitoConta());
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
		GrupoRegistrosCNAB empresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB enderecoEmpresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB ocorrencias = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		
		servico.getFieldsCNAB().put("operacao", this.operacao);
		servico.getFieldsCNAB().put("servico", this.servico);
		servico.getFieldsCNAB().put("formaLancamento", this.formaLancamento);
		servico.getFieldsCNAB().put("layoutDoLote", this.layoutDoLote);
		servico.getFieldsCNAB().put("brancos", this.brancos);
		
		empresa.getFieldsCNAB().put("tipoInscricao",this.tipoInscricao);
		empresa.getFieldsCNAB().put("numeroInscricao",this.numeroInscricao);
		empresa.getFieldsCNAB().put("identificacaoLancamento",this.identificacaoLancamento);
		empresa.getFieldsCNAB().put("brancos2",this.brancos2);
		empresa.getFieldsCNAB().put("codigoAgencia",this.codigoAgencia);
		empresa.getFieldsCNAB().put("brancos3",this.brancos3);		
		empresa.getFieldsCNAB().put("numeroConta",this.numeroConta);
		empresa.getFieldsCNAB().put("brancos4",this.brancos4);
		empresa.getFieldsCNAB().put("digitoVerificadorAgenciaConta",this.digitoVerificadorAgenciaConta);
		empresa.getFieldsCNAB().put("nomeEmpresa",this.nomeEmpresa);
		empresa.getFieldsCNAB().put("finalidadeDoLote",this.finalidadeDoLote);
		empresa.getFieldsCNAB().put("historicoDeCC",this.historicoDeCC);
			
		enderecoEmpresa.getFieldsCNAB().put("logradouro", this.logradouro);
		enderecoEmpresa.getFieldsCNAB().put("numero", this.numero);
		enderecoEmpresa.getFieldsCNAB().put("complemento", this.complemento);
		enderecoEmpresa.getFieldsCNAB().put("cidade", this.cidade);
		enderecoEmpresa.getFieldsCNAB().put("cep", this.cep);
		enderecoEmpresa.getFieldsCNAB().put("complementoCep", this.complementoCep);
		enderecoEmpresa.getFieldsCNAB().put("estado", this.estado);
		enderecoEmpresa.getFieldsCNAB().put("brancos5", this.brancos5);
		
		ocorrencias.getFieldsCNAB().put("ocorrencias",this.ocorrencias);
		
	
		segmentosHeader.put("controle", controle);
		segmentosHeader.put("servico", servico);
		segmentosHeader.put("empresa", empresa);
		segmentosHeader.put("enderecoEmpresa", enderecoEmpresa);
		segmentosHeader.put("ocorrencias", ocorrencias);
		
		this.segmentosCNAB=segmentosHeader;
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

	public FieldCNAB getBrancos() {
		return brancos;
	}

	public void setBrancos(FieldCNAB brancos) {
		this.brancos = brancos;
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

	public FieldCNAB getIdentificacaoLancamento() {
		return identificacaoLancamento;
	}

	public void setIdentificacaoLancamento(FieldCNAB identificacaoLancamento) {
		this.identificacaoLancamento = identificacaoLancamento;
	}

	public FieldCNAB getBrancos2() {
		return brancos2;
	}

	public void setBrancos2(FieldCNAB brancos2) {
		this.brancos2 = brancos2;
	}

	public FieldCNAB getCodigoAgencia() {
		return codigoAgencia;
	}

	public void setCodigoAgencia(FieldCNAB codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}

	public FieldCNAB getBrancos3() {
		return brancos3;
	}

	public void setBrancos3(FieldCNAB brancos3) {
		this.brancos3 = brancos3;
	}

	public FieldCNAB getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(FieldCNAB numeroConta) {
		this.numeroConta = numeroConta;
	}

	public FieldCNAB getBrancos4() {
		return brancos4;
	}

	public void setBrancos4(FieldCNAB brancos4) {
		this.brancos4 = brancos4;
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

	public FieldCNAB getFinalidadeDoLote() {
		return finalidadeDoLote;
	}

	public void setFinalidadeDoLote(FieldCNAB finalidadeDoLote) {
		this.finalidadeDoLote = finalidadeDoLote;
	}

	public FieldCNAB getHistoricoDeCC() {
		return historicoDeCC;
	}

	public void setHistoricoDeCC(FieldCNAB historicoDeCC) {
		this.historicoDeCC = historicoDeCC;
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

	public FieldCNAB getBrancos5() {
		return brancos5;
	}

	public void setBrancos5(FieldCNAB brancos5) {
		this.brancos5 = brancos5;
	}

	public FieldCNAB getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(FieldCNAB ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

}

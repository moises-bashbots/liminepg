package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class HeaderArquivo240Santander extends RegistroCNAB
{
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat sdft = new SimpleDateFormat("HHmmss");
	
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","0","N");	
	private FieldCNAB cnab0=new FieldCNAB(4,9,17,9,"Uso exclusivo FEBRABAN/CNAB cnab0","","A");	
	private FieldCNAB tipoInscricao=new FieldCNAB(5,18,18,1,"Tipo de Inscricao da Empresa","","N");
	private FieldCNAB numeroInscricao=new FieldCNAB(6,19,32,14,"Numero de Inscricao da Empresa","","N");
	private FieldCNAB convenio=new FieldCNAB(7,33,52,20,"Codigo do Convenio no Banco","","A");
	private FieldCNAB codigoAgencia=new FieldCNAB(8,53,57,5,"Agencia Mantenedora da Conta","","N");
	private FieldCNAB digitoVerificadorAgencia=new FieldCNAB(9,58,58,1,"Digito Verificador da Agencia","","A");
	private FieldCNAB numeroConta=new FieldCNAB(10,59,70,12,"Numero da Conta Corrente","","N");
	private FieldCNAB digitoVerificadorConta=new FieldCNAB(11,71,71,1,"Digito Verificador da Conta","","A");
	private FieldCNAB digitoVerificadorAgenciaConta=new FieldCNAB(12,72,72,1,"Digito Verificador da Agencia/Conta","","A");
	private FieldCNAB nomeEmpresa=new FieldCNAB(13,73,102,30,"Nome da Empresa","","A");
	private FieldCNAB nomeBanco=new FieldCNAB(14,103,132,30,"Nome do Banco","","A");
	private FieldCNAB cnab1=new FieldCNAB(15,133,142,10,"Uso Exclusivo FEBRABAN/CNAB cnab1","","A");
	private FieldCNAB codigoArquivo=new FieldCNAB(16,143,143,1,"Codigo Remessa/Retorno","","N");
	private FieldCNAB dataDeGeracaoArquivo=new FieldCNAB(17,144,151,8,"Data de Geracao do Arquivo","","N");
	private FieldCNAB horaDeGeracaoArquivo=new FieldCNAB(18,152,157,6,"Hora de Geracao do Arquivo","","N");
	private FieldCNAB numeroSequencialArquivo=new FieldCNAB(19,158,163,6,"Numero Sequencial do Arquivo","","N");
	private FieldCNAB layoutDoArquivo=new FieldCNAB(20,164,166,3,"Numero Versao Layout do Arquivo",BuilderPaymentsMultibanco.getVersaoLayout().get("033"),"N");
	private FieldCNAB densidadeArquivo=new FieldCNAB(21,167,171,5,"Densidade de Gravacao do Arquivo","0000","N");
	private FieldCNAB reservadoBanco=new FieldCNAB(22,172,191,20,"Para uso reservado do Banco","","A");
	private FieldCNAB reservadoEmpresa=new FieldCNAB(23,192,211,20,"Para uso reservado da Empresa","","A");	
	private FieldCNAB cnab2=new FieldCNAB(24,212,240,29,"Uso Exclusivo FEBRABAN/CNAB cnab2","","A");
	
	public HeaderArquivo240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderArquivo240");
	}
	
	
	public HeaderArquivo240Santander(Pagador pagador, int iArquivo)
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderArquivo240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getTipoInscricao().setContent(pagador.getTipoDeInscricao());
		this.getNumeroInscricao().setContent(pagador.getNumeroInscricao());
		this.getConvenio().setContent(pagador.getDadosBancarios().getConvenio());
		this.getCodigoAgencia().setContent(pagador.getDadosBancarios().getAgencia());
		this.getDigitoVerificadorAgencia().setContent(pagador.getDadosBancarios().getDigitoAgencia());
		this.getNumeroConta().setContent(pagador.getDadosBancarios().getNumeroConta());
		this.getDigitoVerificadorConta().setContent(pagador.getDadosBancarios().getDigitoConta());
		this.getDigitoVerificadorAgenciaConta().setContent(pagador.getDadosBancarios().getDigitoAgenciaConta());
		this.getNomeEmpresa().setContent(pagador.getNome());
		this.getNomeBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getNome());
		this.getCodigoArquivo().setContent("1"); //1 - Remessa; 2 - Retorno
		this.getDataDeGeracaoArquivo().setContent(sdf.format(Calendar.getInstance().getTime()));
		this.getHoraDeGeracaoArquivo().setContent(sdft.format(Calendar.getInstance().getTime()));
		this.getNumeroSequencialArquivo().setContent(Integer.toString(iArquivo));
		
		this.buildHeader();
		this.show();
		
	}
	public void buildHeader()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosHeader = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab0 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB empresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB nomeDoBanco = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab1 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB arquivo = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB reservadoBanco = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB reservadoEmpresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab2 = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		
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
		
		nomeDoBanco.getFieldsCNAB().put("nomeBanco", this.nomeBanco);
		
		cnab1.getFieldsCNAB().put("cnab1",this.cnab1);
		
		arquivo.getFieldsCNAB().put("codigoArquivo", this.codigoArquivo);
		arquivo.getFieldsCNAB().put("dataDeGeracaoArquivo", this.dataDeGeracaoArquivo);
		arquivo.getFieldsCNAB().put("horaDeGeracaoArquivo", this.horaDeGeracaoArquivo);
		arquivo.getFieldsCNAB().put("numeroSequencialArquivo", this.numeroSequencialArquivo);
		arquivo.getFieldsCNAB().put("layoutDoArquivo", this.layoutDoArquivo);
		arquivo.getFieldsCNAB().put("densidadeArquivo", this.densidadeArquivo);
		
		reservadoBanco.getFieldsCNAB().put("reservadoBanco", this.reservadoBanco);
		
		reservadoEmpresa.getFieldsCNAB().put("reservadoEmpresa", this.reservadoEmpresa);
		
		cnab2.getFieldsCNAB().put("cnab2", this.cnab2);
		
		segmentosHeader.put("controle", controle);
		segmentosHeader.put("cnab0", cnab0);
		segmentosHeader.put("empresa", empresa);
		segmentosHeader.put("nomeDoBanco", nomeDoBanco);
		segmentosHeader.put("cnab1", cnab1);
		segmentosHeader.put("arquivo", arquivo);
		segmentosHeader.put("reservadoBanco", reservadoBanco);
		segmentosHeader.put("reservadoEmpresa", reservadoEmpresa);
		segmentosHeader.put("cnab2", cnab2);
		
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

	public FieldCNAB getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(FieldCNAB nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public FieldCNAB getCnab1() {
		return cnab1;
	}

	public void setCnab1(FieldCNAB cnab1) {
		this.cnab1 = cnab1;
	}

	public FieldCNAB getCodigoArquivo() {
		return codigoArquivo;
	}

	public void setCodigoArquivo(FieldCNAB codigoArquivo) {
		this.codigoArquivo = codigoArquivo;
	}

	public FieldCNAB getDataDeGeracaoArquivo() {
		return dataDeGeracaoArquivo;
	}

	public void setDataDeGeracaoArquivo(FieldCNAB dataDeGeracaoArquivo) {
		this.dataDeGeracaoArquivo = dataDeGeracaoArquivo;
	}

	public FieldCNAB getHoraDeGeracaoArquivo() {
		return horaDeGeracaoArquivo;
	}

	public void setHoraDeGeracaoArquivo(FieldCNAB horaDeGeracaoArquivo) {
		this.horaDeGeracaoArquivo = horaDeGeracaoArquivo;
	}

	public FieldCNAB getNumeroSequencialArquivo() {
		return numeroSequencialArquivo;
	}

	public void setNumeroSequencialArquivo(FieldCNAB numeroSequencialArquivo) {
		this.numeroSequencialArquivo = numeroSequencialArquivo;
	}

	public FieldCNAB getLayoutDoArquivo() {
		return layoutDoArquivo;
	}

	public void setLayoutDoArquivo(FieldCNAB layoutDoArquivo) {
		this.layoutDoArquivo = layoutDoArquivo;
	}

	public FieldCNAB getDensidadeArquivo() {
		return densidadeArquivo;
	}

	public void setDensidadeArquivo(FieldCNAB densidadeArquivo) {
		this.densidadeArquivo = densidadeArquivo;
	}

	public FieldCNAB getReservadoBanco() {
		return reservadoBanco;
	}

	public void setReservadoBanco(FieldCNAB reservadoBanco) {
		this.reservadoBanco = reservadoBanco;
	}

	public FieldCNAB getReservadoEmpresa() {
		return reservadoEmpresa;
	}

	public void setReservadoEmpresa(FieldCNAB reservadoEmpresa) {
		this.reservadoEmpresa = reservadoEmpresa;
	}

	public FieldCNAB getCnab2() {
		return cnab2;
	}

	public void setCnab2(FieldCNAB cnab2) {
		this.cnab2 = cnab2;
	}
	
	
}

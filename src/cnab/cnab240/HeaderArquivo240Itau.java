package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class HeaderArquivo240Itau extends RegistroCNAB
{
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat sdft = new SimpleDateFormat("HHmmss");
	
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","0","N");
	private FieldCNAB brancos=new FieldCNAB(4,9,14,6,"Complemento de Registro","","A");
//	private FieldCNAB layoutDoArquivo=new FieldCNAB(5,15,17,3,"Numero Versao Layout do Arquivo",BuilderCNAB240MultipagMultibanco.getVersaoLayout().get("341"),"N");
	private FieldCNAB layoutDoArquivo=new FieldCNAB(5,15,17,3,"Numero Versao Layout do Arquivo",BuilderPaymentsMultibanco.getVersaoLayout().get("341"),"N");

	private FieldCNAB tipoInscricao=new FieldCNAB(6,18,18,1,"Tipo de Inscricao da Empresa","","N");
	private FieldCNAB numeroInscricao=new FieldCNAB(7,19,32,14,"Numero de Inscricao da Empresa","","N");
	private FieldCNAB brancos2=new FieldCNAB(8,33,52,20,"Complemento de Registro 2","","A");
	private FieldCNAB codigoAgencia=new FieldCNAB(9,53,57,5,"Agencia Mantenedora da Conta","","N");
	private FieldCNAB brancos3=new FieldCNAB(10,58,58,1,"Complemento de Registro 3","","A");
	private FieldCNAB numeroConta=new FieldCNAB(11,59,70,12,"Numero da Conta Corrente","","N");
	private FieldCNAB brancos4=new FieldCNAB(12,71,71,1,"Complemento de Registro 4","","A");
	private FieldCNAB digitoVerificadorAgenciaConta=new FieldCNAB(12,72,72,1,"Digito Verificador da Agencia/Conta","","N");
	private FieldCNAB nomeEmpresa=new FieldCNAB(13,73,102,30,"Nome da Empresa","","A");
	private FieldCNAB nomeBanco=new FieldCNAB(14,103,132,30,"Nome do Banco","","A");
	private FieldCNAB brancos5=new FieldCNAB(15,133,142,10,"Complemento de Registro 5","","A");

	private FieldCNAB codigoArquivo=new FieldCNAB(16,143,143,1,"Codigo Remessa/Retorno","","N");
	private FieldCNAB dataDeGeracaoArquivo=new FieldCNAB(17,144,151,8,"Data de Geracao do Arquivo","","N");
	private FieldCNAB horaDeGeracaoArquivo=new FieldCNAB(18,152,157,6,"Hora de Geracao do Arquivo","","N");
	private FieldCNAB zeros=new FieldCNAB(19,158,166,9,"Complemento de Registro 6","0","N");
	private FieldCNAB densidadeArquivo=new FieldCNAB(21,167,171,5,"Densidade de Gravacao do Arquivo","0","N");
	private FieldCNAB brancos6=new FieldCNAB(22,172,240,69,"Complemento de Registro 6","","A");
	
	public HeaderArquivo240Itau()
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderArquivo240");
	}
	
	
	public HeaderArquivo240Itau(Pagador pagador, int iArquivo)
	{
		super();
		this.setSize(240);
		this.setTipo("HeaderArquivo240");
		
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());		
		this.getTipoInscricao().setContent(pagador.getTipoDeInscricao());
		this.getNumeroInscricao().setContent(pagador.getNumeroInscricao());
		this.getCodigoAgencia().setContent(pagador.getDadosBancarios().getAgencia());
		this.getNumeroConta().setContent(pagador.getDadosBancarios().getNumeroConta());
		this.getDigitoVerificadorAgenciaConta().setContent(pagador.getDadosBancarios().getDigitoAgenciaConta());
		this.getNomeEmpresa().setContent(pagador.getNome());
		this.getNomeBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getNome());
		this.getCodigoArquivo().setContent("1"); //1 - Remessa; 2 - Retorno
		this.getDataDeGeracaoArquivo().setContent(sdf.format(Calendar.getInstance().getTime()));
		this.getHoraDeGeracaoArquivo().setContent(sdft.format(Calendar.getInstance().getTime()));
		
		
		this.buildHeader();
		this.show();
		
	}
	public void buildHeader()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosHeader = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();		
		GrupoRegistrosCNAB empresa = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB nomeDoBanco = new GrupoRegistrosCNAB();		
		GrupoRegistrosCNAB arquivo = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		controle.getFieldsCNAB().put("brancos",this.brancos);
		controle.getFieldsCNAB().put("layoutDoArquivo",this.layoutDoArquivo);
		
		empresa.getFieldsCNAB().put("tipoInscricao",this.tipoInscricao);
		empresa.getFieldsCNAB().put("numeroInscricao",this.numeroInscricao);
		empresa.getFieldsCNAB().put("brancos2",this.brancos2);
		empresa.getFieldsCNAB().put("codigoAgencia",this.codigoAgencia);
		empresa.getFieldsCNAB().put("brancos3",this.brancos3);			
		empresa.getFieldsCNAB().put("numeroConta",this.numeroConta);
		empresa.getFieldsCNAB().put("brancos4",this.brancos4);		
		empresa.getFieldsCNAB().put("digitoVerificadorAgenciaConta",this.digitoVerificadorAgenciaConta);
		empresa.getFieldsCNAB().put("nomeEmpresa",this.nomeEmpresa);
		
		nomeDoBanco.getFieldsCNAB().put("nomeBanco", this.nomeBanco);
		nomeDoBanco.getFieldsCNAB().put("brancos5", this.brancos5);
		
		arquivo.getFieldsCNAB().put("codigoArquivo", this.codigoArquivo);
		arquivo.getFieldsCNAB().put("dataDeGeracaoArquivo", this.dataDeGeracaoArquivo);
		arquivo.getFieldsCNAB().put("horaDeGeracaoArquivo", this.horaDeGeracaoArquivo);
		arquivo.getFieldsCNAB().put("zeros", this.zeros);
		arquivo.getFieldsCNAB().put("densidadeArquivo", this.densidadeArquivo);
		arquivo.getFieldsCNAB().put("brancos6", this.brancos6);
		
		segmentosHeader.put("controle", controle);	
		segmentosHeader.put("empresa", empresa);
		segmentosHeader.put("nomeDoBanco", nomeDoBanco);
		segmentosHeader.put("arquivo", arquivo);		
		this.segmentosCNAB=segmentosHeader;
	}


	public static SimpleDateFormat getSdf() {
		return sdf;
	}


	public static void setSdf(SimpleDateFormat sdf) {
		HeaderArquivo240Itau.sdf = sdf;
	}


	public static SimpleDateFormat getSdft() {
		return sdft;
	}


	public static void setSdft(SimpleDateFormat sdft) {
		HeaderArquivo240Itau.sdft = sdft;
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


	public FieldCNAB getBrancos() {
		return brancos;
	}


	public void setBrancos(FieldCNAB brancos) {
		this.brancos = brancos;
	}


	public FieldCNAB getLayoutDoArquivo() {
		return layoutDoArquivo;
	}


	public void setLayoutDoArquivo(FieldCNAB layoutDoArquivo) {
		this.layoutDoArquivo = layoutDoArquivo;
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


	public FieldCNAB getNomeBanco() {
		return nomeBanco;
	}


	public void setNomeBanco(FieldCNAB nomeBanco) {
		this.nomeBanco = nomeBanco;
	}


	public FieldCNAB getBrancos5() {
		return brancos5;
	}


	public void setBrancos5(FieldCNAB brancos5) {
		this.brancos5 = brancos5;
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


	public FieldCNAB getZeros() {
		return zeros;
	}


	public void setZeros(FieldCNAB zeros) {
		this.zeros = zeros;
	}


	public FieldCNAB getDensidadeArquivo() {
		return densidadeArquivo;
	}


	public void setDensidadeArquivo(FieldCNAB densidadeArquivo) {
		this.densidadeArquivo = densidadeArquivo;
	}


	public FieldCNAB getBrancos6() {
		return brancos6;
	}


	public void setBrancos6(FieldCNAB brancos6) {
		this.brancos6 = brancos6;
	}

}

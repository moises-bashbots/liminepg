package cnab.cnab240;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PagamentoFornecedor 
{
	private int idPagamento=0;
	private String nomeArquivo="";
	private Pagador pagador = new Pagador();
	private Favorecido favorecido=new Favorecido();
	private Credito credito = new Credito();
	private Date dataVencimento = Calendar.getInstance().getTime();
	private double valorDocumento = 0;
	private double valorAbatimento = 0;
	private double valorDesconto = 0;
	private double valorMora = 0;
	private double valorMulta = 0;
	private long valorInteiro=0;
	private boolean problema=false;
	private int tentativa=1;
	private static SimpleDateFormat sdfh = new SimpleDateFormat("yyyyMMddHHmmss"); 
	private static SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd"); 
	private boolean pagamentoPIX=false;

	
	public PagamentoFornecedor()
	{
		
	}
	
	public PagamentoFornecedor(Connection connMYSQL, Connection connMSSQL, int seuNumero)
	{
		//00000000000000001492
		String stringSeuNumero=String.format("%020d",seuNumero);
		System.out.println("SeuNumero: " + seuNumero + " StringSeuNumero: "+stringSeuNumero);
		
			String query="select * from operacoes_multipag where seu_numero_multipag_inteiro="+seuNumero;
		
		Statement st = null;
		
		try {
			st=connMYSQL.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.getResultSet();
			while(rs.next())
			{
				this.nomeArquivo=rs.getString("nome_arquivo");
				Pagador pagador = new Pagador();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public PagamentoFornecedor(String nomeArquivo,Pagador pagador, Favorecido favorecido, Credito credito, Date dataVencimento, double valorDocumento, double valorAbatimento, double valorDesconto, double valorMora, double valorMulta)
	{
		this.nomeArquivo=nomeArquivo;
		this.pagador=pagador;
		this.favorecido=favorecido;
		this.credito=credito;
		this.dataVencimento=dataVencimento;
		this.valorDocumento=valorDocumento;
		this.valorAbatimento=(double)((int)(valorAbatimento*100.0))/100.0;
		this.valorDesconto=(double)((int)(valorDesconto*100.0))/100.0;
		this.valorMora=(double)((int)(valorMora*100.0))/100.0;
		this.valorMulta=(double)((int)(valorMulta*100.0))/100.0;
		this.valorInteiro=(long)(this.valorDocumento*100);
		
		if(valorDocumento>0.0)
		{
			if(valorDocumento!=this.valorDocumento || this.valorInteiro/100.0 != valorDocumento)
			{
				System.out.println("ERROR!");
			}

			System.out.println("Pagamento " + favorecido.getNome() + " " + this.valorDocumento);
//			System.out.println(valorDocumento);
//			System.out.println(this.valorDocumento);
//			System.out.println(this.valorInteiro);
		}
	}
                                                  //(nomeArquivo, fundoPagador, cedente1, credito1, Calendar.getInstance().getTime(), credito1.getValorPagamento(), 0, 0, 0, 0,(int)valor1CedenteConvertido);
	public PagamentoFornecedor(String nomeArquivo,Pagador pagador, Favorecido favorecido, Credito credito, Date dataVencimento, double valorDocumento, double valorAbatimento, double valorDesconto, double valorMora, double valorMulta, int valorInteiro)
	{
		this.nomeArquivo=nomeArquivo;
		this.pagador=pagador;
		this.favorecido=favorecido;
		this.credito=credito;
		this.dataVencimento=dataVencimento;
		this.valorDocumento=valorDocumento;
		this.valorAbatimento=(double)((int)(valorAbatimento*100.0))/100.0;
		this.valorDesconto=(double)((int)(valorDesconto*100.0))/100.0;
		this.valorMora=(double)((int)(valorMora*100.0))/100.0;
		this.valorMulta=(double)((int)(valorMulta*100.0))/100.0;
		this.valorInteiro=valorInteiro;
		
		if(valorDocumento>0.0)
		{
			if(valorDocumento!=this.valorDocumento || this.valorInteiro/100.0 != valorDocumento)
			{
				System.out.println("ERROR!");
			}

			System.out.println("Pagamento " + favorecido.getNome());
			System.out.println(valorDocumento);
			System.out.println(this.valorDocumento);
			System.out.println(this.valorInteiro);
		}
	}
	
	public PagamentoFornecedor(Pagador pagador, Favorecido favorecido, Credito credito)
	{  
		this.pagador=pagador;
		this.favorecido=favorecido;
		this.credito=credito;
//		this.nomeArquivo=pagador.getDadosBancarios().getDsname()+"CSV_"+this.favorecido.getNumeroInscricao()+"_"+sdfh.format(Calendar.getInstance().getTime())+".rem";
		if(pagador.getDadosBancarios().getDsname()!=null)
		{
			this.nomeArquivo=pagador.getDadosBancarios().getDsname()+"CSV_"+sdfh.format(Calendar.getInstance().getTime())+".rem";
		}
		else
		{
			this.nomeArquivo="CSV_"+sdfh.format(Calendar.getInstance().getTime())+".rem";
		}
		this.dataVencimento=this.credito.getDataPagamento();
		this.valorDocumento=credito.getValorPagamento();
		this.valorAbatimento=0.0;
		this.valorDesconto=0.0;
		this.valorMora=0.0;
		this.valorMulta=0.0;
		this.valorInteiro=credito.getValorInteiro();
		
		if(this.valorDocumento>0.0)
		{
			if(this.valorInteiro/100.0 != this.valorDocumento)
			{
				System.out.println("ERROR!");
			}

			System.out.println("Pagamento " + favorecido.getNome());
			System.out.println("Logradouro:" + favorecido.getEndereco().getLogradouro());
			System.out.println("Bairro:" + favorecido.getEndereco().getBairro());
			System.out.println("Cidade:" + favorecido.getEndereco().getCidade());
			System.out.println(valorDocumento);
			System.out.println(this.valorDocumento);
			System.out.println(this.valorInteiro);
		}
	}
	
	public void show()
	{
		System.out.println("------------------------");
		System.out.println("Pagamento de: " + this.pagador.getNome());
		System.out.println("NomeArquivo: " + this.nomeArquivo);
		System.out.println("SeuNumeroMultipag: " + this.credito.getSeuNumero());
		System.out.println("Pagador: " + this.pagador.getNome());
		System.out.println("Banco: " + this.pagador.getDadosBancarios().getDadosBanco().getNome());
		System.out.println(" CódigoCompe: " + this.pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
        System.out.println(" CódigoISPB: " + this.pagador.getDadosBancarios().getDadosBanco().getCodigoISPB());
		System.out.println("Agencia: " + this.pagador.getDadosBancarios().getAgencia());
		System.out.println("Conta: " + this.pagador.getDadosBancarios().getNumeroConta());
		System.out.println("DigitoConta: " + this.pagador.getDadosBancarios().getDigitoConta());
		System.out.println("Pagamento para: " + this.favorecido.getNome());
		System.out.println("CNPJ Favorecido: "+this.favorecido.getNumeroInscricao());
		System.out.println("Logradouro: " + this.favorecido.getEndereco().getLogradouro());
		System.out.println("Numero: " + this.favorecido.getEndereco().getNumero());
		System.out.println("Bairro: " + this.favorecido.getEndereco().getBairro());
		System.out.println("Cidade: " + this.favorecido.getEndereco().getCidade());
		System.out.println("UF: " + this.favorecido.getEndereco().getEstado());
		System.out.println("CEP: " + this.favorecido.getEndereco().getCep());
		if(this.favorecido.getChavePIX().length()>0)
		{
			System.out.println("Chave PIX: "+this.favorecido.getChavePIX());
			System.out.println("Tipo de chave PIX: "+this.favorecido.getTipoChavePIX());
		}
		if(this.favorecido.getNumeroBoletoDARF().length()>0)
		{
			System.out.println("Boleto a pagar: "+this.favorecido.getNumeroBoletoDARF());
			System.out.println("CodigoSize: "+this.favorecido.getNumeroBoletoDARF().length());
		}
		else 
		{
			System.out.println("Sem chave PIX");
			System.out.println("Banco: " + this.favorecido.getDadosBancarios().getDadosBanco().getNome());
			System.out.println(" CódigoCompe: " + this.favorecido.getDadosBancarios().getDadosBanco().getCodigoCompe());
			System.out.println(" CódigoISPB: " + this.favorecido.getDadosBancarios().getDadosBanco().getCodigoISPB());
			System.out.println("Agencia: " + this.favorecido.getDadosBancarios().getAgencia());
			System.out.println("Conta: " + this.favorecido.getDadosBancarios().getNumeroConta());
			System.out.println("DigitoConta: " + this.favorecido.getDadosBancarios().getDigitoConta());
			System.out.println("DataPagamento: " + sdfm.format(this.credito.getDataPagamento()));
		}
		System.out.println("Valor: " + this.valorDocumento);
		System.out.println(this.valorDocumento);
		System.out.println(this.valorInteiro);
		System.out.println("------------------------");
	}
	
	public Favorecido getFavorecido() {
		return favorecido;
	}
	public void setFavorecido(Favorecido favorecido) {
		this.favorecido = favorecido;
	}
	public Credito getCredito() {
		return credito;
	}
	public void setCredito(Credito credito) {
		this.credito = credito;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public double getValorDocumento() {
		return valorDocumento;
	}
	public void setValorDocumento(double valorDocumento) {
		this.valorDocumento = valorDocumento;
	}
	public double getValorAbatimento() {
		return valorAbatimento;
	}
	public void setValorAbatimento(double valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}
	public double getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public double getValorMora() {
		return valorMora;
	}
	public void setValorMora(double valorMora) {
		this.valorMora = valorMora;
	}
	public double getValorMulta() {
		return valorMulta;
	}
	public void setValorMulta(double valorMulta) {
		this.valorMulta = valorMulta;
	}

	public Pagador getPagador() {
		return pagador;
	}

	public void setPagador(Pagador pagador) {
		this.pagador = pagador;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public long getValorInteiro() {
		return valorInteiro;
	}

	public void setValorInteiro(long valorInteiro) {
		this.valorInteiro = valorInteiro;
	}

	public boolean isProblema() {
		return problema;
	}

	public void setProblema(boolean problema) {
		this.problema = problema;
	}

	public int getTentativa() {
		return tentativa;
	}

	public void setTentativa(int tentativa) {
		this.tentativa = tentativa;
	}

	public int getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(int idPagamento) {
		this.idPagamento = idPagamento;
	}

	public static SimpleDateFormat getSdfh() {
		return sdfh;
	}

	public static void setSdfh(SimpleDateFormat sdfh) {
		PagamentoFornecedor.sdfh = sdfh;
	}

	public static SimpleDateFormat getSdfm() {
		return sdfm;
	}

	public static void setSdfm(SimpleDateFormat sdfm) {
		PagamentoFornecedor.sdfm = sdfm;
	}

	public boolean isPagamentoPIX() {
		return this.pagamentoPIX;
	}

	public void setPagamentoPIX(boolean pagamentoPIX) {
		this.pagamentoPIX = pagamentoPIX;
	}
	
	
			
	

}

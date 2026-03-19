package cnab.cnab240;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class PagamentoTituloCobranca 
{
	private int idPagamento=0;
	private String codigoBarras="";
	private String nomeCedente="";
	private Date dataVencimento = Calendar.getInstance().getTime();
	private double valorTitulo=0.0;
	private double valorDesconto = 0;
	private double valorAbatimento = 0;	
	private double valorMora = 0;
	private double valorMulta = 0;
	private double valorDescontos=0;
	private double valorAcrescimos=0;
	private Date dataPagamento=Calendar.getInstance().getTime();
	private int quantidadeMoeda=0;
	private String referenciaSacado="";
	private Pagador pagador = new Pagador();
	private int tentativa=1;
	
	
	public PagamentoTituloCobranca()
	{
		
	}
	
	public PagamentoTituloCobranca(Connection connMYSQL, Connection connMSSQL, int seuNumero)
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
//				this.nomeArquivo=rs.getString("nome_arquivo");
				Pagador pagador = new Pagador();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public PagamentoTituloCobranca(String nomeArquivo,Pagador pagador, Favorecido favorecido, Credito credito, Date dataVencimento, double valorDocumento, double valorAbatimento, double valorDesconto, double valorMora, double valorMulta)
	{
		this.pagador=pagador;
		this.dataVencimento=dataVencimento;
//		this.valorDocumento=valorDocumento;
		this.valorAbatimento=(double)((int)(valorAbatimento*100.0))/100.0;
		this.valorDesconto=(double)((int)(valorDesconto*100.0))/100.0;
		this.valorMora=(double)((int)(valorMora*100.0))/100.0;
		this.valorMulta=(double)((int)(valorMulta*100.0))/100.0;
//		this.valorInteiro=(long)(this.valorDocumento*100);
//		
//		if(valorDocumento>0.0)
//		{
//			if(valorDocumento!=this.valorDocumento || this.valorInteiro/100.0 != valorDocumento)
//			{
//				System.out.println("ERROR!");
//			}
//
//			System.out.println("Pagamento " + favorecido.getNome() + " " + this.valorDocumento);
//			System.out.println(valorDocumento);
//			System.out.println(this.valorDocumento);
//			System.out.println(this.valorInteiro);
//		}
	}

	public PagamentoTituloCobranca(String nomeArquivo,Pagador pagador, Favorecido favorecido, Credito credito, Date dataVencimento, double valorDocumento, double valorAbatimento, double valorDesconto, double valorMora, double valorMulta, int valorInteiro)
	{
//		this.nomeArquivo=nomeArquivo;
//		this.pagador=pagador;
//		this.favorecido=favorecido;
//		this.credito=credito;
//		this.dataVencimento=dataVencimento;
//		this.valorDocumento=valorDocumento;
//		this.valorAbatimento=(double)((int)(valorAbatimento*100.0))/100.0;
//		this.valorDesconto=(double)((int)(valorDesconto*100.0))/100.0;
//		this.valorMora=(double)((int)(valorMora*100.0))/100.0;
//		this.valorMulta=(double)((int)(valorMulta*100.0))/100.0;
//		this.valorInteiro=valorInteiro;
//		
//		if(valorDocumento>0.0)
//		{
//			if(valorDocumento!=this.valorDocumento || this.valorInteiro/100.0 != valorDocumento)
//			{
//				System.out.println("ERROR!");
//			}
//
//			System.out.println("Pagamento " + favorecido.getNome());
//			System.out.println(valorDocumento);
//			System.out.println(this.valorDocumento);
//			System.out.println(this.valorInteiro);
//		}
	}
	
	
}

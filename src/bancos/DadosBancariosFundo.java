package bancos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DadosBancariosFundo {
	private int idDadosBancariosFundo=0;
	private DadosFundo dadosFundo=new DadosFundo();
	private DadosBanco dadosBanco=new DadosBanco();
	private String convenio="";
	private String agencia="";
	private String digitoAgencia="";
	private String conta="";
	private String digitoConta="";
	private String contaCompleto="";
	private boolean ativo=false;
	private boolean aprovado=false;
	private boolean branco=false;
	private String dsname="";
	
	private static SimpleDateFormat sdfs=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DadosBancariosFundo()
	{
		
	}
	
	public DadosBancariosFundo(Connection conn, int idDadosBancariosFundo)
	{
		this.idDadosBancariosFundo=idDadosBancariosFundo;
			String query="select * from dados_bancarios_fundo"
					+ " where id_dados_bancarios_fundo="+this.idDadosBancariosFundo;		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.dadosBanco=new DadosBanco(conn, rs.getInt("dados_banco_id_dados_banco"));
				this.dadosFundo=new DadosFundo(conn, rs.getInt("dados_fundo_id_dados_fundo"));
				this.convenio=rs.getString("convenio");
				this.agencia=rs.getString("agencia");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.conta=rs.getString("conta");
				this.digitoConta=rs.getString("digito_conta");
				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public DadosBancariosFundo(Connection conn, int idDadosFundo, int idDadosBanco, String agencia, String conta)
	{
		this.dadosFundo=new DadosFundo(conn, idDadosFundo);
		this.dadosBanco=new DadosBanco(conn, idDadosBanco);
		this.agencia=agencia;
		this.conta=conta;
		
			String query="select * from dados_bancarios_fundo"
					+ " where dados_fundo_id_dados_fundo="+this.dadosFundo.getIdDadosFundo()
					+ " and dados_banco_id_dados_banco="+this.dadosBanco.getIdDadosBanco()
					+ " and agencia='"+this.agencia+"'"
					+ " and conta='"+this.conta+"'";
		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.digitoConta=rs.getString("digito_conta");
				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public DadosBancariosFundo(Connection conn, String codigoBanco, String cnpj)
	{
		this.dadosBanco= new DadosBanco(conn, codigoBanco);
		this.dadosFundo= new DadosFundo(conn, cnpj);
		
			String query="select * from dados_bancarios_fundo"
					+ " where "
					+ " dados_banco_id_dados_banco="+this.dadosBanco.getIdDadosBanco()
					+ " and dados_fundo_id_dados_fundo="+this.dadosFundo.getIdDadosFundo();
		System.out.println(query);
		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancariosFundo=rs.getInt("id_dados_bancarios_fundo");
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.agencia=rs.getString("agencia");
				this.conta=rs.getString("conta");
				this.digitoConta=rs.getString("digito_conta");

				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	public DadosBancariosFundo(Connection conn, String codigoBanco, String cnpj, String agencia, String conta)
	{
		this.dadosBanco= new DadosBanco(conn, codigoBanco);
		this.dadosFundo= new DadosFundo(conn, cnpj);
		this.agencia=agencia;
		this.conta=conta;
		
			String query="select * from dados_bancarios_fundo"
					+ " where agencia='"+this.agencia+"'"
					+ " and conta='"+this.conta+"'"
					+ " and dados_banco_id_dados_banco="+this.dadosBanco.getIdDadosBanco()
					+ " and dados_fundo_id_dados_fundo="+this.dadosFundo.getIdDadosFundo();
		System.out.println(query);
		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancariosFundo=rs.getInt("id_dados_bancarios_fundo");
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.digitoConta=rs.getString("digito_conta");

				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public DadosBancariosFundo(Connection conn, String codigoBanco, String conta, String digitoConta, boolean noAgencia)
	{
		this.dadosBanco=new DadosBanco(conn, codigoBanco);
		this.conta=conta;
		this.digitoConta=digitoConta;
		
			String query="select * from dados_bancarios_fundo"
					+ " where "
					+ " dados_banco_id_dados_banco="+this.dadosBanco.getIdDadosBanco()
					+ " and conta='"+this.conta+"'"
					+ " and digito_conta='"+this.digitoConta+"'";
		System.out.println(query);
		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancariosFundo=rs.getInt("id_dados_bancarios_fundo");
				this.dadosFundo=new DadosFundo(conn, rs.getInt("dados_fundo_id_dados_fundo"));
				this.convenio=rs.getString("convenio");
				this.agencia=rs.getString("agencia");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.digitoConta=rs.getString("digito_conta");

				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}	
	
	public DadosBancariosFundo(Connection conn, String codigoBanco, String agencia, String conta)
	{
		this.dadosBanco=new DadosBanco(conn, codigoBanco);
		this.agencia=agencia;
		this.conta=conta;
		
			String query="select * from dados_bancarios_fundo"
					+ " where agencia='"+this.agencia+"'"
					+ " and dados_banco_id_dados_banco="+this.dadosBanco.getIdDadosBanco()
					+ " and conta='"+this.conta+"'";
		System.out.println(query);
		
		Statement st=null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancariosFundo=rs.getInt("id_dados_bancarios_fundo");
				this.dadosFundo=new DadosFundo(conn, rs.getInt("dados_fundo_id_dados_fundo"));
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.digitoConta=rs.getString("digito_conta");

				if(rs.getInt("ativo")>0)
				{
					this.ativo=true;
				}
				if(rs.getInt("aprovado")>0)
				{
					this.aprovado=true;
				}
				if(rs.getInt("branco")>0)
				{
					this.branco=true;
				}
				this.conta=rs.getString("conta");
				this.dsname=rs.getString("dsname");
				this.contaCompleto=this.conta+this.digitoConta;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static boolean updateUltimaAtualizacaoExtrato(Connection conn, DadosBancariosFundo dadosBancariosFundo, Date dataAtualizacao, int idUltimaMovimentacao)
	{
		String sql = "update dados_bancarios_fundo"
					+ " set ultima_atualizacao_extrato='"+sdfs.format(dataAtualizacao)+"'"
					+ ", id_ultima_movimentacao="+idUltimaMovimentacao
					+ " where id_dados_bancarios_fundo="+dadosBancariosFundo.getIdDadosBancariosFundo();
		System.out.println(sql);
		Statement st=null;

		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void show()
	{
		System.out.println(this.getDadosBanco().getNomeBanco());
		System.out.println(this.getDadosFundo().getNome());
		System.out.println("AG: "+this.agencia);
		System.out.println("CC: "+this.conta);
	}
	

	public DadosFundo getDadosFundo() {
		return dadosFundo;
	}

	public void setDadosFundo(DadosFundo dadosFundo) {
		this.dadosFundo = dadosFundo;
	}

	public DadosBanco getDadosBanco() {
		return dadosBanco;
	}

	public void setDadosBanco(DadosBanco dadosBanco) {
		this.dadosBanco = dadosBanco;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getContaCompleto() {
		return contaCompleto;
	}

	public void setContaCompleto(String contaCompleto) {
		this.contaCompleto = contaCompleto;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isAprovado() {
		return aprovado;
	}

	public void setAprovado(boolean aprovado) {
		this.aprovado = aprovado;
	}

	public boolean isBranco() {
		return branco;
	}

	public void setBranco(boolean branco) {
		this.branco = branco;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public int getIdDadosBancariosFundo() {
		return idDadosBancariosFundo;
	}

	public void setIdDadosBancariosFundo(int idDadosBancariosFundo) {
		this.idDadosBancariosFundo = idDadosBancariosFundo;
	}

}

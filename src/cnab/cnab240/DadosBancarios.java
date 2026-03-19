package cnab.cnab240;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DadosBancarios 
{
	private int idDadosBancarios=0;
	private String codigoBanco="";
	private String nomeBanco="";
	private String convenio="";
	private String agencia="";
	private String digitoAgencia="";
	private String numeroConta="";
	private String digitoConta="";
	private String digitoAgenciaConta="";
	private String dsname="";
	private boolean ativo=false;
	private boolean aprovado=false;
	private boolean branco=false;
	private boolean pagamentoPIX=false;
	private DadosBanco dadosBanco=new DadosBanco();
	private DadosFundo dadosFundo= new DadosFundo();
	
	public DadosBancarios()
	{
		
	}

	public DadosBancarios(String codigoBanco, String nomeBanco, String convenio, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta)	
	{
		this.codigoBanco=codigoBanco;
		this.nomeBanco=nomeBanco;
		this.convenio=convenio;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;		
	}
	
	public DadosBancarios(String codigoBanco, String nomeBanco, String convenio, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta, int ativo, int aprovado, int branco)	
	{
		this.codigoBanco=codigoBanco;
		this.nomeBanco=nomeBanco;
		this.convenio=convenio;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;	
		if(ativo==1)
		{
			this.ativo=true;
		}
		if(aprovado==1)
		{
			this.aprovado=true;
		}
		if(branco==1)
		{
			this.branco=true;
		}
	}
	
	public DadosBancarios(String codigoBanco, String nomeBanco, String convenio, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta, String dsname, int ativo, int aprovado, int branco)	
	{
		this.codigoBanco=codigoBanco;
		this.nomeBanco=nomeBanco;
		this.convenio=convenio;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;
		this.dsname=dsname;
		if(ativo==1)
		{
			this.ativo=true;
		}
		if(aprovado==1)
		{
			this.aprovado=true;
		}
		if(branco==1)
		{
			this.branco=true;
		}
	}

	public DadosBancarios(int idDadosBancarios, DadosBanco dadosBanco, DadosFundo dadosFundo, String convenio, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta, String dsname, int ativo, int aprovado, int branco, int pix)	
	{
		this.idDadosBancarios=idDadosBancarios;
		this.dadosBanco=dadosBanco;
		this.dadosFundo=dadosFundo;
		this.convenio=convenio;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;
		this.dsname=dsname;
		if(ativo==1)
		{
			this.ativo=true;
		}
		if(aprovado==1)
		{
			this.aprovado=true;
		}
		if(branco==1)
		{
			this.branco=true;
		}
		if(pix==1)
		{
			this.pagamentoPIX=true;
		}
	
	}
	
	
	public DadosBancarios(String codigoBanco, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta)	
	{
		this.codigoBanco=codigoBanco;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;
		
	}

	public DadosBancarios(Connection conn, String codigoBanco, String agencia, String digitoAgencia, String numeroConta, String digitoConta, String digitoAgenciaConta)	
	{
		this.dadosBanco=new DadosBanco(conn, codigoBanco);
		this.codigoBanco=codigoBanco;
		this.agencia=agencia;
		this.digitoAgencia=digitoAgencia;
		this.numeroConta=numeroConta;
		this.digitoConta=digitoConta;
		this.digitoAgenciaConta=digitoAgenciaConta;
		
	}

	public DadosBancarios(Connection conn, int idDadosBancarios)	
	{
		this.idDadosBancarios=idDadosBancarios;
			String query="select * from dados_bancarios_fundo"
				+ " where"
				+ " id_dados_bancarios_fundo="+this.idDadosBancarios;
		
		System.out.println(query);
		this.digitoAgenciaConta="";
		Statement st = null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.dadosBanco=new DadosBanco(conn,rs.getInt("dados_banco_id_dados_banco"));
				this.dadosFundo=new DadosFundo(conn,rs.getInt("dados_fundo_id_dados_fundo"));
				this.convenio=rs.getString("convenio");
				this.agencia=rs.getString("agencia");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.numeroConta=rs.getString("conta");
				this.digitoConta=rs.getString("digito_conta");
				int ativo=rs.getInt("ativo");
				int aprovado=rs.getInt("aprovado");
				int branco=rs.getInt("branco");
				int pix=rs.getInt("pagamentos_pix");
				this.dsname=rs.getString("dsname");
				if(ativo==1)
				{
					this.ativo=true;
				}
				if(aprovado==1)
				{
					this.aprovado=true;
				}
				if(branco==1)
				{
					this.branco=true;
				}
				if(pix==1)
				{
					this.pagamentoPIX=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DadosBancarios(Connection conn, String codigoBancoTeste, String agenciaTeste, String numeroContaTeste)	
	{
		this.dadosBanco=new DadosBanco(conn, codigoBancoTeste);
		this.agencia=agenciaTeste;
		this.digitoAgencia="";
		numeroContaTeste=numeroContaTeste.replaceAll("-", "");
		this.numeroConta=numeroContaTeste.substring(0,numeroContaTeste.length()-1);
		this.digitoConta=numeroContaTeste.substring(numeroContaTeste.length()-1);
		
		this.codigoBanco=this.dadosBanco.getCodigoCompe();
		this.nomeBanco=this.dadosBanco.getNome();
			String query="select * from dados_bancarios_fundo"
				+ " where"
				+ " codigo_banco=" + this.dadosBanco.getCodigoCompe()
				+ " and agencia='"+this.agencia+"'"
				+ " and conta='"+this.numeroConta+"'"
				+ " and digito_conta='"+this.digitoConta+"'"
				;
		
		System.out.println(query);
		this.digitoAgenciaConta="";
		Statement st = null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancarios=rs.getInt("id_dados_bancarios_fundo");
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.dsname=rs.getString("dsname");
				int ativo=rs.getInt("ativo");
				int aprovado=rs.getInt("aprovado");
				int branco=rs.getInt("branco");
				int pix=rs.getInt("pagamentos_pix");
				if(ativo==1)
				{
					this.ativo=true;
				}
				if(aprovado==1)
				{
					this.aprovado=true;
				}
				if(branco==1)
				{
					this.branco=true;
				}
				if(pix==1)
				{
					this.pagamentoPIX=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public DadosBancarios(Connection conn, DadosFundo dadosFundo, String codigoBanco, String agencia, String numeroConta)	
	{
		this.dadosFundo=dadosFundo;
		this.dadosBanco=new DadosBanco(conn, codigoBanco);
		this.agencia=agencia;
		this.digitoAgencia="";
		numeroConta=numeroConta.replaceAll("-", "");
		this.numeroConta=numeroConta.substring(0,numeroConta.length()-1);
		this.digitoConta=numeroConta.substring(numeroConta.length()-1);
		this.digitoAgenciaConta="";
		this.codigoBanco=this.dadosBanco.getCodigoCompe();
		this.nomeBanco=this.dadosBanco.getNome();
		
			String query="select * from dados_bancarios_fundo"
				+ " where"
				+ " cnpj_fundo='" + this.dadosFundo.getCnpj()+"'"
				+ " and codigo_banco=" + this.dadosBanco.getCodigoCompe()
				+ " and agencia='"+this.agencia+"'"
				+ " and conta='"+this.numeroConta+"'"
				+ " and digito_conta='"+this.digitoConta+"'"
				;
		System.out.println(query);
		this.digitoAgenciaConta="";
		Statement st = null;
		
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBancarios=rs.getInt("id_dados_bancarios_fundo");
				this.convenio=rs.getString("convenio");
				this.digitoAgencia=rs.getString("digito_agencia");
				this.dsname=rs.getString("dsname");
				int ativo=rs.getInt("ativo");
				int aprovado=rs.getInt("aprovado");
				int branco=rs.getInt("branco");
				int pix=rs.getInt("pagamentos_pix");
				if(ativo==1)
				{
					this.ativo=true;
				}
				if(aprovado==1)
				{
					this.aprovado=true;
				}
				if(branco==1)
				{
					this.branco=true;
				}
				if(pix==1)
				{
					this.pagamentoPIX=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void show()
	{
		System.out.println(this.codigoBanco);
		System.out.println(this.agencia);
		System.out.println(this.digitoAgencia);
		System.out.println(this.numeroConta);
		System.out.println(this.digitoConta);
		System.out.println(this.digitoAgenciaConta);
		System.out.println(this.dsname);
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

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getDigitoAgenciaConta() {
		return digitoAgenciaConta;
	}

	public void setDigitoAgenciaConta(String digitoAgenciaConta) {
		this.digitoAgenciaConta = digitoAgenciaConta;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
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

	public DadosBanco getDadosBanco() {
		return dadosBanco;
	}

	public void setDadosBanco(DadosBanco dadosBanco) {
		this.dadosBanco = dadosBanco;
	}

	public DadosFundo getDadosFundo() {
		return dadosFundo;
	}

	public void setDadosFundo(DadosFundo dadosFundo) {
		this.dadosFundo = dadosFundo;
	}

	public int getIdDadosBancarios() {
		return idDadosBancarios;
	}

	public void setIdDadosBancarios(int idDadosBancarios) {
		this.idDadosBancarios = idDadosBancarios;
	}

	public boolean isPagamentoPIX() {
		return pagamentoPIX;
	}

	public void setPagamentoPIX(boolean pagamentoPIX) {
		this.pagamentoPIX = pagamentoPIX;
	}

}

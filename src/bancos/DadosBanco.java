package bancos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DadosBanco {
	private int idDadosBanco=0;
	private String nomeBanco="";
	private String codigoCompe="";
	private String codigoIspb="";
	
	public DadosBanco()
	{
		
	}
	
	public DadosBanco(Connection conn, String codigo)
	{
		this.codigoCompe=codigo;
			String query="select * from dados_banco where codigo_compe = '"+this.codigoCompe+"'";
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBanco=rs.getInt("id_dados_banco");
				this.nomeBanco=rs.getString("nome_banco");
				this.codigoIspb=rs.getString("codigo_ispb");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DadosBanco(Connection conn, int idDadosBanco)
	{
		this.idDadosBanco=idDadosBanco;
			String query="select * from dados_banco where id_dados_banco = "+this.idDadosBanco;
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.codigoCompe=rs.getString("codigo_compe");
				this.codigoIspb=rs.getString("codigo_ispb");
				this.nomeBanco=rs.getString("nome_banco");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getIdDadosBanco() {
		return idDadosBanco;
	}
	public void setIdDadosBanco(int idDadosBanco) {
		this.idDadosBanco = idDadosBanco;
	}

	public String getCodigoCompe() {
		return this.codigoCompe;
	}

	public void setCodigoCompe(String codigoCompe) {
		this.codigoCompe = codigoCompe;
	}

	public String getCodigoIspb() {
		return this.codigoIspb;
	}

	public void setCodigoIspb(String codigoIspb) {
		this.codigoIspb = codigoIspb;
	}

	public String getNomeBanco() {
		return this.nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
	
	
}

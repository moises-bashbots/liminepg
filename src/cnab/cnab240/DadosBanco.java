package cnab.cnab240;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DadosBanco {
	private int idDadosBanco=0;
	private String nome="";
	private String codigoCompe="";
	private String codigoISPB="";
	public DadosBanco()
	{
		
	}
	
	public DadosBanco(Connection conn, int idDadosBanco)
	{
		this.idDadosBanco=idDadosBanco;
			String query = "select * from dados_banco where id_dados_banco="+this.idDadosBanco;

		Statement st=null;
		
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				this.nome=rs.getString("nome_banco");
				this.codigoCompe=rs.getString("codigo_compe");
				this.codigoISPB=rs.getString("codigo_ispb");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DadosBanco(Connection conn, String codigo)
	{
		this.codigoCompe=codigo;
			String query = "select * from dados_banco where codigo_compe='"+this.codigoCompe+"'";

		System.out.println(query);
		Statement st=null;
		
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosBanco=rs.getInt("id_dados_banco");
				this.nome=rs.getString("nome_banco");
				this.codigoCompe=rs.getString("codigo_compe");
				this.codigoISPB=rs.getString("codigo_ispb");
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigoCompe() {
		return codigoCompe;
	}

	public void setCodigoCompe(String codigoCompe) {
		this.codigoCompe = codigoCompe;
	}

	public String getCodigoISPB() {
		return codigoISPB;
	}

	public void setCodigoISPB(String codigoISPB) {
		this.codigoISPB = codigoISPB;
	}
	
	

}

package cnab;

import java.util.HashMap;

public class GrupoRegistrosCNAB {
	private HashMap<String,FieldCNAB> fieldsCNAB = new HashMap<String,FieldCNAB>();
	private String nome="";
	
	public GrupoRegistrosCNAB()
	{
		
	}
	
	public void extractFields(String line)
	{
		for(String key:fieldsCNAB.keySet())
		{
			fieldsCNAB.get(key).extract(line);
		}
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public HashMap<String, FieldCNAB> getFieldsCNAB() {
		return fieldsCNAB;
	}

	public void setFieldsCNAB(HashMap<String, FieldCNAB> fieldsCNAB) {
		this.fieldsCNAB = fieldsCNAB;
	}
	
	
}

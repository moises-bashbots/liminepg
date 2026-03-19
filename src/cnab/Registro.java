package cnab;

import java.util.HashMap;

public class Registro {
	protected HashMap<String, Field> fieldsCNAB = new HashMap<String,Field>();
	private String tipo="";
	private int size=0;	
	
	public Registro()
	{
		
	}
	
	public void extract(String line)
	{	
		for(String keyField:this.fieldsCNAB.keySet())
		{
			this.fieldsCNAB.get(keyField).extract(line);
		}

	}
	
	public String lineRegister()
	{
		String line=new String(new char[this.size]).replace('\0',' ');
//		System.out.println("'"+line+"'");
		for(String keyField:this.fieldsCNAB.keySet())
		{				
			System.out.println("\nField: " + keyField + " " + this.fieldsCNAB.get(keyField).getCharset() + " from " + this.getTipo());
			line = fieldsCNAB.get(keyField).replaceByChar(line);
		}

		return line;
	}
	
	public String lineRegisterSilent()
	{
		String line=new String(new char[this.size]).replace('\0',' ');
//		System.out.println("'"+line+"'");
		for(String keyField:this.fieldsCNAB.keySet())
		{				
			System.out.println("\nField: " + keyField + " " + this.fieldsCNAB.get(keyField).getCharset() + " from " + this.getTipo());
			line = fieldsCNAB.get(keyField).replaceByCharSilent(line);
		}

		return line;
	}
	

	public String lineRegisterV()
	{
		String line=new String(new char[this.size]).replace('\0',' ');
		System.out.println("'"+line+"'");

		for(String keyField:this.fieldsCNAB.keySet())
		{				
			System.out.println("\nField: " + keyField + " " + this.fieldsCNAB.get(keyField).getCharset() + " from " + this.getTipo());
			line = this.fieldsCNAB.get(keyField).replaceByChar(line);
		}
	
		return line;
	}
	
	
	public void show()
	{
		System.out.println("Tipo de Registro: " + this.getTipo());

		for(String keyField:this.fieldsCNAB.keySet())
		{				
			Field field =this.fieldsCNAB.get(keyField);
			System.out.println("Field: " + keyField + " Begin " + field.getBegin() + " End " + field.getEnd() + "\t'" + field.getContent()+"'");
			
		}
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public HashMap<String, Field> getFieldsCNAB() {
		return fieldsCNAB;
	}

	public void setFieldsCNAB(HashMap<String, Field> fieldsCNAB) {
		this.fieldsCNAB = fieldsCNAB;
	}
	

}

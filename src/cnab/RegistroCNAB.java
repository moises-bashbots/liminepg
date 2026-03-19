package cnab;

import java.util.HashMap;

public class RegistroCNAB {
	protected HashMap<String, GrupoRegistrosCNAB> segmentosCNAB = new HashMap<String,GrupoRegistrosCNAB>();
	private String tipo="";
	private int size=0;	
	
	public RegistroCNAB()
	{
		
	}
	
	public void extract(String line)
	{
		if(segmentosCNAB.get("controle")!=null)
		{
			String tipoDeRegistro=segmentosCNAB.get("controle").getFieldsCNAB().get("registro").getContent();
			if(tipoDeRegistro.equals(segmentosCNAB.get("controle").getFieldsCNAB().get("registro").view(line)))
			{
				for(String keySegmento:segmentosCNAB.keySet())
				{
					for(String keyField:segmentosCNAB.get(keySegmento).getFieldsCNAB().keySet())
					{
						segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField).extract(line);
					}
				}
			}
		}
	}
	
	public String lineRegister()
	{
		String line=new String(new char[this.size]).replace('\0',' ');
//		System.out.println("'"+line+"'");
		for(String keySegmento:segmentosCNAB.keySet())
		{
//			System.out.println("Segmento: " + keySegmento);
			for(String keyField:segmentosCNAB.get(keySegmento).getFieldsCNAB().keySet())
			{				
				System.out.println("\nField: " + keyField + " " + segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField).getCharset() + " from " + this.getTipo());
				line = segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField).replaceByChar(line);
			}
		}
		return line;
	}

	public String lineRegisterV()
	{
		String line=new String(new char[this.size]).replace('\0',' ');
		System.out.println("'"+line+"'");
		for(String keySegmento:segmentosCNAB.keySet())
		{
			System.out.println("Segmento: " + keySegmento);
			for(String keyField:segmentosCNAB.get(keySegmento).getFieldsCNAB().keySet())
			{				
				System.out.println("\nField: " + keyField + " " + segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField).getCharset() + " from " + this.getTipo());
				line = segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField).replaceByChar(line);
			}
		}
		return line;
	}
	
	
	public void show()
	{
		System.out.println("Tipo de Registro: " + this.getTipo());
		System.out.println("Segmentos: " + this.getSegmentosCNAB().size());
		for(String keySegmento:segmentosCNAB.keySet())
		{
			System.out.println("Segmento: " + keySegmento);
			for(String keyField:segmentosCNAB.get(keySegmento).getFieldsCNAB().keySet())
			{				
				FieldCNAB field =segmentosCNAB.get(keySegmento).getFieldsCNAB().get(keyField);
				System.out.println("Field: " + keyField + " Begin " + field.getBegin() + " End " + field.getEnd() + "\t'" + field.getContent()+"'");
				
			}
		}
		
	}
	
	public HashMap<String, GrupoRegistrosCNAB> getSegmentosCNAB() {
		return segmentosCNAB;
	}
	public void setSegmentosCNAB(HashMap<String, GrupoRegistrosCNAB> segmentosCNAB) {
		this.segmentosCNAB = segmentosCNAB;
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
	

}

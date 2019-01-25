package nl.sysqa.webservice;

public class Hello {
	public String helloName(String name) throws Exception{
		String question = "?";
		if(name.equals(question)) {
			throw new Exception();
		}
		else {
			if (name.equals("Miranda")){
				return name+" is inmiddels al lang geen lid van de kennisgroep meer";
			}
			else{
				return "Hello there "+name;
			}
		}
	}
}
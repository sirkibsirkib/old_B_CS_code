package assignment2;

public class Table {
	private ListInterface<KVPairInterface<SetInterface<NaturalNumberInterface>>> vars;
	
	public Table(){
		vars = new List<>();
	}

	public void assign(IdentifierInterface i, SetInterface<NaturalNumberInterface> v) {
		KVPairInterface<SetInterface<NaturalNumberInterface>> x = new KVPair<>(i.clone(), v.clone());
		if(vars.find(x))
			vars.remove();
		vars.insert(x);
	}
	
	public SetInterface<NaturalNumberInterface> tryRetrieve(IdentifierInterface i){
		KVPairInterface<SetInterface<NaturalNumberInterface>> x = new KVPair<>(i);
		if(vars.find(x))
			return vars.retrieve().getValue();
		return null;
	}
}

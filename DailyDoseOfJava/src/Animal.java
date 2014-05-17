import java.util.ArrayList;

public class Animal {
	public void isA(){
		System.out.println("is an Animal");
	}
	
	public static void main(String[] args){
		ArrayList<Animal> animals = new ArrayList<Animal>();
		animals.add(new Hippo());
		animals.add(new Lion());
		animals.add(new Tiger());
		animals.add(new Cat());
		animals.add(new Wolf());
		animals.add(new Dog());
		
		animals.get(0).isA();
		animals.get(1).isA();
		animals.get(2).isA();
		animals.get(3).isA();
		animals.get(4).isA();
		animals.get(5).isA();
	}
}

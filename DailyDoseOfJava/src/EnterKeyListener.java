//This class doesn't run. It is only example code to explain how to handle the enter key being pressed,
//checking to see if a class exists, and checking to see if it is runnable
public class EnterKeyListener {
	//when the enter key is pressed after typing in the text field, perform this method
    userTextField.setOnAction((event) -> {            
        String userText = userTextField.getText();  //text that the user input

        //check to see if the item is already in the list
        boolean alreadyInList = false;
        if (runnableItems.contains(userText)) {
            alreadyInList = true;
        }
        
        //if the item isn't already in the list, check to see if the class exists
        boolean classExists = false;
        Class classChosen = null;
        if (!alreadyInList) {
            try {
                classChosen = Class.forName("boldness." + userText);
                classExists = true;
                //Object ob = exampleClass.newInstance();
            } catch(ClassNotFoundException ex) {
                //Class doesn't exist
                System.out.println("Class not found");
            } 
        }
        
        //if the class does exist and if it isn't already in the list, then check to see if it is runnable
        boolean classIsRunnable = false;
        if (classExists) {
            try {
                Runnable r = (Runnable) classChosen.newInstance();
                classIsRunnable = true;
            } catch (Exception ex) {
                System.out.println("Class not runnable");
            }
        }
        
        //if the class is not in the list, and the class exists, and it is runnable, then add it to the list
        if (classIsRunnable) {
            //...
        }
        
        //class was already in the list, or the class didn't exist, or the class wasn't runnable
        else {
        	//...
        }
    });
}

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DynamicPageCreation extends Activity {
    
	int count = 0;
	String phrase = "";
	String checked = "false";
	
	String state;  //used for checking the environmental state
	boolean canW, canR;  //can read and can write
	
	File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);  //path where files are stored
	File filename;  //name of file when stored
	
	boolean fileNameFound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fileNameFound = false;  //used in comparing the user's title with the filenames in the list 
		
		//check to see if we can read and write
		checkState();
				
		//main layout
		final LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout header = new LinearLayout(this);
		header.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.LinearLayout.LayoutParams paramsHeader = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
		paramsHeader.setMargins(0,10,0,10);  //margins (left,top,right,bottom)
		header.setLayoutParams(paramsHeader);
		
		mainLayout.addView(header);
		//Header
		final TextView tvNewList = new TextView(this);
		tvNewList.setText("           New List");
		
		tvNewList.setTextSize(40);
		tvNewList.setTypeface(null, Typeface.BOLD);
		android.widget.LinearLayout.LayoutParams paramsHeaderRow = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
		paramsHeaderRow.weight = 1.0f;
		tvNewList.setLayoutParams(paramsHeaderRow);
		tvNewList.setGravity(Gravity.CENTER_HORIZONTAL);
		header.addView(tvNewList);  //add the header to the main layout
		
		Button cancelEdit = new Button(this);
		cancelEdit.setText("Cancel Edit");
		android.widget.LinearLayout.LayoutParams paramsCancel = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
		paramsCancel.gravity = Gravity.CENTER_HORIZONTAL;
		cancelEdit.setLayoutParams(paramsCancel);
		header.addView(cancelEdit);
		
		//row for title (text view and edit text)
		final LinearLayout layoutTitle = new LinearLayout(this);
		layoutTitle.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.LinearLayout.LayoutParams paramsTitleLayout = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
		paramsTitleLayout.setMargins(0,0,0,10);  //margins (left,top,right,bottom)
		layoutTitle.setLayoutParams(paramsTitleLayout);
		
		//title text view
		TextView tvTitle = new TextView(this);
		tvTitle.setText("Title:");
		tvTitle.setTextSize(30);
		layoutTitle.addView(tvTitle);  //add the title text view to the title row

		//title text box
		final EditText etTitle = new EditText(this);
		etTitle.setId(count);  //id = 0
		count = count + 1;
		
		//set the weight of the title text box so that it fills in the rest of the screen width
		android.widget.LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		etTitle.setLayoutParams(paramsTitle);
		etTitle.setTextSize(30);
		layoutTitle.addView(etTitle);  //add the title text box to the title row
		
		mainLayout.addView(layoutTitle);  //add title row to the main layout
		
		//scroll view for all of the item text boxes
		final ScrollView sv = new ScrollView(this);
		android.widget.LinearLayout.LayoutParams paramsScrollView = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		sv.setLayoutParams(paramsScrollView);
		mainLayout.addView(sv);  //add the scroll view to the main layout
		
		//layout for all of the user's items
		final LinearLayout itemsLayout = new LinearLayout(this);
		itemsLayout.setOrientation(LinearLayout.VERTICAL);
		sv.addView(itemsLayout);  //add the user's items to the scroll view layout
		
		
		
		//row for delete, plus, and finish buttons
		final LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainLayout.addView(buttonsLayout);	
		
		//set the parameters of the buttons so that they are all the same width and have a space between them and the last row of text boxes
		android.widget.LinearLayout.LayoutParams paramsButtons = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		paramsButtons.setMargins(0, 20, 0, 0);
		
		//delete button
		Button bDelete = new Button(this);
		bDelete.setText("Delete");
		bDelete.setLayoutParams(paramsButtons);
		
		//plus button
		final Button bPlus = new Button(this);
		bPlus.setText("+");
		bPlus.setId(9998);
		bPlus.setLayoutParams(paramsButtons);
		
		//finish button
		Button bFinish = new Button(this);
		bFinish.setText("Finish");
		bFinish.setLayoutParams(paramsButtons);
		
		//create the dialog to prompt the user to confirm deleteion
		final Builder alertDialog = new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Are you sure you want to delete this list?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        { 
	            // continue with delete
	        	finish();  //closes the activity (no data has been saved so no need to do any deletions)
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        { 
	            // do nothing
	        }
	     });
		
		//when the delete button is pressed, check if this is a new list or editing the list
		//if new list, then close page without saving or delete anything
		//if edit list, then delete the current file and remove the name of the list from myfiles.txt
		bDelete.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				alertDialog.show(); //prompt the user to confirm deletion
			}
		});
		
		//when the plus button is pressed, add a new text box
		bPlus.setOnClickListener(new View.OnClickListener() 
		{
			EditText etNew;
			LinearLayout layoutNewRow;  //horizontal layout for each new row (contains text box and all editing buttons)
			Button up, down, delete;
			
			@Override
			public void onClick(View v)   
			{
				layoutNewRow = new LinearLayout(MainActivity.this);
				layoutNewRow.setOrientation(LinearLayout.HORIZONTAL);
				layoutNewRow.setId(count+4000);
				
				//text box
				android.widget.LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(
		                LayoutParams.MATCH_PARENT,
		                LayoutParams.WRAP_CONTENT, 1.0f);  //fills in the rest of the the screen width 
				etNew = new EditText(MainActivity.this);
				etNew.setId(count);  // id >= 1
				etNew.setLayoutParams(paramsItem);
				etNew.setFocusableInTouchMode(true);
				etNew.requestFocus();
				etNew.addTextChangedListener(new TextWatcher() 
				{
			        @Override
			        public void onTextChanged(CharSequence s, int start, int before, int count) {}
			        @Override
			        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			    
			        //if the user presses enter, then perform a bPlus action
			        @Override
			        public void afterTextChanged(Editable s) 
			        {
			        	if (s.length() > 0)
			        	{
				            if (s.charAt(s.length() - 1) == '\n') 
				            {
				            	String len = Integer.toString(s.length());
				            	tvNewList.setText(len);
				            	s.delete(s.length()-1, s.length());
				            	Button btn = (Button)findViewById(9998);
				                btn.performClick();
				            }
			        	}
			        }
			    });
				layoutNewRow.addView(etNew);
				//etNew.requestFocus(); //the new row gets the focus so that the user doesn't have to click on the new row to start typing 
				
				//up button
				up = new Button(MainActivity.this);
				up.setText("up");
				up.setId(count+1000);
				up.setOnClickListener(new View.OnClickListener() 
				{	
					//swap the text from the current text box with the text of the text box above the current text box
					TextView boxAbove, currentBox;
					
					@Override
					public void onClick(View v)   
					{
						int textId = v.getId()-1000;
						if (textId > 1) //if this item is in the first row, then don't shift up --- otherwise swap the text with the text of the box above
						{
							
							boxAbove = (TextView)findViewById(textId-1);
							currentBox = (TextView)findViewById(textId);
							String textAbove = boxAbove.getText().toString();
							boxAbove.setText(currentBox.getText());
							currentBox.setText(textAbove);
						}
					}
				});
				layoutNewRow.addView(up);  //add the up button to the row
				
				//down button
				down = new Button(MainActivity.this);
				down.setText("down");
				down.setId(count+2000);
				down.setOnClickListener(new View.OnClickListener() 
				{	
					//swap the text from the current text box with the text of the text box below the current text box
					TextView boxBelow, currentBox;
					
					@Override
					public void onClick(View v)   
					{
						int textId = v.getId()-2000;
						if (textId != count-1) //if this item is in the first row, then don't shift up --- otherwise swap the text with the text of the box above
						{
							boxBelow = (TextView)findViewById(textId+1);
							currentBox = (TextView)findViewById(textId);
							String textBelow = boxBelow.getText().toString();
							boxBelow.setText(currentBox.getText());
							currentBox.setText(textBelow);
						}
					}
				});
				layoutNewRow.addView(down);  //add the down button to the row
				
				//delete button
				delete = new Button(MainActivity.this);
				delete.setText("x");
				delete.setId(count+3000);
				delete.setOnClickListener(new View.OnClickListener() 
				{	
					//perform down button click until the current text box is at the bottom of the list
					//delete the last text box
					//decrement count by 1
					Button myDown;
					
					@Override
					public void onClick(View v)   
					{
						int downId = v.getId()-1000;
						int textId = downId - 2000;
						int layoutId = count-1+4000;
						
						//perform the down button method until the current text box is at the bottom
						for (int i = textId; i < count-1; i++)
						{
							myDown = (Button)findViewById(downId);
							myDown.performClick();
							downId++;
						}
						
						//delete the last row
						LinearLayout layout = (LinearLayout)findViewById(layoutId);
						itemsLayout.removeView(layout);
						count--;
					}
				});
				layoutNewRow.addView(delete);  //add the delete button to the row
				
				itemsLayout.addView(layoutNewRow);  //add the row to the page
				count = count + 1;	//increment count for the next id
			}
		});
		
		//when the finish button is pressed, write the data from all of the text boxes to the file
		bFinish.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				String doc = "";
				for (int i = 0; i < count; i++)
				{
					EditText txt = (EditText)findViewById(i);
				}
				EditText title = (EditText)findViewById(0);
				String userTitle = title.getText().toString();
				String userTitleWithExtension = userTitle + ".txt";
				
				filename = new File(path, userTitleWithExtension);	
				
				checkState();  //update canW and canR
				
				//read all of the text boxes and save it into a string
				for (int i = 1; i < count; i++)
				{
					EditText txt = (EditText)findViewById(i);
					doc = doc + txt.getText().toString() + "\n";
					doc = doc + checked + "\n";
				}
				
				//if we can write
				if (canW == true)
				{
					//write the users file
					try {
						OutputStream os = new FileOutputStream(filename);
						//String doc = et1.getText().toString() + "\n" + et2.getText().toString();
						byte[] data = doc.getBytes();
						os.write(data);
						os.close();
					}
					catch (FileNotFoundException e)	{e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
					
					//We need to read in the file and compare the user's title with the filenames 
					//already listed. If the user's title is a new one, then we need to add the 
					//new name to the file
					filename = new File(path,"myfiles.txt");  //location of the file containing the list of lists
					FileInputStream is = null;
					BufferedReader br = null;
					try {
						is = new FileInputStream(filename);
						DataInputStream in = new DataInputStream(is);
						br = new BufferedReader(new InputStreamReader(in));
					} catch (FileNotFoundException e) {	e.printStackTrace();}
					
					//read the file and compare the user's title with the filenames line by line
					try {
						String line = "";
						
						//while not the end of the file and the name hasn't been found
						while ((line = br.readLine()) != null && !fileNameFound)
						{
							if (line.equals(userTitle))  //if current line text == user's title
							{
								fileNameFound = true;
							}
						}
					} catch (IOException e) {e.printStackTrace();}
					
					//close the file
					try {is.close();} catch (IOException e) {e.printStackTrace();}
					
					
					//if the filename was not found in the list of files, then add the name to the end of the file
					if (!fileNameFound)
					{
						try {
							OutputStream os = new BufferedOutputStream(new FileOutputStream(filename, true));  //true = append
							//String doc = userTitle.toString() + "\n";  //new filename to be added
							doc = userTitle.toString() + "\n";  //new filename to be added
							byte[] dataOut = doc.getBytes();
							os.write(dataOut); //add new file name to the end of the file
							os.close();
						}
						catch (FileNotFoundException e1) {e1.printStackTrace();} catch (IOException e1) {e1.printStackTrace();}
					}
				}
				
				// redirect the user to the view list page
				Intent openList = new Intent("com.herd.checklist.VIEWLIST");
				openList.putExtra("title", userTitle);  //pass the name of the list created to the view list page
				startActivity(openList);
				finish();  //closes the activity so that if the user presses the back button, the user will go to home instead of the new list page
			}
		});
		
		//add the buttons to the buttons layout
		buttonsLayout.addView(bDelete);
		buttonsLayout.addView(bPlus);
		buttonsLayout.addView(bFinish);

		//set the view of the page to be the main layout
		this.setContentView(mainLayout);
	}

	//check to make sure we are able to read and write
	private void checkState() 
	{
		state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			canW = canR = true;
		}
		else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			canW = false;
			canR = true;
		}
		else
		{
			canW = canR = false;
		}
	}

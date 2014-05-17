import java.awt.*;

import javax.swing.*;
import javax.sound.midi.*;

import java.util.*;
import java.awt.event.*;
import java.io.*;

public class BeatBox2 {
	JPanel mainpanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame theFrame;
	
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat",
	                                          "Acoustic Snare", "Crash Cymbal", "Hand Clap", "High Tom",
	                                          "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell",
	                                          "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
	int [] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	 
	public static void main(String[] args){
		new BeatBox2().buildGUI();
	}//end main
	 
	public void buildGUI(){
		theFrame = new JFrame("Cyber BeatBox");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		  
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		  
		JButton start = new JButton("Start");
		start.addActionListener(new MyStartlistener());
		buttonBox.add(start);
		  
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStoplistener());
		buttonBox.add(stop);  
		  
		JButton uptempo = new JButton("Tempo up");
		uptempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(uptempo);
		  
		JButton downtempo = new JButton("Tempo down");
		downtempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downtempo);
		  
		//This is new from BeatBox (original)
		JButton serialize = new JButton("Serialize");
		serialize.addActionListener(new MySendListener());
		buttonBox.add(serialize);
		
		//This is new from BeatBox (original)
		JButton restore = new JButton("Restore");
		restore.addActionListener(new MyReadInListener());
		buttonBox.add(restore);
		  
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i = 0; i < 16; i++){
			nameBox.add(new Label(instrumentNames[i]));
		}//end for loop
		  
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		  
		theFrame.getContentPane().add(background);
		  
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainpanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainpanel);
		  
		for(int i = 0; i < 256; i++){
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainpanel.add(c);
		}//end for loop
		  
		setUpMidi();
		  
		theFrame.setBounds(50,50,300,300);
		theFrame.pack();
		theFrame.setVisible(true);
	}//end buildgui
		
	public void setUpMidi(){
		try{
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ,4);
			track  = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		}//end try
		catch (Exception e){
			e.printStackTrace();
		}//end catch
	}//end setupmidi
	 
	public void buildTrackAndStart(){
		int[] trackList = null;
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		for(int i = 0; i < 16; i++){
			trackList = new int[16];
			int key = instruments[i];
		    
			for(int j = 0; j < 16; j++){
				JCheckBox jc = checkboxList.get(j + (16*i));
				
				if(jc.isSelected()){
					trackList[j] = key;
				}//end if
				else{
					trackList[j] = 0;
				}//end else
			}//end inner for loop
		
			makeTracks(trackList);
			track.add(makeEvent(176,1,127,0,16));
		}//end outer for loop
		
		track.add(makeEvent(192,9,1,0,15));
		  
		try{
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
			sequencer.setTempoInBPM(120);
		}//end try
		catch (Exception e){
			e.printStackTrace();
		}//end catch
	}//end buildtrackandstart
	 
	public class MyStartlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			buildTrackAndStart();
		}//end actionperformed
	}//end inner class start
	 
	public class MyStoplistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			sequencer.stop();
		}//end actionperformed
	}//end inner class stop
	 
	public class MyUpTempoListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor*1.03));
		}//end actionperformed
	}//end inner class uptempo
	 
	public class MyDownTempoListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor*.97));
		}//end actionperformed
	}//end innerclass downtempo
	 
	//this part is new from BeatBox (original)
	public class MySendListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			boolean[] checkboxState = new boolean[256];
			   
			for(int i = 0; i < 256; i++){
				JCheckBox check = checkboxList.get(i);
				if(check.isSelected()){
					checkboxState[i] = true;
				}//end if
			}//end for loop
			   
			try {
				FileOutputStream fileStream = new FileOutputStream(new File("Checkbox.ser"));
				ObjectOutputStream os = new ObjectOutputStream(fileStream);
				os.writeObject(checkboxState);
			}//end try
			catch(Exception ex) {
				ex.printStackTrace();
			}//end catch
		}//end actionperformed
	}//end innerclass mysendlistener
	
	//this part is new from BeatBox (original)
	public class MyReadInListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			boolean[] checkboxState = null;
			try{
				FileInputStream filein = new FileInputStream(new File("Checkbox.ser"));
				ObjectInputStream is = new ObjectInputStream(filein);
				checkboxState = (boolean[]) is.readObject();
			}//end try
			catch (Exception ex){
				ex.printStackTrace();
			}//end catch
				
			for(int i = 0; i <256; i++){
				JCheckBox check = checkboxList.get(i);
				if(checkboxState[i]){
					check.setSelected(true);
				}//end if
				else{
					check.setSelected(false);
				}//end else
			}//end for loop
				
			sequencer.stop();
			
		}//end actionperformed
	}//end innerclass
	  
	//this part is new from BeatBox (original)
	public void loadFile(File file){
		try {  
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			reader.close();  
		}//end try
		catch (IOException ex) {  
			System.out.println("Couldn't read the file");  
			ex.printStackTrace();  
		}//end catch
	}//end loadfile 
	 
	public void makeTracks(int[] list){
		for(int i = 0; i<16; i++){
			int key = list[i];
			   
			if(key !=0){
				track.add(makeEvent(144,9,key,100,i));
				track.add(makeEvent(128,9,key,100,i+1));	
			}//end if
		}//end for loop
	}//end maketracks
	 
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
		MidiEvent event = null;
		try{
			ShortMessage a = new ShortMessage();
			a.setMessage(comd,chan,one,two);
			event = new MidiEvent(a,tick);
		}//end try
		catch (Exception e){
			e.printStackTrace();
		}//end catch
		   
		return event;
	}//end makeevent
}//end class beatbox
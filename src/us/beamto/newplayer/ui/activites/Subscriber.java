package us.beamto.newplayer.ui.activites;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;


public class Subscriber extends Observable{
	private ArrayList<Observer> observers;
	public static Subscriber instance = null;
	public static Subscriber getInstance(){
		if(instance == null){
			instance = new Subscriber();
		}
		return instance;
	}
	
	public Subscriber(){
		observers = new ArrayList<Observer>();
	}

	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		observers.add(observer);
	}
	
	public void message(String message){
		setChanged();
		notifyObservers(message);
	}
}

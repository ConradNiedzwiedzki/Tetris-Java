import java.awt.event.ActionListener;

/**
 * Klasa Timer oodpowiada za zliczanie czasu dzialania gry. Na jej podstawie mozliwe jest np. pauzowanie gry i przyspieszanie spadania klockow.
 * @author Konrad Niedzwiedzki
 *
 */
public class Timer extends Thread
{
	/**
	 * Pole odowiadajace czasowi gry.
	 */
	private long czas;
	
	/**
	 * Pole informujace o zatrzymaniu gry.
	 */
	private boolean fPauza = true;
	
	/**
	 * Pole opisujace listener klawiatury.
	 */
	private ActionListener listener;
	
	/**
	 * Konstruktor timera.
	 * @param czas Parametr czasu gry.
	 * @param listener Parametr listenera.
	 */
	public Timer(long czas, ActionListener listener)
	{ 
		setCzas(czas);
		this.listener = listener;
	}
	
	/**
	 * Metoda setPaused() odpowiada za pauzowanie (zatrzymywanie) gry.
	 * @param pause Wartosc 1 tego parametru to informacja o tym, ze uzytkownik nacisnal przycisk Pauzy.
	 */
	public void setPaused(boolean pause)
	{ 
		this.fPauza = pause;
		if(!this.fPauza)
		{ 
			synchronized(this) 
			{ 
				this.notify();
			} 
		}			
	}
	
	/**
	 * Metoda setCzas() ustawia czas.
	 * @param czas W aplikacji jest to moment startu Timera.
	 */
	public void setCzas(long czas) 
	{ 
		this.czas = czas; 
	}
	
	/**
	 * Metoda przyspiesz() pozwala na przyspieszenie czasu spadania klockow przy zwiekszaniu poziomu gry.
	 */
	public void przyspiesz() 
	{
		this.czas = (int)(this.czas * 0.9); 
	}
	
	/**
	 * Metoda tun() odpowiada za uruchomienie.
	 */
	public void run() 
	{
		while(true) 
		{
			try 
			{ 
				sleep(this.czas); 
			} 
			catch (Exception e) {}
			
			if(this.fPauza)
			{
				try 
				{ 
					synchronized(this) 
					{ 
						this.wait(); 
					} 
				} 
				catch(InterruptedException ie) {}
			}
			
			synchronized(this) 
			{ 
				this.listener.actionPerformed(null); 
			}
		}
	}
} 
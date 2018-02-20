import java.awt.Color;
import java.awt.Font;
import java.awt.Label;


/**
 * Klasa Etykieta opisuje etykiety, ktore wyswietlaja bierzaca punktacje oraz poziom gry.
 * @author Konrad Niedzwiedzki
 */
@SuppressWarnings("serial")
public class Etykieta extends Label
{
	/**
	 * Pole opisujace czcionke napisow wyswietlanych w statystykach gry.
	 */
	private final static Font font = new Font("Jokerman", Font.BOLD, 18);
	
	/**
	 * Konstruktor etykiety statystyk gry.
	 * @param tekst Tekst wyswietlany przez etykiete.
	 */
	public Etykieta(String tekst) 
	{
		super(tekst);
		this.setForeground(Color.white);
		setFont(this.font);
	}		
}
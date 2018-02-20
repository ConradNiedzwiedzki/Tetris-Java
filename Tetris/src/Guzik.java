import java.awt.Button;
import java.awt.Dimension;

/**
 * Klasa odpowiadajaca przyciskom wyswietlanym w lewym gornym rogu aplikacji.
 * @author Konrad Niedzwiedzki
 *
 */
@SuppressWarnings("serial")
public class Guzik extends Button 
{
		/**
		 * Konstruktor przyciskow menu gry.
		 * @param etykieta Etykieta przypisana do danego przycisku.
		 */
		public Guzik(String etykieta) 
		{
			super(etykieta);
		}
		
		/** 
		 * Metoda pobierajaca preferowany rozmiar.
		 */
		public Dimension getPreferredSize()
		{
			return new Dimension(220, 30);
		}
}
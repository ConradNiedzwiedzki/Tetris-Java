import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

/**
 * Klasa Grafika to klasa odpowiadajaca za grafike calej aplikacji (tj. tlo, plansza, figury wyswietlane na ekranie, obrazek w tle okienka ze spadajacymi figurami itp.)
 * @author Konrad Niedzwiedzki
 *
 */
@SuppressWarnings("serial")
public class Grafika extends Canvas
{
	/**
	 * Pole Image opisuje obraz wyswietlany w tle.
	 */
	private Image obraz = null;
	
	/**
	 * Implementacja wymagana do skonfigurowania wymiaru grafiki aplikacji.
	 */
	private Dimension wymiary = new Dimension(-1,-1);
	
	/**
	 * Implementacja wymagana do skonfigurowania grafiki aplikacji.
	 */
	private Graphics graphics = null;
	
	/**
	 * Implementacja wymagana do skonfigurowania grafiki aplikacji.
	 */
	private Graphics graphics2 = null;
	
	/**
	 * Mozliwe kolory figur pojawiajacych sie w grze.
	 */
	public final static Color kolorFigury[] =
	{	
		new Color(0xCC00CC), 
		new Color(0xDC143C), 
		new Color(0x00CED1), 
		new Color(0xFFD700), 
		new Color(0x32CD32), 
		new Color(0x6600CC), 
		new Color(0xFFA500),
	};
	
	/**
	 * Pole opisujace losowe kolory kwadracikow w figurach.
	 */
	private static Random random = new Random();
	
	/**
	 * Pole odpowiadajace glownej klasie aplikacji.
	 */
	private static Tetris tetris = new Tetris();

	/**
	 * Pole opisujace plansze gry.
	 */
	private int plansza[][];
	
	/**
	 * W polu czyRysowacTlo dla wartosci true tlo w aplikacji jest rysowane.
	 */
	private boolean czyRysowacTlo;
	
	/**
	 * Konstruktor grafiki.
	 * @param plansza Pole opisujace plansze gry.
	 * @param czyRysowacTlo Parametr odnoszaca sie do rysowania tla.
	 */
	public  Grafika (int[][] plansza, boolean czyRysowacTlo) 
	{
		this.plansza = plansza;
		this.czyRysowacTlo = czyRysowacTlo;
		clear();
	}
	
	/**
	 * Metoda update() odswieza grafike wyswietlana na ekranie.
	 */
	public void update(Graphics g) 
	{
		paint(g);
	}

	/**
	 * Metoda startPaint() zawiera poczatkowe wartosci i tworzy grafike przy wywolaniu w metodzie paint().
	 * @param g Jest to parametr typu Graphics (z Abstract Window Toolkit - graficznego interfejsu uzytkownika).
	 * @return Zwracany jest zmodyfikowany parametr g.
	 */
	public Graphics startPaint (Graphics g) 
	{
		this.graphics2 = g;
		Dimension d = getSize();
		
		if ( (this.obraz == null) || (d.width != this.wymiary.width) ||	(d.height != this.wymiary.height) ) 
		{
			this.obraz = createImage(d.width, d.height);
			this.graphics = this.obraz.getGraphics();
			this.wymiary = d;
			this.graphics.setFont(getFont());
		}
		return this.graphics;
	}
	
	/**
	 * Metoda clear() czysci plansze gry.
	 */
	public void clear() 
	{
		for(int i=0; i<plansza.length; i++)
		{
			for(int j=0; j<plansza[0].length; j++)
			{
				plansza[i][j] = -1;
			}
		}
	}
	
	/**
	 * Metoda paint() odpowiada za rysowanie grafiki aplikacji.
	 */
	@SuppressWarnings("static-access")
	public void paint(Graphics g) 
	{
		g = this.startPaint(g); 
		int szerokosc = this.getSize().width;
		int wysokosc = this.getSize().height;
		g.clearRect(0, 0, szerokosc, wysokosc);
			
		int wymiarKratki = 23;
		int xPocz = 150;
		int yPocz = 0;
	
		if(this.czyRysowacTlo) 
		{
			xPocz = 16;
			Image im = getToolkit().getImage("res/images.jpg");
		    g.drawImage( im, 0, 0, this );
		}
		
		for(int i=0; i<this.plansza.length; i++) 
		{
			for(int j=0; j<this.plansza[0].length; j++) 
			{
				if(this.plansza[i][j] != -1) 
				{
					g.setColor(this.kolorFigury[this.plansza[i][j]]);
					
					if(this.tetris.get_level() % 4 == 0)
					{
						// kolorowe kwadraciki 
						int rand = Math.abs(this.random.nextInt());
						g.setColor(this.kolorFigury[rand % 7]);
					}
					
					int x = xPocz + j * wymiarKratki;
					int y = yPocz + i * wymiarKratki;
					
					g.fill3DRect(x, y, wymiarKratki, wymiarKratki, true);
					
					g.setColor(Color.black);
					g.draw3DRect(x, y, wymiarKratki, wymiarKratki, true);
				}
			}
		}	
		this.graphics2.drawImage(obraz, 0, 0, null); 
	}
}
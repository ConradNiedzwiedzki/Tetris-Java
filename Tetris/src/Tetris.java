import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;



/** Glowna klasa programu. Zawarte sa w niej wszystkie funkcje obslugujace logike gry.
 * @author Konrad Niedzwiedzki
 *
 */
@SuppressWarnings("serial")
public class Tetris extends Applet 
{
	private final static int TimerStart = 600;
	
	/**
	 * Pole WIERSZE odpowiada liczbie wierszy planszy gry.
	 */
	private final static byte WIERSZE = 18;
	
	/**
	 * Pole KOLUMNY odpowiada liczbie kolumn planszy gry.
	 */
	private final static byte KOLUMNY = 10;
	
	/**
	 * Zmienna odpowiadajaca z kolor tla.
	 */
	private final static Color Tlo = new Color(0x30050);
	
	//   o    oo   o    o    o    o
	//   o    o    o    oo   oo   oo   oo
	//   o    o    oo    o   o    o    oo
	//   o
	//
	//   0    1    2    3    4    5    6   	
	
	/**
	 * FIGURY to tablica trojwymiarowa wszystkich figur, jakie moga pojawic sie w grze.
	 */
	private final static boolean FIGURY[][][] = {
		{ {false, true, false, false}, {false, true, false, false}, {false, true, false, false}, {false, true, false, false}, },  // 0
		{ {false, false, false, false}, {false, true, true, false}, {false, true, false, false}, {false, true, false, false}, },  // 1
		{ {false, false, false, false}, {false, true, false, false}, {false, true, false, false}, {false, true, true, false}, },  // 2 
		{ {false, false, false, false}, {false, true, false, false}, {false, true, true, false}, {false, false, true, false}, },  // 3
		{ {false, false, false, false}, {false, false, true, false}, {false, true, true, false}, {false, true, false, false}, },  // 4
		{ {false, false, false, false}, {false, true, false, false}, {false, true, true, false}, {false, true, false, false}, },  // 5 
		{ {false, false, false, false}, {false, false, false, false}, {false, true, true, false}, {false, true, true, false}, },  // 6
	};
	
	/**
	 * Zmienna pomocnicza planszy.
	 */
	private static boolean tempPlansza[][] = new boolean[4][4];
	
	/**
	 * Pole fStart to informacja o tym, czy gra zostala rozpoczeta.
	 */
	private static boolean fStart = true;
	
	/**
	 * Pole fPauza to informacja o tym, czy gracz zatrzymal gre.
	 */
	private static boolean fPauza = false;
	
	/**
	 * Deklaracja zmiennej losujacej.
	 */
	private static Random random = new Random();	
	
	/**
	 * Zmienna sluzaca do zliczania punktow.
	 */
	private static int punkty = 0;
	
	/**
	 * Zmienna sluzaca do zliczania poziomow gry.
	 */
	private static int poziom = 1;
	
	/**
	 * Pole odpowiadajace planszy gry.
	 */
	private int plansza[][] = new int[WIERSZE][KOLUMNY];
	
	/**
	 * Pole sluzace do pomocy w wysietlaniu kolejnej planszy
	 */
	private int nastepnaPlansza[][] = new int[4][4];
	
	/**
	 * Zmienna pomocnicza do zliczania pelnych linii.
	 */
	private int linie = 0;
	
	/**
	 * Pole odpowiadajace za grafike planszy gry.
	 */
	private Grafika planszaCanvas = new Grafika(plansza, true);
	
	/**
	 * Pole odpowiadajace za grafike nastepnej planszy gry.
	 */
	private Grafika nastepnaPlanszaCanvas = new Grafika(nastepnaPlansza, false);
	
	/**
	 * Pole timera.
	 */
	private Timer timer;
	
	/**
	 * Zmienna odzwierciedlajaca obecna figure na planszy.
	 */
	private KlasaFigura obecnaFigura;
	
	/**
	 * Pole odpowiadajace za nastepna figure.
	 */
	private KlasaFigura nastepnaFigura = randomFigura();
	
	/**
	 * Pole etykietaPoziomu odpowiada za napis w oknie aplikacji informujacy o danym poziomie w grze.
	 */
	private Etykieta etykietaPoziomu;
	
	/**
	 * Pole etykietaPunktacji odpowiada za napis w oknie aplikacji informujacy o posiadanej przez gracza punktacji.
	 */
	private Etykieta etykietaPunktacji;
	
	/**
	 * Pole guzikStartu odpowiada za przycisk rozpoczecia gry w aplikacji.
	 */
	private final Button guzikStartu = new Guzik("Rozpocznij grê");
	
	/**
	 * Pole guzikPauzy odpowiada za przycisk zatrzymania gry w aplikacji.
	 */
	private final Button guzikPauzy = new Guzik("Pauza");	
	
	/**
	 * Metoda pozwalajaca pozyskac poziom gry, na ktorym w obecnej chwili jest gracz.
	 * @return Zwracany jest poziom gry.
	 */
	public static int get_level()
	{
		return poziom;
	}
	
	/**
	 * Klasa wewnetrzna, reprezentujaca figure. 
	 * @author Konrad Niedzwiedzki
	 *
	 */
	private class KlasaFigura
	{
		/** 
		 * Pole opisujace polozenie figury na planszy.
		 */
		private boolean polozenie[][];
		
		/**
		 * Pole opisujace figure.
		 */
		private int figura;
		
		/**
		 * Pole opisujace pozycje figury.
		 */
		private Point pozycja = new Point(3, -4);
		
		/**
		 * Metoda pozwalajaca pobrac wartosc wspolrzednej X.
		 * @return Zwracana jest wartosc wspolrzednej X.
		 */
		public int pobierzX() { return pozycja.x; }
		
		/**
		 * Ustawienie wspolrzednej X.
		 * @param nowyx Parametr okreslajacy nowa wartosc wspolrzednej x.
		 */
		public void ustawX(int nowyx) { pozycja.x = nowyx; }
		
		/**
		 * Ustawienie wspolrzednej Y.
		 * @param nowyy Parametr okreslajacy nowa wartosc wspolrzednej y.
		 */
		public void ustawY(int nowyy) { pozycja.y = nowyy; }
		
		/**
		 * Ustawienie nowej pozycji figury.
		 * @param newx Paramter wspolrzednej x.
		 * @param newy Parametr wspolrzednej y.
		 */
		public void ustawPozycje(int newx, int newy) { ustawX(newx); ustawY(newy); }
		
		/**
		 * Konstruktor klasy KlasaFigura.
		 * @param figura Parametr odpowiadajacy okreslonej figurze.
		 */
		public KlasaFigura(int figura) 
		{
			this.figura = figura;
			this.polozenie = new boolean[4][4];
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					this.polozenie[i][j] = FIGURY[figura][i][j];
				}
			}
		}
		
		/**
		 * Przyspieszenie spadania figury w dol.
		 * @return Zwracana jest wartosc true/false, ktora byla pobrana wczesniej z metody czyRuch().
		 */
		public boolean czyWDol() 
		{
			synchronized(timer) 
			{
				kasujPozycje();
				this.pozycja.y++;
				boolean OK = czyRuch();
				this.pozycja.y--;
				ruch(plansza);
				return OK;
			}
		}
		
		/**
		 * Metoda opisujaca ruch figury.
		 * @return Zwracana jest wartosc true lub false w zaleznosci od tego, czy ruch mozna wykonac.
		 */
		public boolean czyRuch() 
		{
			for(int i=0; i<4; i++) 
			{
				for(int j=0; j<4; j++) 
				{
					int do_xa = j + this.pozycja.x;
					int do_yka = i + this.pozycja.y;
					
					if(this.polozenie[i][j] && (0 > do_xa || do_xa >= KOLUMNY || do_yka >= WIERSZE || (do_yka >= 0 && plansza[do_yka][do_xa] != -1)))
					{
						return false;
					}
				}
			}
			return true;
		}
		
		/**
		 * Metoda pozwalajaca skasowac wczesniejsza pozycje.
		 */
		public void kasujPozycje() 
		{
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					if(this.polozenie[i][j] && this.pozycja.y+i>=0)
					{
						plansza[this.pozycja.y + i][this.pozycja.x + j] = -1;
					}
				}
			}
		}
		
		/**
		 * Metoda okreslajaca ruch figury.
		 * @param plansza Parametr planszy gry.
		 */
		public void ruch(int plansza[][]) 
		{
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					if(this.polozenie[i][j] && this.pozycja.y+i>=0)
					{
						plansza[this.pozycja.y + i][this.pozycja.x + j] = figura;
					}
				}
			}
		}
		
		/**
		 * Metoda pozwalajaca na rotacje figury.
		 */
		public void rotacja()
		{
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					tempPlansza[i][j] = this.polozenie[i][j];
				}
			}
			
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					this.polozenie[j][i] = tempPlansza[i][3-j];
				}
			}
		}
		
		/**
		 * Metoda pozwalajaca na rotacje odwrocona.
		 */
		public void rotacjaWstecz() 
		{
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					this.polozenie[i][j] = tempPlansza[i][j];
				}
			}
		}
		
		/**
		 * Metoda sprawdzajaca, czy gracz odniosl porazke.
		 * @return Zwracana jest informacja, czy gracz zapelnil cala plansze (przegral).
		 */
		public boolean czyKoniecGry() 
		{
			for(int i=0; i<4; i++) 
			{
				if(this.pozycja.y + i >= 0)
				{
					return false;
				}
				
				for(int j=0; j<4; j++) 
				{
					if(this.polozenie[i][j])
					{ 
						return true;
					}
				}
			}
			return true;
		}
	} 
	
	/**
	 * Metoda pozwalajaca stworzyc losow¹ figure.
	 * @return Zwracana jest wylosowana figura.
	 */
	private KlasaFigura randomFigura() 
	{
		int rand = Math.abs(this.random.nextInt());
		
		return new KlasaFigura(rand % (FIGURY.length));
	}
	
	/**
	 * Metoda, ktora pozwala wyswietlic nastepna figure.
	 */
	private void tworzNastepnaFigure() 
	{
		this.nastepnaPlanszaCanvas.clear();
		this.obecnaFigura = nastepnaFigura;
		this.obecnaFigura.ustawPozycje(3, -2);
		
		if(this.obecnaFigura.czyRuch()) 
		{
			this.nastepnaFigura = randomFigura();
			this.nastepnaFigura.ustawPozycje(0, 0);
			this.nastepnaFigura.ruch(nastepnaPlansza);
			this.nastepnaPlanszaCanvas.repaint();
		}
		else 
		{
			koniecGry();
		}
	}
	
	/**
	 * Metoda uzywana do zakonczenia gry.
	 */
	private void koniecGry() 
	{
		this.fPauza = false;
		this.timer.setPaused(true);
		this.guzikPauzy.setEnabled(false);
	}
	
	/**
	 * Metoda sprawdzajaca, czy gracz zapelnil caly wiersz.
	 * @param rzad Prarametr ten odzwierciedla rzad planszy.
	 * @return Metoda zwraca true w wypadku, gdy gracz zapelnil caly wiersz i false w przeciwnym.
	 */
	private boolean czyPelnyWiersz(int rzad) 
	{
		for(int i=0; i<KOLUMNY; i++)
		{
			if(this.plansza[rzad][i] == -1) 
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metoda liczaca pelne wiersze, jakie udalo sie ulozyc graczowi.
	 * @return Zwraca liczbe pelnych wierszy.
	 */
	private int liczPelneWiersze() 
	{
		int licznikPelnychLinii = 0;
		
		for(int i=0; i<WIERSZE; i++)
		{
			if(czyPelnyWiersz(i)) 
			{
				licznikPelnychLinii++;
			}
		}
		
		return licznikPelnychLinii;
	}	
	
	/**
	 * Metoda usuwa wiersz z planszy.
	 * @param rzad parametrem jest rzad, ktory metoda ma usunac.
	 */
	private void kasujWiersz(int rzad) 
	{
		for(int j=0; j<KOLUMNY; j++)
		{
			this.plansza[rzad][j] = -1;
		}
		
		for(int i=rzad; i>0; i--) 
		{
			for(int j=0; j<KOLUMNY; j++) 
			{
				this.plansza[i][j] = this.plansza[i-1][j];
			}
		}
	}
	
	/**
	 * Metoda usuwa pelny wiersz z planszy, dolicza punkty i zwieksza poziom gry.
	 */
	private void kasujPelnyWiersz() 
	{
		int PelneLinie = liczPelneWiersze();
		
		if(this.poziom % 4 == 0)
		{
			this.punkty += (10 * PelneLinie*PelneLinie*this.poziom*2);
		}
		else 
		{
			this.punkty += (10 * PelneLinie*PelneLinie*this.poziom);
		}
		
		this.etykietaPunktacji.setText("                        Punkty: " + this.punkty); 
		
		if(PelneLinie != 0)
		{
			this.linie += PelneLinie;
			
			if(this.linie >=  7 * this.poziom) 
			{
				this.timer.przyspiesz();
				this.poziom++;
				this.etykietaPoziomu.setText("                        Poziom: " + this.poziom);
				this.etykietaPoziomu.repaint();
			}
				
			for(int i=WIERSZE-1; i>=0; i--)
			{
				while(czyPelnyWiersz(i))
				{
					kasujWiersz(i);
				}
			}
			this.planszaCanvas.repaint();
			
			if(this.linie%5==0)
			{
				this.planszaCanvas.clear();
			}
		}
	}
	
	/**
	 * Metoda start, ktora jest wykonywana po inicjalizacji (metodzie init())
	 */
	public void start() 
	{
		this.timer = new Timer(TimerStart, new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				synchronized(timer) 
				{
					if(obecnaFigura.czyWDol()) 
					{
						obecnaFigura.kasujPozycje();
						obecnaFigura.pozycja.y++;
						obecnaFigura.ruch(plansza);
					}
					else if(!obecnaFigura.czyKoniecGry())
					{
						kasujPelnyWiersz();
						tworzNastepnaFigure();				
					}
					else 
					{
						koniecGry();
					}
				}
				planszaCanvas.repaint();
			}
		});
		this.timer.start();
	}
	
	/**
	 * Stop() to metoda wywolywana, gdy uzytkownik opusci aplikacje lub po bezposrednim wywolaniu.
	 */
	@SuppressWarnings("deprecation")
	public void stop() 
	{
		pauzaGry();
		synchronized(this.timer)
		{
			this.timer.stop();
		}
		this.timer = null;
	}
	
	/**
	 * Metoda, ktora ustawia parametry aplikacji przy starcie gry.
	 */
	private void startGry() 
	{
		this.fStart = false;
		this.timer.setCzas(TimerStart);
		this.timer.setPaused(false);
		this.guzikStartu.setLabel("Nowa Gra");
		
		this.fPauza = true;
		this.guzikPauzy.setEnabled(true); 
		this.guzikPauzy.setLabel("Pauza");
		this.guzikPauzy.validate();
	}
	
	/**
	 * Metoda inicjujaca nowa gre.
	 */
	private void nowaGra() 
	{
		this.planszaCanvas.clear();
		tworzNastepnaFigure();
		
		this.linie = 0;
		this.poziom = 1;
		this.punkty = 0;
		
		this.etykietaPoziomu.setText("                        Poziom: " + this.poziom);
		this.etykietaPunktacji.setText("                        Punkty: " + this.punkty);
		
		startGry();
	}
	
	/**
	 * Metoda, ktora realizuje zatrzymywanie gry.
	 */
	private void pauzaGry() 
	{
		this.fPauza = false;
		this.timer.setPaused(true);
		this.guzikPauzy.setLabel("Wznów grê");
	}
	
	/**
	 * Metoda, ktora realizuje wznowienie gry.
	 */
	private void wznowienieGry() 
	{
		this.fPauza = true;
		this.timer.setPaused(false);
		this.guzikPauzy.setLabel("Pauza");
	}
	
	/**
	 * Metoda init - metoda inicjujaca. Tworzy wszystkie obiekty, jakie sa zawarte w metodzie. Zawiera ustawienie listener'ow na guziki, na klawiature i akcje, jakie sa wykonywane przy nacisnieciu odpowiednich strzalek na klawiaturze.
	 */
	public void init() 
	{
		// Metoda inicjuj¹ca 
		
		tworzNastepnaFigure();

		this.guzikPauzy.setEnabled(false);
		
		// Ustawienie listener-ów na guziki 
		this.guzikPauzy.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				if(fPauza) 
				{
					pauzaGry();
				}
				else 
				{
					wznowienieGry();
				}
			}
		});
		
		this.guzikStartu.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				if(fStart) 
				{
					startGry();
				}
				else 
				{
					nowaGra();
				}
			}
		});		
		
		// Ustawienie listenera na klawiature 
		KeyListener listener_klaw = new KeyAdapter() 
		{
			// Akcja na strza³ki 
			public void keyPressed(KeyEvent e) 
			{
				if(fPauza) 
				{
					synchronized(timer) 
					{
						if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
						{ 
							// Akcja na <- lub -> 
							int ofset = 1;
							
							if(e.getKeyCode() == KeyEvent.VK_LEFT) 
							{
								ofset = -1;
							}
							
							obecnaFigura.kasujPozycje();
							
							obecnaFigura.ustawX(obecnaFigura.pobierzX() + ofset); 
							
							if(!obecnaFigura.czyRuch())
							{
								obecnaFigura.ustawX(obecnaFigura.pobierzX() - ofset); 
							}
							
							obecnaFigura.ruch(plansza);	
						}
						else if (e.getKeyCode() == KeyEvent.VK_UP)
						{ 
							// Akcja na ^
							
							obecnaFigura.kasujPozycje();
								
							obecnaFigura.rotacja();
							if(!obecnaFigura.czyRuch())
							{
								obecnaFigura.rotacjaWstecz();
							}
								
							obecnaFigura.ruch(plansza);
							
						}
						
						if(e.getKeyCode() == KeyEvent.VK_DOWN && obecnaFigura.czyWDol()) 
						{ 
							// Akcja na V 
							obecnaFigura.kasujPozycje();
							obecnaFigura.pozycja.y++;
							obecnaFigura.ruch(plansza);	
						}
					}
					planszaCanvas.repaint();
				}
			}
		};
		
		this.guzikStartu.addKeyListener(listener_klaw);
		this.guzikPauzy.addKeyListener(listener_klaw);
		
		JPanel panel_sterujacy = new JPanel();
		panel_sterujacy.add(this.guzikStartu);
		panel_sterujacy.add(this.guzikPauzy);
		panel_sterujacy.setBackground(Tlo);
		panel_sterujacy.setOpaque(false);
		
		JPanel prawy_panel = new JPanel(new GridLayout(5, 2));	
		prawy_panel.setBackground(Tlo);
		prawy_panel.add("Center",panel_sterujacy); 
	
		JPanel tmp = new JPanel(new GridLayout(1,1,3,3));
		tmp.add("Center", this.nastepnaPlanszaCanvas);
    	tmp.setBackground(Tlo);
		tmp.setOpaque(false);
		
		this.etykietaPoziomu = new Etykieta("                        Poziom: " + this.poziom);
		this.etykietaPunktacji = new Etykieta("                        Punkty: " + this.punkty);
		
		JPanel panel_statystyk_gry = new JPanel(new GridLayout(2, 2));
		panel_statystyk_gry.add(this.etykietaPoziomu);
		panel_statystyk_gry.add(this.etykietaPunktacji);
		panel_statystyk_gry.setBackground(Tlo);
		panel_statystyk_gry.setOpaque(false);
		
		JPanel tmp1 = new JPanel(new BorderLayout());
		tmp1.add("Center", panel_statystyk_gry);
		
		prawy_panel.add(tmp1);
		prawy_panel.add(tmp);
		
		this.setLayout(new GridLayout(1, 2));
		
		this.add(prawy_panel);
		this.add(this.planszaCanvas);
		this.validate();
	}
	
	/**
	 * Glowna metoda aplikacji, w ktorej tworzony jest program okienkowy za pomoca komponentu JFrame, a nastepnie wywolywane sa metoda inicjujaca i metoda start. Ustawiane sa takze parametry okna aplikacji.
	 * @param args Podstawowy argument metody main().
	 */
	public static void main(String[] args) 
	{
		JFrame okno = new JFrame("Tetris");
		
		Tetris tetris;
		tetris = new Tetris();
		
		okno.add(tetris);
		tetris.init();
		tetris.start();
		
		okno.setSize(530, 490);
		okno.setResizable(false);
		okno.setVisible(true);
	}
}
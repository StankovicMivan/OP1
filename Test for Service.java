package com.servis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Test {
	public static Scanner sc = new Scanner(System.in);
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

	public static void main(String[] args) {

		Servis s = new Servis();
		s.load("Nalozi.txt");

		//MENI
		String opcija;
		do {
			System.out.println("******************** MENI ********************");
			
			System.out.println("1.  Unos novog radnog naloga na server.");
			System.out.println("2.  Ispis liste naloga sa server.");
			System.out.println("3.  Izmena podataka o radnom nalogu");
			System.out.println("4.  Brisanje podataka o radnom nalogu.");
			System.out.println("5.  Pretraga i ispis svih radnih naloga za odredjenog servisera.");
			System.out.println("6.  Pretraga i ispis svih radnih naloga odredjenog servisera, za odredjen datum.");
			System.out.println("7.  Izracunavanje i ispis prosecne cene za odredjen tip kvara.");
			System.out.println("8.  Pronalazenje i ispis najskupljeg kvara u zadatom vremenu.");
			System.out.println("9. Ispis podataka o servisu.");

			System.out.println("0.  Izlaz iz programa.");
			System.out.println("Unesite opciju:");

			opcija= sc.nextLine();

			switch(opcija) {

			
			case "1" :
				unosNovogNaloga(s);	//work
				break;
			case "2" :
				ispis11();			//work
				break;
			case "3" :
				izmenaNaloga(s);	
				break;
			case "4" :
				izbrisiNalog(s);
				break;
			case "5" :
				ispisNaloga6(s);
				break;
			case "6" :
				ispisNaloga7(s);
				break;
			case "7" :
				izracunavanje8(s);
				break;
			case "8" :
				ispisNajkupljeg9(s);
				break;
			case "9" :
				ispisPodatakaOServisu(s);
				break;

			}
		}while(!opcija.equals("0"));

		s.save("Nalozi.txt");
		sc.close();



	}

	public static void ispis11() {
	
		//load
		String url = "jdbc:mysql://localhost:3306/servis";
		String user = "root";
		String pass = "koliko21";
		zaglavlje();
		try {
			//1. Get a cinnection to database
			Connection myConn = DriverManager.getConnection(url, user, pass);
			// 2. Create a statemt
			Statement myStmt = myConn.createStatement();
			// 3.
			ResultSet myRs = myStmt.executeQuery("select * from lista");
			//4.
			while(myRs.next()) {
				String identifikator = myRs.getString("Identifikator");
				String imeS =  myRs.getString("imeServisera");
				String prez = myRs.getString("prezimeServisera");
				String datumR = myRs.getString("datum");
			//	LocalDate datumA = LocalDate.parse(datumR, dtf);
			//	System.out.println(datumR);
				String opisK = myRs.getString("opisKvara");
				String cena =  myRs.getString("cena");
				String adresa =  myRs.getString("adresa") ;
				String tipKvara =  myRs.getString("tipKvara")  ;
				System.out.println(String.format( "|%14s | %10s | %10s | %16s | %25s | %12s| %17s| %16s |",identifikator,imeS,prez,datumR,opisK,cena,adresa,tipKvara));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void ispisNajkupljeg9(Servis s) {
		LocalDate datum1= null;
		LocalDate datum2= null;
		String datum;
		do {
			System.out.println("Unesite donju granicu pretrage po datumu");
			datum = sc.nextLine();
		}while(!datumValid(datum));
		datum1 = LocalDate.parse(datum, dtf);
		do {
			System.out.println("Unesite gornju granicu pretrage po datumu");
			datum = sc.nextLine();
		}while(!datumValid(datum));
		datum2 = LocalDate.parse(datum, dtf);

		System.out.printf("Najskuplji kvar u periodu [ %s - %s ] iznosi %.2f .",dtf.format(datum1), dtf.format(datum2), s.ispis9(datum1,datum2));
		System.out.println();
		System.out.println();

	}

	public static void izracunavanje8(Servis s) {
		String tipKvara = null;
		do {
			System.out.println("Unesite tip kvara:");
			tipKvara = sc.nextLine().trim();
		}while(!isTipKvaraValid(tipKvara));

		System.out.printf("Prosecna cena kvara je %.2f za kvar %s", s.izracunavanje7(tipKvara),tipKvara);
		System.out.println();

	}

	public static void ispisNaloga7(Servis s) {

		System.out.println("Unesite ime servisera: ");
		String ime= sc.nextLine().trim();

		System.out.println("Unesite prezime servisera: ");
		String prezime= sc.nextLine().trim();

		LocalDate datum1= null;
		LocalDate datum2= null;
		String datum;
		do {
			System.out.println("Unesite donju granicu pretrage po datumu");
			datum = sc.nextLine();
		}while(!datumValid(datum));
		datum1 = LocalDate.parse(datum, dtf);
		do {
			System.out.println("Unesite gornju granicu pretrage po datumu");
			datum = sc.nextLine();
		}while(!datumValid(datum));
		datum2 = LocalDate.parse(datum, dtf);
		zaglavlje();
		System.out.println( s.pretraga7(ime,prezime,datum1, datum2));


	}

	public static void ispisNaloga6(Servis s) {

		System.out.println("Unesite ime servisera: ");
		String ime= sc.nextLine().trim();

		System.out.println("Unesite prezime servisera: ");
		String prezime= sc.nextLine().trim();
		zaglavlje();
		s.ispisPoRadniku( ime, prezime);
	}

	public static void izbrisiNalog(Servis s) {

		String id1;
		do {
			System.out.println("Unesite broj racuna koji zelite da obrisete:");
			id1 = sc.nextLine().trim();
		}while(!isIdValid(id1, s));
		int id = Integer.parseInt(id1);

		if(s.izbrisiNalog(id)) {
			System.out.println("Nalog je uspesno obrisan iz liste.");
			;
		}else
			System.out.println("Nalog nije obrisan iz liste.");

	}

	public static void izmenaNaloga(Servis s) {
		String id1;
		do {
			System.out.println("Unesite identifikator naloga koji zelite da promenite:");
			id1 = sc.nextLine().trim();
		}while(!isIdValid(id1, s));
		int id = Integer.parseInt(id1);

		System.out.println("Unesite ime servisera: ");
		String ime= sc.nextLine().trim();

		System.out.println("Unesite prezime servisera: ");
		String prezime= sc.nextLine().trim();

		String datum;
		do {
			System.out.println("Unesite datum rada:");
			datum = sc.nextLine().trim();
		}while(!datumValid(datum));
		LocalDate datumRada= LocalDate.parse(datum, dtf);


		System.out.println("Unesite opis kvara:");
		String opisKvara = sc.nextLine().trim();

		String temp;
		do {
			System.out.println("Unesite cenu popravke:");
			temp = sc.nextLine().trim();
		}while(!isDoubleValid(temp));
		double cena = Double.parseDouble(temp);

		System.out.println("Unesite adresu vlasnika:");
		String adresa = sc.nextLine().trim();

		String tipKvara = null;
		do {
			System.out.println("Unesite tip kvara:");
			tipKvara = sc.nextLine().trim();
		}while(!isTipKvaraValid(tipKvara));


		s.izmenaNaloga(id,ime,prezime,datumRada,opisKvara,cena,adresa,tipKvara);
	

	}

	private static boolean isIdValid(String id1, Servis s) {		//Metod overload ovaj sluzi da proverimo ako postoji da zamenimo informacije, a onaj drugi ako postoji da ne dozvolimo dalji unos
		try {
			int id = Integer.parseInt(id1);
			if(id >0) {
				for(int i=0; i< s.getKolekcijaNaloga().size();i++) {
					if(id == s.getKolekcijaNaloga().get(i).getIdentifikator()) {
						return true;
					}
				}
				System.out.println("Ne postoji takav identifikator naloga!");
				return false;
			}
			System.out.println("Identifikator mora biti veci od 0!!!");
			return false;
		}catch (Exception e) {
			System.out.println("Identifikator mora biti broj!");
			return false;
		}
	}


	public static void ispisSvihNaloga(Servis s) {
		String temp = "";

		zaglavlje();

		for(int i = 0; i < s.getKolekcijaNaloga().size(); i++) {
			temp += s.getKolekcijaNaloga().get(i) + "\n";
		}
		temp += ("______________________________________________________________________________________________________________________________________________");

		System.out.println(temp);
		System.out.println();


	}

	public static void zaglavlje() {
		System.out.println("______________________________________________________________________________________________________________________________________________");
		System.out.println(String.format("|%14s | %10s | %10s | %16s | %25s | %11s | %16s | %16s |","Identifikator", "Ime","Prezime", "Datum Rada","Opis kvara" , "Cena" , "Adresa vlasnika" ,"Tip Kvara"));
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
	}
	public static void unosNovogNaloga(Servis s) {
		String id1;
		do {
			System.out.println("Unesite identifikator naloga:");
			id1 = sc.nextLine().trim();
		}while(!isIdValid(s, id1));
		int id = Integer.parseInt(id1);

		System.out.println("Unesite ime servisera: ");
		String ime= sc.nextLine().trim();

		System.out.println("Unesite prezime servisera: ");
		String prezime= sc.nextLine().trim();

		String datum;
		do {
			System.out.println("Unesite datum rada:");
			datum = sc.nextLine().trim();
		}while(!datumValid(datum));
		LocalDate datumRada= LocalDate.parse(datum, dtf);


		System.out.println("Unesite opis kvara:");
		String opisKvara = sc.nextLine().trim();

		String temp;
		do {
			System.out.println("Unesite cenu popravke:");
			temp = sc.nextLine().trim();
		}while(!isDoubleValid(temp));
		double cena = Double.parseDouble(temp);

		System.out.println("Unesite adresu vlasnika:");
		String adresa = sc.nextLine().trim();

		String tipKvara = null;
		do {
			System.out.println("Unesite tip kvara:");
			tipKvara = sc.nextLine().trim();
		}while(!isTipKvaraValid(tipKvara));


		s.unosNaloga(id,ime,prezime,datumRada,opisKvara,cena,adresa,tipKvara);

	}

	private static boolean isTipKvaraValid(String tipKvara) {

		if(tipKvara.equals("mehanicki")) {
			return true;
		}else if(tipKvara.equalsIgnoreCase("elektronika")) {
			return true;
		}else if(tipKvara.equalsIgnoreCase("softver")) {
			return true;
		}else if(tipKvara.equalsIgnoreCase("ostalo")) {
			return true;
		}
		return false;
	}
	public static boolean isDoubleValid(String temp) {
		try {
			double x= Double.parseDouble(temp);
			if(x >=0) {
				return true;
			}else 
				return false;
		}catch (Exception e){
			return false;
		}
	}	

	public static boolean datumValid(String datum) {
		try {
			LocalDate datum1 = LocalDate.parse(datum, dtf);
			LocalDate datumNow = LocalDate.now();
			if(datum1.compareTo(datumNow)<=0) {
				return true;
			}else 
				return false;

		}catch (Exception e) {
			return false;
		}

	}

	public static boolean isIdValid(Servis s, String id1) {
		try {
			int id = Integer.parseInt(id1);
			if(id >0) {
				boolean provera = provera(id);
				if(provera) {
					return true;
				}else
					return false;
			}
			return false;

		}catch (Exception e) {
			return false;
		}

	}

	public static boolean provera(int id) {



		//load
		String url = "jdbc:mysql://localhost:3306/servis";
		String user = "root";
		String pass = "koliko21";

		try {
			//1. Get a cinnection to database
			Connection myConn = DriverManager.getConnection(url, user, pass);
			// 2. Create a statemt
			Statement myStmt = myConn.createStatement();
			// 3.
			ResultSet myRs = myStmt.executeQuery("select * from lista");
			//4.
			while(myRs.next()) {
				String identifikator = myRs.getString("Identifikator");
				int broj = Integer.parseInt(identifikator);
				if(id == broj) {
					return false;
				}

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;


	}

	public static void ispisPodatakaOServisu(Servis s) {
		System.out.println(s);

	}

	public static void unosPodatakaOServisu(Servis s) {
		System.out.println("Unesite naziv servisa:");
		String naziv = sc.nextLine().trim();

		System.out.println("Unesite adresu servisa:");
		String adresa = sc.nextLine().trim();

		System.out.println("Unesite telefon servisa:");
		String telefon = sc.nextLine().trim();


		s.setNaziv(naziv);
		s.setAdresa(adresa);
		s.setTelefon(telefon);
	}

}

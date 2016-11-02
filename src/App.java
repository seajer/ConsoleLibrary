import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

	public static void main( String[] args ) {
		MySQLConnection con = new MySQLConnection();
		Scanner input = new Scanner(System.in);

		try {
			con.connectToDb();
			System.out.println( "Hi there !!!" );
			while( true ) {
				System.out.println( "What u'r whant to do with your library?" );
				System.out.println( "1. Wiew all my books." );
				System.out.println( "2. Add new book." );
				System.out.println( "3. Remove book." );
				System.out.println( "4. Edit one of my books." );
				
				int userAction = input.nextInt();
				switch( userAction ) {
				case ( 1 ): {
					List<Book> books = con.findAllOrderByName();
					if( books.size() > 0 ) {
						for ( Book book : books ) {
							System.out.println( book );
						}
					} else {
						System.out.println( "Looks like u haven't any books." );
					}
					System.out.println();
					break;
				}
				case ( 2 ): {
					String name = null;
					String author = null;
					
					do {
						System.out.println( "Enter books name:" );
						name = input.next();
						if ( !validString( name ) ){
							System.out.println( "Books name can't be empty." );
						}
					} while ( !validString( name ) );
					
					do {
						System.out.println( "Enter books author:" );
						author = input.next();
						if ( validString( author ) ){
							System.out.println( "Books author can't be empty." );
						}
					} while ( !validString( author ) );
					con.addBook( name, author );
					System.out.println();
					break;
				}
				case ( 3 ): {
					System.out.println( "Enter name of book, that u want to remove." );
					String name = null;
					do {
						name = input.next();
						if ( validString( name ) ){
							System.out.println( "Books name can't be empty." );
						}
					} while ( !validString( name ) );
					boolean validNumber = true;
					List<Book> books = con.findBookByName( name );
					if ( books.size() == 1 ) {
						con.deleteBookByName( name );
					} else if( books.size() > 1 ){
						for (Book book : books) {
							System.out.println( book );
						}
						do {
							System.out.println( "Enter number of book, u want to remove (Counting start from 1)." );
							int number = input.nextInt();
							if( number > 0 && number <= books.size() ) {
								con.deleteBookById( books.get( number - 1 ).getId() );
								validNumber = false;
							} else {
								System.out.println( "Wrong number, try again." );
							}
						} while ( validNumber );
					} else {
						System.out.println( "You havent book with than name. Try something else." );
					}
					System.out.println();
					break;
				}
				case ( 4 ): {
					boolean validNumber = true;
					int id = 0;
					String oldName = null;
					String newName = null;
					
					do {
						System.out.println( "Enter name of book, that u want to rename." );
						oldName = input.next();
						if ( !validString( oldName ) ){
							System.out.println( "Books name can't be empty." );
						}
					} while ( !validString( oldName ) );
					
					List<Book> books = con.findBookByName(oldName);
					if ( books.size() == 0 ) {
						System.out.println( "You havent book with than name. Try something else." );
						System.out.println();
						break;
					} else if( books.size() > 1 ) {
						do {
							System.out.println( "Enter number of book, u want to rename (Counting start from 1)." );
							int number = input.nextInt();
							if( number > 0 && number <= books.size() ) {
								id = books.get( number - 1).getId();
								validNumber = false;
							} else {
								System.out.println( "Wrong number, try again." );
							}
						} while ( validNumber );
					} else {
						do {
							System.out.println( "Enter new name for book:" );
							newName = input.next();
							if ( !validString( newName ) ){
								System.out.println( "Books name can't be empty." );
							}
						} while ( !validString( newName ) );
					}
					if ( id != 0 ) {
						con.updateBookNameById(id, newName);
					} else {
						con.updateBookNameByName(oldName, newName);
					}
				System.out.println();
				break;
				}
				default: {
					System.out.println( "Chose only from 1, 2, 3 or 4." );
				}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			con.closeConnection();
		}
	}
	
	private static boolean validString( String inputString ) {
		if ( inputString != null && inputString.length() > 0 ) {
			return true;
		} else {
			return false;
		}
	}

}

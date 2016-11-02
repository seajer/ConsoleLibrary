import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MySQLConnection {
	// JDBC driver name and database URL
	static final String DB_URL = "jdbc:mysql://localhost/";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";
	static final String DB_NAME = "Library";
	static final String TABLE_NAME = "books";
	private Connection conn = null;
	private Statement stmt = null;
	private static Random rnd = new Random();

	public void connectToDb() throws ClassNotFoundException, SQLException {

		//connecting to mysql
		Class.forName( "com.mysql.jdbc.Driver" );
		conn = DriverManager.getConnection( DB_URL, USER, PASS );
		stmt = conn.createStatement();

		// creating database
		stmt = conn.createStatement();
		String createDb = "create database " + DB_NAME;
		stmt.executeUpdate( createDb );

		// using created DB
		String useDb = "USE " + DB_NAME;
		stmt.executeUpdate( useDb );

		// creating table
		String createTable = "create table " + TABLE_NAME
				+ " (id int not null, name varchar(255), author varchar(255), primary key(id));";
		stmt.executeUpdate( createTable );

		String query = creatingDefault();
		stmt.executeUpdate( query );

	}
	
	public void addBook( String name, String author ) throws SQLException {
		String query = "insert into " + TABLE_NAME + " VALUES (" + rnd.nextInt() + ", '" + name + "', '" + author + "' )";
		stmt.executeUpdate( query );
	}
	
	public void deleteBookByName( String name ) throws SQLException {
		String query = "delete from " + TABLE_NAME + " where name ='" + name +"'";
		stmt.executeUpdate( query );
	}
	
	public void deleteBookById( int id ) throws SQLException {
		String query = "delete from " + TABLE_NAME + " where id=" + id;
		stmt.executeUpdate( query );
	}
	
	public List<Book> findBookByName( String name ) throws SQLException {
		List<Book> books = new ArrayList<Book>();
		String query = "select id, name, author from " + TABLE_NAME + " where name=" + name;
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			Book book = new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"));
			books.add(book);
		}
		return books;
	}
	
	public void updateBookNameByName( String oldName, String newName ) throws SQLException {
		String query = "update " + TABLE_NAME + " set name='" + newName + "' where name='"+ oldName + "'";
		stmt.executeUpdate( query );
	}
	
	public void updateBookNameById( int id, String newName ) throws SQLException {
		String query = "update " + TABLE_NAME + " set name='" + newName + "' where id="+ id;
		stmt.executeUpdate( query );
	}

	public List<Book> findAllOrderByName() throws SQLException {
		List<Book> books = new ArrayList<Book>();
		String query = "select id, name, author from " + TABLE_NAME + " order by name";
		ResultSet rs = stmt.executeQuery( query );
		while( rs.next() ) {
			Book book = new Book( rs.getInt("id"), rs.getString("name"), rs.getString("author") );
			books.add( book );
		}
		return books;
	}
	public void closeConnection() {
		try {
			if (stmt != null)
				conn.close();
		} catch (SQLException se) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	private static String creatingDefault() {
		String query = "insert into " + TABLE_NAME + " VALUES (" 
				+ rnd.nextInt() + ", 'We were liars', 'E. Lockhart' ), (" 
				+ rnd.nextInt() + ", 'Harry Potter', 'J. Roaling' ), ("
				+ rnd.nextInt() + ", 'The Valley of the Moon', 'J. London' )";
		return query;
	}

	public MySQLConnection() {
		super();
	}
}

package lib;

import java.sql.SQLException;

public interface Library {
 void addBook() throws SQLException;
 void BarrowBook()throws SQLException;
 void returnBook() throws SQLException;
 void addMember ()throws SQLException;
//void  menuMethod() throws SQLException;

}


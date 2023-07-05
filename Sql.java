package lib;

import com.mysql.cj.xdevapi.Column;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Sql {
    static Connection con;
    static Statement stmt;
    static ArrayList<String> list = new ArrayList<>();
    static ArrayList<String> barrowlist = new ArrayList<>();
    private  String Updatebrr[] = {"barrowStudent", "barrowStaff"};

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1818");
            stmt = con.createStatement();
            list.add("member");
            list.add("staff");
            barrowlist.add("studentBarrow");
            barrowlist.add("staffBarrow");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    String barrowBookDetails[]={"barrowBookDetailsStudents","barrowBookDetailsStaff"};
    static   ArrayList<String> department = new ArrayList<>();
    static {
        ResultSet rs1 = null;
        try {
            rs1 = stmt.executeQuery("select * from section");

            while (rs1.next()) {
                department.add(rs1.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    Scanner sc = new Scanner(System.in);
    protected int checkInput(){
        Scanner sc=new Scanner(System.in);
        int Id=0;
        try {
            System.out.println("Enter The User Id :");
            Id = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Id");
            checkInput();
        }
        return Id;
    }

    public static void BookColumnName() {
        System.out.printf("%50s", "BookId");
        System.out.printf("%50s", "BookName");
        System.out.printf("%50s", "Author");
        System.out.printf("%50s", "PublishYear");
        System.out.printf("%50s", "Department");
        System.out.printf("%50s", "Status\n");

    }

    void newSection(String secName, String seccode, int code) throws SQLException {
        String query = "insert into section(department,depcode,code) values('" + secName + "','" + seccode + "','" + code + "')";
        stmt.executeUpdate(query);
        System.out.println("Branch Successfully Added");
    }

    public String showDepartment() throws SQLException {
        System.out.println("Press 0 Home :");
        ResultSet rs = stmt.executeQuery("select * from section");
        int cunt = 1;
        String dep = "";
        while (rs.next()) {
            for (int i = 1; i < 2; i++) {
                dep = rs.getString("department");
                System.out.println("Press " + cunt +" " +dep +" Branch");
            }
            cunt++;
        }

        int choice2 = sc.nextInt();
        if(choice2==0) {
            Books books=new Books();
            books.menuMethod();
        }

        if(choice2<=cunt) {
            ResultSet rs1 = stmt.executeQuery("select * from section where number='" + choice2 + "'");
            while (rs1.next()) {
                for (int i = 1; i < 2; i++)
                    dep = rs1.getString("department");
            }
        }

        else{
            System.out.println("Invalid Key");
            showDepartment();
        }
        return dep;

    }

    protected int bookIdGenerate(String dep, int i) throws SQLException {
        int id1 = 100;
        try {
            ResultSet set = stmt.executeQuery("select * from  book  where type='" + dep + "'");
            while (set.next())
                id1++;
        } catch (SQLIntegrityConstraintViolationException e) {
            id1 = 100;
        }
        return id1;
    }

    int depCode = 0;
    private String sections(String type) throws SQLException {

        String id = "";
        ResultSet set = stmt.executeQuery("select * from section where department='" + type + "'");
        while (set.next()) {
            for (int i = 1; i <= 2; i++) {
                id = set.getString("depcode");
                depCode = set.getInt("code");
            }
        }
        return id;
    }
    protected void addBook(int id1, String title, String author, int publishYear, String type, float price) throws SQLException {
        String status = "Available";
        ResultSet rs = null;
        String type1 = sections(type);
        String id = type1 + id1;

        PreparedStatement stmt1 = con.prepareStatement("insert into book values(?,?,?,?,?,?,?)");
        stmt1.setString(1, id);
        stmt1.setString(2, title);
        stmt1.setString(3, author);
        stmt1.setInt(4, publishYear);
        stmt1.setString(5, type);
        stmt1.setString(6, status);
        stmt1.setFloat(7, price);
        stmt1.executeUpdate();
        System.out.println("Book Successfully Added\n");

    }

    protected void MemberShip(int rollNum, String name, String department, int year, String moblieNumber, String mailId) throws SQLException {

        try {
            PreparedStatement pstmt = con.prepareStatement(""
                    + "insert into member(id,name,department,year,phoneNumber,mailId)values(?,?,?,?,?,?) ");
            pstmt.setInt(1, rollNum);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setInt(4, year);
            pstmt.setString(5, moblieNumber);
            pstmt.setString(6, mailId);
            pstmt.executeUpdate();

            System.out.println("Successfully Added");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("This Id Is Already Existing");
        }
    }

    protected void MemberShip(String name, String department, String moblieNumber, String mailId) throws SQLException {
        sections(department);
        String count = String.valueOf(bookIdGenerate(department, 1));
        String id = depCode + count;

        try {
            PreparedStatement pstmt = con.prepareStatement(""
                    + "insert into staff(Id,name,department,phoneNumber,mailId)values(?,?,?,?,?) ");
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setString(4, moblieNumber);
            pstmt.setString(5, mailId);
            pstmt.executeUpdate();

            System.out.println("successfully Added");
            System.out.println("Staff UserId :"+id);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("This Id Is Already Existing");
        }
    }
    protected  void showBookDetails() throws SQLException {
        System.out.println("");
        System.out.println("************************************************************************");
        System.out.printf("%15s","Section");System.out.printf("%15s","Available");System.out.printf("%15s","NotAvailable");System.out.printf("%15s","Total\n");
        System.out.println("************************************************************************\n");

        int sum=0;
        for(int i=0;i<department.size();i++){
            System.out.printf("%15s",department.get(i));
            ResultSet rs = null;int total=0;
            for(int j=1;j<=2;j++){
                if(j==1)
                    rs=stmt.executeQuery("select * from book where type='"+department.get(i)+"' AND status='Available'");
                if(j==2)
                    rs=stmt.executeQuery("select * from book where type='"+department.get(i)+"' AND status='NotAvailable'");
                int count=0;
                while (rs.next())
                    count++;
                System.out.printf("%15s",count);
                total+=count;
            }
            sum+=total;
            System.out.printf("%15s",total);

            System.out.println();

        }
        System.out.printf("%15s","TOTAL");
        System.out.printf("%46s",sum+"\n");
        System.out.println("************************************************************************\n");
    }
    private void Column() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        barrowColumnName();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }
    static void Column1(){
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        BookColumnName();
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }
    static void loop(ResultSet rs, int end) throws SQLException {
        int count=0;
        while (rs.next()) {
            for (int i = 1; i <= end; i++) {
                System.out.printf("%50s", rs.getString(i));
            }
            System.out.println("\n");
        }
        if(count==0)
           // System.out.println(" NO Books\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    private  void loop(ResultSet rs,int end,Boolean b) throws SQLException {

        while (b) {

            for (int i = 1; i <= end; i++) {
                System.out.printf("%50s", rs.getString(i));
            }
            System.out.println("\n");
            b=rs.next();
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    public void showAllBooks() throws SQLException {

        for (int j = 0; j < department.size(); j++) {
            System.out.println("Section :" + department.get(j) + "\n");

            ResultSet rs = stmt.executeQuery("select * from book where type='" + department.get(j) + "'");
            boolean b=rs.next();
            if(b) {
                Column1();
                loop(rs, 6,b);
            }else System.out.println("\tNo Books\n");
        }
    }

    protected void BookFiltering() throws SQLException {

        String dep = showDepartment();
        ResultSet rs = null;

        rs = stmt.executeQuery("select * from book where type='" + dep + "'");
        boolean b=rs.next();
        Column1();
        loop(rs, 6,b);
    }
    private void choice1(int x) throws SQLException{
        Books books=new Books();
        int op1=books.inputMismatch("Enter 1 for back \nEnter 2 for Main Menu:\nEnter 3 for exit: ");
        if(op1==1)
            choice(x);
        else if(op1==2){

            books.menuMethod();
        }else if(op1==3)
            System.exit(0);
        else
            System.out.println("Invalid Key");
    }

    protected void choice(int x) throws SQLException {
        Books books=new Books();

        int op = books.inputMismatch("Press 1 Show All Books:\nPress 2 Branch Wise:\nPress 3 Back :");

        switch (op) {
            case 1:
                showAllBooks();
                if(x==1) {
                    showBookDetails();
                    choice1(x);
                }
                break;
            case 2:
                BookFiltering();
                if(x==1)
                    choice1(x);
                break;
            case 3:
                if(x==1){
                    // Books books=new Books();
                    books.menuMethod();
                }
                if(x==2){
                    //  Books books=new Books();
                    books.menuMethod();
                }

                break;
            default:
                System.out.println("Invalid Key");
                choice(x);
                break;
        }
    }

    protected void barrow() throws SQLException {
        boolean boo;
        int arr[] = {4, 10};
        Books books=new Books();

        int option = books.inputMismatch("Press 0 Student :\nPress 1 Staff :\nPress 2 Back :");
        choice(2);

        if(option<=1){
            int check=1;
            while(check==1){

                int Id = checkInput();

                int continue1 =1;
                while (continue1==1){
                    ResultSet rs = stmt.executeQuery("select * from " + list.get(option) + " where id='" + Id + "'");

                    if (rs.next()) {
                        check=2;
                        ResultSet rs2 = stmt.executeQuery("select * from " + barrowlist.get(option) + " where id='" + Id + "'");
                        int count = 0;
                        while (rs2.next()) {
                            count++;
                        }
                        if (count < arr[option]) {
                            System.out.println("Enter The Book Id:");
                            String bookId = sc.next();
                            ResultSet rs1 = stmt.executeQuery("select * from book where Id='" + bookId + "' AND status='Available'");
                            boolean bool = rs1.next();
                            if (bool) {
                                Column();
                                loop(rs1,6,bool);

                                int ch = books.inputMismatch("Conformation \nPress 1 Yes :\nPress 2 No : ");
                                if (ch == 1)
                                    conformation(Id, bookId, option);
                                else if (ch == 2) {
                                    System.out.println();
                                } else
                                    System.out.println("Invalid Key");

                                int in=books.inputMismatch("Press 1 Add Book:\nPress 2 Back\nPress 3 Home :");
                                if(in==1){
                                    continue1=1;
                                } else if (in==2) {
                                    continue1=0;
                                    barrow();

                                } else if(in==3){
                                    continue1=0;
                                    // Books books=new Books();
                                    books.menuMethod();
                                }
                                else {
                                    continue1=0;
                                    System.out.println("Invalid Key");
                                }
                            } else {
                                System.out.println("Invalid Book ID or Unavailable Book\n");
                            }

                        } else {
                            continue1=0;
                            System.out.println("per Id 4 books only\n");
                            Column();
                            ResultSet rs3 = stmt.executeQuery("select * from "+barrowBookDetails[option]+" where id='"+Id+"'");
                            loop(rs3,7);

                        }
                    } else {
                        continue1=0;
                        System.out.println("Invalid Id\n");
                    }}

            }} else if (option==3) {
            //   Books books=new Books();
            books.menuMethod();
        }else {
            System.out.println("Invalid Key");
            barrow();
        }

    }


    public void barrowColumnName() {
        System.out.printf("%50s", "StudentName");
        System.out.printf("%50s", "StudentId");
        System.out.printf("%50s", "BookId");
        System.out.printf("%50s", "Date");
        System.out.printf("%50s", "ReturnDate");
        System.out.printf("%50s", "BookName");
        System.out.printf("%50s", "Department\n");

    }
    public void conformation(int Id, String bookId, int op) throws SQLException {
        int arr[] = {10, 15};

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        stmt.executeUpdate("insert into " + barrowlist.get(op) + " values"
                + "('" + Id + "','" + bookId + "','" + date + "','" + time + "','" + date.plusDays(arr[op]) + "')");
        stmt.executeUpdate("insert into " + Updatebrr[op] + " SELECT "+list.get(op)+".name, " + barrowlist.get(op) + ".Id," + barrowlist.get(op) + ".bookId," + barrowlist.get(op) + ".date," + barrowlist.get(op) + ".returnDate,book.title,"+list.get(op)+".department FROM ((" + barrowlist.get(op) + " INNER JOIN book ON " + barrowlist.get(op) + ".bookId = book.id) INNER JOIN "+list.get(op)+" ON " + barrowlist.get(op) + ".Id = "+list.get(op)+".id)");

        stmt.executeUpdate("insert into " + barrowBookDetails[op] + " SELECT "+list.get(op)+".name, " + barrowlist.get(op) + ".Id," + barrowlist.get(op) + ".bookId," + barrowlist.get(op) + ".date," + barrowlist.get(op) + ".returnDate,book.title,"+list.get(op)+".department FROM ((" + barrowlist.get(op) + " INNER JOIN book ON " + barrowlist.get(op) + ".bookId = book.id) INNER JOIN "+list.get(op)+" ON " + barrowlist.get(op) + ".Id = "+list.get(op)+".id)");

        stmt.executeUpdate("UPDATE book SET status = 'NotAvailable' WHERE id='" + bookId + "'");

        System.out.println("Book Return Date :" + date.plusDays(10));


    }

    /*  private*/ String retrun[] = {"studentReturn", "staffReturn"};

    public void ReturnBook() throws SQLException {
        Books books=new Books();
        int option =books.inputMismatch("Press 0 Student :\nPress 1 Staff :\nPress 2 Back :");
        if(option<=1) {

            int continue1 = 1;
            while (continue1 == 1) {

                int Id =checkInput();

                int check=1;
                while(check==1){
                    ResultSet rs1= stmt.executeQuery("select * from " + list.get(option) + " where id='" + Id + "'");
                    boolean b1=rs1.next();
                    if (b1) {
                        continue1=0;
                        ResultSet rs3 = stmt.executeQuery("select * from " + barrowBookDetails[option] + " where id= '" + Id+"'" );
                        Column();
                        loop(rs3,7);

                        System.out.println("Enter The Book Id: ");
                        String bookId = sc.next();
                        ResultSet rs = stmt.executeQuery("select * from " + barrowBookDetails[option] + " where id='" + Id + "' AND bookId='" + bookId + "'");

                        boolean b=rs.next();
                        if (b) {
                            Date returnDate1 = null;

                            while(b){

                                for(int i=1;i<=7;i++){
                                    returnDate1 = rs.getDate("returnDate");
                                }
                                b=rs.next();
                            }
                            // returnDate1=rs.getDate("returnDate");
                            LocalDate date1 = LocalDate.now()/*.plusDays(11)*/;

                            Date date = Date.valueOf(date1);
                            //  System.out.println(date + "   " + returnDate1);
                            if (date.equals(returnDate1) || date.before(returnDate1)) {
                                returnUpdate(option, Id, bookId, date);
                            } else {
                                long difference_In_Time = date.getTime() - returnDate1.getTime();
                                long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
                                System.out.println("Fine Amount :" + difference_In_Days);
                                System.out.println();
                                int fine = books.inputMismatch("Enter The Fine Amount:");
                                if (fine == difference_In_Days)
                                    returnUpdate(option, Id, bookId, date);
                            }

                        }
                    }
                    else
                        System.out.println("Invalid Id");

                    int op1=books.inputMismatch("Press 1 Return\nPress 2 Back\nPress 3 Home");
                    if(op1==1)
                        check=1;
                    else if(op1==2){
                        ReturnBook();}
                    else if(op1==3) {
                        // Books books = new Books();
                        books.menuMethod();
                    }

                }}

        }
        else if(option==2){
            // Books books=new Books();
            books.menuMethod();
        }
        else{
            System.out.println("Invalid Key");
        }

    }
    private void returnUpdate(int option, int Id, String bookId, Date returnDate1) throws SQLException {
        stmt.executeUpdate("UPDATE " + barrowlist.get(option) + " SET returnDate = '" + returnDate1 + "'  WHERE bookId='" + bookId + "' AND id='" + Id + "'");
        stmt.executeUpdate("insert into " + retrun[option] + " SELECT "+list.get(option)+".name, " + barrowlist.get(option) + ".Id," + barrowlist.get(option) + ".bookId," + barrowlist.get(option) + ".date," + barrowlist.get(option) + ".returnDate,book.title,"+list.get(option)+".department FROM ((" + barrowlist.get(option) + " INNER JOIN book ON " + barrowlist.get(option) + ".bookId = book.id) INNER JOIN "+list.get(option)+" ON " + barrowlist.get(option) + ".Id = "+list.get(option)+".id)");
        stmt.executeUpdate("delete from " + barrowlist.get(option) + " where Id='" + Id + "' AND bookId='" + bookId + "'");
        stmt.executeUpdate("delete from " + Updatebrr[option] + " where Id='" + Id + "' AND bookId='" + bookId + "'");
        stmt.executeUpdate("delete from " + barrowBookDetails[option] + " where Id='" + Id + "' AND bookId='" + bookId + "'");

        stmt.executeUpdate("UPDATE book SET status = 'Available' WHERE id='" + bookId + "'");
        System.out.println("Successfully Returned");

    }

    protected void memberDetails() throws SQLException {
        Books books=new Books();

        int op = books.inputMismatch("Press 0 Student\nPress 1 Staff");
        int cont=1;
        while(cont==1) {

            int Id = checkInput();
            ResultSet rs = null;

            for (int j = 1; j <= 2; j++) {
                ResultSet set = stmt.executeQuery("select * from " + list.get(op) + " where id='" + Id + "'");
                if (set.next()) {
                    try {
                        if (j == 1) {
                            System.out.println("History :");
                            rs = stmt.executeQuery("select * from " + retrun[op] + " where Id='" + Id + "'");
                        }
                        if (j == 2) {
                            System.out.println("Borrow Books : ");
                            rs = stmt.executeQuery("select * from " + barrowBookDetails[op] + " where Id='" + Id + "'");
                        }
                        boolean b=rs.next();
                        if(b) {
                            Column();
                            loop(rs, 7, b);
                        }else System.out.println("\t No Books\n");
                    } catch (SQLSyntaxErrorException e) {

                        System.out.println("\t\t\t\tNo Books\n");
                    }
                } else {
                    System.out.println("Invalid Id");
                    memberDetails();
                }
            }
            Boolean b1=true;
            while( b1==true){

                int ch =books.inputMismatch("Press 1 Continue\nPress 2 Back\nPress 3 Home");
                if (ch == 1){
                    cont = 1;b1=false;}
                else if (ch == 2){
                    memberDetails();b1=false;}
                else if (ch == 3) {
                    //  Books books = new Books();
                    books.menuMethod();b1=false;
                } else {
                    System.out.println("Invalid Key");
                }}
        }

    }
    protected  void DeleteMember() throws SQLException {
        Books books=new Books();

        int op = books.inputMismatch("Press 0 Student :\nPress 1 Staff :");
        int Id =checkInput();

        ResultSet set= stmt.executeQuery("select * from " + list.get(op) + " where id='" + Id + "'");
        if (set.next()) {
            ResultSet rs = stmt.executeQuery("select * from " + barrowBookDetails[op] + " where id='" + Id + "'");
            int count = 0;
            while (rs.next())
                count++;
            System.out.println(count);
            if (count == 0) {
                stmt.executeUpdate("delete from " + list.get(op) + " where id='" + Id + "'");
                System.out.println("Successfully Deleted");
            } else {
                System.out.println("Borrow Books");
                rs = stmt.executeQuery("select * from " + barrowBookDetails[op] + " where Id='" + Id + "'");
               boolean b=rs.next();
               if(b==true){
                Column();
                loop(rs,7,b);}
               else
                   System.out.println("\tNo Books\n");

            }
        } else {
            System.out.println("Invalid Id\n");

        }
    }

    protected void DeleteBook() throws SQLException {

        System.out.println("Enter The Book Id :");
        String Id =sc.next();
        ResultSet set= stmt.executeQuery("select * from book where id='" + Id + "'");
        Boolean booo= set.next();
        if (booo) {
            BookColumnName();
            loop(set,7,booo);
            Books books=new Books();
            int conform=books.inputMismatch("Conformation\nPress 1 yes\nPress 2 No");
            if(conform==1) {
                stmt.executeUpdate("delete from book where id='" + Id + "'");
                System.out.println("Successfully Deleted");
            }
            else
                System.out.println();

        } else {
            System.out.println("Invalid Book Id");
            DeleteBook();
        }
    }
}

package lib;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Books extends Users implements Library  {
   static Scanner sc=new Scanner(System.in);
    Sql sql=new Sql();

    public void exit() {
        System.exit(0);
    }
    public void options() {
        System.out.println("\t\t---------------------------------------");
        System.out.println("\n\t\t\t\tPress 1 ADD BOOK"	+ "\n\t\t\t\tPress 2 New Membership \n\t\t\t\tPress 3 Show Book \n"
                	+ "\t\t\t\tPress 4 Borrower's"	+
                "\n\t\t\t\tPress 5 Return\n\t\t\t\tPress 6 More Details\n");
        System.out.println("\t\t---------------------------------------");
    }
    protected int inputMismatch(String str){
        Scanner sc1=new Scanner(System.in);
        int id=0;
        try{
            System.out.println(str);
            id=sc1.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Invalid Key");
            inputMismatch(str);
        }
        return id;
    }
    public  void menuMethod() throws SQLException {
        options();

        while(true) {
            int choice=0,cho=0;
            while(cho==0){
                Scanner sc=new Scanner(System.in);
                cho=1;
                try {
                    choice = sc.nextInt();

                }catch(InputMismatchException e){
                    cho=0;
                    System.out.println("Invalid Key\n");
                  options();
                }
            }


            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                   addMember();
                    break;
                case 3:
                    sql.choice(1);
                    break;
                case 4:
                    BarrowBook();
                    break;
                case 5:
                  ReturnBooks();
                    break;
                case 6:
                    int cont=1;
                        String str="Enter 1 for Member Details\nEnter 2 for Delete Member\nPress 3 Delete Book";
                        int choice1 = inputMismatch(str);
                    while(cont==1) {
                        if (choice1 == 1)
                            sql.memberDetails();
                        else if (choice1 == 2)
                            sql.DeleteMember();
                        else if (choice1 == 3) {
                            sql.DeleteBook();
                           String str1="Press 1 Continue\nPress 2 Back\nPress 3 Home";
                            int in = inputMismatch(str1);
                            if (in == 1)
                                sql.DeleteBook();
                            else if (in == 2) {
                                 cont=0;
                            } else if (in==3) {
                                menuMethod();
                            } else System.out.println("Invalid Key");
                        } else
                            System.out.println("Invalid Key");
                    }
                    break;
                default:
                    System.out.println("Invalid choice");
                    menuMethod();
                    break;
            }
        }

    }
    private void ReturnBooks() throws SQLException {

        returnBook();

        int option1=inputMismatch("Enter 1 for back:\nEnter 2 for mainMenu: \nEnter 3 for exit:");
        if(option1==1)
            ReturnBooks();
        else if(option1==2)
            menuMethod();
        else
            exit();
    }
    private void AllBook() throws SQLException {
        sql.showAllBooks();
        boolean bool=true;
          sql. showBookDetails();

                int op1=inputMismatch("enter 1 for back:\nEnter 2 for exit:");
                if(op1==1) {
                    menuMethod();
                }
                else
                    exit();
    }
    private  void filtering() throws SQLException {
        sql. BookFiltering();

        int op=inputMismatch("Press 1 Back:\nPress 2 Home\nPress 3 END :");
        if(op==1)
            sql.BookFiltering();
        if(op==2){
            menuMethod();
        }
        else
            exit();
    }
    @Override
    public void addBook() throws SQLException {

    int op=inputMismatch("Press 1 Add Existing Branch\nPress 2  Add New Branch\nPress 3 Back");
    if(op==1) {
        ExistingSection();
    }else if(op==2){
        System.out.println("Enter The New Branch Name :");
        String secName=sc.next();
        System.out.println("Enter The Department Code(ex:EC,CS,ME..) :");
        String seccode=sc.next();
        int code=inputMismatch("Enter The Branch Code(ex:100,200,..) :");
        sql. newSection(secName,seccode,code);
        ExistingSection();
    }else if(op==3){
        menuMethod();
    }
    else {
        System.out.println("Invalid choice...");
        addBook();
    }
}
    private void ExistingSection() throws SQLException {
        String dep=sql.showDepartment();
        System.out.println("How Many Books You Want To Add?");
        int add=sc.nextInt();
        for(int i=1;i<=add;i++) {
            System.out.println("Enter The Book Name :");
            String title = sc.next();
            System.out.println("Enter The Author Name :");
            sc.nextLine();
            String author = sc.next();
            int year = inputMismatch("Enter The Publish Year :");

            float price = inputMismatch("Enter The Book Price :");
            int id1 = sql.bookIdGenerate(dep,0);
            sql.addBook(id1, title, author, year, dep, price);
        }
        int i=1;
        while(i==1){
            i=0;

        int ch=inputMismatch("Press 1 Back\nPress 2 Home\nPress 3 END ");

        if(ch==1)
            ExistingSection();
        else if(ch==2)
          menuMethod();
        else if(ch==3)
            exit();
        else{
            System.out.println("Invalid Key");
            i=1;
        }
    }
    }

    @Override
    public void BarrowBook() throws SQLException {

        sql.barrow();

        int option=inputMismatch("Press 1 Back:\nPress 2 Home: \nPress 3 END :");
        if(option==1)
            BarrowBook();
        else if(option==2)
            menuMethod();
        else if(option==3)
            exit();
        else {
            System.out.println("Invalid Key\n");
            BarrowBook();
        }
    }

@Override
    public void returnBook() throws SQLException {
       sql.ReturnBook();
}

    public static void main(String[] args) throws SQLException {

        Books book=new Books();
        System.out.println("\t\t\t\t\tWELCOME TO PODHIGAI LIBRARY");
        boolean signIn=false;
        book.SignIn();

            book.menuMethod();
    }


}












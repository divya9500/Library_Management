package lib;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Users {
      Sql sql=new Sql();
      Scanner sc=new Scanner(System.in);
    protected void SignIn() {
        String userId = "admin123", password = "12345";
        boolean signIn = false;
        while (signIn == false) {

            System.out.println("\nEnter Admin User ID : ");
            String userId1 = sc.next();
            if ((userId.equalsIgnoreCase(userId1))) {
                while(signIn==false) {
                    System.out.println("Enter Admin Password : ");
                    String password1 = sc.next();
                    if (password.equals(password1))
                        signIn = true;
                    else
                        System.out.println("oops!,enter correct  password:");
                }
            } else
                System.out.println("oops!,enter correct user id :");

        }
    }
    public void addMember() throws SQLException {
        int rollNum = 0, year = 0;
        String moblieNumber = null, mailId = null, name = null, department = null;
        Books books1=new Books();
        int ch =books1.inputMismatch("Press 1 Student :\nPress 2 Staff :\nPress 3 Back:") ;
        int continue1 = 1;
        while (continue1 == 1) {
            if (ch == 1) {
                      //  System.out.println("Enter Student RollNumber");
                        rollNum = sql.checkInput();
            }
            if (ch <= 2) {
                System.out.println("Enter User Name :");
                name = sc.next();
                sc.nextLine();

                System.out.println("Enter The Department (ex:CSE,ECE):");
                department = sc.next();
                if (ch == 1) {
                    boolean bo=true;
                    while(bo==true){
                        try{

                    year = books1.inputMismatch("Enter The Year (ex: 1,2,3,4)");
                        bo=false;
                        }
                        catch (InputMismatchException e){
                            System.out.println("Invalid Key");
                        }
                }}
                boolean bool = true;
                System.out.println("Enter The Mobile Number");
                moblieNumber = moblieNumber(2);
                System.out.println("Enter the E-Mail ID :");
                mailId = moblieNumber(1);
            }
            if (ch == 1)
                sql.MemberShip(rollNum, name, department, year, moblieNumber, mailId);

            else if (ch == 2)
                sql.MemberShip(name, department, moblieNumber, mailId);
            else if (ch == 3) {
                Books books = new Books();
                books.menuMethod();
            } else {
                System.out.println("Invalid Key");
                addMember();
            }
int bb=1;
            while(bb==1){
                bb=0;
                System.out.println();
                int ch1 = books1.inputMismatch("Press 1 Add\nPress 2 Back\nPress 3 Home");
                if (ch1 == 1) {
                    continue1 = 1;

                } else if (ch1 == 2) {
                    continue1 = 0;
                    addMember();

                } else if (ch1 == 3) {
                    Books books = new Books();
                    books.menuMethod();

                } else{
                    System.out.println("Invalid Key");
                    bb=1;
            }
        }
        }
    }


    protected String moblieNumber(int ch){
        String  moblieNumber1 = null,str="";
        boolean bool=true;Pattern pattern = null;
        while(bool==true) {
            moblieNumber1 = sc.next();
            if(ch==1){
                pattern = Pattern.compile("^(.+)@(.+)$"); str=" Mail Id ";}
            if(ch==2){
           pattern = Pattern.compile("(0|91)?[6-9][0-9]{9}");str =" phone number ";}
            Matcher matcher = pattern.matcher(moblieNumber1);
            if (matcher.matches())
                bool=false;
            else
                System.out.println("Given"+str+" is InValid \nplease,Enter valid"+str+":");
        }
      return  moblieNumber1;
    }


    private String mail(){
        String email;
        boolean bool=true;
        email=sc.next();
        while(bool==true){
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches())
                bool=false;
            else
                System.out.println("Given phone number is InValid \nplease,Enter valid mobile Number :");
        }
        return email;
    }


}








/*static String  moblieNumber1;
protected void moblieNumber(){
        boolean bool=true;
    while(true) {
       moblieNumber1 = sc.next();
        Pattern pattern = Pattern.compile("(0|91)?[6-9][0-9]{9}");
        Matcher matcher = pattern.matcher(moblieNumber1);
        if (matcher.matches())
            bool=false;
        else
            System.out.println("Given phone number is InValid \nplease,Enter valid mobile Number :");
    }

}


*/

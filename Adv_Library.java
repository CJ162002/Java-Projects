import java.util.HashMap;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Set;
import java.lang.Exception;

class Library_Store {
TreeMap<String,String> lib = new TreeMap<>();
TreeMap<String,String> issued = new TreeMap<>();
Set<String> keys = lib.keySet();
Set<String> issue = issued.keySet();
public Library_Store() {
lib.put("Book1","A");
lib.put("Book2","B");
lib.put("Book3","C");
lib.put("Book4","D");
}
public void addBook(String a, String b) {
lib.put(a,b);
System.out.println("Book has been added successfully..");
}
public void getBooks() {
for(String k : keys) {
System.out.println(k+" by "+lib.get(k));
}
}
public void issueBook(String o,String m,String use) {
int flag = 0;
for(String s : keys) {
if(o.equals(s) && m.equals(lib.get(s))) {
issued.put(o,use);
flag = 1;
lib.remove(s);
break;
}
}
if(flag==1) {
System.out.println("Book issued successfully..");
}else {
System.out.println("Book not available..");   
}
}
public void returnBook(String p,String q,String r) {
int fl = 0;
for(String i : issue) {
if(q.equals(i) && p.equals(issued.get(i))) {
lib.put(q,r);
fl = 1;
issued.remove(i);
break;
}
}
if(fl==1) {
System.out.println("Book returned successfully..");
}else {
System.out.println("Book not issued..");   
}
}
public void showIssuedBook() {
System.out.println("Book"+"\t\t"+"User");
for(String st : issue) {
System.out.println(st+"\t\t"+issued.get(st));
}
}
}

class User {
TreeMap<String,Integer> map = new TreeMap<>();
Set<String> key = map.keySet();
public User() {
map.put("Chinmay",1234);
map.put("Admin",9999);
map.put("Pratik",9876);
map.put("Dipti",1111);
}
public boolean authenticate(String un,int p) {
if(un==null || p==-1) {
System.out.println("Please check the enter Username and Password is correct");
} else {
for(String k : key) {
if(un.equals(k) && p == map.get(k)) {
return true;
}
}
}
return false;
}
public void addUser(String new_user,int new_pass) {
map.put(new_user,new_pass);
}
public void showUser() {
System.out.println("User Name\tPassword");
for(String uname : key) {
System.out.println(uname+"\t\t"+map.get(uname));
}
}
}

public class Adv_Library {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
Library_Store ls = new Library_Store();
User us = new User();
String m,n,o;
Boolean x = true;
Boolean logout = false;

System.out.println("Available books in the Library are :");
ls.getBooks();
while(x) {
System.out.println("\nPlease verify your identity :");
System.out.print("Enter your name : ");
String name = sc.nextLine();
System.out.print("Enter your Password : ");
int pass = sc.nextInt();
sc.nextLine();
if(us.authenticate(name,pass)) {
logout = true;
}else {
System.out.println("Please check the Username and Password!!");
}
while(logout) {
if(name.equals("Admin")) {
System.out.println("1.Add Book\t\t2.Issue Book \t3.Return Book\n4.Updated List of Books\t5.Logout\t6.Exit\n7.Issued Books Table\t8.Add User\t9.View User List");
} else {
System.out.println("1.Add Book\t\t2.Issue Book \t3.Return Book\n4.Updated List of Books\t5.Logout\t6.Exit");
}
try {
int num = sc.nextInt();
sc.nextLine();
switch(num) {
case 1 :
System.out.println("Enter your Book name :");
m = sc.nextLine();
System.out.println("Enter your Author name :");
o = sc.nextLine();
ls.addBook(m,o);
break;
case 2 :
System.out.println("Enter your Book name :");
m = sc.nextLine();
System.out.println("Enter your Author name :");
o = sc.nextLine();
ls.issueBook(m,o,name);
break;
case 3 :
System.out.println("Enter name who has issued :");
m = sc.nextLine();
System.out.println("Enter your Book name :");
n = sc.nextLine();
System.out.println("Enter your Author name :");
o = sc.nextLine();
ls.returnBook(m,n,o);
break;
case 4 :
ls.getBooks();
break;
case 5 :
logout=false;
break;
case 6 :
x = false;
logout=false;
System.out.println("Please visit again...");
break;
case 7 :
if(name.equals("Admin")) {
ls.showIssuedBook();
} else {
System.out.println("You are not Admin...");
}
break;
case 8 :
System.out.println("Enter your User name :");
m = sc.nextLine();
System.out.println("Enter your Password :");
int om = sc.nextInt();
us.addUser(m,om);
break;
case 9 :
us.showUser();
break;
default :
System.out.println("Please verify your choice !!");
}
}catch (Exception e) {
    System.out.println("Invalid input. Please enter an integer.");
    sc.nextLine();
}
}
}
}
}
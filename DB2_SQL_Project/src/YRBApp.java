//Test Class
import java.util.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.lang.*;
import java.io.*;
import java.sql.*;


public class YRBApp {
//created GIT repository
	
	private Scanner scanner; //= new Scanner(System.in);
	private Connection conDB;   // Connection to the database system.
    private String url;         // URL: Which database?
    private String userStringInput;
    private String userIntInput;
    private int custID;     // Who are we tallying?
    private String  custName;   // Name of that customer.
    private String custCity;	//Customer's City
    private int userChoice;		//User's choices in the menus
	
	public YRBApp (Scanner in){
		
		scanner = in;
		//inputCheck();
		
		
		// Set up the DB connection.
		          try {
		             // Register the driver with DriverManager.
		              Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		          } catch (ClassNotFoundException e) {
		              e.printStackTrace();
		              System.exit(0);
		          } catch (InstantiationException e) {
		              e.printStackTrace();
		              System.exit(0);
		          } catch (IllegalAccessException e) {
		              e.printStackTrace();
		              System.exit(0);
		          }
		  
		          // URL: Which database?
		          url = "jdbc:db2:c3421a";
		  
		          // Initialize the connection.
		          try {
		              // Connect with a fall-thru id & password
		              conDB = DriverManager.getConnection(url);
		          } catch(SQLException e) {
		              System.out.print("\nSQL: database connection error.\n");
		              System.out.println(e.toString());
		              System.exit(0);
		          }    
		  
		          // Let's have autocommit turned off.  No particular reason here.
		          try {
		              conDB.setAutoCommit(false);
		          } catch(SQLException e) {
		              System.out.print("\nFailed trying to turn autocommit off.\n");
		              e.printStackTrace();
		              System.exit(0);
		          }    
		          message();
		          userIntInput = scanner.nextLine();
		          while(!intInputCheck(userIntInput)){
		        	  userIntInput = scanner.nextLine();
		          };
		          custID = Integer.parseInt(userIntInput);
		          
		          while(!find_customer(custID)){
		        	  System.out.print("We are sorry, the customer ID you entered" +
		          "is not in our database. Please enter a valid customer ID: ");
		        	  
		        	  userIntInput = scanner.nextLine();
		        	  
		        	  while(!intInputCheck(userIntInput)){
			        	  userIntInput = scanner.nextLine();
			          		};
			          custID = Integer.parseInt(userIntInput);
		        	  }
		          //Found the customer
		          System.out.println("Would you like to update the customer info?"+
		          "Enter yes/no or y/n:");
		          userStringInput = scanner.nextLine().toLowerCase();
		          
		          while(!stringInputCheck(userStringInput)){
		        	  userStringInput = scanner.nextLine();
		        	  
		          }
		          
		          while(userStringInput.equals("yes")||userStringInput.equals("y")){
		        	  
		        	  
		        	  
		        	  
		          }
		          
		          
		          
		          // Who are we tallying?
		         /* if (args.length != 1) {
		              // Don't know what's wanted.  Bail.
		              System.out.println("\nUsage: java CustTotal cust#");
		              System.exit(0);
		          } else {
		              try {
		                  custID = new Integer(args[0]);
		              } catch (NumberFormatException e) {
		                  System.out.println("\nUsage: java CustTotal cust#");
		                  System.out.println("Provide an INT for the cust#.");
		                  System.exit(0);
		              }
		          }
		  */
		          // Is this custID for real?
		         /* if (!customerCheck()) {
		              System.out.print("There is no customer #");
		              System.out.print(custID);
		              System.out.println(" in the database.");
		              System.out.println("Bye.");
		              System.exit(0);
		              
		          }
		          */
		  
		          // Report total sales for this customer.
		          //reportSalesForCustomer();
		  
		          // Commit.  Okay, here nothing to commit really, but why not...
		          try {
		              conDB.commit();
		          } catch(SQLException e) {
		              System.out.print("\nFailed trying to commit.\n");
		              e.printStackTrace();
		              System.exit(0);
		         }    
		         // Close the connection.
		         try {
		             conDB.close();
		         } catch(SQLException e) {
		             System.out.print("\nFailed trying to close the connection.\n");
		             e.printStackTrace();
		             System.exit(0);
		         }    
		 
		     
	    
	}
	
	private void message(){ 
	System.out.print("Intro message. Welcome to the database." 
			+ "Let's start with entering your customer ID:");
	}
	
	private boolean stringInputCheck(String s){
		try{
		String temporary = s.toLowerCase();}
		catch(NullPointerException e){
			System.out.println("We are sorry, but you have reached the End of File."+
		"The program will end now. Please relaunch it to try again.");
			System.exit(0);
		}
		String temporary = s.toLowerCase();
		String output= "doesnt work";
		if (temporary.equals("yes")||temporary.equals("no")
				||temporary.equals("n")||temporary.equals("y")){
			output = temporary;
			return true;
		}
		else{
			System.out.println("You have entered an invalid answer."+
		"Kindly use yes/no or y/n as the inputs, and try again.");
			return false;
		}
	}
		
	private boolean intInputCheck(String a){
			try{
			int temp = Integer.parseInt(a);}
			catch(NumberFormatException e){
				System.out.println("We are sorry, but you have entered an invalid integer"+
			"The program requires integers as inputs. Please try again.");
				return false; 
				//System.exit(0);
			}
			//int temp = Integer.parseInt(s);
			/*String output= "doesnt work";
			if (temporary.equals("yes")||temporary.equals("no")
					||temporary.equals("n")||temporary.equals("y")){
				output = temporary;
				return true;
			}
			else{
				System.out.println("You have entered an invalid answer."+
			"Kindly use yes/no or y/n as the inputs, and try again");
				*/
			return true;
			}
		
		//System.out.println("Doesn't work");		
		//return false;
	
	
	public boolean find_customer(int input) {
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        ResultSet         answers   = null;   // A cursor.

        boolean           inDB      = false;  // Return.

        queryText =
            "SELECT name       "
          + "FROM yrb_customer "
          + "WHERE cid = ?     ";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setInt(1, custID);
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?
        try {
            if (answers.next()) {
                inDB = true;
                custID = answers.getInt("cid");
                custName = answers.getString("name");
                custCity = answers.getString("city");
                System.out.println("Customer ID: " + custID 
                		+ "       Customer Name: " + custName 
                		+ "        City: " + custCity);
                
            } else {
                inDB = false;
                custName = null;
            }
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch(SQLException e) {
            System.out.print("SQL#1 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch(SQLException e) {
            System.out.print("SQL#1 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        return inDB;
    }
	
	public void update_customer(int id){
		//Handling user's inputs
		System.out.println("At this time, you can only update your Name or City"+
				"What would you like to update? Kindly enter: \n" +
				"1 - to update your Name \n" +
				"2 - to update your City \n" +
				"3 - to Exit");
		userIntInput = scanner.nextLine();
		 while(!intInputCheck(userIntInput)){
       	  userIntInput = scanner.nextLine();
         };
         userChoice = Integer.parseInt(userIntInput);
         
         while(!(userChoice!=1||userChoice!=2||userChoice!=3)){
        	 System.out.println("You have entered an invalid selection."+
        		"What would you like to update? Kindly enter: \n" +
				"1 - to update your Name \n" +
				"2 - to update your City \n" +
				"3 - to Exit");
        	 userIntInput = scanner.nextLine();
    		 while(!intInputCheck(userIntInput)){
           	  userIntInput = scanner.nextLine();
             };
             userChoice = Integer.parseInt(userIntInput);
        	 
         }
       //Now finally execute the query
         if (userChoice == 1){
        	 System.out.print("Please enter your new Name:");
        	 userStringInput = scanner.nextLine();
	          
	          while(!stringInputCheck(userStringInput)){
	        	  userStringInput = scanner.nextLine();
	        	  
	          }
        	 
        	 
        	 //if(!updateCustomerName){
        		 
        	 
         }
          
         
	}
	
	private boolean updateCustomerName(int id, String name){
		//Now finally execute the query 
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        //ResultSet         answers   = null;   // A cursor.

        boolean           updateDB      = false;  // Return.

        queryText =
            "UPDATE yrb_customer       "
          + "SET custName = ?"
          + "WHERE cid = ?     ";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, name);
            querySt.setInt(2, id);
            querySt.executeQuery();
            System.out.print("Update Successful!");
            updateDB = true;
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }


        // We're done with the handle.
        try {
            querySt.close();
        } catch(SQLException e) {
            System.out.print("SQL#1 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
        
        return updateDB;
		
	}
	
	
	
	
	
	
	
	
}

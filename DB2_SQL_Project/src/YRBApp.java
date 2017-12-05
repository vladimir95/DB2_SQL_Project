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
    private Map<Integer,String> categories = new TreeMap<Integer,String>(); //categories
    private Map<Integer,String> bookTitles = new TreeMap<Integer,String>(); //book titles in this category
    private int categoryChosen;
    private int titleNumberChosen;
    private Map<Integer,ArrayList<String>> bookInformation = new TreeMap<Integer,ArrayList<String>>();
    private int bookNumberChosen;
	
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
		          /*try {
		              conDB.setAutoCommit(false);
		          } catch(SQLException e) {
		              System.out.print("\nFailed trying to turn autocommit off.\n");
		              e.printStackTrace();
		              System.exit(0);
		          } */   
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
		          //Found the customer, time to ask for updates
		          System.out.println("Before we show you the list of book categories,"+
		          "would you like to update the customer info?"+
		          "Enter yes/no or y/n:");
		          userStringInput = scanner.nextLine().toLowerCase();
		          
		          while(!yesNoInputCheck(userStringInput)){
		        	  userStringInput = scanner.nextLine();
		        	  
		          }
		          
		          while(userStringInput.equals("yes")||userStringInput.equals("y")){
		        	  update_customer(custID);
		        	  userStringInput = scanner.nextLine();
		        	  while(!yesNoInputCheck(userStringInput)){
			        	  userStringInput = scanner.nextLine();
			        	  
			          }
		        	  
		          }
		          
		          //Done with updates, let's show the categories
		          System.out.print("Alright, here are the available book cateogries:");
		          
		          while(!fetch_categories()){
		        	  System.out.println("We are sorry about this."+
		        			  "This session is now terminated. Please rerun the"+
		        			  "program and try again");
		        	  System.exit(0);
		        	  
		          };
		          
		          System.out.println("Please enter the number that corresponds to"+
		        		  "the category of your interest");
		          userIntInput = scanner.nextLine();
		          while(!intInputCheck(userIntInput)){
		        	  userIntInput = scanner.nextLine();
		          };
		          
		          categoryChosen = Integer.parseInt(userIntInput);
		          
		          //If customer enters wrong input
		          while(!categories.containsKey(new Integer(categoryChosen))){
		        	  System.out.println("You have entered an invalid selection"+
		        			  "Please enter a number corresponding to your"+
		        			  "category of interest");
		        	  userIntInput = scanner.nextLine();
			          while(!intInputCheck(userIntInput)){
			        	  userIntInput = scanner.nextLine();
			          };
			          
			          categoryChosen = Integer.parseInt(userIntInput);
			          
		        	  
		          }
		          
		          //We now display all the books in the category 
		          //for smooth program flow. The books are also based on customer's
		          //club
		          
		          System.out.println("Here are the books we have in selected Category:");
		          
		          //Fetching the book titles
		          while(!fetch_titles(categoryChosen)){
		        	  System.out.println("We are sorry about this."+
		        			  "This session is now terminated. Please rerun the"+
		        			  "program and try again");
		        	  System.exit(0);
		        	  
		          };
		          
		          //Ask customer for the title they are interested in
		          System.out.println("Please enter the number that corresponds to"+
		        		  "the title of your interest");
		          
		          //****************** IMPLEMENT THIS FEATURE!
		          System.out.println("If you made a mistake, or you don't"+
				          "see the title of your interest, press 0 to go back to"
				          		+ "the list of Categories");
		          userIntInput = scanner.nextLine();
		          while(!intInputCheck(userIntInput)){
		        	  userIntInput = scanner.nextLine();
		          };
		          
		          titleNumberChosen = Integer.parseInt(userIntInput);
		          
		          //Show the customer the books with selected title
		          System.out.println("Here are the books we have of the selected title:");
		          
		          while(!find_book(titleNumberChosen,categoryChosen)){
		        	  System.out.println("We are sorry about this."+
		        			  "This session is now terminated. Please rerun the"+
		        			  "program and try again");
		        	  System.exit(0);
		        	  
		          }
		         
		          //Take in cusotmer's input
		          userIntInput = scanner.nextLine();
		          
		          while(!intInputCheck(userIntInput)){
		        	  userIntInput = scanner.nextLine();
		          }
		          
		          bookNumberChosen = Integer.parseInt(userIntInput);
		          
		          while (!bookInformation.containsKey(new Integer(bookNumberChosen))){
		        	  System.out.println("You have entered an invalid selection."+
		        			  "Please enter a number corresponding to your"+
		        			  "book of interest");
		        	  userIntInput = scanner.nextLine();
			          while(!intInputCheck(userIntInput)){
			        	  userIntInput = scanner.nextLine();
			          };
			          
			          bookNumberChosen = Integer.parseInt(userIntInput);
			            
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
	
	private boolean yesNoInputCheck(String s){
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
	
	private boolean stringInputCheck(String s){
		boolean correctInput = false;
		try{
		String temporary = s.toLowerCase();
		correctInput = true;
		}
		catch(NullPointerException e){
			System.out.println("We are sorry, but you have reached the End of File."+
		"The program will end now. Please relaunch it to try again.");
			System.exit(0);
		}
		return correctInput;
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
            "SELECT *       "
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
		
		boolean updateDB = false;
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
        	 
        	 while(!updateCustomerName(id,userStringInput)){
        		 System.out.print("Something went wrong. Please enter your name again");
        	 userStringInput = scanner.nextLine();
        	 	}
        	//updateDB = true;
        	 System.out.print("Would you like to update anything else?");
        	 return;
        	 
        	 
        	 
         }
         
         if (userChoice == 2){
        	 System.out.print("Please enter your new City:");
        	 userStringInput = scanner.nextLine();
	          
	          while(!stringInputCheck(userStringInput)){
	        	  userStringInput = scanner.nextLine();
	        	  
	          }
        	 
        	 while(!updateCustomerCity(id,userStringInput)){
        		 System.out.print("Something went wrong. Please enter your name again");
        	 userStringInput = scanner.nextLine();
        	 	}
        	//updateDB = true;
        	 System.out.print("Would you like to update anything else?");
        	 return;
        	 }
         if (userChoice == 3){
        	 System.out.print("Alright, nothing to update." +
         "Would you like to still update something? Enter yes or y to do so. ");
        	 return;
         }
         
         System.out.println("Something went wrong. Would you like to try again?");
         return;
          
         
	}
	
	private boolean updateCustomerName(int id, String name){
		//Now finally execute the query 
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        ResultSet         answers   = null;   // A cursor.

        boolean           updateDB = false; 
        boolean inDB      = false;  // Return.

        queryText =
            "UPDATE yrb_customer SET name = ? WHERE cid = ?";

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
        // Any answer?
        /*try {
            if (answers.next()) {
                inDB = true;
                System.out.print("Here is what information we have now about you:");
                custID = answers.getInt("cid");
                custName = answers.getString("name");
                custCity = answers.getString("city");
                System.out.println("Customer ID: " + custID 
                		+ "       Customer Name: " + custName 
                		+ "        City: " + custCity);
                
            } else {
                inDB = false;
                updateDB = false;
                custName = null;
            }
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }
        */
        
        //Show the updated info
        while(!find_customer(id)){
        	System.out.println("Something went wrong in fetching your updated info."+
        			"Please try again");
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
	
	private boolean updateCustomerCity(int id, String city){
		//Now finally execute the query 
        String            queryText = "";     // The SQL text.
        PreparedStatement querySt   = null;   // The query handle.
        ResultSet         answers   = null;   // A cursor.

        boolean           updateDB = false; 
        boolean inDB      = false;  // Return.

        queryText =
            "UPDATE yrb_customer SET city = ? WHERE cid = ?";

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
            querySt.setString(1, city);
            querySt.setInt(2, id);
            querySt.executeQuery();
            System.out.print("Update Successful!");
            updateDB = true;
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        // Any answer?
       /* try {
            if (answers.next()) {
                inDB = true;
                System.out.print("Here is what information we have now about you:");
                custID = answers.getInt("cid");
                custName = answers.getString("name");
                custCity = answers.getString("city");
                System.out.println("Customer ID: " + custID 
                		+ "       Customer Name: " + custName 
                		+ "        City: " + custCity);
                
            } else {
                inDB = false;
                updateDB = false;
                custName = null;
            }
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }*/
        
        //Show the updated info
        while(!find_customer(id)){
        	System.out.println("Something went wrong in fetching your updated info."+
        			"Please try again");
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
	
	
	public boolean fetch_categories(){
		String queryText = ""; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        boolean inDB = false;
        
        //ArrayList < String > str = new ArrayList < String > ();
        queryText = "SELECT *" + 
        			"FROM yrb_category ";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("SQL#2 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            //querySt.setInt(1, Integer.parseInt(ID));
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL#2 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?
        try {
            for (int i = 1; answers.next(); i++) {
                String category = answers.getString("cat");
                categories.put(i, category);
            }
        } catch (SQLException e) {
            System.out.println("SQL#2 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("SQL#2 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("SQL#2 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        //Now let's print the categories
        try{
        for (int i = 1; i<categories.size();i++){
        	System.out.println(i + " - " + categories.get(i));
        	inDB = true;
        	
        } 
        }catch(NullPointerException e){
        	System.out.println("Something went wrong with fetching" + 
        			"the available book cateogries");
        	
        }
        
        
        return inDB;
        }
	
	
	public boolean fetch_titles(int categoryNumber) {
        String queryText = ""; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        boolean inDB = false;
        
        //ArrayList < String > str = new ArrayList < String > ();
        queryText = "SELECT DISTINCT title " + 
        			"FROM yrb_book" +
        			"WHERE cat = ? and title IN "+
        					"(SELECT o.title"+
        					"FROM yrb_offer o " + 
        					"WHERE o.club in " + 
        						"(SELECT club " + 
        						"FROM yrb_member " +
        						"WHERE cid = ?)) and year IN " + 
        										"(SELECT o.year " + 
        										"FROM yrb_offer o " +
        										"WHERE o.club IN"+
        												"SELECT club " + 
        												"FROM yrb_member " + 
        												"WHERE cid = ?))";

        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("SQL#3 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
        	//temporary variable
        	String categoryName = categories.get(categoryNumber);
        	
        	//Now run the query
            querySt.setString(1, categoryName);
            querySt.setInt(2, custID);
            querySt.setInt(3, custID);
            answers = querySt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL#3 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Any answer?
        try {
            for (int i = 1; answers.next(); i++) {
                String bookTitle = answers.getString("title");
                //str.add(titles);
                bookTitles.put(i, bookTitle);
            }
        } catch (SQLException e) {
            System.out.println("SQL#3 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("SQL#3 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("SQL#3 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
        
        //Now let's try printing the book titles
        try{
            for (int i = 1; i<bookTitles.size();i++){
            	System.out.println(i + " - " + bookTitles.get(i));
            	inDB = true;
            	
            } 
            }catch(NullPointerException e){
            	System.out.println("Something went wrong with fetching" + 
            			"the available book titles");
            	
            }
            
        
        

        return inDB;
    }
	
	
	
	
	
	
	
	
    
	//Based on selected category and book numbers, we now find book information 	
	public boolean find_book(int titleNumber, int categoryNumber) {
        String queryText = ""; // The SQL text.
        PreparedStatement querySt = null; // The query handle.
        ResultSet answers = null; // A cursor.
        boolean inDB = false;
        String titleChosen = bookTitles.get(new Integer(titleNumber));
        String categoryName = categories.get(new Integer(categoryNumber)); 
        
        // ArrayList<String> books=new ArrayList<String>();
        queryText = "SELECT * " + 
        			"FROM yrb_book " + 
        			"WHERE title = ? and cat = ?";
        
		
    
        // Prepare the query.
        try {
            querySt = conDB.prepareStatement(queryText);
        } catch (SQLException e) {
            System.out.println("SQL#4 failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Execute the query.
        try {
            querySt.setString(1, titleChosen);
            querySt.setString(2, categoryName);
            //querySt.setInt(1, Integer.parseInt(ID));
            answers = querySt.executeQuery();
        } catch (SQLException e) {

            System.out.println("SQL#4 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        
        // Any answer?
        try {
        	String title;
            Integer weight;
            String language;
            Integer year;
            for (int i = 1; answers.next(); i++) {
            	title = answers.getString("title");
                year = answers.getInt("year");
                language = answers.getString("language");
                weight = answers.getInt("weight");
                bookInformation.put(i, new ArrayList < String > (Arrays.asList(title, 
                		Integer.toString(year), language, Integer.toString(weight))));
                //System.out.println("Title: "+titles+"\tYear: "+years +"\tLanguage: "+languages+
                //"\tWeight: "+weights);	   

            }

        } catch (SQLException e) {
            System.out.println("SQL#4 failed in cursor.");
            System.out.println(e.toString());
            System.exit(0);
        }

        
        
        
      //Now let's try printing the book information
        try{
        	String title;
            Integer weight;
            String language;
            Integer year;
            System.out.println("Available Books:");
            for (int i = 1; i<bookTitles.size();i++){
            	title = bookInformation.get(i).get(0);
            	year = Integer.parseInt(bookInformation.get(i).get(1));
                language = bookInformation.get(i).get(2);
                weight = Integer.parseInt((bookInformation.get(i).get(3)));
                
            	System.out.println(i + " - " + title +", " + year +", " +
            	language + "," + weight + ";");
            	inDB = true;
            	
            } 
            }catch(NullPointerException e){
            	System.out.println("Something went wrong with fetching" + 
            			"the available book titles");
            	
            }
        // Close the cursor.
        try {
            answers.close();
        } catch (SQLException e) {
            System.out.print("SQL#4 failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // We're done with the handle.
        try {
            querySt.close();
        } catch (SQLException e) {
            System.out.print("SQL#4 failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
        return inDB;
    }
	
	
	
	
	
	
	
	
	
	
	
}

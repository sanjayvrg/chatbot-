import java.util.Scanner;
/**
 * A program to carry on conversations with a human user.
 * This version:
 *<ul><li>
 * 		Uses advanced search for keywords 
 *</li><li>
 * 		Will transform statements as well as react to keywords
 *</li></ul>
 * @author Laurie White
 * @version April 2012
 *
 */
public class Magpie4
{
  private Boolean greeting;
  int currState;
  public Magpie4(){
   	greeting=true; // initial state
   	currState=0;
}
 
	/**
	 * Get a default greeting 	
	 * @return a greeting
	 */	
	public String getGreeting()
	{
		if (greeting = true) 
    {
			return "Hi, I'm cook-bot. Ask me for recipes or cooking advice!";
		} 
    else {
			return "";
		}
	}

	/**
	 * Gives a response to a user statement
	 * 
	 * @param statement
	 *            the user statement
	 * @return a response based on the rules given
	 */

  //setState methods prints to user based on state variables
  private void setState(int nextState){
    switch (nextState) {
      case 1:
        System.out.println("Hi, I'm cook-bot. Ask me for recipes or cooking advice!");
        break;
      case 2:
        System.out.println("Say something, please.");
        break;
      case 3:
        System.out.println("I hope you enjoy eating that selection.");
        break;
      case 4:
        System.out.println("What foods do your family enjoy?");
        break;
      case 5:
        System.out.println(getRoboResponse());
        break;
      case 6:
        System.out.println(getCommonFoodResponse() + ".");
        break;
      case 7:
        System.out.println("I would recommend " + getFoodResponse());
        break;
      case 8:
        System.out.println(getGreetingResponse());
        break;
    }
  }

  //ParseInput modified to return ints to be used in setState method in most cases
	public String parseInput(String statement)
	{

		String response = "";
		if (statement.length() == 0)
		{
      setState(2);
		}

		else if (findKeyword(statement, "no") >= 0)
		{
      setState(3);
		}
		else if (findKeyword(statement, "mother") >= 0
				|| findKeyword(statement, "father") >= 0
				|| findKeyword(statement, "sister") >= 0
				|| findKeyword(statement, "brother") >= 0)
		{
      setState(4);
		}
		else if (findKeyword(statement, "robot") >= 0|| findKeyword(statement, "chatbot") >= 0|| findKeyword(statement, "AI") >= 0|| findKeyword(statement, "you") >= 0)
		{
      setState(5);
		}
		else if (findKeyword(statement, "salad") >= 0|| findKeyword(statement, "chicken") >= 0|| findKeyword(statement, "cheese") >= 0|| findKeyword(statement, "rice") >= 0|| findKeyword(statement, "tea") >= 0|| findKeyword(statement, "coffee") >= 0|| findKeyword(statement, "milk") >= 0|| findKeyword(statement, "eggs") >= 0)
		{
      setState(6);
		}
		else if (findKeyword(statement, "recipe") >= 0|| findKeyword(statement, "cook") >= 0|| findKeyword(statement, "breakfast") >= 0|| findKeyword(statement, "dinner") >= 0|| findKeyword(statement, "lunch") >= 0 || findKeyword(statement, "food") >= 0 || findKeyword(statement, "yes") >= 0)
		{
			setState(7);
    }
		else if (findKeyword(statement, "hi") >= 0|| findKeyword(statement, "hello") >= 0|| findKeyword(statement, "hey") >= 0|| findKeyword(statement, "greetings") >= 0)
		{
			setState(8);
		}
		else if (findKeyword(statement, "I want to", 0) >= 0){
			response = transformIWantToStatement(statement);
		}
		else if ((findKeyword(statement, "Give me cooking advice", 0) >= 0) || (findKeyword(statement, "Give me cooking tips", 0) >= 0))
		{
			System.out.println("Are you a vegetarian?");
			Scanner in = new Scanner (System.in);
			String statement1 = in.nextLine();
			if ((findKeyword(statement1, "yes" , 0) >=0)){
				System.out.println("Make sure to take supplements or use lots of protein rich vegetables for a healthy diet. A good recipe: " + getVegetarianResponse());
			}
			else if ((findKeyword(statement1, "no" , 0) >=0)){
				System.out.println("Try to eat more white meats and avoid fatty processed food.");
			}
		}
		else
		{
			// Look for a two word (you <something> me)
			// pattern
			int psn = findKeyword(statement, "you", 0);

			if (psn >= 0
					&& findKeyword(statement, "me", psn) >= 0)
			{
				response = transformYouMeStatement(statement);
			}
			else
			{
				response = getRandomResponse();
			}
		}
		return response;
	}
	
	/**
	 * Take a statement with "I want to <something>." and transform it into 
	 * "What would it mean to <something>?"
	 * @param statement the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String transformIWantToStatement(String statement)
	{
		//  Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement
				.length() - 1);
		if (lastChar.equals("."))
		{
			statement = statement.substring(0, statement
					.length() - 1);
		}
		int psn = findKeyword (statement, "I want to", 0);
		String restOfStatement = statement.substring(psn + 9).trim();
		return "I can help you with "+ restOfStatement + "ing or perhaps cooking if you want?";
	}

	
	
	/**
	 * Take a statement with "you <something> me" and transform it into 
	 * "What makes you think that I <something> you?"
	 * @param statement the user statement, assumed to contain "you" followed by "me"
	 * @return the transformed statement
	 */
	private String transformYouMeStatement(String statement)
	{
		//  Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement
				.length() - 1);
		if (lastChar.equals("."))
		{
			statement = statement.substring(0, statement
					.length() - 1);
		}
		
		int psnOfYou = findKeyword (statement, "you", 0);
		int psnOfMe = findKeyword (statement, "me", psnOfYou + 3);
		
		String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe).trim();
		return "What do you mean by me " + restOfStatement + "ing you?";
	}
	

	
	/**
	 * Search for one word in phrase.  The search is not case sensitive.
	 * This method will check that the given goal is not a substring of a longer string
	 * (so, for example, "I know" does not contain "no").  
	 * @param statement the string to search
	 * @param goal the string to search for
	 * @param startPos the character of the string to begin the search at
	 * @return the index of the first occurrence of goal in statement or -1 if it's not found
	 */
	private int findKeyword(String statement, String goal, int startPos)
	{
		String phrase = statement.trim();
		//  The only change to incorporate the startPos is in the line below
		int psn = phrase.toLowerCase().indexOf(goal.toLowerCase(), startPos);
		
		//  Refinement--make sure the goal isn't part of a word 
		while (psn >= 0) 
		{
			//  Find the string of length 1 before and after the word
			String before = " ", after = " "; 
			if (psn > 0)
			{
				before = phrase.substring (psn - 1, psn).toLowerCase();
			}
			if (psn + goal.length() < phrase.length())
			{
				after = phrase.substring(psn + goal.length(), psn + goal.length() + 1).toLowerCase();
			}
			
			//  If before and after aren't letters, we've found the word
			if (((before.compareTo ("a") < 0 ) || (before.compareTo("z") > 0))  //  before is not a letter
					&& ((after.compareTo ("a") < 0 ) || (after.compareTo("z") > 0)))
			{
				return psn;
			}
			
			//  The last position didn't work, so let's find the next, if there is one.
			psn = phrase.indexOf(goal.toLowerCase(), psn + 1);
			
		}
		
		return -1;
	}
	
	/**
	 * Search for one word in phrase.  The search is not case sensitive.
	 * This method will check that the given goal is not a substring of a longer string
	 * (so, for example, "I know" does not contain "no").  The search begins at the beginning of the string.  
	 * @param statement the string to search
	 * @param goal the string to search for
	 * @return the index of the first occurrence of goal in statement or -1 if it's not found
	 */
	private int findKeyword(String statement, String goal)
	{
		return findKeyword (statement, goal, 0);
	}
	


	/**
	 * Pick a default response to use if nothing else fits.
	 * @return a non-committal string
	 */
	private String getFoodResponse()
	{
		final int NUMBER_OF_RESPONSES = 4;
		double r = Math.random();
		int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
		String response = "";
		
		if (whichResponse == 0)
		{
			response = "scrambled eggs and toast. Would you like a different food?";
		}
		else if (whichResponse == 1)
		{
			response = "a sightly sauteed chicken breast. Would you like a different food?";
		}
		else if (whichResponse == 2)
		{
			response = "a creamy angel-hair carbonara. Would you like a different food?";
		}
		else if (whichResponse == 3)
		{
			response = "steak and mushrooms. Would you like a different food?";
		}

		return response;
	}
  	private String getVegetarianResponse()
	{
		final int NUMBER_OF_RESPONSES = 3;
		double r = Math.random();
		int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
		String response = "";
		if (whichResponse == 0)
		{
			response = "I would reccomend a squash pasta.";
		}
		else if (whichResponse == 1)
		{
			response = "I would reccomend tofu cooked with tomatoes";
		}
		else if (whichResponse == 2)
		{
			response = "A pesto based pasta.";
		}
		return response;
	}
	private String getRandomResponse()
	{
		final int NUMBER_OF_RESPONSES = 5;
		double r = Math.random();
		int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
		String response = "";
		
		if (whichResponse == 0)
		{
			response = "Ask me for some recipes!";
		}
		else if (whichResponse == 1)
		{
			response = "My favorite food is instant ramen";
		}
		else if (whichResponse == 2)
		{
			response = "There are 95 calories in an apple.";
		}
		else if (whichResponse == 3)
		{
			response = "They call me cook-bot, but my name is really Benedict.";
		}
		else if (whichResponse == 4)
		{
			response = "What else would you like to talk about?";
		}
		return response;
	}
private String getCommonFoodResponse()
	{
		final int NUMBER_OF_RESPONSES = 3;
		double r = Math.random();
		int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
		String response = "";
		
		if (whichResponse == 0)
		{
			response = "I love that food";
		}
		else if (whichResponse == 1)
		{
			response = "I eat a lot of that food";
		}
		else if (whichResponse == 2)
		{
			response = "My favorite recipes have that food";
		}
		return response;
	}
  private String getGreetingResponse()
  {
    final int NUMBER_OF_RESPONSES = 3;
    double r = Math.random();
    int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
    String response = "";
    if (whichResponse == 0)
    {
      response = "Hello. What is your favorite food?";
    }
    else if (whichResponse == 1)
    {
      response = "Hi. I'm cookbot, nice to meet you.";
    }
    else if (whichResponse == 2)
    {
      response = "I am cookbot. I love cooking.";
    }
    return response;
  }
	private String getRoboResponse()
	{
		final int NUMBER_OF_RESPONSES = 3;
		double r = Math.random();
		int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
		String response = "";
		
		if (whichResponse == 0)
		{
			response = "I am a chatbot that focuses around food.";
		}
		else if (whichResponse == 1)
		{
			response = "I use keywords to tailor responses to your food questions.";
		}
		else if (whichResponse == 2)
		{
			response = "Cookbots are a declining species of chatbots.";
		}
		return response;
	}
}

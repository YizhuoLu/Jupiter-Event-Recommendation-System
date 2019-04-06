# Jupiter-Event-Recommendation-System

1. Overview:
  This a customized event recommendation system, I use Tomcat Apache as the server and make a web page to support different kinds of operations like user log in/log out, surrounding events display, set/unset favorite events, customized relative events recommendation. 
  Technique: Java servlet, HTML/CSS/JavaScript, MySQL, RESTful API, TicketMasterAPI.
  
2. Design:
  A: Logic tier:
  I write three primary servlets to handle primary logic, they are ItemHistory, RecommendItem, SearchItem.
  a. SearchItem:
     In order to show customers nearby events, I first send HTTP request to TicketMaster web server using TicketMaster API. (I user JSON as the format through the whole project). Based on the curstomer's geo-location, I can get a list of events and their information from TicketMaster. Each time when I get the relative list of events, I will automatically store them into my database.
   b. ItemHistory: 
      Each time when customer set an event as favorite (by cliking heart icon on the web page), I will send (doGet()) to the server a JSONObject in which I set the favorite to be true. And in the doPost() I will add the received event into history table.
   c. Recommenditem:
      Here I handle recommeded events need of customers.
     
  B: Data tier:
    I make four tables for this project, one is items (just event, item_id is the primary key), Users (user_id is the primary key), Categories(item_id is the primary key and foreign key), history(user_id & item_id are primary keys and foreign keys).
  
  C: Presentation tier:
    I write an index.html to act as the main web page. (main.css, main.js accompany)
    
  3.  Recommendation Algorithm:
    I use a map<String, Integer> to load a specific customer's favorite items' category and corresponding appearing times. Then I will sort event categories by their count. Then I search events given the location (latitude, longitude) and sorted events' category. In the process, I will sort the events by their distance to the customer's location. Eventually, I will show the double sorted (by favorite category and distance) results to the customer.

// Agent nluAgent in project Web40SOJason

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

@in_msg
+user_msg(Msg, Query) : true
	<-  sendNLU(Query, Msg);
		-user_msg(Msg, Query). // clear the memory


/* Tell the user agent what the NLU system understood */	
+price(Terms, Price)[query(Query), domain(travel)] : true
	<- .send(userAgent, tell, price(Terms, Price)[query(Query)], domain(travel));
	   .print("Percibido: price ",Terms, " ", Price ).
	
+date(Terms, Day, Month, Year)[query(Query), domain(travel)] : true 
    <- .send(userAgent, tell, date(Terms, Day, Month, Year)[query(Query), domain(travel)]);
       .print("Percibido: date ",Terms, " ", Day, " ", Month, " ", Year).
    
+time(Terms, Hours, Minutes)[query(Query), domain(travel)] : true 
    <- .send(userAgent, tell, time(Terms, Hours, Minutes)[query(Query), domain(travel)]);
       .print("Percibido: time ",Terms, " ", Hours, " ", Minutes).
    
+location(Terms, Place)[query(Query), domain(travel)] : true 
    <- .send(userAgent, tell, location(Terms, Place)[query(Query), domain(travel)]);
       .print("Percibido: location ",Terms, " ", Place).
    
+type(Terms)[query(Query), domain(travel)] : true 
    <- .send(userAgent, tell, type(Terms)[query(Query), domain(travel)]);
       .print("Percibido: type ",Terms).

@sendFindTravel
+done[query(Query), domain(Domain)] : true 
	<- .wait(1000); // wait until all other information is sent
	   .print("Percepcion completada"); 
	   .send(userAgent, achieve, find(Domain, Query)).
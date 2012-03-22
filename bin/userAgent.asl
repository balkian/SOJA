// Agent userAgent in project Web40

/* Initial beliefs and rules */
new_query(Query) :- .random(R) & Query = (1000*R)+1.

!start.

/* Initial goals */

/******* Plans ***************************/

/* Wait for service introduction (temporal plan, to erase) */
+!start : true 
	<-	.wait(1000);
		+user_msg("I want to travel from Madrid to Cuenca in the morning that costs no more than 200€ and dinner in a romantic restaurant").


/* Ask the nlu agent */	
+user_msg(Msg) : new_query(Query)
	<-  .send(nluAgent, tell, user_msg(Msg, Query) ).

/* Log the received data */
+price(Terms, Price)[query(Query), domain(Domain)] : true
	<- .print("Percibido: price ",Terms, " ", Price );
		+data(price(Terms), Query, Domain).
	
+date(Terms, Day, Month, Year)[query(Query), domain(Domain)] : true 
    <- .print("Percibido: date ",Terms, " ", Day, " ", Month, " ", Year);
    	+data(date(Terms, Day, Month, Year), Query, Domain).
    
+time(Terms, Hours, Minutes)[query(Query), domain(Domain)] : true 
    <- .print("Percibido: time ",Terms, " ", Hours, " ", Minutes);
       +data(time(Terms, Hours, Minutes), Query, Domain).
    
+location(Terms, Place)[query(Query), domain(Domain)] : true 
    <- .print("Percibido: location ",Terms, " ", Place);
       +data(location(Terms, Place), Query, Domain).
    
+type(Terms)[query(Query), domain(Domain)] : true 
    <- .print("Percibido: type ",Terms).
    
/* find travel */
/*@find_travel
+!find(travel, Query) : true
	<-  .println("lets find travel ", Query);
		.findall(Name, service(Name, travel), List);
		.send(List, achieve, find(travel, Query)).
*/

@do_search	
+!find(Domain, Query) : true
	<-  .print("Perform find ", Domain, " ", Query);
		.findall(Name, service(Name, Domain), AgList);
		.findall(Atom[query(Query)], data(Atom, Query, Domain), DataList);
		.send(AgList, tell, DataList);
		.send(AgList, achieve, find(Domain, Query)).		
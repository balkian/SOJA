// Agent travelAgent in project Web40

/* Initial beliefs and rules */
canFindTravel(Query) 
	:- location(from,_)[query(Query)] & 
	   location(to,_)[query(Query)] & 
	   date(departure,_,_,_)[query(Query)].
	   
/* Initial goals */
contact(userAgent).
my_service(travel).
my_service(train).

fare(turista).

/************** Plans *****************/

/* Introduce myself to the user agent */
@introduce_myself
+my_service(Domain) 
	: contact(Agent) & .my_name(Me)
	<- .send(Agent, tell, service(Me, Domain)).

@introduction_rety
+my_service(Domain) : not contact(Agent)
	<- -+my_service(Domain).


/* Find travel plans */
@findTravel1    
+!find(travel, Query) : not canFindTravel(Query) & not delay(Query) 
	<-	.print("Not enought data. Lets wait some time");
		.wait(3000);
		+delay(Query);
		!find(travel, Query).
		
@findTravel2 
+!find(travel, Query) : not canFindTravel(Query) & delay(Query)
	<-	-delay(Query);
		.print("Not enought data. TODO:Lets ask!").

@findTravel3
+!find(travel, Query) : canFindTravel(Query)
	<- 	?location(to, To);
		?location(from, From);
		?date(departure, Day, Month, Year);
		findTravel(Query,From, To, Day, Month, Year);
		.print("ok").

@findTravelFailureRety
-!find(travel, Query) : not error(Msg, Query)<- !findTravel(Query).

@findTravelFailureError	 
-!find(travel, Query) : error(Msg, Query) 
	<-  .print("Problema al encontrar viajes:", Msg);
		!findTravel(Query).    

  
+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query), source(percept)] 
	:	fare(FName)
	<-	+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query)]. //note it down
	
+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query), source(percept)]
	:	~fare(FName)
	<-	true. // skip this
	
+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query), source(percept)]
	: not fare(_) & not ~fare(FName)	// no restrictions, so note them all down
	<-	+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query)].


/* log results */
@log_the_journey
+journey(From, To, Departure, Arrival, fare(FName, FPrice))[query(Query), source(percept)] 
	: 	true 
	<- 	.print("Discard travel : From ", From,"<", Departure, "> to ", To, "<", Arrival, "> for ", Fares).
		
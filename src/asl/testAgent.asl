// Agent testAgent in project Web40 - For socket.io

!start.
/* Wait for service introduction (temporal plan, to erase) */
+!start : true 
	<-	+have(started);
		.print("Hola");
		.wait(1000);
		sendSocket("test","hola").
		
+success("yes"): true
	<- .print("Succes!!!!!").

==============================
TRACTOR SIMULATION PROGRAM
==============================

1. ERLANG INSTALLATION

- run the "otp_win64_21.0.1" application to install erlang. The default installation directory should be "C:/Program Files/erl10.0.1".
- after installation, create a shortcut (or pin to taskbar) to the "werl" application in the  "~/erl10.0.1/bin" directory.
- when you click the shortcut, the erlang shell application should open.

2. .ERLANG FILE

- open the provided ".erlang" file in a text editor (like notepad).
- in line 10, replace the "..." with the path to where your "ebin" folder is located.
- save and close the changed ".erlang" file.
- copy the changed ".erlang" file to "C:/Program Files/erl10.0.1/usr".

3. RUNNING THE PROGRAM

- open the erlang shell.
- type "erlangrc(["."])." and hit enter. A printout of ERLANG should appear - the program code is now loaded.
-  start the program with "startup:start(2,3)." and hit enter. The first argument specifies the number of tractors to be started - this can be 1 or more. The second argument specifies the number of farms with position sensors - use 3 for this.
- the simulation program should now be running, with TCP servers to request information from: 
		The servers for the fuel sensors run on ports 9001, 9002, ..., 900N - e.g. the fuel sensor on tractor 1 is accessed via port 9001. "N" is the number of tractors launched.
		The servers for the position sensors run on ports 9101, 9102 and 9103 - e.g. the position sensor for farm 1 is accessed via port 9101.
		
- the program can handle the following messages over TCP:
		The fuel sensor data can be obtained by sending a string "request" to the corresponding port.
		The position data for each position on each farm can be obtained  by sending a string "farmXpYZ" (X represents the farm number - 1,2 or 3; Y represents the position sensor row - 1 or 2; Z represents the position sensor column - 1, 2 or 3) to the corresponding port.

4. TESTING THE PROGRAM

- run the provided "Client_sim.java" program (you need to run this from a main or otherwise).
- the test program will allow you to obtain sensor data by specifying inputs through the console. Example inputs:
		To obtain the fuel consumption reading from the fuel sensor on tractor 1:
		Port:
		9001 *hit enter*
		Msg:
		request *hit enter*
		
		To obtain the fuel consumption reading from the position sensor on farm 1:
		Port:
		9101 *hit enter*
		Msg:
		farm1_p11 *hit enter*
		
5. ADDING RESOURCES TO THE SIMULATION WHILE RUNNING

Functions are available for adding tractors or one additional farm (farm 4) to the simulation, which can called from the erlang shell as follows:

		startup:start_tractor(add, <TractorID>).
				--> the function adds one tractor with the ID given in <TractorID>. The function can be a called numerous times to add tractors - 
				however, the <TractorID> must be unique in the simulation (i.e. cannot be duplicated).
				The fuel sensor of the tractor can be accessed over TCP in the same way as explained in (3), i.e. by sending a message to port 900<TractorID> (e.g. 9005).
						Example of use: > startup:start_tractor(add, 5).
						
		startup:start_farm4().
				--> the function adds farm 4 as an additional farm resource in the simulation.
				Only farm 4 can be added in this version of the simulation program.
				The farm can be accessed over TCP at port 9104.

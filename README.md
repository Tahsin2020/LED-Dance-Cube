## =UBC CPEN 391 - Team 41 ##

Team members:
- Josh Wong
- Tahsin Hasan
- April Ju
- Adin Mauer

Date Submitted: 20232-04-12

### Dance Cube

This project includes a 3D 8x8x8 LED cube, connnected and controlled via WIFI by an app. Additionally, besides lighting up cool moving animations in the LED cube, there is an function to use the camera on the phone with a local ML model that does pose eatimation to project the real-time moving shape of the person in the camera frame into the cube in 3D.

<br/><br/>
<p align="center">
  <img src="https://user-images.githubusercontent.com/89616796/231640521-6e43ef89-aa31-4e59-8e8f-6284d2ae8689.png" />
</p>



<p align="center">
  <img src="https://user-images.githubusercontent.com/89616796/231642571-2c826fe3-f7d6-44e4-b82c-6db165e1cf98.gif" />
</p>

### Components:

- UI - Android Application used to control the Cube
- ESP8266 used fo WIFI connection from the APP to the FPGA Cube driver
- Hardware (FPGA based) LED Cube Driver
- LED Cube Hardware (Custom PCB)

### Features : What can the app do?

- Turn all light on / off
- Loop through all 7 animations - 30 seconds per animation
- Choose specific animation to stay on
- Per account/user, can see information on how many times each specific animation was chosen / which animation is most popular
- Turn on phone camera, point to a person, and show the persons avatar and his/her movements in real-time

### How to use?
FPGA component
In order to use this project, you need the hardware, an FPGA, an ESP and an Android phone.
Assuming that the hardware is present and an Altera FPGA is present, the following steps can be taken to run this project:
- Create Quartus project names LED_Cube_Controller
- Add all files in the RTL directory to the project
- Open the Cube_controller.qsys file and generate rtl -> then add the file Cube_controller/synthesis/Cube_controller.qip to the project
- Compile and download design to FPGA
- Hook up all GPIO_0 to the GPIO extension board on the hardware (ribbon recommended)

ESP:
- Download the NodeMCU firmware onto the ESP8266
-  Download the .lua script in the lua directory (add your phone's hotspot credentials)

Android Application:
-    Build app in Android Studio and download to phone

Enjoy!

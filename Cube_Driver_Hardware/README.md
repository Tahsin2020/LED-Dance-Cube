# Hardware Component

## FPGA based LED Cube driver

For full design - go to <b>[LED_Cube_Controller](https://github.com/UBC-CPEN391/l2c-l2c-41/tree/main/Cube_Driver_Hardware/LED_cube_controller) directory</b>. All of the rest are partial designs, meant for testing, debuggin, or alternative usage cases.

### Directory Structure

- <b>[LED_Cube_Controller/RTL](https://github.com/UBC-CPEN391/l2c-l2c-41/tree/main/Cube_Driver_Hardware/LED_cube_controller/RTL)</b>: this directory has all the RTL (System Verilog) for the design, excluding the qsys verilog that which is under LED_Cube_Controller/CubeController/synthesis. <br/><b>Top Level Module: </b> LED_Cube_Controller/RTL/LED_Cube_Controller.sv . <br/>[Top Level Module](https://github.com/UBC-CPEN391/l2c-l2c-41/blob/main/Cube_Driver_Hardware/LED_cube_controller/RTL/LED_cube_controller.sv)
- <b>[LED_Cube_Controller/Animations](https://github.com/UBC-CPEN391/l2c-l2c-41/tree/main/Cube_Driver_Hardware/LED_cube_controller/Animations)</b>: This directory has all of the HEX files that are downloaded onto the FPGA memory inside the driver. The data in these files are what drive the animations. There are also python scripts that were used to generate the hex files from code, and gifs (that are used in the UI).
- <b>[LED_Cube_Controller/Solidworks](https://github.com/UBC-CPEN391/l2c-l2c-41/tree/main/Cube_Driver_Hardware/LED_cube_controller/Solidworks)</b>: Inckludes 3D print design for case.

<p align="center">
  <img src="https://user-images.githubusercontent.com/89616796/231647137-e89bcd71-861d-403c-b2c3-f1c576e6d489.png" />
</p>

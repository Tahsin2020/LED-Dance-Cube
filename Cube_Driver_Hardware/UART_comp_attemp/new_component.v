// new_component.v

// This file was auto-generated as a prototype implementation of a module
// created in component editor.  It ties off all outputs to ground and
// ignores all inputs.  It needs to be edited to make it do something
// useful.
// 
// This file will not be automatically regenerated.  You should check it in
// to your version control system if you want to keep it.

`timescale 1 ps / 1 ps
module new_component (
		output wire [7:0]  avm_m0_address,     // avm_m0.address
		output wire        avm_m0_read,        //       .read
		input  wire [15:0] avm_m0_readdata,    //       .readdata
		output wire        avm_m0_write,       //       .write
		output wire [15:0] avm_m0_writedata,   //       .writedata
		input  wire        avm_m0_waitrequest, //       .waitrequest
		input  wire        clock_clk,          //  clock.clk
		input  wire        reset_reset         //  reset.reset
	);

	// TODO: Auto-generated HDL template

	assign avm_m0_address = 8'b00000000;

	assign avm_m0_read = 1'b0;

	assign avm_m0_write = 1'b0;

	assign avm_m0_writedata = 16'b0000000000000000;

endmodule

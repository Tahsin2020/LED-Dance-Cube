module LED_all_on(
    // basic signals
	input logic clk,
	input logic rst_n,
	input logic all_on_start,
	input logic all_on_stop,
	
	// config signals
	input logic [3:0] mode,
	input logic [3:0] brightness,

	// outputs
	output logic [7:0] Layers,
	output logic [7:0] Latches,
	output logic [7:0] Data
);

endmodule : LED_all_on
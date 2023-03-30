module LED_stream(
    // basic signals
	input logic clk,
	input logic rst_n,
	input logic frame_start,
	input logic frame_stop,
	
	// config signals
	input logic [3:0] mode,
	input logic [3:0] brightness,

	// in_data
	input logic [7:0] data_in,

	// outputs
	output logic [7:0] Layers,
	output logic [7:0] Latches,
	output logic [7:0] Data
);

endmodule: LED_stream
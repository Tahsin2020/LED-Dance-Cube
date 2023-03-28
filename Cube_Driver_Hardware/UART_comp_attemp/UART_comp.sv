module UART_comp(
	input logic clk,
	input logic rst_n,
	input logic avalon_master_readdatavalid,
	input logic avalon_master_waitrequest,
	input logic [7:0] avalon_master_readdata,
	
	
	output logic [4:0] avalon_master_address,
	output logic avalon_master_read);

endmodule : UART_comp